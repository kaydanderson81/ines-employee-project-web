package com.web.service;

import com.web.model.ChartYear;
import com.web.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects();
    void saveProject(Project project);
    Project getProjectById(long id);
    void deleteProjectById(long id);

    void updateEmployeeProjectBookedMonths();

    List<Project> getAListOfProjectsAndTheirPersonMonthsByYear(ChartYear year);
    Page<Project> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    Double getListOfEmployeeBookedMonths(String startDates, String endDates);
}
