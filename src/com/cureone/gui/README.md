# GUI Module

This module contains the **entire Swing-based user interface** of the CureOne system.

---

## 📌 Responsibilities
- User interaction
- Screen navigation
- Role-based dashboard rendering

---

## 🧩 Key Components

### Core
- `MainFrame`
    - Central window
    - Uses CardLayout for navigation

- `GUIContext`
    - Stores shared services
    - Stores logged-in user

### Panels
- LoginPanel
- SignupPanel
- AdminDashboard
- DoctorDashboard
- PatientDashboard
- PharmacistDashboard
- CustomerDashboard

### Utilities
- NavigationUtil
    - Handles back navigation by role

---

## 🎯 Design Pattern
- MVC-inspired structure
- GUI only talks to Services
- No direct DB access

---

## ⚙️ Used By
- End users
- All application workflows
