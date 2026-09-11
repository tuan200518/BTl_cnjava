package com.hrm.dao;
<<<<<<< HEAD
import com.hrm.config.DBConnection; import com.hrm.model.UserSession; import java.sql.*;
public class UserDAO {
 public UserSession find(String username) throws SQLException { String q="SELECT user_id,employee_id,username,role,password_hash FROM users WHERE username=?"; try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setString(1,username);try(ResultSet r=p.executeQuery()){if(!r.next())return null;return new UserSession(r.getInt(1),(Integer)r.getObject(2),r.getString(3),r.getString(4)+"|"+r.getString(5));}} }
=======

import com.hrm.config.DBConnection;
import com.hrm.model.UserSession;
import java.sql.*;

public class UserDAO {
    public UserSession find(String username) throws SQLException {
        String q = "SELECT user_id,employee_id,username,role,password_hash FROM users WHERE username=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
            p.setString(1, username);
            try (ResultSet r = p.executeQuery()) {
                if (!r.next())
                    return null;
                return new UserSession(r.getInt(1), (Integer) r.getObject(2), r.getString(3),
                        r.getString(4) + "|" + r.getString(5));
            }
        }
    }
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
}
