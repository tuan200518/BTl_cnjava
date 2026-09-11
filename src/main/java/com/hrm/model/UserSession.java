package com.hrm.model;
<<<<<<< HEAD
public record UserSession(int userId, Integer employeeId, String username, String role) {
    public boolean isAdmin(){ return "ADMIN".equals(role); }
    public boolean isHR(){ return "HR".equals(role); }
    public boolean isAccountant(){ return "ACCOUNTANT".equals(role); }
    public boolean isEmployee(){ return "EMPLOYEE".equals(role); }
=======

public record UserSession(int userId, Integer employeeId, String username, String role) {
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public boolean isHR() {
        return "HR".equals(role);
    }

    public boolean isAccountant() {
        return "ACCOUNTANT".equals(role);
    }

    public boolean isEmployee() {
        return "EMPLOYEE".equals(role);
    }
>>>>>>> c888654e27fb43d1b0002d042a50270e736e50b5
}
