package com.healthcare.service;

import com.healthcare.model.Medicine;
import com.healthcare.repository.MedicineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }
}
