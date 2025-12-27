package com.cureone.pharmacyandinventory.repository;

import com.cureone.pharmacyandinventory.model.Medicine;
import java.util.List;

public interface MedicineRepository {

    void save(Medicine medicine);

    List<Medicine> findAll();

    Medicine findById(int id);

    boolean update(Medicine medicine);

    boolean delete(int id);

    boolean existsById(int id);


    List<Medicine> findByName(String name);

    List<Medicine> findExpiredMedicines();
    public void updateQuantity(int medicineId, int qty);
}
