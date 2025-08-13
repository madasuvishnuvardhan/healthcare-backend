package com.healthcare.repository;

import com.healthcare.model.Prescription;
import com.healthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByUser(User user);
}