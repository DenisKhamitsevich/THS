package com.market.ths.controllers;

import com.market.ths.item.Item;
import com.market.ths.item.ItemService;
import com.market.ths.order.Order;
import com.market.ths.order.OrderService;
import com.market.ths.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
@SessionAttributes("order")
public class OrderController {
    private final OrderService orderService;
    private final ItemService itemService;

    @Autowired
    public OrderController(OrderService orderService,ItemService itemService) {
        this.orderService = orderService;
        this.itemService=itemService;
    }

    @GetMapping("basket")
    public String getBasket(@ModelAttribute("order") Set<Long> ids, Model model)
    {
        List<Item> result=itemService.getItemsById(ids);
        Double price=orderService.getTotalPrice(result);
        model.addAttribute("price",price);
        model.addAttribute("items",result);
        return "basket";
    }

    @GetMapping("allOrders")
    public String getAllOrders(Model model)
    {
       List<Order> orders=orderService.findAll();
       model.addAttribute("values",orders);
       model.addAttribute("type","all");
        return "orders";
    }

    @GetMapping("myOrders")
    public String getMyOrders(Model model)
    {
        User user=DefaultController.getLoggedUser();
        List<Order> orders=orderService.findByUserId(user.getId());
        model.addAttribute("values",orders);
        model.addAttribute("type","my");
        return "orders";
    }

    @GetMapping("orderSuccessful")
    public String successfulOrder()
    {
        return "orderSuccessful";
    }

    @PostMapping("addToBasket")
    public String addToBasket(@RequestParam("id") Long itemId, Model model)
    {
        Set<Long> orders= (Set<Long>) model.getAttribute("order");
        orders.add(itemId);
        model.addAttribute("order",orders);
        return "redirect:catalog";
    }

    @PostMapping("deleteFromBasket")
    public String deleteFromBasket(@RequestParam("id") Long itemId, Model model)
    {
        Set<Long> orders= (Set<Long>) model.getAttribute("order");
        orders.remove(itemId);
        model.addAttribute("order",orders);
        return "redirect:basket";
    }

    @PostMapping("saveOrder")
    public String saveOrder(@ModelAttribute("order") Set<Long> ids,Model model)
    {
        User loggedUser=DefaultController.getLoggedUser();
        List<Item> items= itemService.getItemsById(ids);
        orderService.saveOrder(items,loggedUser.getId());
        model.addAttribute("order",new HashSet<Long>());
        return "redirect:orderSuccessful";
    }


}
