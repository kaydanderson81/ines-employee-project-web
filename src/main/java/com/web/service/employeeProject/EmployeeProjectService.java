package com.web.service.employeeProject;

import com.web.model.Employee;
import com.web.model.EmployeeProject;
import com.web.model.Project;

import java.util.List;

public interface EmployeeProjectService {



    List<EmployeeProject> getAllEmployeeProjects();

    EmployeeProject getEmployeeProjectById(Long id);

    void saveEmployeeProject(EmployeeProject employeeProject);
    void saveEmployeeProjectWithEmployeeProjectBookedMonths(Employee employee, Project project, double employeeBookedMonths);
    void saveEmployeeProjectEmployeeOnly(EmployeeProject employeeProject);

    List<Employee> getAllEmployeeProjectsByEmployeeId();

    List<EmployeeProject> getAllEmployeeProjectsByEmployeeId(Long id);

    List<EmployeeProject> getAllEmployeeProjectsByProjectId(Long id);

    void deleteEmployeeProjectById(Long id);

    List<String> findAllStartAndEndDatesByYear();

}
