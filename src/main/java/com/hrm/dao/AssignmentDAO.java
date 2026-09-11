package com.hrm.dao;

import com.hrm.config.DBConnection;
import java.sql.*;
import java.util.*;

public class AssignmentDAO {
<<<<<<< HEAD
    public List<String[]> employees(String text,String department,String job,String project,String status) throws SQLException {
        List<String[]> a=new ArrayList<>();
        String q="SELECT e.employee_id,CONCAT('NV',LPAD(e.employee_id,4,'0')),e.full_name,d.department_name,j.job_title,COALESCE(GROUP_CONCAT(DISTINCT p.project_name ORDER BY p.project_name SEPARATOR ', '),'Chưa phân công'),COALESCE(GROUP_CONCAT(DISTINCT pa.role_in_project ORDER BY pa.role_in_project SEPARATOR ', '),'-'),e.status " +
                "FROM employees e JOIN departments d ON d.department_id=e.department_id JOIN jobs j ON j.job_id=e.job_id LEFT JOIN project_allocations pa ON pa.employee_id=e.employee_id LEFT JOIN projects p ON p.project_id=pa.project_id " +
                "WHERE (?='' OR e.full_name LIKE ? OR CONCAT('NV',LPAD(e.employee_id,4,'0')) LIKE ? OR e.email LIKE ?) AND (?='Tất cả' OR d.department_name=?) AND (?='Tất cả' OR j.job_title=?) AND (?='Tất cả' OR p.project_name=?) AND (?='Tất cả' OR e.status=?) " +
                "GROUP BY e.employee_id,e.full_name,d.department_name,j.job_title,e.status ORDER BY e.employee_id";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){
            int i=1;p.setString(i++,text);p.setString(i++,"%"+text+"%");p.setString(i++,"%"+text+"%");p.setString(i++,"%"+text+"%");p.setString(i++,department);p.setString(i++,department);p.setString(i++,job);p.setString(i++,job);p.setString(i++,project);p.setString(i++,project);p.setString(i++,status);p.setString(i++,status);
            try(ResultSet r=p.executeQuery()){while(r.next())a.add(new String[]{String.format("NV%04d",r.getInt(1)),r.getString(3),r.getString(4),r.getString(5),r.getString(6),r.getString(7),r.getString(8)});}
        }return a;
    }
    public List<String[]> employees()throws SQLException{return employees("","Tất cả","Tất cả","Tất cả","Tất cả");}
    public List<String[]> departments()throws SQLException{List<String[]>a=new ArrayList<>();try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT department_id,department_name FROM departments ORDER BY department_name");ResultSet r=p.executeQuery()){while(r.next())a.add(new String[]{r.getString(1),r.getString(2)});}return a;}
    public List<String[]> jobs()throws SQLException{List<String[]>a=new ArrayList<>();String q="SELECT j.job_id,j.job_title,d.department_name FROM jobs j JOIN departments d ON d.department_id=j.department_id ORDER BY d.department_name,j.job_title";try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){while(r.next())a.add(new String[]{r.getString(1),r.getString(2),r.getString(3)});}return a;}
    public List<String[]> projects()throws SQLException{List<String[]>a=new ArrayList<>();try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT project_id,project_name,status FROM projects ORDER BY project_id");ResultSet r=p.executeQuery()){while(r.next())a.add(new String[]{r.getString(1),r.getString(2),r.getString(3)});}return a;}
    public void assignJob(int employeeId,int jobId)throws SQLException{String q="UPDATE employees e JOIN jobs j ON j.job_id=? SET e.job_id=j.job_id,e.department_id=j.department_id WHERE e.employee_id=?";try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setInt(1,jobId);p.setInt(2,employeeId);if(p.executeUpdate()==0)throw new SQLException("Không tìm thấy nhân viên hoặc vị trí công việc.");}}
    public void assignProject(int employeeId,int projectId,String role)throws SQLException{String q="INSERT INTO project_allocations(project_id,employee_id,role_in_project,assigned_date) VALUES(?,?,?,CURDATE()) ON DUPLICATE KEY UPDATE role_in_project=VALUES(role_in_project)";try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setInt(1,projectId);p.setInt(2,employeeId);p.setString(3,role);p.executeUpdate();}}
    public List<String[]> projectAssignments(int employeeId)throws SQLException{List<String[]>a=new ArrayList<>();String q="SELECT p.project_id,p.project_name,COALESCE(pa.role_in_project,'-') FROM project_allocations pa JOIN projects p ON p.project_id=pa.project_id WHERE pa.employee_id=? ORDER BY p.project_id";try(Connection c=DBConnection.getConnection();PreparedStatement ps=c.prepareStatement(q)){ps.setInt(1,employeeId);try(ResultSet r=ps.executeQuery()){while(r.next())a.add(new String[]{r.getString(1),r.getString(2),r.getString(3)});}}return a;}
    public void removeProject(int employeeId,int projectId)throws SQLException{try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM project_allocations WHERE employee_id=? AND project_id=?")){p.setInt(1,employeeId);p.setInt(2,projectId);p.executeUpdate();}}
=======
    public List<String[]> employees(String text, String department, String job, String project, String status)
            throws SQLException {
        List<String[]> a = new ArrayList<>();
        String q = "SELECT e.employee_id,CONCAT('NV',LPAD(e.employee_id,4,'0')),e.full_name,d.department_name,j.job_title,COALESCE(GROUP_CONCAT(DISTINCT p.project_name ORDER BY p.project_name SEPARATOR ', '),'Chưa phân công'),COALESCE(GROUP_CONCAT(DISTINCT pa.role_in_project ORDER BY pa.role_in_project SEPARATOR ', '),'-'),e.status "
                +
                "FROM employees e JOIN departments d ON d.department_id=e.department_id JOIN jobs j ON j.job_id=e.job_id LEFT JOIN project_allocations pa ON pa.employee_id=e.employee_id LEFT JOIN projects p ON p.project_id=pa.project_id "
                +
                "WHERE (?='' OR e.full_name LIKE ? OR CONCAT('NV',LPAD(e.employee_id,4,'0')) LIKE ? OR e.email LIKE ?) AND (?='Tất cả' OR d.department_name=?) AND (?='Tất cả' OR j.job_title=?) AND (?='Tất cả' OR p.project_name=?) AND (?='Tất cả' OR e.status=?) "
                +
                "GROUP BY e.employee_id,e.full_name,d.department_name,j.job_title,e.status ORDER BY e.employee_id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
            int i = 1;
            p.setString(i++, text);
            p.setString(i++, "%" + text + "%");
            p.setString(i++, "%" + text + "%");
            p.setString(i++, "%" + text + "%");
            p.setString(i++, department);
            p.setString(i++, department);
            p.setString(i++, job);
            p.setString(i++, job);
            p.setString(i++, project);
            p.setString(i++, project);
            p.setString(i++, status);
            p.setString(i++, status);
            try (ResultSet r = p.executeQuery()) {
                while (r.next())
                    a.add(new String[] { String.format("NV%04d", r.getInt(1)), r.getString(3), r.getString(4),
                            r.getString(5), r.getString(6), r.getString(7), r.getString(8) });
            }
        }
        return a;
    }

    public List<String[]> employees() throws SQLException {
        return employees("", "Tất cả", "Tất cả", "Tất cả", "Tất cả");
    }

    public List<String[]> departments() throws SQLException {
        List<String[]> a = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement(
                        "SELECT department_id,department_name FROM departments ORDER BY department_name");
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

    public List<String[]> projects() throws SQLException {
        List<String[]> a = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c
                        .prepareStatement("SELECT project_id,project_name,status FROM projects ORDER BY project_id");
                ResultSet r = p.executeQuery()) {
            while (r.next())
                a.add(new String[] { r.getString(1), r.getString(2), r.getString(3) });
        }
        return a;
    }

    public void assignJob(int employeeId, int jobId) throws SQLException {
        String q = "UPDATE employees e JOIN jobs j ON j.job_id=? SET e.job_id=j.job_id,e.department_id=j.department_id WHERE e.employee_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
            p.setInt(1, jobId);
            p.setInt(2, employeeId);
            if (p.executeUpdate() == 0)
                throw new SQLException("Không tìm thấy nhân viên hoặc vị trí công việc.");
        }
    }

    public void assignProject(int employeeId, int projectId, String role) throws SQLException {
        String q = "INSERT INTO project_allocations(project_id,employee_id,role_in_project,assigned_date) VALUES(?,?,?,CURDATE()) ON DUPLICATE KEY UPDATE role_in_project=VALUES(role_in_project)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
            p.setInt(1, projectId);
            p.setInt(2, employeeId);
            p.setString(3, role);
            p.executeUpdate();
        }
    }

    public List<String[]> projectAssignments(int employeeId) throws SQLException {
        List<String[]> a = new ArrayList<>();
        String q = "SELECT p.project_id,p.project_name,COALESCE(pa.role_in_project,'-') FROM project_allocations pa JOIN projects p ON p.project_id=pa.project_id WHERE pa.employee_id=? ORDER BY p.project_id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
            ps.setInt(1, employeeId);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next())
                    a.add(new String[] { r.getString(1), r.getString(2), r.getString(3) });
            }
        }
        return a;
    }

    public void removeProject(int employeeId, int projectId) throws SQLException {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c
                        .prepareStatement("DELETE FROM project_allocations WHERE employee_id=? AND project_id=?")) {
            p.setInt(1, employeeId);
            p.setInt(2, projectId);
            p.executeUpdate();
        }
    }
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
}
