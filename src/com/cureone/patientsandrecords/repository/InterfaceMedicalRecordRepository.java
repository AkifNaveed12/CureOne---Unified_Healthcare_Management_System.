package com.cureone.patientsandrecords.repository;

//Made By Hamza Ali FA24-BSE-124
import java.util.List;
import com.cureone.patientsandrecords.model.MedicalRecord;
import java.util.List;

public interface InterfaceMedicalRecordRepository {
    void add(MedicalRecord record);
    boolean update(MedicalRecord record);
    boolean delete(int recordID);
    MedicalRecord findById(int recordID);
    List<MedicalRecord> findByPatientId(int patientID);
    List<MedicalRecord> findAll();
}
