package gui;

import bus.TaiKhoanBUS;
import model.TaiKhoan;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginForm extends JFrame {
    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap, btnThoat;
    private TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();

    public LoginForm() {
        setTitle("Đăng Nhập Hệ Thống");
        setSize(440, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Nền tổng thể màu xám nhạt
        JPanel mainBackground = new JPanel(new GridBagLayout());
        mainBackground.setBackground(new Color(241, 245, 249));

        // Khung Card trắng trung tâm
        JPanel cardPanel = new JPanel(new BorderLayout(0, 18));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setPreferredSize(new Dimension(340, 330));
        cardPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(25, 30, 25, 30)
        ));

        // --- TOP: TIÊU ĐỀ ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setOpaque(false);

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 41, 59));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Hệ thống Quản lý Nhân sự");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(100, 116, 139));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlHeader.add(lblTitle);
        pnlHeader.add(Box.createVerticalStrut(4));
        pnlHeader.add(lblSubtitle);

        // --- CENTER: FORM NHẬP (CĂN THẲNG HÀNG 100% RỒNG) ---
        JPanel pnlForm = new JPanel(new GridLayout(4, 1, 0, 5));
        pnlForm.setOpaque(false);

        JLabel lblUser = new JLabel("Tài khoản");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(51, 65, 85));

        txtTaiKhoan = new JTextField();
        styleTextField(txtTaiKhoan);

        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(51, 65, 85));

        txtMatKhau = new JPasswordField();
        styleTextField(txtMatKhau);

        pnlForm.add(lblUser);
        pnlForm.add(txtTaiKhoan);
        pnlForm.add(lblPass);
        pnlForm.add(txtMatKhau);

        // --- BOTTOM: NÚT BẤM (RỘNG BẰNG KHUNG NHẬP LIỆU) ---
        JPanel pnlButtons = new JPanel(new GridLayout(1, 2, 12, 0));
        pnlButtons.setOpaque(false);
        pnlButtons.setPreferredSize(new Dimension(0, 38));

        btnDangNhap = new JButton("Đăng Nhập");
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDangNhap.setBackground(new Color(37, 99, 235));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnThoat.setBackground(new Color(241, 245, 249));
        btnThoat.setForeground(new Color(71, 85, 105));
        btnThoat.setFocusPainted(false);
        btnThoat.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlButtons.add(btnDangNhap);
        pnlButtons.add(btnThoat);

        // Ghép các phần vào Card
        cardPanel.add(pnlHeader, BorderLayout.NORTH);
        cardPanel.add(pnlForm, BorderLayout.CENTER);
        cardPanel.add(pnlButtons, BorderLayout.SOUTH);

        mainBackground.add(cardPanel);
        add(mainBackground);

        initEvents();
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private void initEvents() {
        btnDangNhap.addActionListener(e -> handleLogin());

        txtMatKhau.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        btnThoat.addActionListener(e -> System.exit(0));
    }

    private void handleLogin() {
        String user = txtTaiKhoan.getText().trim();
        String pass = new String(txtMatKhau.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Thông Báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            TaiKhoan tk = taiKhoanBUS.dangNhap(user, pass);
            if (tk != null) {
                MainFrame mainFrame = new MainFrame(tk);
                mainFrame.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác!", "Lỗi Đăng Nhập", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo hệ thống:\n" + ex.getMessage(), "Lỗi Fatal", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}