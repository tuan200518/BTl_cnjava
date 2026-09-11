package com.hrm.gui;

import com.hrm.service.AssignmentService;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class AssignmentPanel extends JPanel {
    private final JTable t=Ui.table(new String[]{"Mã NV","Nhân viên","Phòng ban","Vị trí công việc","Dự án","Vai trò trong dự án","Trạng thái"});
    private final JTextField q=new JTextField(18);
    private final JComboBox<String> department=new JComboBox<>(), job=new JComboBox<>(), project=new JComboBox<>(), status=new JComboBox<>(new String[]{"Tất cả","ACTIVE","RESIGNED"});
    private final JButton assignJob=Ui.btn("Phân công công việc"),assignProject=Ui.btn("Phân công dự án"),removeProject=Ui.btn("Bỏ khỏi dự án"),refresh=Ui.btn("Làm mới");
    private final AssignmentService service=new AssignmentService();
    public AssignmentPanel(){
        setLayout(new BorderLayout(8,8));add(Ui.top("Phân công nhân viên - công việc và dự án"),BorderLayout.NORTH);
        JPanel f=new JPanel(new FlowLayout(FlowLayout.LEFT));f.add(new JLabel("Tìm:"));f.add(q);f.add(new JLabel("Phòng ban:"));f.add(department);f.add(new JLabel("Vị trí:"));f.add(job);f.add(new JLabel("Dự án:"));f.add(project);f.add(new JLabel("Trạng thái:"));f.add(status);
        JPanel a=new JPanel(new FlowLayout(FlowLayout.LEFT));a.add(assignJob);a.add(assignProject);a.add(removeProject);a.add(refresh);
        JPanel north=new JPanel(new BorderLayout());north.add(f,BorderLayout.NORTH);north.add(a,BorderLayout.SOUTH);add(north,BorderLayout.NORTH);add(new JScrollPane(t),BorderLayout.CENTER);
        loadFilters(); Runnable load=this::load; q.addActionListener(e->load.run());department.addActionListener(e->load.run());job.addActionListener(e->load.run());project.addActionListener(e->load.run());status.addActionListener(e->load.run());refresh.addActionListener(e->load.run());
        assignJob.addActionListener(e->assignJob());assignProject.addActionListener(e->assignProject());removeProject.addActionListener(e->removeProject());load.run();
    }
    private void loadFilters(){try{department.addItem("Tất cả");for(String[] x:service.departments())department.addItem(x[1]);job.addItem("Tất cả");for(String[] x:service.jobs())job.addItem(x[1]);project.addItem("Tất cả");for(String[] x:service.projects())project.addItem(x[1]);}catch(Exception e){Ui.error(this,e);}}
    private void load(){try{Ui.fill(t,service.employees(q.getText().trim(),(String)department.getSelectedItem(),(String)job.getSelectedItem(),(String)project.getSelectedItem(),(String)status.getSelectedItem()));}catch(Exception e){Ui.error(this,e);}}
    private int selectedId(){int r=t.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Hãy chọn nhân viên trước.");return -1;}return Integer.parseInt(t.getValueAt(r,0).toString().replace("NV",""));}
    private void assignJob(){int id=selectedId();if(id<0)return;try{java.util.List<String[]> jobs=service.jobs();JComboBox<String> cb=new JComboBox<>();for(String[] x:jobs)cb.addItem(x[0]+" | "+x[1]+" | "+x[2]);JPanel p=new JPanel(new BorderLayout(8,8));p.add(new JLabel("Vị trí mới:"),BorderLayout.WEST);p.add(cb,BorderLayout.CENTER);if(JOptionPane.showConfirmDialog(this,p,"Phân công công việc",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){int jobId=Integer.parseInt(cb.getSelectedItem().toString().split(" \\|")[0].trim());service.assignJob(id,jobId);load();JOptionPane.showMessageDialog(this,"Đã cập nhật vị trí công việc và phòng ban theo vị trí mới.");}}catch(Exception e){Ui.error(this,e);}}
    private void assignProject(){int id=selectedId();if(id<0)return;try{java.util.List<String[]> ps=service.projects();JComboBox<String> cb=new JComboBox<>();for(String[] x:ps)cb.addItem(x[0]+" | "+x[1]+" | "+x[2]);JTextField role=new JTextField("Thành viên dự án");JPanel p=new JPanel(new GridLayout(2,2,8,8));p.add(new JLabel("Dự án:"));p.add(cb);p.add(new JLabel("Vai trò:"));p.add(role);if(JOptionPane.showConfirmDialog(this,p,"Phân công dự án",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){int projectId=Integer.parseInt(cb.getSelectedItem().toString().split(" \\|")[0].trim());service.assignProject(id,projectId,role.getText().trim());load();JOptionPane.showMessageDialog(this,"Đã phân công nhân viên vào dự án.");}}catch(Exception e){Ui.error(this,e);}}
    private void removeProject(){int id=selectedId();if(id<0)return;try{java.util.List<String[]> ps=service.projectAssignments(id);if(ps.isEmpty()){JOptionPane.showMessageDialog(this,"Nhân viên này chưa được phân công dự án.");return;}JComboBox<String> cb=new JComboBox<>();for(String[] x:ps)cb.addItem(x[0]+" | "+x[1]+" | "+x[2]);if(JOptionPane.showConfirmDialog(this,cb,"Chọn dự án cần bỏ",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){int projectId=Integer.parseInt(cb.getSelectedItem().toString().split(" \\|")[0].trim());service.removeProject(id,projectId);load();JOptionPane.showMessageDialog(this,"Đã bỏ phân công dự án.");}}catch(Exception e){Ui.error(this,e);}}
}
