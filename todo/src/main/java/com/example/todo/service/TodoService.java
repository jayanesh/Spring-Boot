package com.example.todo.service;

import java.util.List;

import com.example.todo.model.TodoModel;

public interface TodoService {
    public TodoModel creaTodoModel(TodoModel task);
    public List<TodoModel> getAllTasks();
    public TodoModel updateTask(Long id, TodoModel task);
    public String deleteTask(Long id);
    

}