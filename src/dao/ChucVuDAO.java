package dao;

import config.DBConnection;
import model.ChucVu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO - Chuc vu.
 */
public class ChucVuDAO {
    public ChucVuDAO() {}

    public boolean insert(ChucVu chucVu) {
        String sql = "INSERT INTO ChucVu (TenChucVu, HeSoLuong) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, chucVu.getTenChucVu());
            ps.setDouble(2, chucVu.getHeSoLuong());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(ChucVu chucVu) {
        String sql = "UPDATE ChucVu SET TenChucVu=?, HeSoLuong=? WHERE MaChucVu=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, chucVu.getTenChucVu());
            ps.setDouble(2, chucVu.getHeSoLuong());
            ps.setInt(3, chucVu.getMaChucVu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maChucVu) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM ChucVu WHERE MaChucVu=?")) {
            ps.setInt(1, maChucVu);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ChucVu selectById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM ChucVu WHERE MaChucVu=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new ChucVu(rs.getInt(1), rs.getString(2), rs.getDouble(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ChucVu> selectAll() {
        List<ChucVu> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM ChucVu ORDER BY MaChucVu");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(new ChucVu(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
