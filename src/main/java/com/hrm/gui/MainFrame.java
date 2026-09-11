package com.hrm.gui;

import com.hrm.model.UserSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Khung chính của ứng dụng.
 * Chỉ thay đổi cách trình bày/navigation, không thay đổi quyền hay backend.
 */
public class MainFrame extends JFrame {

    private final JPanel content = new JPanel(new CardLayout());
    private final JPanel menu = new JPanel();
    private final List<JButton> menuButtons = new ArrayList<>();
    private final Color SIDEBAR = new Color(0x171B4D);
    private final Color SIDEBAR_ACTIVE = new Color(0x5146E5);

    public MainFrame(UserSession s) {
        Ui.init();

        setTitle("HRM Desktop - " + s.username() + " [" + roleLabel(s.role()) + "]");
        setSize(1400, 850);
        setMinimumSize(new Dimension(1050, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        build(s);
    }

    private void build(UserSession s) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Ui.BG);

        root.add(createSidebar(s), BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel createSidebar(UserSession s) {
        JPanel side = new JPanel(new BorderLayout());
        side.setBackground(SIDEBAR);
        side.setPreferredSize(new Dimension(260, 0));
        side.setBorder(new EmptyBorder(0, 14, 14, 14));

        // Brand
        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setBorder(new EmptyBorder(28, 12, 22, 8));
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));

        JLabel name = new JLabel("HRM Desktop");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel sub = new JLabel("HỆ THỐNG QUẢN TRỊ NHÂN SỰ");
        sub.setForeground(new Color(0xBFC4E9));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        brand.add(name);
        brand.add(Box.createVerticalStrut(7));
        brand.add(sub);

        side.add(brand, BorderLayout.NORTH);

        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        if (s.isHR()) {
            addMenu("Tổng quan", "dashboard", new DashboardPanel());
            addMenu("Nhân viên", "employees", new EmployeePanel());
            addMenu("Chấm công", "attendance", new AttendancePanel());
            addMenu("Sự kiện nhân sự", "events", new HRHistoryPanel(true));
            addMenu("Định biên", "staffing", new StaffingPanel());
            addMenu("Dự án", "projects", new ProjectPanel());
            addMenu("Phân công NV", "assignment", new AssignmentPanel());
            addMenu("Phiếu lương", "payroll", new HRPayrollPanel(false));
        } else if (s.isAccountant()) {
            addMenu("Tổng quan", "dashboard", new DashboardPanel());
            addMenu("Chấm công", "attendance", new AttendancePanel());
            addMenu("Sự kiện nhân sự", "events", new HRHistoryPanel(false));
            addMenu("Phiếu lương", "payroll", new HRPayrollPanel(true));
        } else if (s.isEmployee()) {
            addMenu("Tổng quan cá nhân", "home", new EmployeeHomePanel(s.employeeId()));
            addMenu("Phiếu lương của tôi", "mypayslip", new MyPayslipPanel(s.employeeId()));
        } else if (s.isAdmin()) {
            addMenu("Quản trị hệ thống", "maintenance", new MaintenancePanel());
        }

        JScrollPane menuScroll = new JScrollPane(menu);
        menuScroll.setBorder(null);
        menuScroll.setOpaque(false);
        menuScroll.getViewport().setOpaque(false);
        menuScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        menuScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        side.add(menuScroll, BorderLayout.CENTER);

        // User footer
        JPanel footer = new JPanel(new BorderLayout(10, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(15, 6, 0, 6));

        JPanel user = new JPanel();
        user.setOpaque(false);
        user.setLayout(new BoxLayout(user, BoxLayout.Y_AXIS));

        JLabel userName = new JLabel(s.username());
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel role = new JLabel(roleLabel(s.role()));
        role.setForeground(new Color(0xBFC4E9));
        role.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        user.add(userName);
        user.add(Box.createVerticalStrut(3));
        user.add(role);

        JButton logout = new JButton("Đăng xuất");
        logout.setFocusPainted(false);
        logout.setBorderPainted(true);
        logout.setOpaque(true);
        logout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logout.setBackground(new Color(0x37328F));
        logout.setForeground(Color.WHITE);
        logout.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.LineBorder(new Color(0x6D67D8), 1, true),
                new EmptyBorder(10, 12, 10, 12)));
        logout.setPreferredSize(new Dimension(220, 42));
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                logout.setBackground(new Color(0x4F46B8));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                logout.setBackground(new Color(0x37328F));
            }
        });
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        footer.add(user, BorderLayout.CENTER);
        footer.add(logout, BorderLayout.SOUTH);

        side.add(footer, BorderLayout.SOUTH);

        return side;
    }

    private void addMenu(String title, String key, JComponent panel) {
        content.add(panel, key);

        JButton b = new JButton(title);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        b.setPreferredSize(new Dimension(220, 46));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setBackground(SIDEBAR);
        b.setForeground(new Color(0xDDE1FF));
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(new EmptyBorder(0, 18, 0, 10));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addActionListener(e -> {
            ((CardLayout) content.getLayout()).show(content, key);
            selectMenu(b);
        });

        menuButtons.add(b);
        menu.add(b);
        menu.add(Box.createVerticalStrut(3));

        if (menuButtons.size() == 1) {
            SwingUtilities.invokeLater(() -> {
                ((CardLayout) content.getLayout()).show(content, key);
                selectMenu(b);
            });
        }
    }

    private void selectMenu(JButton selected) {
        for (JButton b : menuButtons) {
            b.setBackground(b == selected ? SIDEBAR_ACTIVE : SIDEBAR);
            b.setForeground(b == selected ? Color.WHITE : new Color(0xDDE1FF));
        }
    }

    private String roleLabel(String role) {
        return "ACCOUNTANT".equals(role) ? "KẾ TOÁN TRƯỞNG" : role;
    }
}
