package com.example.todoapi.service;

import com.example.todoapi.Task;
import com.example.todoapi.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Long userId, String title){
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle(title);
        return taskRepository.save(task);
    }

    public List<Task> getTasksByUser(Long userId){
        return taskRepository.findByUserId(userId);
    }

//    public List<Task> markDone(Long taskId){
//        Task task = taskRepository.findById(taskId);
//        if (task.isCompleted()){
//            return Task;
//        }
//
//    }

//    public Task deleteTask(Long taskId){
//        Task task = new Task();
//        task.setUserId(taskId);
//        return taskRepository.delete(taskId);
//    }


}
