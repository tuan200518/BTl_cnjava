package bus;

import dao.ChucVuDAO;
import model.ChucVu;
import java.util.List;

/**
 * Business Logic Layer - Chức vụ
 */
public class ChucVuBUS {
    private ChucVuDAO chucVuDAO;
    
    public ChucVuBUS() {
        this.chucVuDAO = new ChucVuDAO();
    }
    
    public boolean themChucVu(ChucVu chucVu) {
        // Validate dữ liệu
        if (chucVu.getTenChucVu() == null || chucVu.getTenChucVu().isEmpty()) {
            return false;
        }
        if (chucVu.getHeSoLuong() <= 0) {
            return false;
        }
        return chucVuDAO.insert(chucVu);
    }
    
    public boolean suaChucVu(ChucVu chucVu) {
        if (chucVu.getTenChucVu() == null || chucVu.getTenChucVu().isEmpty()) {
            return false;
        }
        if (chucVu.getHeSoLuong() <= 0) {
            return false;
        }
        return chucVuDAO.update(chucVu);
    }
    
    public boolean xoaChucVu(int maChucVu) {
        return chucVuDAO.delete(maChucVu);
    }
    
    public ChucVu layChucVu(int maChucVu) {
        return chucVuDAO.selectById(maChucVu);
    }
    
    public List<ChucVu> layTatCaChucVu() {
        return chucVuDAO.selectAll();
    }
}
