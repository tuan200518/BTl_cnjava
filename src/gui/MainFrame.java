package gui;

import model.TaiKhoan;
import config.DBConnection;
import utils.CurrencyUtils;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class MainFrame extends JFrame {
    private TaiKhoan currentUser;

    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    // Kết nối CSDL dùng chung trong config.DBConnection

    private static final Color COLOR_SIDEBAR = new Color(11, 19, 38);          
    private static final Color COLOR_SIDEBAR_HOVER = new Color(30, 41, 59);    
    private static final Color COLOR_SIDEBAR_ACTIVE = new Color(37, 99, 235);  
    private static final Color COLOR_BG = new Color(241, 245, 249);             
    private static final Color COLOR_CARD = Color.WHITE;
    
    private static final Color COLOR_TEXT_DARK = new Color(15, 23, 42);         
    private static final Color COLOR_TEXT_MUTED = new Color(51, 65, 85);        
    private static final Color COLOR_BORDER = new Color(148, 163, 184);         

    private static final Color COLOR_BTN_PRIMARY = new Color(37, 99, 235);
    private static final Color COLOR_BTN_SUCCESS = new Color(13, 148, 136);     
    private static final Color COLOR_BTN_DANGER = new Color(225, 29, 72);       

    public MainFrame(TaiKhoan user) {
        this.currentUser = user;

        setTitle("Hệ Thống Quản Lý Nhân Sự Enterprise");
        setSize(1280, 780);
        setMinimumSize(new Dimension(1100, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        createSidebar();
        rootPanel.add(sidebarPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);

        rightPanel.add(createTopBar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        contentPanel.add(createPhongBanView(), "PhongBan");
        contentPanel.add(createNhanVienView(), "NhanVien");
        contentPanel.add(createChamCongView(), "ChamCong");
        contentPanel.add(createQuanLyLuongView(), "QuanLyLuong");
        contentPanel.add(createThongKeView(), "ThongKe");

        rightPanel.add(contentPanel, BorderLayout.CENTER);
        rightPanel.add(createStatusBar(), BorderLayout.SOUTH);

        rootPanel.add(rightPanel, BorderLayout.CENTER);
        add(rootPanel);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(COLOR_SIDEBAR);
        sidebarPanel.setPreferredSize(new Dimension(230, 0));
        sidebarPanel.setBorder(new EmptyBorder(20, 14, 20, 14));

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        brandPanel.setOpaque(false);
        brandPanel.setMaximumSize(new Dimension(230, 40));

        JLabel lblLogo = new JLabel("HRM");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);

        JLabel lblSubLogo = new JLabel("PRO");
        lblSubLogo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSubLogo.setForeground(new Color(96, 165, 250));

        brandPanel.add(lblLogo);
        brandPanel.add(lblSubLogo);
        sidebarPanel.add(brandPanel);
        sidebarPanel.add(Box.createVerticalStrut(28));

        addNavItem("Phòng Ban", "PhongBan", true);
        addNavItem("Nhân Viên", "NhanVien", false);
        addNavItem("Chấm Công", "ChamCong", false);
        addNavItem("Quản Lý Lương", "QuanLyLuong", false);
        addNavItem("Thống Kê & Báo Cáo", "ThongKe", false);

        sidebarPanel.add(Box.createVerticalGlue());

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogout.setForeground(new Color(254, 202, 202));
        btnLogout.setBackground(new Color(30, 41, 59));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(new EmptyBorder(10, 15, 10, 15));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setMaximumSize(new Dimension(202, 38));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });
        sidebarPanel.add(btnLogout);
    }

    private JPanel activeNavItem = null;

    private void addNavItem(String text, String cardName, boolean isActive) {
        JPanel navItem = new JPanel(new BorderLayout());
        navItem.setMaximumSize(new Dimension(202, 42));
        navItem.setPreferredSize(new Dimension(202, 42));
        navItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navItem.setBorder(new EmptyBorder(0, 16, 0, 16));

        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("Segoe UI", Font.BOLD, 13));

        if (isActive) {
            navItem.setBackground(COLOR_SIDEBAR_ACTIVE);
            lblText.setForeground(Color.WHITE);
            activeNavItem = navItem;
        } else {
            navItem.setBackground(COLOR_SIDEBAR);
            lblText.setForeground(new Color(226, 232, 240));
        }

        navItem.add(lblText, BorderLayout.WEST);

        navItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (activeNavItem != null) {
                    activeNavItem.setBackground(COLOR_SIDEBAR);
                    ((JLabel) activeNavItem.getComponent(0)).setForeground(new Color(226, 232, 240));
                }
                navItem.setBackground(COLOR_SIDEBAR_ACTIVE);
                lblText.setForeground(Color.WHITE);
                activeNavItem = navItem;

                cardLayout.show(contentPanel, cardName);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (navItem != activeNavItem) {
                    navItem.setBackground(COLOR_SIDEBAR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (navItem != activeNavItem) {
                    navItem.setBackground(COLOR_SIDEBAR);
                }
            }
        });

        sidebarPanel.add(navItem);
        sidebarPanel.add(Box.createVerticalStrut(6));
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(COLOR_CARD);
        topBar.setPreferredSize(new Dimension(0, 56));
        topBar.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(0, 24, 0, 24)
        ));

        JLabel lblAppTitle = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ DOANH NGHIỆP");
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAppTitle.setForeground(COLOR_TEXT_DARK);

        String nameDisplay = (currentUser != null) ? currentUser.getHoTen() : "Administrator";
        JLabel lblUser = new JLabel("Xin chào, " + nameDisplay);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(COLOR_SIDEBAR_ACTIVE);

        topBar.add(lblAppTitle, BorderLayout.WEST);
        topBar.add(lblUser, BorderLayout.EAST);
        return topBar;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(COLOR_CARD);
        statusBar.setPreferredSize(new Dimension(0, 28));
        statusBar.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
                new EmptyBorder(0, 16, 0, 16)
        ));

        JLabel lblStatus = new JLabel("● Trạng thái: Đã kết nối CSDL (MySQL)");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(new Color(13, 148, 136));

        JLabel lblTime = new JLabel("Phiên làm việc: Enterprise Edition v2.5");
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTime.setForeground(COLOR_TEXT_MUTED);

        statusBar.add(lblStatus, BorderLayout.WEST);
        statusBar.add(lblTime, BorderLayout.EAST);
        return statusBar;
    }

    private JPanel createPhongBanView() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setOpaque(false);

        JPanel formCard = createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMaPhong = new JTextField(20);
        JTextField txtTenPhong = new JTextField(20);
        JTextField txtMoTa = new JTextField(45);

        addFormField(formCard, gbc, 0, 0, "Mã Phòng Ban:", txtMaPhong);
        addFormField(formCard, gbc, 1, 0, "Tên Phòng Ban:", txtTenPhong);
        addFormField(formCard, gbc, 0, 1, "Mô Tả:", txtMoTa, 3);

        JPanel tableCard = createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 12));

        String[] cols = {"Mã Phòng Ban", "Tên Phòng Ban", "Mô Tả"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tbl = createModernTable(model);
        loadDataToTable(model, "SELECT MaPhongBan, TenPhongBan, MoTa FROM PhongBan");

        JButton btnThem = createStyledButton("Thêm", COLOR_BTN_PRIMARY, Color.WHITE);
        btnThem.addActionListener(e -> {
            String sql = "INSERT INTO PhongBan (MaPhongBan, TenPhongBan, MoTa) VALUES (?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtMaPhong.getText().trim());
                pstmt.setString(2, txtTenPhong.getText().trim());
                pstmt.setString(3, txtMoTa.getText().trim());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm phòng ban thành công!");
                model.setRowCount(0);
                loadDataToTable(model, "SELECT MaPhongBan, TenPhongBan, MoTa FROM PhongBan");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        JPanel actionHeader = createCustomActionHeader("Danh Sách Phòng Ban", true, btnThem);

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);

        tableCard.add(actionHeader, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        main.add(formCard, BorderLayout.NORTH);
        main.add(tableCard, BorderLayout.CENTER);
        return main;
    }

    private void loadPhongBanToComboBox(JComboBox<String> cb) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MaPhongBan, TenPhongBan FROM PhongBan")) {
            while (rs.next()) {
                cb.addItem(rs.getString("MaPhongBan") + " - " + rs.getString("TenPhongBan"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createNhanVienView() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setOpaque(false);

        JPanel formCard = createCardPanel();
        formCard.setLayout(new GridLayout(2, 4, 16, 10));

        JTextField txtMaNV = new JTextField();
        JTextField txtHoTen = new JTextField();
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField txtSdt = new JTextField();
        JTextField txtEmail = new JTextField();
        
        // Sử dụng JComboBox để chọn phòng ban trực quan thay vì gõ tay
        JComboBox<String> cbMaPhongBan = new JComboBox<>();
        loadPhongBanToComboBox(cbMaPhongBan);

        JTextField txtLuong = new JTextField();

        formCard.add(createInputGroup("Mã Nhân Viên", txtMaNV));
        formCard.add(createInputGroup("Họ Và Tên", txtHoTen));
        formCard.add(createInputGroup("Giới Tính", cbGioiTinh));
        formCard.add(createInputGroup("Số Điện Thoại", txtSdt));
        formCard.add(createInputGroup("Email", txtEmail));
        formCard.add(createInputGroup("Phòng Ban", cbMaPhongBan));
        formCard.add(createInputGroup("Lương Cơ Bản (VNĐ)", txtLuong));

        JPanel tableCard = createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 12));

        String[] cols = {"Mã NV", "Họ Tên", "Giới Tính", "SĐT", "Email", "Mã Phòng", "Lương CB"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tbl = createModernTable(model);
        loadDataToTable(model, "SELECT MaNV, HoTen, GioiTinh, SoDienThoai, Email, MaPhongBan, LuongCoBan FROM NhanVien");

        JButton btnThem = createStyledButton("Thêm", COLOR_BTN_PRIMARY, Color.WHITE);
        btnThem.addActionListener(e -> {
            String sql = "INSERT INTO NhanVien (MaNV, HoTen, GioiTinh, SoDienThoai, Email, MaPhongBan, LuongCoBan) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, txtMaNV.getText().trim());
                pstmt.setString(2, txtHoTen.getText().trim());
                pstmt.setString(3, cbGioiTinh.getSelectedItem().toString());
                pstmt.setString(4, txtSdt.getText().trim());
                pstmt.setString(5, txtEmail.getText().trim());
                
                // Lọc lấy riêng Mã Phòng Ban (phần trước dấu " - ") để lưu vào DB
                String selectedPhong = (String) cbMaPhongBan.getSelectedItem();
                String maPhong = (selectedPhong != null && selectedPhong.contains(" - ")) 
                        ? selectedPhong.split(" - ")[0] : selectedPhong;
                pstmt.setString(6, maPhong);
                
                pstmt.setDouble(7, Double.parseDouble(txtLuong.getText().trim()));
                
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                
                model.setRowCount(0);
                loadDataToTable(model, "SELECT MaNV, HoTen, GioiTinh, SoDienThoai, Email, MaPhongBan, LuongCoBan FROM NhanVien");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhân viên: " + ex.getMessage());
            }
        });

        JPanel actionHeader = createCustomActionHeader("Danh Sách Nhân Viên", true, btnThem);

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);

        tableCard.add(actionHeader, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        main.add(formCard, BorderLayout.NORTH);
        main.add(tableCard, BorderLayout.CENTER);
        return main;
    }

    private JPanel createChamCongView() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setOpaque(false);

        JPanel formCard = createCardPanel();
        formCard.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 8));

        JTextField txtMaNVCC = new JTextField(10);
        JTextField txtThang = new JTextField(5);
        JTextField txtNam = new JTextField(5);
        JTextField txtSoNgayLam = new JTextField(5);

        formCard.add(createInputGroup("Mã NV", txtMaNVCC));
        formCard.add(createInputGroup("Tháng", txtThang));
        formCard.add(createInputGroup("Năm", txtNam));
        formCard.add(createInputGroup("Số Ngày Làm", txtSoNgayLam));

        JButton btnGhiNhan = createStyledButton("Ghi Nhận Chấm Công", COLOR_BTN_PRIMARY, Color.WHITE);
        btnGhiNhan.addActionListener(e -> {
            String sql = "INSERT INTO ChamCong (MaNV, Thang, Nam, SoNgayLam) VALUES (?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtMaNVCC.getText().trim());
                pstmt.setInt(2, Integer.parseInt(txtThang.getText().trim()));
                pstmt.setInt(3, Integer.parseInt(txtNam.getText().trim()));
                pstmt.setDouble(4, Double.parseDouble(txtSoNgayLam.getText().trim()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Chấm công thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });
        formCard.add(btnGhiNhan);

        JPanel tableCard = createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 12));

        String[] cols = {"Mã CC", "Mã NV", "Tháng", "Nam", "Số Ngày Làm", "Số Ngày Nghỉ", "Số Giờ OT"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tbl = createModernTable(model);
        loadDataToTable(model, "SELECT MaCC, MaNV, Thang, Nam, SoNgayLam, SoNgayNghi, SoGioOT FROM ChamCong");

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);

        tableCard.add(createActionHeader("Nhật Ký Chấm Công", false, false), BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        main.add(formCard, BorderLayout.NORTH);
        main.add(tableCard, BorderLayout.CENTER);
        return main;
    }

    private JPanel createQuanLyLuongView() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setOpaque(false);

        JPanel filterCard = createCardPanel();
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 8));

        filterCard.add(createInputGroup("Tháng", new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})));
        filterCard.add(createInputGroup("Năm", new JComboBox<>(new String[]{"2025", "2026", "2027"})));
        filterCard.add(createStyledButton("Xem Bảng Lương", new Color(226, 232, 240), COLOR_TEXT_DARK));
        filterCard.add(createStyledButton("Tính / Cập Nhật Lương", COLOR_BTN_PRIMARY, Color.WHITE));

        JPanel tableCard = createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 12));

        String[] cols = {"Mã Lương", "Mã NV", "Tháng", "Năm", "Lương CB", "Phụ Cấp", "Lương OT", "Khấu Trừ", "Thực Lĩnh"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tbl = createModernTable(model);
        loadDataToTable(model, "SELECT MaLuong, MaNV, Thang, Nam, LuongCoBan, PhuCap, LuongOT, KhauTru, ThucLinh FROM BangLuong");

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);

        tableCard.add(createActionHeader("Bảng Lương Chi Tiết", true, false), BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        main.add(filterCard, BorderLayout.NORTH);
        main.add(tableCard, BorderLayout.CENTER);
        return main;
    }

    private JPanel createThongKeView() {
        JPanel main = new JPanel(new BorderLayout(0, 18));
        main.setOpaque(false);

        String tongNhanVien = getDbValue("SELECT COUNT(*) FROM NhanVien");
        String tongPhongBan = getDbValue("SELECT COUNT(*) FROM PhongBan");
        double quyLuong = getDbDouble("SELECT COALESCE(SUM(LuongCoBan), 0) FROM NhanVien");

        JPanel kpiPanel = new JPanel(new GridLayout(1, 3, 18, 0));
        kpiPanel.setOpaque(false);

        kpiPanel.add(createKPICard("TỔNG SỐ NHÂN VIÊN", tongNhanVien, "Nhân viên", new Color(37, 99, 235)));
        kpiPanel.add(createKPICard("TỔNG SỐ PHÒNG BAN", tongPhongBan, "Phòng ban", new Color(13, 148, 136)));
        kpiPanel.add(createKPICard("QUỸ LƯƠNG THÁNG", CurrencyUtils.formatVND(quyLuong), "", new Color(124, 58, 237)));

        JPanel tableCard = createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 12));

        String[] cols = {"Mã Phòng", "Tên Phòng Ban", "Số Lượng Nhân Viên"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tbl = createModernTable(model);
        loadDataToTable(model, "SELECT p.MaPhongBan, p.TenPhongBan, COUNT(n.MaNV) FROM PhongBan p LEFT JOIN NhanVien n ON p.MaPhongBan = n.MaPhongBan GROUP BY p.MaPhongBan, p.TenPhongBan");

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);

        tableCard.add(createActionHeader("Thống Kê Nhân Sự Theo Phòng Ban", false, false), BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        main.add(kpiPanel, BorderLayout.NORTH);
        main.add(tableCard, BorderLayout.CENTER);
        return main;
    }

    private void loadDataToTable(DefaultTableModel model, String query) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    String columnName = metaData.getColumnLabel(i);
                    if (value instanceof Number && isMoneyColumn(columnName)) {
                        rowData[i - 1] = CurrencyUtils.formatVND(((Number) value).doubleValue());
                    } else {
                        rowData[i - 1] = value;
                    }
                }
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tải dữ liệu cho câu lệnh: " + query);
        }
    }

    private String getDbValue(String query) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    private double getDbDouble(String query) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean isMoneyColumn(String columnName) {
        if (columnName == null) return false;
        return switch (columnName.toLowerCase()) {
            case "luongcoban", "phucap", "luongot", "khautru", "thuclinh" -> true;
            default -> false;
        };
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(COLOR_CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 20, 16, 20)
        ));
        return card;
    }

    private JPanel createKPICard(String title, String value, String unit, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(COLOR_CARD);
        card.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor),
                new CompoundBorder(
                        new LineBorder(COLOR_BORDER, 1, true),
                        new EmptyBorder(16, 20, 16, 20)
                )
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(COLOR_TEXT_MUTED);

        JLabel lblVal = new JLabel(value + " " + unit);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblVal.setForeground(COLOR_TEXT_DARK);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblVal, BorderLayout.CENTER);
        return card;
    }

    private JPanel createActionHeader(String title, boolean hasSearch, boolean hasCRUD) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        header.add(lblTitle, BorderLayout.WEST);

        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightControls.setOpaque(false);

        if (hasSearch) {
            JLabel lblSearch = new JLabel("Tìm kiếm:");
            lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblSearch.setForeground(COLOR_TEXT_DARK);

            JTextField txtSearch = new JTextField(14);
            txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtSearch.setBorder(new CompoundBorder(
                    new LineBorder(COLOR_BORDER, 1, true),
                    new EmptyBorder(5, 8, 5, 8)
            ));
            rightControls.add(lblSearch);
            rightControls.add(txtSearch);
            rightControls.add(Box.createHorizontalStrut(8));
        }

        header.add(rightControls, BorderLayout.EAST);
        return header;
    }

    private JPanel createCustomActionHeader(String title, boolean hasSearch, JButton customAddButton) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        header.add(lblTitle, BorderLayout.WEST);

        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightControls.setOpaque(false);

        if (hasSearch) {
            JLabel lblSearch = new JLabel("Tìm kiếm:");
            lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblSearch.setForeground(COLOR_TEXT_DARK);

            JTextField txtSearch = new JTextField(14);
            txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txtSearch.setBorder(new CompoundBorder(
                    new LineBorder(COLOR_BORDER, 1, true),
                    new EmptyBorder(5, 8, 5, 8)
            ));
            rightControls.add(lblSearch);
            rightControls.add(txtSearch);
            rightControls.add(Box.createHorizontalStrut(8));
        }

        rightControls.add(customAddButton);
        rightControls.add(createStyledButton("Sửa", new Color(241, 245, 249), COLOR_TEXT_DARK));
        rightControls.add(createStyledButton("Xóa", COLOR_BTN_DANGER, Color.WHITE));
        rightControls.add(createStyledButton("Làm Mới", new Color(241, 245, 249), COLOR_TEXT_DARK));

        header.add(rightControls, BorderLayout.EAST);
        return header;
    }

    private JPanel createInputGroup(String labelText, JComponent component) {
        JPanel group = new JPanel(new BorderLayout(0, 5));
        group.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(COLOR_TEXT_DARK);

        if (component instanceof JTextField) {
            component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            component.setBorder(new CompoundBorder(
                    new LineBorder(COLOR_BORDER, 1, true),
                    new EmptyBorder(6, 10, 6, 10)
            ));
        } else if (component instanceof JComboBox) {
            component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            component.setBackground(Color.WHITE);
        }

        group.add(label, BorderLayout.NORTH);
        group.add(component, BorderLayout.CENTER);
        return group;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int col, int row, String label, JTextField txt) {
        addFormField(panel, gbc, col, row, label, txt, 1);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int col, int row, String label, JTextField txt, int gridwidth) {
        gbc.gridx = col * 2;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(COLOR_TEXT_DARK);
        panel.add(lbl, gbc);

        gbc.gridx = col * 2 + 1;
        gbc.gridwidth = gridwidth;
        gbc.weightx = 1.0;
        
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        panel.add(txt, gbc);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new CompoundBorder(
                new LineBorder(bg.equals(Color.WHITE) || bg.equals(new Color(241, 245, 249)) || bg.equals(new Color(226, 232, 240)) ? COLOR_BORDER : bg, 1, true),
                new EmptyBorder(7, 14, 7, 14)
        ));
        return btn;
    }

    private JTable createModernTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(COLOR_TEXT_DARK);
                } else {
                    c.setBackground(new Color(219, 234, 254));
                    c.setForeground(COLOR_SIDEBAR_ACTIVE);
                }
                return c;
            }
        };

        table.setRowHeight(42);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(COLOR_TEXT_DARK);
        
        table.setGridColor(new Color(148, 163, 184));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(226, 232, 240));
        header.setForeground(new Color(15, 23, 42));
        header.setPreferredSize(new Dimension(0, 44));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 116, 139)));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        return table;
    }
}