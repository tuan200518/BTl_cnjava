package gui;

import bus.PhongBanBUS;
import model.PhongBan;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PhongBanPanel extends JPanel {
    private JTextField txtMaPB, txtTenPB, txtMoTa;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JTable tblPhongBan;
    private DefaultTableModel tableModel;
    private PhongBanBUS phongBanBUS = new PhongBanBUS();

    public PhongBanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- FORM NHẬP LIỆU (TOP) ---
        JPanel pnlForm = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông Tin Phòng Ban"));

        pnlForm.add(new JLabel("Mã Phòng Ban:"));
        txtMaPB = new JTextField();
        pnlForm.add(txtMaPB);

        pnlForm.add(new JLabel("Tên Phòng Ban:"));
        txtTenPB = new JTextField();
        pnlForm.add(txtTenPB);

        pnlForm.add(new JLabel("Mô Tả:"));
        txtMoTa = new JTextField();
        pnlForm.add(txtMoTa);

        add(pnlForm, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU (CENTER) ---
        tableModel = new DefaultTableModel(new String[]{"Mã Phòng Ban", "Tên Phòng Ban", "Mô Tả"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên bảng
            }
        };
        tblPhongBan = new JTable(tableModel);
        tblPhongBan.setRowHeight(25);
        add(new JScrollPane(tblPhongBan), BorderLayout.CENTER);

        // --- THANH NÚT BẤM (SOUTH) ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm Mới");

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        add(pnlButtons, BorderLayout.SOUTH);

        // --- SỰ KIỆN ---
        initEvents();
        loadTableData();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        for (PhongBan pb : phongBanBUS.getAll()) {
            tableModel.addRow(new Object[]{pb.getMaPhongBan(), pb.getTenPhongBan(), pb.getMoTa()});
        }
    }

    private void initEvents() {
        // Event click dòng trong bảng
        tblPhongBan.getSelectionModel().addListSelectionListener(e -> {
            int row = tblPhongBan.getSelectedRow();
            if (row >= 0) {
                txtMaPB.setText(tableModel.getValueAt(row, 0).toString());
                txtTenPB.setText(tableModel.getValueAt(row, 1).toString());
                txtMoTa.setText(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
                txtMaPB.setEditable(false); // Không cho sửa khóa chính
            }
        });

        // Event Thêm
        btnThem.addActionListener(e -> {
            PhongBan pb = new PhongBan(txtMaPB.getText().trim(), txtTenPB.getText().trim(), txtMoTa.getText().trim());
            String res = phongBanBUS.add(pb);
            if ("SUCCESS".equals(res)) {
                JOptionPane.showMessageDialog(this, "Thêm phòng ban thành công!");
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, res, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Event Sửa
        btnSua.addActionListener(e -> {
            PhongBan pb = new PhongBan(txtMaPB.getText().trim(), txtTenPB.getText().trim(), txtMoTa.getText().trim());
            String res = phongBanBUS.update(pb);
            if ("SUCCESS".equals(res)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, res, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Event Xóa
        btnXoa.addActionListener(e -> {
            String maPB = txtMaPB.getText().trim();
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phòng ban này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                String res = phongBanBUS.delete(maPB);
                if ("SUCCESS".equals(res)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadTableData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, res, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Event Làm mới Form
        btnLamMoi.addActionListener(e -> clearForm());
    }

    private void clearForm() {
        txtMaPB.setText("");
        txtTenPB.setText("");
        txtMoTa.setText("");
        txtMaPB.setEditable(true);
        tblPhongBan.clearSelection();
    }
}