🏥 CureOne – Unified Healthcare Management System

CureOne is a Java-based desktop healthcare management system built using Swing, JDBC, and MySQL.
It provides a role-based unified platform for managing doctors, patients, appointments, pharmacy inventory, billing, and medical records.

🚀 Features Overview
👤 Role-Based Access

Each user logs in with a role and is redirected to a dedicated dashboard:

Admin – Full system control

Doctor – Appointments & medical records

Patient – Book appointments & manage profile

Pharmacist – Inventory & billing

Customer – Browse pharmacy (no login required)

🧱 System Architecture

CureOne follows a Layered (MVC-inspired) Architecture:

GUI Layer (Swing)
│
├── Service Layer (Business Logic)
│
├── Repository Layer (JDBC / SQL)
│
└── MySQL Database

Architecture Responsibilities
Layer	Responsibility
GUI	User interaction & dashboards
Service	Validation, business rules
Repository	Database access via JDBC
Database	Persistent data storage
🗂️ Project Structure
com.cureone
│
├── auth
│   ├── User, AuthService
│   ├── UserRepository, JdbcUserRepository
│
├── gui
│   ├── MainFrame, GUIContext
│   ├── dashboards (Admin, Doctor, Patient, Pharmacist)
│   ├── panels (AppointmentPanel, PatientPanel, etc.)
│
├── appointmentsandscheduling
│   ├── model (Appointment, Doctor)
│   ├── service (AppointmentService, DoctorService)
│   ├── repository (JDBC Repositories)
│
├── patientsandrecords
│   ├── model (Patient)
│   ├── service (PatientService)
│   ├── repository (JdbcPatientRepository)
│
├── pharmacyandinventory
│   ├── medicines, stock, billing
│
├── common
│   ├── DBUtil, Result
│
└── Main.java

🔐 Authentication & User Linking
🔑 Users Table

All system users are stored in a single table:

users (
id,
username,
password_hash,
role,
linked_id
)

🔗 linked_id Concept (CORE DESIGN)

linked_id connects a user to a domain entity

Example:

Doctor user → linked_id = doctors.id

Patient user → linked_id = patients.id

This allows:

One login system

Clean role separation

Zero duplication of user logic

🧑‍⚕️ Doctor Module
Doctor Capabilities

View assigned appointments

View patient medical history

Access records securely

Doctor Resolution Logic
int doctorId = loggedInUser.getLinkedId();
Doctor d = doctorService.getDoctorById(doctorId);

🧑‍🦱 Patient Module
Patient Capabilities

Book appointments

View appointment history

Update profile

View medical records

Patient Resolution Logic
int patientId = loggedInUser.getLinkedId();
Patient p = patientService.getPatientById(patientId);

📅 Appointment Management

Centralized scheduling system

Role-aware access:

Doctor → appointments assigned to doctor

Patient → own appointments

Status tracking: Pending, Approved, Completed

💊 Pharmacy & Inventory
Features

Medicine catalog

Stock management

Invoice & billing system

Customer checkout

🧠 GUI Context (Shared State)

GUIContext acts as a central registry:

public class GUIContext {
public static User loggedInUser;
public static AuthService authService;
public static DoctorService doctorService;
public static PatientService patientService;
...
}


Purpose:

Share services

Track logged-in user

Avoid tight coupling between screens

🗄️ Database Design
Core Tables

users

doctors

patients

appointments

medical_records

medicines

inventory_items

invoices

Relational integrity maintained using foreign keys & linked IDs.

🛠️ Technologies Used
Technology	Purpose
Java (JDK 17+)	Core language
Swing	Desktop GUI
JDBC	Database connectivity
MySQL	Relational database
IntelliJ IDEA	Development
MVC Pattern	Clean architecture
▶️ How to Run

Clone repository

Import project in IntelliJ

Configure MySQL and update DBUtil

Add MySQL Connector JAR

Run Main.java

📌 Highlights

Clean separation of concerns

Role-based dashboards

Central authentication

Scalable architecture

Academic + real-world design


📐 Diagrams & Design – CureOne
🧱 1. System Architecture Diagram
High-Level Architecture (Layered Design)
+--------------------------------------------------+
|                  GUI LAYER                       |
|--------------------------------------------------|
|  LoginPanel | Dashboards | Panels | Forms        |
|  (Swing UI)                                       |
+------------------------▲-------------------------+
|
|
+------------------------|-------------------------+
|               SERVICE LAYER                      |
|--------------------------------------------------|
| AuthService | DoctorService | PatientService     |
| AppointmentService | Pharmacy | Billing          |
| (Business Logic, Validation)                     |
+------------------------▲-------------------------+
|
|
+------------------------|-------------------------+
|            REPOSITORY / DAO LAYER                |
|--------------------------------------------------|
| JdbcUserRepository | JdbcDoctorRepository        |
| JdbcPatientRepository | AppointmentRepository   |
| InventoryRepository | InvoiceRepository         |
| (JDBC + SQL Queries)                             |
+------------------------▲-------------------------+
|
|
+------------------------|-------------------------+
|                 DATABASE LAYER                   |
|--------------------------------------------------|
| MySQL (users, doctors, patients, appointments,  |
| medical_records, medicines, inventory, invoices)|
+--------------------------------------------------+

Why This Architecture?

✅ Clean separation of concerns

✅ Easy debugging & maintenance

✅ Real-world enterprise style

✅ Scalable for future features

🔄 2. Authentication & Role Flow Diagram
Login → Role Resolution → Dashboard Routing
User
│
│  enters username & password
▼
LoginPanel
│
│ calls
▼
AuthService.login()
│
│ fetch user from DB
▼
UserRepository (users table)
│
│ returns User(id, role, linked_id)
▼
GUIContext.loggedInUser
│
│ role-based routing
▼
MainFrame
├── ADMIN       → AdminDashboard
├── DOCTOR      → DoctorDashboard
├── PATIENT     → PatientDashboard
├── PHARMACIST  → PharmacistDashboard
└── CUSTOMER    → CustomerDashboard

🔗 linked_id Concept (Critical)
users
├── id
├── role
└── linked_id
│
├── doctors.id   (if role = DOCTOR)
└── patients.id  (if role = PATIENT)


✔ Single login system
✔ Zero duplication
✔ Clean mapping between users & domain entities

🗄️ 3. ER Diagram (Database Design)
Entity Relationship Diagram 
+---------+        +-------------+
|  users  |        |   doctors   |
+---------+        +-------------+
| id (PK) |◄───────| id (PK)     |
| username|        | name        |
| password|        | specialization
| role    |        | phone       |
| linked_id ───────► user_id (FK)
+---------+        | email       |
+-------------+

+---------+        +-------------+
|  users  |        |  patients   |
+---------+        +-------------+
| id (PK) |◄───────| id (PK)     |
| role    |        | name        |
| linked_id ───────► age         |
+---------+        | gender      |
| contact     |
| disease     |
+-------------+

+----------------+      +----------------+
|  appointments  |◄────►|   doctors     |
+----------------+      +----------------+
| id (PK)        |      | id (PK)        |
| doctor_id (FK) |      +----------------+
| patient_id(FK) |
| date           |◄────►+----------------+
| time           |      |   patients     |
| status         |      | id (PK)        |
| reason         |      +----------------+
+----------------+

+-------------------+
| medical_records   |
+-------------------+
| id (PK)           |
| appointment_id(FK)|
| diagnosis         |
| notes             |
+-------------------+

+------------------+     +-------------------+
| medicines        |◄───►| inventory_items  |
+------------------+     +-------------------+
| id (PK)          |     | id (PK)           |
| name             |     | medicine_id (FK)  |
| category_id(FK)  |     | quantity          |
| price            |     | expiry_date       |
+------------------+     +-------------------+

+------------------+     +------------------+
| invoices         |◄───►| invoice_items   |
+------------------+     +------------------+
| id (PK)          |     | id (PK)          |
| pharmacist_id    |     | invoice_id (FK) |
| customer_name    |     | medicine_id(FK) |
| total_amount     |     | quantity        |
| created_at       |     | line_total      |
+------------------+     +------------------+

🧠 Design Decisions 

Single users table for authentication

linked_id avoids duplicating user data

Domain tables (doctors, patients) stay clean

Appointments act as a central junction

Inventory & billing are modular and independent

📌 Why This Project Stands Out

✔ Real-world healthcare workflow

✔ Proper role-based access control

✔ Clean JDBC usage (no ORM magic)

✔ GUI + backend fully integrated

✔ University + industry-ready design


👨‍💻 Author

Akif
Software Engineering Student
Project: CureOne – Unified Healthcare Management System



LinkedIn  : https://www.linkedin.com/in/akif-naveed-malik30
Email     : hello.akif_naveed@gmail.com
Youtube   : https://youtube.com/@notestocode?si=eO0qF54cpBgT8iNv
Instagram : https://www.instagram.com/notestocode?igsh=MWxqOG91bmtka3dwbg==