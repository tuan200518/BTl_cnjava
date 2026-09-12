package com.hrm.gui;

import com.hrm.service.AssignmentService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

public class AssignmentPanel extends JPanel {
    private final JTable t = Ui.table(new String[] { "Mã NV", "Nhân viên", "Phòng ban", "Vị trí công việc", "Dự án",
            "Vai trò trong dự án", "Trạng thái" });
    private final JTextField q = new JTextField(18);
    private final JComboBox<String> department = new JComboBox<>(), job = new JComboBox<>(),
            project = new JComboBox<>(), status = new JComboBox<>(new String[] { "Tất cả", "ACTIVE", "RESIGNED" });
    private final JButton assignJob = Ui.primaryBtn("+ Phân công công việc"),
            assignProject = Ui.primaryBtn("+ Phân công dự án"), removeProject = Ui.dangerBtn("Bỏ khỏi dự án"),
            refresh = Ui.btn("Làm mới");
    private final AssignmentService service = new AssignmentService();

    public AssignmentPanel() {
        Ui.init();
        setBackground(Ui.BG);
        setLayout(new BorderLayout(0, 10));
        JPanel filters = Ui.toolbar(new JLabel("Tìm"), q,
                new JLabel("Phòng ban"), department,
                new JLabel("Vị trí"), job,
                new JLabel("Dự án"), project,
                new JLabel("Trạng thái"), status);

        JPanel actions = Ui.toolbar(assignJob, assignProject, removeProject, refresh);
        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(Ui.BG);
        north.add(filters, BorderLayout.NORTH);
        north.add(actions, BorderLayout.SOUTH);
        JPanel pageNorth = new JPanel(new BorderLayout());
        pageNorth.setBackground(Ui.BG);
        pageNorth.add(Ui.top("Phân công nhân viên", "Phân công vị trí công việc và thành viên cho dự án"),
                BorderLayout.NORTH);
        pageNorth.add(north, BorderLayout.SOUTH);
        add(pageNorth, BorderLayout.NORTH);
        add(Ui.scroll(t), BorderLayout.CENTER);

        loadFilters();
        Runnable load = this::load;
        q.addActionListener(e -> load.run());
        department.addActionListener(e -> load.run());
        job.addActionListener(e -> load.run());
        project.addActionListener(e -> load.run());
        status.addActionListener(e -> load.run());
        refresh.addActionListener(e -> load.run());
        assignJob.addActionListener(e -> assignJob());
        assignProject.addActionListener(e -> assignProject());
        removeProject.addActionListener(e -> removeProject());
        load.run();
    }

    private void loadFilters() {
        try {
            department.addItem("Tất cả");
            for (String[] x : service.departments())
                department.addItem(x[1]);
            job.addItem("Tất cả");
            for (String[] x : service.jobs())
                job.addItem(x[1]);
            project.addItem("Tất cả");
            for (String[] x : service.projects())
                project.addItem(x[1]);
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private void load() {
        try {
            Ui.fill(t,
                    service.employees(q.getText().trim(), (String) department.getSelectedItem(),
                            (String) job.getSelectedItem(), (String) project.getSelectedItem(),
                            (String) status.getSelectedItem()));
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private int selectedId() {
        int r = t.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên trước.");
            return -1;
        }
        return Integer.parseInt(t.getValueAt(r, 0).toString().replace("NV", ""));
    }

    private void assignJob() {
        int id = selectedId();
        if (id < 0)
            return;
        try {
            java.util.List<String[]> jobs = service.jobs();
            JComboBox<String> cb = new JComboBox<>();
            for (String[] x : jobs)
                cb.addItem(x[0] + " | " + x[1] + " | " + x[2]);

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(Ui.SURFACE);
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(8, 0, 8, 12);
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            form.add(new JLabel("Vị trí mới"), c);
            c.gridx = 1;
            c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            form.add(cb, c);

            if (showAssignmentDialog("Phân công công việc",
                    "Cập nhật vị trí và phòng ban cho nhân viên NV" +
                            String.format("%04d", id),
                    form)) {
                int jobId = Integer.parseInt(cb.getSelectedItem().toString().split(" \\|")[0].trim());
                service.assignJob(id, jobId);
                load();
                JOptionPane.showMessageDialog(this,
                        "Đã cập nhật vị trí công việc và phòng ban.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private void assignProject() {
        int id = selectedId();
        if (id < 0)
            return;
        try {
            java.util.List<String[]> ps = service.projects();
            JComboBox<String> cb = new JComboBox<>();
            for (String[] x : ps)
                cb.addItem(x[0] + " | " + x[1] + " | " + x[2]);
            JTextField role = new JTextField("Thành viên dự án");

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(Ui.SURFACE);
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(8, 0, 8, 12);
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            form.add(new JLabel("Dự án"), c);
            c.gridx = 1;
            c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            form.add(cb, c);
            c.gridx = 0;
            c.gridy = 1;
            c.weightx = 0;
            c.fill = GridBagConstraints.NONE;
            form.add(new JLabel("Vai trò"), c);
            c.gridx = 1;
            c.weightx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            form.add(role, c);

            if (showAssignmentDialog("Phân công dự án",
                    "Thêm nhân viên vào dự án", form)) {
                int projectId = Integer.parseInt(cb.getSelectedItem().toString().split(" \\|")[0].trim());
                service.assignProject(id, projectId, role.getText().trim());
                load();
                JOptionPane.showMessageDialog(this,
                        "Đã phân công nhân viên vào dự án.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private void removeProject() {
        int id = selectedId();
        if (id < 0)
            return;
        try {
            java.util.List<String[]> ps = service.projectAssignments(id);
            if (ps.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nhân viên này chưa được phân công dự án.",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JComboBox<String> cb = new JComboBox<>();
            for (String[] x : ps)
                cb.addItem(x[0] + " | " + x[1] + " | " + x[2]);

            JPanel form = new JPanel(new BorderLayout(10, 8));
            form.setBackground(Ui.SURFACE);
            form.add(new JLabel("Chọn dự án cần bỏ:"), BorderLayout.NORTH);
            form.add(cb, BorderLayout.CENTER);

            if (showAssignmentDialog("Bỏ khỏi dự án",
                    "Xóa phân công dự án của nhân viên", form)) {
                int projectId = Integer.parseInt(cb.getSelectedItem().toString().split(" \\|")[0].trim());
                service.removeProject(id, projectId);
                load();
                JOptionPane.showMessageDialog(this,
                        "Đã bỏ phân công dự án.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private boolean showAssignmentDialog(String title, String subtitle, JComponent form) {
        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this),
                title, Dialog.ModalityType.APPLICATION_MODAL);
        d.setSize(560, 300);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(Ui.BG);
        d.add(Ui.top(title, subtitle), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Ui.SURFACE);
        center.setBorder(new EmptyBorder(15, 28, 15, 28));
        center.add(form, BorderLayout.CENTER);
        d.add(center, BorderLayout.CENTER);

        final boolean[] ok = { false };
        JButton cancel = Ui.btn("Hủy");
        JButton save = Ui.primaryBtn("Xác nhận");
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(Ui.BG);
        footer.add(cancel);
        footer.add(save);
        d.add(footer, BorderLayout.SOUTH);

        cancel.addActionListener(e -> d.dispose());
        save.addActionListener(e -> {
            ok[0] = true;
            d.dispose();
        });
        d.getRootPane().setDefaultButton(save);
        d.setVisible(true);
        return ok[0];
    }
}
