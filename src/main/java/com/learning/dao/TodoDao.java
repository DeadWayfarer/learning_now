package com.learning.dao;

import com.learning.entity.Todo;
import com.learning.entity.User;

import java.util.List;

public interface TodoDao {
    Integer getTodoCountOfUser(User user, String nameFilter);
    List<Todo> getTodosOfUser(User user, int skip, int limit, String nameFilter);
    Todo getTodoById(Integer todoId);
    void saveOrUpdate(Todo todo);
    void delete(Integer todoId);
}
