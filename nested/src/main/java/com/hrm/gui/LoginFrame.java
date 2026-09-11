package com.hrm.gui;
import com.hrm.model.UserSession;import com.hrm.service.AuthService;import javax.swing.*;import java.awt.*;
public class LoginFrame extends JFrame{
 private final JTextField user=new JTextField(); private final JPasswordField pass=new JPasswordField();
 public LoginFrame(){setTitle("HRM Desktop - Đăng nhập");setSize(430,300);setLocationRelativeTo(null);setDefaultCloseOperation(EXIT_ON_CLOSE);JPanel p=new JPanel(new GridLayout(0,1,8,8));p.setBorder(BorderFactory.createEmptyBorder(30,45,30,45));JLabel t=new JLabel("QUẢN LÝ NHÂN SỰ",SwingConstants.CENTER);t.setFont(t.getFont().deriveFont(Font.BOLD,24f));p.add(t);p.add(new JLabel("Tên đăng nhập"));p.add(user);p.add(new JLabel("Mật khẩu"));p.add(pass);JButton b=Ui.btn("ĐĂNG NHẬP");p.add(b);add(p);b.addActionListener(e->login());getRootPane().setDefaultButton(b);}
 private void login(){try{String u=user.getText().trim(),pw=new String(pass.getPassword());if(u.isEmpty()||pw.isEmpty()){JOptionPane.showMessageDialog(this,"Vui lòng nhập tài khoản và mật khẩu.");return;}UserSession s=new AuthService().login(u,pw);if(s==null){JOptionPane.showMessageDialog(this,"Sai tài khoản/mật khẩu hoặc tài khoản đã bị khóa.");return;}dispose();new MainFrame(s).setVisible(true);}catch(Exception e){Ui.error(this,e);}}
}
