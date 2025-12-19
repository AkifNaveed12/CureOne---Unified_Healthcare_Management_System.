INSERT INTO medical_records (patientID, diagnosis, treatment, doctorName, recordDate) VALUES
(1, 'Mild Fever and Cold', 'Paracetamol 500mg thrice daily for 5 days', 'Dr. Ahmed Raza', '2025-10-01'),
(2, 'Type 2 Diabetes', 'Metformin 500mg twice daily, lifestyle modifications', 'Dr. Usman Khalid', '2025-09-15'),
(3, 'Skin Rash and Allergies', 'Cetirizine 10mg once daily, avoid allergens', 'Dr. Ayesha Noor', '2025-08-20'),
(4, 'Lower Back Pain', 'Physiotherapy sessions + Ibuprofen 400mg as needed', 'Dr. Ali Hassan', '2025-07-30'),
(5, 'Migraine Episodes', 'Sumatriptan 50mg as needed, avoid triggers', 'Dr. Sana Iqbal', '2025-06-25'),
(6, 'Hypertension', 'Amlodipine 5mg daily, low-sodium diet', 'Dr. Bilal Sheikh', '2025-05-18'),
(7, 'Asthma', 'Salbutamol inhaler 2 puffs as needed', 'Dr. Farhan Malik', '2025-04-10'),
(8, 'Arthritis', 'Naproxen 250mg twice daily, physiotherapy', 'Dr. Hina Shah', '2025-03-05'),
(9, 'Seasonal Flu', 'Oseltamivir 75mg twice daily for 5 days', 'Dr. Kamran Akram', '2025-02-22'),
(10, 'Headache due to stress', 'Paracetamol 500mg as needed, stress management', 'Dr. Noman Saeed', '2025-01-18'),
(11, 'Diabetes and high BP', 'Metformin 500mg, Amlodipine 5mg, regular checkup', 'Dr. Imran Qureshi', '2025-01-05'),
(12, 'Skin Allergy', 'Topical Hydrocortisone 1%, antihistamines', 'Dr. Rabia Aslam', '2024-12-20');


INSERT INTO prescriptions (appointment_id, doctor_id, patient_id, notes) VALUES
(NULL, 1, 1, 'Prescription for mild fever and cold'),
(NULL, 2, 2, 'Prescription for diabetes management'),
(NULL, 3, 3, 'Prescription for skin rash'),
(NULL, 4, 4, 'Prescription for back pain'),
(NULL, 5, 5, 'Prescription for migraine'),
(NULL, 6, 6, 'Prescription for hypertension'),
(NULL, 7, 7, 'Prescription for asthma'),
(NULL, 8, 8, 'Prescription for arthritis'),
(NULL, 9, 9, 'Prescription for flu treatment'),
(NULL, 10, 10, 'Prescription for stress headaches'),
(NULL, 11, 11, 'Prescription for diabetes and BP'),
(NULL, 12, 12, 'Prescription for skin allergy');


INSERT INTO prescription_items (prescription_id, medicine_id, quantity, unit_price) VALUES
(1, 5, 10, 60),   -- Paracetamol
(2, 7, 60, 850),  -- Metformin
(3, 15, 20, 120), -- Cetirizine
(4, 2, 15, 120),  -- Ibuprofen
(5, 28, 10, 250), -- Sumatriptan
(6, 20, 30, 500), -- Amlodipine (dummy example)
(7, 8, 1, 250),   -- Salbutamol inhaler
(8, 6, 20, 90),   -- Naproxen
(9, 12, 10, 120), -- Oseltamivir
(10, 5, 10, 60),  -- Paracetamol
(11, 7, 60, 850), -- Metformin
(12, 15, 20, 120);-- Cetirizine


INSERT INTO invoices (invoice_number, patient_id, pharmacist_id, total_amount) VALUES
('INV-1001', 1, 1, 600),
('INV-1002', 2, 2, 51000),
('INV-1003', 3, 3, 2400),
('INV-1004', 4, 1, 1800),
('INV-1005', 5, 2, 2500),
('INV-1006', 6, 3, 15000),
('INV-1007', 7, 1, 2500),
('INV-1008', 8, 2, 1800),
('INV-1009', 9, 3, 1200),
('INV-1010', 10, 1, 600),
('INV-1011', 11, 2, 85800),
('INV-1012', 12, 3, 2400);


INSERT INTO stock (medicine_id, quantity) VALUES
(1, 200),
(2, 150),
(3, 80),
(4, 100),
(5, 180),
(6, 300),
(7, 40),
(8, 60),
(9, 90),
(10, 50),
(11, 120),
(12, 100);

