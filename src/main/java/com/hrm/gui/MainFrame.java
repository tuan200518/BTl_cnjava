package com.hrm.gui;
import com.hrm.model.UserSession;import javax.swing.*;import java.awt.*;
public class MainFrame extends JFrame{
 public MainFrame(UserSession s){setTitle("HRM Desktop - "+s.username()+" ["+s.role()+"]");setSize(1300,800);setLocationRelativeTo(null);setDefaultCloseOperation(EXIT_ON_CLOSE);JTabbedPane tabs=new JTabbedPane();
  if(s.isHR()){tabs.addTab("Tổng quan",new DashboardPanel());tabs.addTab("Nhân viên",new EmployeePanel());tabs.addTab("Chấm công",new AttendancePanel());tabs.addTab("Sự kiện nhân sự",new HRHistoryPanel());tabs.addTab("Định biên",new StaffingPanel());tabs.addTab("Dự án",new ProjectPanel());tabs.addTab("Phân công NV",new AssignmentPanel());tabs.addTab("Phiếu lương",new HRPayrollPanel());}
  else if(s.isEmployee()){tabs.addTab("Tổng quan cá nhân",new EmployeeHomePanel(s.employeeId()));tabs.addTab("Phiếu lương của tôi",new MyPayslipPanel(s.employeeId()));}
  else if(s.isAdmin()){tabs.addTab("Bảo trì hệ thống",new MaintenancePanel());}
  add(tabs,BorderLayout.CENTER);JPanel bottom=new JPanel(new BorderLayout());bottom.add(new JLabel("  "+s.username()+" | "+s.role()+"  "),BorderLayout.WEST);JButton logout=Ui.btn("Đăng xuất");bottom.add(logout,BorderLayout.EAST);add(bottom,BorderLayout.SOUTH);logout.addActionListener(e->{dispose();new LoginFrame().setVisible(true);});
 }
}
