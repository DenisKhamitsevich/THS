package com.market.ths.controllers;

import com.market.ths.item.Item;
import com.market.ths.item.ItemService;
import com.market.ths.user.User;
import com.market.ths.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class AccountController {
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public AccountController(UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    @GetMapping("account")
    public String getAccount(Model model) {
        User user =DefaultController.getLoggedUser();
        user = userService.loadUserById(user.getId());
        model.addAttribute("user", user);
        return "account";
    }

    @GetMapping("item/{itemId}")
    public String getItem(@PathVariable Long itemId, Model model)
    {
        Item item=itemService.getItemById(itemId);
        model.addAttribute("item",item);
        return "item";
    }

    @GetMapping("change-items")
    public String changeItems(Model model) {
        model.addAttribute("item", new Item());
        return "change-items";
    }

    @GetMapping("getItem")
    public String getChangeItemPage(@RequestParam Long id, Model model)
    {
        if(itemService.itemExists(id))
        {
            Item item=itemService.getItemById(id);
            model.addAttribute("item",item);
        }
        return "modify-item";
    }


    @PostMapping("saveItem")
    public String saveItem(@RequestParam MultipartFile file, @ModelAttribute("item") Item item, Model model) {
        itemService.saveItem(file, item);
        model.addAttribute("result", "Товар успешно добавлен!");
        return "result";
    }

    @PostMapping("changeItem")
    public String changeItem(@RequestParam MultipartFile file, @ModelAttribute("item") Item item,Model model)
    {
        itemService.updateItem(file,item);
        model.addAttribute("item",new Item());
        return "change-items";
    }

    @PostMapping("deleteItem")
    public String deleteItem(@RequestParam Long id, Model model) {
        if (itemService.deleteItem(id))
            model.addAttribute("result", "Товар успешно удален!");
        else
            model.addAttribute("result", "Товара с данным ID не существует!");
        return "result";
    }


}
