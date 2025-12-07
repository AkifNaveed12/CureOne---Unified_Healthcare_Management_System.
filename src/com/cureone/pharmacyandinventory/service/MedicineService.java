package com.cureone.pharmacyandinventory.service;
import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.Medicine;
import java.util.List;


public interface MedicineService {

    Result<Medicine> addMedicine(Medicine medicine);
    List<Medicine> getAllMedicines();
    Medicine getMedicineById(int Id);
    Result<Medicine> updateMedicine(Medicine medicine);
    Result<Boolean> deleteMedicine(int Id);
    List<Medicine> searchByName(String name);
    List<Medicine> getExpiredMedicines();

}
