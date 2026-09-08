package model;

/**
 * Doi tuong bang cham cong, khop voi CSDL quanly_nhansu.
 */
public class BangChamCong {
    private int maChamCong;
    private String maNhanVien;
    private int thang;
    private int nam;
    private double soNgayLam;
    private double soNgayNghi;
    private double soGioOT;

    public BangChamCong() {}

    public BangChamCong(int maChamCong, String maNhanVien, int thang, int nam,
                         double soNgayLam, double soNgayNghi, double soGioOT) {
        this.maChamCong = maChamCong;
        this.maNhanVien = maNhanVien;
        this.thang = thang;
        this.nam = nam;
        this.soNgayLam = soNgayLam;
        this.soNgayNghi = soNgayNghi;
        this.soGioOT = soGioOT;
    }

    public int getMaChamCong() { return maChamCong; }
    public void setMaChamCong(int maChamCong) { this.maChamCong = maChamCong; }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public double getSoNgayLam() { return soNgayLam; }
    public void setSoNgayLam(double soNgayLam) { this.soNgayLam = soNgayLam; }

    public double getSoNgayNghi() { return soNgayNghi; }
    public void setSoNgayNghi(double soNgayNghi) { this.soNgayNghi = soNgayNghi; }

    public double getSoGioOT() { return soGioOT; }
    public void setSoGioOT(double soGioOT) { this.soGioOT = soGioOT; }
}
