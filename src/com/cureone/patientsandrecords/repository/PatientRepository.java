package com.cureone.patientsandrecords.repository;

//Made By Hamza Ali FA24-BSE-124


import com.cureone.common.DBUtil;
import com.cureone.patientsandrecords.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository implements InterfacePatientRepository {

    private final List<Patient> patients = new ArrayList<>();

    @Override
    public void add(Patient patient) {
        patients.add(patient);
    }
    @Override
    public boolean update(Patient patient) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId() == patient.getId()) {
                patients.set(i, patient);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean delete(int id) {
        return patients.removeIf(p -> p.getId() == id);
    }
    @Override
    public Patient findById(int id) {
        for (Patient p : patients) {
            if (p.getId() == id) return p;
        }
        return null;
    }
    @Override
    public List<Patient> findByName(String name) {
        List<Patient> result = new ArrayList<>();
        for (Patient p : patients) {
            if (p.getName().equalsIgnoreCase(name)) result.add(p);
        }
        return result;
    }
    @Override
    public List<Patient> findAll() {
        return new ArrayList<>(patients);
    }

    @Override
    public Patient findByUserId(int userId) {
        String sql = "SELECT * FROM patients WHERE user_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setGender(rs.getString("gender"));
                p.setAge(rs.getInt("age"));
                p.setCountry(rs.getString("country"));
                p.setContact(rs.getString("contact"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
