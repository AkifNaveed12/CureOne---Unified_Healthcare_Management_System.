# Common Utilities Module

This module contains **shared utilities and helper classes** used across the entire CureOne application.

---

## 📌 Responsibilities
- Database connection handling
- Standard result wrapping
- Shared constants and helpers

---

## 🧩 Key Components

### Utilities
- `DBUtil`
    - Manages JDBC connections
    - Centralizes database configuration

### Helpers
- `Result<T>`
    - Standard response wrapper
    - Contains success flag, message, and data

---

## 🧠 Why This Module Exists
- Avoids duplicate code
- Keeps services clean
- Centralized error handling

---

## ⚙️ Used By
- All repositories
- All services
