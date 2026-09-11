package com.hrm.gui;

import com.hrm.model.Employee;
import com.hrm.service.EmployeeService;
import com.hrm.service.PayrollService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Form riêng cho nghiệp vụ tạo phiếu lương, luôn tải lại nhân viên theo đúng
 * tháng/năm.
 */
public class CreatePayrollDialog extends JDialog {
    private final JTable table = Ui
            .table(new String[] { "Mã NV", "Họ tên", "Phòng ban", "Vị trí", "Lương CB", "Trạng thái" });
    private final JTextField search = new JTextField(18);
    private final JComboBox<Integer> year = new JComboBox<>(new Integer[] { 2022, 2023, 2024, 2025, 2026 });
    private final JComboBox<Integer> month = new JComboBox<>();
    private final JComboBox<String> department = new JComboBox<>();
    private final JComboBox<String> job = new JComboBox<>();
    private final EmployeeService employeeService = new EmployeeService();
    private final PayrollService payrollService = new PayrollService();

    public CreatePayrollDialog(Window owner) {
        super(owner, "Tạo phiếu lương", ModalityType.APPLICATION_MODAL);
        Ui.init();
        setSize(1080, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(0, 10));
        getContentPane().setBackground(Ui.BG);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filters.add(new JLabel("Tìm NV:"));
        filters.add(search);
        filters.add(new JLabel("Năm:"));
        filters.add(year);
        filters.add(new JLabel("Tháng:"));
        for (int i = 1; i <= 12; i++)
            month.addItem(i);
        month.setSelectedItem(9);
        filters.add(month);
        filters.add(new JLabel("Phòng ban:"));
        filters.add(department);
        filters.add(new JLabel("Vị trí:"));
        filters.add(job);
        JButton find = Ui.btn("Tìm kiếm"), refresh = Ui.btn("Làm mới danh sách");
        filters.add(find);
        filters.add(refresh);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton create = Ui.btn("Tạo phiếu lương"), close = Ui.btn("Đóng");
        actions.add(create);
        actions.add(close);
        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(Ui.BG);
        north.add(filters, BorderLayout.CENTER);
        JPanel pageNorth = new JPanel(new BorderLayout());
        pageNorth.setBackground(Ui.BG);
        pageNorth.add(Ui.top("Tạo phiếu lương", "Tìm nhân viên theo đúng kỳ lương trước khi tạo phiếu"),
                BorderLayout.NORTH);
        pageNorth.add(north, BorderLayout.SOUTH);
        add(pageNorth, BorderLayout.NORTH);
        add(Ui.scroll(table), BorderLayout.CENTER);
        actions.setBackground(Ui.BG);
        add(actions, BorderLayout.SOUTH);
        loadFilters();
        Runnable load = this::load;
        find.addActionListener(e -> load.run());
        refresh.addActionListener(e -> load.run());
        search.addActionListener(e -> load.run());
        year.addActionListener(e -> load.run());
        month.addActionListener(e -> load.run());
        department.addActionListener(e -> load.run());
        job.addActionListener(e -> load.run());
        create.addActionListener(e -> create());
        close.addActionListener(e -> dispose());
        load.run();
    }

    private void loadFilters() {
        try {
            department.addItem("Tất cả");
            for (String[] x : employeeService.departments())
                department.addItem(x[1]);
            job.addItem("Tất cả");
            for (String[] x : employeeService.jobs())
                job.addItem(x[1]);
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private void load() {
        try {
            int y = (Integer) year.getSelectedItem(), m = (Integer) month.getSelectedItem();
            String d = (String) department.getSelectedItem(), j = (String) job.getSelectedItem(),
                    q = search.getText().trim();
            List<Employee> employees = payrollService.employeesForPayroll(q, y, m, d, j);
            var rows = new ArrayList<String[]>();
            for (Employee e : employees)
                rows.add(new String[] { e.code(), e.fullName(), e.department(), e.job(),
                        String.format("%,.0f", e.salary()), e.status() });
            Ui.fill(table, rows);
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private void create() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần tạo phiếu lương.");
            return;
        }
        int employeeId = Integer.parseInt(table.getValueAt(row, 0).toString().replace("NV", ""));
        int m = (Integer) month.getSelectedItem(), y = (Integer) year.getSelectedItem();
        String name = table.getValueAt(row, 1).toString();
        int confirm = JOptionPane
                .showConfirmDialog(this,
                        "Tạo phiếu lương DRAFT cho " + table.getValueAt(row, 0) + " - " + name + "\nKỳ lương: " + m
                                + "/" + y + "?",
                        "Xác nhận tạo phiếu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION)
            return;
        try {
            payrollService.create(employeeId, m, y);
            JOptionPane.showMessageDialog(this, "Đã tạo/cập nhật phiếu DRAFT cho " + table.getValueAt(row, 0) + ".");
            load();
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }
}
