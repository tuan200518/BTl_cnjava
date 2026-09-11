package com.hrm.model;
public record UserSession(int userId, Integer employeeId, String username, String role) {
    public boolean isAdmin(){ return "ADMIN".equals(role); }
    public boolean isHR(){ return "HR".equals(role); }
    public boolean isEmployee(){ return "EMPLOYEE".equals(role); }
}
