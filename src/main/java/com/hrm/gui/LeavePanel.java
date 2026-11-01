package com.hrm.gui;

import com.hrm.config.DBConnection;
import com.hrm.model.UserSession;
import com.hrm.util.CsvExporter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

/** Quản lý đơn nghỉ phép: nhân viên gửi/xem đơn; HR duyệt/từ chối. */
public class LeavePanel extends JPanel {
    private final UserSession session;
    private final boolean hr;
    private final JTable table = Ui.table(new String[]{"Mã đơn","Mã NV","Họ tên","Loại nghỉ","Từ ngày","Đến ngày","Số ngày","Lý do","Trạng thái","Người duyệt"});
    private final JComboBox<String> filter = new JComboBox<>(new String[]{"Tất cả","PENDING","APPROVED","REJECTED","CANCELLED"});
    public LeavePanel(UserSession session) {
        this.session=session; this.hr=session.isHR() || session.isAdmin();
        Ui.init(); setLayout(new BorderLayout(0,10)); setBackground(Ui.BG);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT,8,8)); actions.setBackground(Ui.BG);
        JButton refresh=Ui.btn("Làm mới"), export=Ui.btn("Xuất Excel/CSV"), request=Ui.primaryBtn("+ Xin nghỉ phép");
        actions.add(new JLabel("Trạng thái")); actions.add(filter); actions.add(refresh); actions.add(export); actions.add(request);
        if(hr){ JButton approve=Ui.primaryBtn("Duyệt đơn"), reject=Ui.dangerBtn("Từ chối"); actions.add(approve); actions.add(reject); approve.addActionListener(e->review("APPROVED")); reject.addActionListener(e->review("REJECTED")); }
        JPanel north=new JPanel(new BorderLayout()); north.setBackground(Ui.BG); north.add(Ui.top("Quản lý nghỉ phép", hr?"Tiếp nhận, duyệt và theo dõi đơn nghỉ phép":"Gửi đơn và theo dõi trạng thái nghỉ phép của bạn"),BorderLayout.NORTH); north.add(actions,BorderLayout.SOUTH);
        add(north,BorderLayout.NORTH); add(Ui.scroll(table),BorderLayout.CENTER);
        refresh.addActionListener(e->load()); filter.addActionListener(e->load()); export.addActionListener(e->CsvExporter.export(this,table,"bao_cao_nghi_phep.csv")); request.addActionListener(e->newRequest()); load();
    }
    private void load(){
        String sql="SELECT l.request_id,l.employee_id,e.full_name,l.leave_type,l.start_date,l.end_date,l.total_days,l.reason,l.status,COALESCE(l.reviewed_by,'') FROM leave_requests l JOIN employees e ON e.employee_id=l.employee_id WHERE (?=1 OR l.employee_id=?) AND (?='Tất cả' OR l.status=?) ORDER BY l.request_id DESC";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,hr?1:0); p.setObject(2,session.employeeId()); String f=(String)filter.getSelectedItem(); p.setString(3,f);p.setString(4,f);
            try(ResultSet r=p.executeQuery()){ var m=(javax.swing.table.DefaultTableModel)table.getModel(); m.setRowCount(0); while(r.next())m.addRow(new Object[]{r.getLong(1),String.format("NV%04d",r.getInt(2)),r.getString(3),r.getString(4),r.getDate(5),r.getDate(6),r.getBigDecimal(7),r.getString(8),r.getString(9),r.getString(10)}); }
        }catch(Exception ex){Ui.error(this,ex);}
    }
    private void newRequest(){
        if(session.employeeId()==null){JOptionPane.showMessageDialog(this,"Tài khoản này chưa liên kết hồ sơ nhân viên.");return;}
        JTextField type=new JTextField("Nghỉ phép năm"), start=new JTextField(LocalDate.now().toString()), end=new JTextField(LocalDate.now().toString()), days=new JTextField("1"), reason=new JTextField();
        JPanel form=new JPanel(new GridLayout(0,2,8,8)); form.add(new JLabel("Loại nghỉ"));form.add(type);form.add(new JLabel("Từ ngày (YYYY-MM-DD)"));form.add(start);form.add(new JLabel("Đến ngày (YYYY-MM-DD)"));form.add(end);form.add(new JLabel("Số ngày nghỉ"));form.add(days);form.add(new JLabel("Lý do"));form.add(reason);
        if(JOptionPane.showConfirmDialog(this,form,"Tạo đơn xin nghỉ phép",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)!=JOptionPane.OK_OPTION)return;
        try{
            LocalDate s=LocalDate.parse(start.getText().trim()), en=LocalDate.parse(end.getText().trim()); BigDecimal d=new BigDecimal(days.getText().trim());
            if(type.getText().isBlank()||en.isBefore(s)||d.signum()<=0||d.compareTo(BigDecimal.valueOf(java.time.temporal.ChronoUnit.DAYS.between(s,en)+1))>0)throw new IllegalArgumentException("Ngày nghỉ hoặc số ngày không hợp lệ.");
            try(Connection c=DBConnection.getConnection()){
                try(PreparedStatement check=c.prepareStatement("SELECT COUNT(*) FROM leave_requests WHERE employee_id=? AND status IN ('PENDING','APPROVED') AND NOT(end_date<? OR start_date>?)")){check.setInt(1,session.employeeId());check.setDate(2,Date.valueOf(s));check.setDate(3,Date.valueOf(en));try(ResultSet r=check.executeQuery()){r.next();if(r.getInt(1)>0)throw new IllegalArgumentException("Khoảng thời gian này bị trùng với đơn đang chờ hoặc đã duyệt.");}}
                try(PreparedStatement p=c.prepareStatement("INSERT INTO leave_requests(employee_id,leave_type,start_date,end_date,total_days,reason,status) VALUES(?,?,?,?,?,?,'PENDING')")){p.setInt(1,session.employeeId());p.setString(2,type.getText().trim());p.setDate(3,Date.valueOf(s));p.setDate(4,Date.valueOf(en));p.setBigDecimal(5,d);p.setString(6,reason.getText().trim());p.executeUpdate();}
            } load(); JOptionPane.showMessageDialog(this,"Đã gửi đơn nghỉ phép, trạng thái: PENDING.");
        }catch(Exception ex){JOptionPane.showMessageDialog(this,"Không thể tạo đơn: "+ex.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);}
    }
    private void cancelRequest(){
        int row=table.getSelectedRow();
        if(row<0){JOptionPane.showMessageDialog(this,"Hãy chọn đơn cần hủy.");return;}
        if(!"PENDING".equals(table.getValueAt(row,8).toString())){JOptionPane.showMessageDialog(this,"Chỉ có thể hủy đơn đang PENDING.");return;}
        long id=Long.parseLong(table.getValueAt(row,0).toString());
        if(JOptionPane.showConfirmDialog(this,"Bạn có chắc muốn hủy đơn nghỉ phép này?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("UPDATE leave_requests SET status='CANCELLED' WHERE request_id=? AND employee_id=? AND status='PENDING'")){
            p.setLong(1,id);p.setInt(2,session.employeeId());p.executeUpdate();load();
        }catch(Exception ex){Ui.error(this,ex);}
    }
    private void review(String status){int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Hãy chọn đơn cần xử lý.");return;}long id=Long.parseLong(table.getValueAt(row,0).toString());if(!"PENDING".equals(table.getValueAt(row,8).toString())){JOptionPane.showMessageDialog(this,"Chỉ xử lý đơn đang PENDING.");return;}
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("UPDATE leave_requests SET status=?,reviewed_by=?,reviewed_at=NOW() WHERE request_id=? AND status='PENDING'")){p.setString(1,status);p.setString(2,session.username());p.setLong(3,id);p.executeUpdate();load();JOptionPane.showMessageDialog(this,status.equals("APPROVED")?"Đã duyệt đơn nghỉ phép.":"Đã từ chối đơn nghỉ phép.");}catch(Exception ex){Ui.error(this,ex);}}
}
