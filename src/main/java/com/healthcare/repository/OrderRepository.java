package com.healthcare.repository;

import com.healthcare.model.Order;
import com.healthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
}