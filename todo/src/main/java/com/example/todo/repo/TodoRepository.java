package com.example.todo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo.model.TodoModel;

public interface TodoRepository extends JpaRepository<TodoModel, Long>{

}