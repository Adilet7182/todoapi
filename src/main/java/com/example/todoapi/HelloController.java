package com.example.todoapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/hello")
    public String hello(){
        return "Hello from Spring Boot!";
    }

    @GetMapping("/name")
    public String name(){
        return "my name is Adik";
    }

    @GetMapping("/info")
    public String info(){
        return "Name: Adik, Status: Learning Spring Boot";
    }

    @GetMapping("/time")
    public String time(){
        return LocalDateTime.now().toString();
    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    @GetMapping("/tasks/{id}")
    public Task getTaskById(@PathVariable Long id){

        return taskRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable Long id){
            if (taskRepository.existsById(id)){
                taskRepository.existsById(id);
                return "Task deleted";
            }
        return "Task not found";
    }
}
