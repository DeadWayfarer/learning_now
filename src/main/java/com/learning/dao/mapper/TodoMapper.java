package com.learning.dao.mapper;

import com.learning.entity.Todo;
import com.learning.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TodoMapper implements RowMapper<Todo> {
    @Override
    public Todo mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        Todo todo = new Todo();

        todo.setTodoId(rs.getInt("todo_id"));
        todo.setName(rs.getString("name"));
        todo.setChecked(rs.getBoolean("checked"));
        todo.setDeleted(rs.getBoolean("deleted"));
        todo.setUserId(rs.getInt("user_id"));

        return todo;
    }
}
