package com.hrm.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {
    private static final Properties P = new Properties();
    static {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null)
                P.load(in);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("db.url", P.getProperty("db.url"));
        String user = System.getProperty("db.user", P.getProperty("db.user", "root"));
        String pass = System.getProperty("db.password", P.getProperty("db.password", "tuan1234"));
        return DriverManager.getConnection(url, user, pass);
    }
}
