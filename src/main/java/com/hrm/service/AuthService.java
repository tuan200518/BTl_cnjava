package com.hrm.service;

import com.hrm.config.DBConnection;
import com.hrm.model.UserSession;
import com.hrm.util.SecurityUtil;
import java.sql.*;

public class AuthService {
    public UserSession login(String username, String password) throws Exception {
        String q = "SELECT u.user_id,u.employee_id,u.username,u.role,u.password_hash,u.is_active,e.status,e.hire_date FROM users u LEFT JOIN employees e ON e.employee_id=u.employee_id WHERE u.username=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
            p.setString(1, username);
            try (ResultSet r = p.executeQuery()) {
                if (!r.next() || !r.getBoolean(6))
                    return null;
                String status = r.getString(7);
                if ("EMPLOYEE".equals(r.getString(4)) && "RESIGNED".equals(status))
                    return null;
                if (!SecurityUtil.verifyPassword(password, r.getString(5)))
                    return null;
                return new UserSession(r.getInt(1), (Integer) r.getObject(2), r.getString(3), r.getString(4));
            }
        }
    }
}
