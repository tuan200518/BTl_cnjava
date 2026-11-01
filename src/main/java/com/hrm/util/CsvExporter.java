package com.hrm.util;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;

/** Xuất JTable ra CSV UTF-8 có BOM để Excel đọc tiếng Việt đúng. */
public final class CsvExporter {
    private CsvExporter() {}
    public static void export(ComponentParent parent, JTable table, String suggestedName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(suggestedName));
        if (chooser.showSaveDialog(parent.component()) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) file = new File(file.getParentFile(), file.getName()+".csv");
        try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            w.write('\ufeff');
            TableModel m = table.getModel();
            for (int c=0;c<m.getColumnCount();c++) { if(c>0) w.write(','); w.write(quote(m.getColumnName(c))); }
            w.write("\r\n");
            for (int r=0;r<m.getRowCount();r++) {
                for (int c=0;c<m.getColumnCount();c++) { if(c>0) w.write(','); Object v=m.getValueAt(r,c); w.write(quote(v==null?"":v.toString())); }
                w.write("\r\n");
            }
            JOptionPane.showMessageDialog(parent.component(), "Đã xuất báo cáo: " + file.getAbsolutePath());
        } catch (IOException ex) { JOptionPane.showMessageDialog(parent.component(), "Không thể xuất tệp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE); }
    }
    private static String quote(String s) { return "\"" + s.replace("\"", "\"\"") + "\""; }
    public interface ComponentParent { java.awt.Component component(); }
    public static void export(java.awt.Component parent, JTable table, String name) {
        export(new ComponentParent(){ public java.awt.Component component(){return parent;} }, table, name);
    }
}
