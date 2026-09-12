package com.hrm.model;

public record Payroll(int id, int employeeId, String employeeCode, String employeeName, int month, int year,
        double base, double overtime, double bonus, double deduction, double net, String status, int dependents) {
}
