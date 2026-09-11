package com.hrm.service;
<<<<<<< HEAD
import com.hrm.dao.ProjectDAO;
import java.time.LocalDate;
public class ProjectService{private final ProjectDAO dao=new ProjectDAO();public void create(String n,int r,LocalDate d,String s)throws Exception{dao.create(n,r,d,s);}}
=======

import com.hrm.dao.ProjectDAO;
import java.time.LocalDate;

public class ProjectService {
    private final ProjectDAO dao = new ProjectDAO();

    public void create(String n, int r, LocalDate d, String s) throws Exception {
        dao.create(n, r, d, s);
    }
}
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
