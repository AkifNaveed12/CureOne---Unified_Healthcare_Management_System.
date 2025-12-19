package com.cureone.appointmentsandscheduling.model;


import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private int durationMinutes;
    private String reason;
    private String status; // SCHEDULED, COMPLETED, CANCELLED

    public Appointment() {}
    public Appointment(int id, int patientId, int doctorId, LocalDate appointmentDate, LocalTime appointmentTime, int durationMinutes,String status, String reason) {}

    // getters / setters
    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public int getPatientId(){return patientId;}
    public void setPatientId(int patientId){this.patientId = patientId;}
    public int getDoctorId(){return doctorId;}
    public void setDoctorId(int doctorId){this.doctorId = doctorId;}
    public LocalDate getAppointmentDate(){return appointmentDate;}
    public void setAppointmentDate(LocalDate appointmentDate){this.appointmentDate = appointmentDate;}
    public LocalTime getAppointmentTime(){return appointmentTime;}
    public void setAppointmentTime(LocalTime appointmentTime){this.appointmentTime = appointmentTime;}
    public int getDurationMinutes(){return durationMinutes;}
    public void setDurationMinutes(int durationMinutes){this.durationMinutes = durationMinutes;}
    public String getReason(){return reason;}
    public void setReason(String reason){this.reason = reason;}
    public String getStatus(){return status;}
    public void setStatus(String status){this.status = status;}
    @Override
    public String toString() {
        return "ID:" + id +
                " Patient:" + patientId +
                " Doctor:" + doctorId +
                " Date:" + appointmentDate +
                " Time:" + appointmentTime +
                " Duration:" + durationMinutes +
                " Status:" + status +
                " Reason:" + reason;
    }

}
