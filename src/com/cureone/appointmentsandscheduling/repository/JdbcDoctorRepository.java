package com.cureone.appointmentsandscheduling.repository;

import com.cureone.appointmentsandscheduling.model.Doctor;
import com.cureone.common.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcDoctorRepository implements DoctorRepository {

    @Override
    public int save(Doctor doctor) {
        String sql = "INSERT INTO doctors (name, specialization, phone, email) VALUES (?, ?, ?, ?)";
        try {
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, doctor.getName());
                ps.setString(2, doctor.getSpecialization());
                ps.setString(3, doctor.getPhone());
                ps.setString(4, doctor.getEmail());

                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        doctor.setId(id);
                        return id;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean update(Doctor doctor) {
        String sql = "UPDATE doctors SET specialization=?, phone=?, email=? WHERE id=?";
        try {
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setString(1, doctor.getSpecialization());
                ps.setString(2, doctor.getPhone());
                ps.setString(3, doctor.getEmail());
                ps.setInt(4, doctor.getId());

                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM doctors WHERE id=?";
        try {
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Doctor findById(int id) {
        String sql = "SELECT * FROM doctors WHERE id=?";
        try {
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Doctor d = new Doctor();
                        d.setId(rs.getInt("id"));
                        d.setName(rs.getString("name"));
                        d.setSpecialization(rs.getString("specialization"));
                        d.setPhone(rs.getString("phone"));
                        d.setEmail(rs.getString("email"));
                        return d;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Doctor> findAll() {
        String sql = "SELECT * FROM doctors";
        List<Doctor> list = new ArrayList<>();
        try {
            try (Connection c = DBUtil.getConnection();
                 Statement st = c.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    Doctor d = new Doctor();
                    d.setId(rs.getInt("id"));
                    d.setName(rs.getString("name"));
                    d.setSpecialization(rs.getString("specialization"));
                    d.setPhone(rs.getString("phone"));
                    d.setEmail(rs.getString("email"));
                    list.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Doctor findByUserId(int userId) {
        String sql = "SELECT * FROM doctors WHERE user_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Doctor d = new Doctor();
                    d.setId(rs.getInt("id"));
                    d.setName(rs.getString("name"));
                    d.setSpecialization(rs.getString("specialization"));
                    d.setPhone(rs.getString("phone"));
                    d.setEmail(rs.getString("email"));
                    return d;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
