package com.cureone.pharmacyandinventory.service;

import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.Cart;
import com.cureone.pharmacyandinventory.model.Invoice;

import java.util.List;

public interface BillingService {
    Result<String> addToCart(Cart cart, int medicineId, int qty); // returns message / errors
    Result<Invoice> checkout(Cart cart, String customerName, int pharmacistId);
    List<Invoice> getAllInvoices();

    Invoice getInvoiceById(int id);
    Invoice getInvoiceByNumber(String invoiceNumber);

}
