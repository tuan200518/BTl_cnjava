package bus;

import dao.NhanVienDAO;
import model.NhanVien;
import java.util.List;

public class NhanVienBUS {
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public List<NhanVien> getAll() {
        return nhanVienDAO.getAll();
    }

    public List<NhanVien> search(String keyword) {
        return nhanVienDAO.search(keyword);
    }

    public String validateAndSave(NhanVien nv, boolean isUpdate) {
        if (nv.getMaNV() == null || nv.getMaNV().trim().isEmpty()) {
            return "Mã nhân viên không được trống!";
        }
        if (nv.getHoTen() == null || nv.getHoTen().trim().isEmpty()) {
            return "Họ tên không được trống!";
        }
        if (!nv.getSoDienThoai().matches("^0\\d{9}$")) {
            return "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0!";
        }
        if (!nv.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Định dạng Email không hợp lệ!";
        }
        if (nv.getLuongCoBan() <= 0) {
            return "Lương cơ bản phải lớn hơn 0!";
        }

        if (isUpdate) {
            return nhanVienDAO.update(nv) ? "SUCCESS" : "Cập nhật nhân viên thất bại!";
        } else {
            return nhanVienDAO.insert(nv) ? "SUCCESS" : "Thêm thất bại (Trùng Mã Nhân Viên)!";
        }
    }

    public String delete(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            return "Vui lòng chọn nhân viên cần xóa!";
        }
        return nhanVienDAO.delete(maNV) ? "SUCCESS" : "Không thể xóa nhân viên này!";
    }
}