package com.learning.web.todo;

import com.learning.util.paginated.SimplePaginatedForm;

/**
 * Created by ulyanov on 29.09.16.
 */
public class TodoForm extends SimplePaginatedForm {
    private String todoNameFilter;

    public String getTodoNameFilter() {
        return todoNameFilter;
    }

    public void setTodoNameFilter(String username) {
        this.todoNameFilter = username;
        fixPageNumber(0);
    }
}
