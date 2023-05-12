package com.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "employee_projects")
public class EmployeeProject implements Serializable {

    @Id
    @Column(name = "employee_project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "employee_booked_months")
    private double employeeBookedMonths;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="dd-MMM-YYYY")
    @Column(name = "employee_project_start_date")
    private String employeeProjectStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="dd-MMM-YYYY")
    @Column(name = "employee_project_end_date")
    private String employeeProjectEndDate;

    @Column(name = "project_name")
    private String employeeProjectName;

    @Column(name = "confirm_booking", nullable = false)
    private boolean confirmBooking = false;

    public EmployeeProject() {}

    public EmployeeProject(Long id) {
        this.id = id;
    }

    public EmployeeProject(Employee employee, Project project, double employeeBookedMonths) {
        this.employee = employee;
        this.project = project;
        this.employeeBookedMonths = employeeBookedMonths;
    }

    public EmployeeProject(Employee employee) {
        this.employee = employee;
    }

    public EmployeeProject(Project project) {
        this.project = project;
    }

    public EmployeeProject(Employee employee, Project project) {
        this.employee = employee;
        this.project = project;
    }

    public EmployeeProject(Employee employee,double employeeBookedMonths) {
        this.employee = employee;
        this.employeeBookedMonths = employeeBookedMonths;
    }

    public EmployeeProject(Employee employee, Project project, String employeeProjectStartDate,
                           String employeeProjectEndDate, String employeeProjectName, boolean confirmBooking) {
        this.employee = employee;
        this.project = project;
        this.employeeProjectStartDate = employeeProjectStartDate;
        this.employeeProjectEndDate = employeeProjectEndDate;
        this.employeeProjectName = employeeProjectName;
        this.confirmBooking = confirmBooking;
    }

    public EmployeeProject(Long id, Employee employee, Project project, double employeeBookedMonths) {
        this.id = id;
        this.employee = employee;
        this.project = project;
        this.employeeBookedMonths = employeeBookedMonths;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public double getEmployeeBookedMonths() {
        return employeeBookedMonths;
    }

    public void setEmployeeBookedMonths(double employeeBookedMonths) {
        this.employeeBookedMonths = employeeBookedMonths;
    }

    public String getEmployeeProjectStartDate() {
        return employeeProjectStartDate;
    }

    public void setEmployeeProjectStartDate(String employeeProjectStartDate) {
        this.employeeProjectStartDate = employeeProjectStartDate;
    }

    public String getEmployeeProjectEndDate() {
        return employeeProjectEndDate;
    }

    public void setEmployeeProjectEndDate(String employeeProjectEndDate) {
        this.employeeProjectEndDate = employeeProjectEndDate;
    }

    public String getEmployeeProjectName() {
        return employeeProjectName;
    }

    public void setEmployeeProjectName(String employeeProjectName) {
        this.employeeProjectName = employeeProjectName;
    }

    public boolean getConfirmBooking() {
        return confirmBooking;
    }

    public void setConfirmBooking(boolean confirmBooking) {
        this.confirmBooking = confirmBooking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeProject)) return false;
        EmployeeProject that = (EmployeeProject) o;
        return Double.compare(that.getEmployeeBookedMonths(), getEmployeeBookedMonths()) == 0 && Objects.equals(getId(), that.getId()) && Objects.equals(getEmployee(), that.getEmployee()) && Objects.equals(getProject(), that.getProject()) && Objects.equals(getEmployeeProjectStartDate(), that.getEmployeeProjectStartDate()) && Objects.equals(getEmployeeProjectEndDate(), that.getEmployeeProjectEndDate()) && Objects.equals(getEmployeeProjectName(), that.getEmployeeProjectName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmployee(), getProject(), getEmployeeBookedMonths(), getEmployeeProjectStartDate(), getEmployeeProjectEndDate(), getEmployeeProjectName());
    }

    //Causes 500 error (don't know why)


    @Override
    public String toString() {
        return "EmployeeProject{" +
                "id=" + id +
//                ", employee=" + employee +
                ", project=" + project +
                ", employeeBookedMonths=" + employeeBookedMonths +
                ", employeeProjectStartDate='" + employeeProjectStartDate + '\'' +
                ", employeeProjectEndDate='" + employeeProjectEndDate + '\'' +
                ", employeeProjectName='" + employeeProjectName + '\'' +
//                ", confirmBooking=" + confirmBooking +
                '}';
    }
}
