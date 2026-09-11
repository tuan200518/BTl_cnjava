package com.hrm.gui;

import com.hrm.model.Payroll;
import com.hrm.service.PayrollCalculator;
import com.hrm.service.PayrollService;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/** Phiếu lương chi tiết theo bố cục gần giống mẫu người dùng cung cấp. */
public class PayslipDetailDialog extends JDialog {
    public PayslipDetailDialog(Window owner, Payroll p) {
        super(owner, "Phiếu lương chi tiết - " + p.employeeCode(), ModalityType.APPLICATION_MODAL);
        setSize(820, 860); setLocationRelativeTo(owner); setLayout(new BorderLayout(8,8));

        JPanel header = new JPanel(new BorderLayout(8,4));
        header.setBorder(BorderFactory.createEmptyBorder(14,16,8,16));
        JLabel title = new JLabel("PHIẾU LƯƠNG CHI TIẾT");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel info = new JLabel(p.employeeCode()+" - "+p.employeeName()+"   |   Kỳ lương: "+p.month()+"/"+p.year()+"   |   "+p.status());
        info.setFont(info.getFont().deriveFont(Font.PLAIN, 14f));
        header.add(title, BorderLayout.NORTH); header.add(info, BorderLayout.SOUTH); add(header, BorderLayout.NORTH);

        try {
            int dependents = new PayrollService().dependentCount(p.employeeId());
            double gross = p.base()+p.overtime()+p.bonus();
            double insuranceSalary = p.base();
            double insurance = PayrollCalculator.insurance(insuranceSalary);
            double personal = p.year() >= 2026 ? PayrollCalculator.PERSONAL_DEDUCTION_2026 : PayrollCalculator.PERSONAL_DEDUCTION_OLD;
            double dependentUnit = p.year() >= 2026 ? PayrollCalculator.DEPENDENT_DEDUCTION_2026 : PayrollCalculator.DEPENDENT_DEDUCTION_OLD;
            double dependent = dependents * dependentUnit;
            double taxable = Math.max(0, gross - insurance - personal - dependent);
            double pit = PayrollCalculator.pit(taxable, p.year());
            double totalDeduction = insurance + personal + dependent;
            double net = PayrollCalculator.net(gross, insurance, pit, p.deduction());

            String[][] rows = {
                {"Lương cơ bản chính thức", money(p.base())},
                {"Tổng lương theo ngày công thực tế", money(p.base())},
                {"Lương ngày chưa nghỉ phép (TH đã nghỉ)", "0"},
                {"Phụ cấp ăn trưa", "0"},
                {"Phụ cấp gửi xe", "0"},
                {"Sinh nhật", "0"},
                {"Tổng phụ cấp", "0"},
                {"Tiền làm thêm giờ (OT)", money(p.overtime())},
                {"Thưởng hiệu suất", money(p.bonus())},
                {"Tổng thu nhập hiệu quả", money(p.overtime()+p.bonus())},
                {"TỔNG THU NHẬP", money(gross)},
                {"Mức lương đóng BHXH", money(insuranceSalary)},
                {"Giảm trừ bảo hiểm bắt buộc (10,5%)", money(insurance)},
                {"Giảm trừ bản thân", money(personal)},
                {"Giảm trừ NPT ("+dependents+" người × "+String.format("%,.0f", dependentUnit)+")", money(dependent)},
                {"Các khoản không chịu thuế", "0"},
                {"Tổng giảm trừ", money(totalDeduction)},
                {"THU NHẬP TÍNH THUẾ", money(taxable)},
                {"THUẾ TNCN", money(pit)},
                {"Phạt / trừ lương", money(p.deduction())},
                {"THỰC LĨNH", money(net)}
            };
            JTable table=Ui.table(new String[]{"Khoản mục","Số tiền"});
            Ui.fill(table, Arrays.asList(rows));
            table.getColumnModel().getColumn(0).setPreferredWidth(540); table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.setRowHeight(31); table.setFont(table.getFont().deriveFont(14f));
            add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel bottom=new JPanel(new BorderLayout());
            JLabel note=new JLabel("BH bắt buộc = 10,5% lương đóng BHXH; PIT tính theo biểu thuế lũy tiến của từng kỳ (2026 dùng 5 bậc).");
            note.setBorder(BorderFactory.createEmptyBorder(4,12,4,12)); bottom.add(note,BorderLayout.CENTER);
            JButton close=Ui.btn("Đóng"); JPanel bp=new JPanel(new FlowLayout(FlowLayout.RIGHT)); bp.add(close); bottom.add(bp,BorderLayout.EAST); close.addActionListener(e->dispose()); add(bottom,BorderLayout.SOUTH);
        } catch(Exception ex) { Ui.error(this,ex); dispose(); }
    }
    private String money(double d){return String.format("%,.0f",d);}
}
