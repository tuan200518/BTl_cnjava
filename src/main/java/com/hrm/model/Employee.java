package com.hrm.model;

import java.time.LocalDate;

public record Employee(int id, String code, String fullName, String email, String department, String job, double salary,
        LocalDate hireDate, LocalDate resignationDate, String status, String username) {
}
