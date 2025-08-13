package com.healthcare.controller;

import com.healthcare.model.Prescription;
import com.healthcare.model.User;
import com.healthcare.repository.UserRepository;
import com.healthcare.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final UserRepository userRepository;

    public PrescriptionController(PrescriptionService prescriptionService, UserRepository userRepository) {
        this.prescriptionService = prescriptionService;
        this.userRepository = userRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<Prescription> uploadPrescription(@RequestParam("file") MultipartFile file) throws IOException {
        User user = getCurrentUser();
        Prescription prescription = prescriptionService.savePrescription(user, file);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping
    public ResponseEntity<List<Prescription>> getUserPrescriptions() {
        User user = getCurrentUser();
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByUser(user);
        return ResponseEntity.ok(prescriptions);
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return userRepository.findByEmail(currentPrincipalName)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}