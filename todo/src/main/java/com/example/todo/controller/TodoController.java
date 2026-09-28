package com.example.todo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.model.TodoModel;
import com.example.todo.service.TodoService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController 
@RequestMapping("/")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    @PostMapping("/")
    public TodoModel createTodoModel(@RequestBody TodoModel task) {        
        return todoService.creaTodoModel(task);
    }
    @GetMapping("/")
    public List<TodoModel> getAllTasks() {
        return todoService.getAllTasks();
    }
    @PutMapping("/{id}")
    public TodoModel updateTask(@PathVariable Long id, @RequestBody TodoModel task) {
        return todoService.updateTask(id, task);
    }
    
    
    
    

}
