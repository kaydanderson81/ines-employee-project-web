package com.web.service;

import java.util.List;

import com.web.model.ChartYear;
import com.web.model.Employee;
import com.web.model.EmployeeProject;
import com.web.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface EmployeeService {

	List<Employee> getAllEmployees();
	Employee saveEmployee(Employee employee, EmployeeProject employeeProject);
//	void saveEmployeeWithProject(Employee employee, Long id);
	Employee getEmployeeById(long id);
	void deleteEmployeeById(long id);
	List<Project> getAllProjects();

	void saveEmployeeProjectToEmployee(Employee employee, List<Project> projectIds, List<Double> monthList,
									   List<String> employeeProjectStartDate, List<String> employeeProjectEndDate);

	List<Employee> getAllEmployeesByEmployeeProjectStartDate(ChartYear year);
	Double getEmployeeBookedMonths(String startDates, String endDates);

	Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

}
