package utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Tiện ích định dạng và xử lý tiền tệ Việt Nam.
 * Giá trị trong CSDL vẫn là số (VND), chỉ thay đổi cách hiển thị trên giao diện.
 */
public final class CurrencyUtils {
    private static final DecimalFormat VND_FORMAT;
    private static final DecimalFormat NUMBER_FORMAT;

    static {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(new Locale("vi", "VN"));
        VND_FORMAT = new DecimalFormat("#,##0", symbols);
        VND_FORMAT.setGroupingUsed(true);
        VND_FORMAT.setMaximumFractionDigits(0);

        NUMBER_FORMAT = new DecimalFormat("#,##0", symbols);
        NUMBER_FORMAT.setGroupingUsed(true);
        NUMBER_FORMAT.setMaximumFractionDigits(0);
    }

    private CurrencyUtils() {
    }

    /**
     * Hiển thị tiền theo chuẩn dễ đọc của Việt Nam, ví dụ: 18.000.000 VNĐ.
     */
    public static synchronized String formatVND(double amount) {
        return VND_FORMAT.format(amount) + " VNĐ";
    }

    /**
     * Hiển thị số có dấu phân cách hàng nghìn, không kèm đơn vị.
     */
    public static synchronized String formatNumber(double amount) {
        return NUMBER_FORMAT.format(amount);
    }

    /**
     * Alias tương thích với code cũ.
     */
    public static String formatMoneyVN(double amount) {
        return formatVND(amount);
    }

    /**
     * Chuyển chuỗi tiền VND về số.
     * Hỗ trợ cả 18.000.000 VNĐ, 18,000,000 và 18000000.
     */
    public static double parseVND(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }

        String cleaned = value.trim()
                .replace("VNĐ", "")
                .replace("VND", "")
                .replace("₫", "")
                .replace("đ", "")
                .replace("Đ", "")
                .replace(" ", "")
                .replace(".", "")
                .replace(",", "");

        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số tiền không hợp lệ: " + value, e);
        }
    }

    public static double roundMoney(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }
}
