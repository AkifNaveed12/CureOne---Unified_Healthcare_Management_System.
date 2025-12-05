package com.cureone.pharmacyandinventory.repository;

import com.cureone.common.IdGenerator;
import com.cureone.pharmacyandinventory.model.Medicine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InMemoryMedicineRepository implements MedicineRepository {

    private final List<Medicine> medicines = new ArrayList<>();

    @Override
    public void save(Medicine medicine) {
        if (medicine == null) {
            throw new IllegalArgumentException("Medicine cannot be null");
        }

        // auto-generate ID only if it is not assigned already
        if (medicine.getId() == 0) {
            medicine.setId(IdGenerator.generateId());
        }

        medicines.add(medicine);
    }

    @Override
    public List<Medicine> findAll() {
        return new ArrayList<>(medicines);
    }

    @Override
    public Medicine findById(int id) {
        for (Medicine m : medicines) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    @Override
    public boolean update(Medicine medicine) {
        if (medicine == null || medicine.getId() == 0) return false;

        for (int i = 0; i < medicines.size(); i++) {
            if (medicines.get(i).getId() == medicine.getId()) {
                medicines.set(i, medicine);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        return medicines.removeIf(m -> m.getId() == id);
    }

    @Override
    public boolean existsById(int id) {
        return findById(id) != null;
    }

    @Override
    public List<Medicine> findByName(String name) {
        List<Medicine> result = new ArrayList<>();
        if (name == null) return result;

        for (Medicine m : medicines) {
            if (m.getName().equalsIgnoreCase(name)) {
                result.add(m);
            }
        }

        return result;
    }

    @Override
    public List<Medicine> findExpiredMedicines() {
        List<Medicine> expired = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Medicine m : medicines) {
            if (m.getExpiryDate().isBefore(today)) {
                expired.add(m);
            }
        }

        return expired;
    }
}
