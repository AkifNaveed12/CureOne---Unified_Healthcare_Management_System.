package com.cureone.pharmacyandinventory.repository;

import com.cureone.common.DBUtil;
import com.cureone.pharmacyandinventory.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCategoryRepository implements CategoryRepository {
    private final Connection conn ;

    public JdbcCategoryRepository() {
        this.conn = DBUtil.getConnection();
    }


    // ---------------- SAVE ----------------
    @Override
    public int save(Category category) {

        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps =
                     c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    category.setId(id);
                    return id;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ---------------- FIND ALL ----------------
    @Override
    public List<Category> findAll() {

        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY id";

        try (Connection c = DBUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Category cat = new Category(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("id")
                );
                list.add(cat);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ---------------- FIND BY ID ----------------
    @Override
    public Category findById(int id) {

        String sql = "SELECT * FROM categories WHERE id = ?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("id")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------------- EXISTS BY NAME ----------------
    @Override
    public boolean existsByName(String name) {

        String sql = "SELECT 1 FROM categories WHERE LOWER(name) = LOWER(?)";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<Category> searchByName(String keyword) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE LOWER(name) LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
