package com.hrm.gui;

import com.hrm.service.AttendanceService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MyAttendancePanel extends JPanel {
    private final int employeeId;
    private final AttendanceService service = new AttendanceService();
    private final JLabel today = new JLabel("Đang tải...");
    private final JTable history = Ui.table(new String[]{"Ngày", "Giờ vào", "Giờ ra", "Trạng thái", "OT (giờ)", "Phạt đi muộn", "Phạt về sớm", "Tổng phạt"});

    public MyAttendancePanel(int employeeId) {
        this.employeeId=employeeId;
        Ui.init(); setLayout(new BorderLayout(0,12)); setBackground(Ui.BG);
        JPanel header=new JPanel(new BorderLayout()); header.setBackground(Ui.BG);
        header.add(Ui.top("Chấm công của tôi", "Chấm công vào/ra và theo dõi lịch sử cá nhân"),BorderLayout.NORTH);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT)); actions.setOpaque(false);
        JButton in=new JButton("Chấm công vào"); JButton out=new JButton("Chấm công ra"); JButton refresh=new JButton("Làm mới");
        actions.add(in); actions.add(out); actions.add(refresh); header.add(actions,BorderLayout.SOUTH); add(header,BorderLayout.NORTH);
        JPanel center=new JPanel(new BorderLayout(0,10)); center.setBackground(Ui.BG); center.setBorder(new EmptyBorder(0,20,20,20));
        JPanel status=new JPanel(new BorderLayout()); status.setBackground(Ui.SURFACE); status.setBorder(new EmptyBorder(14,16,14,16)); status.add(today,BorderLayout.CENTER);
        center.add(status,BorderLayout.NORTH); center.add(Ui.scroll(history),BorderLayout.CENTER); add(center,BorderLayout.CENTER);
        in.addActionListener(e->act(true)); out.addActionListener(e->act(false)); refresh.addActionListener(e->load()); load();
    }
    private void act(boolean clockIn){
        try { if(clockIn) service.clockIn(employeeId); else service.clockOut(employeeId); JOptionPane.showMessageDialog(this,clockIn?"Đã ghi nhận giờ vào.":"Đã ghi nhận giờ ra."); load(); }
        catch(Exception ex){ Ui.error(this,ex); }
    }
    private void load(){
        try{
            String[] t=service.today(employeeId);
            today.setText("<html><b>Hôm nay ("+t[0]+")</b> &nbsp; | &nbsp; Vào: "+t[1]+" &nbsp; | &nbsp; Ra: "+t[2]+" &nbsp; | &nbsp; Trạng thái: "+t[3]+"<br><br>Quy định: sau 07:30 phạt 100.000 VNĐ/lần; trước 15:30 phạt 100.000 VNĐ/lần; ra sau 15:30 được tính OT.</html>");
            Ui.fill(history,service.history(employeeId));
        }catch(Exception ex){Ui.error(this,ex);}
    }
}
