package com.market.ths.controllers;


import com.market.ths.exception.RegistrationException;
import com.market.ths.item.Item;
import com.market.ths.item.ItemService;
import com.market.ths.user.User;
import com.market.ths.user.UserService;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Controller
@RequestMapping("/")
public class DefaultController {
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public DefaultController(UserService userService,ItemService itemService) {
        this.userService = userService;
        this.itemService=itemService;
    }

    @GetMapping("account")
    public String getAccount(Model model){
        User user = getLoggedUser();
        user=userService.loadUserById(user.getId());
        model.addAttribute("user",user);
        return "account";}


    @PostMapping("savePassword")
    public String savePassword(@RequestParam("password") String password)
    {
        User loggedUser=getLoggedUser();
        Long id=loggedUser.getId();
        userService.UpdatePassword(id,password);
        return "redirect:account";
    }

    @GetMapping("change-items")
    public String changeItems(Model model)
    {
        model.addAttribute("item",new Item());
        return "change-items";
    }

    @GetMapping("result")
    public String getResult()
    {
        return "result";
    }

    @PostMapping("saveItem")
    public String saveItem(@RequestParam MultipartFile file, @ModelAttribute("item") Item item,Model model)
    {
        itemService.SaveItem(file,item);
        model.addAttribute("result","Товар успешно добавлен!");
        return "result";
    }

    @PostMapping("deleteItem")
    public String deleteItem(@RequestParam Long id,Model model)
    {
        if(itemService.DeleteItem(id))
            model.addAttribute("result","Товар успешно удален!");
        else
            model.addAttribute("result","Товара с данным ID не существует!");
        return "result";
    }

    public static User getLoggedUser()
    {
        User loggedUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return loggedUser;
    }


}
