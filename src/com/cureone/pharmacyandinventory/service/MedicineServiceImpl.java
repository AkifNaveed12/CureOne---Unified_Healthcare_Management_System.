package com.cureone.pharmacyandinventory.service;

import com.cureone.pharmacyandinventory.model.Medicine;
import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.repository.MedicineRepository;

import java.util.List;
import java.util.Objects;

public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository repository;

    public MedicineServiceImpl(MedicineRepository repository) {
        this.repository = repository;
    }
    @Override
    public Result<Medicine> addMedicine(Medicine medicine) {
        if(medicine == null)
            return new Result<>(false, "Medicine is null");
        if(medicine.getName() == null || medicine.getName().trim().isEmpty()){
            return new Result<>(false, "Medicine name is Required");
        }
        if (medicine.getPrice() < 0){
            return new Result<>(false, "Medicine price cannot be negative");
        }
        if (medicine.getQuantity() < 0){
            return new Result<>(false, "Medicine quantity cannot be negative");
        }
        repository.save(medicine);
        return new Result<>(true, "Medicine added successfully", medicine);
    }

    @Override
    public List<Medicine> getAllMedicines() {
        return repository.findAll();
    }

    @Override
    public Medicine getMedicineById(int id) {
        return repository.findById(id);
    }
    @Override
    public Result<Medicine>  updateMedicine(Medicine medicine) {
        if(medicine == null || medicine.getId() == 0){
            return new Result<>(false, "Invalid Medicine For update");
        }
        boolean ok = repository.update(medicine);
        if(!ok){
            return new Result<>(false, "Medicine not found");
        }

        return new Result<>(true, "Medicine updated successfully", medicine);
    }

    @Override
    public Result<Boolean>  deleteMedicine(int id) {
        boolean ok = repository.delete(id);
        if(!ok){
            return new Result<>(false, "Medicine not found");
        }
        return new Result<>(true, "Medicine deleted successfully",  true);
    }
    @Override
    public List<Medicine> searchByName(String name) {
        return repository.findByName(name);
    }
    @Override
    public List<Medicine> getExpiredMedicines() {
        return repository.findExpiredMedicines();
    }

}
