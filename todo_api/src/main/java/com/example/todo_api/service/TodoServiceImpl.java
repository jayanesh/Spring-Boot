package com.example.todo_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todo_api.model.TodoModel;
import com.example.todo_api.repo.TodoRepository;

@Service 
public class TodoServiceImpl implements TodoService{
    private final TodoRepository todoRepo;
    public TodoServiceImpl(TodoRepository todoRepo) {
        this.todoRepo = todoRepo;
    }
    @Override
    public TodoModel createTodo(TodoModel task){
        return todoRepo.save(task);
    }
    @Override 
    public List<TodoModel> getAllTodo(){
        return todoRepo.findAll();
    }

    @Override
    public TodoModel updateTodo(Long id, TodoModel task) {
        TodoModel Todo = todoRepo.findById(id).orElse(null);
        if(Todo==null) return null;
        Todo.setTask(task.getTask());
        return todoRepo.save(Todo);
    }
    @Override
    public TodoModel getTodo(Long id) {
        TodoModel Todo = todoRepo.findById(id).orElse(null);
        if(Todo==null) return null;
        return Todo;
    }
    @Override
    public String deleteTodo(long id) {
        TodoModel Todo = todoRepo.findById(id).orElse(null);
        todoRepo.deleteById(id);
        if(Todo==null) return "Task not found!";
        else return "Task deleted sucessfully";
    }
    
    
}
