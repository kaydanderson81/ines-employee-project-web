package com.web.repository;

import com.web.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    Boolean existsByName(String name);
//    @Query(value = "SELECT * FROM employees, employee_projects WHERE employees.employee_id=employee_projects.employee_id AND YEAR(employee_projects.employee_project_start_date) = :year", nativeQuery = true)
//    List<EmployeeProject> findEmployeeProjectsByStartDate(@Param("year") String year);

    @Query(value = "SELECT * FROM employees WHERE EXISTS (" +
            "SELECT 1 FROM employee_projects " +
            "WHERE " +
            "employee_projects.employee_id = employees.employee_id AND " +
            ":yearStart < employee_project_end_date AND " +
            ":yearEnd > employee_project_start_date)", nativeQuery = true)
    List<Employee> findAllEmployeeProjectsByYear(@Param("yearStart") String yearStart, @Param("yearEnd")String yearEnd);
}
