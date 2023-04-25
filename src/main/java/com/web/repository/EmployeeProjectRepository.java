package com.web.repository;

import com.web.model.EmployeeProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Long> {

    @Query(value = "SELECT * FROM employee_projects ep WHERE ep.employee_id = :id", nativeQuery = true)
    List<EmployeeProject> findAllEmployeeProjectByEmployeeId(@Param("id")Long id);


    @Query(value = "SELECT project_id FROM employee_projects ep where ep.employee_id = :id", nativeQuery = true)
    List<Long> findAllProjectIdsByEmployeeId(@Param("id")Long employeeId);

    @Query(value = "SELECT employee_project_start_date FROM employee_projects ep where ep.employee_id = :id", nativeQuery = true)
    List<Date> findAllEmployeeProjectStartDatesByEmployeeId(@Param("id")Long employeeId);

    @Query(value = "SELECT employee_project_id FROM employee_projects ep where ep.employee_id = :employeeId AND ep.project_id = :projectId", nativeQuery = true)
    List<Long> findEmployeeProjectIdByEmployeeIdAndProjectId(@Param("employeeId")Long employeeId, @Param("projectId")Long projectId);

    @Query(value = "SELECT * FROM employee_projects WHERE YEAR(employee_project_start_date) = :year", nativeQuery = true)
    List<EmployeeProject> findEmployeeProjectsByStartDate(@Param("year") String year);

    @Query(value = "SELECT * FROM employee_projects WHERE YEAR(employee_project_end_date) = :year", nativeQuery = true)
    List<EmployeeProject> findEmployeeProjectsByEndDate(@Param("year") String year);

    @Query(value = "SELECT employee_booked_months FROM employee_projects WHERE project_id = :projectId", nativeQuery = true)
    List<Double> listAllEmployeeBookedMonthsByProjectId(@Param("projectId") Long projectId);

    @Query(value = "SELECT employee_project_start_date FROM employee_projects", nativeQuery = true)
    Set<String> findAllEmployeeProjectStartDates();

    @Query(value = "SELECT employee_project_end_date FROM employee_projects", nativeQuery = true)
    Set<String> findAllEmployeeProjectEndDates();

    @Query(value = "SELECT * FROM employee_projects WHERE project_id = :projectId", nativeQuery = true)
    List<EmployeeProject> getAllEmployeeProjectsByProjectId(@Param("projectId") Long projectId);

    @Query(value = "SELECT SUM(employee_booked_months) AS sum_booked_months FROM employee_projects WHERE project_id = :id ", nativeQuery = true)
    double findSumOfEmployeeBookedMonthsByProjectId(@Param("id") Long id);
}
