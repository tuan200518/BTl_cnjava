package com.hrm.service;
<<<<<<< HEAD
import com.hrm.dao.PayrollDAO;import com.hrm.model.Payroll;import java.util.List;
public class PayrollService{private final PayrollDAO dao=new PayrollDAO();public List<Payroll>hr(int y)throws Exception{return dao.findForHR(y);}public List<Payroll>mine(int e,int y)throws Exception{return dao.findMine(e,y);}public List<Payroll>history(String q,int y,int m,String s)throws Exception{return dao.history(q,y,m,s);}public java.util.List<com.hrm.model.Employee> employeesForPayroll(String q,int y,int m,String d,String j)throws Exception{return dao.findEmployeesForPayroll(q,y,m,d,j);}
 public void create(int e,int m,int y)throws Exception{dao.createDraft(e,m,y);}public void send(int id,int e)throws Exception{dao.send(id,e);}public int dependentCount(int e)throws Exception{return dao.dependentCount(e);}}
=======

import com.hrm.dao.PayrollDAO;
import com.hrm.model.Payroll;
import java.util.List;

public class PayrollService {
    private final PayrollDAO dao = new PayrollDAO();

    public List<Payroll> hr(int y) throws Exception {
        return dao.findForHR(y);
    }

    public List<Payroll> mine(int e, int y) throws Exception {
        return dao.findMine(e, y);
    }

    public List<Payroll> history(String q, int y, int m, String s) throws Exception {
        return dao.history(q, y, m, s);
    }

    public java.util.List<com.hrm.model.Employee> employeesForPayroll(String q, int y, int m, String d, String j)
            throws Exception {
        return dao.findEmployeesForPayroll(q, y, m, d, j);
    }

    public void create(int e, int m, int y) throws Exception {
        dao.createDraft(e, m, y);
    }

    public void send(int id, int e) throws Exception {
        dao.send(id, e);
    }

    public int dependentCount(int e) throws Exception {
        return dao.dependentCount(e);
    }
}
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
