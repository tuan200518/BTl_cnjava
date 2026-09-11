# Mô tả cơ sở dữ liệu

Database `hrm_enterprise_db` dùng MySQL 8+. Không tạo 5 database khác nhau; dữ liệu 2022-2026 nằm trong cùng database và được phân biệt bằng ngày/năm nghiệp vụ.

| Bảng | Mục đích | Khóa chính | Quan hệ chính |
|---|---|---|---|
| departments | Phòng ban | department_id | 1-N với jobs, employees |
| jobs | Vị trí việc làm | job_id | N-1 departments; 1-N employees |
| employees | Hồ sơ nhân viên | employee_id | Liên kết lương, sự kiện, OT, dự án, tài khoản |
| employment_events | Lịch sử thưởng/nghỉ/thăng cấp/tăng lương/OT/nghỉ việc | event_id | N-1 employees |
| rewards_disciplines | Thưởng/kỷ luật | record_id | N-1 employees |
| attendance_records | Chấm công/ngày công | attendance_id | N-1 employees |
| leave_records | Nghỉ phép | leave_id | N-1 employees |
| overtime_records | Giờ OT và đơn giá giờ | ot_id | N-1 employees |
| payrolls | Phiếu lương từng tháng | payroll_id | N-1 employees; UNIQUE nhân viên-tháng-năm |
| projects | Dự án và nhu cầu nhân lực | project_id | 1-N allocations |
| project_allocations | Phân công nhân viên vào dự án | allocation_id | N-1 employees/projects |
| users | Tài khoản + quyền | user_id | 0/1-N với employee |
| system_logs | Nhật ký bảo trì | log_id | ghi hành động hệ thống |

## Quy tắc dữ liệu
- Có 2.500 nhân viên, 500 nhân viên được tuyển trong mỗi năm 2022, 2023, 2024, 2025, 2026.
- Mã nhân viên dạng `NV0001`...`NV2500`; email dạng `NV0001@gmail.com`...
- Có nhân viên ACTIVE và RESIGNED; người nghỉ việc có `resignation_date` và event RESIGN.
- Tên được ghép từ 18 họ + 18 tên đệm + 30 tên, tạo hàng nghìn tổ hợp.
- Phiếu lương có DRAFT/SENT. HR chỉ gửi phiếu của nhân viên được chọn; EMPLOYEE chỉ truy vấn phiếu `SENT` của chính `employee_id`.
- ADMIN không có tab dữ liệu nhân sự/lương, chỉ có bảo trì hệ thống.
- Mật khẩu dùng PBKDF2-HMAC-SHA256; dữ liệu nhạy cảm có thể tiếp tục mã hóa AES-GCM ở tầng Service/DAO.
