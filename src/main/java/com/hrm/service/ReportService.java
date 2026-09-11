package com.hrm.service;
<<<<<<< HEAD
import com.hrm.dao.ReportDAO;
public class ReportService{
 private final ReportDAO dao=new ReportDAO();
 public java.util.List<String[]>staffing()throws Exception{return dao.staffing();}
 public java.util.List<String[]>projects()throws Exception{return dao.projects();}
 public int count(String t)throws Exception{return dao.count(t);}
 public double totalPaid(int y)throws Exception{return dao.totalPaid(y);}
 public java.util.List<String[]>employeesByJob(String j)throws Exception{return dao.employeesByJob(j);}
 public java.util.List<String[]>employeesByJob(String j,String q)throws Exception{return dao.employeesByJob(j,q);}
 public java.util.List<String[]>employeesByProject(String p)throws Exception{return dao.employeesByProject(p);}
 public java.util.List<String[]>employeesByProject(String p,String q)throws Exception{return dao.employeesByProject(p,q);}
=======

import com.hrm.dao.ReportDAO;

public class ReportService {
    private final ReportDAO dao = new ReportDAO();

    public java.util.List<String[]> staffing() throws Exception {
        return dao.staffing();
    }

    public java.util.List<String[]> projects() throws Exception {
        return dao.projects();
    }

    public int count(String t) throws Exception {
        return dao.count(t);
    }

    public double totalPaid(int y) throws Exception {
        return dao.totalPaid(y);
    }

    public java.util.List<String[]> employeesByJob(String j) throws Exception {
        return dao.employeesByJob(j);
    }

    public java.util.List<String[]> employeesByJob(String j, String q) throws Exception {
        return dao.employeesByJob(j, q);
    }

    public java.util.List<String[]> employeesByProject(String p) throws Exception {
        return dao.employeesByProject(p);
    }

    public java.util.List<String[]> employeesByProject(String p, String q) throws Exception {
        return dao.employeesByProject(p, q);
    }
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
}
