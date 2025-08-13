package com.healthcare.service;

import com.healthcare.model.*;
import com.healthcare.repository.MedicineRepository;
import com.healthcare.repository.OrderRepository;
import com.healthcare.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, MedicineRepository medicineRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.medicineRepository = medicineRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order createOrder(User user, List<OrderItem> items) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : items) {
            Medicine medicine = medicineRepository.findById(item.getMedicine().getId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + item.getMedicine().getId()));

            if (medicine.getStock() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + medicine.getName());
            }

            medicine.setStock(medicine.getStock() - item.getQuantity());
            medicineRepository.save(medicine);

            totalPrice = totalPrice.add(BigDecimal.valueOf(medicine.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(items);
        order.setTotalPrice(totalPrice);
        order.setStatus(Order.OrderStatus.PENDING);

        for(OrderItem item : items) {
            item.setOrder(order);
        }

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void cancelOrder(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("You are not authorized to cancel this order");
        }
        orderRepository.delete(order);
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}