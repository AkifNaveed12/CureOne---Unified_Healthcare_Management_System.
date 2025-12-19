package com.cureone.appointmentsandscheduling.service;

import com.cureone.appointmentsandscheduling.model.Doctor;
import com.cureone.appointmentsandscheduling.repository.DoctorRepository;

import java.util.List;

public class DoctorService {

    private final DoctorRepository repo;

    public DoctorService(DoctorRepository repo) {
        this.repo = repo;
    }

    public int addDoctor(Doctor d) {
        return repo.save(d);
    }

    public List<Doctor> getAllDoctors() {
        return repo.findAll();
    }

    public Doctor getDoctorById(int id) {
        return repo.findById(id);
    }

    public boolean updateDoctorProfile(int id, String newSpec, String phone, String email) {

        Doctor existing = repo.findById(id);
        if (existing == null) return false;

        //  Name cannot be changed
        //  Specialization can only be ADDED
        if (newSpec != null && !newSpec.isBlank()) {
            if (!existing.getSpecialization().contains(newSpec)) {
                existing.setSpecialization(existing.getSpecialization() + ", " + newSpec);
            }
        }

        if (phone != null && !phone.isBlank()) existing.setPhone(phone);
        if (email != null && !email.isBlank()) existing.setEmail(email);

        return repo.update(existing);
    }

    public boolean deleteDoctor(int id) {
        return repo.delete(id);
    }

    public Doctor getDoctorByUserId(int userId) {
        return repo.findByUserId(userId);
    }

}
