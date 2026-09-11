package com.hrm.gui;

import com.hrm.model.Payroll;
import com.hrm.service.PayrollService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HRPayrollPanel extends JPanel {
    private final JTable t=Ui.table(new String[]{"ID","Mã NV","Nhân viên","Tháng","Năm","Lương CB","OT","Thưởng","Khấu trừ","Thực lĩnh","Trạng thái"});
    private final JTextField q=new JTextField(16);
    private final JComboBox<String> y=new JComboBox<>(new String[]{"Tất cả 5 năm","2022","2023","2024","2025","2026"});
    private final JComboBox<String> status=new JComboBox<>(new String[]{"Tất cả","DRAFT","SENT"});
    private final JButton create=Ui.btn("Tạo phiếu lương"),send=Ui.btn("Gửi phiếu lương"),history=Ui.btn("Lịch sử phiếu lương");

    public HRPayrollPanel(){
        setLayout(new BorderLayout(8,8));
        add(Ui.top("Phiếu lương - Quản lý tạo, kiểm tra và gửi phiếu"),BorderLayout.NORTH);
        JPanel b=new JPanel(new FlowLayout(FlowLayout.LEFT));
        b.add(new JLabel("Tìm NV:"));b.add(q);b.add(new JLabel("Năm:"));b.add(y);b.add(new JLabel("Trạng thái:"));b.add(status);
        b.add(create);b.add(send);b.add(history);
        add(b,BorderLayout.SOUTH); add(new JScrollPane(t),BorderLayout.CENTER);
        Runnable load=this::load;
        q.addActionListener(e->load.run());y.addActionListener(e->load.run());status.addActionListener(e->load.run());
        create.addActionListener(e->new CreatePayrollDialog(SwingUtilities.getWindowAncestor(this)).setVisible(true));
        send.addActionListener(e->new SendPayrollDialog(SwingUtilities.getWindowAncestor(this)).setVisible(true));
        history.addActionListener(e->new PayrollHistoryDialog(SwingUtilities.getWindowAncestor(this)).setVisible(true));
        load.run();
    }

    private int yearValue(){String v=(String)y.getSelectedItem();return "Tất cả 5 năm".equals(v)?0:Integer.parseInt(v);}
    private void load(){
        try{
            var rows=new ArrayList<String[]>(); String k=q.getText().trim().toLowerCase(); String s=(String)status.getSelectedItem();
            for(Payroll p:new PayrollService().hr(yearValue()))
                if((k.isEmpty()||p.employeeCode().toLowerCase().contains(k)||p.employeeName().toLowerCase().contains(k)) && ("Tất cả".equals(s)||p.status().equals(s)))
                    rows.add(new String[]{String.valueOf(p.id()),p.employeeCode(),p.employeeName(),String.valueOf(p.month()),String.valueOf(p.year()),fmt(p.base()),fmt(p.overtime()),fmt(p.bonus()),fmt(p.deduction()),fmt(p.net()),p.status()});
            Ui.fill(t,rows);
        }catch(Exception e){Ui.error(this,e);}
    }
    private String fmt(double d){return String.format("%,.0f",d);}
}
