package com.hrm.gui;

import com.hrm.service.AttendanceService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AttendancePanel extends JPanel {
    private final JTable t = Ui.table(new String[] { "Mã NV", "Nhân viên", "Số bản ghi", "Ngày công", "Giờ OT" });
    private final JComboBox<String> y = new JComboBox<>(
            new String[] { "Tất cả 5 năm", "2022", "2023", "2024", "2025", "2026" });
    private final JTextField q = new JTextField(18);

    public AttendancePanel() {
        Ui.init();
        setLayout(new BorderLayout(0, 10));
        setBackground(Ui.BG);
        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(Ui.BG);
        north.add(Ui.top("Chấm công & overtime", "Theo dõi ngày công và giờ làm thêm theo nhân viên"),
                BorderLayout.NORTH);
        north.add(Ui.toolbar(new JLabel("Tìm nhân viên"), q, new JLabel("Năm"), y), BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);
        add(Ui.scroll(t), BorderLayout.CENTER);

        Runnable load = () -> {
            try {
                Ui.fill(t, new AttendanceService().find(yearValue(), q.getText().trim()));
            } catch (Exception e) {
                Ui.error(this, e);
            }
        };
        y.addActionListener(e -> load.run());
        q.addActionListener(e -> load.run());
        load.run();
    }

    private int yearValue() {
        String v = (String) y.getSelectedItem();
        return "Tất cả 5 năm".equals(v) ? 0 : Integer.parseInt(v);
    }
}
