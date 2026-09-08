package dao;

import config.DBConnection;
import model.BangChamCong;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO - Cham cong.
 */
public class ChamCongDAO {
    public boolean insert(BangChamCong cc) {
        String sql = "INSERT INTO ChamCong (MaNV, Thang, Nam, SoNgayLam, SoNgayNghi, SoGioOT) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cc.getMaNhanVien());
            ps.setInt(2, cc.getThang());
            ps.setInt(3, cc.getNam());
            ps.setDouble(4, cc.getSoNgayLam());
            ps.setDouble(5, cc.getSoNgayNghi());
            ps.setDouble(6, cc.getSoGioOT());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(BangChamCong cc) {
        String sql = "UPDATE ChamCong SET MaNV=?, Thang=?, Nam=?, SoNgayLam=?, SoNgayNghi=?, SoGioOT=? WHERE MaCC=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cc.getMaNhanVien());
            ps.setInt(2, cc.getThang());
            ps.setInt(3, cc.getNam());
            ps.setDouble(4, cc.getSoNgayLam());
            ps.setDouble(5, cc.getSoNgayNghi());
            ps.setDouble(6, cc.getSoGioOT());
            ps.setInt(7, cc.getMaChamCong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maChamCong) {
        String sql = "DELETE FROM ChamCong WHERE MaCC=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChamCong);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BangChamCong selectById(int id) {
        String sql = "SELECT MaCC, MaNV, Thang, Nam, SoNgayLam, SoNgayNghi, SoGioOT FROM ChamCong WHERE MaCC=?";
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

    public List<BangChamCong> selectAll() {
        List<BangChamCong> list = new ArrayList<>();
        String sql = "SELECT MaCC, MaNV, Thang, Nam, SoNgayLam, SoNgayNghi, SoGioOT FROM ChamCong ORDER BY Nam DESC, Thang DESC, MaNV";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private BangChamCong map(ResultSet rs) throws SQLException {
        return new BangChamCong(
                rs.getInt("MaCC"),
                rs.getString("MaNV"),
                rs.getInt("Thang"),
                rs.getInt("Nam"),
                rs.getDouble("SoNgayLam"),
                rs.getDouble("SoNgayNghi"),
                rs.getDouble("SoGioOT"));
    }
}
