package com.cureone.appointmentsandscheduling.controller;

import com.cureone.appointmentsandscheduling.model.Appointment;
import com.cureone.appointmentsandscheduling.service.AppointmentService;
import com.cureone.appointmentsandscheduling.service.DoctorService;
import com.cureone.appointmentsandscheduling.model.Doctor;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class DoctorSelfController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final int loggedInDoctorId;
    private final Scanner scanner = new Scanner(System.in);

    public DoctorSelfController(
            AppointmentService appointmentService,
            DoctorService doctorService,
            int loggedInDoctorId
    ) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.loggedInDoctorId = loggedInDoctorId;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n======= DOCTOR DASHBOARD =======");
            System.out.println("1. Today's Schedule");
            System.out.println("2. View Schedule by Date");
            System.out.println("3. Appointment History");
            System.out.println("4. View My Profile");
            System.out.println("0. Logout");
            System.out.print("Choose: ");

            int ch = Integer.parseInt(scanner.nextLine());

            switch (ch) {
                case 1 -> todaySchedule();
                case 2 -> scheduleByDate();
                case 3 -> appointmentHistory();
                case 4 -> viewProfile();
                case 0 -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    // ---------------- TODAY ----------------
    private void todaySchedule() {
        LocalDate today = LocalDate.now();
        List<Appointment> list =
                appointmentService.getDoctorSchedule(loggedInDoctorId, today);

        if (list.isEmpty()) {
            System.out.println("(no appointments today)");
            return;
        }
        list.forEach(System.out::println);
    }

    // ---------------- BY DATE ----------------
    private void scheduleByDate() {
        System.out.print("Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        List<Appointment> list =
                appointmentService.getDoctorSchedule(loggedInDoctorId, date);

        if (list.isEmpty()) {
            System.out.println("(no appointments)");
            return;
        }
        list.forEach(System.out::println);
    }

    // ---------------- HISTORY ----------------
    private void appointmentHistory() {
        System.out.println("Showing past appointments only");

        LocalDate today = LocalDate.now();
        List<Appointment> all =
                appointmentService.getDoctorSchedule(loggedInDoctorId, today.minusYears(50));

        all.stream()
                .filter(a -> a.getAppointmentDate().isBefore(today))
                .forEach(a ->
                        System.out.println(
                                a.getAppointmentDate() +
                                        " | Patient ID: " + a.getPatientId() +
                                        " | Reason: " + a.getReason()
                        )
                );
    }

    // ---------------- PROFILE ----------------
    private void viewProfile() {
        Doctor d = doctorService.getDoctorById(loggedInDoctorId);

        if (d == null) {
            System.out.println("Doctor not found");
            return;
        }

        System.out.println("\n--- My Profile ---");
        System.out.println("ID: " + d.getId());
        System.out.println("Name: " + d.getName());
        System.out.println("Specialization: " + d.getSpecialization());
        System.out.println("Contact: " + d.getPhone());
    }
}
