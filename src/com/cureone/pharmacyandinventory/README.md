# Pharmacy & Inventory Module

This module handles **medicine inventory, billing, and pharmacy operations**.

---

## 📌 Responsibilities
- Medicine management
- Inventory tracking
- Invoice generation
- Billing calculations

---

## 🧩 Key Components

### Models
- Medicine
- InventoryItem
- Invoice
- InvoiceItem

### Services
- MedicineService
- InventoryService
- BillingService
- CategoryService

### Repositories
- JDBC repositories for medicines, stock, and invoices

---

## 🔗 Relationships
- Medicine → InventoryItem
- Invoice → InvoiceItems
- Pharmacy → Billing

---

## ⚙️ Used By
- Pharmacist Dashboard
- Customer Purchase Flow
