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
import org.springframework.security.core.context.SecurityContextHolder;
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user;
        if (principal instanceof User)
            user = (User)principal;
        else
            return "redirect:/login.html";
        
        if (command.getPage() <= 0) {
            command.setPage(1);
        }
        int totalCount = todoDao.getTodoCountOfUser(user, command.getTodoNameFilter());
        int startOffset = (command.getPage() - 1) * command.getPageSize();
        int endOffset = startOffset + command.getPageSize();

        List<Todo> list = todoDao.getTodosOfUser(user, startOffset, endOffset, command.getTodoNameFilter());
        SimplePaginatedList paginatedList = PaginatedListHelper.getPaginatedList(list, totalCount, command);;
        model.addAttribute("list", paginatedList);
        model.addAttribute("command", command);
        return "todo/todos";
    }

    @RequestMapping("/todo/todoDelete.html")
    public String delTodo(@RequestParam(required = false) Integer todoId){
        if (todoId!=null){
            todoDao.delete(todoId);
        }
        return "redirect:/todo/todos.html";
    }

    @RequestMapping("/todo/todoCheck.html") 
    public String checkTodo(@RequestParam(required = false) Integer todoId){
        Todo todo = todoDao.getTodoById(todoId);
        if (todo!=null){
            todo.setChecked(!todo.isChecked());
            todoDao.saveOrUpdate(todo);
        }
        return "redirect:/todo/todos.html";
    }
}
