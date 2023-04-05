package com.example.project;

public class Project implements Nameable {
    private int project_id;
    private String lastName;
    private double pay;

    public Project(int project_id, String lastName, double pay) {
        this.lastName = lastName;
        this.project_id = project_id;
        this.pay = pay;
    }
    public double getProjectPay (Employee e) {
        double t=0;
        if (e instanceof Deductible)
            t = (e.getProject_hours(this) * pay);
        else
            t = 90.0/100.0 * (e.getProject_hours(this) * pay);
        return t;
    }
    @Override
    public String getLastName() {
        return lastName;
    }
    @Override
    public void setLastName(String name) {
        this.lastName = name;
    }
    public int getProject_id() {
        return project_id;
    }
    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
    public double getPay() {
        return pay;
    }
    public void setPay(double pay) {
        this.pay = pay;
    }
}
