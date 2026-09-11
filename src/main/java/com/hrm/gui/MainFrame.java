package com.hrm.gui;
import com.hrm.model.UserSession;import javax.swing.*;import java.awt.*;
public class MainFrame extends JFrame{
 public MainFrame(UserSession s){
  setTitle("HRM Desktop - "+s.username()+" ["+roleLabel(s.role())+"]");setSize(1300,800);setLocationRelativeTo(null);setDefaultCloseOperation(EXIT_ON_CLOSE);JTabbedPane tabs=new JTabbedPane();
  if(s.isHR()){
   tabs.addTab("Tổng quan",new DashboardPanel());tabs.addTab("Nhân viên",new EmployeePanel());tabs.addTab("Chấm công",new AttendancePanel());tabs.addTab("Sự kiện nhân sự",new HRHistoryPanel(true));tabs.addTab("Định biên",new StaffingPanel());tabs.addTab("Dự án",new ProjectPanel());tabs.addTab("Phân công NV",new AssignmentPanel());tabs.addTab("Phiếu lương",new HRPayrollPanel(false));
  } else if(s.isAccountant()){
   // Kế toán trưởng được xem các form nghiệp vụ chung, nhưng không có Nhân viên/Định biên/Dự án/Phân công NV.
   // Sự kiện nhân sự ở chế độ chỉ xem; không được ghi phạt hay chỉnh hồ sơ.
   tabs.addTab("Tổng quan",new DashboardPanel());
   tabs.addTab("Chấm công",new AttendancePanel());
   tabs.addTab("Sự kiện nhân sự",new HRHistoryPanel(false));
   tabs.addTab("Phiếu lương",new HRPayrollPanel(true));
  } else if(s.isEmployee()){tabs.addTab("Tổng quan cá nhân",new EmployeeHomePanel(s.employeeId()));tabs.addTab("Phiếu lương của tôi",new MyPayslipPanel(s.employeeId()));}
  else if(s.isAdmin()){
   // ADMIN chỉ có khu vực kỹ thuật: giám sát, kiểm tra, nhật ký và bảo trì.
   // Không được truy cập nghiệp vụ HR, nhân viên, lương, dự án, định biên hay phân công.
   tabs.addTab("Quản trị hệ thống",new MaintenancePanel());
  }
  add(tabs,BorderLayout.CENTER);JPanel bottom=new JPanel(new BorderLayout());bottom.add(new JLabel("  "+s.username()+" | "+roleLabel(s.role())+"  "),BorderLayout.WEST);JButton logout=Ui.btn("Đăng xuất");bottom.add(logout,BorderLayout.EAST);add(bottom,BorderLayout.SOUTH);logout.addActionListener(e->{dispose();new LoginFrame().setVisible(true);});
 }
 private String roleLabel(String role){return "ACCOUNTANT".equals(role)?"KẾ TOÁN TRƯỞNG":role;}
}
