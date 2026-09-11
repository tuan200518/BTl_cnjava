package com.hrm.gui;

import com.hrm.model.Payroll;
import com.hrm.service.PayrollService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HRPayrollPanel extends JPanel {
<<<<<<< HEAD
    private final JTable t=Ui.table(new String[]{"ID","Mã NV","Nhân viên","Tháng","Năm","Lương CB","OT","Thưởng","Phạt / trừ lương","Thực lĩnh","Trạng thái"});
    private final JTextField q=new JTextField(16);
    private final JComboBox<String> y;
    private final JComboBox<String> status=new JComboBox<>(new String[]{"Tất cả","DRAFT","SENT"});
    private final JButton create=Ui.btn("Tạo phiếu / tìm NV mới"),send=Ui.btn("Gửi phiếu lương"),history=Ui.btn("Lịch sử phiếu lương"),detail=Ui.btn("Xem chi tiết");

    public HRPayrollPanel(){this(false);}

    public HRPayrollPanel(boolean accountantMode){
        y=new JComboBox<>(accountantMode ? new String[]{"2022","2023","2024","2025","2026"} : new String[]{"Tất cả 5 năm","2022","2023","2024","2025","2026"});
        if(accountantMode)y.setSelectedItem("2026");
        setLayout(new BorderLayout(8,8));
        add(Ui.top(accountantMode ? "Phiếu lương - Kế toán trưởng: tạo, kiểm tra và gửi phiếu" : "Phiếu lương - Quản lý tạo, kiểm tra và gửi phiếu"),BorderLayout.NORTH);
        JPanel b=new JPanel(new FlowLayout(FlowLayout.LEFT));
        b.add(new JLabel("Tìm NV:"));b.add(q);b.add(new JLabel("Năm:"));b.add(y);b.add(new JLabel("Trạng thái:"));b.add(status);
        b.add(create);b.add(send);b.add(history);b.add(detail);
        add(b,BorderLayout.SOUTH); add(new JScrollPane(t),BorderLayout.CENTER);
        Runnable load=this::load;
        q.addActionListener(e->load.run());y.addActionListener(e->load.run());status.addActionListener(e->load.run());
        create.addActionListener(e->new CreatePayrollDialog(SwingUtilities.getWindowAncestor(this)).setVisible(true));
        send.addActionListener(e->new SendPayrollDialog(SwingUtilities.getWindowAncestor(this), accountantMode).setVisible(true));
        history.addActionListener(e->new PayrollHistoryDialog(SwingUtilities.getWindowAncestor(this), accountantMode).setVisible(true));
        detail.addActionListener(e->showDetail());
        t.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){if(e.getClickCount()==2)showDetail();}});
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
    private void showDetail(){int r=t.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Chọn một phiếu lương trước.");return;}int id=Integer.parseInt(t.getValueAt(r,0).toString());try{for(Payroll p:new PayrollService().hr(yearValue()))if(p.id()==id){new PayslipDetailDialog(SwingUtilities.getWindowAncestor(this),p).setVisible(true);return;}}catch(Exception e){Ui.error(this,e);}}
    private String fmt(double d){return String.format("%,.0f",d);}
=======
    private final JTable t = Ui.table(new String[] { "ID", "Mã NV", "Nhân viên", "Tháng", "Năm", "Lương CB", "OT",
            "Thưởng", "Phạt / trừ lương", "Thực lĩnh", "Trạng thái" });
    private final JTextField q = new JTextField(16);
    private final JComboBox<String> y;
    private final JComboBox<String> status = new JComboBox<>(new String[] { "Tất cả", "DRAFT", "SENT" });
    private final JButton create = Ui.primaryBtn("+ Tạo phiếu lương"), send = Ui.primaryBtn("Gửi phiếu lương"),
            history = Ui.btn("Lịch sử phiếu lương"), detail = Ui.btn("Xem chi tiết");

    public HRPayrollPanel() {
        this(false);
    }

    public HRPayrollPanel(boolean accountantMode) {
        y = new JComboBox<>(accountantMode ? new String[] { "2022", "2023", "2024", "2025", "2026" }
                : new String[] { "Tất cả 5 năm", "2022", "2023", "2024", "2025", "2026" });
        if (accountantMode)
            y.setSelectedItem("2026");
        Ui.init();
        setBackground(Ui.BG);
        setLayout(new BorderLayout(0, 10));

        JPanel b = Ui.toolbar(new JLabel("Tìm nhân viên"), q, new JLabel("Năm"), y,
                new JLabel("Trạng thái"), status);
        b.add(create);
        b.add(send);
        b.add(history);
        b.add(detail);
        JPanel pageNorth = new JPanel(new BorderLayout());
        pageNorth.setBackground(Ui.BG);
        pageNorth.add(Ui.top("Phiếu lương",
                accountantMode ? "Kế toán trưởng: tạo, kiểm tra và gửi phiếu"
                        : "HR: tạo, kiểm tra và gửi phiếu"),
                BorderLayout.NORTH);
        pageNorth.add(b, BorderLayout.SOUTH);
        add(pageNorth, BorderLayout.NORTH);
        add(Ui.scroll(t), BorderLayout.CENTER);
        Runnable load = this::load;
        q.addActionListener(e -> load.run());
        y.addActionListener(e -> load.run());
        status.addActionListener(e -> load.run());
        create.addActionListener(e -> new CreatePayrollDialog(SwingUtilities.getWindowAncestor(this)).setVisible(true));
        send.addActionListener(
                e -> new SendPayrollDialog(SwingUtilities.getWindowAncestor(this), accountantMode).setVisible(true));
        history.addActionListener(
                e -> new PayrollHistoryDialog(SwingUtilities.getWindowAncestor(this), accountantMode).setVisible(true));
        detail.addActionListener(e -> showDetail());
        t.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2)
                    showDetail();
            }
        });
        load.run();
    }

    private int yearValue() {
        String v = (String) y.getSelectedItem();
        return "Tất cả 5 năm".equals(v) ? 0 : Integer.parseInt(v);
    }

    private void load() {
        try {
            var rows = new ArrayList<String[]>();
            String k = q.getText().trim().toLowerCase();
            String s = (String) status.getSelectedItem();
            for (Payroll p : new PayrollService().hr(yearValue()))
                if ((k.isEmpty() || p.employeeCode().toLowerCase().contains(k)
                        || p.employeeName().toLowerCase().contains(k)) && ("Tất cả".equals(s) || p.status().equals(s)))
                    rows.add(new String[] { String.valueOf(p.id()), p.employeeCode(), p.employeeName(),
                            String.valueOf(p.month()), String.valueOf(p.year()), fmt(p.base()), fmt(p.overtime()),
                            fmt(p.bonus()), fmt(p.deduction()), fmt(p.net()), p.status() });
            Ui.fill(t, rows);
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private void showDetail() {
        int r = t.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Chọn một phiếu lương trước.");
            return;
        }
        int id = Integer.parseInt(t.getValueAt(r, 0).toString());
        try {
            for (Payroll p : new PayrollService().hr(yearValue()))
                if (p.id() == id) {
                    new PayslipDetailDialog(SwingUtilities.getWindowAncestor(this), p).setVisible(true);
                    return;
                }
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private String fmt(double d) {
        return String.format("%,.0f", d);
    }
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
}
