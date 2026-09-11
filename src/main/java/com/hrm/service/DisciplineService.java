package com.hrm.service;

public class DisciplineService {
    private final com.hrm.dao.DisciplineDAO dao = new com.hrm.dao.DisciplineDAO();

    public void add(int e, double a, String r, java.time.LocalDate d) throws Exception {
        dao.add(e, a, r, d);
    }
}
