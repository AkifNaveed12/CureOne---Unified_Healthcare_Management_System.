package com.cureone.appointmentsandscheduling.controller;

import com.cureone.appointmentsandscheduling.model.Doctor;
import com.cureone.appointmentsandscheduling.service.DoctorService;

import java.util.List;
import java.util.Scanner;

public class DoctorController {

    private final DoctorService service;
    private final Scanner scanner = new Scanner(System.in);

    public DoctorController(DoctorService service) {
        this.service = service;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Doctor Management ---");
            System.out.println("1. Add doctor");
            System.out.println("2. List doctors");
            System.out.println("3. Find doctor by id");
            System.out.println("4. Update doctor profile");
            System.out.println("5. Delete doctor");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int ch = Integer.parseInt(scanner.nextLine());
            switch (ch) {
                case 1 -> addDoctor();
                case 2 -> listDoctors();
                case 3 -> findDoctor();
                case 4 -> updateDoctor();
                case 5 -> deleteDoctor();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void addDoctor() {
        Doctor d = new Doctor();
        System.out.print("Name: "); d.setName(scanner.nextLine());
        System.out.print("Specialization: "); d.setSpecialization(scanner.nextLine());
        System.out.print("Phone: "); d.setPhone(scanner.nextLine());
        System.out.print("Email: "); d.setEmail(scanner.nextLine());

        int id = service.addDoctor(d);
        System.out.println("Saved doctor with id=" + id);
    }

    private void listDoctors() {
        List<Doctor> list = service.getAllDoctors();
        if (list.isEmpty()) System.out.println("(none)");
        for (Doctor d : list) {
            System.out.printf("ID:%d | %s | %s | %s%n",
                    d.getId(), d.getName(), d.getSpecialization(), d.getPhone());
        }
    }

    private void findDoctor() {
        System.out.print("Enter id: ");
        int id = Integer.parseInt(scanner.nextLine());
        Doctor d = service.getDoctorById(id);

        if (d == null) System.out.println("Not found");
        else System.out.printf("ID:%d Name:%s Spec:%s Phone:%s Email:%s%n",
                d.getId(), d.getName(), d.getSpecialization(), d.getPhone(), d.getEmail());
    }

    private void updateDoctor() {
        System.out.print("Doctor id: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Add specialization (optional): ");
        String spec = scanner.nextLine();

        System.out.print("Phone (optional): ");
        String phone = scanner.nextLine();

        System.out.print("Email (optional): ");
        String email = scanner.nextLine();

        boolean ok = service.updateDoctorProfile(id, spec, phone, email);
        System.out.println(ok ? "Updated" : "Update failed");
    }

    private void deleteDoctor() {
        System.out.print("Enter id: ");
        int id = Integer.parseInt(scanner.nextLine());
        boolean ok = service.deleteDoctor(id);
        System.out.println(ok ? "Deleted" : "Not found");
    }
}
