package com.cureone.pharmacyandinventory.repository;

import com.cureone.common.IdGenerator;
import com.cureone.pharmacyandinventory.model.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InMemoryBillingRepository implements BillingRepository {

    private final List<Invoice> invoices = new ArrayList<>();

    @Override
    public void save(Invoice invoice) {
        if (invoice == null) throw new IllegalArgumentException("invoice null");
        if (invoice.getId() == 0) invoice.setId(IdGenerator.generateId());
        invoices.add(invoice);
    }

    @Override
    public List<Invoice> findAll() {
        return new ArrayList<>(invoices);
    }

    @Override
    public Invoice findById(int id) {
        for (Invoice inv : invoices) if (inv.getId() == id) return inv;
        return null;
    }
}
