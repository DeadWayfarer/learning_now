package com.learning.web.todo;

import com.learning.dao.TodoDao;
import com.learning.entity.Todo;
import com.learning.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by ulyanov on 30.09.16.
 */
@Controller
public class TodoEditController {
    @Autowired private TodoDao todoDao;
    @Autowired private TodoEditValidator todoEditValidator;

    @RequestMapping(value = "/todo/todoEdit.html", method = RequestMethod.GET)
    public String onGet(
            Model model,
            @RequestParam(required = false) Integer todoId
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user;
        if (principal instanceof User)
            user = (User)principal;
        else
            return "redirect:/login.html";

        Todo command = todoDao.getTodoById(todoId);
        if (command==null){
            command = new Todo();
            command.setUserId(user.getUserId());
        }
        model.addAttribute("command", command);
        return "todo/todoEdit";
    }

    @RequestMapping(value = "/todo/todoEdit.html", method = RequestMethod.POST, params = "!_cancel")
    public String onPost(
            Model model,
            @ModelAttribute("command") Todo command,
            BindingResult result
    ) {
        todoEditValidator.validate(command, result);
        if (!result.hasErrors()) {
            todoDao.saveOrUpdate(command);
        } else {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("command", command);
            return "todo/todoEdit";
        }
        return "redirect:/todo/todos.html?todoId=" + command.getTodoId();
    }

    @RequestMapping(value = "/todo/todoEdit.html", method = RequestMethod.POST, params = "_cancel")
    public String onCancel() {
        return "redirect:/todo/todos.html";
    }
}
