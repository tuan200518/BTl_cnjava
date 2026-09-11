package com.hrm.service;

import com.hrm.config.DBConnection;
import java.sql.*;

<<<<<<< HEAD
/** ADMIN only: technical monitoring and maintenance. No HR business operations live here. */
=======
/**
 * ADMIN only: technical monitoring and maintenance. No HR business operations
 * live here.
 */
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
public class MaintenanceService {
    public String testConnection() {
        try (Connection c = DBConnection.getConnection()) {
            DatabaseMetaData m = c.getMetaData();
            return "KẾT NỐI OK\nMySQL: " + m.getDatabaseProductVersion()
                    + "\nDriver: " + m.getDriverName() + " " + m.getDriverVersion()
                    + "\nURL: " + m.getURL();
<<<<<<< HEAD
        } catch (Exception e) { return "LỖI KẾT NỐI: " + e.getMessage(); }
    }

    public String systemStats() throws Exception {
        String[] tables = {"employees","users","departments","jobs","projects","project_allocations",
                "payrolls","attendance_records","leave_records","overtime_records","employment_events",
                "rewards_disciplines","system_logs"};
=======
        } catch (Exception e) {
            return "LỖI KẾT NỐI: " + e.getMessage();
        }
    }

    public String systemStats() throws Exception {
        String[] tables = { "employees", "users", "departments", "jobs", "projects", "project_allocations",
                "payrolls", "attendance_records", "leave_records", "overtime_records", "employment_events",
                "rewards_disciplines", "system_logs" };
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
        StringBuilder b = new StringBuilder("THỐNG KÊ HỆ THỐNG\n");
        try (Connection c = DBConnection.getConnection(); Statement s = c.createStatement()) {
            for (String t : tables) {
                try (ResultSet r = s.executeQuery("SELECT COUNT(*) FROM " + t)) {
<<<<<<< HEAD
                    r.next(); b.append(String.format("%-22s : %,d%n", t, r.getLong(1)));
=======
                    r.next();
                    b.append(String.format("%-22s : %,d%n", t, r.getLong(1)));
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
                }
            }
        }
        return b.toString();
    }

    public String accountStats() throws Exception {
        String sql = "SELECT role, SUM(is_active=1) active_count, SUM(is_active=0) inactive_count, COUNT(*) total "
<<<<<<< HEAD
                   + "FROM users GROUP BY role ORDER BY role";
        StringBuilder b = new StringBuilder("TÌNH TRẠNG TÀI KHOẢN\n");
        try (Connection c = DBConnection.getConnection(); Statement s = c.createStatement(); ResultSet r = s.executeQuery(sql)) {
            while (r.next()) b.append(String.format("%-12s tổng: %,d | hoạt động: %,d | ngừng: %,d%n",
                    r.getString(1), r.getLong(4), r.getLong(2), r.getLong(3)));
=======
                + "FROM users GROUP BY role ORDER BY role";
        StringBuilder b = new StringBuilder("TÌNH TRẠNG TÀI KHOẢN\n");
        try (Connection c = DBConnection.getConnection();
                Statement s = c.createStatement();
                ResultSet r = s.executeQuery(sql)) {
            while (r.next())
                b.append(String.format("%-12s tổng: %,d | hoạt động: %,d | ngừng: %,d%n",
                        r.getString(1), r.getLong(4), r.getLong(2), r.getLong(3)));
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
        }
        return b.toString();
    }

    public String integrityCheck() throws Exception {
        StringBuilder b = new StringBuilder("KIỂM TRA TOÀN VẸN DỮ LIỆU\n");
        try (Connection c = DBConnection.getConnection()) {
            b.append("Nhân viên không có tài khoản: ").append(count(c,
<<<<<<< HEAD
                    "SELECT COUNT(*) FROM employees e LEFT JOIN users u ON u.employee_id=e.employee_id WHERE u.employee_id IS NULL")).append('\n');
            b.append("Tài khoản EMPLOYEE không có nhân viên: ").append(count(c,
                    "SELECT COUNT(*) FROM users u LEFT JOIN employees e ON e.employee_id=u.employee_id WHERE u.role='EMPLOYEE' AND e.employee_id IS NULL")).append('\n');
            b.append("Nhân viên RESIGNED nhưng tài khoản còn hoạt động: ").append(count(c,
                    "SELECT COUNT(*) FROM users u JOIN employees e ON e.employee_id=u.employee_id WHERE u.role='EMPLOYEE' AND e.status='RESIGNED' AND u.is_active=1")).append('\n');
            b.append("Phiếu lương không có nhân viên hợp lệ: ").append(count(c,
                    "SELECT COUNT(*) FROM payrolls p LEFT JOIN employees e ON e.employee_id=p.employee_id WHERE e.employee_id IS NULL")).append('\n');
            b.append("Phân công không có dự án/nhân viên hợp lệ: ").append(count(c,
                    "SELECT COUNT(*) FROM project_allocations pa LEFT JOIN projects p ON p.project_id=pa.project_id LEFT JOIN employees e ON e.employee_id=pa.employee_id WHERE p.project_id IS NULL OR e.employee_id IS NULL")).append('\n');
=======
                    "SELECT COUNT(*) FROM employees e LEFT JOIN users u ON u.employee_id=e.employee_id WHERE u.employee_id IS NULL"))
                    .append('\n');
            b.append("Tài khoản EMPLOYEE không có nhân viên: ").append(count(c,
                    "SELECT COUNT(*) FROM users u LEFT JOIN employees e ON e.employee_id=u.employee_id WHERE u.role='EMPLOYEE' AND e.employee_id IS NULL"))
                    .append('\n');
            b.append("Nhân viên RESIGNED nhưng tài khoản còn hoạt động: ").append(count(c,
                    "SELECT COUNT(*) FROM users u JOIN employees e ON e.employee_id=u.employee_id WHERE u.role='EMPLOYEE' AND e.status='RESIGNED' AND u.is_active=1"))
                    .append('\n');
            b.append("Phiếu lương không có nhân viên hợp lệ: ").append(count(c,
                    "SELECT COUNT(*) FROM payrolls p LEFT JOIN employees e ON e.employee_id=p.employee_id WHERE e.employee_id IS NULL"))
                    .append('\n');
            b.append("Phân công không có dự án/nhân viên hợp lệ: ").append(count(c,
                    "SELECT COUNT(*) FROM project_allocations pa LEFT JOIN projects p ON p.project_id=pa.project_id LEFT JOIN employees e ON e.employee_id=pa.employee_id WHERE p.project_id IS NULL OR e.employee_id IS NULL"))
                    .append('\n');
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
        }
        b.append("Kết quả 0 ở các dòng trên nghĩa là dữ liệu đang nhất quán.");
        return b.toString();
    }

    public String recentLogs(int limit) throws Exception {
        String sql = "SELECT username,action_type,COALESCE(details,''),created_at FROM system_logs ORDER BY log_id DESC LIMIT ?";
        StringBuilder b = new StringBuilder("NHẬT KÝ HỆ THỐNG GẦN ĐÂY\n");
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, Math.max(1, Math.min(limit, 200)));
            try (ResultSet r = p.executeQuery()) {
<<<<<<< HEAD
                int n=0;
                while (r.next()) { n++; b.append(r.getTimestamp(4)).append(" | ").append(r.getString(1)).append(" | ")
                        .append(r.getString(2)).append(" | ").append(r.getString(3)).append('\n'); }
                if(n==0) b.append("Chưa có nhật ký.\n");
=======
                int n = 0;
                while (r.next()) {
                    n++;
                    b.append(r.getTimestamp(4)).append(" | ").append(r.getString(1)).append(" | ")
                            .append(r.getString(2)).append(" | ").append(r.getString(3)).append('\n');
                }
                if (n == 0)
                    b.append("Chưa có nhật ký.\n");
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
            }
        }
        return b.toString();
    }

    public String optimize() throws Exception {
<<<<<<< HEAD
        String[] tables={"employees","users","payrolls","project_allocations","employment_events",
                "rewards_disciplines","attendance_records","leave_records","overtime_records","system_logs"};
        try (Connection c=DBConnection.getConnection(); Statement s=c.createStatement()) {
            for(String t:tables) s.execute("OPTIMIZE TABLE "+t);
=======
        String[] tables = { "employees", "users", "payrolls", "project_allocations", "employment_events",
                "rewards_disciplines", "attendance_records", "leave_records", "overtime_records", "system_logs" };
        try (Connection c = DBConnection.getConnection(); Statement s = c.createStatement()) {
            for (String t : tables)
                s.execute("OPTIMIZE TABLE " + t);
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
        }
        return "Đã gửi lệnh OPTIMIZE cho các bảng kỹ thuật/nghiệp vụ chính.";
    }

    public String cleanupOldLogs(int days) throws Exception {
<<<<<<< HEAD
        days=Math.max(30,days);
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(
                "DELETE FROM system_logs WHERE created_at < DATE_SUB(NOW(), INTERVAL ? DAY)")) {
            p.setInt(1,days); int n=p.executeUpdate(); return "Đã xóa "+n+" nhật ký cũ hơn "+days+" ngày.";
        }
    }

    private long count(Connection c,String sql)throws SQLException { try(Statement s=c.createStatement();ResultSet r=s.executeQuery(sql)){r.next();return r.getLong(1);} }
=======
        days = Math.max(30, days);
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement(
                        "DELETE FROM system_logs WHERE created_at < DATE_SUB(NOW(), INTERVAL ? DAY)")) {
            p.setInt(1, days);
            int n = p.executeUpdate();
            return "Đã xóa " + n + " nhật ký cũ hơn " + days + " ngày.";
        }
    }

    private long count(Connection c, String sql) throws SQLException {
        try (Statement s = c.createStatement(); ResultSet r = s.executeQuery(sql)) {
            r.next();
            return r.getLong(1);
        }
    }
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
}
