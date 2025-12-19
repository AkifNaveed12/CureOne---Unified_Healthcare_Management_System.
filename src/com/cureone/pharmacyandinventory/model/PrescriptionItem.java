package com.cureone.pharmacyandinventory.model;

public class PrescriptionItem {
    private int medicineId;
    private int quantity;

    public PrescriptionItem() {}
    public PrescriptionItem(int medicineId, int quantity) {
        this.medicineId = medicineId;
        this.quantity = quantity;
    }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
