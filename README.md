# HRM Desktop - Java 21

## Chức năng
- Đăng nhập PBKDF2: ADMIN / HR / EMPLOYEE.
- Quản lý nhân viên: tìm kiếm, lọc, thêm, sửa, xóa; nhân viên mới tự tạo tài khoản NVxxxx / mật khẩu mặc định 123456.
- Phân công công việc và dự án.
- Phiếu lương: **Tạo phiếu lương** mở form riêng để tìm/lọc nhân viên và chọn kỳ lương; **Gửi phiếu lương** mở form riêng chỉ hiển thị phiếu DRAFT để kiểm tra trước khi gửi; **Lịch sử phiếu lương** mở form riêng để tra cứu.
- Nhân viên chỉ xem các phiếu đã gửi của chính mình.

## Chạy
1. Import `database/dulieumau.sql` vào MySQL.
2. Sửa `src/main/resources/application.properties` nếu mật khẩu MySQL khác.
3. Chạy `target/hrm-desktop-2.0.0.jar` hoặc `run.bat`.

Tài khoản demo:
- `admin_sys / 123456`
- `hr_manager / 123456`
- `NV0001 / 123456`


## Chức năng hoàn thiện cuối
- Đăng xuất và màn hình đăng nhập để trống tài khoản/mật khẩu.
- Tài khoản nhân viên có trạng thái kích hoạt; nhân viên RESIGNED được tự động khóa, HR có thể kích hoạt lại.
- Nhân viên chỉ xem phiếu lương từ năm gia nhập trở đi; có lựa chọn xem toàn bộ 5 năm ở các màn hình tra cứu theo năm.
- Phiếu lương có thưởng và khoản phạt/trừ lương (DISCIPLINE).
- HR có thể ghi phạt, ví dụ nghỉ quá số ngày quy định; khoản phạt được đưa vào cột Khấu trừ khi tạo phiếu.
- Hồ sơ nhân viên có vị trí công việc, phòng ban và dự án; HR có thể tìm kiếm/phân công.
- Định biên và Dự án có thể mở danh sách nhân viên theo vị trí/dự án và tìm kiếm.
- ADMIN có kiểm tra kết nối, thống kê dữ liệu và tối ưu bảng.
- Dashboard hiển thị Tổng lương đã trả (chỉ phiếu SENT).
