package bus;

import dao.TaiKhoanDAO;
import model.TaiKhoan;

public class TaiKhoanBUS {
    private TaiKhoanDAO taiKhoanDAO;

    public TaiKhoanBUS() {
        this.taiKhoanDAO = new TaiKhoanDAO();
    }

    public TaiKhoan dangNhap(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        return taiKhoanDAO.checkLogin(username, password);
    }
}