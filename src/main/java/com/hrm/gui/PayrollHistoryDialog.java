package com.hrm.gui;
<<<<<<< HEAD
import com.hrm.model.Payroll;import com.hrm.service.PayrollService;import javax.swing.*;import java.awt.*;import java.util.ArrayList;
public class PayrollHistoryDialog extends JDialog{
 private final JTable t=Ui.table(new String[]{"ID","Mã NV","Nhân viên","Tháng","Năm","Lương CB","OT","Thưởng","Phạt / trừ lương","Thực lĩnh","Trạng thái"});
 private final JTextField q=new JTextField(16);private final JComboBox<String> year;private final JComboBox<Integer> month=new JComboBox<>(new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12});private final JComboBox<String> status=new JComboBox<>(new String[]{"Tất cả","DRAFT","SENT"});
 public PayrollHistoryDialog(Window owner){this(owner,false);}
 public PayrollHistoryDialog(Window owner, boolean accountantMode){super(owner,"Lịch sử phiếu lương",ModalityType.APPLICATION_MODAL);year=new JComboBox<>(accountantMode?new String[]{"2022","2023","2024","2025","2026"}:new String[]{"Tất cả 5 năm","2022","2023","2024","2025","2026"});if(accountantMode)year.setSelectedItem("2026");setSize(1050,600);setLocationRelativeTo(owner);setLayout(new BorderLayout(8,8));add(Ui.top("Lịch sử tạo và gửi phiếu lương"),BorderLayout.NORTH);JPanel f=new JPanel(new FlowLayout(FlowLayout.LEFT));f.add(new JLabel("Tìm NV:"));f.add(q);f.add(new JLabel("Năm:"));f.add(year);f.add(new JLabel("Tháng:"));f.add(month);f.add(new JLabel("Trạng thái:"));f.add(status);JButton search=Ui.btn("Tìm kiếm");f.add(search);add(f,BorderLayout.SOUTH);add(new JScrollPane(t),BorderLayout.CENTER);Runnable load=this::load;search.addActionListener(e->load.run());q.addActionListener(e->load.run());year.addActionListener(e->load.run());month.addActionListener(e->load.run());status.addActionListener(e->load.run());load.run();}
 private int yearValue(){String v=(String)year.getSelectedItem();return "Tất cả 5 năm".equals(v)?0:Integer.parseInt(v);}
    private void load(){try{var rows=new ArrayList<String[]>();for(Payroll p:new PayrollService().history(q.getText().trim(),yearValue(),(Integer)month.getSelectedItem(),(String)status.getSelectedItem()))rows.add(new String[]{String.valueOf(p.id()),p.employeeCode(),p.employeeName(),String.valueOf(p.month()),String.valueOf(p.year()),fmt(p.base()),fmt(p.overtime()),fmt(p.bonus()),fmt(p.deduction()),fmt(p.net()),p.status()});Ui.fill(t,rows);}catch(Exception e){Ui.error(this,e);}}
 private String fmt(double d){return String.format("%,.0f",d);}
=======

import com.hrm.model.Payroll;
import com.hrm.service.PayrollService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PayrollHistoryDialog extends JDialog {
   private final JTable t = Ui.table(new String[] { "ID", "Mã NV", "Nhân viên", "Tháng", "Năm", "Lương CB", "OT",
         "Thưởng", "Phạt / trừ lương", "Thực lĩnh", "Trạng thái" });
   private final JTextField q = new JTextField(16);
   private final JComboBox<String> year;
   private final JComboBox<Integer> month = new JComboBox<>(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
   private final JComboBox<String> status = new JComboBox<>(new String[] { "Tất cả", "DRAFT", "SENT" });

   public PayrollHistoryDialog(Window owner) {
      this(owner, false);
   }

   public PayrollHistoryDialog(Window owner, boolean accountantMode) {
      super(owner, "Lịch sử phiếu lương", ModalityType.APPLICATION_MODAL);
      Ui.init();
      getContentPane().setBackground(Ui.BG);
      year = new JComboBox<>(accountantMode ? new String[] { "2022", "2023", "2024", "2025", "2026" }
            : new String[] { "Tất cả 5 năm", "2022", "2023", "2024", "2025", "2026" });
      if (accountantMode)
         year.setSelectedItem("2026");
      setSize(1050, 600);
      setLocationRelativeTo(owner);
      setLayout(new BorderLayout(8, 8));
      JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT));
      f.add(new JLabel("Tìm NV:"));
      f.add(q);
      f.add(new JLabel("Năm:"));
      f.add(year);
      f.add(new JLabel("Tháng:"));
      f.add(month);
      f.add(new JLabel("Trạng thái:"));
      f.add(status);
      JButton search = Ui.btn("Tìm kiếm");
      f.add(search);
      f.setBackground(Ui.SURFACE);
      JPanel pageNorth = new JPanel(new BorderLayout());
      pageNorth.setBackground(Ui.BG);
      pageNorth.add(Ui.top("Lịch sử phiếu lương", "Tra cứu phiếu theo nhân viên, kỳ lương và trạng thái"),
            BorderLayout.NORTH);
      pageNorth.add(f, BorderLayout.SOUTH);
      add(pageNorth, BorderLayout.NORTH);
      add(Ui.scroll(t), BorderLayout.CENTER);
      Runnable load = this::load;
      search.addActionListener(e -> load.run());
      q.addActionListener(e -> load.run());
      year.addActionListener(e -> load.run());
      month.addActionListener(e -> load.run());
      status.addActionListener(e -> load.run());
      load.run();
   }

   private int yearValue() {
      String v = (String) year.getSelectedItem();
      return "Tất cả 5 năm".equals(v) ? 0 : Integer.parseInt(v);
   }

   private void load() {
      try {
         var rows = new ArrayList<String[]>();
         for (Payroll p : new PayrollService().history(q.getText().trim(), yearValue(),
               (Integer) month.getSelectedItem(), (String) status.getSelectedItem()))
            rows.add(new String[] { String.valueOf(p.id()), p.employeeCode(), p.employeeName(),
                  String.valueOf(p.month()), String.valueOf(p.year()), fmt(p.base()), fmt(p.overtime()), fmt(p.bonus()),
                  fmt(p.deduction()), fmt(p.net()), p.status() });
         Ui.fill(t, rows);
      } catch (Exception e) {
         Ui.error(this, e);
      }
   }

   private String fmt(double d) {
      return String.format("%,.0f", d);
   }
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
}
