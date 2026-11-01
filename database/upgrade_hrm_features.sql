-- Nâng cấp bổ sung cho HRM Desktop.
-- Chạy trong database hrm_enterprise_db nếu database đã tồn tại.
-- Sao lưu dữ liệu trước khi chạy. Các lệnh đều dùng IF NOT EXISTS để hạn chế ảnh hưởng dữ liệu cũ.
USE hrm_enterprise_db;

CREATE TABLE IF NOT EXISTS employee_contracts (
 contract_id BIGINT AUTO_INCREMENT PRIMARY KEY,
 employee_id INT NOT NULL,
 contract_no VARCHAR(60) NOT NULL UNIQUE,
 contract_type VARCHAR(50) NOT NULL,
 start_date DATE NOT NULL,
 end_date DATE NULL,
 salary DECIMAL(15,2) NOT NULL,
 status ENUM('DRAFT','ACTIVE','EXPIRED','TERMINATED') NOT NULL DEFAULT 'ACTIVE',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(employee_id) REFERENCES employees(employee_id),
 INDEX idx_contract_employee(employee_id,start_date)
);

CREATE TABLE IF NOT EXISTS leave_requests (
 request_id BIGINT AUTO_INCREMENT PRIMARY KEY,
 employee_id INT NOT NULL,
 leave_type VARCHAR(50) NOT NULL,
 start_date DATE NOT NULL,
 end_date DATE NOT NULL,
 total_days DECIMAL(5,2) NOT NULL,
 reason VARCHAR(500),
 status ENUM('PENDING','APPROVED','REJECTED','CANCELLED') NOT NULL DEFAULT 'PENDING',
 reviewed_by VARCHAR(80),
 reviewed_at DATETIME,
 FOREIGN KEY(employee_id) REFERENCES employees(employee_id),
 INDEX idx_leave_status(status,start_date)
);
