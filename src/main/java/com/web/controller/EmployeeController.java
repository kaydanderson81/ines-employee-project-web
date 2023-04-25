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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
	public String viewEmployeesPage(Model model) throws ParseException {
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
		return "new_employee";
	}


	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee,
							   @RequestParam("projectId") List<Project> projectIds,
							   @RequestParam("employeeProjectStartDate") List<String> employeeProjectStartDate,
							   @RequestParam("employeeProjectEndDate") List<String> employeeProjectEndDate,
//							   @RequestParam(value = "confirmBooking", required = false) List<String> confirmBooking,
//							   @RequestParam("confirmBookingHidden") List<String> confirmBookingHidden,
							   RedirectAttributes redirectAttributes) {

//		List<Boolean> confirmBookingList = new ArrayList<>();
//
//		for (int i = 0; i < projectIds.size(); i++) {
//			String checkboxValue = confirmBooking.get(i);
//			String hiddenValue = confirmBookingHidden.get(i);
//
//			if (checkboxValue != null) {
//				confirmBookingList.add(Boolean.valueOf(checkboxValue));
//			} else {
//				confirmBookingList.add(Boolean.valueOf(hiddenValue));
//			}
//		}

//		System.out.println("Booking: " + confirmBookingList);
//		System.out.println("Booking: " + confirmBookingList.size());

//		confirmBooking.removeAll(Arrays.asList(null, ""));

		employeeProjectStartDate.removeAll(Arrays.asList(null, ""));
		employeeProjectEndDate.removeAll(Arrays.asList(null, ""));


		if (employeeProjectStartDate.size() != employeeProjectEndDate.size()) {
			redirectAttributes.addFlashAttribute("failed", "An employee project start date and end date must be entered.");
			return "redirect:/ines/showNewEmployeeForm";
		}

		if (employeeRepository.existsByName(employee.getName())) {
			redirectAttributes.addFlashAttribute("failed", "Employee with name: " + employee.getName() +
					" already exists, please try another name.");
			return "redirect:/ines/showNewEmployeeForm";
		}

		if (ProjectWarning.ifProjectStartDateIsNotSetCorrectly(employeeProjectStartDate)) {
			redirectAttributes.addFlashAttribute("failed", "Employee project start date day must be the 1st or " +
					"15th. Please check dates entered.");
			return "redirect:/ines/showNewEmployeeForm";
		}

		if (ProjectWarning.ifProjectEndDateIsNotSetCorrectly(employeeProjectEndDate)) {
			redirectAttributes.addFlashAttribute("failed", "Employee project end date day must be the 14th or the " +
					"last day of the month. Please check dates entered.");
			return "redirect:/ines/showNewEmployeeForm";
		}

		List<Double> monthList = employeeService.getListOfEmployeeBookedMonths(employeeProjectStartDate, employeeProjectEndDate);

		employeeService.saveEmployeeProjectToEmployee(employee, projectIds, monthList,
				employeeProjectStartDate, employeeProjectEndDate);

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
											 @ModelAttribute("employee") Employee employee) {
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
		return "redirect:/ines/employees";
	}
	
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable (value = "id") long id) {
		this.employeeService.deleteEmployeeById(id);
		return "redirect:/ines/employees";
	}

	@GetMapping("/deleteEmployeeProject/{id}")
	public String deleteEmployeeProject(@PathVariable (value = "id") long id) {
		this.employeeProjectService.deleteEmployeeProjectById(id);
		return "redirect:/ines/employees";
	}

	@GetMapping("/chart")
	public String viewChartPage(Model model) throws ParseException {
//		List<Employee> employees = employeeService.getAllEmployees();
		ChartYear year = new ChartYear("2023");
		List<Employee> employees = employeeService.getAllEmployeesByEmployeeProjectStartDate(year);
		employees.sort(Comparator.comparing(Employee::getLastName));
		List<String> yearList = employeeProjectService.findAllStartAndEndDatesByYear();
		List<Project> projects = projectService.getAListOfProjectsAndTheirPersonMonthsByYear(year);

		model.addAttribute("employees", employees);
		model.addAttribute("projects", projects);
		model.addAttribute("yearList", yearList);
		model.addAttribute("currentYear", Year.now());
		return "chart.html";
	}

	@PostMapping("/chart")
	public String reloadChartWithYear(@ModelAttribute("chartYear") ChartYear chartYear,
									  Model model) throws InterruptedException, ParseException {
		List<String> yearList = employeeProjectService.findAllStartAndEndDatesByYear();
		List<Employee> employees = employeeService.getAllEmployeesByEmployeeProjectStartDate(chartYear);
		List<Project> projects = projectService.getAListOfProjectsAndTheirPersonMonthsByYear(chartYear);
		System.out.println("Employees: " + employees);
		model.addAttribute("employees", employees);
		model.addAttribute("projects", projects);
		model.addAttribute("yearList", yearList);
		model.addAttribute("currentYear", chartYear.getYear());

		return "chart.html";
	}


//	@GetMapping("/chart/{year}")
//	public String viewChartPage(@PathVariable (value = "year") ChartYear year,
//								@ModelAttribute("chartYear") ChartYear selectedYear,
//								Model model) {
//
//		List<Employee> employees = employeeService.getAllEmployeesByEmployeeProjectStartDate(selectedYear);
//		System.out.println("Year: " + year);
//		System.out.println("Selected Year: " + selectedYear);
//
//		List<String> yearList = employeeProjectService.findAllStartAndEndDatesByYear();
//		model.addAttribute("employees", employees);
//		model.addAttribute("yearList", yearList);
//		model.addAttribute("currentYear", Year.now());
//		model.addAttribute("selectedYear", selectedYear.getYear());
//		return "chart.html";
//	}


}
