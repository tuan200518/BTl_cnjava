package com.hrm.gui;

import com.hrm.model.UserSession;
import com.hrm.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField user = new JTextField();
    private final JPasswordField pass = new JPasswordField();

    public LoginFrame() {
        Ui.init();

        setTitle("HRM Desktop - Đăng nhập");
        setSize(860, 520);
        setMinimumSize(new Dimension(760, 480));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(brand());
        root.add(form());
        setContentPane(root);

        getRootPane().setDefaultButton(findLoginButton());
    }

    private JButton loginButton;

    private JButton findLoginButton() {
        return loginButton;
    }

    private JPanel brand() {
        JPanel p = new JPanel();
        p.setBackground(new Color(0x171B4D));
        p.setBorder(new EmptyBorder(45, 45, 45, 45));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("HR");
        logo.setOpaque(true);
        logo.setBackground(Ui.PRIMARY);
        logo.setForeground(Color.WHITE);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        logo.setPreferredSize(new Dimension(64, 64));
        logo.setMaximumSize(new Dimension(64, 64));

        JLabel title = new JLabel("HRM Desktop");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel sub = new JLabel(
                "<html>Quản lý nhân sự<br>Chấm công & tiền lương</html>");
        sub.setForeground(new Color(0xC8CDF0));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        p.add(logo);
        p.add(Box.createVerticalStrut(28));
        p.add(title);
        p.add(Box.createVerticalStrut(10));
        p.add(sub);
        p.add(Box.createVerticalGlue());

        JLabel note = new JLabel("Hệ thống quản trị doanh nghiệp");
        note.setForeground(new Color(0xAEB4DE));
        note.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(note);

        return p;
    }

    private JPanel form() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(Ui.BG);

        JPanel p = new JPanel();
        p.setBackground(Ui.SURFACE);
        p.setBorder(new EmptyBorder(36, 42, 36, 42));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setPreferredSize(new Dimension(350, 370));

        JLabel title = new JLabel("Đăng nhập");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(Ui.TEXT);

        JLabel sub = new JLabel("Đăng nhập để tiếp tục vào hệ thống");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(Ui.MUTED);

        p.add(title);
        p.add(Box.createVerticalStrut(7));
        p.add(sub);
        p.add(Box.createVerticalStrut(28));

        p.add(label("Tên đăng nhập"));
        p.add(Box.createVerticalStrut(7));
        p.add(user);
        p.add(Box.createVerticalStrut(18));

        p.add(label("Mật khẩu"));
        p.add(Box.createVerticalStrut(7));
        p.add(pass);
        p.add(Box.createVerticalStrut(24));

        loginButton = Ui.primaryBtn("Đăng nhập");
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginButton.addActionListener(e -> login());
        p.add(loginButton);

        outer.add(p);
        return outer;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(Ui.TEXT);
        return l;
    }

    private void login() {
        try {
            String u = user.getText().trim();
            String pw = new String(pass.getPassword());

            if (u.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập tài khoản và mật khẩu.",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UserSession s = new AuthService().login(u, pw);

            if (s == null) {
                JOptionPane.showMessageDialog(this,
                        "Sai tài khoản/mật khẩu hoặc tài khoản đã bị khóa.",
                        "Đăng nhập thất bại",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
            new MainFrame(s).setVisible(true);

        } catch (Exception e) {
            Ui.error(this, e);
        }
    }
}
