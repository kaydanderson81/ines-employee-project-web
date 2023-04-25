//package com.web.service;
//
//import com.web.model.Employee;
//import com.web.model.EmployeeProject;
//import com.web.model.Project;
//import com.web.repository.EmployeeProjectRepository;
//import com.web.repository.EmployeeRepository;
//import com.web.repository.ProjectRepository;
//import com.web.service.employeeProject.EmployeeProjectServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest
//class EmployeeServiceImplTest {
//
//    @InjectMocks
//    EmployeeServiceImpl employeeServiceImpl;
//
//    @Mock
//    EmployeeRepository employeeRepository;
//
//    @InjectMocks
//    ProjectServiceImpl projectService;
//
//    @Mock
//    ProjectRepository projectRepository;
//
//    @InjectMocks
//    EmployeeProjectServiceImpl employeeProjectServiceImpl;
//
//    @Mock
//    EmployeeProjectRepository employeeProjectRepository;
//
//    private Employee employee1;
//    private Employee employee2;
//    private Employee employee3;
//
//    private Project project1;
//    private Project project2;
//
//    @BeforeEach
//    public void init() {
//        employee1 = new Employee(1L, "Test", "Employee1", "Test Employee 1", "2021-01-01", "2021-12-31");
//        employee2 = new Employee(2L, "Test", "Employee2", "Test Employee 2", "2022-02-01", "2022-12-31");
//        employee3 = new Employee(3L, "Test", "Employee3", "Test Employee 3", "2023-03-01", "2023-12-31");
//        project1 = new Project(1L, "Test Project 1", "2021-01-01", "2021-12-31",
//                12, 12, 0, 2, "Test Comment 1");
//        project2 = new Project(2L, "Test Project 2", "2022-02-01", "2022-12-31",
//                11, 11, 0, 3, "Test Comment 2");
//
//    }
//
//    @Test
//    @DisplayName("Should get all employees from database")
//    void getAllEmployees() {
//        List<Employee> employees = new ArrayList<>();
//        employees.add(employee1);
//        employees.add(employee2);
//        employees.add(employee3);
//
//        when(employeeRepository.findAll()).thenReturn(employees);
//
//        List<Employee> getAllEmployees = employeeServiceImpl.getAllEmployees();
//        assertEquals(3, getAllEmployees.size());
//        assertNotNull(getAllEmployees);
//        verify(employeeRepository, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("Should save Employee to database")
//    void saveEmployee() {
//        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);
//        Employee newEmployee = employeeServiceImpl.saveEmployee(employee1);
//        verify(employeeRepository, times(1)).save(any());
//        assertNotNull(newEmployee);
//        assertEquals(newEmployee.getName(), employee1.getName());
//    }
//
//    @Test
//    @DisplayName("Should return Employee by Id")
//    void getEmployeeById() {
//        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee1));
//        Employee existingEmployee = employeeServiceImpl.getEmployeeById(1L);
//        assertNotNull(existingEmployee);
//        assertEquals(existingEmployee.getId(), employee1.getId());
//    }
//
//    @Test
//    @DisplayName("Should delete an Employee by id")
//    void deleteEmployeeById() {
//        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee1));
//        doNothing().when(employeeRepository).delete(any(Employee.class));
//
//        employeeServiceImpl.deleteEmployeeById(employee1.getId());
//        verify(employeeRepository, times(1)).deleteById(employee1.getId());
//    }
//
//
////    @Test
////    void saveEmployeeProjectToEmployee() {
////        List<Project> projects = new ArrayList<>();
////        projects.add(project1);
////        projects.add(project2);
////        List<Double> monthList = new ArrayList<>();
////        monthList.add(project1.getCurrentBookedMonths());
////        monthList.add(project2.getCurrentBookedMonths());
////        List<String> startDates = new ArrayList<>();
////        startDates.add(project1.getStartDate());
////        startDates.add(project2.getStartDate());
////        List<String> endDates = new ArrayList<>();
////        endDates.add(project1.getEndDate());
////        endDates.add(project2.getEndDate());
////        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);
////        EmployeeProject newEmployeeProject = employeeServiceImpl.saveEmployeeProjectToEmployee(
////                employee1, projects, monthList, startDates, endDates);
////        employeeProjectServiceImpl.saveEmployeeProjectEmployeeOnly(newEmployeeProject);
////        verify(employeeRepository, times(1)).save(any());
////        assertNotNull(newEmployeeProject);
////        assertEquals(newEmployeeProject.getEmployee(), employee1);
////
////
////    }
//
//    @Test
//    void getListOfEmployeeBookedMonths() {
//    }
//
//    @Test
//    void getEmployeeBookedMonths() {
//    }
//
//    @Test
//    void getAllEmployeesByEmployeeProjectStartDate() {
//    }
//
//    @Test
//    void removeAllEmployeeProjectsOutsideTheGivenYear() {
//    }
//
//    @Test
//    void findPaginated() {
//    }
//}