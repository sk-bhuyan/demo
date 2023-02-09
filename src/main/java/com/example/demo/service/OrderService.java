package com.example.demo.service;

import com.example.demo.entities.Order;
import com.example.demo.entities.OrderRequest;

import java.util.List;

public interface OrderService {
    List<Order> getOrders();
    Order getOrder(Long id);
    Order createOrder(OrderRequest orderRequest);
    double calculateDiscount(Order order);
}
