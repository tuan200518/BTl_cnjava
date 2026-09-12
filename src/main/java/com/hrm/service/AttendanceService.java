package com.hrm.service;

import com.hrm.dao.AttendanceDAO;
import java.util.List;

public class AttendanceService {
    private final AttendanceDAO dao = new AttendanceDAO();

    public List<String[]> find(int y) throws Exception {
        return dao.find(y);
    }

    public List<String[]> find(int y, String q) throws Exception {
        return dao.find(y, q);
    }
}
