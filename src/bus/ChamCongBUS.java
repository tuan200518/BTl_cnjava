package bus;

import dao.ChamCongDAO;
import model.BangChamCong;
import java.util.List;

/**
 * Business Logic Layer - Cham cong.
 */
public class ChamCongBUS {
    private final ChamCongDAO chamCongDAO = new ChamCongDAO();

    public boolean themChamCong(BangChamCong cc) {
        return valid(cc) && chamCongDAO.insert(cc);
    }

    public boolean suaChamCong(BangChamCong cc) {
        return valid(cc) && chamCongDAO.update(cc);
    }

    private boolean valid(BangChamCong cc) {
        return cc != null
                && cc.getMaNhanVien() != null
                && !cc.getMaNhanVien().trim().isEmpty()
                && cc.getThang() >= 1 && cc.getThang() <= 12
                && cc.getNam() >= 2000
                && cc.getSoNgayLam() >= 0
                && cc.getSoNgayNghi() >= 0
                && cc.getSoGioOT() >= 0;
    }

    public boolean xoaChamCong(int id) { return chamCongDAO.delete(id); }
    public BangChamCong layChamCong(int id) { return chamCongDAO.selectById(id); }
    public List<BangChamCong> layTatCaChamCong() { return chamCongDAO.selectAll(); }
}
