package com.web.service.chart;

import com.web.repository.EmployeeProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class ChartServiceImpl implements ChartService {

    @Autowired
    private EmployeeProjectRepository employeeProjectRepository;

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
        return sortedYears.stream().map(Object::toString).collect(Collectors.toList());
    }
}
