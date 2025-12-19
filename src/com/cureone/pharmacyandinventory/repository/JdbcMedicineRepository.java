package com.cureone.pharmacyandinventory.repository;

import com.cureone.common.DBUtil;
import com.cureone.pharmacyandinventory.model.Category;
import com.cureone.pharmacyandinventory.model.Medicine;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcMedicineRepository implements MedicineRepository {

    // ---------------- SAVE ----------------
    @Override
    public void save(Medicine medicine) {

        String sql = """
            INSERT INTO medicines
            (name, manufacturer, expiry_date, price, category_id, quantity)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, medicine.getName());
            ps.setString(2, medicine.getManufacturer());
            ps.setDate(3,
                    medicine.getExpiryDate() == null ? null : Date.valueOf(medicine.getExpiryDate()));
            ps.setDouble(4, medicine.getPrice());

            if (medicine.getCategory() != null)
                ps.setInt(5, medicine.getCategory().getId());
            else
                ps.setNull(5, Types.INTEGER);

            ps.setInt(6, medicine.getQuantity());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    medicine.setId(rs.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- FIND ALL ----------------
    @Override
    public List<Medicine> findAll() {

        String sql = """
            SELECT m.id, m.name, m.manufacturer, m.expiry_date, m.price, m.quantity,
                   c.id AS category_id, c.name AS category_name, c.description
            FROM medicines m
            LEFT JOIN categories c ON m.category_id = c.id
        """;

        List<Medicine> out = new ArrayList<>();

        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                out.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    // ---------------- FIND BY ID ----------------
    @Override
    public Medicine findById(int id) {

        String sql = """
            SELECT m.id, m.name, m.manufacturer, m.expiry_date, m.price, m.quantity,
                   c.id AS category_id, c.name AS category_name, c.description
            FROM medicines m
            LEFT JOIN categories c ON m.category_id = c.id
            WHERE m.id = ?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRow(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------------- UPDATE ----------------
    @Override
    public boolean update(Medicine medicine) {

        String sql = """
            UPDATE medicines
            SET name=?, manufacturer=?, expiry_date=?, price=?, category_id=?, quantity=?
            WHERE id=?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, medicine.getName());
            ps.setString(2, medicine.getManufacturer());
            ps.setDate(3,
                    medicine.getExpiryDate() == null ? null : Date.valueOf(medicine.getExpiryDate()));
            ps.setDouble(4, medicine.getPrice());

            if (medicine.getCategory() != null)
                ps.setInt(5, medicine.getCategory().getId());
            else
                ps.setNull(5, Types.INTEGER);

            ps.setInt(6, medicine.getQuantity());
            ps.setInt(7, medicine.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- DELETE ----------------
    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM medicines WHERE id=?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- EXISTS ----------------
    @Override
    public boolean existsById(int id) {
        return findById(id) != null;
    }

    // ---------------- SEARCH ----------------
    @Override
    public List<Medicine> findByName(String name) {

        String sql = """
            SELECT m.id, m.name, m.manufacturer, m.expiry_date, m.price, m.quantity,
                   c.id AS category_id, c.name AS category_name, c.description
            FROM medicines m
            LEFT JOIN categories c ON m.category_id = c.id
            WHERE m.name LIKE ?
        """;

        List<Medicine> out = new ArrayList<>();

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    out.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    // ---------------- EXPIRED ----------------
    @Override
    public List<Medicine> findExpiredMedicines() {

        String sql = """
            SELECT m.id, m.name, m.manufacturer, m.expiry_date, m.price, m.quantity,
                   c.id AS category_id, c.name AS category_name, c.description
            FROM medicines m
            LEFT JOIN categories c ON m.category_id = c.id
            WHERE m.expiry_date < CURDATE()
        """;

        List<Medicine> out = new ArrayList<>();

        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next())
                out.add(mapRow(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    // ---------------- ROW MAPPER ----------------
    private Medicine mapRow(ResultSet rs) throws SQLException {

        Medicine m = new Medicine();

        m.setId(rs.getInt("id"));
        m.setName(rs.getString("name"));
        m.setManufacturer(rs.getString("manufacturer"));

        Date d = rs.getDate("expiry_date");
        m.setExpiryDate(d == null ? null : d.toLocalDate());

        m.setPrice(rs.getDouble("price"));
        m.setQuantity(rs.getInt("quantity"));

        int catId = rs.getInt("category_id");
        if (!rs.wasNull()) {
            Category c = new Category();
            c.setId(catId);
            c.setName(rs.getString("category_name"));
            c.setDescription(rs.getString("description"));
            m.setCategory(c);
        }

        return m;
    }
}
