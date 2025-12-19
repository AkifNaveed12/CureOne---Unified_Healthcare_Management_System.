package com.cureone.appointmentsandscheduling.repository;

import com.cureone.appointmentsandscheduling.model.Doctor;
import java.util.List;

public interface DoctorRepository {
    int save(Doctor d);
    boolean update(Doctor d);
    boolean delete(int id);
    Doctor findById(int id);
    List<Doctor> findAll();
    Doctor findByUserId(int userId);
}
