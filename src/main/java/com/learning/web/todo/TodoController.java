package com.learning.web.todo;

import com.learning.dao.TodoDao;
import com.learning.dao.UserDao;
import com.learning.entity.Todo;
import com.learning.entity.User;
import com.learning.util.paginated.PaginatedListHelper;
import com.learning.util.paginated.SimplePaginatedList;
import com.learning.web.user.UsersForm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ulyanov on 29.09.16.
 */

@Controller
@SessionAttributes(names = "TodoController.command")
public class TodoController {

    @Autowired private TodoDao todoDao;

    @ModelAttribute("TodoController.command")
    public TodoForm getCommand(){
        return new TodoForm();
    }

    @RequestMapping("/todo/todos.html")
    public String searchUsers(
            Model model,
            @ModelAttribute("TodoController.command") TodoForm command
    ){
        User user = new User();
        user.setUserId(1);

        int totalCount = todoDao.getTodoCountOfUser(user);
        List<Todo> list = todoDao.getTodosOfUser(user, command.getFirstResult(), command.getPageSize());
        SimplePaginatedList paginatedList = PaginatedListHelper.getPaginatedList(list, totalCount, command);;
        model.addAttribute("list", paginatedList);
        model.addAttribute("command", command);
        return "todo/todos";
    }

    @RequestMapping("/todo/todoDelete.html")
    public String delUser(@RequestParam(required = false) Integer todoId){
        if (todoId!=null){
            todoDao.delete(todoId);
        }
        return "redirect:/todo/todoDelete.html";
    }

}
