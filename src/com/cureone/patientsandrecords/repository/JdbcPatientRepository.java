//package com.cureone.patientsandrecords.repository;
//
//import com.cureone.patientsandrecords.model.Patient;
//import com.cureone.common.DBUtil;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class JdbcPatientRepository implements InterfacePatientRepository {
//    private final Connection conn;
//
//    public JdbcPatientRepository() {
//        this.conn = DBUtil.getConnection();
//    }
//
//
//    @Override
//    public void add(Patient patient) {
//        String sql = "INSERT INTO patients (name, age, gender, country, contact, disease_description) VALUES (?, ?, ?, ?, ?, ?)";
//        try {
//            try (Connection c = DBUtil.getConnection();
//                 PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//                ps.setString(1, patient.getName());
//                ps.setInt(2, patient.getAge());
//                ps.setString(3, patient.getGender());
//                ps.setString(4, patient.getCountry());
//                ps.setString(5, patient.getContact());
//                ps.setString(6, patient.getDiseaseDescription());
//                ps.executeUpdate();
//
//                try (ResultSet rs = ps.getGeneratedKeys()) {
//                    if (rs.next()) {
//                        patient.setId(rs.getInt(1));
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public boolean update(Patient patient) {
//        String sql = "UPDATE patients SET name=?, age=?, gender=?, country=?, contact=?, disease_description=? WHERE id=?";
//        try {
//            try (Connection c = DBUtil.getConnection();
//                 PreparedStatement ps = c.prepareStatement(sql)) {
//
//                ps.setString(1, patient.getName());
//                ps.setInt(2, patient.getAge());
//                ps.setString(3, patient.getGender());
//                ps.setString(4, patient.getCountry());
//                ps.setString(5, patient.getContact());
//                ps.setString(6, patient.getDiseaseDescription());
//                ps.setInt(7, patient.getId());
//
//                return ps.executeUpdate() > 0;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public boolean delete(int id) {
//        String sql = "DELETE FROM patients WHERE id=?";
//        try {
//            try (Connection c = DBUtil.getConnection();
//                 PreparedStatement ps = c.prepareStatement(sql)) {
//
//                ps.setInt(1, id);
//                return ps.executeUpdate() > 0;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public Patient findById(int id) {
//        String sql = "SELECT * FROM patients WHERE id=?";
//        try {
//            try (Connection c = DBUtil.getConnection();
//                 PreparedStatement ps = c.prepareStatement(sql)) {
//
//                ps.setInt(1, id);
//                try (ResultSet rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        Patient p = new Patient();
//                        p.setId(rs.getInt("id"));
//                        p.setName(rs.getString("name"));
//                        p.setAge(rs.getInt("age"));
//                        p.setGender(rs.getString("gender"));
//                        p.setCountry(rs.getString("country"));
//                        p.setContact(rs.getString("contact"));
//                        p.setDiseaseDescription(rs.getString("disease_description"));
//                        return p;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public List<Patient> findByName(String name) {
//        String sql = "SELECT * FROM patients WHERE name LIKE ?";
//        List<Patient> list = new ArrayList<>();
//        try {
//            try (Connection c = DBUtil.getConnection();
//                 PreparedStatement ps = c.prepareStatement(sql)) {
//
//                ps.setString(1, "%" + name + "%");
//                try (ResultSet rs = ps.executeQuery()) {
//                    while (rs.next()) {
//                        Patient p = new Patient();
//                        p.setId(rs.getInt("id"));
//                        p.setName(rs.getString("name"));
//                        p.setAge(rs.getInt("age"));
//                        p.setGender(rs.getString("gender"));
//                        p.setCountry(rs.getString("country"));
//                        p.setContact(rs.getString("contact"));
//                        p.setDiseaseDescription(rs.getString("disease_description"));
//                        list.add(p);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    @Override
//    public List<Patient> findAll() {
//        String sql = "SELECT * FROM patients";
//        List<Patient> list = new ArrayList<>();
//        try {
//            try (Connection c = DBUtil.getConnection();
//                 Statement st = c.createStatement();
//                 ResultSet rs = st.executeQuery(sql)) {
//
//                while (rs.next()) {
//                    Patient p = new Patient();
//                    p.setId(rs.getInt("id"));
//                    p.setName(rs.getString("name"));
//                    p.setAge(rs.getInt("age"));
//                    p.setGender(rs.getString("gender"));
//                    p.setCountry(rs.getString("country"));
//                    p.setContact(rs.getString("contact"));
//                    p.setDiseaseDescription(rs.getString("disease_description"));
//                    list.add(p);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    private Patient mapRow(ResultSet rs) throws SQLException {
//        Patient p = new Patient();
//        p.setId(rs.getInt("id"));
//        p.setId(rs.getInt("user_id"));
//        p.setName(rs.getString("name"));
//        p.setAge(rs.getInt("age"));
//        p.setGender(rs.getString("gender"));
//        p.setCountry(rs.getString("country"));
//        p.setContact(rs.getString("contact"));
//        p.setDiseaseDescription(rs.getString("disease"));
//        return p;
//    }
//
//
//    public Patient findByUserId(int userId) {
//        String sql = "SELECT * FROM patients WHERE user_id = ?";
//
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, userId);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                return mapRow(rs);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//}
package com.cureone.patientsandrecords.repository;

import com.cureone.patientsandrecords.model.Patient;
import com.cureone.common.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcPatientRepository implements InterfacePatientRepository {

    @Override
    public void add(Patient patient) {
        String sql = """
            INSERT INTO patients (name, age, gender, country, contact, disease_description)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, patient.getName());
            ps.setInt(2, patient.getAge());
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getCountry());
            ps.setString(5, patient.getContact());
            ps.setString(6, patient.getDiseaseDescription());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    patient.setId(rs.getInt(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(Patient patient) {
        String sql = """
            UPDATE patients
            SET name=?, age=?, gender=?, country=?, contact=?, disease_description=?
            WHERE id=?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, patient.getName());
            ps.setInt(2, patient.getAge());
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getCountry());
            ps.setString(5, patient.getContact());
            ps.setString(6, patient.getDiseaseDescription());
            ps.setInt(7, patient.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM patients WHERE id=?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Patient findById(int id) {
        String sql = "SELECT * FROM patients WHERE id=?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Patient> findByName(String name) {
        String sql = "SELECT * FROM patients WHERE name LIKE ?";
        List<Patient> list = new ArrayList<>();

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Patient> findAll() {
        String sql = "SELECT * FROM patients";
        List<Patient> list = new ArrayList<>();

        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ CORRECT MAPPER
    private Patient mapRow(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setAge(rs.getInt("age"));
        p.setGender(rs.getString("gender"));
        p.setCountry(rs.getString("country"));
        p.setContact(rs.getString("contact"));
        p.setDiseaseDescription(rs.getString("disease_description"));
        return p;
    }
    @Override
    public Patient findByUserId(int userId) {
        // NOTE:
        // patients table does NOT have user_id
        // userId here is actually patients.id (linkedId)
        return findById(userId);
    }

}

