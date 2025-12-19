package com.cureone.appointmentsandscheduling.repository;

import com.cureone.appointmentsandscheduling.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository {
    int save(Appointment appointment);
    boolean update(Appointment appointment);
    boolean delete(int id);
    Appointment findById(int id);
    List<Appointment> findByDoctorAndDate(int doctorId, LocalDate date);
    boolean existsByDoctorAndTime(int doctorId, LocalDate date, LocalTime time); // time as "HH:MM:SS" or use LocalTime.
    List<Appointment> findByPatient(int patientId);
    List<Appointment> findByPatientAndDate(int patientId, LocalDate date);
    List<Appointment> findAll();

    List<Appointment> findByDoctor(int doctorId);

    List<Appointment> findByDoctorOrPatient(int id);

}
