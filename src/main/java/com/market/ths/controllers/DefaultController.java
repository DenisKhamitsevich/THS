package com.market.ths.controllers;


import com.market.ths.item.Item;
import com.market.ths.item.ItemService;
import com.market.ths.user.User;
import com.market.ths.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/")
@SessionAttributes(value = {"page", "filters"})
public class DefaultController {
    private final UserService userService;
    private final ItemService itemService;
    private final Integer amountOfItemsOnPage = 5;

    @Autowired
    public DefaultController(UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    @GetMapping("account")
    public String getAccount(Model model) {
        User user = getLoggedUser();
        user = userService.loadUserById(user.getId());
        model.addAttribute("user", user);
        return "account";
    }

    @GetMapping(value = {"", "catalog"})
    public String getMain(Model model) {
        setModelAttributes(model);
        int page = (int) model.getAttribute("page");
        HashMap<String, String> filters = (HashMap<String, String>) model.getAttribute("filters");
        List<Item> values = itemService.getPageOfItems(page * amountOfItemsOnPage, amountOfItemsOnPage, filters);
        model.addAttribute("values", values);
        if (page * amountOfItemsOnPage + amountOfItemsOnPage < Integer.valueOf(filters.get("size")))
            model.addAttribute("nextPage", true);
        if ((int) model.getAttribute("page") != 0)
            model.addAttribute("prevPage", true);
        return "catalog";
    }

    @GetMapping("nextPage")
    public String nextPage(@ModelAttribute("page") Integer current, Model model) {
        model.addAttribute("page", ++current);
        return "redirect:catalog";
    }

    @GetMapping("prevPage")
    public String prevPage(@ModelAttribute("page") Integer current, Model model) {
        model.addAttribute("page", --current);
        return "redirect:catalog";
    }

    @GetMapping("/search")
    public String search(@RequestParam("search") String searchValue, Model model) {
        HashMap<String, String> values = (HashMap<String, String>) model.getAttribute("filters");
        values.put("search", searchValue);
        values.put("isFilterActive", "true");
        model.addAttribute("filters", values);
        model.addAttribute("page", 0);
        return "redirect:catalog";
    }

    @GetMapping("/filters")
    public String filters(@RequestParam("min_price") String min, @RequestParam("max_price") String max, Model model) {
        HashMap<String, String> values = (HashMap<String, String>) model.getAttribute("filters");
        if (!min.equals(""))
            values.put("minPrice", min);
        if (!max.equals(""))
            values.put("maxPrice", max);
        values.put("isFilterActive", "true");
        model.addAttribute("filters", values);
        model.addAttribute("page", 0);
        return "redirect:catalog";
    }

    @GetMapping("clearSearch")
    public String clear(Model model) {
        HashMap<String, String> values = (HashMap<String, String>) model.getAttribute("filters");
        model.addAttribute("page", 0);
        values.put("search", "");
        values.put("minPrice", "0");
        values.put("maxPrice", "1000000000");
        values.put("isFilterActive", "false");
        model.addAttribute("filters", values);
        return "redirect:catalog";
    }

    @GetMapping("item/{itemId}")
    public String getItem(@PathVariable Long itemId,Model model)
    {
        Item item=itemService.getItemById(itemId);
        model.addAttribute("item",item);
        return "item";
    }


    @PostMapping("savePassword")
    public String savePassword(@RequestParam("password") String password) {
        User loggedUser = getLoggedUser();
        Long id = loggedUser.getId();
        userService.updatePassword(id, password);
        return "redirect:account";
    }

    @GetMapping("change-items")
    public String changeItems(Model model) {
        model.addAttribute("item", new Item());
        return "change-items";
    }

    @GetMapping("result")
    public String getResult() {
        return "result";
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

    public static User getLoggedUser() {
        User loggedUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return loggedUser;
    }

    private void setModelAttributes(Model model) {
        if (!model.containsAttribute("page"))
            model.addAttribute("page", 0);
        if (!model.containsAttribute("filters")) {
            HashMap<String, String> result = new HashMap<String, String>();
            result.put("search", "");
            result.put("minPrice", "0");
            result.put("maxPrice", "1000000000");
            result.put("isFilterActive", "false");
            model.addAttribute("filters", result);
        }

    }


}
