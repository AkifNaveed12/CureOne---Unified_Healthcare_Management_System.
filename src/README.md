CureOne – Unified Healthcare Management System
Overview

CureOne is a modular healthcare management system designed to unify pharmacy inventory, appointment scheduling, patient records, and user authentication with billing. It is implemented in Java using OOP principles and stores data using arrays or text files. The system is structured for clarity, maintainability, and modular collaboration among team members.

Features

Pharmacy & Inventory – Manage medicines, categories, and inventory items.

Appointments & Scheduling – Schedule, update, and cancel patient appointments with doctors.

Patients & Records – Register patients and maintain their medical history and records.

Users & Authentication – Manage users, roles, and login functionality.

Billing & Reports – Generate invoices and track payments (module integrated with pharmacy).

Project Architecture
Package Structure
com.cureone
├─ pharmacyinventory (Akif)
│    ├─ model
│    ├─ service
│    ├─ controller
│    └─ repository
├─ appointments (Haider)
│    ├─ model
│    ├─ service
│    ├─ controller
│    └─ repository
├─ patients (Hamza)
│    ├─ model
│    ├─ service
│    ├─ controller
│    └─ repository
└─ users (Hammad)
├    |─ model
├    |─ service
├    |─ controller
└    |─ repository
└─ common
└    |─ Result<T>

Example Classes
Module	Model Classes	Service Classes	Repository Classes	Controller Classes
Pharmacy & Inventory	Medicine, InventoryItem, Category	MedicineService, InventoryService	MedicineRepository, InventoryRepository	MedicineController, InventoryController
Appointments & Scheduling	Appointment, Doctor, Timeslot	AppointmentService	AppointmentRepository	AppointmentController
Patients & Records	Patient, MedicalRecord	PatientService	PatientRepository	PatientController
Users & Authentication	User, Role, Invoice	UserService, BillingService	UserRepository, BillingRepository	UserController, BillingController
Workflow Overview

Controller – Receives input from the user/UI.

Service – Validates and processes data according to business rules.

Repository – Stores and retrieves data from arrays or text files.

Model – Represents actual data objects.

Result<T> – Utility class used to return operation outcomes (success/failure and messages).

Example Flow:

User → Controller → Service → Repository → Model → Result<T> → Service → Controller → User

How to Run the Project

Clone Repository

git clone <your-repo-url>
cd CureOne


Open in IntelliJ IDEA

Open existing project from folder

Make sure Git is enabled in IntelliJ

Compile & Run

Use Main.java (or module-specific runners for testing)

Execute via IntelliJ or terminal

Branching & Collaboration

Each team member works on their module branch:

git checkout -b feature/<module-name>


Commit changes and push:

git add .
git commit -m "Implemented skeleton of module X"
git push origin feature/<module-name>

Future Enhancements

Integrate database support (MySQL/PostgreSQL) instead of arrays/text files.

Add GUI interface (Swing/JavaFX).

Implement encryption & secure authentication.

Generate PDF/Excel reports for billing and inventory.

Add REST APIs for integration with external applications.

Contributors

Akif Naveed Malik – Pharmacy & Inventory Module

Haider – Appointments & Scheduling Module

Hamza – Patients & Records Module

Hammad – Users, Authentication & Billing Module