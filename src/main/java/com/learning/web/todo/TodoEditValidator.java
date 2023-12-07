package com.learning.web.todo;

import com.learning.entity.Todo;
import com.learning.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by ulyanov on 30.09.16.
 */
@Repository("todoEditValidator")
public class TodoEditValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Todo.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        if (user == null) {
            errors.reject("user", "Нельзя создать задачу анонимному пользователю!");
            return;
        }

        Todo todo = (Todo)o;
        if (StringUtils.isBlank(todo.getName())) {
            errors.reject("name", "Нужно указать задачу, она не должна быть пустой!");
            return;
        }
        if (todo.getName().length() < 3) {
            errors.reject("name", "Содержание задачи должно быть как минимум длиною в 3 символа!");
            return;
        }

        if (!user.isAdmin() & user.getUserId() != todo.getUserId())
            errors.reject("user", "Нет доступа на редактирование чужих записей!");
    }
}
