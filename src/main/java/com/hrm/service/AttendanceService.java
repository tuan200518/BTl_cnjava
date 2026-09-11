<<<<<<< HEAD
package com.hrm.service;import com.hrm.dao.AttendanceDAO;import java.util.List;public class AttendanceService{private final AttendanceDAO dao=new AttendanceDAO();public List<String[]>find(int y)throws Exception{return dao.find(y);}public List<String[]>find(int y,String q)throws Exception{return dao.find(y,q);}}
=======
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
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
