package com.healthcare.controller;

import com.healthcare.model.Order;
import com.healthcare.model.User;
import com.healthcare.repository.UserRepository;
import com.healthcare.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody List<com.healthcare.model.OrderItem> items) {
        User user = getCurrentUser();
        Order newOrder = orderService.createOrder(user, items);
        return ResponseEntity.ok(newOrder);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders() {
        User user = getCurrentUser();
        List<Order> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByEmail(currentPrincipalName)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}