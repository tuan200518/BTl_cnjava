package bus;

import dao.LuongDAO;
import model.BangLuong;
import java.util.List;

/**
 * Business Logic Layer - Lương
 */
public class LuongBUS {
    private LuongDAO luongDAO;
    
    public LuongBUS() {
        this.luongDAO = new LuongDAO();
    }
    
    public boolean themLuong(BangLuong luong) {
        // Validate dữ liệu
        if (luong.getMaNhanVien() == null || luong.getMaNhanVien().trim().isEmpty()) {
            return false;
        }
        if (luong.getThang() < 1 || luong.getThang() > 12) {
            return false;
        }
        if (luong.getNam() < 2000) {
            return false;
        }
        if (luong.getLuongCoBan() <= 0) {
            return false;
        }
        return luongDAO.insert(luong);
    }
    
    public boolean suaLuong(BangLuong luong) {
        if (luong.getThang() < 1 || luong.getThang() > 12) {
            return false;
        }
        if (luong.getNam() < 2000) {
            return false;
        }
        return luongDAO.update(luong);
    }
    
    public boolean xoaLuong(int maLuong) {
        return luongDAO.delete(maLuong);
    }
    
    public BangLuong layLuong(int maLuong) {
        return luongDAO.selectById(maLuong);
    }
    
    public List<BangLuong> layTatCaLuong() {
        return luongDAO.selectAll();
    }
    
    // Tính lương thực nhận
    public double tinhLuongThucNhan(double luongCoBan, double phuCap, double tienThuong,
                                     double tienBaoHiem, double tienThue) {
        return luongCoBan + phuCap + tienThuong - tienBaoHiem - tienThue;
    }
}
