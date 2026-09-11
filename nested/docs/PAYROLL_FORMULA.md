# Công thức phiếu lương

## Kỳ tính thuế 2026
- Bảo hiểm bắt buộc người lao động: **10,5%** tiền lương đóng bảo hiểm = BHXH 8% + BHYT 1,5% + BHTN 1%.
- Giảm trừ bản thân: **15.500.000 đồng/tháng**.
- Giảm trừ mỗi người phụ thuộc: **6.200.000 đồng/tháng**.
- Thu nhập tính thuế = Tổng thu nhập - bảo hiểm bắt buộc - giảm trừ bản thân - giảm trừ người phụ thuộc - khoản không chịu thuế.
- Thuế TNCN 5 bậc: 5% đến 10 triệu; 10% trên 10-30 triệu; 20% trên 30-60 triệu; 30% trên 60-100 triệu; 35% trên 100 triệu/tháng.
- Thực lĩnh = Tổng thu nhập - bảo hiểm bắt buộc - thuế TNCN - phạt/trừ lương.

## Kỳ 2022-2025
Ứng dụng dùng mức giảm trừ cũ 11 triệu/người nộp thuế và 4,4 triệu/người phụ thuộc cùng biểu thuế 7 bậc để dữ liệu lịch sử không bị tính theo quy định 2026.

## Ghi chú
Tiền lương đóng bảo hiểm trong bản demo được lấy bằng lương cơ bản. Phụ cấp hiện chưa phát sinh riêng trong schema payroll nên mặc định 0. Người phụ thuộc được lưu tại `employees.dependent_count`.
