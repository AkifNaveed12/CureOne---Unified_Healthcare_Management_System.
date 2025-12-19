package com.cureone.patientsandrecords.service;

import com.cureone.common.Result;
import com.cureone.patientsandrecords.model.Patient;
import com.cureone.patientsandrecords.repository.InterfacePatientRepository;

import java.util.List;

public class PatientService implements InterfacePatientService {

    private final InterfacePatientRepository patientRepo;

    public PatientService(InterfacePatientRepository repo) {
        this.patientRepo = repo;
    }

    @Override
    public Result<Patient> addPatient(Patient patient) {
        if (patient == null || patient.getAge() <= 0) {
            return new Result<>(false, "Invalid patient");
        }

        patientRepo.add(patient);
        return new Result<>(true, "Patient added successfully", patient);
    }

    @Override
    public Result<Patient> updatePatient(Patient patient) {
        if (patient == null || patient.getId() <= 0) {
            return new Result<>(false, "Invalid patient");
        }

        boolean updated = patientRepo.update(patient);
        return updated
                ? new Result<>(true, "Patient updated successfully", patient)
                : new Result<>(false, "Patient not found");
    }

    @Override
    public Result<Boolean> deletePatient(int id) {
        boolean deleted = patientRepo.delete(id);
        return new Result<>(true, "Patient deleted", deleted);
    }

    // 🔥 FIX: ID == patients.id (from linkedId)
    @Override
    public Patient getPatientById(int id) {
        return patientRepo.findById(id);
    }

    @Override
    public List<Patient> getPatientsByName(String name) {
        return patientRepo.findByName(name);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }
}
