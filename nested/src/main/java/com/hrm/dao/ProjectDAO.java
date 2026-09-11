package com.hrm.dao;
import com.hrm.config.DBConnection;
import java.sql.*;
import java.time.LocalDate;
public class ProjectDAO {
 public void create(String name,int required,LocalDate start,String status)throws SQLException{
  if(name==null||name.isBlank())throw new SQLException("Tên dự án không được để trống.");
  if(required<=0)throw new SQLException("Số nhân viên cần tuyển phải lớn hơn 0.");
  String q="INSERT INTO projects(project_name,required_headcount,start_date,status) VALUES(?,?,?,?)";
  try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setString(1,name.trim());p.setInt(2,required);p.setDate(3,Date.valueOf(start));p.setString(4,status);p.executeUpdate();}
 }
}
