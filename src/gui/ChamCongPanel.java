package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel Ghi nhận Chấm công hàng ngày
 */
public class ChamCongPanel extends JPanel {
    private JTable tableChamCong;
    private JComboBox<String> cbNhanVien, cbTrangThai;
    private JTextField txtNgayLam;
    private JButton btnAdd, btnEdit, btnDelete, btnLoadData;
    
    public ChamCongPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel - Input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Ghi Nhận Chấm Công"));
        
        inputPanel.add(new JLabel("Nhân Viên:"));
        cbNhanVien = new JComboBox<>();
        inputPanel.add(cbNhanVien);
        
        inputPanel.add(new JLabel("Ngày Làm:"));
        txtNgayLam = new JTextField();
        inputPanel.add(txtNgayLam);
        
        inputPanel.add(new JLabel("Trạng Thái:"));
        cbTrangThai = new JComboBox<>(new String[]{"Có Mặt", "Vắng", "Nghỉ Phép", "Nghỉ Bệnh"});
        inputPanel.add(cbTrangThai);
        
        inputPanel.add(new JLabel(""));
        inputPanel.add(new JLabel(""));
        
        add(inputPanel, BorderLayout.NORTH);
        
        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnAdd = new JButton("Ghi Nhận");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnLoadData = new JButton("Tải Dữ Liệu");
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(btnLoadData);
        add(btnPanel, BorderLayout.CENTER);
        
        // Table
        String[] columns = {"ID", "Mã NV", "Ngày Làm", "Trạng Thái", "Ghi Chú"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tableChamCong = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableChamCong);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, btnPanel, scrollPane);
        add(splitPane, BorderLayout.CENTER);
        
        // Add listeners
        btnAdd.addActionListener(e -> addChamCong());
        btnEdit.addActionListener(e -> editChamCong());
        btnDelete.addActionListener(e -> deleteChamCong());
        btnLoadData.addActionListener(e -> loadData());
    }
    
    private void addChamCong() {
        JOptionPane.showMessageDialog(this, "Ghi nhận chấm công thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editChamCong() {
        JOptionPane.showMessageDialog(this, "Sửa chấm công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteChamCong() {
        JOptionPane.showMessageDialog(this, "Xóa chấm công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void loadData() {
        JOptionPane.showMessageDialog(this, "Tải dữ liệu chấm công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
