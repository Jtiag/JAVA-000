package com.jtiag.rpcfx.demo.provider;

import com.jtiag.rpcfx.demo.api.Order;
import com.jtiag.rpcfx.demo.api.OrderService;

public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
