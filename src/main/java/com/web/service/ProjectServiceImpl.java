package com.web.service;

import com.web.model.ChartYear;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

    @Autowired
    private EmployeeProjectService employeeProjectService;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public void saveProject(Project project) {
        List<Project> projects = getAllProjects();
            this.projectRepository.save(project);
    }

    @Override
    public Project getProjectById(long id) {
        Optional<Project> optional = projectRepository.findById(id);
        Project project;
        if (optional.isPresent()) {
            project = optional.get();
        } else {
            throw new RuntimeException(" Project not found for id :: " + id);
        }
        return project;
    }

    @Override
    public void deleteProjectById(long id) {
        this.projectRepository.deleteById(id);
    }

    @Override
    public void updateEmployeeProjectBookedMonths() {
        List<Project> projects = getAllProjects();
        for (Project project : projects) {
            List<Double> listDoubles = employeeProjectRepository.listAllEmployeeBookedMonthsByProjectId(project.getId());
            double total = listDoubles.stream().mapToDouble(Double::doubleValue).sum();
            project.setCurrentBookedMonths(total);
            project.setRemainingBookedMonths(project.getProjectLengthInMonths() - total);
            saveProject(project);
        }
    }

    @Override
    public List<Project> getAListOfProjectsAndTheirPersonMonthsByYear(ChartYear year) {
        LocalDate yearObjStart = LocalDate.parse(year.getYear() + "-01-01");
        LocalDate yearObjEnd = LocalDate.parse(year.getYear() + "-12-31");
        List<Project> projects = getAllProjects();
        Iterator<Project> iter = projects.iterator();
        while (iter.hasNext()) {
                Project project = iter.next();
            if (project.getStartDate().equals("Date Not Set") || project.getEndDate().equals("Date Not Set")) {
                project.setStartDate(year.getYear() + "-01-01");
                project.setEndDate(year.getYear() + "-12-31");
            }
            LocalDate startDate = LocalDate.parse(project.getStartDate());
            LocalDate endDate = LocalDate.parse(project.getEndDate());
            if ((startDate.isAfter(yearObjStart) || (startDate.isEqual(yearObjStart))) &&
                    (endDate.isBefore(yearObjEnd) || endDate.isEqual(yearObjEnd))) {
                Double months = getListOfEmployeeBookedMonths(String.valueOf(startDate), String.valueOf(endDate));
                project.setProjectLengthInMonths(months);
            }
            if (startDate.isBefore(yearObjStart) && endDate.isAfter(yearObjEnd)) {
                project.setProjectLengthInMonths(12);
            }
            if ((startDate.isAfter(yearObjStart) || startDate.isEqual(yearObjStart)) &&
                    (endDate.isAfter(yearObjEnd) || endDate.isEqual(yearObjEnd))) {
                Double months = getListOfEmployeeBookedMonths(String.valueOf(startDate), String.valueOf(yearObjEnd));
                project.setProjectLengthInMonths(months);
            }
            if ((startDate.isBefore(yearObjStart) || startDate.isEqual(yearObjStart)) &&
                    (endDate.isBefore(yearObjEnd) || endDate.isEqual(yearObjEnd))) {
                Double months = getListOfEmployeeBookedMonths(String.valueOf(yearObjStart), String.valueOf(endDate));
                project.setProjectLengthInMonths(months);
            }
            if ((startDate.isBefore(yearObjStart) && endDate.isBefore(yearObjStart)) ||
                    (startDate.isAfter(yearObjEnd) && endDate.isAfter(yearObjEnd))) {
                iter.remove();
            }

            List<EmployeeProject> employeeProjects =
                    employeeProjectService.getAllEmployeeProjectsByProjectId(project.getId());
            if (!employeeProjects.isEmpty()) {
                double sumBookedMonths = 0.0;
                for (EmployeeProject employeeProject : employeeProjects) {
                    LocalDate epStartDate = LocalDate.parse(employeeProject.getEmployeeProjectStartDate());
                    LocalDate epEndDate = LocalDate.parse(employeeProject.getEmployeeProjectEndDate());
                    if ((epStartDate.isAfter(yearObjStart) || epStartDate.isEqual(yearObjStart))&&
                            ((epEndDate.isBefore(yearObjEnd) || epEndDate.isEqual(yearObjEnd)))) {
                            double bookedMonths = getListOfEmployeeBookedMonths(String.valueOf(epStartDate), String.valueOf(epEndDate));
                            sumBookedMonths += bookedMonths;
                    }
                }
                project.setCurrentBookedMonths(sumBookedMonths);
            }
        }
        return projects;
    }

    @Override
    public Page<Project> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.projectRepository.findAll(pageable);
    }

    @Override
    public List<String> findAllStartAndEndDatesByYear() {
        Set<String> startDates = projectRepository.findAllStartDates();
        Set<String> endDates = projectRepository.findAllEndDates();
        startDates.removeAll(Collections.singleton(""));
        endDates.removeAll(Collections.singleton(""));

        String minStartDate = Collections.min(startDates);
        String maxEndDate = Collections.max(endDates);

        List<String> yearList = new ArrayList<>();
        DateFormat formater = new SimpleDateFormat("yyyy");

        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        try {
            beginCalendar.setTime(formater.parse(minStartDate));
            finishCalendar.setTime(formater.parse(maxEndDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        while (beginCalendar.before(finishCalendar)) {
            String date =     formater.format(beginCalendar.getTime()).toUpperCase();
            yearList.add(date);
            beginCalendar.add(Calendar.YEAR, 1);
        }
        return yearList;
    }

    @Override
    public Double getListOfEmployeeBookedMonths(String startDates, String endDates) {
        double monthsBetween = ChronoUnit.MONTHS.between(
                LocalDate.parse(startDates),
                LocalDate.parse(endDates).plusDays(1));
        String substr = endDates.substring(endDates.length() - 2);
        if (substr.equals("15")) {
            double v = monthsBetween + 0.5;
            return v;
        }

        return monthsBetween;
    }

//    public List<Project> updateProjectBookedMonthsByYear(Long id) {
//
//    }

}
