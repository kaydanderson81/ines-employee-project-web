package com.web.repository;

import com.web.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Boolean existsByName(String name);
    Boolean existsByProjectNumber(Long number);

    @Query(value = "SELECT start_date FROM projects", nativeQuery = true)
    Set<String> findAllStartDates();

    @Query(value = "SELECT end_date FROM projects", nativeQuery = true)
    Set<String> findAllEndDates();

}
