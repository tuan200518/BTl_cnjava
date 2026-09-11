package com.hrm.gui;
<<<<<<< HEAD
import com.hrm.model.Employee;import com.hrm.service.EmployeeService;import javax.swing.*;import java.awt.*;import java.time.LocalDate;import java.util.*;
public class EmployeePanel extends JPanel{private final JTable t=Ui.table(new String[]{"Mã NV","Họ tên","Email","Phòng ban","Vị trí","Lương CB","Ngày vào","Ngày nghỉ","Trạng thái","Tài khoản"});private final JTextField q=new JTextField(18);private final JComboBox<String> year=new JComboBox<>(new String[]{"Tất cả 5 năm","2022","2023","2024","2025","2026"});private final JComboBox<String> department=new JComboBox<>(),job=new JComboBox<>(),status=new JComboBox<>(new String[]{"Tất cả","ACTIVE","RESIGNED"});private final JButton add=Ui.btn("Thêm nhân viên"),edit=Ui.btn("Sửa thông tin"),del=Ui.btn("Xóa nhân viên"),account=Ui.btn("Kích hoạt/Khóa tài khoản"),refresh=Ui.btn("Làm mới");private final EmployeeService service=new EmployeeService();
 public EmployeePanel(){setLayout(new BorderLayout(8,8));add(Ui.top("Quản lý nhân viên - tìm kiếm, lọc và quản lý hồ sơ"),BorderLayout.NORTH);JPanel f=new JPanel(new FlowLayout(FlowLayout.LEFT));f.add(new JLabel("Tìm kiếm:"));f.add(q);f.add(new JLabel("Năm:"));f.add(year);f.add(new JLabel("Phòng ban:"));f.add(department);f.add(new JLabel("Vị trí:"));f.add(job);f.add(new JLabel("Trạng thái:"));f.add(status);JPanel a=new JPanel(new FlowLayout(FlowLayout.LEFT));a.add(add);a.add(edit);a.add(del);a.add(account);a.add(refresh);JPanel n=new JPanel(new BorderLayout());n.add(f,BorderLayout.NORTH);n.add(a,BorderLayout.SOUTH);add(n,BorderLayout.CENTER);remove(n);add(n,BorderLayout.NORTH);add(new JScrollPane(t),BorderLayout.CENTER);loadFilters();Runnable l=this::load;year.addActionListener(e->l.run());department.addActionListener(e->l.run());job.addActionListener(e->l.run());status.addActionListener(e->l.run());q.addActionListener(e->l.run());refresh.addActionListener(e->l.run());add.addActionListener(e->showEditor(-1));edit.addActionListener(e->editSelected());del.addActionListener(e->deleteSelected());account.addActionListener(e->toggleAccount());t.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){if(e.getClickCount()==2)editSelected();}});l.run();}
 private int yearValue(){String v=(String)year.getSelectedItem();return "Tất cả 5 năm".equals(v)?0:Integer.parseInt(v);}private void loadFilters(){try{department.addItem("Tất cả");for(String[] x:service.departments())department.addItem(x[1]);job.addItem("Tất cả");for(String[] x:service.jobs())job.addItem(x[1]);}catch(Exception e){Ui.error(this,e);}}private void load(){try{var a=service.find(q.getText().trim(),yearValue(),(String)department.getSelectedItem(),(String)job.getSelectedItem(),(String)status.getSelectedItem());var rows=new ArrayList<String[]>();for(Employee e:a)rows.add(new String[]{e.code(),e.fullName(),e.email(),e.department(),e.job(),String.format("%,.0f",e.salary()),e.hireDate().toString(),e.resignationDate()==null?"":e.resignationDate().toString(),e.status(),e.username()});Ui.fill(t,rows);}catch(Exception e){Ui.error(this,e);}}private void editSelected(){int r=t.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Hãy chọn nhân viên cần sửa.");return;}showEditor(Integer.parseInt(t.getValueAt(r,0).toString().replace("NV","")));}private void deleteSelected(){int r=t.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Hãy chọn nhân viên cần xóa.");return;}int id=Integer.parseInt(t.getValueAt(r,0).toString().replace("NV",""));if(JOptionPane.showConfirmDialog(this,"Xóa hồ sơ NV"+String.format("%04d",id)+"?","Xác nhận",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION)try{service.delete(id);load();}catch(Exception e){Ui.error(this,e);}}private void toggleAccount(){int r=t.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Hãy chọn nhân viên.");return;}int id=Integer.parseInt(t.getValueAt(r,0).toString().replace("NV",""));try{boolean a=service.accountActive(id);service.setAccountActive(id,!a);load();JOptionPane.showMessageDialog(this,(a?"Đã khóa":"Đã kích hoạt")+" tài khoản "+t.getValueAt(r,9));}catch(Exception e){Ui.error(this,e);}}
 private void showEditor(int id){JTextField name=new JTextField(),email=new JTextField(),hire=new JTextField(LocalDate.now().toString()),resign=new JTextField();JComboBox<String> jobs=new JComboBox<>();try{for(String[] x:service.jobs())jobs.addItem(x[0]+" | "+x[1]+" | "+x[2]);}catch(Exception e){Ui.error(this,e);return;}JComboBox<String> st=new JComboBox<>(new String[]{"ACTIVE","RESIGNED"});if(id>0){int r=findRow(id);if(r<0)return;name.setText(t.getValueAt(r,1).toString());email.setText(t.getValueAt(r,2).toString());hire.setText(t.getValueAt(r,6).toString());resign.setText(t.getValueAt(r,7).toString());st.setSelectedItem(t.getValueAt(r,8).toString());String cur=t.getValueAt(r,4).toString();for(int i=0;i<jobs.getItemCount();i++)if(jobs.getItemAt(i).contains("| "+cur+" |")){jobs.setSelectedIndex(i);break;}}JPanel f=new JPanel(new GridLayout(0,2,8,8));f.add(new JLabel("Họ tên:*"));f.add(name);f.add(new JLabel("Email:*"));f.add(email);f.add(new JLabel("Vị trí:*"));f.add(jobs);f.add(new JLabel("Ngày vào:*"));f.add(hire);f.add(new JLabel("Ngày nghỉ:"));f.add(resign);f.add(new JLabel("Trạng thái:"));f.add(st);if(JOptionPane.showConfirmDialog(this,f,id<0?"Thêm nhân viên":"Cập nhật nhân viên",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION)try{LocalDate hd=LocalDate.parse(hire.getText().trim());LocalDate rd=resign.getText().trim().isEmpty()?null:LocalDate.parse(resign.getText().trim());if(rd!=null&&rd.isBefore(hd))throw new IllegalArgumentException("Ngày nghỉ phải sau ngày vào.");int jid=Integer.parseInt(jobs.getSelectedItem().toString().split(" \\|")[0].trim());if(id<0){int ni=service.create(name.getText().trim(),email.getText().trim(),jid,hd,rd,(String)st.getSelectedItem());JOptionPane.showMessageDialog(this,"Đã thêm NV"+String.format("%04d",ni)+". Tài khoản: NV"+String.format("%04d",ni)+" / 123456");}else service.update(id,name.getText().trim(),email.getText().trim(),jid,hd,rd,(String)st.getSelectedItem());load();}catch(Exception e){Ui.error(this,e);}}
 private int findRow(int id){for(int i=0;i<t.getRowCount();i++)if(Integer.parseInt(t.getValueAt(i,0).toString().replace("NV",""))==id)return i;return -1;}}
=======

import com.hrm.model.Employee;
import com.hrm.service.EmployeeService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;

public class EmployeePanel extends JPanel {
        private final JTable t = Ui.table(new String[] { "Mã NV", "Họ tên", "Email", "Phòng ban", "Vị trí", "Lương CB",
                        "Ngày vào", "Ngày nghỉ", "Trạng thái", "Tài khoản" });
        private final JTextField q = new JTextField(18);
        private final JComboBox<String> year = new JComboBox<>(
                        new String[] { "Tất cả 5 năm", "2022", "2023", "2024", "2025", "2026" });
        private final JComboBox<String> department = new JComboBox<>(), job = new JComboBox<>(),
                        status = new JComboBox<>(new String[] { "Tất cả", "ACTIVE", "RESIGNED" });
        private final JButton add = Ui.primaryBtn("+ Thêm nhân viên"), edit = Ui.btn("Sửa thông tin"),
                        del = Ui.dangerBtn("Xóa nhân viên"), account = Ui.btn("Kích hoạt / Khóa"),
                        refresh = Ui.btn("Làm mới");
        private final EmployeeService service = new EmployeeService();

        public EmployeePanel() {
                Ui.init();
                setBackground(Ui.BG);
                setLayout(new BorderLayout(0, 10));
                // =========================
                // BỘ LỌC: dùng GridBagLayout
                // Không dùng FlowLayout vì khi cửa sổ hẹp,
                // các component có thể xuống dòng nhưng panel
                // không tăng chiều cao => bị khu vực nút/bảng đè lên.
                // =========================
                JPanel filters = new JPanel(new GridBagLayout());
                filters.setBackground(Ui.SURFACE);
                filters.setBorder(new EmptyBorder(10, 14, 10, 14));

                GridBagConstraints fc = new GridBagConstraints();
                fc.insets = new Insets(4, 6, 4, 6);
                fc.anchor = GridBagConstraints.WEST;
                fc.fill = GridBagConstraints.HORIZONTAL;
                fc.weighty = 0;

                addFilter(filters, fc, 0, 0, "Tìm kiếm", q, 1.0);
                addFilter(filters, fc, 0, 2, "Năm", year, 0.5);
                addFilter(filters, fc, 0, 4, "Phòng ban", department, 0.8);

                addFilter(filters, fc, 1, 0, "Vị trí", job, 0.8);
                addFilter(filters, fc, 1, 2, "Trạng thái", status, 0.5);

                // =========================
                // NÚT THAO TÁC
                // Luôn nằm ở khu vực riêng,
                // không thể đè lên bộ lọc hoặc JTable.
                // =========================
                JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
                actions.setBackground(Ui.BG);
                actions.setBorder(new EmptyBorder(0, 8, 2, 8));
                actions.add(add);
                actions.add(edit);
                actions.add(del);
                actions.add(account);
                actions.add(refresh);

                JPanel north = new JPanel(new BorderLayout(0, 0));
                north.setBackground(Ui.BG);
                north.add(filters, BorderLayout.NORTH);
                north.add(actions, BorderLayout.SOUTH);
                JPanel pageNorth = new JPanel(new BorderLayout());
                pageNorth.setBackground(Ui.BG);
                pageNorth.add(Ui.top("Nhân viên", "Quản lý hồ sơ, tìm kiếm và trạng thái tài khoản"),
                                BorderLayout.NORTH);
                pageNorth.add(north, BorderLayout.SOUTH);
                add(pageNorth, BorderLayout.NORTH);

                JScrollPane sp = new JScrollPane(t);
                sp.setBorder(new LineBorder(Ui.BORDER));
                sp.getViewport().setBackground(Color.WHITE);
                add(sp, BorderLayout.CENTER);

                loadFilters();
                Runnable l = this::load;
                year.addActionListener(e -> l.run());
                department.addActionListener(e -> l.run());
                job.addActionListener(e -> l.run());
                status.addActionListener(e -> l.run());
                q.addActionListener(e -> l.run());
                refresh.addActionListener(e -> l.run());
                add.addActionListener(e -> showEditor(-1));
                edit.addActionListener(e -> editSelected());
                del.addActionListener(e -> deleteSelected());
                account.addActionListener(e -> toggleAccount());
                t.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                                if (e.getClickCount() == 2)
                                        editSelected();
                        }
                });
                l.run();
        }

        private int yearValue() {
                String v = (String) year.getSelectedItem();
                return "Tất cả 5 năm".equals(v) ? 0 : Integer.parseInt(v);
        }

        private void addFilter(
                        JPanel panel,
                        GridBagConstraints base,
                        int row,
                        int x,
                        String label,
                        JComponent field,
                        double weight) {

                GridBagConstraints c1 = (GridBagConstraints) base.clone();
                c1.gridx = x;
                c1.gridy = row;
                c1.weightx = 0;
                c1.fill = GridBagConstraints.NONE;

                JLabel l = new JLabel(label);
                l.setFont(l.getFont().deriveFont(Font.PLAIN, 14f));
                panel.add(l, c1);

                GridBagConstraints c2 = (GridBagConstraints) base.clone();
                c2.gridx = x + 1;
                c2.gridy = row;
                c2.weightx = weight;
                c2.fill = GridBagConstraints.HORIZONTAL;

                if (field instanceof JTextField) {
                        field.setPreferredSize(new Dimension(220, 40));
                } else {
                        field.setPreferredSize(new Dimension(190, 40));
                }

                panel.add(field, c2);
        }

        private void loadFilters() {
                try {
                        department.addItem("Tất cả");
                        for (String[] x : service.departments())
                                department.addItem(x[1]);
                        job.addItem("Tất cả");
                        for (String[] x : service.jobs())
                                job.addItem(x[1]);
                } catch (Exception e) {
                        Ui.error(this, e);
                }
        }

        private void load() {
                try {
                        var a = service.find(q.getText().trim(), yearValue(), (String) department.getSelectedItem(),
                                        (String) job.getSelectedItem(), (String) status.getSelectedItem());
                        var rows = new ArrayList<String[]>();
                        for (Employee e : a)
                                rows.add(new String[] { e.code(), e.fullName(), e.email(), e.department(), e.job(),
                                                String.format("%,.0f", e.salary()), e.hireDate().toString(),
                                                e.resignationDate() == null ? "" : e.resignationDate().toString(),
                                                e.status(), e.username() });
                        Ui.fill(t, rows);
                } catch (Exception e) {
                        Ui.error(this, e);
                }
        }

        private void editSelected() {
                int r = t.getSelectedRow();
                if (r < 0) {
                        JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần sửa.");
                        return;
                }
                showEditor(Integer.parseInt(t.getValueAt(r, 0).toString().replace("NV", "")));
        }

        private void deleteSelected() {
                int r = t.getSelectedRow();
                if (r < 0) {
                        JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần xóa.");
                        return;
                }
                int id = Integer.parseInt(t.getValueAt(r, 0).toString().replace("NV", ""));
                if (JOptionPane.showConfirmDialog(this, "Xóa hồ sơ NV" + String.format("%04d", id) + "?", "Xác nhận",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)
                        try {
                                service.delete(id);
                                load();
                        } catch (Exception e) {
                                Ui.error(this, e);
                        }
        }

        private void toggleAccount() {
                int r = t.getSelectedRow();
                if (r < 0) {
                        JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên.");
                        return;
                }
                int id = Integer.parseInt(t.getValueAt(r, 0).toString().replace("NV", ""));
                try {
                        boolean a = service.accountActive(id);
                        service.setAccountActive(id, !a);
                        load();
                        JOptionPane.showMessageDialog(this,
                                        (a ? "Đã khóa" : "Đã kích hoạt") + " tài khoản " + t.getValueAt(r, 9));
                } catch (Exception e) {
                        Ui.error(this, e);
                }
        }

        private void showEditor(int id) {
                JTextField name = new JTextField();
                JTextField email = new JTextField();
                JTextField hire = new JTextField(LocalDate.now().toString());
                JTextField resign = new JTextField();
                JComboBox<String> jobs = new JComboBox<>();
                try {
                        for (String[] x : service.jobs())
                                jobs.addItem(x[0] + " | " + x[1] + " | " + x[2]);
                } catch (Exception ex) {
                        Ui.error(this, ex);
                        return;
                }

                JComboBox<String> st = new JComboBox<>(new String[] { "ACTIVE", "RESIGNED" });

                if (id > 0) {
                        int r = findRow(id);
                        if (r < 0)
                                return;
                        name.setText(t.getValueAt(r, 1).toString());
                        email.setText(t.getValueAt(r, 2).toString());
                        hire.setText(t.getValueAt(r, 6).toString());
                        resign.setText(t.getValueAt(r, 7).toString());
                        st.setSelectedItem(t.getValueAt(r, 8).toString());
                        String cur = t.getValueAt(r, 4).toString();
                        for (int i = 0; i < jobs.getItemCount(); i++)
                                if (jobs.getItemAt(i).contains("| " + cur + " |")) {
                                        jobs.setSelectedIndex(i);
                                        break;
                                }
                }

                JPanel form = new JPanel(new GridBagLayout());
                form.setBackground(Ui.SURFACE);
                form.setBorder(new EmptyBorder(8, 4, 8, 4));
                GridBagConstraints c = new GridBagConstraints();
                c.insets = new Insets(7, 0, 7, 12);
                c.anchor = GridBagConstraints.WEST;
                c.gridx = 0;
                c.weightx = 0;
                c.fill = GridBagConstraints.NONE;
                addField(form, c, 0, "Họ tên *", name);
                addField(form, c, 1, "Email *", email);
                addField(form, c, 2, "Vị trí *", jobs);
                addField(form, c, 3, "Ngày vào *", hire);
                addField(form, c, 4, "Ngày nghỉ", resign);
                addField(form, c, 5, "Trạng thái", st);

                JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this),
                                id < 0 ? "Thêm nhân viên" : "Cập nhật nhân viên",
                                Dialog.ModalityType.APPLICATION_MODAL);
                d.setSize(650, 520);
                d.setMinimumSize(new Dimension(600, 470));
                d.setLocationRelativeTo(this);
                d.setLayout(new BorderLayout());
                d.getContentPane().setBackground(Ui.BG);

                JPanel header = Ui.top(id < 0 ? "Thêm nhân viên" : "Cập nhật nhân viên",
                                id < 0 ? "Tạo hồ sơ nhân viên mới" : "Chỉnh sửa thông tin hồ sơ nhân viên");
                d.add(header, BorderLayout.NORTH);

                JPanel center = new JPanel(new BorderLayout());
                center.setBackground(Ui.SURFACE);
                center.setBorder(new EmptyBorder(8, 28, 8, 28));
                center.add(form, BorderLayout.NORTH);
                d.add(center, BorderLayout.CENTER);

                JButton cancel = Ui.btn("Hủy");
                JButton save = Ui.primaryBtn(id < 0 ? "Thêm nhân viên" : "Lưu thay đổi");
                JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
                footer.setBackground(Ui.BG);
                footer.add(cancel);
                footer.add(save);
                d.add(footer, BorderLayout.SOUTH);

                cancel.addActionListener(e -> d.dispose());
                save.addActionListener(e -> {
                        try {
                                LocalDate hd = LocalDate.parse(hire.getText().trim());
                                LocalDate rd = resign.getText().trim().isEmpty() ? null
                                                : LocalDate.parse(resign.getText().trim());
                                if (rd != null && rd.isBefore(hd))
                                        throw new IllegalArgumentException("Ngày nghỉ phải sau ngày vào.");
                                int jid = Integer.parseInt(jobs.getSelectedItem().toString().split(" \\|")[0].trim());

                                if (id < 0) {
                                        int ni = service.create(name.getText().trim(), email.getText().trim(), jid, hd,
                                                        rd, (String) st.getSelectedItem());
                                        JOptionPane.showMessageDialog(d,
                                                        "Đã thêm NV" + String.format("%04d", ni) + ".\nTài khoản: NV"
                                                                        + String.format("%04d", ni) + " / 123456",
                                                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                        service.update(id, name.getText().trim(), email.getText().trim(), jid, hd, rd,
                                                        (String) st.getSelectedItem());
                                        JOptionPane.showMessageDialog(d, "Đã cập nhật hồ sơ nhân viên.", "Thành công",
                                                        JOptionPane.INFORMATION_MESSAGE);
                                }
                                d.dispose();
                                load();
                        } catch (Exception ex) {
                                Ui.error(d, ex);
                        }
                });

                d.getRootPane().setDefaultButton(save);
                d.setVisible(true);
        }

        private void addField(JPanel p, GridBagConstraints c, int row, String label, Component field) {
                c.gridy = row;
                c.gridx = 0;
                c.weightx = 0;
                c.fill = GridBagConstraints.NONE;
                JLabel l = new JLabel(label);
                l.setPreferredSize(new Dimension(105, 34));
                p.add(l, c);
                c.gridx = 1;
                c.weightx = 1;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.insets = new Insets(7, 0, 7, 0);
                if (field instanceof JComponent)
                        ((JComponent) field).setPreferredSize(new Dimension(390, 36));
                p.add(field, c);
                c.insets = new Insets(7, 0, 7, 12);
        }

        private int findRow(int id) {
                for (int i = 0; i < t.getRowCount(); i++)
                        if (Integer.parseInt(t.getValueAt(i, 0).toString().replace("NV", "")) == id)
                                return i;
                return -1;
        }
}
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
