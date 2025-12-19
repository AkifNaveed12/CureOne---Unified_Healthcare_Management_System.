package com.cureone.pharmacyandinventory.repository;

import com.cureone.common.DBUtil;
import com.cureone.pharmacyandinventory.model.Invoice;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcBillingRepository implements BillingRepository {

    @Override
    public void save(Invoice invoice) {

        String invoiceSql = """
        INSERT INTO invoices
        (invoice_number, patient_id, pharmacist_id, total_amount, created_at)
        VALUES (?, ?, ?, ?, ?)
    """;

        String itemSql = """
        INSERT INTO invoice_items
        (invoice_id, medicine_id, quantity, unit_price, line_total)
        VALUES (?, ?, ?, ?, ?)
    """;

        try (Connection con = DBUtil.getConnection()) {

            // 1️⃣ Save invoice header
            try (PreparedStatement ps = con.prepareStatement(
                    invoiceSql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, invoice.getInvoiceNumber());
                ps.setInt(2, invoice.getPatientId());          // IMPORTANT
                ps.setInt(3, invoice.getPharmacistId());
                ps.setDouble(4, invoice.getTotalAmount());
                ps.setTimestamp(5, Timestamp.valueOf(
                        invoice.getCreatedAt() != null
                                ? invoice.getCreatedAt()
                                : LocalDateTime.now()
                ));

                ps.executeUpdate();

                // 2️⃣ Get generated invoice ID
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        invoice.setId(rs.getInt(1));
                    }
                }
            }

            // 3️⃣ Save invoice items
            try (PreparedStatement psItem = con.prepareStatement(itemSql)) {
                for (var item : invoice.getItems()) {

                    psItem.setInt(1, invoice.getId());
                    psItem.setInt(2, item.getMedicineId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, item.getUnitPrice());
                    psItem.setDouble(5, item.lineTotal());

                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Invoice> findAll() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices";

        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Invoice inv = new Invoice();
                inv.setId(rs.getInt("id"));
                inv.setInvoiceNumber(rs.getString("invoice_number"));
                inv.setPharmacistId(rs.getInt("pharmacist_id"));
                inv.setTotalAmount(rs.getDouble("total_amount"));
                inv.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(inv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Invoice findById(int id) {
        String sql = "SELECT * FROM invoices WHERE id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Invoice inv = new Invoice();
                inv.setId(rs.getInt("id"));
                inv.setInvoiceNumber(rs.getString("invoice_number"));
                inv.setPharmacistId(rs.getInt("pharmacist_id"));
                inv.setTotalAmount(rs.getDouble("total_amount"));
                inv.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return inv;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
