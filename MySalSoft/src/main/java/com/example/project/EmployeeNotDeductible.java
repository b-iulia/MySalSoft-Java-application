package com.example.project;

public class EmployeeNotDeductible extends Employee {

    public EmployeeNotDeductible(int ID, String firstName, String lastName, String email, String NIN, double grossPay, String empType, int vouchers, int dependents) {
        super(ID, firstName, lastName, email, NIN, grossPay, empType, vouchers, dependents);
    }
}
