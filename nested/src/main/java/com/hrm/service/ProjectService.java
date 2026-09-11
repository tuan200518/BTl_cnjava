package com.hrm.service;
import com.hrm.dao.ProjectDAO;
import java.time.LocalDate;
public class ProjectService{private final ProjectDAO dao=new ProjectDAO();public void create(String n,int r,LocalDate d,String s)throws Exception{dao.create(n,r,d,s);}}
