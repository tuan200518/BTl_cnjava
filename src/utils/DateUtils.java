package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Tiện ích format ngày tháng
 */
public class DateUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatterISO = DateTimeFormatter.ISO_LOCAL_DATE;
    
    /**
     * Convert String to LocalDate (format: dd/MM/yyyy)
     */
    public static LocalDate stringToDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Convert LocalDate to String (format: dd/MM/yyyy)
     */
    public static String dateToString(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(formatter);
    }
    
    /**
     * Format LocalDate to ISO format (yyyy-MM-dd)
     */
    public static String dateToISO(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(formatterISO);
    }
    
    /**
     * Get current date
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * Get current date as string
     */
    public static String getCurrentDateString() {
        return dateToString(LocalDate.now());
    }
    
    /**
     * Calculate days difference between two dates
     */
    public static long daysDifference(LocalDate date1, LocalDate date2) {
        return java.time.temporal.ChronoUnit.DAYS.between(date1, date2);
    }
}
