package com.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@Column(name = "employee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "name", nullable = false)
	private String name;

	@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
	@Column(name = "contracted_from")
	private String contractedFrom;

	@JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
	@Column(name = "contracted_to")
	private String contractedTo;

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<EmployeeProject> employeeProjects = new ArrayList<>();

	@Transient
	private List<Long> projectIds;

	@Transient
	private List<Date> startDates;

	@Transient
	private List<String> endDates;

	public Employee() {
	}

	public Employee(Long id) {
		this.id = id;
	}

	public Employee(String firstName, String lastName, String name, String contractedFrom, String contractedTo) {
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contractedFrom = contractedFrom;
		this.contractedTo = contractedTo;
	}

	public Employee(Long id, String firstName, String lastName, String name, String contractedFrom, String contractedTo) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.name = name;
		this.contractedFrom = contractedFrom;
		this.contractedTo = contractedTo;
	}

	public Employee(Long id, String firstName, String lastName, String name, String contractedFrom, String contractedTo, List<EmployeeProject> employeeProjects) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.name = name;
		this.contractedFrom = contractedFrom;
		this.contractedTo = contractedTo;
		this.employeeProjects = employeeProjects;
	}

	public Employee(Long id, String firstName, String lastName, String name, List<EmployeeProject> employeeProjects) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.name = name;
		this.employeeProjects = employeeProjects;
	}

	public Employee(Long id, String firstName, String lastName, String name, String contractedFrom, String contractedTo,
					List<EmployeeProject> employeeProjects, List<Long> projectIds, List<Date> startDates,
					List<String> endDates) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.name = name;
		this.contractedFrom = contractedFrom;
		this.contractedTo = contractedTo;
		this.employeeProjects = employeeProjects;
		this.projectIds = projectIds;
		this.startDates = startDates;
		this.endDates = endDates;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String firstName, String lastName) {
		this.name = firstName + " " + lastName;
	}

	public String getContractedFrom() {
		return contractedFrom;
	}

	public void setContractedFrom(String contractedFrom) {
		this.contractedFrom = contractedFrom;
	}

	public String getContractedTo() {
		return contractedTo;
	}

	public void setContractedTo(String contractedTo) {
		this.contractedTo = contractedTo;
	}

	public List<EmployeeProject> getEmployeeProjects() {
		return employeeProjects;
	}

	public void setEmployeeProject(List<EmployeeProject> employeeProjects) {
		this.employeeProjects = employeeProjects;
	}

	public void addEmployeeProjectToEmployee(EmployeeProject employeeProject) {
		this.employeeProjects.add(employeeProject);
	}

	public void addEmployeeProject(EmployeeProject employeeProject) {
		this.employeeProjects.add(employeeProject);
		employeeProject.setEmployee(this);
	}

	public List<Long> getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(List<Long> projectIds) {
		this.projectIds = projectIds;
	}

	public List<Date> getStartDates() {
		return startDates;
	}

	public void setStartDates(List<Date> startDates) {
		this.startDates = startDates;
	}

	public List<String> getEndDates() {
		return endDates;
	}

	public void setEndDates(List<String> endDates) {
		this.endDates = endDates;
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", name='" + name + '\'' +
				", contractedFrom='" + contractedFrom + '\'' +
				", contractedTo='" + contractedTo + '\'' +
				", employeeProjects=" + employeeProjects +
				", projectIds=" + projectIds +
				", startDates=" + startDates +
				", endDates=" + endDates +
				'}';
	}

//	Employee{id=2, name='Katja Kretschmer', contractedFrom='2022-01-01', contractedTo='2022-12-31',
//			employeeProjects=[EmployeeProject{id=3, project=Project{id=4, projectNumber=61341508, name='HVBatCycle', startDate='2022-01-01', endDate='2024-12-31', projectLengthInMonths=36.0, currentBookedMonths=0.0, remainingBookedMonths=0.0, numberOfEmployees=0}, employeeBookedMonths=4.0, employeeProjectStartDate=2022-01-01, employeeProjectEndDate=2022-04-30, projectName='null'},
//		EmployeeProject{id=5, project=Project{id=7, projectNumber=61241514, name='InCa2', startDate='2022-01-01', endDate='2023-03-31', projectLengthInMonths=24.0, currentBookedMonths=0.0, remainingBookedMonths=0.0, numberOfEmployees=0}, employeeBookedMonths=4.5, employeeProjectStartDate=2022-10-01, employeeProjectEndDate=2022-12-31, projectName='null'},
//		EmployeeProject{id=4, project=Project{id=5, projectNumber=61741501, name='MoonLight', startDate='2022-04-01', endDate='2022-09-30', projectLengthInMonths=3.0, currentBookedMonths=0.0, remainingBookedMonths=0.0, numberOfEmployees=0}, employeeBookedMonths=4.0, employeeProjectStartDate=2022-05-01, employeeProjectEndDate=2022-09-15, projectName='null'},
//		EmployeeProject{id=20, project=Project{id=8, projectNumber=61241513, name='SolidSafe', startDate='2022-01-01', endDate='2023-03-31', projectLengthInMonths=14.0, currentBookedMonths=0.0, remainingBookedMonths=0.0, numberOfEmployees=0}, employeeBookedMonths=6.6, employeeProjectStartDate=null, employeeProjectEndDate=null, projectName='null'}],
//		projectIds=[4, 5, 7, 8], startDates=[2022-01-01, 2022-05-01, 2022-10-01, null], endDates=null}
}
