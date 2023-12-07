package com.learning.web.todo;

import com.learning.util.paginated.SimplePaginatedForm;

/**
 * Created by ulyanov on 29.09.16.
 */
public class TodoForm extends SimplePaginatedForm {
    private String todoName;

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String username) {
        this.todoName = username;
    }
}
