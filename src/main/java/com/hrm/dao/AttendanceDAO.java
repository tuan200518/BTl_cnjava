package com.hrm.dao;

import com.hrm.config.DBConnection;
import java.sql.*;
import java.util.*;

public class AttendanceDAO {
    public List<String[]> find(int year, String text) throws SQLException {
        List<String[]> a = new ArrayList<>();
        String q = "SELECT CONCAT('NV',LPAD(e.employee_id,4,'0')),e.full_name,"
                + "COUNT(ar.attendance_id),COALESCE(SUM(ar.work_days),0),"
                + "COALESCE(ot.total_hours,0),"
                + "COALESCE(SUM(CASE WHEN ar.check_in > '07:30:00' THEN 100000 ELSE 0 END),0),"
                + "COALESCE(SUM(CASE WHEN ar.check_out < '15:30:00' THEN 100000 ELSE 0 END),0) "
                + "FROM employees e "
                + "LEFT JOIN attendance_records ar ON ar.employee_id=e.employee_id "
                + " AND (?=0 OR YEAR(ar.work_date)=?) "
                + "LEFT JOIN (SELECT employee_id,SUM(hours) total_hours FROM overtime_records "
                + " WHERE (?=0 OR YEAR(work_date)=?) GROUP BY employee_id) ot ON ot.employee_id=e.employee_id "
                + "WHERE (?=0 OR (e.hire_date<=STR_TO_DATE(CONCAT(?,'-12-31'),'%Y-%m-%d') "
                + " AND (e.resignation_date IS NULL OR e.resignation_date>=STR_TO_DATE(CONCAT(?,'-01-01'),'%Y-%m-%d')))) "
                + "AND (?='' OR e.full_name LIKE ? OR CONCAT('NV',LPAD(e.employee_id,4,'0')) LIKE ? OR e.email LIKE ?) "
                + "GROUP BY e.employee_id,e.full_name,ot.total_hours ORDER BY e.employee_id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
            int i = 1;
            p.setInt(i++, year); p.setInt(i++, year);
            p.setInt(i++, year); p.setInt(i++, year);
            p.setInt(i++, year); p.setInt(i++, year); p.setInt(i++, year);
            p.setString(i++, text); p.setString(i++, "%" + text + "%"); p.setString(i++, "%" + text + "%"); p.setString(i++, "%" + text + "%");
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) a.add(new String[] {rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),String.valueOf(rs.getLong(6)+rs.getLong(7))});
            }
        }
        return a;
    }
    public List<String[]> find(int year) throws SQLException { return find(year, ""); }

    public String[] today(int employeeId) throws SQLException {
        String sql="SELECT work_date,check_in,check_out,status FROM attendance_records WHERE employee_id=? AND work_date=CURDATE()";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,employeeId); try(ResultSet r=p.executeQuery()){
                if(r.next()) return new String[]{String.valueOf(r.getDate(1)),r.getTime(2)==null?"Chưa chấm":r.getTime(2).toString(),r.getTime(3)==null?"Chưa chấm":r.getTime(3).toString(),r.getString(4)};
            }
        }
        return new String[]{java.time.LocalDate.now().toString(),"Chưa chấm","Chưa chấm","Chưa có bản ghi"};
    }

    public void clockIn(int employeeId) throws SQLException {
        String active="SELECT status FROM employees WHERE employee_id=?";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(active)){
            p.setInt(1,employeeId); try(ResultSet r=p.executeQuery()){
                if(!r.next() || !"ACTIVE".equals(r.getString(1))) throw new SQLException("Tài khoản không còn trạng thái nhân viên đang làm việc.");
            }
        }
        String sql="INSERT INTO attendance_records(employee_id,work_date,work_days,check_in,status) VALUES (?,CURDATE(),1,CURTIME(),'PRESENT')";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,employeeId); p.executeUpdate();
        } catch(SQLIntegrityConstraintViolationException ex){ throw new SQLException("Bạn đã chấm công vào hôm nay rồi.",ex); }
    }

    public void clockOut(int employeeId) throws SQLException {
        String sql="UPDATE attendance_records SET check_out=CURTIME() WHERE employee_id=? AND work_date=CURDATE() AND check_in IS NOT NULL AND check_out IS NULL";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql)){
            c.setAutoCommit(false);
            try {
                p.setInt(1,employeeId);
                if(p.executeUpdate()==0) throw new SQLException("Chưa chấm công vào hôm nay hoặc bạn đã chấm công ra rồi.");

                // Giờ làm thêm được tính tự động từ thời điểm chuẩn kết thúc ca 15:30.
                // Ví dụ: ra 16:00 -> OT 0.50 giờ; ra 17:00 -> OT 1.50 giờ.
                String otSql = "INSERT INTO overtime_records(employee_id,work_date,hours,hourly_rate,multiplier) "
                        + "SELECT a.employee_id,a.work_date,"
                        + "ROUND(TIME_TO_SEC(TIMEDIFF(a.check_out,'15:30:00'))/3600,2),"
                        + "e.base_salary/26/8,1.5 "
                        + "FROM attendance_records a JOIN employees e ON e.employee_id=a.employee_id "
                        + "WHERE a.employee_id=? AND a.work_date=CURDATE() AND a.check_out>'15:30:00' "
                        + "ON DUPLICATE KEY UPDATE hours=VALUES(hours), hourly_rate=VALUES(hourly_rate), multiplier=VALUES(multiplier)";
                // Dùng INSERT ... ON DUPLICATE KEY sau khi bổ sung unique key trong database.
                try(PreparedStatement ot=c.prepareStatement(otSql)){
                    ot.setInt(1,employeeId);
                    ot.executeUpdate();
                }
                c.commit();
            } catch(Exception ex) {
                c.rollback();
                if(ex instanceof SQLException se) throw se;
                throw new SQLException(ex);
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<String[]> history(int employeeId) throws SQLException {
        List<String[]> rows=new ArrayList<>();
        String sql="SELECT ar.work_date,ar.check_in,ar.check_out,ar.status,"
                + "CASE WHEN ar.check_in>'07:30:00' THEN 100000 ELSE 0 END,"
                + "CASE WHEN ar.check_out<'15:30:00' THEN 100000 ELSE 0 END,"
                + "COALESCE(ot.hours,0) "
                + "FROM attendance_records ar "
                + "LEFT JOIN overtime_records ot ON ot.employee_id=ar.employee_id AND ot.work_date=ar.work_date "
                + "WHERE ar.employee_id=? ORDER BY ar.work_date DESC";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,employeeId);try(ResultSet r=p.executeQuery()){
                while(r.next()) rows.add(new String[]{String.valueOf(r.getDate(1)),r.getTime(2)==null?"—":r.getTime(2).toString(),r.getTime(3)==null?"—":r.getTime(3).toString(),r.getString(4),
                        String.format("%.2f",r.getDouble(7)),
                        String.format("%,.0f",r.getDouble(5)),String.format("%,.0f",r.getDouble(6)),
                        String.format("%,.0f",r.getDouble(5)+r.getDouble(6))});
            }
        }
        return rows;
    }
}
