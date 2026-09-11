USE hrm_enterprise_db;

-- 1) Làm lại dữ liệu phân công dự án để demo có nhiều mức thiếu nhân sự.
-- Không xóa nhân viên; chỉ làm lại project_allocations mẫu.
DELETE FROM project_allocations;

INSERT INTO project_allocations(project_id,employee_id,role_in_project,assigned_date)
SELECT CASE
 WHEN MOD(e.employee_id,100) BETWEEN 0 AND 4 THEN 1
 WHEN MOD(e.employee_id,100) BETWEEN 5 AND 7 THEN 2
 WHEN MOD(e.employee_id,100) BETWEEN 8 AND 11 THEN 3
 WHEN MOD(e.employee_id,100) BETWEEN 12 AND 13 THEN 4
 WHEN MOD(e.employee_id,100) BETWEEN 14 AND 16 THEN 5
 WHEN MOD(e.employee_id,100) BETWEEN 17 AND 21 THEN 6
 WHEN MOD(e.employee_id,100)=22 THEN 7
 WHEN MOD(e.employee_id,100) BETWEEN 23 AND 24 THEN 8
 WHEN MOD(e.employee_id,100) BETWEEN 25 AND 26 THEN 9
 WHEN MOD(e.employee_id,100) BETWEEN 27 AND 31 THEN 10
 WHEN MOD(e.employee_id,100) BETWEEN 32 AND 34 THEN 11
 WHEN MOD(e.employee_id,100) BETWEEN 35 AND 37 THEN 12 END,
e.employee_id,j.job_title,e.hire_date
FROM employees e JOIN jobs j ON j.job_id=e.job_id
WHERE e.status='ACTIVE' AND MOD(e.employee_id,100) BETWEEN 0 AND 37;

-- 2) Xóa view cũ nếu cần và tạo lại view thống kê dự án.
DROP VIEW IF EXISTS v_project_staffing;
CREATE VIEW v_project_staffing AS
SELECT p.project_id,p.project_name,p.required_headcount,
       COUNT(DISTINCT CASE WHEN e.status='ACTIVE' THEN pa.employee_id END) current_headcount,
       GREATEST(0,p.required_headcount-COUNT(DISTINCT CASE WHEN e.status='ACTIVE' THEN pa.employee_id END)) missing_headcount
FROM projects p
LEFT JOIN project_allocations pa ON pa.project_id=p.project_id
LEFT JOIN employees e ON e.employee_id=pa.employee_id
GROUP BY p.project_id,p.project_name,p.required_headcount;

-- Kiểm tra sau khi chạy:
-- SELECT * FROM v_project_staffing ORDER BY project_id;


-- Bổ sung thông tin người phụ thuộc để tính giảm trừ gia cảnh.
ALTER TABLE employees ADD COLUMN IF NOT EXISTS dependent_count INT NOT NULL DEFAULT 0;
UPDATE employees SET dependent_count = CASE
  WHEN MOD(employee_id,20)<10 THEN 0
  WHEN MOD(employee_id,20)<16 THEN 1
  WHEN MOD(employee_id,20)<19 THEN 2
  ELSE 3 END
WHERE employee_id > 0;

-- Cập nhật các phiếu lương hiện có theo công thức PIT 2026.
UPDATE payrolls p JOIN employees e ON e.employee_id=p.employee_id
SET p.deduction=COALESCE((SELECT SUM(r.amount) FROM rewards_disciplines r WHERE r.employee_id=e.employee_id AND r.type='DISCIPLINE' AND MONTH(r.record_date)=p.pay_month AND YEAR(r.record_date)=p.pay_year),0),
    p.net_salary=GREATEST(0,(p.base_salary+p.overtime_pay+p.bonus)-(p.base_salary*0.105)-
      CASE
        WHEN GREATEST(0,(p.base_salary+p.overtime_pay+p.bonus)-(p.base_salary*0.105)-15500000-COALESCE(e.dependent_count,0)*6200000)<=10000000 THEN GREATEST(0,(p.base_salary+p.overtime_pay+p.bonus)-(p.base_salary*0.105)-15500000-COALESCE(e.dependent_count,0)*6200000)*0.05
        WHEN GREATEST(0,(p.base_salary+p.overtime_pay+p.bonus)-(p.base_salary*0.105)-15500000-COALESCE(e.dependent_count,0)*6200000)<=30000000 THEN 500000+(GREATEST(0,(p.base_salary+p.overtime_pay+p.bonus)-(p.base_salary*0.105)-15500000-COALESCE(e.dependent_count,0)*6200000)-10000000)*0.10
        WHEN GREATEST(0,(p.base_salary+p.overtime_pay+p.bonus)-(p.base_salary*0.105)-15500000-COALESCE(e.dependent_count,0)*6200000)<=60000000 THEN 2500000+(GREATEST(0,(p.base_salary+p.overtime_pay+p.bonus)-(p.base_salary*0.105)-15500000-COALESCE(e.dependent_count,0)*6200000)-30000000)*0.20
        WHEN GREATEST(0,(p.base_salary+p.overtime_pay+p.bonus)-(p.base_salary*0.105)-15500000-COALESCE(e.dependent_count,0)*6200000)<=100000000 THEN 8500000+(GREATEST(0,(p.base_salary+p.overtime_pay+p.bonus)-(p.base_salary*0.105)-15500000-COALESCE(e.dependent_count,0)*6200000)-60000000)*0.30
        ELSE 20500000+(GREATEST(0,(p.base_salary+p.overtime_pay+p.bonus)-(p.base_salary*0.105)-15500000-COALESCE(e.dependent_count,0)*6200000)-100000000)*0.35
      END-p.deduction);
