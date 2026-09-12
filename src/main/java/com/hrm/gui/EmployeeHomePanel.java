package com.hrm.gui;

import com.hrm.config.DBConnection;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class EmployeeHomePanel extends JPanel {
    private final int emp;
    private final JLabel info = new JLabel();
    private final JTable jobs = Ui.table(new String[] { "Vị trí công việc", "Phòng ban", "Lương CB" });
    private final JTable projects = Ui.table(new String[] { "Dự án", "Vai trò", "Ngày tham gia", "Trạng thái" });
    private final JComboBox<String> year = new JComboBox<>(
            new String[] { "Tất cả 5 năm", "2022", "2023", "2024", "2025", "2026" });

    public EmployeeHomePanel(int e) {
        emp = e;
        Ui.init();
        setBackground(Ui.BG);
        setLayout(new BorderLayout(0, 10));
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Ui.BG);
        header.add(Ui.top("Tổng quan cá nhân", "Thông tin hồ sơ, vị trí công việc và dự án đang tham gia"),
                BorderLayout.CENTER);
        year.setPreferredSize(new Dimension(130, 36));
        JPanel yr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        yr.setOpaque(false);
        yr.add(year);
        header.add(yr, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel profile = new JPanel(new BorderLayout());
        profile.setBackground(Ui.SURFACE);
        profile.setBorder(new CompoundBorder(new LineBorder(Ui.BORDER), new EmptyBorder(14, 18, 14, 18)));
        profile.add(info, BorderLayout.CENTER);

        JPanel jobsBox = new JPanel(new BorderLayout());
        jobsBox.setBackground(Ui.SURFACE);
        jobsBox.setBorder(new CompoundBorder(new LineBorder(Ui.BORDER), new EmptyBorder(12, 14, 12, 14)));
        jobsBox.add(Ui.heading("Vị trí hiện tại"), BorderLayout.NORTH);
        jobsBox.add(Ui.scroll(jobs), BorderLayout.CENTER);

        JPanel projectBox = new JPanel(new BorderLayout());
        projectBox.setBackground(Ui.SURFACE);
        projectBox.setBorder(new CompoundBorder(new LineBorder(Ui.BORDER), new EmptyBorder(12, 14, 12, 14)));
        projectBox.add(Ui.heading("Dự án tham gia"), BorderLayout.NORTH);
        projectBox.add(Ui.scroll(projects), BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(Ui.BG);
        center.setBorder(new EmptyBorder(0, 20, 20, 20));
        center.add(profile, BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jobsBox, projectBox);
        split.setDividerLocation(190);
        split.setBorder(null);
        split.setOpaque(false);
        center.add(split, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        year.addActionListener(x -> loadProjects());
        load();
    }

    private void load() {
        try (Connection c = DBConnection.getConnection()) {
            try (PreparedStatement p = c.prepareStatement(
                    "SELECT e.full_name,e.email,d.department_name,j.job_title,e.base_salary,e.hire_date,e.resignation_date,e.status FROM employees e JOIN departments d ON d.department_id=e.department_id JOIN jobs j ON j.job_id=e.job_id WHERE e.employee_id=?")) {
                p.setInt(1, emp);
                try (ResultSet r = p.executeQuery()) {
                    if (r.next()) {
                        info.setText("<html><b>NV" + String.format("%04d", emp) + " - " + r.getString(1) + "</b> | "
                                + r.getString(2) + " | " + r.getString(3) + " | Vào: " + r.getDate(6)
                                + " | Trạng thái: " + r.getString(8) + "</html>");
                    }
                }
            }
            try (PreparedStatement p = c.prepareStatement(
                    "SELECT j.job_title,d.department_name,e.base_salary FROM employees e JOIN jobs j ON j.job_id=e.job_id JOIN departments d ON d.department_id=e.department_id WHERE e.employee_id=?")) {
                p.setInt(1, emp);
                try (ResultSet r = p.executeQuery()) {
                    var a = new ArrayList<String[]>();
                    if (r.next())
                        a.add(new String[] { r.getString(1), r.getString(2), String.format("%,.0f", r.getDouble(3)) });
                    Ui.fill(jobs, a);
                }
            }
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
        loadProjects();
    }

    private void loadProjects() {
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement(
                        "SELECT p.project_name,COALESCE(pa.role_in_project,'-'),pa.assigned_date,p.status FROM project_allocations pa JOIN projects p ON p.project_id=pa.project_id WHERE pa.employee_id=? ORDER BY pa.assigned_date DESC")) {
            p.setInt(1, emp);
            try (ResultSet r = p.executeQuery()) {
                var a = new ArrayList<String[]>();
                while (r.next())
                    a.add(new String[] { r.getString(1), r.getString(2), String.valueOf(r.getDate(3)),
                            r.getString(4) });
                Ui.fill(projects, a);
            }
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }
}
