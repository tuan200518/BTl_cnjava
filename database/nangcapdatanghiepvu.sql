USE hrm_enterprise_db;

ALTER TABLE users MODIFY COLUMN role ENUM('ADMIN','HR','ACCOUNTANT','EMPLOYEE') NOT NULL;

CREATE TABLE IF NOT EXISTS employee_contracts (
 contract_id BIGINT AUTO_INCREMENT PRIMARY KEY, employee_id INT NOT NULL,
 contract_no VARCHAR(60) NOT NULL UNIQUE, contract_type VARCHAR(50) NOT NULL,
 start_date DATE NOT NULL, end_date DATE NULL, salary DECIMAL(15,2) NOT NULL,
 status ENUM('DRAFT','ACTIVE','EXPIRED','TERMINATED') NOT NULL DEFAULT 'ACTIVE',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(employee_id) REFERENCES employees(employee_id), INDEX idx_contract_employee(employee_id,start_date)
);
CREATE TABLE IF NOT EXISTS salary_history (
 salary_history_id BIGINT AUTO_INCREMENT PRIMARY KEY, employee_id INT NOT NULL,
 old_salary DECIMAL(15,2) NULL, new_salary DECIMAL(15,2) NOT NULL,
 effective_date DATE NOT NULL, reason VARCHAR(300), approved_by VARCHAR(80),
 FOREIGN KEY(employee_id) REFERENCES employees(employee_id), INDEX idx_salary_effective(employee_id,effective_date)
);
CREATE TABLE IF NOT EXISTS leave_requests (
 request_id BIGINT AUTO_INCREMENT PRIMARY KEY, employee_id INT NOT NULL,
 leave_type VARCHAR(50) NOT NULL, start_date DATE NOT NULL, end_date DATE NOT NULL,
 total_days DECIMAL(5,2) NOT NULL, reason VARCHAR(500),
 status ENUM('PENDING','APPROVED','REJECTED','CANCELLED') NOT NULL DEFAULT 'PENDING',
 reviewed_by VARCHAR(80), reviewed_at DATETIME,
 FOREIGN KEY(employee_id) REFERENCES employees(employee_id), INDEX idx_leave_status(status,start_date)
);
CREATE TABLE IF NOT EXISTS training_courses (
 course_id BIGINT AUTO_INCREMENT PRIMARY KEY, course_name VARCHAR(180) NOT NULL,
 provider VARCHAR(180), start_date DATE, end_date DATE, budget DECIMAL(15,2) DEFAULT 0,
 status ENUM('PLANNED','ONGOING','COMPLETED','CANCELLED') NOT NULL DEFAULT 'PLANNED'
);
CREATE TABLE IF NOT EXISTS training_participants (
 course_id BIGINT NOT NULL, employee_id INT NOT NULL, result VARCHAR(100), completed_at DATE,
 PRIMARY KEY(course_id,employee_id), FOREIGN KEY(course_id) REFERENCES training_courses(course_id),
 FOREIGN KEY(employee_id) REFERENCES employees(employee_id)
);
CREATE TABLE IF NOT EXISTS performance_reviews (
 review_id BIGINT AUTO_INCREMENT PRIMARY KEY, employee_id INT NOT NULL,
 period_start DATE NOT NULL, period_end DATE NOT NULL, score DECIMAL(5,2), reviewer VARCHAR(80),
 comments TEXT, improvement_plan TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(employee_id) REFERENCES employees(employee_id), INDEX idx_review_period(employee_id,period_start,period_end)
);
CREATE TABLE IF NOT EXISTS employee_benefits (
 benefit_id BIGINT AUTO_INCREMENT PRIMARY KEY, employee_id INT NOT NULL,
 benefit_type VARCHAR(80) NOT NULL, amount DECIMAL(15,2) NOT NULL DEFAULT 0,
 start_date DATE NOT NULL, end_date DATE NULL, status ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
 notes VARCHAR(300), FOREIGN KEY(employee_id) REFERENCES employees(employee_id)
);
CREATE TABLE IF NOT EXISTS attendance_audit (
 audit_id BIGINT AUTO_INCREMENT PRIMARY KEY, attendance_id BIGINT NOT NULL,
 employee_id INT NOT NULL, changed_by VARCHAR(80) NOT NULL, change_type ENUM('CLOCK_IN','CLOCK_OUT','HR_EDIT') NOT NULL,
 old_check_in TIME NULL, new_check_in TIME NULL, old_check_out TIME NULL, new_check_out TIME NULL,
 changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, notes VARCHAR(300),
 FOREIGN KEY(attendance_id) REFERENCES attendance_records(attendance_id),
 FOREIGN KEY(employee_id) REFERENCES employees(employee_id), INDEX idx_attendance_audit(attendance_id,changed_at)
);

-- Xem phat theo quy tac da thong nhat, khong can thay doi schema bang attendance_records.
CREATE OR REPLACE VIEW v_attendance_penalties AS
SELECT attendance_id, employee_id, work_date, check_in, check_out,
 CASE WHEN check_in > '07:30:00' THEN 100000 ELSE 0 END late_penalty,
 CASE WHEN check_out < '15:30:00' THEN 100000 ELSE 0 END early_penalty,
 (CASE WHEN check_in > '07:30:00' THEN 100000 ELSE 0 END +
  CASE WHEN check_out < '15:30:00' THEN 100000 ELSE 0 END) total_penalty
FROM attendance_records;
