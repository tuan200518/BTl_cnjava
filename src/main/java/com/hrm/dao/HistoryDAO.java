package com.hrm.dao;

import com.hrm.config.DBConnection;
import java.sql.*;
import java.util.*;

public class HistoryDAO {
    public List<String[]> find(int year, String text) throws SQLException {
        List<String[]> a = new ArrayList<>();
        String q = "SELECT CONCAT('NV',LPAD(h.employee_id,4,'0')),e.full_name,h.event_type,h.event_date,h.description FROM employment_events h JOIN employees e ON e.employee_id=h.employee_id WHERE (?=0 OR YEAR(h.event_date)=?) AND (?='' OR e.full_name LIKE ? OR CONCAT('NV',LPAD(h.employee_id,4,'0')) LIKE ?) ORDER BY h.event_date DESC,h.employee_id";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(q)) {
            p.setInt(1, year);
            p.setInt(2, year);
            p.setString(3, text);
            p.setString(4, "%" + text + "%");
            p.setString(5, "%" + text + "%");
            try (ResultSet r = p.executeQuery()) {
                while (r.next())
                    a.add(new String[] { r.getString(1), r.getString(2), r.getString(3), r.getDate(4).toString(),
                            r.getString(5) });
            }
        }
        return a;
    }

    public List<String[]> find(int year) throws SQLException {
        return find(year, "");
    }
}
