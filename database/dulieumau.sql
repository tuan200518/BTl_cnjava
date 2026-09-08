-- =========================================================
-- DATABASE CHO PROJECT BTl_java
-- MySQL 8.x
-- =========================================================

CREATE DATABASE IF NOT EXISTS quanly_nhansu
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE quanly_nhansu;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS BangLuong;
DROP TABLE IF EXISTS ChamCong;
DROP TABLE IF EXISTS NhanVien;
DROP TABLE IF EXISTS PhongBan;
DROP TABLE IF EXISTS TaiKhoan;
DROP TABLE IF EXISTS ChucVu;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE TaiKhoan (
    TenDangNhap VARCHAR(50) PRIMARY KEY,
    MatKhau VARCHAR(100) NOT NULL,
    HoTen VARCHAR(100),
    Quyen VARCHAR(20) DEFAULT 'User'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE PhongBan (
    MaPhongBan VARCHAR(20) PRIMARY KEY,
    TenPhongBan VARCHAR(100) NOT NULL,
    MoTa VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE ChucVu (
    MaChucVu INT AUTO_INCREMENT PRIMARY KEY,
    TenChucVu VARCHAR(100) NOT NULL,
    HeSoLuong DOUBLE NOT NULL DEFAULT 1.0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE NhanVien (
    MaNV VARCHAR(20) PRIMARY KEY,
    HoTen VARCHAR(100) NOT NULL,
    GioiTinh VARCHAR(10),
    SoDienThoai VARCHAR(15),
    Email VARCHAR(100),
    MaPhongBan VARCHAR(20),
    LuongCoBan DOUBLE NOT NULL DEFAULT 0,
    CONSTRAINT fk_nhanvien_phongban
        FOREIGN KEY (MaPhongBan) REFERENCES PhongBan(MaPhongBan)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Một nhân viên chỉ có một dòng chấm công cho mỗi tháng/năm.
CREATE TABLE ChamCong (
    MaCC INT AUTO_INCREMENT PRIMARY KEY,
    MaNV VARCHAR(20) NOT NULL,
    Thang INT NOT NULL,
    Nam INT NOT NULL,
    SoNgayLam DOUBLE NOT NULL DEFAULT 0,
    SoNgayNghi DOUBLE NOT NULL DEFAULT 0,
    SoGioOT DOUBLE NOT NULL DEFAULT 0,
    CONSTRAINT uq_chamcong_nv_thang UNIQUE (MaNV, Thang, Nam),
    CONSTRAINT fk_chamcong_nhanvien
        FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_chamcong_thang CHECK (Thang BETWEEN 1 AND 12),
    CONSTRAINT chk_chamcong_so_ngay CHECK (SoNgayLam >= 0 AND SoNgayNghi >= 0),
    CONSTRAINT chk_chamcong_ot CHECK (SoGioOT >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Một nhân viên chỉ có một bảng lương cho mỗi tháng/năm.
CREATE TABLE BangLuong (
    MaLuong INT AUTO_INCREMENT PRIMARY KEY,
    MaNV VARCHAR(20) NOT NULL,
    Thang INT NOT NULL,
    Nam INT NOT NULL,
    LuongCoBan DOUBLE NOT NULL DEFAULT 0,
    PhuCap DOUBLE NOT NULL DEFAULT 0,
    LuongOT DOUBLE NOT NULL DEFAULT 0,
    KhauTru DOUBLE NOT NULL DEFAULT 0,
    ThucLinh DOUBLE NOT NULL DEFAULT 0,
    CONSTRAINT uq_bangluong_nv_thang UNIQUE (MaNV, Thang, Nam),
    CONSTRAINT fk_bangluong_nhanvien
        FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_bangluong_thang CHECK (Thang BETWEEN 1 AND 12)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- TAI KHOAN
-- =========================================================
INSERT INTO TaiKhoan (TenDangNhap, MatKhau, HoTen, Quyen) VALUES
('admin', '123456', 'Quản Trị Viên', 'Admin'),
('hr_manager', '123456', 'Quản Lý Nhân Sự', 'Admin'),
('hr_staff', '123456', 'Nhân Viên HR', 'User');

-- =========================================================
-- PHONG BAN
-- =========================================================
INSERT INTO PhongBan (MaPhongBan, TenPhongBan, MoTa) VALUES
('PB_IT',  'Phòng Công Nghệ Thông Tin', 'Phát triển phần mềm và bảo trì hệ thống'),
('PB_HR',  'Phòng Hành Chính Nhân Sự', 'Tuyển dụng, đào tạo và tính lương'),
('PB_KT',  'Phòng Kế Toán', 'Quản lý thu chi và tài chính doanh nghiệp'),
('PB_KD',  'Phòng Kinh Doanh', 'Tìm kiếm khách hàng, phát triển thị trường'),
('PB_MKT', 'Phòng Marketing', 'Truyền thông và quảng bá thương hiệu');

-- =========================================================
-- CHUC VU (phục vụ ChucVuDAO nếu cần dùng sau này)
-- =========================================================
INSERT INTO ChucVu (TenChucVu, HeSoLuong) VALUES
('Nhân viên', 1.0),
('Trưởng nhóm', 1.5),
('Trưởng phòng', 2.0),
('Quản lý', 2.5);

-- =========================================================
-- NHAN VIEN
-- =========================================================
INSERT INTO NhanVien
(MaNV, HoTen, GioiTinh, SoDienThoai, Email, MaPhongBan, LuongCoBan) VALUES
('NV001', 'Đào Gia Huân', 'Nam', '0912345678', 'huan.dao@company.com', 'PB_IT', 18000000),
('NV002', 'Nguyễn Thị Mai', 'Nữ', '0987654321', 'mai.nguyen@company.com', 'PB_HR', 12000000),
('NV003', 'Trần Văn Kiên', 'Nam', '0901112233', 'kien.tran@company.com', 'PB_KD', 15000000),
('NV004', 'Lê Hương Giang', 'Nữ', '0933445566', 'giang.le@company.com', 'PB_KT', 14000000),
('NV005', 'Phạm Tuấn Anh', 'Nam', '0977889900', 'anh.pham@company.com', 'PB_IT', 16000000),
('NV006', 'Hoàng Bảo Trâm', 'Nữ', '0922334455', 'tram.hoang@company.com', 'PB_MKT', 13500000),
('NV007', 'Vũ Đức Hải', 'Nam', '0966778899', 'hai.vu@company.com', 'PB_KD', 14500000);

-- =========================================================
-- CHAM CONG THANG 8/2026
-- =========================================================
INSERT INTO ChamCong
(MaNV, Thang, Nam, SoNgayLam, SoNgayNghi, SoGioOT) VALUES
('NV001', 8, 2026, 22,   0,   15.5),
('NV002', 8, 2026, 21,   1,    0),
('NV003', 8, 2026, 20,   2,    8),
('NV004', 8, 2026, 22,   0,    4.5),
('NV005', 8, 2026, 22,   0,   20),
('NV006', 8, 2026, 21.5, 0.5,  0),
('NV007', 8, 2026, 19,   3,   12);

-- =========================================================
-- BANG LUONG THANG 8/2026
-- =========================================================
INSERT INTO BangLuong
(MaNV, Thang, Nam, LuongCoBan, PhuCap, LuongOT, KhauTru, ThucLinh) VALUES
('NV001', 8, 2026, 18000000, 1000000, 2325000,       0, 21325000),
('NV002', 8, 2026, 12000000,  500000,       0,  545454, 11954546),
('NV003', 8, 2026, 15000000,  800000, 1200000, 1363636, 15636364),
('NV004', 8, 2026, 14000000,  500000,  675000,       0, 15175000),
('NV005', 8, 2026, 16000000, 1000000, 3000000,       0, 20000000),
('NV006', 8, 2026, 13500000,  500000,       0,  306818, 13693182),
('NV007', 8, 2026, 14500000,  800000, 1800000, 1977272, 15122728);

-- Kiểm tra nhanh sau khi import:
-- SELECT * FROM TaiKhoan;
-- SELECT * FROM PhongBan;
-- SELECT * FROM NhanVien;
-- SELECT * FROM ChamCong;
-- SELECT * FROM BangLuong;
