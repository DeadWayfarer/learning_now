package com.learning.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping("/login.html")
    public String getLogin(
            @RequestParam(required = false) String error,
            Model model)
    {
        model.addAttribute("error",error);
        return "login";
    }

    @RequestMapping({"", "/", "/index.html"})
    public String getAdminForm(){
        return "index";
    }
}
