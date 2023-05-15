package com.web.controller;

import com.web.model.Employee;
import com.web.model.EmployeeProject;
import com.web.model.Project;
import com.web.repository.EmployeeProjectRepository;
import com.web.repository.ProjectRepository;
import com.web.service.EmployeeService;
import com.web.service.ProjectService;
import com.web.service.employeeProject.EmployeeProjectService;
import com.web.warnings.project.ProjectWarning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ines")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeProjectService employeeProjectService;

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @GetMapping("/projects")
    public String viewProjectsPage(Model model) {
        return findProjectPaginated(1, "name", "asc", model);
    }

    @GetMapping("/showNewProjectForm")
    public String showNewProjectForm(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        return "project/new_project.html";
    }

    @PostMapping("/saveProject")
    public String saveProject(@ModelAttribute("project") Project project, RedirectAttributes redirectAttributes) {
        if (projectRepository.existsByName(project.getName())) {
            redirectAttributes.addFlashAttribute("failed", "Project name: " + project.getName() + " already exists");
            return "redirect:/ines/showNewProjectForm";
        }
        if (projectRepository.existsByProjectNumber(project.getProjectNumber())) {
            redirectAttributes.addFlashAttribute("failed", "Project with number: " + project.getProjectNumber() + " already exists");
            return "redirect:/ines/showNewProjectForm";
        }
        if (project.getStartDate().isEmpty()) {
            project.setStartDate("Date Not Set");
        }
        if (project.getEndDate().isEmpty()) {
            project.setEndDate("Date Not Set");
        }
        projectService.saveProject(project);
        redirectAttributes.addFlashAttribute("success", "Added project: " + project.getName() + " to list");
        return "redirect:/ines/projects";
    }

    @GetMapping("/showFormForAddProjectToEmployee/{id}")
    public String showFormForAddProjectToEmployee(@PathVariable(value = "id") long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        List<Project> projects = projectService.getAllProjects();
        List<EmployeeProject> findEmployeeProjects = employeeProjectRepository.findAllEmployeeProjectByEmployeeId(id);

        model.addAttribute("employee", employee);
        model.addAttribute("projects", projects);
        model.addAttribute("employeeProject", new EmployeeProject());
        model.addAttribute("employeeSavedProject", findEmployeeProjects);
        return "project/add_Project_To_Employee";
    }

    @PostMapping("/addProjectToEmployee/{id}")
    public String addProjectToEmployee(@PathVariable(value = "id") long id,
                                       @ModelAttribute("employee") Employee employee,
                                       @ModelAttribute EmployeeProject employeeProject,
                                       RedirectAttributes redirectAttributes) {

        String validationError = ProjectWarning.validateEmployeeProject(employeeProject);

        if (!validationError.isEmpty()) {
            redirectAttributes.addFlashAttribute("failed", validationError);
            return "redirect:/ines/showFormForAddProjectToEmployee/" + id;
        }

        employeeProject.setEmployeeBookedMonths(
                employeeService.getEmployeeBookedMonths(employeeProject.getEmployeeProjectStartDate(),
                        employeeProject.getEmployeeProjectEndDate()));

        Employee updateEmployee = employeeService.getEmployeeById(id);
        employeeService.saveProjectToExistingEmployee(updateEmployee, employeeProject);
        redirectAttributes.addAttribute("updatedEmployeeId", employee.getId());
        return "redirect:/ines/employees";
    }

    @GetMapping("/showFormForProjectUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "project/update_project";
    }

    @PostMapping("/updateProject/{id}")
    public String updateProject(@PathVariable(value = "id") long id,
                                @ModelAttribute("project") Project project,
                                RedirectAttributes redirectAttributes) {
        Project projectToUpdate = projectService.getProjectById(id);
        projectToUpdate.setProjectNumber(project.getProjectNumber());
        projectToUpdate.setName(project.getName());
        projectToUpdate.setStartDate(project.getStartDate());
        projectToUpdate.setEndDate(project.getEndDate());
        projectToUpdate.setProjectLengthInMonths(project.getProjectLengthInMonths());
        projectToUpdate.setComment(project.getComment());
        if (project.getStartDate().isEmpty()) {
            project.setStartDate("Date Not Set");
        }
        if (project.getEndDate().isEmpty()) {
            project.setEndDate("Date Not Set");
        }
        projectService.saveProject(projectToUpdate);
        redirectAttributes.addFlashAttribute("success", "Updated project: " + project.getName());
        return "redirect:/ines/projects";
    }

    @GetMapping("/showFormForEmployeeProjectUpdate/{id}")
    public String showFormForUpdateEmployeeProject(@PathVariable(value = "id") long id, Model model) {
        EmployeeProject employeeProject = employeeProjectService.getEmployeeProjectById(id);
        Employee employee = employeeService.getEmployeeById(employeeProject.getEmployee().getId());
        List<EmployeeProject> findEmployeeProjects = employeeProjectRepository.findAllEmployeeProjectByEmployeeId(employee.getId());
        model.addAttribute("employee", employee);
        model.addAttribute("employeeProject", employeeProject);
        model.addAttribute("employeeSavedProject", findEmployeeProjects);
        return "project/update_employee_project";
    }

    @PostMapping("/updateEmployeeProject/{id}")
    public String updateEmployeeProject(@PathVariable(value = "id") long id,
                                        @ModelAttribute("employeeProject") EmployeeProject employeeProjects,
                                        RedirectAttributes redirectAttributes) {
        String validationError = ProjectWarning.validateEmployeeProject(employeeProjects);
        Long employeeId = employeeProjects.getEmployee().getId();

        if (!validationError.isEmpty()) {
            redirectAttributes.addFlashAttribute("failed", validationError);
            return "redirect:/ines/showFormForEmployeeProjectUpdate/" + id;
        }

        Double monthList =
                employeeService.getEmployeeBookedMonths(employeeProjects.getEmployeeProjectStartDate(),
                        employeeProjects.getEmployeeProjectEndDate());
        EmployeeProject updateEmployeeProject = employeeProjectService.getEmployeeProjectById(id);
        updateEmployeeProject.setEmployeeProjectStartDate(employeeProjects.getEmployeeProjectStartDate());
        updateEmployeeProject.setEmployeeProjectEndDate(employeeProjects.getEmployeeProjectEndDate());
        updateEmployeeProject.setConfirmBooking(employeeProjects.getConfirmBooking());
        updateEmployeeProject.setEmployeeBookedMonths(monthList);
        employeeProjectService.saveEmployeeProject(updateEmployeeProject);

        redirectAttributes.addAttribute("updatedEmployeeId", employeeId);
        return "redirect:/ines/employees";

    }

    @GetMapping("/deleteProject/{id}")
    public String deleteProject(@PathVariable(value = "id") long id) {
        this.projectService.deleteProjectById(id);
        return "redirect:/ines/projects";
    }

    @GetMapping("/showFormForAddProjectBlockToEmployee/{id}")
    public String showFormForAddProjectBlockToEmployee(@PathVariable(value = "id") long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        List<Project> projects = projectService.getAllProjects();

        List<EmployeeProject> findEmployeeProjects = employeeProjectRepository.findAllEmployeeProjectByEmployeeId(id);

        model.addAttribute("employee", employee);
        model.addAttribute("projects", projects);
        model.addAttribute("employeeSavedProject", findEmployeeProjects);
        return "project/employee_project_block";
    }


    @GetMapping("/projectPage/{pageNo}")
    public String findProjectPaginated(@PathVariable(value = "pageNo") int pageNo,
                                       @RequestParam("sortField") String sortField,
                                       @RequestParam("sortDir") String sortDir,
                                       Model model) {
        int pageSize = 10;

        Page<Project> page = projectService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Project> listProjects = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listProjects", listProjects);
        return "project/projects.html";
    }

}
