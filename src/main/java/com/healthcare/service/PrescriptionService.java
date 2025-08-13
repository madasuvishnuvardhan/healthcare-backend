package com.healthcare.service;

import com.healthcare.model.Prescription;
import com.healthcare.model.User;
import com.healthcare.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final Path rootLocation = Paths.get("uploads");

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public Prescription savePrescription(User user, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        String filename = user.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));

        Prescription prescription = new Prescription();
        prescription.setUser(user);
        prescription.setFileUrl(this.rootLocation.resolve(filename).toString());
        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPrescriptionsByUser(User user) {
        return prescriptionRepository.findByUser(user);
    }
}