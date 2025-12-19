package com.cureone.appointmentsandscheduling.controller;

import com.cureone.appointmentsandscheduling.model.Appointment;
import com.cureone.appointmentsandscheduling.repository.AppointmentRepository;
import com.cureone.appointmentsandscheduling.repository.DoctorRepository;
import com.cureone.appointmentsandscheduling.service.AppointmentService;
import com.cureone.common.Result;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class AppointmentController {
    private final AppointmentService service;
    private final AppointmentRepository apptRepo;
    private final DoctorRepository doctorRepo;
    private final Scanner scanner = new Scanner(System.in);

    public AppointmentController(AppointmentService service, AppointmentRepository apptRepo, DoctorRepository doctorRepo) {
        this.service = service;
        this.apptRepo = apptRepo;
        this.doctorRepo = doctorRepo;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Appointments ---");
            System.out.println("1. Book appointment");
            System.out.println("2. View doctor schedule (by date)");
            System.out.println("3. List appointments for doctor (date)");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            int ch = Integer.parseInt(scanner.nextLine().trim());
            switch (ch) {
                case 1 -> book();
                case 2 -> viewDoctorSchedule();
                case 3 -> listAppointmentsForDoctor();
                case 0 -> { return; }
                default -> System.out.println("Invalid");
            }
        }
    }

    private void book() {

        try {
            System.out.print("Patient id: ");
            int patientId = Integer.parseInt(scanner.nextLine());

            System.out.print("Doctor id: ");
            int doctorId = Integer.parseInt(scanner.nextLine());

            System.out.print("Date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("Time (HH:mm): ");
            String timeInput = scanner.nextLine();

            if (timeInput.length() == 5) {
                timeInput = timeInput + ":00";
            }

            // ✅ validate time format early
            LocalTime time = LocalTime.parse(timeInput);

            System.out.print("Duration minutes: ");
            int duration = Integer.parseInt(scanner.nextLine());

            System.out.print("Reason: ");
            String reason = scanner.nextLine();

            Result<Appointment> appointment =
                    service.bookAppointment(
                            patientId,
                            doctorId,
                            date,
                            String.valueOf(time),
                            duration,
                            reason
                    );
            System.out.println(appointment.getMessage());

            if (appointment.isSuccess() && appointment.getData() != null) {
                System.out.println(appointment.getData());
            }


        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }

    }
    private void viewDoctorSchedule() {
        System.out.print("Doctor id: ");
        int did = Integer.parseInt(scanner.nextLine());

        System.out.print("Date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        List<Appointment> list = apptRepo.findByDoctorAndDate(did, date);

        if (list.isEmpty()) {
            System.out.println("(no appointments)");
            return;
        }

        for (Appointment a : list) {
            System.out.printf(
                    "ID:%d Patient:%d Time:%s Dur:%d Status:%s Reason:%s%n",
                    a.getId(),
                    a.getPatientId(),
                    a.getAppointmentTime(),
                    a.getDurationMinutes(),
                    a.getStatus(),
                    a.getReason()
            );
        }
    }

    private void listAppointmentsForDoctor() { viewDoctorSchedule(); }
}
