package com.cureone.patientsandrecords.repository;

//Made By Hamza Ali FA24-BSE-124
import com.cureone.patientsandrecords.model.Patient;
import java.util.List;

public interface InterfacePatientRepository {
    void add(Patient patient);
    boolean update(Patient patient);
    boolean delete(int id);
    Patient findById(int id);
    List<Patient> findByName(String name);
    List<Patient> findAll();
    Patient findByUserId(int userId);

}

