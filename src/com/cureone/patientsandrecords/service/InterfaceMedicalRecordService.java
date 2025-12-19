package com.cureone.patientsandrecords.service;

//Made By Hamza Ali FA24-BSE-124

import com.cureone.patientsandrecords.model.MedicalRecord;
import java.util.List;

public interface InterfaceMedicalRecordService {

    void addRecord(MedicalRecord record);
    boolean updateRecord(MedicalRecord record);
    boolean deleteRecord(int recordID);
    MedicalRecord findRecord(int recordID);
    List<MedicalRecord> findRecordsByPatientId(int patientID);
    List<MedicalRecord> findAllRecords();
}

