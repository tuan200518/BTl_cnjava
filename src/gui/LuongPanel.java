package gui;

import config.DBConnection;
import utils.ExcelExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import utils.CurrencyUtils;

public class LuongPanel extends JPanel {
    private JComboBox<Integer> cbThang, cbNam;
    private JTable tblLuong;
    private DefaultTableModel tableModel;
    private JButton btnXem, btnTinhLuong, btnXuatExcel;

    public LuongPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- TOP PANEL (BỘ LỌC THỜI GIAN & NÚT LỆNH) ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTop.setBorder(BorderFactory.createTitledBorder("Chọn Kỳ Lương"));

        pnlTop.add(new JLabel("Tháng:"));
        cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);

        pnlTop.add(new JLabel("Năm:"));
        cbNam = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 5; i <= currentYear + 1; i++) cbNam.addItem(i);

        // Mặc định chọn tháng năm hiện tại
        cbThang.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH) + 1);
        cbNam.setSelectedItem(currentYear);

        btnXem = new JButton("Xem Bảng Lương");
        btnTinhLuong = new JButton("Tính / Cập Nhật Luống");
        btnTinhLuong.setBackground(new Color(41, 128, 185));
        btnTinhLuong.setForeground(Color.WHITE);

        btnXuatExcel = new JButton("Xuất File Excel");
        btnXuatExcel.setBackground(new Color(39, 174, 96));
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pnlTop.add(cbThang);
        pnlTop.add(cbNam);
        pnlTop.add(btnXem);
        pnlTop.add(btnTinhLuong);
        pnlTop.add(btnXuatExcel);

        add(pnlTop, BorderLayout.NORTH);

        // --- TABLE BẢNG LƯƠNG (CENTER) ---
        String[] columns = {"Mã NV", "Họ Tên", "Tháng/Năm", "Lương CB", "Số Ngày Làm", "Lương Ngày", "Số Giờ OT", "Tiền OT", "Thực Lĩnh"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblLuong = new JTable(tableModel);
        tblLuong.setRowHeight(28);
        tblLuong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(new JScrollPane(tblLuong), BorderLayout.CENTER);

        // --- EVENTS ---
        btnXem.addActionListener(e -> loadBangLuongData());
        btnTinhLuong.addActionListener(e -> tinhToanVaCapNhatLuong());
        btnXuatExcel.addActionListener(e -> {
            int thang = (int) cbThang.getSelectedItem();
            int nam = (int) cbNam.getSelectedItem();
            ExcelExporter.exportJTableToCSV(tblLuong, "BangLuong_Thang_" + thang + "_" + nam);
        });

        // Khởi tạo bảng dữ liệu
        loadBangLuongData();
    }

    public void loadBangLuongData() {
        tableModel.setRowCount(0);
        int thang = (int) cbThang.getSelectedItem();
        int nam = (int) cbNam.getSelectedItem();

        String sql = "SELECT l.MaNV, n.HoTen, l.Thang, l.Nam, l.LuongCoBan, c.SoNgayLam, c.SoGioOT, l.LuongOT, l.ThucLinh " +
                     "FROM BangLuong l " +
                     "JOIN NhanVien n ON l.MaNV = n.MaNV " +
                     "LEFT JOIN ChamCong c ON l.MaNV = c.MaNV AND l.Thang = c.Thang AND l.Nam = c.Nam " +
                     "WHERE l.Thang = ? AND l.Nam = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double luongCB = rs.getDouble("LuongCoBan");
                int soNgayLam = rs.getInt("SoNgayLam");
                double luongNgay = (luongCB / 26) * soNgayLam;
                double soGioOT = rs.getDouble("SoGioOT");
                double tienOT = rs.getDouble("LuongOT");
                double thucLinh = rs.getDouble("ThucLinh");

                tableModel.addRow(new Object[]{
                        rs.getString("MaNV"),
                        rs.getString("HoTen"),
                        rs.getInt("Thang") + "/" + rs.getInt("Nam"),
                        CurrencyUtils.formatVND(luongCB),
                        soNgayLam + " ngày",
                        CurrencyUtils.formatVND(luongNgay),
                        soGioOT + " giờ",
                        CurrencyUtils.formatVND(tienOT),
                        CurrencyUtils.formatVND(thucLinh)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tinhToanVaCapNhatLuong() {
        int thang = (int) cbThang.getSelectedItem();
        int nam = (int) cbNam.getSelectedItem();

        // Công thức: 
        // 1 Ngày công chuẩn = 26 ngày. Lương 1 ngày = Lương cơ bản / 26
        // Lương 1 giờ OT = (Lương cơ bản / 26 / 8) * 1.5
        // Thực Lĩnh = (Lương cơ bản / 26 * Số ngày làm) + Tiền OT
        String sqlQuery = "SELECT n.MaNV, n.LuongCoBan, COALESCE(c.SoNgayLam, 0) as SoNgayLam, COALESCE(c.SoGioOT, 0) as SoGioOT " +
                          "FROM NhanVien n LEFT JOIN ChamCong c ON n.MaNV = c.MaNV AND c.Thang = ? AND c.Nam = ?";

        String sqlUpsert = "INSERT INTO BangLuong (MaNV, Thang, Nam, LuongCoBan, LuongOT, ThucLinh) " +
                           "VALUES (?, ?, ?, ?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE LuongCoBan = VALUES(LuongCoBan), LuongOT = VALUES(LuongOT), ThucLinh = VALUES(ThucLinh)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psQuery = conn.prepareStatement(sqlQuery);
             PreparedStatement psUpsert = conn.prepareStatement(sqlUpsert)) {

            psQuery.setInt(1, thang);
            psQuery.setInt(2, nam);
            ResultSet rs = psQuery.executeQuery();

            int count = 0;
            while (rs.next()) {
                String maNV = rs.getString("MaNV");
                double luongCB = rs.getDouble("LuongCoBan");
                int soNgayLam = rs.getInt("SoNgayLam");
                double soGioOT = rs.getDouble("SoGioOT");

                double luongMotGio = (luongCB / 26) / 8;
                double tienOT = soGioOT * luongMotGio * 1.5;
                double thucLinh = (luongCB / 26 * soNgayLam) + tienOT;

                psUpsert.setString(1, maNV);
                psUpsert.setInt(2, thang);
                psUpsert.setInt(3, nam);
                psUpsert.setDouble(4, luongCB);
                psUpsert.setDouble(5, tienOT);
                psUpsert.setDouble(6, thucLinh);
                psUpsert.addBatch();
                count++;
            }

            psUpsert.executeBatch();
            JOptionPane.showMessageDialog(this, "Đã tự động tính và cập nhật lương thành công cho " + count + " nhân viên!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            loadBangLuongData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tính toán bảng lương: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}