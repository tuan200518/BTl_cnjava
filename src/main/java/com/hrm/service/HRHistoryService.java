package com.hrm.service;

import com.hrm.dao.HistoryDAO;
import java.util.List;

public class HRHistoryService {
    private final HistoryDAO dao = new HistoryDAO();

    public List<String[]> find(int y) throws Exception {
        return dao.find(y);
    }

    public List<String[]> find(int y, String q) throws Exception {
        return dao.find(y, q);
    }
}
