package bus;

import dao.PhongBanDAO;
import model.PhongBan;
import java.util.List;

public class PhongBanBUS {
    private PhongBanDAO phongBanDAO = new PhongBanDAO();

    public List<PhongBan> getAll() {
        return phongBanDAO.getAll();
    }

    public String add(PhongBan pb) {
        if (pb.getMaPhongBan() == null || pb.getMaPhongBan().trim().isEmpty()) {
            return "Mã phòng ban không được để trống!";
        }
        if (pb.getTenPhongBan() == null || pb.getTenPhongBan().trim().isEmpty()) {
            return "Tên phòng ban không được để trống!";
        }
        return phongBanDAO.insert(pb) ? "SUCCESS" : "Thêm thất bại (Trùng Mã Phòng Ban)!";
    }

    public String update(PhongBan pb) {
        if (pb.getTenPhongBan() == null || pb.getTenPhongBan().trim().isEmpty()) {
            return "Tên phòng ban không được để trống!";
        }
        return phongBanDAO.update(pb) ? "SUCCESS" : "Cập nhật thất bại!";
    }

    public String delete(String maPB) {
        if (maPB == null || maPB.trim().isEmpty()) {
            return "Vui lòng chọn phòng ban cần xóa!";
        }
        return phongBanDAO.delete(maPB) ? "SUCCESS" : "Không thể xóa (Đang có nhân viên thuộc phòng ban này)!";
    }
}