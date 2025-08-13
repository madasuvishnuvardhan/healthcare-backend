package com.healthcare.controller;

import com.healthcare.model.Medicine;
import com.healthcare.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineRepository medicineRepository;

    // GET all medicines
    @GetMapping
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    // GET medicine by ID
    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        Optional<Medicine> medicine = medicineRepository.findById(id);
        return medicine.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CREATE new medicine
    @PostMapping
    public Medicine createMedicine(@RequestBody Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    // UPDATE medicine
    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicineDetails) {
        return medicineRepository.findById(id).map(medicine -> {
            medicine.setName(medicineDetails.getName());
            medicine.setBrand(medicineDetails.getBrand());
            medicine.setCategory(medicineDetails.getCategory());
            medicine.setSymptoms(medicineDetails.getSymptoms());
            medicine.setPrice(medicineDetails.getPrice());
            medicine.setStock(medicineDetails.getStock());
            Medicine updatedMedicine = medicineRepository.save(medicine);
            return ResponseEntity.ok(updatedMedicine);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE medicine
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMedicine(@PathVariable Long id)
 {
        return medicineRepository.findById(id).map(medicine -> {
            medicineRepository.delete(medicine);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
