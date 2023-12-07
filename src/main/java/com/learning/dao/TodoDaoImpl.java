package com.learning.dao;

import com.learning.dao.mapper.TodoMapper;
import com.learning.dao.mapper.UserMapper;
import com.learning.entity.Todo;
import com.learning.entity.User;
import com.learning.util.paginated.PaginatedListHelper;
import com.learning.util.paginated.SimplePaginatedList;
import com.learning.web.user.UsersForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository("todoDao")
public class TodoDaoImpl extends BaseDaoImpl implements TodoDao {

    private final String SQL_INSERT_TEMPLATE = "insert into todo (user_id, name, deleted, checked) values (%s,'%s',%s,%s)";
    private final String SQL_UPDATE_TEMPLATE = "update todo set user_id = %s, name = '%s', deleted = %s, checked = %s where todo_id = %s";

    public List<Todo> getTodosOfUser(User user) {
        int userId = user.getUserId();
        String sql = "select * from todo where deleted = 1 and user_id = " + userId;
        return getJdbcTemplate()
            .query(sql, new TodoMapper());
    }

    public Todo getTodoById(Integer todoId) {
        String sql = "select * from todo where deleted = 1 and todo_id = " + todoId;
        Todo todo = getJdbcTemplate()
            .queryForObject(sql, new TodoMapper());
        return todo;
    }

    public void saveOrUpdate(Todo todo) {
        String sql;
        if (todo.getTodoId() == 0) {
            sql = String.format(SQL_INSERT_TEMPLATE, todo.getUserId(), todo.getName(), todo.isDeleted(), todo.isChecked());
        } else {
            sql = String.format(SQL_UPDATE_TEMPLATE, todo.getTodoId(), todo.getUserId(), todo.getName(), todo.isDeleted(), todo.isChecked());
        }
         
        getJdbcTemplate()
            .query(sql, new TodoMapper());
    }
    
    public void delete(Integer todoId) {
        String sql = "delete from todo where todo_id = " + todoId;
        getJdbcTemplate()
            .query(sql, new TodoMapper());
    }
}
