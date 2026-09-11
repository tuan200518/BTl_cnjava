package com.hrm.gui;

import com.hrm.service.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;

public class HRHistoryPanel extends JPanel {
        private final JTable t = Ui.table(new String[] { "Mã NV", "Họ tên", "Sự kiện", "Ngày", "Mô tả" });
        private final JComboBox<String> y = new JComboBox<>(
                        new String[] { "Tất cả 5 năm", "2022", "2023", "2024", "2025", "2026" });
        private final JTextField q = new JTextField(18);
        private final JButton penalty = Ui.btn("Ghi phạt / trừ lương");

        public HRHistoryPanel() {
                this(true);
        }

        public HRHistoryPanel(boolean editable) {
                Ui.init();
                setBackground(Ui.BG);
                setLayout(new BorderLayout(0, 10));

                JPanel b = Ui.toolbar(new JLabel("Tìm nhân viên"), q, new JLabel("Năm"), y);
                if (editable)
                        b.add(penalty);
                JPanel pageNorth = new JPanel(new BorderLayout());
                pageNorth.setBackground(Ui.BG);
                pageNorth.add(Ui.top("Sự kiện nhân sự",
                                editable ? "Thưởng, phạt, nghỉ, thăng chức và các biến động nhân sự"
                                                : "Chế độ xem dành cho Kế toán trưởng"),
                                BorderLayout.NORTH);
                pageNorth.add(b, BorderLayout.SOUTH);
                add(pageNorth, BorderLayout.NORTH);
                add(Ui.scroll(t), BorderLayout.CENTER);
                Runnable load = () -> {
                        try {
                                Ui.fill(t, new HRHistoryService().find(yearValue(), q.getText().trim()));
                        } catch (Exception e) {
                                Ui.error(this, e);
                        }
                };
                y.addActionListener(e -> load.run());
                q.addActionListener(e -> load.run());
                if (editable)
                        penalty.addActionListener(e -> showPenalty());
                load.run();
        }

        private int yearValue() {
                String v = (String) y.getSelectedItem();
                return "Tất cả 5 năm".equals(v) ? 0 : Integer.parseInt(v);
        }

        private void showPenalty() {
                JTextField code = new JTextField();
                JTextField amount = new JTextField("500000");
                JTextField reason = new JTextField("Nghỉ quá số ngày quy định");
                JTextField date = new JTextField(LocalDate.now().toString());

                JPanel form = new JPanel(new GridBagLayout());
                form.setBackground(Ui.SURFACE);
                GridBagConstraints c = new GridBagConstraints();
                c.insets = new Insets(7, 0, 7, 12);
                addPenaltyField(form, c, 0, "Mã NV *", code);
                addPenaltyField(form, c, 1, "Số tiền trừ *", amount);
                addPenaltyField(form, c, 2, "Lý do", reason);
                addPenaltyField(form, c, 3, "Ngày", date);

                JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this),
                                "Ghi phạt / trừ lương", Dialog.ModalityType.APPLICATION_MODAL);
                d.setSize(600, 390);
                d.setLocationRelativeTo(this);
                d.setLayout(new BorderLayout());
                d.getContentPane().setBackground(Ui.BG);
                d.add(Ui.top("Ghi phạt / trừ lương",
                                "Khoản phạt sẽ được áp dụng khi tạo phiếu lương"), BorderLayout.NORTH);

                JPanel center = new JPanel(new BorderLayout());
                center.setBackground(Ui.SURFACE);
                center.setBorder(new EmptyBorder(10, 28, 10, 28));
                center.add(form, BorderLayout.NORTH);
                d.add(center, BorderLayout.CENTER);

                JButton cancel = Ui.btn("Hủy"), save = Ui.primaryBtn("Lưu khoản phạt");
                JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
                footer.setBackground(Ui.BG);
                footer.add(cancel);
                footer.add(save);
                d.add(footer, BorderLayout.SOUTH);

                cancel.addActionListener(e -> d.dispose());
                save.addActionListener(e -> {
                        try {
                                int id = Integer.parseInt(code.getText().trim().toUpperCase().replace("NV", ""));
                                double a = Double.parseDouble(amount.getText().trim());
                                String rs = reason.getText().trim();
                                LocalDate ld = LocalDate.parse(date.getText().trim());
                                if (a < 0)
                                        throw new IllegalArgumentException("Số tiền trừ không hợp lệ.");
                                new DisciplineService().add(id, a, rs, ld);
                                d.dispose();
                                JOptionPane.showMessageDialog(this,
                                                "Đã ghi khoản phạt. Khi tạo phiếu lương, khoản này sẽ được cộng vào Khấu trừ.",
                                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                                Ui.error(d, ex);
                        }
                });
                d.getRootPane().setDefaultButton(save);
                d.setVisible(true);
        }

        private void addPenaltyField(JPanel p, GridBagConstraints c, int row, String label, Component field) {
                c.gridy = row;
                c.gridx = 0;
                c.weightx = 0;
                c.fill = GridBagConstraints.NONE;
                JLabel l = new JLabel(label);
                l.setPreferredSize(new Dimension(125, 34));
                p.add(l, c);
                c.gridx = 1;
                c.weightx = 1;
                c.fill = GridBagConstraints.HORIZONTAL;
                field.setPreferredSize(new Dimension(350, 36));
                p.add(field, c);
        }
}
