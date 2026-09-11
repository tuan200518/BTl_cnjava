package com.hrm.gui;

import com.hrm.model.Employee;
import com.hrm.service.EmployeeService;
import com.hrm.service.PayrollService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Form riêng cho nghiệp vụ tạo phiếu lương. */
public class CreatePayrollDialog extends JDialog {
    private final JTable table = Ui.table(new String[]{"Mã NV","Họ tên","Phòng ban","Vị trí","Lương CB","Trạng thái"});
    private final JTextField search = new JTextField(18);
    private final JComboBox<Integer> year = new JComboBox<>(new Integer[]{2022,2023,2024,2025,2026});
    private final JComboBox<Integer> month = new JComboBox<>();
    private final JComboBox<String> department = new JComboBox<>();
    private final JComboBox<String> job = new JComboBox<>();
    private final EmployeeService employeeService = new EmployeeService();
    private final PayrollService payrollService = new PayrollService();

    public CreatePayrollDialog(Window owner) {
        super(owner, "Tạo phiếu lương", ModalityType.APPLICATION_MODAL);
        setSize(1050, 620);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8,8));

        add(Ui.top("Tạo phiếu lương - Chọn nhân viên và kỳ lương"), BorderLayout.NORTH);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filters.add(new JLabel("Tìm NV:")); filters.add(search);
        filters.add(new JLabel("Năm:")); filters.add(year);
        filters.add(new JLabel("Tháng:"));
        for(int i=1;i<=12;i++) month.addItem(i);
        month.setSelectedItem(9);
        filters.add(month);
        filters.add(new JLabel("Phòng ban:")); filters.add(department);
        filters.add(new JLabel("Vị trí:")); filters.add(job);
        JButton find = Ui.btn("Tìm kiếm"); filters.add(find);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton create = Ui.btn("Tạo phiếu lương");
        JButton close = Ui.btn("Đóng");
        actions.add(create); actions.add(close);

        JPanel south = new JPanel(new BorderLayout());
        south.add(filters, BorderLayout.NORTH); south.add(actions, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadFilters();
        Runnable load = this::load;
        find.addActionListener(e -> load.run());
        search.addActionListener(e -> load.run());
        year.addActionListener(e -> load.run());
        department.addActionListener(e -> load.run());
        job.addActionListener(e -> load.run());
        create.addActionListener(e -> create());
        close.addActionListener(e -> dispose());
        load.run();
    }

    private void loadFilters() {
        try {
            department.addItem("Tất cả");
            for(String[] x: employeeService.departments()) department.addItem(x[1]);
            job.addItem("Tất cả");
            for(String[] x: employeeService.jobs()) job.addItem(x[1]);
        } catch(Exception e) { Ui.error(this,e); }
    }

    private void load() {
        try {
            int y = (Integer)year.getSelectedItem();
            String d = (String)department.getSelectedItem();
            String j = (String)job.getSelectedItem();
            String q = search.getText().trim();
            List<Employee> employees = employeeService.find(q, y, d, j, "ACTIVE");
            var rows = new ArrayList<String[]>();
            for(Employee e: employees) {
                rows.add(new String[]{e.code(),e.fullName(),e.department(),e.job(),String.format("%,.0f",e.salary()),e.status()});
            }
            Ui.fill(table, rows);
        } catch(Exception e) { Ui.error(this,e); }
    }

    private void create() {
        int row = table.getSelectedRow();
        if(row < 0) { JOptionPane.showMessageDialog(this,"Hãy chọn nhân viên cần tạo phiếu lương."); return; }
        int employeeId = Integer.parseInt(table.getValueAt(row,0).toString().replace("NV",""));
        int m = (Integer)month.getSelectedItem();
        int y = (Integer)year.getSelectedItem();
        String name = table.getValueAt(row,1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tạo phiếu lương DRAFT cho " + table.getValueAt(row,0) + " - " + name + "\nKỳ lương: " + m + "/" + y + "?",
                "Xác nhận tạo phiếu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(confirm != JOptionPane.YES_OPTION) return;
        try {
            payrollService.create(employeeId,m,y);
            JOptionPane.showMessageDialog(this,"Đã tạo phiếu lương DRAFT cho " + table.getValueAt(row,0) + ".\nBạn có thể sang chức năng 'Gửi phiếu lương' để kiểm tra và gửi cho nhân viên.");
            load();
        } catch(Exception e) { Ui.error(this,e); }
    }
}
