package com.cureone.patientsandrecords.service;

//Made By Hamza Ali FA24-BSE-124
import com.cureone.patientsandrecords.model.MedicalRecord;
import com.cureone.common.IdGenerator;
import com.cureone.patientsandrecords.repository.InterfaceMedicalRecordRepository;
import com.cureone.patientsandrecords.repository.PatientRepository;

import java.util.List;

public class MedicalRecordService implements InterfaceMedicalRecordService {

    private final InterfaceMedicalRecordRepository recordRepo;
    private final PatientRepository patientRepo; // check if patient exists

    public MedicalRecordService(InterfaceMedicalRecordRepository recordRepo, PatientRepository patientRepo) {
        this.recordRepo = recordRepo;
        this.patientRepo = patientRepo;
    }

    @Override
    public void addRecord(MedicalRecord record) {
        // ---------------- VALIDATIONS ----------------
        if (patientRepo.findById(record.getPatientID()) == null) {
            System.out.println("Patient ID " + record.getPatientID() + " does not exist.");
            return;
        }

        if (record.getDiagnosis() == null || record.getDiagnosis().isEmpty()) {
            System.out.println("Diagnosis cannot be empty.");
            return;
        }
        if (record.getTreatment() == null || record.getTreatment().isEmpty()) {
            System.out.println("Treatment cannot be empty.");
            return;
        }
        if (record.getDoctorName() == null || record.getDoctorName().isEmpty()) {
            System.out.println("Doctor name cannot be empty.");
            return;
        }
        if (record.getRecordDate() == null || record.getRecordDate().isEmpty()) {
            System.out.println("Record date cannot be empty.");
            return;
        }

        // ---------------- AUTO-GENERATE ID ----------------
        record.setRecordID(IdGenerator.generateId());

        // ---------------- ADD USING REPOSITORY ----------------
        recordRepo.add(record);

        System.out.println("Medical Record added successfully with ID: " + record.getRecordID());
    }

    @Override
    public boolean updateRecord(MedicalRecord record) {
        if (recordRepo.findById(record.getRecordID()) == null) {
            System.out.println("Record not found!");
            return false;
        }

        if (patientRepo.findById(record.getPatientID()) == null) {
            System.out.println("Patient ID " + record.getPatientID() + " does not exist.");
            return false;
        }

        return recordRepo.update(record);
    }

    @Override
    public boolean deleteRecord(int recordID) {
        if (recordRepo.findById(recordID) == null) {
            System.out.println("Record not found!");
            return false;
        }
        return recordRepo.delete(recordID);
    }

    @Override
    public MedicalRecord findRecord(int recordID) {
        return recordRepo.findById(recordID);
    }

    @Override
    public List<MedicalRecord> findRecordsByPatientId(int patientID) {
        return recordRepo.findByPatientId(patientID);
    }

    @Override
    public List<MedicalRecord> findAllRecords() {
        return recordRepo.findAll();
    }
}

