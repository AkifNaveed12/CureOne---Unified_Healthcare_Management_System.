package com.cureone.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    private static final Properties p = new Properties();

    static {
        try (InputStream in = new FileInputStream("config/db.properties")) {
            p.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load DB configuration", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    p.getProperty("jdbc.url"),
                    p.getProperty("jdbc.user"),
                    p.getProperty("jdbc.pass")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }
}
