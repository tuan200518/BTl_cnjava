package com.hrm.gui;

import com.hrm.service.ProjectService;
import com.hrm.service.ReportService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;

public class ProjectPanel extends JPanel {
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
    }
}
