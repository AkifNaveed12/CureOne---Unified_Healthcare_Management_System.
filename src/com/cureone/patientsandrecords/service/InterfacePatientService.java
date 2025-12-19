package com.cureone.patientsandrecords.service;

import com.cureone.common.Result;
import com.cureone.patientsandrecords.model.Patient;

import java.util.List;

public interface InterfacePatientService {

    // COMMANDS (return Result for message + status)
    Result<Patient> addPatient(Patient patient);

    Result<Patient> updatePatient(Patient patient);

    Result<Boolean> deletePatient(int patientId);

    // QUERIES (return data only)
    Patient getPatientById(int id);

    List<Patient> getPatientsByName(String name);

    List<Patient> getAllPatients();
}
