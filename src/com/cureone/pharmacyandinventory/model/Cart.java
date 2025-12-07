package com.cureone.pharmacyandinventory.model;

import com.cureone.pharmacyandinventory.model.Medicine;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() { return new ArrayList<>(items); }

    public void addItem(CartItem item) {
        // if same medicine exists, increase qty
        for (CartItem it : items) {
            if (it.getMedicineId() == item.getMedicineId()) {
                it.setQuantity(it.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public boolean removeItem(int medicineId) {
        return items.removeIf(i -> i.getMedicineId() == medicineId);
    }

    public double getTotal() {
        double t = 0;
        for (CartItem it : items) t += it.lineTotal();
        return t;
    }

    public void clear() { items.clear(); }

    public boolean isEmpty() { return items.isEmpty(); }

    @Override
    public String toString() {
        if (items.isEmpty()) return "(cart empty)";
        StringBuilder sb = new StringBuilder();
        for (CartItem it : items) {
            sb.append(it).append("\n");
        }
        sb.append("Total: ").append(getTotal());
        return sb.toString();
    }
}
