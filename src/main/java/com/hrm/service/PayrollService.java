package com.hrm.service;
import com.hrm.dao.PayrollDAO;import com.hrm.model.Payroll;import java.util.List;
public class PayrollService{private final PayrollDAO dao=new PayrollDAO();public List<Payroll>hr(int y)throws Exception{return dao.findForHR(y);}public List<Payroll>mine(int e,int y)throws Exception{return dao.findMine(e,y);}public List<Payroll>history(String q,int y,int m,String s)throws Exception{return dao.history(q,y,m,s);}public void create(int e,int m,int y)throws Exception{dao.createDraft(e,m,y);}public void send(int id,int e)throws Exception{dao.send(id,e);}}
