<<<<<<< HEAD
package com.hrm.dao; import com.hrm.config.DBConnection; import java.sql.*;
public class DisciplineDAO{public void add(int employeeId,double amount,String reason,java.time.LocalDate date)throws SQLException{if(amount<=0)throw new IllegalArgumentException("Mức phạt phải lớn hơn 0.");try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("INSERT INTO rewards_disciplines(employee_id,type,amount,reason,record_date) VALUES(?,'DISCIPLINE',?,?,?)")){p.setInt(1,employeeId);p.setDouble(2,amount);p.setString(3,reason);p.setDate(4,Date.valueOf(date));p.executeUpdate();}try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("INSERT INTO employment_events(employee_id,event_type,event_date,description) VALUES(?,'DISCIPLINE',?,?)")){p.setInt(1,employeeId);p.setDate(2,Date.valueOf(date));p.setString(3,"Phạt/trừ lương: "+reason+" - "+String.format("%,.0f",amount)+" VNĐ");p.executeUpdate();}}}
=======
package com.hrm.dao;

import com.hrm.config.DBConnection;
import java.sql.*;

public class DisciplineDAO {
    public void add(int employeeId, double amount, String reason, java.time.LocalDate date) throws SQLException {
        if (amount <= 0)
            throw new IllegalArgumentException("Mức phạt phải lớn hơn 0.");
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement(
                        "INSERT INTO rewards_disciplines(employee_id,type,amount,reason,record_date) VALUES(?,'DISCIPLINE',?,?,?)")) {
            p.setInt(1, employeeId);
            p.setDouble(2, amount);
            p.setString(3, reason);
            p.setDate(4, Date.valueOf(date));
            p.executeUpdate();
        }
        try (Connection c = DBConnection.getConnection();
                PreparedStatement p = c.prepareStatement(
                        "INSERT INTO employment_events(employee_id,event_type,event_date,description) VALUES(?,'DISCIPLINE',?,?)")) {
            p.setInt(1, employeeId);
            p.setDate(2, Date.valueOf(date));
            p.setString(3, "Phạt/trừ lương: " + reason + " - " + String.format("%,.0f", amount) + " VNĐ");
            p.executeUpdate();
        }
    }
}
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
