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

public class NhanVienPanel extends JPanel {
    private JTextField txtMaNV, txtHoTen, txtSoDienThoai, txtEmail, txtLuongCoBan;
    private JComboBox<String> cbGioiTinh, cbPhongBan;
    private JTable tblNhanVien;
    private DefaultTableModel tableModel;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnXuatExcel;

    public NhanVienPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PANEL INPUT FORM (NORTH) ---
        JPanel pnlForm = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông Tin Nhân Viên"));

        pnlForm.add(new JLabel("Mã Nhân Viên:"));
        txtMaNV = new JTextField();
        pnlForm.add(txtMaNV);

        pnlForm.add(new JLabel("Họ Và Tên:"));
        txtHoTen = new JTextField();
        pnlForm.add(txtHoTen);

        pnlForm.add(new JLabel("Giới Tính:"));
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        pnlForm.add(cbGioiTinh);

        pnlForm.add(new JLabel("Số Điện Thoại:"));
        txtSoDienThoai = new JTextField();
        pnlForm.add(txtSoDienThoai);

        pnlForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        pnlForm.add(txtEmail);

        pnlForm.add(new JLabel("Phòng Ban:"));
        cbPhongBan = new JComboBox<>();
        pnlForm.add(cbPhongBan);

        pnlForm.add(new JLabel("Lương Cơ Bản (VNĐ):"));
        txtLuongCoBan = new JTextField();
        pnlForm.add(txtLuongCoBan);

        add(pnlForm, BorderLayout.NORTH);

        // --- TABLE (CENTER) ---
        String[] columns = {"Mã NV", "Họ Tên", "Giới Tính", "SĐT", "Email", "Phòng Ban", "Lương Cơ Bản"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblNhanVien = new JTable(tableModel);
        tblNhanVien.setRowHeight(25);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        // --- PANEL BUTTONS (SOUTH) ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm Mới");
        
        btnXuatExcel = new JButton("Xuất Excel");
        btnXuatExcel.setBackground(new Color(39, 174, 96));
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(btnXuatExcel);

        add(pnlButtons, BorderLayout.SOUTH);

        // --- SỰ KIỆN ---
        initEvents();

        // --- KHỞI TẠO DỮ LIỆU ---
        loadPhongBanCombobox();
        loadNhanVienData();
    }

    private void initEvents() {
        // Chọn dòng trong bảng đẩy dữ liệu lên form
        tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tblNhanVien.getSelectedRow();
            if (selectedRow >= 0) {
                txtMaNV.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtMaNV.setEditable(false); // Không cho sửa khóa chính
                txtHoTen.setText(tableModel.getValueAt(selectedRow, 1).toString());
                cbGioiTinh.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
                txtSoDienThoai.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtEmail.setText(tableModel.getValueAt(selectedRow, 4).toString());
                cbPhongBan.setSelectedItem(tableModel.getValueAt(selectedRow, 5).toString());
                
                String luongStr = tableModel.getValueAt(selectedRow, 6).toString();
                txtLuongCoBan.setText(CurrencyUtils.formatNumber(CurrencyUtils.parseVND(luongStr)));
            }
        });

        btnLamMoi.addActionListener(e -> clearForm());
        btnThem.addActionListener(e -> themNhanVien());
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        btnXuatExcel.addActionListener(e -> ExcelExporter.exportJTableToCSV(tblNhanVien, "DanhSach_NhanVien"));
    }

    public void loadPhongBanCombobox() {
        cbPhongBan.removeAllItems();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT MaPhongBan, TenPhongBan FROM PhongBan");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cbPhongBan.addItem(rs.getString("MaPhongBan") + " - " + rs.getString("TenPhongBan"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNhanVienData() {
        tableModel.setRowCount(0);
        String sql = "SELECT n.MaNV, n.HoTen, n.GioiTinh, n.SoDienThoai, n.Email, p.TenPhongBan, n.LuongCoBan " +
                     "FROM NhanVien n LEFT JOIN PhongBan p ON n.MaPhongBan = p.MaPhongBan";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("MaNV"),
                        rs.getString("HoTen"),
                        rs.getString("GioiTinh"),
                        rs.getString("SoDienThoai"),
                        rs.getString("Email"),
                        rs.getString("TenPhongBan") != null ? rs.getString("TenPhongBan") : "Chưa phân phòng",
                        CurrencyUtils.formatVND(rs.getDouble("LuongCoBan"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtMaNV.setText("");
        txtMaNV.setEditable(true);
        txtHoTen.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtLuongCoBan.setText("");
        tblNhanVien.clearSelection();
    }

    private void themNhanVien() {
        if (txtMaNV.getText().trim().isEmpty() || txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã NV và Họ Tên!", "Thắc mắc", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedPB = (String) cbPhongBan.getSelectedItem();
        String maPB = selectedPB != null ? selectedPB.split(" - ")[0] : null;

        String sql = "INSERT INTO NhanVien (MaNV, HoTen, GioiTinh, SoDienThoai, Email, MaPhongBan, LuongCoBan) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtMaNV.getText().trim());
            ps.setString(2, txtHoTen.getText().trim());
            ps.setString(3, (String) cbGioiTinh.getSelectedItem());
            ps.setString(4, txtSoDienThoai.getText().trim());
            ps.setString(5, txtEmail.getText().trim());
            ps.setString(6, maPB);
            ps.setDouble(7, Double.parseDouble(txtLuongCoBan.getText().trim().isEmpty() ? "0" : txtLuongCoBan.getText().trim()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            clearForm();
            loadNhanVienData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaNhanVien() {
        if (txtMaNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!", "Thắc mắc", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedPB = (String) cbPhongBan.getSelectedItem();
        String maPB = selectedPB != null ? selectedPB.split(" - ")[0] : null;

        String sql = "UPDATE NhanVien SET HoTen=?, GioiTinh=?, SoDienThoai=?, Email=?, MaPhongBan=?, LuongCoBan=? WHERE MaNV=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtHoTen.getText().trim());
            ps.setString(2, (String) cbGioiTinh.getSelectedItem());
            ps.setString(3, txtSoDienThoai.getText().trim());
            ps.setString(4, txtEmail.getText().trim());
            ps.setString(5, maPB);
            ps.setDouble(6, Double.parseDouble(txtLuongCoBan.getText().trim().isEmpty() ? "0" : txtLuongCoBan.getText().trim()));
            ps.setString(7, txtMaNV.getText().trim());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
            clearForm();
            loadNhanVienData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaNhanVien() {
        if (txtMaNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!", "Thắc mắc", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên " + txtMaNV.getText() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM NhanVien WHERE MaNV=?")) {
                ps.setString(1, txtMaNV.getText().trim());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Đã xóa nhân viên!");
                clearForm();
                loadNhanVienData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}