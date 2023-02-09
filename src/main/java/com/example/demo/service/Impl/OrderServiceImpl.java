package com.example.demo.service.Impl;

import com.example.demo.entities.Customer;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.OrderRequest;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + id));
    }

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + orderRequest.getCustomerId() + " not found"));

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(itemRequest -> new OrderItem(itemRequest.getItemId(), itemRequest.getQuantity()))
                .collect(Collectors.toList());

        Order order = new Order(customer, orderItems, LocalDateTime.now(), orderRequest.getShippingAddress());
        order.setDiscount(calculateDiscount(order));

        return orderRepository.save(order);
    }
    @Override
    public double calculateDiscount(Order order) {
        Customer customer = order.getCustomer();
        int numberOfOrders = customer.getOrders().size();
        double discount = 0.0;

        if (numberOfOrders > 10) {
            discount = 0.1;
        }
        if (numberOfOrders > 20) {
            discount = 0.2;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.getMonthValue() == 11 && now.getDayOfMonth() >= 23 && now.getDayOfMonth() <= 27) {
            discount += 0.15;
        }

        return discount;
    }
}
