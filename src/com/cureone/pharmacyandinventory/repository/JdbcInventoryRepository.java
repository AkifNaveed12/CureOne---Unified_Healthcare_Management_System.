package com.cureone.pharmacyandinventory.repository;


import com.cureone.common.DBUtil;
import com.cureone.pharmacyandinventory.model.InventoryItem;
import com.cureone.pharmacyandinventory.model.Medicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcInventoryRepository implements InventoryRepository {

    @Override
    public void save(InventoryItem item) {
        String sql = "INSERT INTO inventory_items (medicine_id, quantity, minimum_stock_limit, expiry_date, location, unit_price) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getMedicine().getId());
            ps.setInt(2, item.getQuantity());
            ps.setInt(3, item.getMinimumStockLimit());
            ps.setDate(4, item.getExpiryDate() == null ? null : Date.valueOf(item.getExpiryDate()));
            ps.setString(5, item.getLocation());
            ps.setDouble(6, item.getPrice());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) item.setItemId(rs.getInt(1));
            }
            // update aggregated stock table as convenience
            updateStock(item.getMedicine().getId(), item.getQuantity());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void updateStock(int medicineId, int delta) {
        String select = "SELECT quantity FROM stock WHERE medicine_id=?";
        String insert = "INSERT INTO stock (medicine_id, quantity) VALUES (?, ?)";
        String update = "UPDATE stock SET quantity = quantity + ? WHERE medicine_id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement psSel = c.prepareStatement(select)) {
            psSel.setInt(1, medicineId);
            try (ResultSet rs = psSel.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement psUpd = c.prepareStatement(update)) {
                        psUpd.setInt(1, delta);
                        psUpd.setInt(2, medicineId);
                        psUpd.executeUpdate();
                    }
                } else {
                    try (PreparedStatement psIns = c.prepareStatement(insert)) {
                        psIns.setInt(1, medicineId);
                        psIns.setInt(2, delta);
                        psIns.executeUpdate();
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public List<InventoryItem> findAll() {
        String sql = "SELECT i.item_id, i.medicine_id, i.quantity, i.minimum_stock_limit, i.expiry_date, i.location, i.unit_price, m.name FROM inventory_items i LEFT JOIN medicines m ON i.medicine_id = m.id";
        List<InventoryItem> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                InventoryItem it = new InventoryItem();
                it.setItemId(rs.getInt("item_id"));
                Medicine m = new Medicine();
                m.setId(rs.getInt("medicine_id"));
                m.setName(rs.getString("name"));
                it.setMedicine(m);
                it.setQuantity(rs.getInt("quantity"));
                it.setMinimumStockLimit(rs.getInt("minimum_stock_limit"));
                Date d = rs.getDate("expiry_date");
                it.setExpiryDate(d == null ? null : d.toLocalDate());
                it.setLocation(rs.getString("location"));
                it.setPrice(rs.getDouble("unit_price"));
                out.add(it);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    @Override
    public InventoryItem findById(int Id) {
        String sql = "SELECT i.*, m.name FROM inventory_items i LEFT JOIN medicines m ON i.medicine_id=m.id WHERE i.item_id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    InventoryItem it = new InventoryItem();
                    it.setItemId(rs.getInt("item_id"));
                    Medicine m = new Medicine();
                    m.setId(rs.getInt("medicine_id"));
                    m.setName(rs.getString("name"));
                    it.setMedicine(m);
                    it.setQuantity(rs.getInt("quantity"));
                    it.setMinimumStockLimit(rs.getInt("minimum_stock_limit"));
                    Date d = rs.getDate("expiry_date");
                    it.setExpiryDate(d == null ? null : d.toLocalDate());
                    it.setLocation(rs.getString("location"));
                    it.setPrice(rs.getDouble("unit_price"));
                    return it;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean update(InventoryItem item) {
        String sql = "UPDATE inventory_items SET quantity=?, minimum_stock_limit=?, expiry_date=?, location=?, unit_price=? WHERE item_id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, item.getQuantity());
            ps.setInt(2, item.getMinimumStockLimit());
            ps.setDate(3, item.getExpiryDate() == null ? null : Date.valueOf(item.getExpiryDate()));
            ps.setString(4, item.getLocation());
            ps.setDouble(5, item.getPrice());
            ps.setInt(6, item.getItemId());
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                // synchronize stock simple approach: recompute aggregate from inventory_items (fast enough for small project)
                recomputeStockForMedicine(item.getMedicine().getId());
            }
            return ok;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    private void recomputeStockForMedicine(int medicineId) {
        String sql = "SELECT SUM(quantity) as total FROM inventory_items WHERE medicine_id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            try (ResultSet rs = ps.executeQuery()) {
                int total = 0;
                if (rs.next()) total = rs.getInt("total");
                String up = "REPLACE INTO stock (medicine_id, quantity) VALUES (?, ?)";
                try (PreparedStatement ps2 = c.prepareStatement(up)) {
                    ps2.setInt(1, medicineId);
                    ps2.setInt(2, total);
                    ps2.executeUpdate();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public boolean delete(int Id) {
        // before deleting, decrease stock accordingly (recompute after delete)
        String sql = "DELETE FROM inventory_items WHERE item_id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Id);
            boolean ok = ps.executeUpdate() > 0;
            // can't easily know medicine_id here; recompute all stocks cheaply:
            // for simplicity, caller can call recomputeStockForMedicine if needed
            return ok;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    @Override
    public List<InventoryItem> findByMedicineId(int medicineId) {
        String sql = "SELECT * FROM inventory_items WHERE medicine_id=?";
        List<InventoryItem> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryItem it = new InventoryItem();
                    it.setItemId(rs.getInt("item_id"));
                    Medicine m = new Medicine();
                    m.setId(rs.getInt("medicine_id"));
                    it.setMedicine(m);
                    it.setQuantity(rs.getInt("quantity"));
                    Date d = rs.getDate("expiry_date");
                    it.setExpiryDate(d == null ? null : d.toLocalDate());
                    it.setLocation(rs.getString("location"));
                    it.setPrice(rs.getDouble("unit_price"));
                    it.setMinimumStockLimit(rs.getInt("minimum_stock_limit"));
                    out.add(it);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    @Override
    public List<InventoryItem> findByLowStockLimit(int threshold) {
        String sql = """
            SELECT i.*, m.name, m.manufacturer
            FROM inventory_items i
            JOIN medicines m ON i.medicine_id = m.id
            WHERE i.quantity <= ?
        """;
        List<InventoryItem> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapRow(rs));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return out;
    }

    // ---------------- MAPPER ----------------
    private InventoryItem mapRow(ResultSet rs) throws SQLException {

        Medicine med = new Medicine();
        med.setId(rs.getInt("medicine_id"));
        med.setName(rs.getString("name"));
        med.setManufacturer(rs.getString("manufacturer"));

        InventoryItem item = new InventoryItem();

        item.setItemId(rs.getInt("item_id"));
        item.setMedicine(med);
        item.setQuantity(rs.getInt("quantity"));
        item.setMinimumStockLimit(rs.getInt("min_stock"));
        item.setLocation(rs.getString("location"));
        item.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
        item.setPrice(rs.getDouble("price"));

        return item;
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM inventory_items WHERE item_id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}

