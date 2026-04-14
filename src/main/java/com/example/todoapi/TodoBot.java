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

    @Override
    public String getBotToken(){
        return botToken;
    }

    @Override
    public String getBotUsername(){
        return botUsername;
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
                        "/add - Добавить задачу\n" +
                        "/list - Показать все задачи\n" +
                        "/done - Отметить задачу выполненной\n" +
                        "/delete - Удалить задачу");
            }

            if (messageText.startsWith("/add")){
                if (messageText.length() > 5) {
                    String text = messageText.substring(5);
                    taskService.createTask(user.getId(), text);
                    sendMessage(chatId, "Задача Добавлена!");
                } else {
                    sendMessage(chatId, "А где сообственно текст?");
                }
            }

            if (messageText.equals("/list")){
                var tasks = taskService.getTasksByUser(user.getId());
                StringBuilder sb = new StringBuilder();
                if (tasks != null && tasks.isEmpty()){
                    sendMessage(chatId, "Задач нет");
                } else {
                    for (Task task : tasks){
                        sb.append(task.getTitle()).append("\n");
                    }
                    sendMessage(chatId, sb.toString());
                }
            }

            if(messageText.startsWith("/done")){
                if (messageText.length() > 6){
                    String number = messageText.substring(6);
                    Long newNumber = Long.parseLong(number);
                    taskService.markDone(newNumber);
                    sendMessage(chatId, "Задача выполнена!");
                }
            }

            if (messageText.startsWith("/delete")){
                if (messageText.length() >8){
                    String number = messageText.substring(8);
                    Long newNumber = Long.parseLong(number);
                    taskService.deleteTask(newNumber);
                    sendMessage(chatId, "Задача удалена!");
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