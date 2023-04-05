package com.example.project;

public class EmployeeDeductible extends Employee implements Deductible{
    public EmployeeDeductible(int ID, String firstName, String lastName, String email, String NIN, double grossPay, String empType, int vouchers, int dependents) {
        super(ID, firstName, lastName, email, NIN, grossPay, empType, vouchers, dependents);
    }

    @Override
    public double get_tax() {
        return 0;
    }
}
