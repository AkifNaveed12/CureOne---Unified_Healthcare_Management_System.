package com.cureone.pharmacyandinventory.repository;
import java.util.List;
import com.cureone.pharmacyandinventory.model.InventoryItem;

public interface InventoryRepository {
    void save(InventoryItem item);
    List<InventoryItem> findAll();
    InventoryItem findById(int id);

    boolean delete(int id);
    boolean update(InventoryItem item);
    boolean existsById(int id);

    List<InventoryItem> findByMedicineId(int medicineId);
    List<InventoryItem> findByLowStockLimit(int threshold);
}
