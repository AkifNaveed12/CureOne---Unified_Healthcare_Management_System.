package com.cureone.patientsandrecords.repository;


import com.cureone.common.DBUtil;
import com.cureone.patientsandrecords.model.MedicalRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcMedicalRecordRepository implements InterfaceMedicalRecordRepository {

    @Override
    public void add(MedicalRecord record) {
        String sql = "INSERT INTO medical_records (patientID, diagnosis, treatment, doctorName, recordDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, record.getPatientID());
            ps.setString(2, record.getDiagnosis());
            ps.setString(3, record.getTreatment());
            ps.setString(4, record.getDoctorName());
            ps.setString(5, record.getRecordDate());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) record.setRecordID(rs.getInt(1));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public boolean update(MedicalRecord record) {
        String sql = "UPDATE medical_records SET patientID=?, diagnosis=?, treatment=?, doctorName=?, recordDate=? WHERE recordID=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, record.getPatientID());
            ps.setString(2, record.getDiagnosis());
            ps.setString(3, record.getTreatment());
            ps.setString(4, record.getDoctorName());
            ps.setString(5, record.getRecordDate());
            ps.setInt(6, record.getRecordID());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean delete(int recordID) {
        String sql = "DELETE FROM medical_records WHERE recordID=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, recordID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    @Override
    public MedicalRecord findById(int recordID) {
        String sql = "SELECT * FROM medical_records WHERE recordID=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, recordID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MedicalRecord r = new MedicalRecord();
                    r.setRecordID(rs.getInt("recordID"));
                    r.setPatientID(rs.getInt("patientID"));
                    r.setDiagnosis(rs.getString("diagnosis"));
                    r.setTreatment(rs.getString("treatment"));
                    r.setDoctorName(rs.getString("doctorName"));
                    r.setRecordDate(rs.getString("recordDate"));
                    return r;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<MedicalRecord> findByPatientId(int patientID) {
        String sql = "SELECT * FROM medical_records WHERE patientID=?";
        List<MedicalRecord> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, patientID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicalRecord r = new MedicalRecord();
                    r.setRecordID(rs.getInt("recordID"));
                    r.setPatientID(rs.getInt("patientID"));
                    r.setDiagnosis(rs.getString("diagnosis"));
                    r.setTreatment(rs.getString("treatment"));
                    r.setDoctorName(rs.getString("doctorName"));
                    r.setRecordDate(rs.getString("recordDate"));
                    out.add(r);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    @Override
    public List<MedicalRecord> findAll() {
        String sql = "SELECT * FROM medical_records";
        List<MedicalRecord> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                MedicalRecord r = new MedicalRecord();
                r.setRecordID(rs.getInt("recordID"));
                r.setPatientID(rs.getInt("patientID"));
                r.setDiagnosis(rs.getString("diagnosis"));
                r.setTreatment(rs.getString("treatment"));
                r.setDoctorName(rs.getString("doctorName"));
                r.setRecordDate(rs.getString("recordDate"));
                list.add(r);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}

