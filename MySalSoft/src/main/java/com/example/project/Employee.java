package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Employee implements Nameable{
    private int ID;
    private String firstName;
    private String lastName;
    private String email;
    private double grossPay;
    private String NIN;
    private String empType;
    private int vouchers;
    private int dependents;
    private double netPay;
    private double totalVouchers;
    private List<Salary> salaries_history = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private Map<Project, Double> project_hours = new HashMap<>();

    public Employee(int ID, String firstName, String lastName, String email, String NIN, double grossPay, String empType, int vouchers, int dependents) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.NIN = NIN;
        this.grossPay = grossPay;
        this.empType = empType;
        this.vouchers = vouchers;
        this.dependents = dependents;
    }

    public double compute_salary (int extra_hours, int bonuses, int worked_days) {
        double after_extra = getGrossPay() + get_extra(extra_hours, worked_days);
        double after_cas = after_extra - (25.0/100.0 * getGrossPay());
        double after_cass = after_cas - (10.0/100.0 * after_cas);
        double tax = get_tax();
        double after_tax = after_cass - tax;
        double projects_pay = getProjectsPay();
        double result = after_tax + bonuses + projects_pay;

        return result;
    }

    public double getProjectsPay() {
        double s=0;
        for (Project p : projects)
            s+=p.getProjectPay(this);
        return s;
    }

    public double compute_meal_vouchers_total(int worked_days) {
        if (getVouchers()!=0)
        {
            double t = getVouchers() * worked_days;
            double total = t - (10.0/100.0 * t);
            return total;
        }
        return 0;
    }

    public double get_tax() {
        int d=0;
        if (getGrossPay() <= 1950)
        {
            if (getDependents()==0)
                d = 510;
            if (getDependents()==1)
                d = 670;
            if (getDependents()==2)
                d = 830;
            if (getDependents()==3)
                d = 990;
            if (getDependents()>=4)
                d = 1310;
        }
        double for_tax = getGrossPay() - d;
        double tax = 10.0/100.0 * for_tax;

        return tax;
    }
    public double get_extra(int extra_hours, int worked_days) {
        if (extra_hours>0)
        {
            double hours_per_day;
            if (getEmpType().compareTo("Part-time: 2h")==0)
                hours_per_day = 2;
            else if (getEmpType().compareTo("Part-time: 4h")==0)
                hours_per_day = 4;
            else if (getEmpType().compareTo("Part-time: 6h")==0)
                hours_per_day = 6;
            else
                hours_per_day = 8;
            double price_per_hour = (getGrossPay() / worked_days) / hours_per_day;

            return (175.0/100.0 * price_per_hour) * extra_hours;
        }
        return 0;
    }

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public double getGrossPay() {
        return grossPay;
    }
    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }
    public String getNIN() {
        return NIN;
    }
    public void setNIN(String NIN) {
        this.NIN = NIN;
    }
    public String getEmpType() {
        return empType;
    }
    public void setEmpType(String empType) {
        this.empType = empType;
    }
    public int getVouchers() {
        return vouchers;
    }
    public void setVouchers(int vouchers) {
        this.vouchers = vouchers;
    }
    public int getDependents() {
        return dependents;
    }
    public void setDependents(int dependents) {
        this.dependents = dependents;
    }
    public double getNetPay() {
        return netPay;
    }
    public double getTotalVouchers() {
        return totalVouchers;
    }
    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }
    public void setTotalVouchers(double totalVouchers) {
        this.totalVouchers = totalVouchers;
    }
    public List<Salary> getSalaries_history() {
        return salaries_history;
    }
    public void setSalaries_history(Salary s) {
        salaries_history.add(s);
    }
    public void setSalaries_history(List<Salary> salaries_history) {
        this.salaries_history = salaries_history;
    }
    public List<Project> getProjects() {
        return projects;
    }
    public void setProjects(Project p) {
        projects.add(p);
    }

    public Double getProject_hours(Project p) {
        return project_hours.get(p);
    }

    public void setProject_hours(Project p, Double hours) {
        project_hours.put(p, hours);
    }
}
