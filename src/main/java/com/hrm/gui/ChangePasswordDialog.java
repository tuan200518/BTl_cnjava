package com.hrm.gui;
import com.hrm.model.UserSession;
import com.hrm.service.AccountService;
import javax.swing.*;
import java.awt.*;
public class ChangePasswordDialog {
    private ChangePasswordDialog(){}
    public static void show(Component parent, UserSession session){JPasswordField old=new JPasswordField(20),next=new JPasswordField(20),confirm=new JPasswordField(20);JPanel p=new JPanel(new GridLayout(0,2,8,8));p.add(new JLabel("Mật khẩu hiện tại"));p.add(old);p.add(new JLabel("Mật khẩu mới (ít nhất 8 ký tự)"));p.add(next);p.add(new JLabel("Nhập lại mật khẩu mới"));p.add(confirm);
        if(JOptionPane.showConfirmDialog(parent,p,"Đổi mật khẩu",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)!=JOptionPane.OK_OPTION)return;
        String n=new String(next.getPassword()),c=new String(confirm.getPassword());if(!n.equals(c)){JOptionPane.showMessageDialog(parent,"Mật khẩu nhập lại không khớp.","Lỗi",JOptionPane.ERROR_MESSAGE);return;}try{boolean ok=new AccountService().changePassword(session.userId(),new String(old.getPassword()),n);JOptionPane.showMessageDialog(parent,ok?"Đổi mật khẩu thành công.":"Mật khẩu hiện tại không đúng.",ok?"Thành công":"Lỗi",ok?JOptionPane.INFORMATION_MESSAGE:JOptionPane.ERROR_MESSAGE);}catch(Exception e){JOptionPane.showMessageDialog(parent,e.getMessage(),"Không thể đổi mật khẩu",JOptionPane.ERROR_MESSAGE);}}
}
