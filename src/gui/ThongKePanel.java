package gui;

import config.DBConnection;
import utils.ExcelExporter;
import utils.CurrencyUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ThongKePanel extends JPanel {
    private JLabel lblTongNV, lblTongPB, lblTongQuyLuong;
    private JTable tblThongKePB;
    private DefaultTableModel tableModel;
    private JButton btnLamMoi, btnXuatExcel;

    public ThongKePanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- TOP: KPI CARDS ---
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));

        lblTongNV = createCardPanel(pnlCards, "TỔNG SỐ NHÂN VIÊN", "0 Nhân viên", new Color(41, 128, 185));
        lblTongPB = createCardPanel(pnlCards, "TỔNG SỐ PHÒNG BAN", "0 Phòng ban", new Color(39, 174, 96));
        lblTongQuyLuong = createCardPanel(pnlCards, "TỔNG QUỸ LƯƠNG (THÁNG GẦN NHẤT)", "0 VNĐ", new Color(142, 68, 173));

        add(pnlCards, BorderLayout.NORTH);

        // --- CENTER: TABLE CHI TIẾT THEO PHÒNG BAN ---
        JPanel pnlCenter = new JPanel(new BorderLayout(5, 5));
        pnlCenter.setBorder(BorderFactory.createTitledBorder("Thống Kê Nhân Sự Theo Phòng Ban"));

        tableModel = new DefaultTableModel(new String[]{"Mã Phòng", "Tên Phòng Ban", "Số Lượng Nhân Viên", "Tổng Lương Cơ Bản"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblThongKePB = new JTable(tableModel);
        tblThongKePB.setRowHeight(28);
        tblThongKePB.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlCenter.add(new JScrollPane(tblThongKePB), BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // --- SOUTH: BUTTONS ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnLamMoi = new JButton("Cập Nhật Dữ Liệu");
        btnXuatExcel = new JButton("Xuất Báo Cáo (Excel)");
        btnXuatExcel.setBackground(new Color(39, 174, 96));
        btnXuatExcel.setForeground(Color.WHITE);

        pnlBottom.add(btnLamMoi);
        pnlBottom.add(btnXuatExcel);
        add(pnlBottom, BorderLayout.SOUTH);

        // Events
        btnLamMoi.addActionListener(e -> loadStatisticsData());
        btnXuatExcel.addActionListener(e -> ExcelExporter.exportJTableToCSV(tblThongKePB, "BaoCao_ThongKe_PhongBan"));

        loadStatisticsData();
    }

    private JLabel createCardPanel(JPanel parent, String title, String defaultValue, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblValue = new JLabel(defaultValue);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setForeground(Color.WHITE);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        parent.add(card);
        return lblValue;
    }

    public void loadStatisticsData() {
        // 1. Thống kê tổng số
        try (Connection conn = DBConnection.getConnection()) {
            // Tổng số NV
            ResultSet rs1 = conn.prepareStatement("SELECT COUNT(*) FROM NhanVien").executeQuery();
            if (rs1.next()) lblTongNV.setText(rs1.getInt(1) + " Nhân viên");

            // Tổng số PB
            ResultSet rs2 = conn.prepareStatement("SELECT COUNT(*) FROM PhongBan").executeQuery();
            if (rs2.next()) lblTongPB.setText(rs2.getInt(1) + " Phòng ban");

            // Tổng Quỹ lương gần nhất
            ResultSet rs3 = conn.prepareStatement("SELECT SUM(ThucLinh) FROM BangLuong").executeQuery();
            if (rs3.next()) {
                double tongL = rs3.getDouble(1);
                lblTongQuyLuong.setText(CurrencyUtils.formatVND(tongL));
            }

            // 2. Thống kê theo từng phòng ban
            tableModel.setRowCount(0);
            String sqlPB = "SELECT p.MaPhongBan, p.TenPhongBan, COUNT(n.MaNV) as SoNV, COALESCE(SUM(n.LuongCoBan), 0) as TongLuong " +
                           "FROM PhongBan p LEFT JOIN NhanVien n ON p.MaPhongBan = n.MaPhongBan " +
                           "GROUP BY p.MaPhongBan, p.TenPhongBan";
            PreparedStatement ps = conn.prepareStatement(sqlPB);
            ResultSet rsPB = ps.executeQuery();
            while (rsPB.next()) {
                tableModel.addRow(new Object[]{
                    rsPB.getString("MaPhongBan"),
                    rsPB.getString("TenPhongBan"),
                    rsPB.getInt("SoNV") + " Người",
                    CurrencyUtils.formatVND(rsPB.getDouble("TongLuong"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}