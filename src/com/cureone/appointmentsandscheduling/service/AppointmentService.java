package com.cureone.appointmentsandscheduling.service;

import com.cureone.appointmentsandscheduling.model.Appointment;
import com.cureone.appointmentsandscheduling.repository.AppointmentRepository;
import com.cureone.common.Result;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentService {

    private final AppointmentRepository repo;

    public AppointmentService(AppointmentRepository repo) {
        this.repo = repo;
    }

    // ---------------- BOOK ----------------
    public Result<Appointment> bookAppointment(
            int patientId,
            int doctorId,
            LocalDate date,
            String time,
            int duration,
            String reason
    ) {

        if (duration <= 0) {
            return new Result<>(false, "Duration must be > 0");
        }

        boolean clash = repo.existsByDoctorAndTime(doctorId, date, LocalTime.parse(time));
        if (clash) {
            return new Result<>(false, "Doctor already has appointment at this time");
        }

        Appointment a = new Appointment();
        a.setPatientId(patientId);
        a.setDoctorId(doctorId);
        a.setAppointmentDate(date);
        a.setAppointmentTime(LocalTime.parse(time));
        a.setDurationMinutes(duration);
        a.setReason(reason);
        a.setStatus("SCHEDULED");

        int id = repo.save(a);
        a.setId(id);

        return new Result<>(true, "Appointment booked", a);
    }


    // ---------------- UPDATE ----------------
    public Result<Appointment> updateAppointment(Appointment appointment) {

        boolean updated = repo.update(appointment);

        if (!updated) {
            return new Result<>(false, "Appointment not found");
        }

        return new Result<>(true, "Appointment updated", appointment);
    }

    // ---------------- DELETE ----------------
    public Result<Boolean> deleteAppointment(int id) {

        Appointment existing = repo.findById(id);
        if (existing == null) {
            return new Result<>(false, "Appointment not found");
        }

        boolean deleted = repo.delete(id);
        return new Result<>(true, "Appointment deleted", deleted);
    }

    // ---------------- FIND ----------------
    public Appointment getAppointmentById(int id) {
        return repo.findById(id);
    }

    // ---------------- LIST ----------------
    public List<Appointment> getDoctorSchedule(int doctorId, LocalDate date) {
        return repo.findByDoctorAndDate(doctorId, date);
    }
    // ---------------- PATIENT VIEWS ----------------
    public List<Appointment> getAppointmentsForPatient(int patientId) {
        return repo.findByPatient(patientId);
    }

    public List<Appointment> getAppointmentsForPatientByDate(int patientId, LocalDate date) {
        return repo.findByPatientAndDate(patientId, date);
    }

    // ================= ADMIN =================
    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    // ================= DOCTOR =================
    public List<Appointment> getAppointmentsForDoctor(int doctorId) {
        return repo.findByDoctor(doctorId);
    }

    public List<Appointment> searchByDoctorOrPatient(int id) {
        // Admin search: match doctorId OR patientId
        return repo.findByDoctorOrPatient(id);
    }


}
