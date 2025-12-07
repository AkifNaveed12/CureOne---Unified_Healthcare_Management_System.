package com.cureone.pharmacyandinventory.model;

public class CartItem {
    private int medicineId;
    private String medicineName;
    private int quantity;
    private double unitPrice;

    public CartItem() {}

    public CartItem(int medicineId, String medicineName, int quantity, double unitPrice) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double lineTotal() { return unitPrice * quantity; }

    @Override
    public String toString() {
        return medicineName + " (id:" + medicineId + ") x" + quantity + " @ " + unitPrice + " = " + lineTotal();
    }
}
