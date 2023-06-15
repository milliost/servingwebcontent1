package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.entity.User;
import com.example.servingwebcontent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
@Controller
public class UsersController {

    @Autowired
    private UserService userService;
    @GetMapping("/users")
    public String users(Model model){
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
    @PostMapping("/users")
    public String usersFind(@RequestParam(name = "email", required=false, defaultValue = "@") String email, Model model){
        User user = userService.findUserByEmail(email);
        List<User> users = new ArrayList<>();
        users.add(user);
        model.addAttribute("users", users);
        return "users";
    }
}