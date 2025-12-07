package com.cureone.pharmacyandinventory.service;

import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.InventoryItem;
import com.cureone.pharmacyandinventory.model.Medicine;
import com.cureone.pharmacyandinventory.repository.InventoryRepository;
import com.cureone.pharmacyandinventory.repository.MedicineRepository;

import java.util.List;
import java.util.Objects;
public class InventoryServiceImpl implements InventoryServices{

    private final InventoryRepository inventoryRepo;
    private final MedicineRepository medicineRepo;

    public InventoryServiceImpl(InventoryRepository inventoryRepo, MedicineRepository medicineRepo) {
        this.inventoryRepo = inventoryRepo;
        this.medicineRepo = medicineRepo;
    }
    @Override
    public Result<InventoryItem> addInventoryItem(InventoryItem item) {
        if (item == null){
            return new Result<>(false, "Inventory Item is null");
        }
        Medicine m = item.getMedicine();
        if(m == null || m.getId() == 0 || medicineRepo.findById(m.getId()) == null){
            return new Result<>(false, "Invalid or Unknown Medicine. Please add medicine first");
        }
        if (item.getQuantity() < 0){
            return new Result<>(false, "Quantity must be > 0 ");
        }
        inventoryRepo.save(item);
        return new Result<>(true, "Inventory Item Saved", item);
    }

    @Override
    public InventoryItem getItemById(int id){
        return inventoryRepo.findById(id);
    }

    @Override
    public Result<InventoryItem> updateItem(InventoryItem item) {
        if (item == null || item.getItemId() == 0){
            return new Result<>(false, "Invalid Item");
        }
        boolean ok = inventoryRepo.update(item);
        if (!ok){
            return new Result<>(false, "Inventory Item not found");
        }
        return new Result<>(true, "Inventory Item Updated", item);
    }

    @Override
    public Result<Boolean> deleteItem(int id) {
        boolean ok = inventoryRepo.delete(id);
        if (!ok){
            return new Result<>(false, "Inventory Item not found");
        }
        return new Result<>(true, "Inventory Item Deleted",true);
    }

    @Override
    public List<InventoryItem> findByMedicineId(int id) {
        return inventoryRepo.findByMedicineId(id);
    }

    @Override
    public List<InventoryItem> findLowStockItems(int threshold){
        return inventoryRepo.findByLowStockLimit(threshold);
    }
    @Override
    public Result<Boolean> reduceStock(int itemId, int amount){
        InventoryItem item = inventoryRepo.findById(itemId);
        if (item == null){
            return new Result<>(false, "Inventory Item not found");
        }
        if (amount < 0 ){
            return new Result<>(false, "Amount must be > 0");
        }
        if (item.getQuantity() < amount){
            return new Result<>(false, "Insufficient Stock");
        }
        item.setQuantity(item.getQuantity() - amount);
        inventoryRepo.update(item);
        return new Result<>(true, "Inventory Item Updated",true);
    }
    @Override
    public List<InventoryItem> getAllItems() {
        return inventoryRepo.findAll();
    }
}