package com.hrm.gui;

import com.hrm.service.ReportService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Dashboard tổng quan.
 * Chỉ sử dụng ReportService hiện có; không thay đổi backend.
 */
public class DashboardPanel extends JPanel {

    private final StatCard employees = new StatCard("Tổng nhân viên", "NV", Ui.PRIMARY);
    private final StatCard departments = new StatCard("Phòng ban", "PB", new Color(0x0891B2));
    private final StatCard projects = new StatCard("Dự án", "DA", new Color(0xD97706));
    private final StatCard totalPaid = new StatCard("Tổng lương đã trả", "LU", Ui.SUCCESS);

    private final StaffingView staffing = new StaffingView();
    private final DepartmentView departmentsView = new DepartmentView();
    private final ProjectView projectsView = new ProjectView();
    private final JLabel status = Ui.muted("Đang tải dữ liệu...");

    public DashboardPanel() {
        Ui.init();
        setLayout(new BorderLayout());
        setBackground(Ui.BG);

        add(header(), BorderLayout.NORTH);
        add(body(), BorderLayout.CENTER);
        load();
    }

    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Ui.BG);
        p.setBorder(new EmptyBorder(22, 26, 12, 26));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Xin chào, HR Manager!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(Ui.TEXT);

        JLabel sub = new JLabel(
                "Tổng quan tình hình nhân sự của doanh nghiệp");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(Ui.MUTED);

        text.add(title);
        text.add(Box.createVerticalStrut(5));
        text.add(sub);

        JLabel date = new JLabel(
                LocalDate.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        date.setFont(new Font("Segoe UI", Font.BOLD, 13));
        date.setForeground(Ui.MUTED);

        p.add(text, BorderLayout.WEST);
        p.add(date, BorderLayout.EAST);
        return p;
    }

    private JScrollPane body() {
        DashboardContent content = new DashboardContent();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Ui.BG);
        content.setBorder(new EmptyBorder(4, 26, 26, 26));

        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setOpaque(false);
        cards.setPreferredSize(new Dimension(900, 120));
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        cards.add(employees);
        cards.add(departments);
        cards.add(projects);
        cards.add(totalPaid);

        content.add(cards);
        content.add(Box.createVerticalStrut(18));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 16, 0));
        row1.setOpaque(false);
        row1.setPreferredSize(new Dimension(900, 330));
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 330));
        row1.add(staffing);
        row1.add(departmentsView);

        content.add(row1);
        content.add(Box.createVerticalStrut(18));

        JPanel row2 = new JPanel(new GridLayout(1, 2, 16, 0));
        row2.setOpaque(false);
        row2.setPreferredSize(new Dimension(900, 300));
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        row2.add(projectsView);
        row2.add(summary());

        content.add(row2);
        content.add(Box.createVerticalStrut(10));

        status.setBorder(new EmptyBorder(2, 2, 0, 0));
        content.add(status);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.getViewport().setBackground(Ui.BG);
        scroll.setBackground(Ui.BG);
        return scroll;
    }

    private JPanel summary() {
        JPanel p = new JPanel(new BorderLayout(8, 12));
        p.setBackground(Ui.SURFACE);
        p.setBorder(new CompoundBorder(
                new LineBorder(Ui.BORDER),
                new EmptyBorder(18, 20, 18, 20)));

        p.add(Ui.heading("Tóm tắt hệ thống"), BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        list.add(info("NH", "Quản lý nhân viên",
                "Hồ sơ, chức danh và trạng thái"));
        list.add(Box.createVerticalStrut(14));
        list.add(info("CC", "Chấm công",
                "Theo dõi ngày công và giờ OT"));
        list.add(Box.createVerticalStrut(14));
        list.add(info("LU", "Phiếu lương",
                "Tạo, kiểm tra và gửi phiếu lương"));
        list.add(Box.createVerticalStrut(14));
        list.add(info("DA", "Phân công dự án",
                "Theo dõi nhân sự theo dự án"));

        p.add(list, BorderLayout.CENTER);
        return p;
    }

    private JPanel info(String code, String title, String desc) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);

        JLabel icon = new JLabel(code, SwingConstants.CENTER);
        icon.setOpaque(true);
        icon.setBackground(new Color(0xEEF0FF));
        icon.setForeground(Ui.PRIMARY);
        icon.setFont(new Font("Segoe UI", Font.BOLD, 12));
        icon.setPreferredSize(new Dimension(44, 38));

        JPanel t = new JPanel();
        t.setOpaque(false);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));

        JLabel a = new JLabel(title);
        a.setFont(new Font("Segoe UI", Font.BOLD, 13));
        a.setForeground(Ui.TEXT);

        JLabel b = new JLabel(desc);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setForeground(Ui.MUTED);

        t.add(a);
        t.add(Box.createVerticalStrut(3));
        t.add(b);

        p.add(icon, BorderLayout.WEST);
        p.add(t, BorderLayout.CENTER);
        return p;
    }

    private void load() {
        new SwingWorker<DashboardData, Void>() {
            @Override
            protected DashboardData doInBackground() throws Exception {
                ReportService r = new ReportService();
                DashboardData d = new DashboardData();
                d.employeeCount = r.count("employees");
                d.departmentCount = r.count("departments");
                d.projectCount = r.count("projects");
                d.totalPaid = r.totalPaid(0);
                d.staffing = r.staffing();
                d.projects = r.projects();
                return d;
            }

            @Override
            protected void done() {
                try {
                    DashboardData d = get();
                    employees.setValue(String.valueOf(d.employeeCount));
                    departments.setValue(String.valueOf(d.departmentCount));
                    projects.setValue(String.valueOf(d.projectCount));
                    totalPaid.setValue(money(d.totalPaid));
                    staffing.setData(d.staffing);
                    departmentsView.setData(d.staffing);
                    projectsView.setData(d.projects);
                    status.setText("Dữ liệu đã được cập nhật");
                } catch (Exception e) {
                    employees.setValue("--");
                    departments.setValue("--");
                    projects.setValue("--");
                    totalPaid.setValue("--");
                    status.setText("Không thể tải dữ liệu: " + e.getMessage());
                }
            }
        }.execute();
    }

    private String money(double value) {
        return new DecimalFormat("#,###").format(value) + " VNĐ";
    }

    private static class DashboardData {
        int employeeCount, departmentCount, projectCount;
        double totalPaid;
        List<String[]> staffing, projects;
    }

    private static class DashboardContent extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(1000, 650);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        @Override
        public Dimension getPreferredSize() {
            Container parent = getParent();
            int w = parent == null ? 1000 : Math.max(700, parent.getWidth());
            Dimension d = super.getPreferredSize();
            return new Dimension(w, d.height);
        }

        @Override
        public int getScrollableUnitIncrement(
                Rectangle r, int orientation, int direction) {
            return 18;
        }

        @Override
        public int getScrollableBlockIncrement(
                Rectangle r, int orientation, int direction) {
            return 120;
        }
    }

    private static class StatCard extends JPanel {
        private final JLabel value = new JLabel("...");

        StatCard(String title, String code, Color accent) {
            setBackground(Ui.SURFACE);
            setBorder(new CompoundBorder(
                    new LineBorder(Ui.BORDER),
                    new EmptyBorder(15, 16, 15, 16)));
            setLayout(new BorderLayout(12, 0));

            JLabel icon = new JLabel(code, SwingConstants.CENTER);
            icon.setOpaque(true);
            icon.setBackground(new Color(
                    accent.getRed(), accent.getGreen(),
                    accent.getBlue(), 25));
            icon.setForeground(accent);
            icon.setFont(new Font("Segoe UI", Font.BOLD, 12));
            icon.setPreferredSize(new Dimension(50, 50));

            JPanel text = new JPanel();
            text.setOpaque(false);
            text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

            JLabel t = new JLabel(title);
            t.setForeground(Ui.MUTED);
            t.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            value.setForeground(Ui.TEXT);
            value.setFont(new Font("Segoe UI", Font.BOLD, 23));

            text.add(t);
            text.add(Box.createVerticalStrut(5));
            text.add(value);

            add(icon, BorderLayout.WEST);
            add(text, BorderLayout.CENTER);
        }

        void setValue(String s) {
            value.setText(s);
        }
    }

    private static abstract class BaseView extends JPanel {
        BaseView() {
            setBackground(Ui.SURFACE);
            setBorder(new CompoundBorder(
                    new LineBorder(Ui.BORDER),
                    new EmptyBorder(17, 20, 17, 20)));
        }

        protected void title(Graphics2D g, String s) {
            g.setColor(Ui.TEXT);
            g.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g.drawString(s, 20, 28);
        }
    }

    private static class StaffingView extends BaseView {
        private List<String[]> data;

        void setData(List<String[]> d) {
            data = d;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D x = (Graphics2D) g.create();
            x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            title(x, "Tình hình định biên");

            if (data == null) {
                x.setColor(Ui.MUTED);
                x.drawString("Đang tải dữ liệu...", 20, 70);
                x.dispose();
                return;
            }

            int y = 62;
            for (int i = 0; i < Math.min(7, data.size()); i++) {
                String[] r = data.get(i);
                int target = parse(r[2]);
                int actual = parse(r[3]);
                int missing = parse(r[4]);

                x.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                x.setColor(Ui.TEXT);
                x.drawString(r[1], 20, y);
                x.drawString(actual + "/" + target, 170, y);

                int bx = 225, bw = 180;
                x.setColor(new Color(0xE7E9EF));
                x.fillRoundRect(bx, y - 11, bw, 9, 9, 9);

                double ratio = target <= 0 ? 0 : Math.min(1d, (double) actual / target);
                x.setColor(missing > 0 ? Ui.WARNING : Ui.SUCCESS);
                x.fillRoundRect(bx, y - 11,
                        (int) (bw * ratio), 9, 9, 9);

                x.setColor(Ui.MUTED);
                x.drawString(missing > 0 ? "Thiếu " + missing : "Đủ",
                        420, y);
                y += 31;
            }
            x.dispose();
        }
    }

    private static class DepartmentView extends BaseView {
        private List<String[]> data;

        void setData(List<String[]> d) {
            data = d;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D x = (Graphics2D) g.create();
            x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            title(x, "Nhân sự theo phòng ban");

            if (data == null) {
                x.setColor(Ui.MUTED);
                x.drawString("Đang tải dữ liệu...", 20, 70);
                x.dispose();
                return;
            }

            Map<String, Integer> map = new LinkedHashMap<>();
            for (String[] r : data) {
                String dept = r[0];
                int n = parse(r[3]);
                map.put(dept, map.getOrDefault(dept, 0) + n);
            }

            int max = 1;
            for (Integer n : map.values())
                max = Math.max(max, n);

            int y = 62, i = 0;
            for (Map.Entry<String, Integer> e : map.entrySet()) {
                if (i++ >= 7)
                    break;
                x.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                x.setColor(Ui.TEXT);
                x.drawString(e.getKey(), 20, y);

                int bx = 145, bw = 205;
                x.setColor(new Color(0xE7E9EF));
                x.fillRoundRect(bx, y - 11, bw, 9, 9, 9);

                int fill = (int) ((double) e.getValue() / max * bw);
                x.setColor(Ui.PRIMARY);
                x.fillRoundRect(bx, y - 11, fill, 9, 9, 9);

                x.setColor(Ui.TEXT);
                x.drawString(String.valueOf(e.getValue()),
                        bx + bw + 12, y);
                y += 31;
            }
            x.dispose();
        }
    }

    private static class ProjectView extends BaseView {
        private List<String[]> data;

        void setData(List<String[]> d) {
            data = d;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D x = (Graphics2D) g.create();
            x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            title(x, "Phân bổ nhân sự dự án");

            if (data == null) {
                x.setColor(Ui.MUTED);
                x.drawString("Đang tải dữ liệu...", 20, 70);
                x.dispose();
                return;
            }

            int y = 62;
            for (int i = 0; i < Math.min(6, data.size()); i++) {
                String[] r = data.get(i);
                int need = parse(r[1]);
                int have = parse(r[2]);
                int missing = parse(r[3]);

                x.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                x.setColor(Ui.TEXT);
                x.drawString(r[0], 20, y);
                x.drawString(have + "/" + need, 205, y);

                x.setColor(missing > 0 ? Ui.WARNING : Ui.SUCCESS);
                x.drawString(missing > 0
                        ? "Thiếu " + missing
                        : "Đủ nhân sự", 275, y);
                y += 32;
            }
            x.dispose();
        }
    }

    private static int parse(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
