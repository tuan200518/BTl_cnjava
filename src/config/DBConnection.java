package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Quan ly ket noi MySQL cho ung dung.
 *
 * Mac dinh:
 *   host: localhost
 *   port: 3306
 *   database: quanly_nhansu
 *   user: root
 *   password: 20112005
 *
 * Co the ghi de bang JVM properties:
 *   -Ddb.url=...
 *   -Ddb.user=...
 *   -Ddb.password=...
 */
public final class DBConnection {
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/quanly_nhansu"
          + "?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh"
          + "&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "tuan1234";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("db.url", DEFAULT_URL);
        String user = System.getProperty("db.user", DEFAULT_USER);
        String password = System.getProperty("db.password", DEFAULT_PASSWORD);

        return DriverManager.getConnection(url, user, password);
    }
}
