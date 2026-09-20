USE hrm_enterprise_db;

-- Nâng cấp chức năng: tự động tính OT khi nhân viên chấm công ra sau 15:30.
-- 15:30 là giờ kết thúc ca chuẩn.
ALTER TABLE overtime_records
  ADD UNIQUE KEY uk_ot_employee_date(employee_id,work_date);

-- Bổ sung OT cho các bản ghi chấm công lịch sử đã có nhưng chưa có overtime_records.
INSERT INTO overtime_records(employee_id,work_date,hours,hourly_rate,multiplier)
SELECT ar.employee_id, ar.work_date,
       ROUND(TIME_TO_SEC(TIMEDIFF(ar.check_out,'15:30:00'))/3600,2),
       e.base_salary/26/8, 1.5
FROM attendance_records ar
JOIN employees e ON e.employee_id=ar.employee_id
WHERE ar.check_out>'15:30:00'
ON DUPLICATE KEY UPDATE
  hours=VALUES(hours),
  hourly_rate=VALUES(hourly_rate),
  multiplier=VALUES(multiplier);

-- Ví dụ:
-- 15:30 -> 0.00 giờ OT
-- 16:00 -> 0.50 giờ OT
-- 17:00 -> 1.50 giờ OT
-- 18:30 -> 3.00 giờ OT
