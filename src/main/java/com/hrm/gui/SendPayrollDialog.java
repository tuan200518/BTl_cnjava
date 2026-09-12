package com.hrm.gui;

import com.hrm.model.Payroll;
import com.hrm.service.PayrollService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/** Form riêng cho nghiệp vụ kiểm tra và gửi phiếu lương DRAFT. */
public class SendPayrollDialog extends JDialog {
    private final JTable table = Ui.table(new String[] { "ID", "Mã NV", "Nhân viên", "Tháng", "Năm", "Lương CB", "OT",
            "Thưởng", "Khấu trừ", "Thực lĩnh", "Trạng thái" });
    private final JTextField search = new JTextField(18);
    private final JComboBox<String> year;
    private final PayrollService service = new PayrollService();

    public SendPayrollDialog(Window owner) {
        this(owner, false);
    }

    public SendPayrollDialog(Window owner, boolean accountantMode) {
        super(owner, "Gửi phiếu lương", ModalityType.APPLICATION_MODAL);
        Ui.init();
        getContentPane().setBackground(Ui.BG);
        year = new JComboBox<>(accountantMode ? new String[] { "2022", "2023", "2024", "2025", "2026" }
                : new String[] { "Tất cả 5 năm", "2022", "2023", "2024", "2025", "2026" });
        if (accountantMode)
            year.setSelectedItem("2026");
        setSize(1120, 620);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filter.add(new JLabel("Tìm NV:"));
        filter.add(search);
        filter.add(new JLabel("Năm:"));
        filter.add(year);
        JButton find = Ui.btn("Tìm kiếm");
        JButton send = Ui.btn("Gửi phiếu đã chọn");
        JButton close = Ui.btn("Đóng");
        filter.add(find);
        filter.add(send);
        filter.add(close);
        filter.setBackground(Ui.SURFACE);
        JPanel pageNorth = new JPanel(new BorderLayout());
        pageNorth.setBackground(Ui.BG);
        pageNorth.add(Ui.top("Gửi phiếu lương", "Kiểm tra các phiếu DRAFT trước khi gửi cho nhân viên"),
                BorderLayout.NORTH);
        pageNorth.add(filter, BorderLayout.SOUTH);
        add(pageNorth, BorderLayout.NORTH);
        add(Ui.scroll(table), BorderLayout.CENTER);

        Runnable load = this::load;
        find.addActionListener(e -> load.run());
        search.addActionListener(e -> load.run());
        year.addActionListener(e -> load.run());
        send.addActionListener(e -> send());
        close.addActionListener(e -> dispose());
        load.run();
    }

    private int yearValue() {
        String v = (String) year.getSelectedItem();
        return "Tất cả 5 năm".equals(v) ? 0 : Integer.parseInt(v);
    }

    private void load() {
        try {
            var rows = new ArrayList<String[]>();
            String q = search.getText().trim().toLowerCase();
            for (Payroll p : service.hr(yearValue())) {
                if (!"DRAFT".equals(p.status()))
                    continue;
                if (!q.isEmpty() && !p.employeeCode().toLowerCase().contains(q)
                        && !p.employeeName().toLowerCase().contains(q))
                    continue;
                rows.add(new String[] { String.valueOf(p.id()), p.employeeCode(), p.employeeName(),
                        String.valueOf(p.month()), String.valueOf(p.year()), fmt(p.base()), fmt(p.overtime()),
                        fmt(p.bonus()), fmt(p.deduction()), fmt(p.net()), p.status() });
            }
            Ui.fill(table, rows);
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private void send() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn phiếu DRAFT cần gửi.");
            return;
        }
        String id = table.getValueAt(r, 0).toString(), code = table.getValueAt(r, 1).toString(),
                name = table.getValueAt(r, 2).toString();
        String period = table.getValueAt(r, 3) + "/" + table.getValueAt(r, 4);
        int ok = JOptionPane.showConfirmDialog(this, "Gửi phiếu lương kỳ " + period + " của " + code + " - " + name
                + " cho tài khoản nhân viên?\nSau khi gửi, nhân viên sẽ nhìn thấy phiếu trong 'Phiếu lương của tôi'.",
                "Xác nhận gửi phiếu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ok != JOptionPane.YES_OPTION)
            return;
        try {
            service.send(Integer.parseInt(id), Integer.parseInt(code.replace("NV", "")));
            load();
            JOptionPane.showMessageDialog(this, "Đã gửi phiếu lương cho " + code + ".");
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private String fmt(double d) {
        return String.format("%,.0f", d);
    }
}
