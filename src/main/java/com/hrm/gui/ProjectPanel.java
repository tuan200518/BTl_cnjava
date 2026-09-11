package com.hrm.gui;

import com.hrm.service.ProjectService;
import com.hrm.service.ReportService;
import javax.swing.*;
<<<<<<< HEAD
=======
import javax.swing.border.*;
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
import java.awt.*;
import java.time.LocalDate;
import java.util.*;

public class ProjectPanel extends JPanel {
<<<<<<< HEAD
    private final JTable t=Ui.table(new String[]{"Dự án","Cần","Đã có","Còn thiếu","Đáp ứng","Ngày bắt đầu","Trạng thái"});
    private final JTextField q=new JTextField(18);
    private final ReportService report=new ReportService();
    private final ProjectService projects=new ProjectService();
    public ProjectPanel(){
        setLayout(new BorderLayout(8,8)); add(Ui.top("Quản lý dự án - nhu cầu và phân bổ nhân lực"),BorderLayout.NORTH);
        JPanel b=new JPanel(new FlowLayout(FlowLayout.LEFT)); b.add(new JLabel("Tìm dự án:")); b.add(q);
        JButton search=Ui.btn("Tìm kiếm"),members=Ui.btn("Xem chi tiết thành viên"),create=Ui.btn("+ Tạo dự án mới"),refresh=Ui.btn("Làm mới");
        b.add(search);b.add(members);b.add(create);b.add(refresh); add(b,BorderLayout.SOUTH); add(new JScrollPane(t),BorderLayout.CENTER);
        Runnable load=this::load; search.addActionListener(e->load.run());q.addActionListener(e->load.run());refresh.addActionListener(e->load.run());
        members.addActionListener(e->showMembers()); create.addActionListener(e->createProject());
        t.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){if(e.getClickCount()==2)showMembers();}});
        load.run();
    }
    private void load(){try{var rows=new ArrayList<String[]>();String k=q.getText().trim().toLowerCase();for(String[] x:report.projects())if(k.isEmpty()||x[0].toLowerCase().contains(k))rows.add(x);Ui.fill(t,rows);}catch(Exception e){Ui.error(this,e);}}
    private void showMembers(){int r=t.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Chọn một dự án trước.");return;}String project=t.getValueAt(r,0).toString();
        try{JTable m=Ui.table(new String[]{"Mã NV","Họ tên","Vị trí","Phòng ban","Vai trò","Trạng thái"});Ui.fill(m,report.employeesByProject(project));
            JTextField s=new JTextField(15);JButton b=Ui.btn("Tìm");JDialog d=new JDialog(SwingUtilities.getWindowAncestor(this),"Chi tiết nhân sự - "+project,Dialog.ModalityType.APPLICATION_MODAL);d.setSize(980,560);d.setLocationRelativeTo(this);
            JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));top.add(new JLabel("Tìm nhân viên:"));top.add(s);top.add(b);d.add(top,BorderLayout.NORTH);d.add(new JScrollPane(m),BorderLayout.CENTER);
            Runnable load=()->{try{Ui.fill(m,report.employeesByProject(project,s.getText().trim()));}catch(Exception e){Ui.error(d,e);}};b.addActionListener(e->load.run());s.addActionListener(e->load.run());d.setVisible(true);
        }catch(Exception e){Ui.error(this,e);}
    }
    private void createProject(){
        JTextField name=new JTextField();JSpinner need=new JSpinner(new SpinnerNumberModel(10,1,10000,1));JTextField start=new JTextField(LocalDate.now().toString());JComboBox<String> status=new JComboBox<>(new String[]{"ACTIVE","PLANNED","COMPLETED"});
        JPanel f=new JPanel(new GridLayout(0,2,8,8));f.add(new JLabel("Tên dự án:*"));f.add(name);f.add(new JLabel("Số nhân viên cần:*"));f.add(need);f.add(new JLabel("Ngày bắt đầu (yyyy-MM-dd):*"));f.add(start);f.add(new JLabel("Trạng thái:"));f.add(status);
        if(JOptionPane.showConfirmDialog(this,f,"Tạo dự án mới",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)!=JOptionPane.OK_OPTION)return;
        try{projects.create(name.getText().trim(),(Integer)need.getValue(),LocalDate.parse(start.getText().trim()),(String)status.getSelectedItem());load();JOptionPane.showMessageDialog(this,"Đã tạo dự án mới. Bạn có thể vào 'Phân công NV' để thêm thành viên.");}catch(Exception e){Ui.error(this,e);}
=======
    private final JTable t = Ui
            .table(new String[] { "Dự án", "Cần", "Đã có", "Còn thiếu", "Đáp ứng", "Ngày bắt đầu", "Trạng thái" });
    private final JTextField q = new JTextField(18);
    private final ReportService report = new ReportService();
    private final ProjectService projects = new ProjectService();

    public ProjectPanel() {
        Ui.init();
        setBackground(Ui.BG);
        setLayout(new BorderLayout(0, 10));

        JPanel b = Ui.toolbar(new JLabel("Tìm dự án"), q);
        JButton search = Ui.primaryBtn("Tìm kiếm"), members = Ui.btn("Xem thành viên"),
                create = Ui.primaryBtn("+ Tạo dự án"), refresh = Ui.btn("Làm mới");
        b.add(search);
        b.add(members);
        b.add(create);
        b.add(refresh);
        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(Ui.BG);
        north.add(b, BorderLayout.CENTER);
        JPanel pageNorth = new JPanel(new BorderLayout());
        pageNorth.setBackground(Ui.BG);
        pageNorth.add(Ui.top("Dự án", "Theo dõi nhu cầu và phân bổ nhân sự theo từng dự án"), BorderLayout.NORTH);
        pageNorth.add(north, BorderLayout.SOUTH);
        add(pageNorth, BorderLayout.NORTH);
        add(Ui.scroll(t), BorderLayout.CENTER);
        Runnable load = this::load;
        search.addActionListener(e -> load.run());
        q.addActionListener(e -> load.run());
        refresh.addActionListener(e -> load.run());
        members.addActionListener(e -> showMembers());
        create.addActionListener(e -> createProject());
        t.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2)
                    showMembers();
            }
        });
        load.run();
    }

    private void load() {
        try {
            var rows = new ArrayList<String[]>();
            String k = q.getText().trim().toLowerCase();
            for (String[] x : report.projects())
                if (k.isEmpty() || x[0].toLowerCase().contains(k))
                    rows.add(x);
            Ui.fill(t, rows);
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private void showMembers() {
        int r = t.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Chọn một dự án trước.");
            return;
        }
        String project = t.getValueAt(r, 0).toString();
        try {
            JTable m = Ui.table(new String[] { "Mã NV", "Họ tên", "Vị trí", "Phòng ban", "Vai trò", "Trạng thái" });
            Ui.fill(m, report.employeesByProject(project));
            JTextField s = new JTextField(15);
            JButton b = Ui.btn("Tìm");
            JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this), "Chi tiết nhân sự - " + project,
                    Dialog.ModalityType.APPLICATION_MODAL);
            d.setSize(980, 560);
            d.setLocationRelativeTo(this);
            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
            top.add(new JLabel("Tìm nhân viên:"));
            top.add(s);
            top.add(b);
            d.add(top, BorderLayout.NORTH);
            d.add(new JScrollPane(m), BorderLayout.CENTER);
            Runnable load = () -> {
                try {
                    Ui.fill(m, report.employeesByProject(project, s.getText().trim()));
                } catch (Exception e) {
                    Ui.error(d, e);
                }
            };
            b.addActionListener(e -> load.run());
            s.addActionListener(e -> load.run());
            d.setVisible(true);
        } catch (Exception e) {
            Ui.error(this, e);
        }
    }

    private void createProject() {
        JTextField name = new JTextField();
        JSpinner need = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
        JTextField start = new JTextField(LocalDate.now().toString());
        JComboBox<String> status = new JComboBox<>(new String[] { "ACTIVE", "PLANNED", "COMPLETED" });

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Ui.SURFACE);
        form.setBorder(new EmptyBorder(8, 4, 8, 4));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 0, 7, 12);
        c.anchor = GridBagConstraints.WEST;

        addProjectField(form, c, 0, "Tên dự án *", name);
        addProjectField(form, c, 1, "Số nhân viên cần *", need);
        addProjectField(form, c, 2, "Ngày bắt đầu *", start);
        addProjectField(form, c, 3, "Trạng thái", status);

        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Tạo dự án mới", Dialog.ModalityType.APPLICATION_MODAL);
        d.setSize(620, 390);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(Ui.BG);
        d.add(Ui.top("Tạo dự án mới", "Khai báo nhu cầu nhân sự và thời gian bắt đầu"), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Ui.SURFACE);
        center.setBorder(new EmptyBorder(8, 28, 8, 28));
        center.add(form, BorderLayout.NORTH);
        d.add(center, BorderLayout.CENTER);

        JButton cancel = Ui.btn("Hủy");
        JButton save = Ui.primaryBtn("Tạo dự án");
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(Ui.BG);
        footer.add(cancel);
        footer.add(save);
        d.add(footer, BorderLayout.SOUTH);

        cancel.addActionListener(e -> d.dispose());
        save.addActionListener(e -> {
            try {
                if (name.getText().trim().isEmpty())
                    throw new IllegalArgumentException("Tên dự án không được để trống.");
                projects.create(name.getText().trim(),
                        (Integer) need.getValue(),
                        LocalDate.parse(start.getText().trim()),
                        (String) status.getSelectedItem());
                d.dispose();
                load();
                JOptionPane.showMessageDialog(this,
                        "Đã tạo dự án mới. Bạn có thể vào 'Phân công NV' để thêm thành viên.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                Ui.error(d, ex);
            }
        });
        d.getRootPane().setDefaultButton(save);
        d.setVisible(true);
    }

    private void addProjectField(JPanel p, GridBagConstraints c, int row,
            String label, Component field) {
        c.gridy = row;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        JLabel l = new JLabel(label);
        l.setPreferredSize(new Dimension(150, 34));
        p.add(l, c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        field.setPreferredSize(new Dimension(340, 36));
        p.add(field, c);
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
    }
}
