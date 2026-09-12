package com.hrm.gui;

import com.hrm.model.Payroll;
import com.hrm.service.PayrollService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyPayslipPanel extends JPanel {
    private final JTable t = Ui.table(new String[] { "Mã NV", "Nhân viên", "Tháng", "Năm", "Lương CB", "OT", "Thưởng",
            "Phạt / trừ lương", "Thực lĩnh", "Trạng thái" });
    private final JComboBox<String> y = new JComboBox<>(
            new String[] { "Tất cả 5 năm", "2022", "2023", "2024", "2025", "2026" });
    private final int emp;

    public MyPayslipPanel(Integer emp) {
        Ui.init();
        this.emp = emp;
        setLayout(new BorderLayout(0, 10));
        setBackground(Ui.BG);

        JButton detail = Ui.btn("Xem phiếu lương chi tiết");
        JPanel b = Ui.toolbar(new JLabel("Năm"), y, detail);
        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(Ui.BG);
        north.add(b, BorderLayout.CENTER);
        JPanel pageNorth = new JPanel(new BorderLayout());
        pageNorth.setBackground(Ui.BG);
        pageNorth.add(Ui.top("Phiếu lương của tôi", "Chỉ hiển thị các phiếu lương đã được HR gửi"), BorderLayout.NORTH);
        pageNorth.add(north, BorderLayout.SOUTH);
        add(pageNorth, BorderLayout.NORTH);
        add(Ui.scroll(t), BorderLayout.CENTER);
        y.addActionListener(e -> load());
        detail.addActionListener(e -> showDetail());
        t.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2)
                    showDetail();
            }
        });
        load();
    }

    private int yearValue() {
        String v = (String) y.getSelectedItem();
        return "Tất cả 5 năm".equals(v) ? 0 : Integer.parseInt(v);
    }

    private void load() {
        try {
            var rows = new ArrayList<String[]>();
            for (Payroll p : new PayrollService().mine(emp, yearValue()))
                rows.add(new String[] { p.employeeCode(), p.employeeName(), String.valueOf(p.month()),
                        String.valueOf(p.year()), fmt(p.base()), fmt(p.overtime()), fmt(p.bonus()), fmt(p.deduction()),
                        fmt(p.net()), p.status() });
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
        int month = Integer.parseInt(t.getValueAt(r, 2).toString()),
                year = Integer.parseInt(t.getValueAt(r, 3).toString());
        try {
            for (Payroll p : new PayrollService().mine(emp, year))
                if (p.month() == month && p.year() == year) {
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
}
