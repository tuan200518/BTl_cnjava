package dao;

import config.DBConnection;
import model.BangLuong;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO - Bang luong.
 */
public class LuongDAO {
    public boolean insert(BangLuong luong) {
        String sql = "INSERT INTO BangLuong (MaNV, Thang, Nam, LuongCoBan, PhuCap, LuongOT, KhauTru, ThucLinh) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fill(ps, luong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(BangLuong luong) {
        String sql = "UPDATE BangLuong SET MaNV=?, Thang=?, Nam=?, LuongCoBan=?, PhuCap=?, LuongOT=?, KhauTru=?, ThucLinh=? WHERE MaLuong=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fill(ps, luong);
            ps.setInt(9, luong.getMaLuong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void fill(PreparedStatement ps, BangLuong l) throws SQLException {
        ps.setString(1, l.getMaNhanVien());
        ps.setInt(2, l.getThang());
        ps.setInt(3, l.getNam());
        ps.setDouble(4, l.getLuongCoBan());
        ps.setDouble(5, l.getPhuCap());
        ps.setDouble(6, l.getTienThuong());
        ps.setDouble(7, l.getTienBaoHiem() + l.getTienThue());
        ps.setDouble(8, l.getLuongThucNhan());
    }

    public boolean delete(int maLuong) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM BangLuong WHERE MaLuong=?")) {
            ps.setInt(1, maLuong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BangLuong selectById(int id) {
        String sql = "SELECT MaLuong, MaNV, Thang, Nam, LuongCoBan, PhuCap, LuongOT, KhauTru, ThucLinh FROM BangLuong WHERE MaLuong=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BangLuong> selectAll() {
        List<BangLuong> list = new ArrayList<>();
        String sql = "SELECT MaLuong, MaNV, Thang, Nam, LuongCoBan, PhuCap, LuongOT, KhauTru, ThucLinh FROM BangLuong ORDER BY Nam DESC, Thang DESC, MaNV";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private BangLuong map(ResultSet rs) throws SQLException {
        double khauTru = rs.getDouble("KhauTru");
        return new BangLuong(
                rs.getInt("MaLuong"),
                rs.getString("MaNV"),
                rs.getInt("Thang"),
                rs.getInt("Nam"),
                rs.getDouble("LuongCoBan"),
                rs.getDouble("PhuCap"),
                rs.getDouble("LuongOT"),
                khauTru,
                0,
                rs.getDouble("ThucLinh"));
    }
}
