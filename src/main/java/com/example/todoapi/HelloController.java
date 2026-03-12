package com.example.todoapi;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    private List<Task> tasks = new ArrayList<>();
    private Long nextId = 1L;

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
        task.setId(nextId++);
        tasks.add(task);
        return task;
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks(){
        return tasks;
    }

    @GetMapping("/tasks/{id}")
    public Task getTaskById(@PathVariable Long id){

        for (Task task : tasks){
            if (task.getId().equals(id)){
                return task;
            }
        }
        return null;
    }

    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable Long id){
        boolean removed = tasks.removeIf(task -> task.getId().equals(id));
            if (removed){
                return "Task deleted";
            }
        return "Task not found";
    }
}
