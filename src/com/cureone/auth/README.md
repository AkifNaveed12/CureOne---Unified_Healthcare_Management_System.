# Authentication Module

The Authentication module provides **secure login and role-based access** for the CureOne system.

---

## 📌 Responsibilities
- User authentication (login)
- Role management (ADMIN, DOCTOR, PATIENT, PHARMACIST)
- Linking users to domain entities using `linked_id`

---

## 🧩 Key Components

### Models
- `User`
    - id
    - username
    - password_hash
    - role
    - linked_id

### Services
- `AuthService`
    - Validates credentials
    - Loads linked_id for dashboard resolution

### Repositories
- `UserRepository`
- `JdbcUserRepository`
    - Fetches users from MySQL
    - Manages signup & login persistence

---

## 🔑 Key Design
- **Single users table**
- `linked_id` maps users to doctors or patients
- Prevents data duplication

---

## ⚙️ Used By
- LoginPanel
- GUIContext
- All Dashboards
