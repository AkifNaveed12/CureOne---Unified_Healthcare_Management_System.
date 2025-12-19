package com.cureone.patientsandrecords.controller;

import com.cureone.appointmentsandscheduling.model.Appointment;
import com.cureone.common.Result;
import com.cureone.patientsandrecords.model.Patient;
import com.cureone.patientsandrecords.service.PatientService;
import com.cureone.appointmentsandscheduling.service.AppointmentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class PatientAdminController {

    private final PatientService service;
    private final Scanner scanner = new Scanner(System.in);
    private AppointmentService appointmentService;

    public PatientAdminController(PatientService service, AppointmentService appointmentService) {
        this.service = service;
        this.appointmentService = appointmentService;
    }

    // MenuInvoker entry
    public void showMenu() {
        startMenu();
    }

    public void startMenu() {
        while (true) {
            System.out.println("\n======= PATIENT MENU =======");
            System.out.println("1. Add Patient");
            System.out.println("2. Update Patient");
            System.out.println("3. Delete Patient");
            System.out.println("4. Find Patient by ID");
            System.out.println("5. View All Patients");
            System.out.println("6. My Appointments");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter Option: ");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input");
                continue;
            }

            switch (option) {
                case 1 -> addPatient();
                case 2 -> updatePatient();
                case 3 -> deletePatient();
                case 4 -> findPatient();
                case 5 -> listAllPatients();
                case 6 -> myAppointments();
                case 0 -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void addPatient() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();

        System.out.print("Enter Country: ");
        String country = scanner.nextLine();

        System.out.print("Enter Contact: ");
        String contact = scanner.nextLine();

        System.out.print("Enter Disease Description: ");
        String disease = scanner.nextLine();

        Patient patient = new Patient(0, name, age, gender, country, contact, disease);

        Result<Patient> result = service.addPatient(patient);
        System.out.println(result.getMessage());

        if (result.isSuccess()) {
            System.out.println(result.getData());
        }
    }

    private void updatePatient() {
        System.out.print("Enter Patient ID to Update: ");
        int id = Integer.parseInt(scanner.nextLine());

        Patient existing = service.getPatientById(id);
        if (existing == null) {
            System.out.println("Patient not found");
            return;
        }

        System.out.print("Enter New Name: ");
        existing.setName(scanner.nextLine());

        System.out.print("Enter New Age: ");
        existing.setAge(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter New Gender: ");
        existing.setGender(scanner.nextLine());

        System.out.print("Enter New Country: ");
        existing.setCountry(scanner.nextLine());

        System.out.print("Enter New Contact: ");
        existing.setContact(scanner.nextLine());

        System.out.print("Enter New Disease Description: ");
        existing.setDiseaseDescription(scanner.nextLine());

        Result<Patient> result = service.updatePatient(existing);
        System.out.println(result.getMessage());

        if (result.isSuccess()) {
            System.out.println(existing);
        }
    }

    private void deletePatient() {
        System.out.print("Enter Patient ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        Result<Boolean> result = service.deletePatient(id);
        System.out.println(result.getMessage());
    }

    private void findPatient() {
        System.out.print("Enter Patient ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        Patient patient = service.getPatientById(id);
        if (patient == null) {
            System.out.println("Patient not found");
        } else {
            System.out.println(patient);
        }
    }

    private void listAllPatients() {
        List<Patient> patients = service.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("(no patients)");
            return;
        }
        patients.forEach(System.out::println);
    }
    private void myAppointments() {

        System.out.print("Enter your Patient ID: ");
        int patientId = Integer.parseInt(scanner.nextLine());

        System.out.println("1. View all appointments");
        System.out.println("2. View appointments by date");
        System.out.print("Choose: ");

        int choice = Integer.parseInt(scanner.nextLine());

        List<Appointment> list;

        if (choice == 2) {
            System.out.print("Date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());
            list = appointmentService.getAppointmentsForPatientByDate(patientId, date);
        } else {
            list = appointmentService.getAppointmentsForPatient(patientId);
        }

        if (list.isEmpty()) {
            System.out.println("(no appointments)");
            return;
        }

        list.forEach(System.out::println);
    }

}
