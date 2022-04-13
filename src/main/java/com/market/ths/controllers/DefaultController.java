package com.market.ths.controllers;


import com.market.ths.user.User;
import com.market.ths.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/")
public class DefaultController {
    private final UserService userService;

    @Autowired
    public DefaultController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("account")
    public String getAccount(Model model){
        User user = getLoggedUser();
        user=userService.loadUserById(user.getId());
        model.addAttribute("user",user);
        return "account";}

    @PostMapping("saveNameAndEmail")
    public String saveNameAndEmail(@ModelAttribute("user") User user)
    {
        User loggedUser = getLoggedUser();
        Long id=loggedUser.getId();
        userService.UpdateNameAndEmail(id,user.getEmail(),user.getName());
        return "redirect:account";
    }

    @PostMapping("savePassword")
    public String savePassword(@RequestParam("password") String password)
    {
        User loggedUser=getLoggedUser();
        Long id=loggedUser.getId();
        userService.UpdatePassword(id,password);
        return "redirect:account";
    }

    private User getLoggedUser()
    {
        User loggedUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return loggedUser;
    }

}
