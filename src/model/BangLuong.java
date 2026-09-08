package model;

/**
 * Doi tuong bang luong, khop voi CSDL quanly_nhansu.
 */
public class BangLuong {
    private int maLuong;
    private String maNhanVien;
    private int thang;
    private int nam;
    private double luongCoBan;
    private double phuCap;
    // Giữ tên tienThuong/tienBaoHiem/tienThue để tương thích code cũ.
    // Khi lưu DB: tienThuong -> LuongOT; tienBaoHiem + tienThue -> KhauTru.
    private double tienThuong;
    private double tienBaoHiem;
    private double tienThue;
    private double luongThucNhan;

    public BangLuong() {}

    public BangLuong(int maLuong, String maNhanVien, int thang, int nam,
                     double luongCoBan, double phuCap, double tienThuong,
                     double tienBaoHiem, double tienThue, double luongThucNhan) {
        this.maLuong = maLuong;
        this.maNhanVien = maNhanVien;
        this.thang = thang;
        this.nam = nam;
        this.luongCoBan = luongCoBan;
        this.phuCap = phuCap;
        this.tienThuong = tienThuong;
        this.tienBaoHiem = tienBaoHiem;
        this.tienThue = tienThue;
        this.luongThucNhan = luongThucNhan;
    }

    public int getMaLuong() { return maLuong; }
    public void setMaLuong(int maLuong) { this.maLuong = maLuong; }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }

    public double getPhuCap() { return phuCap; }
    public void setPhuCap(double phuCap) { this.phuCap = phuCap; }

    public double getTienThuong() { return tienThuong; }
    public void setTienThuong(double tienThuong) { this.tienThuong = tienThuong; }

    public double getTienBaoHiem() { return tienBaoHiem; }
    public void setTienBaoHiem(double tienBaoHiem) { this.tienBaoHiem = tienBaoHiem; }

    public double getTienThue() { return tienThue; }
    public void setTienThue(double tienThue) { this.tienThue = tienThue; }

    public double getLuongThucNhan() { return luongThucNhan; }
    public void setLuongThucNhan(double luongThucNhan) { this.luongThucNhan = luongThucNhan; }
}
