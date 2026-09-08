package utils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ExcelExporter {
    public static void exportJTableToCSV(JTable table, String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Báo Cáo (Excel/CSV)");
        fileChooser.setSelectedFile(new File(defaultFileName + ".csv"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(fileToSave), StandardCharsets.UTF_8)) {

                // Ghi byte order mark (BOM) để Excel nhận diện chuẩn tiếng Việt UTF-8
                writer.write('\ufeff');

                TableModel model = table.getModel();

                // Ghi Tiêu đề cột
                for (int i = 0; i < model.getColumnCount(); i++) {
                    writer.write("\"" + model.getColumnName(i) + "\"");
                    if (i < model.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");

                // Ghi Dữ liệu các dòng
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object val = model.getValueAt(i, j);
                        writer.write("\"" + (val == null ? "" : val.toString()) + "\"");
                        if (j < model.getColumnCount() - 1) writer.write(",");
                    }
                    writer.write("\n");
                }

                JOptionPane.showMessageDialog(null, "Xuất file thành công!\nĐường dẫn: " + fileToSave.getAbsolutePath(), "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi xuất file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}