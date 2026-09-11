package com.hrm.gui;

import com.hrm.service.MaintenanceService;
import javax.swing.*;
import java.awt.*;

<<<<<<< HEAD
/** Chỉ dùng cho ADMIN. Không chứa nghiệp vụ nhân sự, lương, dự án hay định biên. */
=======
/**
 * Chỉ dùng cho ADMIN. Không chứa nghiệp vụ nhân sự, lương, dự án hay định biên.
 */
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
public class MaintenancePanel extends JPanel {
    private final JTextArea output = new JTextArea();
    private final MaintenanceService service = new MaintenanceService();

    public MaintenancePanel() {
<<<<<<< HEAD
        setLayout(new BorderLayout(10,10));
        add(Ui.top("Quản trị hệ thống - ADMIN | Giám sát, kiểm tra và bảo trì"), BorderLayout.NORTH);
        output.setEditable(false); output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        add(new JScrollPane(output), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton conn=Ui.btn("Kết nối MySQL"), stats=Ui.btn("Thống kê hệ thống"), accounts=Ui.btn("Tình trạng tài khoản");
        JButton integrity=Ui.btn("Kiểm tra toàn vẹn"), logs=Ui.btn("Xem nhật ký"), optimize=Ui.btn("Tối ưu bảng");
        JButton clean=Ui.btn("Dọn nhật ký cũ"), clear=Ui.btn("Xóa màn hình");
        for(JButton b:new JButton[]{conn,stats,accounts,integrity,logs,optimize,clean,clear}) controls.add(b);
        add(controls, BorderLayout.SOUTH);

        conn.addActionListener(e->run(() -> service.testConnection()));
        stats.addActionListener(e->run(() -> service.systemStats()));
        accounts.addActionListener(e->run(() -> service.accountStats()));
        integrity.addActionListener(e->run(() -> service.integrityCheck()));
        logs.addActionListener(e->run(() -> service.recentLogs(50)));
        optimize.addActionListener(e->{ if(confirm("Tối ưu các bảng hệ thống?")) run(() -> service.optimize()); });
        clean.addActionListener(e->{
            String v=JOptionPane.showInputDialog(this,"Giữ lại nhật ký trong bao nhiêu ngày?","90");
            if(v!=null) try { int days=Integer.parseInt(v.trim()); if(confirm("Xóa nhật ký cũ hơn "+days+" ngày?")) run(() -> service.cleanupOldLogs(days)); }
            catch(NumberFormatException ex){JOptionPane.showMessageDialog(this,"Số ngày không hợp lệ.","Lỗi",JOptionPane.ERROR_MESSAGE);}
        });
        clear.addActionListener(e->output.setText(""));
    }

    private boolean confirm(String s){return JOptionPane.showConfirmDialog(this,s,"Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;}
    private void run(Task t){try{String r=t.run();output.append("\n["+new java.util.Date()+"]\n"+r+"\n");output.setCaretPosition(output.getDocument().getLength());}catch(Exception ex){Ui.error(this,ex);}}
    @FunctionalInterface private interface Task{String run() throws Exception;}
=======
        setLayout(new BorderLayout(10, 10));
        add(Ui.top("Quản trị hệ thống - ADMIN | Giám sát, kiểm tra và bảo trì"), BorderLayout.NORTH);
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        add(new JScrollPane(output), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton conn = Ui.btn("Kết nối MySQL"), stats = Ui.btn("Thống kê hệ thống"),
                accounts = Ui.btn("Tình trạng tài khoản");
        JButton integrity = Ui.btn("Kiểm tra toàn vẹn"), logs = Ui.btn("Xem nhật ký"), optimize = Ui.btn("Tối ưu bảng");
        JButton clean = Ui.btn("Dọn nhật ký cũ"), clear = Ui.btn("Xóa màn hình");
        for (JButton b : new JButton[] { conn, stats, accounts, integrity, logs, optimize, clean, clear })
            controls.add(b);
        add(controls, BorderLayout.SOUTH);

        conn.addActionListener(e -> run(() -> service.testConnection()));
        stats.addActionListener(e -> run(() -> service.systemStats()));
        accounts.addActionListener(e -> run(() -> service.accountStats()));
        integrity.addActionListener(e -> run(() -> service.integrityCheck()));
        logs.addActionListener(e -> run(() -> service.recentLogs(50)));
        optimize.addActionListener(e -> {
            if (confirm("Tối ưu các bảng hệ thống?"))
                run(() -> service.optimize());
        });
        clean.addActionListener(e -> {
            String v = JOptionPane.showInputDialog(this, "Giữ lại nhật ký trong bao nhiêu ngày?", "90");
            if (v != null)
                try {
                    int days = Integer.parseInt(v.trim());
                    if (confirm("Xóa nhật ký cũ hơn " + days + " ngày?"))
                        run(() -> service.cleanupOldLogs(days));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số ngày không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
        });
        clear.addActionListener(e -> output.setText(""));
    }

    private boolean confirm(String s) {
        return JOptionPane.showConfirmDialog(this, s, "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void run(Task t) {
        try {
            String r = t.run();
            output.append("\n[" + new java.util.Date() + "]\n" + r + "\n");
            output.setCaretPosition(output.getDocument().getLength());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    @FunctionalInterface
    private interface Task {
        String run() throws Exception;
    }
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
}
