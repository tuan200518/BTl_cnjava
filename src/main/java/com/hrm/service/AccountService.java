package com.hrm.service;
import com.hrm.config.DBConnection;
import com.hrm.util.SecurityUtil;
import java.sql.*;
public class AccountService {
    public boolean changePassword(int userId, String current, String next) throws Exception {
        if(next==null||next.length()<8) throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 8 ký tự.");
        try(Connection c=DBConnection.getConnection()){
            c.setAutoCommit(false);
            try(PreparedStatement q=c.prepareStatement("SELECT password_hash FROM users WHERE user_id=? FOR UPDATE")){
                q.setInt(1,userId);try(ResultSet r=q.executeQuery()){
                    if(!r.next()||!SecurityUtil.verifyPassword(current,r.getString(1))){c.rollback();return false;}
                }
            }
            try(PreparedStatement p=c.prepareStatement("UPDATE users SET password_hash=? WHERE user_id=?")){p.setString(1,SecurityUtil.hashPassword(next));p.setInt(2,userId);p.executeUpdate();}
            c.commit();return true;
        }
    }
}
