# Appointments & Scheduling Module

This module handles all appointment-related operations in the CureOne system.  
It acts as the **core interaction layer between Doctors and Patients**.

---

## 📌 Responsibilities
- Booking appointments
- Viewing appointments (Doctor / Patient)
- Managing appointment status
- Fetching appointment history

---

## 🧩 Key Components

### Models
- `Appointment`
    - Stores appointment date, time, duration, reason, status
    - Links doctors and patients

### Services
- `AppointmentService`
    - Validates appointment rules
    - Routes booking logic
    - Fetches appointments by role

### Repositories
- `AppointmentRepository`
- `JdbcAppointmentRepository`
    - Executes SQL queries using JDBC

---

## 🔗 Relationships
- Appointment → Doctor (many-to-one)
- Appointment → Patient (many-to-one)

---

## ⚙️ Used By
- Doctor Dashboard
- Patient Dashboard
- Medical Records Module
