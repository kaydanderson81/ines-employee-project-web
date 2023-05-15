package com.web.controller;

import com.web.model.ChartYear;
import com.web.model.Employee;
import com.web.model.EmployeeProject;
import com.web.model.Project;
import com.web.repository.EmployeeProjectRepository;
import com.web.repository.EmployeeRepository;
import com.web.repository.ProjectRepository;
import com.web.service.EmployeeService;
import com.web.service.ProjectService;
import com.web.service.employeeProject.EmployeeProjectService;
import com.web.warnings.project.ProjectWarning;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Year;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/ines")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeProjectRepository employeeProjectRepository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private EmployeeProjectService employeeProjectService;

	@Autowired
	private ModelMapper modelMapper;

	public EmployeeController() {
	}

	@GetMapping("/employees")
	public String viewEmployeesPage(Model model) {
		List<Employee> employees = employeeService.getAllEmployees();
		projectService.updateEmployeeProjectBookedMonths();

		employees.sort(Comparator.comparing(Employee::getLastName));
		model.addAttribute("listEmployees", employees);
		return "employees.html";
	}
	
	@GetMapping("/showNewEmployeeForm")
	public String showNewEmployeeForm(Model model) {
		Employee employee = new Employee();
		List<Project> projects = projectService.getAllProjects();
		projects.sort(Comparator.comparing(Project::getName));
		model.addAttribute("employee", employee);
		model.addAttribute("projects", projects);
		model.addAttribute("employeeProjects", new EmployeeProject());
		return "new_employee";
	}




	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee,
								@ModelAttribute EmployeeProject employeeProjects,
							   RedirectAttributes redirectAttributes) {

		String validationError = ProjectWarning.validateEmployeeProject(employeeProjects);

		if (!validationError.isEmpty()) {
			redirectAttributes.addFlashAttribute("failed", validationError);
			return "redirect:/ines/showNewEmployeeForm";
		}

		if (employeeRepository.existsByName(employee.getName())) {
			redirectAttributes.addFlashAttribute("failed", "Employee with name: " + employee.getName() +
					" already exists, please try another name.");
			return "redirect:/ines/showNewEmployeeForm";
		}

		double bookedMonths =
				employeeService.getEmployeeBookedMonths(employeeProjects.getEmployeeProjectStartDate(),
															  employeeProjects.getEmployeeProjectEndDate());
		employeeProjects.setEmployeeBookedMonths(bookedMonths);

		employeeService.saveEmployeeAndEmployeeProject(employee, employeeProjects);
		redirectAttributes.addAttribute("updatedEmployeeId", employee.getId());
		return "redirect:/ines/employees";
	}

	@GetMapping("/showFormForEmployeeUpdate/{id}")
	public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {
		Employee employee = employeeService.getEmployeeById(id);
		model.addAttribute("employee", employee);
		return "update_employee";
	}

	@PostMapping("/updateEmployee/{id}")
	public String updateEmployeeWithProjects(@PathVariable( value = "id") long id,
											 @ModelAttribute("employee") Employee employee, RedirectAttributes redirectAttributes) {
		Employee updatedEmployee = employeeService.getEmployeeById(id);
		updatedEmployee.setFirstName(employee.getFirstName());
		updatedEmployee.setLastName(employee.getLastName());
		updatedEmployee.setName(employee.getFirstName(), employee.getLastName());
		updatedEmployee.setContractedFrom(employee.getContractedFrom());
		updatedEmployee.setContractedTo(employee.getContractedTo());
		if (employee.getContractedTo().isEmpty()) {
			updatedEmployee.setContractedTo("Date not set");
		}
		employeeService.saveEmployee(updatedEmployee);
		redirectAttributes.addAttribute("updatedEmployeeId", updatedEmployee.getId());
		return "redirect:/ines/employees";
	}
	
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable (value = "id") long id) {
		this.employeeService.deleteEmployeeById(id);
		return "redirect:/ines/employees";
	}

	@GetMapping("/deleteEmployeeProject/{id}")
	public String deleteEmployeeProject(@PathVariable (value = "id") long id, RedirectAttributes redirectAttributes) {
		EmployeeProject employeeProject = employeeProjectService.getEmployeeProjectById(id);
		this.employeeProjectService.deleteEmployeeProjectById(id);
		redirectAttributes.addAttribute("updatedEmployeeId", employeeProject.getEmployee().getId());
		return "redirect:/ines/employees";
	}

	@GetMapping("/chart")
	public String viewChartPage(@RequestParam(value = "year", required = false) String selectedYear, Model model) {
		ChartYear year;
		if (selectedYear != null && !selectedYear.isEmpty()) {
			year = new ChartYear(selectedYear);
		} else {
			year = new ChartYear(String.valueOf(Year.now()));
		}

		List<Employee> employees = employeeService.getAllEmployeesByEmployeeProjectStartDate(year);
		employees.sort(Comparator.comparing(Employee::getLastName));
		List<String> yearList = employeeProjectService.findAllStartAndEndDatesByYear();
		List<Project> projects = projectService.getAListOfProjectsAndTheirPersonMonthsByYear(year);

		model.addAttribute("employees", employees);
		model.addAttribute("projects", projects);
		model.addAttribute("yearList", yearList);
		model.addAttribute("currentYear", year.getYear());

		return "chart.html";
	}


}
