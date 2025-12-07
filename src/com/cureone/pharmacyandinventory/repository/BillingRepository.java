package com.cureone.pharmacyandinventory.repository;

import com.cureone.pharmacyandinventory.model.Invoice;
import java.util.List;

public interface BillingRepository {
    void save(Invoice invoice);
    List<Invoice> findAll();
    Invoice findById(int id);
}
