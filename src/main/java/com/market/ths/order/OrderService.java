package com.market.ths.order;

import com.market.ths.item.Item;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrder(List<Item> ids, Long userId)
    {
        long millis=System.currentTimeMillis();
        Date date =new Date(millis);
        Time time=new Time(millis);
        String dateAndTime=date+" "+time;
        Order order=new Order(userId,ids,dateAndTime);
        orderRepository.save(order);
    }

    public Double getTotalPrice(List<Item> values)
    {
        Double result=0d;
        for(Item temp:values)
        {
            result+=temp.getPrice();
        }
        return result;
    }

}