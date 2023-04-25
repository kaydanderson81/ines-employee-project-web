package com.web.service.employeeProject;

import com.web.model.Employee;
import com.web.model.EmployeeProject;
import com.web.model.Project;
import com.web.repository.EmployeeProjectRepository;
import com.web.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeProjectServiceImpl implements EmployeeProjectService {

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<EmployeeProject> getAllEmployeeProjects() {
        return employeeProjectRepository.findAll();
    }

    @Override
    public EmployeeProject getEmployeeProjectById(Long id) {
        Optional<EmployeeProject> optional = employeeProjectRepository.findById(id);
        EmployeeProject employeeProject;
        if (optional.isPresent()) {
            employeeProject = optional.get();
        } else {
            throw new RuntimeException("Employee Project not found for id :: " + id);
        }
        return employeeProject;
    }

    @Override
    public void saveEmployeeProject(EmployeeProject employeeProject) {
        this.employeeProjectRepository.save(employeeProject);
    }

    @Override
    public void saveEmployeeProjectWithEmployeeProjectBookedMonths(Employee employee, Project project, double employeeBookedMonths) {
        EmployeeProject employeeProject = new EmployeeProject(employee, project, employeeBookedMonths);
        this.employeeProjectRepository.save(employeeProject);
    }

    @Override
    public void saveEmployeeProjectEmployeeOnly(EmployeeProject employeeProject) {
        this.employeeProjectRepository.save(employeeProject);
    }

    @Override
    public List<Employee> getAllEmployeeProjectsByEmployeeId() {
        List<EmployeeProject> employeeProjects = employeeProjectRepository.findAll();
        List<Employee> newEmployeeList = new ArrayList<>();
        for (int i=0; i<= employeeProjects.size(); i++) {
            for (EmployeeProject employeeProject : employeeProjects) {
                Employee employee = new Employee();
                List<EmployeeProject> projects = new ArrayList<>();
                employee.setName(employeeProject.getEmployee().getFirstName(), employeeProject.getEmployee().getLastName());
                employee.setContractedFrom(employeeProject.getEmployee().getContractedFrom());
                employee.setContractedTo(employeeProject.getEmployee().getContractedTo());
                employee.setEmployeeProject(projects);
                newEmployeeList.add(employee);
            }
        }
        return newEmployeeList;
    }

    @Override
    public List<EmployeeProject> getAllEmployeeProjectsByEmployeeId(Long id) {
        List<EmployeeProject> employeeProjectList = employeeProjectRepository.findAll();
        for (EmployeeProject employeeProject : employeeProjectList) {
            if (employeeProject.getEmployee().getId().equals(id));
            employeeProjectList.add(employeeProject);
        }
        return employeeProjectList;
    }

    @Override
    public List<EmployeeProject> getAllEmployeeProjectsByProjectId(Long id) {
        return employeeProjectRepository.getAllEmployeeProjectsByProjectId(id);
    }

    @Override
    public void deleteEmployeeProjectById(Long id) {
        this.employeeProjectRepository.deleteById(id);
    }

    //Charts

    @Override
    public List<String> findAllStartAndEndDatesByYear() {
        Set<String> startDates = employeeProjectRepository.findAllEmployeeProjectStartDates();
        Set<String> endDates = employeeProjectRepository.findAllEmployeeProjectEndDates();

        Set<Integer> years = new HashSet<>();
        for (String date : startDates) {
            for (String end : endDates) {
                String startYear = date.split("-")[0];
                String endYear = end.split("-")[0];
                years.add(Integer.valueOf(String.valueOf(startYear)));
                years.add(Integer.valueOf(String.valueOf(endYear)));

            }
        }
        List<Integer> sortedYears = new ArrayList<>(years);
        Collections.sort(sortedYears);
        List<String> strings = sortedYears.stream().map(Object::toString).collect(Collectors.toList());
        return strings;
    }




}
