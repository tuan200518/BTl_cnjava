package model;

/**
 * Lớp đối tượng - Chức vụ
 */
public class ChucVu {
    private int maChucVu;
    private String tenChucVu;
    private double heSoLuong;
    
    public ChucVu() {
    }
    
    public ChucVu(int maChucVu, String tenChucVu, double heSoLuong) {
        this.maChucVu = maChucVu;
        this.tenChucVu = tenChucVu;
        this.heSoLuong = heSoLuong;
    }
    
    // Getters and Setters
    public int getMaChucVu() {
        return maChucVu;
    }
    
    public void setMaChucVu(int maChucVu) {
        this.maChucVu = maChucVu;
    }
    
    public String getTenChucVu() {
        return tenChucVu;
    }
    
    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }
    
    public double getHeSoLuong() {
        return heSoLuong;
    }
    
    public void setHeSoLuong(double heSoLuong) {
        this.heSoLuong = heSoLuong;
    }
}
