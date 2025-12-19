# Patients & Medical Records Module

This module manages **patient profiles and medical history**.

---

## 📌 Responsibilities
- Patient profile management
- Medical record viewing
- Linking patients with appointments

---

## 🧩 Key Components

### Models
- `Patient`
    - Personal and medical details

### Services
- `PatientService`
    - Business logic for patients
    - Validates profile updates

### Repositories
- `InterfacePatientRepository`
- `JdbcPatientRepository`
    - Handles patient persistence via JDBC

---

## 🔗 Relationships
- Patient → Appointments
- Patient → Medical Records

---

## ⚙️ Used By
- Patient Dashboard
- Admin Dashboard
- Appointment Booking
