# HRM Desktop - Java 21

## Chức năng
- Đăng nhập PBKDF2: ADMIN / HR / EMPLOYEE.
- Quản lý nhân viên: tìm kiếm, lọc, thêm, sửa, xóa; nhân viên mới tự tạo tài khoản NVxxxx / mật khẩu mặc định 123456.
- Phân công công việc và dự án.
- Phiếu lương: **Tạo phiếu lương** mở form riêng để tìm/lọc nhân viên và chọn kỳ lương; **Gửi phiếu lương** mở form riêng chỉ hiển thị phiếu DRAFT để kiểm tra trước khi gửi; **Lịch sử phiếu lương** mở form riêng để tra cứu.
- Nhân viên chỉ xem các phiếu đã gửi của chính mình.

## Chạy
1. Import `database/hrm_database.sql` vào MySQL (tệp này tạo database `hrm_enterprise_db`).
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

## Bản cập nhật chức năng HRM
- Phiếu lương: nhân viên mới được tìm theo đúng tháng/năm làm việc; thêm nút xem phiếu lương chi tiết.
- Phiếu lương có cột **Phạt / trừ lương** và form chi tiết theo bố cục phiếu mẫu.
- Dự án: hiển thị Cần / Đã có / Còn thiếu / % đáp ứng; double-click hoặc nút để xem danh sách nhân viên và vai trò.
- HR có thể tạo dự án mới trực tiếp trong tab Dự án.
- Dữ liệu mẫu dự án được phân bổ theo nhiều mức thiếu nhân sự, tránh tình trạng dự án 0 người hoặc tất cả đều đủ.
- Nếu đã có database cũ, chạy `database/patch_hrm_features.sql` để làm lại dữ liệu phân công dự án mà không phải xóa toàn bộ database.


## Cập nhật tích hợp chấm công và database doanh nghiệp
- Nhân viên có tab **Chấm công của tôi**, chấm vào/ra và xem lịch sử cá nhân.
- Mức phạt được tính theo từng bản ghi: vào sau 07:30 = 100.000 VNĐ; ra trước 15:30 = 100.000 VNĐ.
- HR xem thống kê tổng hợp và tiền phạt; kế toán không có màn hình chỉnh sửa chấm công.
- Câu UPDATE payroll mẫu giữ nguyên công thức cũ, chỉ thêm `WHERE p.payroll_id > 0` để tương thích Safe Update Mode.
- `database/hrm_database.sql` hiện đã bao gồm các bảng nghiệp vụ mở rộng như hợp đồng, lịch sử lương, đơn nghỉ phép, đào tạo, đánh giá, phúc lợi và audit chấm công. Với database cũ, chạy `database/upgrade_hrm_features.sql` để bổ sung bảng hợp đồng và đơn nghỉ phép còn thiếu; sao lưu trước khi nâng cấp.
- `database/optional_generate_attendance_history.sql`: thủ tục tạo chấm công mô phỏng theo ngày làm việc. Không tự chạy; chỉ gọi thủ tục nếu muốn tạo lượng lớn dữ liệu mẫu.

### Chạy với database đã có
1. Sao lưu database.
2. Chạy `database/upgrade_company_operations.sql`.
3. Không chạy lại file `dulieumau.sql` nếu không muốn DROP và tạo lại database.
4. Mở project chứa `pom.xml`; cần JDK 21 và Maven, chạy `mvn clean package`.


## Chức năng OT tự động khi chấm công ra muộn

- Giờ kết thúc ca chuẩn: **15:30**.
- Khi nhân viên chấm công ra sau 15:30, hệ thống tự tính OT theo công thức:
  `OT (giờ) = (Giờ ra - 15:30)`.
- Ví dụ: 16:00 = 0,50 giờ OT; 17:00 = 1,50 giờ OT.
- Hệ thống tự tạo/cập nhật bản ghi trong `overtime_records` với đơn giá `lương cơ bản / 26 / 8` và hệ số 1,5.
- OT được cộng vào phần tính lương thông qua `PayrollDAO`.
- Lịch sử chấm công của nhân viên hiển thị thêm cột **OT (giờ)**.
- Nếu database đã được tạo từ phiên bản cũ, chạy `database/nangcap_overtime_tu_dong.sql` một lần trước khi chạy ứng dụng.


## Các chức năng được bổ sung
- **Nghỉ phép:** Nhân viên gửi, xem và hủy đơn đang chờ; HR/Admin xem danh sách và duyệt hoặc từ chối đơn đang chờ. Hệ thống kiểm tra ngày hợp lệ và đơn bị trùng khoảng thời gian.
- **Hợp đồng lao động:** HR/Admin tạo hợp đồng, xem danh sách, xuất báo cáo CSV mở được bằng Excel và chấm dứt hợp đồng. Hợp đồng đã qua ngày kết thúc được tự động chuyển sang `EXPIRED` khi tải danh sách.
- **Đổi mật khẩu:** Người dùng nhập mật khẩu hiện tại, nhập mật khẩu mới tối thiểu 8 ký tự và xác nhận lại. Mật khẩu tiếp tục được băm bằng PBKDF2.
- **Xuất báo cáo CSV tương thích Excel:** Có tại danh sách nhân viên, phiếu lương, nghỉ phép và hợp đồng. Tệp xuất dùng UTF-8 BOM để hạn chế lỗi font tiếng Việt.

### Cập nhật sau khi lấy phiên bản này
1. Nếu tạo database mới, import `database/hrm_database.sql`.
2. Nếu đang dùng database cũ, sao lưu trước rồi chạy `database/upgrade_hrm_features.sql` trong MySQL.
3. Kiểm tra `src/main/resources/application.properties` và thay thông tin kết nối cho đúng máy của bạn.
4. Dùng JDK 21 và Maven để chạy `mvn clean package`; nếu đang dùng database thật, không chạy lại file SQL khởi tạo có thao tác xóa/tạo dữ liệu.
