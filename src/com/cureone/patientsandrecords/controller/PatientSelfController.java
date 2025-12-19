package com.cureone.patientsandrecords.controller;

import com.cureone.appointmentsandscheduling.model.Appointment;
import com.cureone.appointmentsandscheduling.service.AppointmentService;
import com.cureone.common.Result;
import com.cureone.patientsandrecords.model.Patient;
import com.cureone.patientsandrecords.service.PatientService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class PatientSelfController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final int loggedInPatientId;
    private final Scanner scanner = new Scanner(System.in);

    public PatientSelfController(
            PatientService patientService,
            AppointmentService appointmentService,
            int loggedInPatientId
    ) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.loggedInPatientId = loggedInPatientId;
    }

    // ================= MENU =================
    public void showMenu() {
        while (true) {
            System.out.println("\n======= PATIENT DASHBOARD =======");
            System.out.println("1. Book Appointment");
            System.out.println("2. My Appointments");
            System.out.println("3. Update My Profile");
            System.out.println("4. My Medical Records");
            System.out.println("0. Logout");
            System.out.print("Choose: ");

            int ch;
            try {
                ch = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input");
                continue;
            }

            switch (ch) {
                case 1 -> bookAppointment();
                case 2 -> myAppointments();
                case 3 -> updateProfile();
                case 4 -> myMedicalRecords();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    // ================= BOOK APPOINTMENT =================
    private void bookAppointment() {

        System.out.print("Doctor ID: ");
        int doctorId = Integer.parseInt(scanner.nextLine());

        System.out.print("Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Time (HH:mm): ");
        String time = scanner.nextLine();

        System.out.print("Duration (minutes): ");
        int duration = Integer.parseInt(scanner.nextLine());

        System.out.print("Reason: ");
        String reason = scanner.nextLine();

        Result<Appointment> result = appointmentService.bookAppointment(
                loggedInPatientId,
                doctorId,
                date,
                time,
                duration,
                reason
        );

        System.out.println(result.getMessage());
    }

    // ================= MY APPOINTMENTS =================
    private void myAppointments() {

        List<Appointment> list =
                appointmentService.getAppointmentsForPatient(loggedInPatientId);

        if (list.isEmpty()) {
            System.out.println("(no appointments)");
            return;
        }

        for (Appointment a : list) {
            System.out.println(
                    "ID: " + a.getId() +
                            " | Doctor ID: " + a.getDoctorId() +
                            " | Date: " + a.getAppointmentDate() +
                            " | Time: " + a.getAppointmentTime() +
                            " | Reason: " + a.getReason()
            );
        }
    }

    // ================= UPDATE PROFILE =================
    private void updateProfile() {

        Patient p = patientService.getPatientById(loggedInPatientId);

        if (p == null) {
            System.out.println("Patient not found");
            return;
        }

        // 🔒 LOCKED FIELDS
        System.out.println("\n--- Profile (Locked Fields) ---");
        System.out.println("Name   : " + p.getName());
        System.out.println("Gender : " + p.getGender());

        // 🔓 EDITABLE FIELDS
        System.out.println("\n--- Editable Fields ---");

        System.out.print("Age [" + p.getAge() + "]: ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isBlank()) {
            p.setAge(Integer.parseInt(ageInput));
        }

        System.out.print("Contact [" + p.getContact() + "]: ");
        String contact = scanner.nextLine();
        if (!contact.isBlank()) {
            p.setContact(contact);
        }

        patientService.updatePatient(p);
        System.out.println("Profile updated successfully");
    }

    // ================= MEDICAL RECORDS =================
    private void myMedicalRecords() {

        List<Appointment> history =
                appointmentService.getAppointmentsForPatient(loggedInPatientId);

        if (history.isEmpty()) {
            System.out.println("(no medical history)");
            return;
        }

        System.out.println("\n--- Medical History ---");
        for (Appointment a : history) {
            System.out.println(
                    a.getAppointmentDate() +
                            " | Doctor ID: " + a.getDoctorId() +
                            " | Reason: " + a.getReason()
            );
        }
    }
}
