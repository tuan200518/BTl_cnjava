package com.hrm.dao;

import com.hrm.config.DBConnection;
import com.hrm.model.Employee;
import com.hrm.util.SecurityUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class EmployeeDAO {
    public List<Employee> find(String text, int year, String department, String job, String status)
            throws SQLException {
        List<Employee> out = new ArrayList<>();
        String q = "SELECT e.employee_id,CONCAT('NV',LPAD(e.employee_id,4,'0')),e.full_name,e.email,d.department_name,j.job_title,e.base_salary,e.hire_date,e.resignation_date,e.status,COALESCE(u.username,CONCAT('NV',LPAD(e.employee_id,4,'0'))) FROM employees e JOIN departments d ON d.department_id=e.department_id JOIN jobs j ON j.job_id=e.job_id LEFT JOIN users u ON u.employee_id=e.employee_id WHERE (?=0 OR (e.hire_date<=STR_TO_DATE(CONCAT(?,'-12-31'),'%Y-%m-%d') AND (e.resignation_date IS NULL OR e.resignation_date>=STR_TO_DATE(CONCAT(?,'-01-01'),'%Y-%m-%d')))) AND (?='' OR e.full_name LIKE ? OR e.email LIKE ? OR CONCAT('NV',LPAD(e.employee_id,4,'0')) LIKE ?) AND (?='Tất cả' OR d.department_name=?) AND (?='Tất cả' OR j.job_title=?) AND (?='Tất cả' OR e.status=?) ORDER BY e.employee_id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
            int i = 1;
            p.setInt(i++, year);
            p.setInt(i++, year);
            p.setInt(i++, year);
            p.setString(i++, text);
            p.setString(i++, "%" + text + "%");
            p.setString(i++, "%" + text + "%");
            p.setString(i++, "%" + text + "%");
            p.setString(i++, department);
            p.setString(i++, department);
            p.setString(i++, job);
            p.setString(i++, job);
            p.setString(i++, status);
            p.setString(i++, status);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next())
                    out.add(map(rs));
            }
        }
        return out;
    }

    public List<Employee> find(String text, int year) throws SQLException {
        return find(text, year, "Tất cả", "Tất cả", "Tất cả");
    }

    private Employee map(ResultSet r) throws SQLException {
        java.sql.Date h = r.getDate(8), d = r.getDate(9);
        return new Employee(r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getString(6),
                r.getDouble(7), h.toLocalDate(), d == null ? null : d.toLocalDate(), r.getString(10), r.getString(11));
    }

    public List<String[]> departments() throws SQLException {
        List<String[]> a = new ArrayList<>();
        String q = "SELECT department_id,department_name FROM departments ORDER BY department_name";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement(q);
                ResultSet r = p.executeQuery()) {
            while (r.next())
                a.add(new String[] { r.getString(1), r.getString(2) });
        }
        return a;
    }

    public List<String[]> jobs() throws SQLException {
        List<String[]> a = new ArrayList<>();
        String q = "SELECT j.job_id,j.job_title,d.department_name FROM jobs j JOIN departments d ON d.department_id=j.department_id ORDER BY d.department_name,j.job_title";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement(q);
                ResultSet r = p.executeQuery()) {
            while (r.next())
                a.add(new String[] { r.getString(1), r.getString(2), r.getString(3) });
        }
        return a;
    }

    public int insert(String fullName, String email, int jobId, LocalDate hireDate, LocalDate resignationDate,
            String status) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int id;
                try (PreparedStatement p = c.prepareStatement("SELECT COALESCE(MAX(employee_id),0)+1 FROM employees");
                        ResultSet rs = p.executeQuery()) {
                    rs.next();
                    id = rs.getInt(1);
                }
                String q = "INSERT INTO employees(employee_id,job_id,department_id,full_name,email,base_salary,hire_date,resignation_date,status) SELECT ?,j.job_id,j.department_id,?,?,j.base_salary_min,?,?,? FROM jobs j WHERE j.job_id=?";
                try (PreparedStatement p = c.prepareStatement(q)) {
                    p.setInt(1, id);
                    p.setString(2, fullName);
                    p.setString(3, email);
                    p.setDate(4, java.sql.Date.valueOf(hireDate));
                    p.setDate(5, resignationDate == null ? null : java.sql.Date.valueOf(resignationDate));
                    p.setString(6, status);
                    p.setInt(7, jobId);
                    if (p.executeUpdate() == 0)
                        throw new SQLException("Không tìm thấy vị trí công việc.");
                }
                String username = String.format("NV%04d", id);
                try (PreparedStatement u = c.prepareStatement(
                        "INSERT INTO users(employee_id,username,password_hash,role,is_active) VALUES(?,?,?,'EMPLOYEE',?)")) {
                    u.setInt(1, id);
                    u.setString(2, username);
                    u.setString(3, SecurityUtil.hashPassword("123456"));
                    u.setBoolean(4, "ACTIVE".equals(status));
                    u.executeUpdate();
                }
                try (PreparedStatement ev = c.prepareStatement(
                        "INSERT INTO employment_events(employee_id,event_type,event_date,description) VALUES(?,'JOIN',?,'Nhân viên được thêm vào hệ thống')")) {
                    ev.setInt(1, id);
                    ev.setDate(2, java.sql.Date.valueOf(hireDate));
                    ev.executeUpdate();
                }
                c.commit();
                return id;
            } catch (Exception ex) {
                c.rollback();
                if (ex instanceof SQLException se)
                    throw se;
                throw new SQLException(ex);
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void update(int id, String fullName, String email, int jobId, LocalDate hireDate, LocalDate resignationDate,
            String status) throws SQLException {
        String q = "UPDATE employees e JOIN jobs j ON j.job_id=? SET e.job_id=j.job_id,e.department_id=j.department_id,e.full_name=?,e.email=?,e.base_salary=GREATEST(e.base_salary,j.base_salary_min),e.hire_date=?,e.resignation_date=?,e.status=? WHERE e.employee_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
            p.setInt(1, jobId);
            p.setString(2, fullName);
            p.setString(3, email);
            p.setDate(4, java.sql.Date.valueOf(hireDate));
            p.setDate(5, resignationDate == null ? null : java.sql.Date.valueOf(resignationDate));
            p.setString(6, status);
            p.setInt(7, id);
            p.executeUpdate();
            try (PreparedStatement u = c.prepareStatement("UPDATE users SET is_active=? WHERE employee_id=?")) {
                u.setBoolean(1, "ACTIVE".equals(status));
                u.setInt(2, id);
                u.executeUpdate();
            }
        }
    }

    public void setAccountActive(int id, boolean active) throws SQLException {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement("UPDATE users SET is_active=? WHERE employee_id=?")) {
            p.setBoolean(1, active);
            p.setInt(2, id);
            p.executeUpdate();
        }
    }

    public boolean accountActive(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement("SELECT is_active FROM users WHERE employee_id=?")) {
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                String[] qs = { "DELETE FROM project_allocations WHERE employee_id=?",
                        "DELETE FROM payrolls WHERE employee_id=?", "DELETE FROM overtime_records WHERE employee_id=?",
                        "DELETE FROM leave_records WHERE employee_id=?",
                        "DELETE FROM attendance_records WHERE employee_id=?",
                        "DELETE FROM rewards_disciplines WHERE employee_id=?",
                        "DELETE FROM employment_events WHERE employee_id=?", "DELETE FROM users WHERE employee_id=?",
                        "DELETE FROM employees WHERE employee_id=?" };
                for (String q : qs)
                    try (PreparedStatement p = c.prepareStatement(q)) {
                        p.setInt(1, id);
                        p.executeUpdate();
                    }
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                if (ex instanceof SQLException se)
                    throw se;
                throw new SQLException(ex);
            } finally {
                c.setAutoCommit(true);
            }
        }
    }
}
