package ru.meetsapp.Meets.App.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.meetsapp.Meets.App.entity.User;
import ru.meetsapp.Meets.App.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserListController {
    @Autowired
    UserService userService;


    @GetMapping("")
    public String usersPage(Model model){
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> users = userService.getLastUsers(20);
        model.addAttribute("users", users);
        model.addAttribute("userDetails", userDetails);

        return "users";
    }

    @GetMapping("/{id}")
    public String usersPage(@PathVariable("id") long userId, Model model){
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("bio", user.getBio());

        return "user_view";
    }
}
