package com.web.warnings.project;

import com.web.model.Project;
import com.web.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class ProjectWarning {

    @Autowired
    ProjectRepository projectRepository;

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

    public static boolean ifProjectStartDateIsNotSetCorrectly(List<String> employeeProjectStartDates) {
        for (String startDate : employeeProjectStartDates) {
            String subStart = startDate.substring(startDate.length() - 2);
            if (!subStart.equals("01") && !subStart.equals("15")) {
                return true;
            }
        }
        return false;
    }

    public static boolean ifProjectEndDateIsNotSetCorrectly(List<String> employeeProjectEndDates) {
        for (String endDate : employeeProjectEndDates) {
            LocalDate end = LocalDate.parse(endDate);
            YearMonth month = YearMonth.from(end);
            if (!end.equals(month.atDay(14)) && !end.equals(month.atEndOfMonth())) {
                return true;
            }
        }
        return false;
    }

    public static boolean ifProjectStartOrEndDateIsMissing(List<String> employeeProjectStartDates,
                                                           List<String> employeeProjectEndDates) {
        if (employeeProjectStartDates.size() == employeeProjectEndDates.size()) {
            return true;
        }
        return false;
    }
}
