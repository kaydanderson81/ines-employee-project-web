//package com.web;
//
//import com.web.model.Employee;
//import com.web.model.EmployeeProject;
//import com.web.model.Project;
//import com.web.repository.EmployeeRepository;
//import com.web.service.EmployeeService;
//import com.web.service.employeeProject.EmployeeProjectService;
//import com.web.repository.EmployeeProjectRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.annotation.Rollback;
//
//import java.util.List;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(value = false)
//public class EmployeeRepositoryTests {
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @Autowired
//    private EmployeeService employeeService;
//
//    @Autowired
//    private EmployeeProjectRepository employeeProjectRepository;
//
//    @Autowired
//    private EmployeeProjectService employeeProjectService;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    public void testAddItemFromDatabase() {
//        Project project = entityManager.find(Project.class, 2L);
//        Employee employee = entityManager.find(Employee.class, 3L);
//
//        EmployeeProject employeeProject = new EmployeeProject(employee, project, 2.5);
//
//        employeeProjectRepository.save(employeeProject);
//    }
//
//    @Test
//    public void testUpdateEmployeeProjectById() {
//        Employee employee = new Employee(2L);
//        Project project = new Project(1L);
//        EmployeeProject emplProj = employeeProjectService.getEmployeeProjectById(3L);
//        emplProj.setEmployee(employee);
//        emplProj.setProject(project);
//        emplProj.setEmployeeProjectName(project.getName());
//        emplProj.setEmployeeProjectStartDate("2022-11-01");
//        emplProj.setEmployeeProjectEndDate("2022-12-31");
//
//    }
//
//    @Test
//    public void testAddNewEmployeeWithExistingProject() {
//        Employee employee =  new Employee("John Smith", "2022-06-01", "2022-12-30");
//        employeeRepository.save(employee);
//        Project project = entityManager.find(Project.class, 2L);
//        EmployeeProject employeeProject = new EmployeeProject(employee, project, 2.2);
//
//        employeeProjectRepository.save(employeeProject);
//    }
//
//    @Test
//    public void testAddEmployeeProjectById() {
//        Project project = new Project(8L);
//        Employee employee = new Employee(2L);
//
//        EmployeeProject employeeProject = new EmployeeProject(employee, project, 6.6);
//
//        employeeProjectRepository.save(employeeProject);
//    }
//
//    @Test
//    public List<EmployeeProject> testGetAllEmployeeProjectsByEmployeeId() {
//        List<EmployeeProject> employeeProjectList = employeeProjectRepository.findAllEmployeeProjectByEmployeeId(12L);
////        Long employeeId = 12L;
////        List<EmployeeProject> allEmployeeProjectList = employeeProjectRepository.findAll();
////        for (EmployeeProject employeeProject : allEmployeeProjectList) {
////            if (employeeProject.getEmployee().getId().equals(employeeId)) {
////                employeeProjectList.add(employeeProject);
////            }
////        }
//        employeeProjectList.forEach(System.out::println);
//        return employeeProjectList;
//    }
//
//    @Test
//    public void testAddMultipleProjects() {
//        Employee employee = new Employee(1L);
//        Project project7 = new Project(7L);
//        Project project8 = new Project(8L);
//        Project project9 = new Project(9L);
//
//        EmployeeProject employeeProject1 = new EmployeeProject(employee, project7, 7.7);
//        EmployeeProject employeeProject2 = new EmployeeProject(employee, project8, 8.8);
//        EmployeeProject employeeProject3 = new EmployeeProject(employee, project9, 9.9);
//
//        employeeProjectRepository.saveAll(List.of(employeeProject1, employeeProject2, employeeProject3));
//    }
//
//    @Test
//    public void testListProjects() {
//        List<EmployeeProject> listProjects = employeeProjectRepository.findAll();
//        listProjects.forEach(System.out::println);
//    }
//
//    @Test
//    public void testUpdateEmployeeProject() {
//        EmployeeProject employeeProject = employeeProjectRepository.findById(291L).get();
//        employeeProject.setEmployeeBookedMonths(17.0);
//        employeeProject.setProject(new Project(7L));
//    }
//
//    @Test
//    public void testDeleteEmployeeProject() {
//        employeeProjectRepository.deleteById(16L);
//    }
//}
