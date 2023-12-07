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

    private final String SQL_COUNT_TEMPLATE = "select count(todo_id) from todo where deleted = 0";
    private final String SQL_SELECT_TEMPLATE = "select * from todo where deleted = 0";
    private final String SQL_INSERT_TEMPLATE = "insert into todo (user_id, name, deleted, checked) values (%s,'%s',%s,%s)";
    private final String SQL_UPDATE_TEMPLATE = "update todo set user_id = %s, name = '%s', deleted = %s, checked = %s where todo_id = %s";
    private final String SQL_DELETE_TEMPLATE = "update todo set deleted = 1 where todo_id = %s";

    public Integer getTodoCountOfUser(User user, String nameFilter) {
        int userId = user.getUserId();
        String sql = SQL_COUNT_TEMPLATE + " and user_id = " + userId;
        if (StringUtils.isNotBlank(nameFilter))
            sql += " and name like '%"+nameFilter+"%' ";
        return getJdbcTemplate()
            .queryForObject(sql, Integer.class);
    }

    public List<Todo> getTodosOfUser(User user, int skip, int limit, String nameFilter) {
        int userId = user.getUserId();
        String sql = SQL_SELECT_TEMPLATE + " and user_id = " + userId;
        if (StringUtils.isNotBlank(nameFilter))
            sql += " and name like '%"+nameFilter+"%' ";
        sql += " limit " + skip + ", " + limit;
        return getJdbcTemplate()
            .query(sql, new TodoMapper());
    }

    public Todo getTodoById(Integer todoId) {
        if (todoId == null || todoId == 0)
            return null;
        String sql = SQL_SELECT_TEMPLATE + " and todo_id = " + todoId;
        Todo todo = getJdbcTemplate()
            .queryForObject(sql, new TodoMapper());
        return todo;
    }

    public void saveOrUpdate(Todo todo) {
        String sql;
        if (todo.getTodoId() == 0) {
            sql = String.format(SQL_INSERT_TEMPLATE, todo.getUserId(), todo.getName(), todo.isDeleted(), todo.isChecked());
        } else {
            sql = String.format(SQL_UPDATE_TEMPLATE, todo.getUserId(), todo.getName(), todo.isDeleted(), todo.isChecked(), todo.getTodoId());
        }
         
        getJdbcTemplate()
            .update(sql);
    }
    
    public void delete(Integer todoId) {
        String sql = String.format(SQL_DELETE_TEMPLATE, todoId);
        getJdbcTemplate()
            .update(sql);
    }
}
