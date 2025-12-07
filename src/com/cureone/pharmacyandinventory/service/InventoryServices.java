package com.cureone.pharmacyandinventory.service;

import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.InventoryItem;
import java.util.List;

public interface InventoryServices {

    Result<InventoryItem> addInventoryItem(InventoryItem Item);
    List<InventoryItem> getAllItems();
    InventoryItem getItemById(int Id);
    Result<InventoryItem> updateItem(InventoryItem Item);
    Result<Boolean> deleteItem(int Id);
    List<InventoryItem> findByMedicineId(int medicineId);
    List<InventoryItem> findLowStockItems(int threshold);
    Result<Boolean> reduceStock(int itemId, int amount);

}
