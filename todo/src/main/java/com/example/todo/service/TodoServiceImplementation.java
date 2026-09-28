package com.example.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todo.model.TodoModel;
import com.example.todo.repo.TodoRepository;

@Service 
public class TodoServiceImplementation implements TodoService{
    private final TodoRepository todoRepository;

    public TodoServiceImplementation(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public TodoModel creaTodoModel(TodoModel task) {
        return todoRepository.save(task);
    }

    @Override
    public List<TodoModel> getAllTasks() {
        return todoRepository.findAll();
    }

    @Override
    public TodoModel updateTask(Long id, TodoModel task) {
        TodoModel fetchedTask = todoRepository.findById(id).orElse(null);
        if(fetchedTask==null) return null;
        fetchedTask.setTask(task.getTask());
        return todoRepository.save(fetchedTask);
    }

    @Override
    public String deleteTask(Long id) {
        TodoModel Task = todoRepository.findById(id).orElse(null);
        if(Task!=null){
            todoRepository.deleteById(id);
            return("Task deleted sucessfully!");
        }
        else return("Task not found!");
    }
    
    

    
}
