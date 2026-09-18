package com.example.todo_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo_api.model.TodoModel;
import com.example.todo_api.service.TodoService;




@RestController 
@RequestMapping("/") 
public class TodoController {
    private final TodoService todoser;
    
    public TodoController(TodoService todoser){
        this.todoser = todoser;
    }
    @PostMapping("/")
    public TodoModel createTodo(@RequestBody TodoModel task) {
        return todoser.createTodo(task);
    }
    @GetMapping("/")
    public List<TodoModel> getAllTodo(){
        return todoser.getAllTodo();
    }
    @GetMapping("/{id}")
    public TodoModel getTodo(@PathVariable Long id){
        return todoser.getTodo(id);
    }
    @PutMapping("/{id}")
    public TodoModel updateTodo(@PathVariable Long id, @RequestBody TodoModel task) {        
        return todoser.updateTodo(id, task);
    }
    @GetMapping("/{id}")
    public String getMethodName(@PathVariable Long id) {
        return todoser.deleteTodo(id);
    }
    
}