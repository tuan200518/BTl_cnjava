package com.hrm.gui;
<<<<<<< HEAD
import com.hrm.service.ReportService;
import javax.swing.*;import java.awt.*;import java.util.*;
public class StaffingPanel extends JPanel{
 JTable t=Ui.table(new String[]{"Phòng ban","Vị trí","Cần","Hiện có","Thiếu"});JTextField q=new JTextField(18);
 public StaffingPanel(){setLayout(new BorderLayout(8,8));add(Ui.top("Định biên nhân sự - phòng ban/vị trí"),BorderLayout.NORTH);JPanel b=new JPanel(new FlowLayout(FlowLayout.LEFT));b.add(new JLabel("Tìm phòng ban/vị trí:"));b.add(q);JButton members=Ui.btn("Xem nhân viên của vị trí"),search=Ui.btn("Tìm kiếm");b.add(search);b.add(members);add(b,BorderLayout.SOUTH);add(new JScrollPane(t),BorderLayout.CENTER);Runnable load=()->load();q.addActionListener(e->load.run());search.addActionListener(e->load.run());members.addActionListener(e->showMembers());load.run();}
 private void load(){try{var rows=new ArrayList<String[]>();String k=q.getText().trim().toLowerCase();for(String[] x:new ReportService().staffing())if(k.isEmpty()||x[0].toLowerCase().contains(k)||x[1].toLowerCase().contains(k))rows.add(x);Ui.fill(t,rows);}catch(Exception e){Ui.error(this,e);}}
 private void showMembers(){int r=t.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Chọn một vị trí trước.");return;}String job=t.getValueAt(r,1).toString();try{JTable m=Ui.table(new String[]{"Mã NV","Họ tên","Vị trí","Phòng ban","Vai trò","Trạng thái"});Ui.fill(m,new ReportService().employeesByJob(job));JTextField s=new JTextField(15);JButton b=Ui.btn("Tìm");JDialog d=new JDialog(SwingUtilities.getWindowAncestor(this),"Nhân viên - "+job,Dialog.ModalityType.APPLICATION_MODAL);d.setSize(900,500);d.setLocationRelativeTo(this);JPanel top=new JPanel();top.add(new JLabel("Tìm:"));top.add(s);top.add(b);d.add(top,BorderLayout.NORTH);d.add(new JScrollPane(m),BorderLayout.CENTER);Runnable load=()->{try{Ui.fill(m,new ReportService().employeesByJob(job,s.getText().trim()));}catch(Exception e){Ui.error(d,e);}};b.addActionListener(e->load.run());d.setVisible(true);}catch(Exception e){Ui.error(this,e);}}
=======

import com.hrm.service.ReportService;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class StaffingPanel extends JPanel {
    JTable t = Ui.table(new String[] { "Phòng ban", "Vị trí", "Cần", "Hiện có", "Thiếu" });
    JTextField q = new JTextField(18);

    public StaffingPanel() {
        Ui.init();
        setBackground(Ui.BG);
        setLayout(new BorderLayout(0, 10));

        JButton members = Ui.btn("Xem nhân viên của vị trí"), search = Ui.primaryBtn("Tìm kiếm");
        JPanel toolbar = Ui.toolbar(new JLabel("Tìm phòng ban / vị trí"), q, search, members);
        JPanel pageNorth = new JPanel(new BorderLayout());
        pageNorth.setBackground(Ui.BG);
        pageNorth.add(Ui.top("Định biên nhân sự", "Theo dõi nhu cầu, hiện có và số nhân sự còn thiếu"),
                BorderLayout.NORTH);
        pageNorth.add(toolbar, BorderLayout.SOUTH);
        add(pageNorth, BorderLayout.NORTH);
        add(Ui.scroll(t), BorderLayout.CENTER);
        Runnable load = () -> load();
        q.addActionListener(e -> load.run());
        search.addActionListener(e -> load.run());
        members.addActionListener(e -> showMembers());
        load.run();
    }

    private void load() {
        try {
            var rows = new ArrayList<String[]>();
            String k = q.getText().trim().toLowerCase();
            for (String[] x : new ReportService().staffing())
                if (k.isEmpty() || x[0].toLowerCase().contains(k) || x[1].toLowerCase().contains(k))
                    rows.add(x);
            Ui.fill(t, rows);
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private void showMembers() {
        int r = t.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Chọn một vị trí trước.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String job = t.getValueAt(r, 1).toString();
        try {
            JTable m = Ui.table(new String[] { "Mã NV", "Họ tên", "Vị trí", "Phòng ban", "Vai trò", "Trạng thái" });
            JTextField s = new JTextField(15);
            JButton b = Ui.primaryBtn("Tìm kiếm");

            JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this),
                    "Nhân viên - " + job, Dialog.ModalityType.APPLICATION_MODAL);
            d.setSize(960, 560);
            d.setLocationRelativeTo(this);
            d.setLayout(new BorderLayout());
            d.getContentPane().setBackground(Ui.BG);
            d.add(Ui.top("Nhân viên theo vị trí", job), BorderLayout.NORTH);
            d.add(Ui.toolbar(new JLabel("Tìm nhân viên"), s, b), BorderLayout.SOUTH);
            d.add(Ui.scroll(m), BorderLayout.CENTER);

            Runnable load = () -> {
                try {
                    Ui.fill(m, new ReportService().employeesByJob(job, s.getText().trim()));
                } catch (Exception e) {
                    Ui.error(d, e);
                }
            };
            b.addActionListener(e -> load.run());
            s.addActionListener(e -> load.run());
            load.run();
            d.setVisible(true);
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
}
