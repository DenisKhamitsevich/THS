package com.market.ths.controllers;

import com.market.ths.exception.RegistrationException;
import com.market.ths.user.User;
import com.market.ths.user.UserRole;
import com.market.ths.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String getLogin()
    {
        return "login";
    }

    @GetMapping("successful")
    public String getSuccessful(){return "successful";}

    @GetMapping("register")
    public String getRegistration(Model model)
    {
        model.addAttribute("userForm", new User());
        return "register";
    }
    @GetMapping("login-failed")
    public String getFailedLogin()
    {
        return "login-failed";
    }


    @PostMapping("register")
    public String Register(@ModelAttribute("userForm") User user)
    {
        user.setRole(UserRole.CUSTOMER);
        userService.signUpUser(user);
        return "successful";
    }

    @PostMapping("account")
    public String saveNameAndEmail(@ModelAttribute("user") User user)
    {
        User loggedUser = DefaultController.getLoggedUser();
        Long id=loggedUser.getId();
        userService.updateNameAndEmail(id,user.getEmail(),user.getName());
        return "redirect:account";
    }

    @PostMapping("savePassword")
    public String savePassword(@RequestParam("password") String password) {
        User loggedUser = DefaultController.getLoggedUser();
        Long id = loggedUser.getId();
        userService.updatePassword(id, password);
        return "redirect:account";
    }

    @ExceptionHandler(RegistrationException.class)
    public ModelAndView registrationError(HttpServletRequest req, Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("registration-failed");
        return mav;
    }






}
