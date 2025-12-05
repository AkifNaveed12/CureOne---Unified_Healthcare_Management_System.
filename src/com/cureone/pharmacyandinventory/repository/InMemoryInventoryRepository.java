package com.cureone.pharmacyandinventory.repository;
import com.cureone.common.IdGenerator;
import com.cureone.pharmacyandinventory.model.InventoryItem;

import java.util.List;
import java.util.ArrayList;

public class InMemoryInventoryRepository implements InventoryRepository {

    private final List<InventoryItem> items = new ArrayList<>();

    @Override
    public void save(InventoryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (item.getItemId() == 0){
            item.setItemId(IdGenerator.generateId());
        }
        items.add(item);
    }
    @Override
    public List<InventoryItem> findAll() {
        return new ArrayList<>(items);
    }
    @Override
    public InventoryItem findById(int Id) {
        for (InventoryItem item : items){
            if(item.getItemId() == Id){
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean update(InventoryItem item){
        if (item == null || item.getItemId() == 0){
            return false;
        }
        for (int i = 0; i< items.size(); i++){
            if(items.get(i).getItemId() == item.getItemId()){
                items.set(i, item);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int Id) {
        return items.removeIf(item -> item.getItemId() == Id);
    }
    @Override
    public List<InventoryItem> findByMedicineId(int medicineId) {
        List<InventoryItem> out = new ArrayList<>();
        for (InventoryItem item : items){
            if(item.getMedicine() != null && item.getMedicine().getId() == medicineId ){
                out.add(item);
            }
        }
        return out;
    }

    @Override
    public List<InventoryItem> findByLowStockLimit(int threshold){
        List<InventoryItem> low = new ArrayList();
        for (InventoryItem item : items){
            if (item.getQuantity()< threshold){
                low.add(item);
            }
        }
        return low;
    }

    @Override
    public boolean existsById(int id) {
        return items.removeIf(item -> item.getItemId() == id);
    }
}
