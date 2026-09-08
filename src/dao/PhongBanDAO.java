package dao;

import config.DBConnection;
import model.PhongBan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongBanDAO {
    public List<PhongBan> getAll() {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongBan";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PhongBan pb = new PhongBan(
                    rs.getString("MaPhongBan"),
                    rs.getString("TenPhongBan"),
                    rs.getString("MoTa")
                );
                list.add(pb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(PhongBan pb) {
        String sql = "INSERT INTO PhongBan (MaPhongBan, TenPhongBan, MoTa) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pb.getMaPhongBan());
            ps.setString(2, pb.getTenPhongBan());
            ps.setString(3, pb.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(PhongBan pb) {
        String sql = "UPDATE PhongBan SET TenPhongBan = ?, MoTa = ? WHERE MaPhongBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pb.getTenPhongBan());
            ps.setString(2, pb.getMoTa());
            ps.setString(3, pb.getMaPhongBan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String maPB) {
        String sql = "DELETE FROM PhongBan WHERE MaPhongBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPB);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}