package com.train.jitag.controller;

import com.train.jitag.domain.OrderInfo;
import com.train.jitag.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jasper
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/findUserById")
    public OrderInfo findUserById(@RequestParam int id) {
        return orderService.findUserById(id);
    }

    @GetMapping("/save")
    public void findUserById(@RequestBody OrderInfo orderInfo) {
        orderService.saveOrder(orderInfo);
    }
}
