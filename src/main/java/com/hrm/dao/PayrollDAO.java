package com.hrm.dao;

import com.hrm.config.DBConnection;
import com.hrm.model.Payroll;
import java.sql.*;
import java.util.*;
import com.hrm.service.PayrollCalculator;

public class PayrollDAO {
  public List<Payroll> findForHR(int year) throws SQLException {
    return query(
        "SELECT p.payroll_id,p.employee_id,CONCAT('NV',LPAD(p.employee_id,4,'0')),e.full_name,p.pay_month,p.pay_year,p.base_salary,p.overtime_pay,p.bonus,p.deduction,p.net_salary,COALESCE(e.dependent_count,0),p.status FROM payrolls p JOIN employees e ON e.employee_id=p.employee_id WHERE (?=0 OR p.pay_year=?) ORDER BY p.employee_id,p.pay_month",
        year, false, 0);
  }

  public List<Payroll> findMine(int employeeId, int year) throws SQLException {
    return query(
        "SELECT p.payroll_id,p.employee_id,CONCAT('NV',LPAD(p.employee_id,4,'0')),e.full_name,p.pay_month,p.pay_year,p.base_salary,p.overtime_pay,p.bonus,p.deduction,p.net_salary,COALESCE(e.dependent_count,0),p.status FROM payrolls p JOIN employees e ON e.employee_id=p.employee_id WHERE p.employee_id=? AND p.pay_year>=YEAR(e.hire_date) AND (?=0 OR p.pay_year=?) AND p.status='SENT' ORDER BY p.pay_year DESC,p.pay_month DESC",
        year, true, employeeId);
  }

  public List<Payroll> history(String text, int year, int month, String status) throws SQLException {
    String q = "SELECT p.payroll_id,p.employee_id,CONCAT('NV',LPAD(p.employee_id,4,'0')),e.full_name,p.pay_month,p.pay_year,p.base_salary,p.overtime_pay,p.bonus,p.deduction,p.net_salary,COALESCE(e.dependent_count,0),p.status FROM payrolls p JOIN employees e ON e.employee_id=p.employee_id WHERE (?=0 OR p.pay_year=?) AND (?='' OR CONCAT('NV',LPAD(p.employee_id,4,'0')) LIKE ? OR e.full_name LIKE ? OR e.email LIKE ?) AND (?=0 OR p.pay_month=?) AND (?='Tất cả' OR p.status=?) ORDER BY p.created_at DESC,p.payroll_id DESC";
    List<Payroll> a = new ArrayList<>();
    try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
      int i = 1;
      p.setInt(i++, year);
      p.setInt(i++, year);
      p.setString(i++, text);
      p.setString(i++, "%" + text + "%");
      p.setString(i++, "%" + text + "%");
      p.setString(i++, "%" + text + "%");
      p.setInt(i++, month);
      p.setInt(i++, month);
      p.setString(i++, status);
      p.setString(i++, status);
      try (ResultSet r = p.executeQuery()) {
        while (r.next())
          a.add(map(r));
      }
    }
    return a;
  }

  private List<Payroll> query(String q, int year, boolean mine, int emp) throws SQLException {
    List<Payroll> a = new ArrayList<>();
    try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
      if (mine) {
        p.setInt(1, emp);
        p.setInt(2, year);
        p.setInt(3, year);
      } else {
        p.setInt(1, year);
        p.setInt(2, year);
      }
      try (ResultSet r = p.executeQuery()) {
        while (r.next())
          a.add(map(r));
      }
    }
    return a;
  }

  private Payroll map(ResultSet r) throws SQLException {
    int id = r.getInt(1), emp = r.getInt(2), month = r.getInt(5), year = r.getInt(6);
    double base = r.getDouble(7), ot = r.getDouble(8), bonus = r.getDouble(9), ded = r.getDouble(10);
    int deps = r.getInt(12);
    double net = PayrollCalculator.calculate(year, base, ot, bonus, ded, deps).net();
    return new Payroll(id, emp, r.getString(3), r.getString(4), month, year, base, ot, bonus, ded, net, r.getString(13),
        deps);
  }

  public List<com.hrm.model.Employee> findEmployeesForPayroll(String text, int year, int month, String department,
      String job) throws SQLException {
    List<com.hrm.model.Employee> out = new ArrayList<>();
    String q = "SELECT e.employee_id,CONCAT('NV',LPAD(e.employee_id,4,'0')),e.full_name,e.email,d.department_name,j.job_title,e.base_salary,e.hire_date,e.resignation_date,e.status,COALESCE(u.username,CONCAT('NV',LPAD(e.employee_id,4,'0'))) FROM employees e JOIN departments d ON d.department_id=e.department_id JOIN jobs j ON j.job_id=e.job_id LEFT JOIN users u ON u.employee_id=e.employee_id WHERE e.status='ACTIVE' AND e.hire_date<=LAST_DAY(STR_TO_DATE(CONCAT(?,'-',?,'-01'),'%Y-%m-%d')) AND (e.resignation_date IS NULL OR e.resignation_date>=STR_TO_DATE(CONCAT(?,'-',?,'-01'),'%Y-%m-%d')) AND (?='' OR e.full_name LIKE ? OR e.email LIKE ? OR CONCAT('NV',LPAD(e.employee_id,4,'0')) LIKE ?) AND (?='Tất cả' OR d.department_name=?) AND (?='Tất cả' OR j.job_title=?) ORDER BY e.employee_id";
    try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
      int i = 1;
      p.setInt(i++, year);
      p.setInt(i++, month);
      p.setInt(i++, year);
      p.setInt(i++, month);
      p.setString(i++, text);
      p.setString(i++, "%" + text + "%");
      p.setString(i++, "%" + text + "%");
      p.setString(i++, "%" + text + "%");
      p.setString(i++, department);
      p.setString(i++, department);
      p.setString(i++, job);
      p.setString(i++, job);
      try (ResultSet rs = p.executeQuery()) {
        while (rs.next()) {
          java.sql.Date h = rs.getDate(8), d = rs.getDate(9);
          out.add(new com.hrm.model.Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
              rs.getString(5), rs.getString(6), rs.getDouble(7), h.toLocalDate(), d == null ? null : d.toLocalDate(),
              rs.getString(10), rs.getString(11)));
        }
      }
    }
    return out;
  }

  public void createDraft(int employeeId, int month, int year) throws SQLException {
    String q = """
          SELECT e.base_salary,
            COALESCE((SELECT SUM(o.hours*o.hourly_rate*o.multiplier) FROM overtime_records o WHERE o.employee_id=e.employee_id AND MONTH(o.work_date)=? AND YEAR(o.work_date)=?),0) ot,
            COALESCE((SELECT SUM(r.amount) FROM rewards_disciplines r WHERE r.employee_id=e.employee_id AND r.type='REWARD' AND MONTH(r.record_date)=? AND YEAR(r.record_date)=?),0) bonus,
            COALESCE((SELECT SUM(r.amount) FROM rewards_disciplines r WHERE r.employee_id=e.employee_id AND r.type='DISCIPLINE' AND MONTH(r.record_date)=? AND YEAR(r.record_date)=?),0) deduction,
            COALESCE(e.dependent_count,0) dependents
          FROM employees e
          WHERE e.employee_id=? AND e.status='ACTIVE'
            AND e.hire_date<=LAST_DAY(STR_TO_DATE(CONCAT(?,'-',?,'-01'),'%Y-%m-%d'))
            AND (e.resignation_date IS NULL OR e.resignation_date>=STR_TO_DATE(CONCAT(?,'-',?,'-01'),'%Y-%m-%d'))
        """;
    try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
      int i = 1;
      p.setInt(i++, month);
      p.setInt(i++, year);
      p.setInt(i++, month);
      p.setInt(i++, year);
      p.setInt(i++, month);
      p.setInt(i++, year);
      p.setInt(i++, employeeId);
      p.setInt(i++, year);
      p.setInt(i++, month);
      p.setInt(i++, year);
      p.setInt(i++, month);
      try (ResultSet r = p.executeQuery()) {
        if (!r.next())
          throw new SQLException("Nhân viên không còn hoạt động hoặc không làm việc trong kỳ lương.");
        double base = r.getDouble("base_salary"), ot = r.getDouble("ot"), bonus = r.getDouble("bonus"),
            deduction = r.getDouble("deduction");
        int dependents = r.getInt("dependents");
        double gross = base + ot + bonus;
        double insurance = com.hrm.service.PayrollCalculator.insurance(base);
        double taxable = com.hrm.service.PayrollCalculator.taxableIncome(gross, insurance, dependents);
        double pit = com.hrm.service.PayrollCalculator.pit(taxable);
        double net = com.hrm.service.PayrollCalculator.net(gross, insurance, pit, deduction);
        String up = "INSERT INTO payrolls(employee_id,pay_month,pay_year,base_salary,overtime_pay,bonus,deduction,net_salary,status) VALUES(?,?,?,?,?,?,?,?, 'DRAFT') ON DUPLICATE KEY UPDATE base_salary=VALUES(base_salary),overtime_pay=VALUES(overtime_pay),bonus=VALUES(bonus),deduction=VALUES(deduction),net_salary=VALUES(net_salary),status=IF(status='SENT','SENT','DRAFT')";
        try (PreparedStatement u = c.prepareStatement(up)) {
          int k = 1;
          u.setInt(k++, employeeId);
          u.setInt(k++, month);
          u.setInt(k++, year);
          u.setDouble(k++, base);
          u.setDouble(k++, ot);
          u.setDouble(k++, bonus);
          u.setDouble(k++, deduction);
          u.setDouble(k++, net);
          u.executeUpdate();
        }
      }
    }
  }

  public int dependentCount(int employeeId) throws SQLException {
    try (Connection c = DBConnection.getConnection();
        PreparedStatement p = c
            .prepareStatement("SELECT COALESCE(dependent_count,0) FROM employees WHERE employee_id=?")) {
      p.setInt(1, employeeId);
      try (ResultSet r = p.executeQuery()) {
        return r.next() ? r.getInt(1) : 0;
      }
    }
  }

  public void send(int payrollId, int employeeId) throws SQLException {
    String q = "UPDATE payrolls SET status='SENT',sent_at=NOW() WHERE payroll_id=? AND employee_id=?";
    try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
      p.setInt(1, payrollId);
      p.setInt(2, employeeId);
      if (p.executeUpdate() == 0)
        throw new SQLException("Không thể gửi phiếu lương. Kiểm tra lại phiếu đã chọn.");
    }
  }
}
