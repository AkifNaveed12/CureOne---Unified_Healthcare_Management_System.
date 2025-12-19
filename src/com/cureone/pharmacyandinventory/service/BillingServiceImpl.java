package com.cureone.pharmacyandinventory.service;

import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.Cart;
import com.cureone.pharmacyandinventory.model.CartItem;
import com.cureone.pharmacyandinventory.model.Invoice;
import com.cureone.pharmacyandinventory.model.InventoryItem;
import com.cureone.pharmacyandinventory.model.Medicine;
import com.cureone.pharmacyandinventory.repository.BillingRepository;
import com.cureone.pharmacyandinventory.repository.InventoryRepository;
import com.cureone.pharmacyandinventory.repository.MedicineRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BillingServiceImpl implements BillingService {

    private final InventoryRepository inventoryRepo;
    private final MedicineRepository medicineRepo;
    private final BillingRepository billingRepo;

    public BillingServiceImpl(InventoryRepository inventoryRepo,
                              MedicineRepository medicineRepo,
                              BillingRepository billingRepo) {
        this.inventoryRepo = inventoryRepo;
        this.medicineRepo = medicineRepo;
        this.billingRepo = billingRepo;
    }

    @Override
    public Result<String> addToCart(Cart cart, int medicineId, int qty) {
        if (qty <= 0) return new Result<>(false, "Quantity must be > 0");
        Medicine m = medicineRepo.findById(medicineId);
        if (m == null) return new Result<>(false, "Medicine not found");
        // check available total qty
        int totalAvailable = 0;
        List<InventoryItem> inv = inventoryRepo.findByMedicineId(medicineId);
        for (InventoryItem it : inv) totalAvailable += it.getQuantity();

        if (totalAvailable < qty) {
            return new Result<>(false, "Only " + totalAvailable + " units available");
        }
        // add to cart (use medicine price)
        CartItem c = new CartItem(medicineId, m.getName(), qty, m.getPrice());
        cart.addItem(c);
        return new Result<>(true, "Added to cart", c.getMedicineName());
    }

    @Override
    public Result<Invoice> checkout(Cart cart, String customerName, int pharmacistId) {
        if (cart == null || cart.isEmpty()) return new Result<>(false, "Cart is empty");
        // 1. Validate availability for all items
        for (CartItem ci : cart.getItems()) {
            int avail = 0;
            List<InventoryItem> inv = inventoryRepo.findByMedicineId(ci.getMedicineId());
            for (InventoryItem it : inv) avail += it.getQuantity();
            if (avail < ci.getQuantity()) {
                return new Result<>(false, "Insufficient stock for " + ci.getMedicineName() + ". Available: " + avail);
            }
        }
        // 2. Reduce inventory (FIFO by expiryDate ascending)
        for (CartItem ci : cart.getItems()) {
            int remaining = ci.getQuantity();
            List<InventoryItem> inv = inventoryRepo.findByMedicineId(ci.getMedicineId());
            // sort by expiry ascending so earliest expiry used first
            inv.sort(Comparator.comparing(InventoryItem::getExpiryDate));
            for (InventoryItem lot : inv) {
                if (remaining <= 0) break;
                if (lot.getQuantity() <= 0) continue;
                int reduce = Math.min(lot.getQuantity(), remaining);
                lot.setQuantity(lot.getQuantity() - reduce);
                inventoryRepo.update(lot);
                remaining -= reduce;
            }
        }
        // 3. Create invoice
        Invoice invObj = new Invoice();
        invObj.setInvoiceNumber("INV-" + System.currentTimeMillis());
        invObj.setPharmacistId(pharmacistId);
        invObj.setPatientId(0);
        invObj.setCreatedAt(LocalDateTime.now());
        invObj.setItems(new ArrayList<>(cart.getItems()));
        invObj.setTotalAmount(cart.getTotal());
        billingRepo.save(invObj);
        // 4. clear cart
        cart.clear();
        return new Result<>(true, "Checkout successful", invObj);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return billingRepo.findAll();
    }

    @Override
    public Invoice getInvoiceById(int id) {
        return billingRepo.findById(id);
    }

    @Override
    public Invoice getInvoiceByNumber(String invoiceNumber) {
        if (invoiceNumber == null) return null;
        List<Invoice> all = billingRepo.findAll();
        for (Invoice inv : all) {
            if (invoiceNumber.equals(inv.getInvoiceNumber())) return inv;
        }
        return null;
    }


}
