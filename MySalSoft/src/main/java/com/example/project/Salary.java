package com.example.project;

public class Salary {
    private int year;
    private String month;
    private double salary;
    private double m_vouchers;

    public Salary(int year, String month, double salary, double m_vouchers) {
        this.year = year;
        this.month = month;
        this.salary = salary;
        this.m_vouchers = m_vouchers;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }
    public double getM_vouchers() {
        return m_vouchers;
    }
    public void setM_vouchers(double m_vouchers) {
        this.m_vouchers = m_vouchers;
    }
}
