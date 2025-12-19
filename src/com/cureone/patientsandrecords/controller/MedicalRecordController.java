package com.cureone.patientsandrecords.controller;

//Made By Hamza Ali FA24-BSE-124

import com.cureone.patientsandrecords.model.MedicalRecord;
import com.cureone.patientsandrecords.service.InterfaceMedicalRecordService;

import java.util.List;
import java.util.Scanner;

public class MedicalRecordController {

    private final InterfaceMedicalRecordService recordService;
    private final Scanner scanner = new Scanner(System.in);

    // ---------Controller depends on Service Layer-----
    public MedicalRecordController(InterfaceMedicalRecordService recordService) {
        this.recordService = recordService;
    }

    // ------------------ Medical Record Menu ------------------
    public void showMenu() {
        while (true) {
            System.out.println("\n======= MEDICAL RECORD MENU =======");
            System.out.println("1. Add Medical Record");
            System.out.println("2. Update Medical Record");
            System.out.println("3. Delete Medical Record");
            System.out.println("4. Find Record by ID");
            System.out.println("5. Find Records by Patient ID");
            System.out.println("6. View All Records");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter Option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // buffer clear krta ha

            switch (option) {
                case 1 -> addRecord();
                case 2 -> updateRecord();
                case 3 -> deleteRecord();
                case 4 -> findById();
                case 5 -> findByPatientId();
                case 6 -> findAll();
                case 0 -> { return; }
                default -> System.out.println("Invalid Option! Try again.");
            }
        }
    }

    // ---------------- Add Record ----------------
    private void addRecord() {
        MedicalRecord record = new MedicalRecord();

        System.out.print("Enter Patient ID: ");
        record.setPatientID(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter Diagnosis: ");
        record.setDiagnosis(scanner.nextLine());

        System.out.print("Enter Treatment: ");
        record.setTreatment(scanner.nextLine());

        System.out.print("Enter Doctor Name: ");
        record.setDoctorName(scanner.nextLine());

        System.out.print("Enter Record Date (YYYY-MM-DD): ");
        record.setRecordDate(scanner.nextLine());

        recordService.addRecord(record);
    }

    // ---------------- Update Record ----------------
    private void updateRecord() {
        System.out.print("Enter Record ID to Update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        MedicalRecord existing = recordService.findRecord(id);
        if (existing == null) {
            System.out.println("Record Not Found!");
            return;
        }

        System.out.print("Enter Patient ID: ");
        existing.setPatientID(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Enter Diagnosis: ");
        existing.setDiagnosis(scanner.nextLine());

        System.out.print("Enter Treatment: ");
        existing.setTreatment(scanner.nextLine());

        System.out.print("Enter Doctor Name: ");
        existing.setDoctorName(scanner.nextLine());

        System.out.print("Enter Record Date (YYYY-MM-DD): ");
        existing.setRecordDate(scanner.nextLine());

        boolean updated = recordService.updateRecord(existing);
        System.out.println(updated ? "Updated Successfully!" : "Update Failed!");
    }

    // ---------------- Delete Record ----------------
    private void deleteRecord() {
        System.out.print("Enter Record ID to Delete: ");
        int id = scanner.nextInt();

        boolean deleted = recordService.deleteRecord(id);
        System.out.println(deleted ? "Deleted Successfully!" : "Record Not Found!");
    }

    // ---------------- Find Record by ID ----------------
    private void findById() {
        System.out.print("Enter Record ID: ");
        int id = scanner.nextInt();

        MedicalRecord record = recordService.findRecord(id);
        if (record != null) {
            printRecord(record);
        } else {
            System.out.println("Record Not Found!");
        }
    }

    // ---------------- Find Records by Patient ID ----------------
    private void findByPatientId() {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();

        List<MedicalRecord> records = recordService.findRecordsByPatientId(patientId);
        if (records.isEmpty()) {
            System.out.println("No Records Found for Patient ID: " + patientId);
            return;
        }

        System.out.println("\n----- RECORDS FOR PATIENT ID " + patientId + " -----");
        for (MedicalRecord r : records) {
            printRecord(r);
        }
    }

    // ---------------- View All Records ----------------
    private void findAll() {
        List<MedicalRecord> records = recordService.findAllRecords();

        if (records.isEmpty()) {
            System.out.println("No Records Found!");
            return;
        }

        System.out.println("\n----- ALL MEDICAL RECORDS -----");
        for (MedicalRecord r : records) {
            printRecord(r);
        }
    }

    // ---------------- Print Record ----------------
    private void printRecord(MedicalRecord r) {
        System.out.println("Record ID: " + r.getRecordID());
        System.out.println("Patient ID: " + r.getPatientID());
        System.out.println("Diagnosis: " + r.getDiagnosis());
        System.out.println("Treatment: " + r.getTreatment());
        System.out.println("Doctor: " + r.getDoctorName());
        System.out.println("Date: " + r.getRecordDate());
        System.out.println("------------------------------");
    }
}
