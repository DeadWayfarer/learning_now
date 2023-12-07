package com.learning.dao;

import com.learning.entity.Todo;
import com.learning.entity.User;

import java.util.List;

public interface TodoDao {
    List<Todo> getTodosOfUser(User user);
    Todo getTodoById(Integer todoId);
    void saveOrUpdate(Todo todo);
    void delete(Integer todoId);
}
