package com.web.warnings.project;

import com.web.model.EmployeeProject;
import com.web.model.Project;
import com.web.repository.EmployeeRepository;
import com.web.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class ProjectWarning {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public static String validateEmployeeProject(EmployeeProject employeeProjects) {
        String error = "";
        if (employeeProjects.getEmployeeProjectStartDate().isEmpty() || employeeProjects.getEmployeeProjectEndDate().isEmpty()) {
            error += "An employee project start date and end date must be entered. ";
        }

        if (ProjectWarning.ifProjectStartDateIsNotSetCorrectly(employeeProjects.getEmployeeProjectStartDate())) {
            error += "Employee project start date day must be the 1st or 15th. Please check dates entered. ";
        }

        if (ProjectWarning.ifProjectEndDateIsNotSetCorrectly(employeeProjects.getEmployeeProjectEndDate())) {
            error += "Employee project end date day must be the 14th or the last day of the month. Please check dates entered. ";
        }

        return error;
    }

    public String warningForExistingProjectNameAndNumber(Project project, RedirectAttributes redirectAttributes) {
        if (projectRepository.existsByName(project.getName())) {
            redirectAttributes.addFlashAttribute("failed", "Project name already exists");
            return "redirect:/ines/showNewProjectForm";
        } if (projectRepository.existsByProjectNumber(project.getProjectNumber())) {
            redirectAttributes.addFlashAttribute("failed", "Project number already exists");
            return "redirect:/ines/showNewProjectForm";
        }
        return null;
    }

    public static boolean ifProjectStartDateIsNotSetCorrectly(String employeeProjectStartDates) {
            String subStart = employeeProjectStartDates.substring(employeeProjectStartDates.length() - 2);
        return !subStart.equals("01") && !subStart.equals("15");
    }

    public static boolean ifProjectEndDateIsNotSetCorrectly(String employeeProjectEndDates) {
            LocalDate end = LocalDate.parse(employeeProjectEndDates);
            YearMonth month = YearMonth.from(end);
        return !end.equals(month.atDay(14)) && !end.equals(month.atEndOfMonth());
    }


}
