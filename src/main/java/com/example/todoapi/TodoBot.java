package com.example.todoapi;

import com.example.todoapi.service.TaskService;
import com.example.todoapi.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TodoBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username}")
    private String botUsername;

    @Value("${admin.chat.id}")
    private Long adminChatId;

    @Override
    public String getBotToken(){
        return botToken;
    }

    @Override
    public String getBotUsername(){
        return botUsername;
    }

    public Long getAdminChatId() {
        return adminChatId;
    }

    private final UserService userService;
    private final TaskService taskService;

    public TodoBot(UserService userService, TaskService taskService){
        this.userService = userService;
        this.taskService = taskService;
    }

    @Override
    public void onUpdateReceived(Update update){
        // проверка текстовое ли это сообщение
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            Long telegramId = update.getMessage().getFrom().getId();
            String username = update.getMessage().getFrom().getUserName();
            User user = userService.getOrCreateUser(telegramId, username);

            if (messageText.equals("/start")){
                sendMessage(chatId, "\uD83D\uDC4B Привет! Я TodoList Helper!\n" +
                        "Доступные команды:\n" +
                        "/start - Начало работы\n" +
                        "/add (текст задачи) - Добавить задачу\n" +
                        "/list - Показать все задачи\n" +
                        "/done (номер задачи) - Отметить задачу выполненной\n" +
                        "/delete (номер задачи) - Удалить задачу\n" +
                        "/review (текст отзывы) - отправить отзыв разработчику");
            }

            if (messageText.startsWith("/add")){
                if (messageText.length() > 5) {
                    String text = messageText.substring(5);
                    taskService.createTask(user.getId(), text);
                    sendMessage(chatId, "Задача Добавлена!");
                } else {
                    sendMessage(chatId, "А где сообственно текст? Напишите текст задачи сразу после команды /add");
                }
            }

            if (messageText.equals("/list")){
                var tasks = taskService.getTasksByUser(user.getId());
                StringBuilder sb = new StringBuilder();
                int index = 1;
                if (tasks != null && tasks.isEmpty()){
                    sendMessage(chatId, "Задач нет");
                } else {
                    for (Task task : tasks){
                        String status = task.isCompleted() ? "✅" : "⬜";
                        sb.append(index).append(". ").append(status).append(" ").append(task.getTitle()).append("\n");
                        index++;
                    }
                    sendMessage(chatId, sb.toString());
                }
            }

            if(messageText.startsWith("/done")){
                if (messageText.length() > 6){
                    String numberStr = messageText.substring(6);
                    int index = Integer.parseInt(numberStr) -1;
                    var tasks = taskService.getTasksByUser(user.getId());
                    Task task = tasks.get(index);
                    taskService.markDone(task.getId());
                    sendMessage(chatId, "Задача выполнена!");
                }
            }

            if (messageText.startsWith("/delete")){
                if (messageText.length() >8){
                    String numberStr = messageText.substring(8);
                    int index = Integer.parseInt(numberStr) -1;
                    var tasks = taskService.getTasksByUser(user.getId());
                    Task task = tasks.get(index);
                    taskService.deleteTask(task.getId());
                    sendMessage(chatId, "Задача удалена!");
                }
            }

            if(messageText.startsWith("/review")){
                if (messageText.length() > 8) {
                    String feedback = messageText.substring(8);
                    sendMessage(adminChatId, "Отзыв от @" + username + ":\n" + feedback);
                    sendMessage(chatId, "Спасибо за отзыв!");
                } else {
                    sendMessage(chatId, "Напишите текст отзыва после команды /review");
                }
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try{
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}