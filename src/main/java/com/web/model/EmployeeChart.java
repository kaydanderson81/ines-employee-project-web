package com.web.model;

import java.util.List;

public class EmployeeChart {

    private String name;
    private List<Project> projects;
    private double employeeProjectMonths;

    public EmployeeChart(String name, List<Project> projects, double employeeProjectMonths) {
        this.name = name;
        this.projects = projects;
        this.employeeProjectMonths = employeeProjectMonths;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public double getEmployeeProjectMonths() {
        return employeeProjectMonths;
    }

    public void setEmployeeProjectMonths(double employeeProjectMonths) {
        this.employeeProjectMonths = employeeProjectMonths;
    }

    @Override
    public String toString() {
        return "EmployeeChart{" +
                "name='" + name + '\'' +
                ", projects=" + projects +
                ", employeeProjectMonths=" + employeeProjectMonths +
                '}';
    }
}
