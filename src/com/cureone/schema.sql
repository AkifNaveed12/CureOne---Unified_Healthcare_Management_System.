-- schema.sql (MySQL) - tailored to your current Java fields
CREATE DATABASE cureone_db;
USE cureone_db;

-- USERS / AUTH (simple)
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('PATIENT','DOCTOR','PHARMACIST','ADMIN','STAFF') NOT NULL,
  linked_id INT DEFAULT NULL, -- optional mapping to patient/doctor id
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PATIENT (Hamza's Patient fields)
CREATE TABLE patients (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  age INT,
  gender VARCHAR(50),
  country VARCHAR(100),
  contact VARCHAR(100),
  disease_description TEXT
);

-- MEDICAL RECORD (Hamza's MedicalRecord)
CREATE TABLE medical_records (
  recordID INT AUTO_INCREMENT PRIMARY KEY,
  patientID INT NOT NULL,
  diagnosis TEXT,
  treatment TEXT,
  doctorName VARCHAR(150),
  recordDate VARCHAR(50),
  CONSTRAINT fk_mr_patient FOREIGN KEY (patientID) REFERENCES patients(id) ON DELETE CASCADE
);

-- DOCTORS (new)
CREATE TABLE doctors (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  specialization VARCHAR(150),
  phone VARCHAR(50),
  email VARCHAR(150)
);

-- APPOINTMENTS (scheduling)
CREATE TABLE appointments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT NOT NULL,
  doctor_id INT NOT NULL,
  appointment_date DATE NOT NULL,
  appointment_time TIME NOT NULL,
  duration_minutes INT DEFAULT 30,
  reason VARCHAR(500),
  status ENUM('SCHEDULED','COMPLETED','CANCELLED') DEFAULT 'SCHEDULED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_app_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
  CONSTRAINT fk_app_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);

-- PHARMACY PART (already in your code: medicines, categories, inventory)
CREATE TABLE categories (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  description TEXT
);

CREATE TABLE medicines (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  manufacturer VARCHAR(200),
  expiry_date DATE,
  price DECIMAL(10,2),
  category_id INT,
  quantity INT DEFAULT 0,
  CONSTRAINT fk_med_cat FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- Inventory items (lots)
CREATE TABLE inventory_items (
  item_id INT AUTO_INCREMENT PRIMARY KEY,
  medicine_id INT NOT NULL,
  quantity INT NOT NULL,
  minimum_stock_limit INT DEFAULT 0,
  expiry_date DATE,
  location VARCHAR(200),
  unit_price DECIMAL(10,2),
  CONSTRAINT fk_inv_med FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE
);

-- STOCK aggregated (easier for quick transactional decrements)
CREATE TABLE stock (
  medicine_id INT PRIMARY KEY,
  quantity INT NOT NULL,
  CONSTRAINT fk_stock_med FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE
);

-- PRESCRIPTIONS: link appointment -> medicines
CREATE TABLE prescriptions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  appointment_id INT,
  doctor_id INT,
  patient_id INT,
  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_pres_app FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE SET NULL
);

CREATE TABLE prescription_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  prescription_id INT NOT NULL,
  medicine_id INT NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(10,2),
  CONSTRAINT fk_pi_pres FOREIGN KEY (prescription_id) REFERENCES prescriptions(id) ON DELETE CASCADE,
  CONSTRAINT fk_pi_med FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE
);

-- Simple invoices (pharmacy)
CREATE TABLE invoices (
  id INT AUTO_INCREMENT PRIMARY KEY,
  invoice_number VARCHAR(100) UNIQUE,
  patient_id INT,
  pharmacist_id INT,
  total_amount DECIMAL(12,2),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE invoice_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  invoice_id INT NOT NULL,
  medicine_id INT NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(10,2),
  line_total DECIMAL(12,2),
  CONSTRAINT fk_ii_inv FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE,
  CONSTRAINT fk_ii_med FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE CASCADE
);
