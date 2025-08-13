package com.healthcare.controller;

import com.healthcare.model.Patient;
import com.healthcare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientsController {

    @Autowired
    private PatientRepository patientRepository;

    // GET all patients
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // GET patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CREATE new patient
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    // UPDATE patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patientDetails) {
        return patientRepository.findById(id).map(patient -> {
            patient.setName(patientDetails.getName());
            patient.setAge(patientDetails.getAge());
            patient.setGender(patientDetails.getGender());
            patient.setDiagnosis(patientDetails.getDiagnosis());
            patient.setAdmissionDate(patientDetails.getAdmissionDate());
            Patient updatedPatient = patientRepository.save(patient);
            return ResponseEntity.ok(updatedPatient);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePatient(@PathVariable Long id) {
        return patientRepository.findById(id).map(patient -> {
            patientRepository.delete(patient);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
