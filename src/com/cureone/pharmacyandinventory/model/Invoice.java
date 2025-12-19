package com.cureone.pharmacyandinventory.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int id;
    private String invoiceNumber;
    private String customerName;
    private int pharmacistId;
    private LocalDateTime createdAt;
    private List<CartItem> items = new ArrayList<>();
    private double totalAmount;
    private String notes;

    private int patientId;


    public Invoice() {}

    public Invoice(int id, String invoiceNumber, String customerName, int pharmacistId, LocalDateTime createdAt, List<CartItem> items, double totalAmount, String notes) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.customerName = customerName;
        this.pharmacistId = pharmacistId;
        this.createdAt = createdAt;
        this.items = items;
        this.totalAmount = totalAmount;
        this.notes = notes;
    }
    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public int getPharmacistId() { return pharmacistId; }
    public void setPharmacistId(int pharmacistId) { this.pharmacistId = pharmacistId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice: ").append(invoiceNumber).append(" | ").append(createdAt).append("\n");
        sb.append("Customer: ").append(customerName).append("\n");
        for (CartItem it : items) sb.append("  ").append(it).append("\n");
        sb.append("Total: ").append(totalAmount).append("\n");
        sb.append("Served by (pharmacistId): ").append(pharmacistId).append("\n");
        return sb.toString();
    }
}
