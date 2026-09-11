package com.hrm.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Bộ tiện ích giao diện dùng chung.
 * Chỉ chứa UI, không chứa nghiệp vụ/backend.
 */
public final class Ui {
    private Ui() {
    }

    public static final Color BG = new Color(0xF4F6FA);
    public static final Color SURFACE = Color.WHITE;
    public static final Color PRIMARY = new Color(0x4F46E5);
    public static final Color PRIMARY_DARK = new Color(0x3730A3);
    public static final Color TEXT = new Color(0x172033);
    public static final Color MUTED = new Color(0x6B7280);
    public static final Color BORDER = new Color(0xE2E6EF);
    public static final Color SUCCESS = new Color(0x16A34A);
    public static final Color WARNING = new Color(0xD97706);
    public static final Color DANGER = new Color(0xDC2626);

    private static boolean initialized;

    public static void init() {
        if (initialized)
            return;
        initialized = true;

        UIManager.put("Panel.background", BG);
        UIManager.put("Viewport.background", BG);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", TEXT);
        UIManager.put("Table.selectionBackground", new Color(0xE8E9FF));
        UIManager.put("Table.selectionForeground", TEXT);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.background", new Color(0xF8F9FC));
        UIManager.put("TableHeader.foreground", TEXT);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", TEXT);
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", TEXT);
        UIManager.put("OptionPane.background", BG);
        UIManager.put("ScrollPane.background", BG);
        UIManager.put("TextField.border", new CompoundBorder(
                new LineBorder(BORDER), new EmptyBorder(7, 10, 7, 10)));
        UIManager.put("PasswordField.border", new CompoundBorder(
                new LineBorder(BORDER), new EmptyBorder(7, 10, 7, 10)));
        UIManager.put("ComboBox.border", new LineBorder(BORDER));
        UIManager.put("Button.border", new EmptyBorder(9, 16, 9, 16));
        UIManager.put("TabbedPane.background", BG);

        Font base = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Label.font", base);
        UIManager.put("TextField.font", base);
        UIManager.put("PasswordField.font", base);
        UIManager.put("ComboBox.font", base);
        UIManager.put("Button.font", base);
        UIManager.put("Table.font", base);
        UIManager.put("TableHeader.font", base.deriveFont(Font.BOLD, 13f));
    }

    public static JButton btn(String text) {
        JButton b = new JButton(text);
        styleButton(b, ButtonKind.SECONDARY);
        return b;
    }

    public static JButton primaryBtn(String text) {
        JButton b = new JButton(text);
        styleButton(b, ButtonKind.PRIMARY);
        return b;
    }

    public static JButton dangerBtn(String text) {
        JButton b = new JButton(text);
        styleButton(b, ButtonKind.DANGER);
        return b;
    }

    private enum ButtonKind {
        PRIMARY, SECONDARY, DANGER
    }

    /**
     * Nút dùng chung: kích thước lớn hơn, chữ đậm, viền rõ và có hiệu ứng hover.
     */
    private static void styleButton(JButton b, ButtonKind kind) {
        b.setFocusPainted(false);
        b.setBorderPainted(true);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(150, 40));
        b.setMinimumSize(new Dimension(100, 40));
        b.setBorder(new CompoundBorder(
                new LineBorder(buttonBorder(kind), 1, true),
                new EmptyBorder(8, 15, 8, 15)));
        b.setBackground(buttonBackground(kind, false));
        b.setForeground(buttonForeground(kind));

        final Color normal = buttonBackground(kind, false);
        final Color hover = buttonBackground(kind, true);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (b.isEnabled())
                    b.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (b.isEnabled())
                    b.setBackground(normal);
            }
        });

        b.addPropertyChangeListener("enabled", e -> {
            if (!b.isEnabled()) {
                b.setBackground(new Color(0xE5E7EB));
                b.setForeground(new Color(0x9CA3AF));
            } else {
                b.setBackground(normal);
                b.setForeground(buttonForeground(kind));
            }
        });
    }

    private static Color buttonBackground(ButtonKind kind, boolean hover) {
        if (kind == ButtonKind.PRIMARY)
            return hover ? PRIMARY_DARK : PRIMARY;
        if (kind == ButtonKind.DANGER)
            return hover ? new Color(0xFEE2E2) : new Color(0xFFF5F5);
        return hover ? new Color(0xEEF0FF) : new Color(0xF8F9FD);
    }

    private static Color buttonForeground(ButtonKind kind) {
        if (kind == ButtonKind.PRIMARY)
            return Color.WHITE;
        if (kind == ButtonKind.DANGER)
            return DANGER;
        return TEXT;
    }

    private static Color buttonBorder(ButtonKind kind) {
        if (kind == ButtonKind.PRIMARY)
            return PRIMARY;
        if (kind == ButtonKind.DANGER)
            return new Color(0xFCA5A5);
        return new Color(0xC7CBF8);
    }

    public static JPanel top(String title) {
        return top(title, "");
    }

    public static JPanel top(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(SURFACE);
        p.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(18, 22, 16, 22)));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(TEXT);
        text.add(l);

        if (subtitle != null && !subtitle.isBlank()) {
            JLabel s = new JLabel(subtitle);
            s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            s.setForeground(MUTED);
            s.setBorder(new EmptyBorder(5, 0, 0, 0));
            text.add(s);
        }

        p.add(text, BorderLayout.WEST);
        return p;
    }

    public static JPanel stack(Component... components) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        for (Component c : components) {
            p.add(c);
        }
        return p;
    }

    /**
     * Thanh công cụ tự xuống dòng khi cửa sổ hẹp.
     * Khắc phục tình trạng các nút bị đè lên JTable/danh sách
     * khi có nhiều nút trên cùng một hàng.
     */
    public static JPanel toolbar(Component... components) {
        JPanel p = new JPanel(new WrapLayout(FlowLayout.LEFT, 10, 8));
        p.setBackground(SURFACE);
        p.setBorder(new EmptyBorder(6, 14, 10, 14));
        for (Component c : components) {
            p.add(c);
        }
        return p;
    }

    /**
     * FlowLayout có chiều cao preferredSize tính đúng số dòng.
     * FlowLayout mặc định chỉ tính chiều cao của 1 dòng, nên khi
     * cửa sổ bị thu nhỏ các nút xuống dòng nhưng component cha
     * không tăng chiều cao -> JTable/list có thể bị đè lên toolbar.
     */
    private static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            Dimension d = layoutSize(target, false);
            d.width -= (getHgap() + 1);
            return d;
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getWidth();

                if (targetWidth <= 0) {
                    targetWidth = Integer.MAX_VALUE;
                }

                Insets insets = target.getInsets();
                int maxWidth = targetWidth
                        - insets.left
                        - insets.right
                        - getHgap() * 2;

                int x = 0;
                int rowHeight = 0;
                int rows = 1;
                int width = 0;

                Component[] comps = target.getComponents();

                for (Component comp : comps) {
                    if (!comp.isVisible())
                        continue;

                    Dimension d = preferred
                            ? comp.getPreferredSize()
                            : comp.getMinimumSize();

                    if (x > 0 && x + d.width > maxWidth) {
                        width = Math.max(width, x);
                        x = 0;
                        rowHeight = 0;
                        rows++;
                    }

                    if (x > 0) {
                        x += getHgap();
                    }

                    x += d.width;
                    rowHeight = Math.max(rowHeight, d.height);
                }

                width = Math.max(width, x);

                int height = rows * rowHeight
                        + Math.max(0, rows - 1) * getVgap()
                        + insets.top
                        + insets.bottom;

                return new Dimension(
                        width + insets.left + insets.right,
                        height);
            }
        }
    }

    public static JPanel section(String title) {
        JPanel p = new JPanel(new BorderLayout(8, 12));
        p.setBackground(SURFACE);
        p.setBorder(new CompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(16, 18, 16, 18)));
        p.add(new JLabel(title), BorderLayout.NORTH);
        return p;
    }

    public static JLabel muted(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(MUTED);
        return l;
    }

    public static JTextField field(int columns) {
        JTextField f = new JTextField(columns);
        f.setBorder(new CompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(7, 10, 7, 10)));
        return f;
    }

    public static void styleCombo(JComboBox<?> combo) {
        combo.setBorder(new LineBorder(BORDER));
        combo.setBackground(Color.WHITE);
    }

    public static JTable table(String[] headers) {
        JTable t = new JTable(new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });
        styleTable(t);
        return t;
    }

    public static void styleTable(JTable t) {
        t.setRowHeight(34);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setFillsViewportHeight(true);
        t.setAutoCreateRowSorter(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        t.getTableHeader().setPreferredSize(new Dimension(0, 38));
        t.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean selected,
                    boolean focused, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, selected, focused, row, column);

                setBorder(new EmptyBorder(0, 10, 0, 10));
                if (!selected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xFAFBFD));
                }
                return c;
            }
        };

        t.setDefaultRenderer(Object.class, renderer);
    }

    public static JScrollPane scroll(Component c) {
        JScrollPane s = new JScrollPane(c);
        s.setBorder(new CompoundBorder(
                new EmptyBorder(4, 14, 14, 14),
                new LineBorder(BORDER)));
        s.getViewport().setBackground(Color.WHITE);
        s.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        s.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return s;
    }

    public static void fill(JTable t, List<String[]> rows) {
        DefaultTableModel m = (DefaultTableModel) t.getModel();
        m.setRowCount(0);
        for (String[] r : rows)
            m.addRow(r);
    }

    public static void error(Component c, Exception e) {
        String msg = e == null || e.getMessage() == null
                ? "Có lỗi xảy ra."
                : e.getMessage();
        JOptionPane.showMessageDialog(c, msg, "Thông báo lỗi",
                JOptionPane.ERROR_MESSAGE);
    }

    public static JPanel card(Component... children) {
        JPanel p = new JPanel();
        p.setBackground(SURFACE);
        p.setBorder(new CompoundBorder(
                new LineBorder(BORDER),
                new EmptyBorder(16, 18, 16, 18)));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        for (Component c : children) {
            p.add(c);
            p.add(Box.createVerticalStrut(6));
        }
        return p;
    }

    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(TEXT);
        return l;
    }

    public static void styleDialog(JDialog d) {
        d.getContentPane().setBackground(BG);
        d.setResizable(true);
    }
}
