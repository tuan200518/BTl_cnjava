package com.hrm.dao;

import com.hrm.config.DBConnection;
import java.sql.*;
import java.util.*;

public class ReportDAO {
    public List<String[]> staffing() throws SQLException {
        List<String[]> a = new ArrayList<>();
        String q = "SELECT d.department_name,j.job_title,j.target_headcount,COUNT(CASE WHEN e.status='ACTIVE' THEN 1 END),GREATEST(0,j.target_headcount-COUNT(CASE WHEN e.status='ACTIVE' THEN 1 END)) FROM jobs j JOIN departments d ON d.department_id=j.department_id LEFT JOIN employees e ON e.job_id=j.job_id GROUP BY j.job_id,d.department_name,j.job_title,j.target_headcount ORDER BY d.department_name,j.job_title";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement(q);
                ResultSet r = p.executeQuery()) {
            while (r.next())
                a.add(new String[] { r.getString(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5) });
        }
        return a;
    }

    public List<String[]> projects() throws SQLException {
        List<String[]> a = new ArrayList<>();
        String q = "SELECT p.project_id,p.project_name,p.required_headcount,COUNT(DISTINCT CASE WHEN e.status='ACTIVE' THEN pa.employee_id END),GREATEST(0,p.required_headcount-COUNT(DISTINCT CASE WHEN e.status='ACTIVE' THEN pa.employee_id END)),ROUND(100*COUNT(DISTINCT CASE WHEN e.status='ACTIVE' THEN pa.employee_id END)/NULLIF(p.required_headcount,0),1),p.start_date,p.status FROM projects p LEFT JOIN project_allocations pa ON pa.project_id=p.project_id LEFT JOIN employees e ON e.employee_id=pa.employee_id GROUP BY p.project_id,p.project_name,p.required_headcount,p.start_date,p.status ORDER BY p.project_id";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement(q);
                ResultSet r = p.executeQuery()) {
            while (r.next())
                a.add(new String[] { r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getString(6),
                        r.getString(7), r.getString(8) });
        }
        return a;
    }

    public List<String[]> employeesByJob(String job, String text) throws SQLException {
        List<String[]> a = new ArrayList<>();
        String q = "SELECT CONCAT('NV',LPAD(e.employee_id,4,'0')),e.full_name,j.job_title,d.department_name,COALESCE((SELECT GROUP_CONCAT(DISTINCT pa.role_in_project ORDER BY pa.role_in_project SEPARATOR ', ') FROM project_allocations pa WHERE pa.employee_id=e.employee_id),'-'),e.status FROM employees e JOIN departments d ON d.department_id=e.department_id JOIN jobs j ON j.job_id=e.job_id WHERE j.job_title=? AND (?='' OR e.full_name LIKE ? OR CONCAT('NV',LPAD(e.employee_id,4,'0')) LIKE ?) ORDER BY e.employee_id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
            ps.setString(1, job);
            ps.setString(2, text);
            ps.setString(3, "%" + text + "%");
            ps.setString(4, "%" + text + "%");
            try (ResultSet r = ps.executeQuery()) {
                while (r.next())
                    a.add(new String[] { r.getString(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5),
                            r.getString(6) });
            }
        }
        return a;
    }

    public List<String[]> employeesByJob(String job) throws SQLException {
        return employeesByJob(job, "");
    }

    public List<String[]> employeesByProject(String project, String text) throws SQLException {
        List<String[]> a = new ArrayList<>();
        String q = "SELECT CONCAT('NV',LPAD(e.employee_id,4,'0')),e.full_name,j.job_title,d.department_name,COALESCE(pa.role_in_project,'-'),e.status FROM project_allocations pa JOIN employees e ON e.employee_id=pa.employee_id JOIN departments d ON d.department_id=e.department_id JOIN jobs j ON j.job_id=e.job_id JOIN projects p ON p.project_id=pa.project_id WHERE p.project_name=? AND (?='' OR e.full_name LIKE ? OR CONCAT('NV',LPAD(e.employee_id,4,'0')) LIKE ?) ORDER BY e.employee_id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
            ps.setString(1, project);
            ps.setString(2, text);
            ps.setString(3, "%" + text + "%");
            ps.setString(4, "%" + text + "%");
            try (ResultSet r = ps.executeQuery()) {
                while (r.next())
                    a.add(new String[] { r.getString(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5),
                            r.getString(6) });
            }
        }
        return a;
    }

    public List<String[]> employeesByProject(String project) throws SQLException {
        return employeesByProject(project, "");
    }

    public double totalPaid(int year) throws SQLException {
        String q = "SELECT COALESCE(SUM(net_salary),0) FROM payrolls WHERE status='SENT' AND (?=0 OR pay_year=?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
            p.setInt(1, year);
            p.setInt(2, year);
            try (ResultSet r = p.executeQuery()) {
                r.next();
                return r.getDouble(1);
            }
        }
    }

    public int count(String table) throws SQLException {
        if (!table.matches("employees|departments|projects|payrolls"))
            throw new IllegalArgumentException();
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement("SELECT COUNT(*) FROM " + table);
                ResultSet r = p.executeQuery()) {
            r.next();
            return r.getInt(1);
        }
    }
}
