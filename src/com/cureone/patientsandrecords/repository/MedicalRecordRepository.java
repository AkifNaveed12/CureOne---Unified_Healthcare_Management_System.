package com.cureone.patientsandrecords.repository;

//Made By Hamza Ali FA24-BSE-124

import com.cureone.patientsandrecords.model.MedicalRecord;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordRepository implements InterfaceMedicalRecordRepository {
// Yeh class interface ke rule follow karti hai (interface batata hai KYA karna hai)

    private final List<MedicalRecord> records = new ArrayList<>();
    // Yeh humara "data store" hai  sab medical records yahan memory me save honge

    @Override
    public void add(MedicalRecord record)
    {
        records.add(record);
    }

    @Override
    public boolean update(MedicalRecord record)
    {
        // Update jo record ID match kare, usko replace kar do

        for (int i = 0; i < records.size(); i++)
        {
            // Har record ko ek ek karke check kare ga

            if (records.get(i).getRecordID() == record.getRecordID())
            {
                // Agar record ID match ho jaye yahi hamara update wala record hai

                records.set(i, record);
                // Purane record ko naya record se replace kar diya

                return true;
            }
        }
        return false;
    }
    @Override
    public boolean delete(int recordID)
    {
        // Agar recordID match kare us record ko delete karo
        return records.removeIf(r -> r.getRecordID() == recordID);
    }
    @Override
    public MedicalRecord findById(int recordID)
    {
        for (MedicalRecord r : records)
        {  // Ek ek record check ho raha hai
            if (r.getRecordID() == recordID)
                return r;
            // Agar ID match mil gayi wohi record return kar do
        }
        return null;
    }
    @Override
    public List<MedicalRecord> findByPatientId(int patientID)
    {
        List<MedicalRecord> result = new ArrayList<>();
        for (MedicalRecord r : records)
        {
            if (r.getPatientID() == patientID)
                result.add(r);
            // Jis record ka patientID match ho  result me add kar do
        }
        return result;
    }
    @Override
    public List<MedicalRecord> findAll()
    {
        return new ArrayList<>(records);
    }
}

