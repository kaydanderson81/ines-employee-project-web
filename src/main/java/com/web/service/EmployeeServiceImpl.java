package com.web.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.web.model.ChartYear;
import com.web.model.Employee;
import com.web.model.EmployeeProject;
import com.web.model.Project;
import com.web.repository.EmployeeProjectRepository;
import com.web.repository.ProjectRepository;
import com.web.service.employeeProject.EmployeeProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private EmployeeProjectRepository employeeProjectRepository;

	@Autowired
	private EmployeeProjectService employeeProjectService;


	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee saveEmployee(Employee employee) {
		employee.setName(employee.getFirstName(), employee.getLastName());
		return this.employeeRepository.save(employee);
    }

	@Override
	public Employee getEmployeeById(long id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		Employee employee = null;
		if (optional.isPresent()) {
			employee = optional.get();
		} else {
			throw new RuntimeException(" Employee not found for id :: " + id);
		}
		return employee;
	}

	@Override
	public void deleteEmployeeById(long id) {
		this.employeeRepository.deleteById(id);
	}

	@Override
	public List<Project> getAllProjects() {
		return this.projectRepository.findAll();
	}

	@Override
	public void saveEmployeeProjectToEmployee(
		Employee employee, List<Project> projectIds, List<Double> monthList,
		List<String> employeeProjectStartDate, List<String> employeeProjectEndDate) {
		saveEmployee(employee);
		for (int i=0; i< projectIds.size(); i++) {
			EmployeeProject employeeProject = new EmployeeProject(employee);
			employeeProject.setEmployeeBookedMonths(monthList.get(i));
			employeeProject.setProject(new Project(projectIds.get(i).getId()));
			employeeProject.setEmployeeProjectStartDate(employeeProjectStartDate.get(i));
			employeeProject.setEmployeeProjectEndDate(employeeProjectEndDate.get(i));
			employeeProject.setEmployeeProjectName(projectIds.get(i).getName());
//			if (!confirmBookings.isEmpty()) {
//				boolean confirmBooking = confirmBookings.get(i);
//				employeeProject.setConfirmBooking(confirmBooking);
//				// rest of the code
//			}
			employeeProjectService.saveEmployeeProjectEmployeeOnly(employeeProject);
		}
	}


	@Override
	public List<Double> getListOfEmployeeBookedMonths(List<String> startDates, List<String> endDates) {
		List<Double> monthList = new ArrayList<>();
		startDates.removeAll(Collections.singleton(""));
		endDates.removeAll(Collections.singleton(""));
		for (int i=0; i< startDates.size(); i++) {
			double monthsBetween = ChronoUnit.MONTHS.between(
					LocalDate.parse(startDates.get(i)),
					LocalDate.parse(endDates.get(i)).plusDays(1));
			String subStart = startDates.get(i).substring(startDates.get(i).length() - 2);
			String subEnd = endDates.get(i).substring(endDates.get(i).length() - 2);
			if (subStart.equals("15") && !subEnd.equals("14")) {
				double v = monthsBetween + 0.5;
				monthList.add(v);
			}
			if (!subStart.equals("15") && subEnd.equals("14")) {
				double v = monthsBetween + 0.5;
				monthList.add(v);
			}
			if (subStart.equals("15") && subEnd.equals("14") && monthsBetween < 0.5) {
				double v = monthsBetween + 1;
				monthList.add(v);
			}if (subStart.equals("15") && subEnd.equals("14")) {
				monthList.add(monthsBetween);
			} else if (!subStart.equals("15") && !subEnd.equals("14")){
				monthList.add(monthsBetween);
			}
		}
		return monthList;
	}

	@Override
	public Double getEmployeeBookedMonths(String startDates, String endDates) {
		double monthsBetween = ChronoUnit.MONTHS.between(
				LocalDate.parse(startDates),
				LocalDate.parse(endDates).plusDays(1));
		String subStart = startDates.substring(startDates.length() - 2);
		String subEnd = endDates.substring(endDates.length() - 2);
		if (subStart.equals("15") && !subEnd.equals("14")) {
			return monthsBetween + 0.5;
		}
		if (!subStart.equals("15") && subEnd.equals("14")) {
			return monthsBetween + 0.5;
		}
		if (subStart.equals("15") && subEnd.equals("14") && monthsBetween < 0.5) {
			return monthsBetween + 1;
		} else {
			return  monthsBetween;
		}
	}

	//Chart
	@Override
	public List<Employee> getAllEmployeesByEmployeeProjectStartDate(ChartYear year) {
		LocalDate yearObjStart = LocalDate.parse(year.getYear() + "-01-01");
		LocalDate yearObjEnd = LocalDate.parse(year.getYear() + "-12-31");
		List<Employee> employees = removeAllEmployeeProjectsOutsideTheGivenYear(year);
		employees.sort(Comparator.comparing(Employee::getLastName));
		for (Employee employee : employees) {
			for (EmployeeProject employeeProject : employee.getEmployeeProjects()) {
				LocalDate startDate = LocalDate.parse(employeeProject.getEmployeeProjectStartDate());
				LocalDate endDate = LocalDate.parse(employeeProject.getEmployeeProjectEndDate());
				if ((startDate.isAfter(yearObjStart) || (startDate.isEqual(yearObjStart))) &&
						(endDate.isBefore(yearObjEnd) || endDate.isEqual(yearObjEnd))) {
					employeeProject.setEmployeeBookedMonths(getEmployeeBookedMonths(employeeProject.getEmployeeProjectStartDate(), employeeProject.getEmployeeProjectEndDate()));
				}
				if (startDate.isBefore(yearObjStart) && endDate.isAfter(yearObjEnd)) {
					employeeProject.setEmployeeProjectStartDate(String.valueOf(yearObjStart));
					employeeProject.setEmployeeProjectEndDate(String.valueOf(yearObjEnd));
					employeeProject.setEmployeeBookedMonths(getEmployeeBookedMonths(String.valueOf(yearObjStart), String.valueOf(yearObjEnd)));
				}
				if ((startDate.isAfter(yearObjStart) || startDate.isEqual(yearObjStart)) &&
						(endDate.isAfter(yearObjEnd) || endDate.isEqual(yearObjEnd))) {
					employeeProject.setEmployeeProjectEndDate(String.valueOf(yearObjEnd));
					employeeProject.setEmployeeBookedMonths(getEmployeeBookedMonths(employeeProject.getEmployeeProjectStartDate(), String.valueOf(yearObjEnd)));
				}
				if ((startDate.isBefore(yearObjStart) || startDate.isEqual(yearObjStart)) &&
						(endDate.isBefore(yearObjEnd) || endDate.isEqual(yearObjEnd))) {
					employeeProject.setEmployeeProjectStartDate(String.valueOf(yearObjStart));
					employeeProject.setEmployeeBookedMonths(getEmployeeBookedMonths(String.valueOf(yearObjStart), employeeProject.getEmployeeProjectEndDate()));
				}
			}
			List<EmployeeProject> employeeProjects = employee.getEmployeeProjects();
			employeeProjects.sort(Comparator.comparing(EmployeeProject::getEmployeeProjectStartDate));
		}
		return employees;
	}

	public List<Employee> removeAllEmployeeProjectsOutsideTheGivenYear(ChartYear year) {
		LocalDate yearObjStart = LocalDate.parse(year.getYear() + "-01-01");
		LocalDate yearObjEnd = LocalDate.parse(year.getYear() + "-12-31");
		List<Employee> employees = employeeRepository.findAll();
		for (Employee employee : employees) {
				Iterator<EmployeeProject> iter = employee.getEmployeeProjects().iterator();
				while (iter.hasNext()) {
					EmployeeProject employeeProject = iter.next();
						LocalDate startDate = LocalDate.parse(employeeProject.getEmployeeProjectStartDate());
						LocalDate endDate = LocalDate.parse(employeeProject.getEmployeeProjectEndDate());
						if ((startDate.isBefore(yearObjStart) && endDate.isBefore(yearObjStart)) ||
								(startDate.isAfter(yearObjEnd) && endDate.isAfter(yearObjEnd))) {
							iter.remove();
						}

				}
		}
		employees.removeIf(empl -> empl.getEmployeeProjects().isEmpty());
		return employees;
	}


	@Override
	public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.employeeRepository.findAll(pageable);
	}


}
