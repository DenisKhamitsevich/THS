package com.market.ths.controllers;


import com.market.ths.item.Item;
import com.market.ths.item.ItemService;
import com.market.ths.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/")
@SessionAttributes(value = {"page", "filters","order"})
public class DefaultController {
    private final ItemService itemService;
    private final Integer amountOfItemsOnPage = 10;

    @Autowired
    public DefaultController(ItemService itemService) {
        this.itemService = itemService;
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


    @GetMapping("result")
    public String getResult() {
        return "result";
    }

    @GetMapping("prev")
    public String getPrev(HttpServletRequest request)
    {
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }


    public static User getLoggedUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void setModelAttributes(Model model) {
        if (!model.containsAttribute("page"))
            model.addAttribute("page", 0);
        if (!model.containsAttribute("filters")) {
            HashMap<String, String> result = new HashMap<>();
            result.put("search", "");
            result.put("minPrice", "0");
            result.put("maxPrice", "1000000000");
            result.put("isFilterActive", "false");
            model.addAttribute("filters", result);
        }
        if(!model.containsAttribute("order"))
            model.addAttribute("order",new HashSet<Long>());

    }


}
