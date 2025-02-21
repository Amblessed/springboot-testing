package com.amblessed.springboottesting.repository;



/*
 * @Project Name: springboot-testing
 * @Author: Okechukwu Bright Onwumere
 * @Created: 19-Feb-25
 */


import com.amblessed.springboottesting.model.Employee;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest // This annotation is used to test JPA repositories
@EnabledForJreRange(min = JRE.JAVA_17) // This annotation is used to enable the test for Java 17 and above since we are using Spring Boot 3
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Faker faker;
    private Employee employee;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        employeeRepository.deleteAll();  // This is to ensure the in-memory database is empty before each test
        employee = Employee.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();
    }

    @Test
    @DisplayName("JUnit test for injected component are not null")
    void injectedComponentAreNotNull(){
        assertThat(employeeRepository).isNotNull();
    }


    @Test
    @DisplayName("JUnit Test for saving an employee")
    @Order(1)
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        //given (or Arrange) - precondition or setup
        /* employee object already created in the set-up method*/

        //when (or Act) - action or behavior that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then (or Assert) - the expected result
        assertTrue(savedEmployee.getId() > 0);
        assertThat(savedEmployee.getId()).isPositive();
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for findAll method")
    @Order(2)
    void givenEmployeeList_whenFindAll_thenReturnEmployeeList(){
        //given (or Arrange) - precondition or setup
        Employee emp;
        for (int i = 0; i < 5; i++){
            emp = Employee.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .build();
            employeeRepository.save(emp);
        }

        //when (or Act) - action or the behaviour that we are going test
        List<Employee> employees = employeeRepository.findAll();

        //then (or Assert)
        assertThat(employees).isNotEmpty().hasSize(5);
        assertEquals(5, employees.size());
        assertNotNull(employees);
    }


    @Test
    @DisplayName("JUnit test for find employee by Id")
    @Order(3)
    void givenEmployeeId_whenFindById_thenReturnEmployeeObject(){
        //given (or Arrange) - precondition or setup
        employeeRepository.save(employee);

        //when (or Act) - action or the behaviour that we are going test
        Employee foundEmployee = employeeRepository.findById(employee.getId()).orElse(null);

        //then (or Assert)
        assertNotNull(foundEmployee);
        assertEquals(employee.getId(), foundEmployee.getId());
        assertEquals(employee.getFirstName(), foundEmployee.getFirstName());
    }

    @Test
    @DisplayName("JUnit test for not existing employee id")
    @Order(4)
    void givenNotExistingEmployeeId_whenFindById_thenReturnNull(){
        //given (or Arrange) - precondition or setup
        /* employee object already created in the set-up method*/

        //when (or Act) - action or the behaviour that we are going test
        Employee notFoundEmployee = employeeRepository.findById(112233445566778899L).orElse(null);

        //then (or Assert)
        assertNull(notFoundEmployee);
    }


    @Test
    @DisplayName("JUnit test for find employee by email")
    @Order(5)
    void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee(){
        //given (or Arrange) - precondition or setup
        employeeRepository.save(employee);

        //when (or Act) - action or the behaviour that we are going test
        Employee foundEmployee = employeeRepository.findByEmail(employee.getEmail()).orElse(null);

        //then (or Assert)
        assertNotNull(foundEmployee);
        assertEquals(employee.getEmail(), foundEmployee.getEmail());
    }

    @Test
    @DisplayName("Not existing employee email returns null")
    @Order(6)
    void givenNotExistingEmployeeEmail_whenFindByEmail_thenReturnNullEmployee(){
        //given (or Arrange) - precondition or setup
        employeeRepository.save(employee);

        //when (or Act) - action or the behaviour that we are going test
        Employee notFoundEmployee = employeeRepository.findByEmail("notexisting.notexisting@nonemail.com").orElse(null);

        //then (or Assert)
        assertNull(notFoundEmployee);

    }

    @Test
    @DisplayName("JUnit test for update an employee details ")
    @Order(7)
    void givenEmployee_whenUpdate_thenReturnUpdatedEmployee(){
        //given (or Arrange) - precondition or setup
        employeeRepository.save(employee);

        //when (or Act) - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).orElse(null);
        assert savedEmployee != null;
        savedEmployee.setFirstName("UpdatedFirstName");
        savedEmployee.setLastName("UpdatedLastName");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then (or Assert)
        assertNotNull(updatedEmployee);
        assertEquals("UpdatedFirstName", updatedEmployee.getFirstName());
        assertEquals("UpdatedLastName", updatedEmployee.getLastName());
    }

    @Test
    @DisplayName("JUnit test for delete an employee")
    @Order(8)
    void givenEmployee_whenDelete_thenRemoveEmployeeAndReturnNoContent(){
        //given (or Arrange) - precondition or setup
        employeeRepository.save(employee);

        //when (or Act) - action or the behaviour that we are going test
        employeeRepository.delete(employee);

        //then (or Assert)
        assertNull(employeeRepository.findById(employee.getId()).orElse(null));
        assertThat(employeeRepository.findById(employee.getId())).isEmpty();
    }


    @Test
    @DisplayName("JUnit test for find By FirstName and LastName using index params")
    @Order(9)
    void givenFirstNameAndLastName_whenFindByFistLastNameIndexParams_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameIndexParams(employee.getFirstName(), employee.getLastName());

        // then (or Assert)
        assertThat(savedEmployee).isNotNull(); // Using AssertJ for assertions
        assertNotNull(savedEmployee);   // Using JUnit for assertions
    }

    @Test
    @DisplayName("JUnit test for find By FirstName and LastName")
    @Order(10)
    void givenFirstNameAndLastName_whenFindByFistLastNameNamedParam_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameNamedParams(employee.getFirstName(), employee.getLastName());

        // then (or Assert)
        assertThat(savedEmployee).isNotNull(); // Using AssertJ for assertions
        assertNotNull(savedEmployee);   // Using JUnit for assertions

    }

    @Test
    @DisplayName("JUnit test for find By FirstName and LastName Native Query Index Param")
    @Order(11)
    void givenFirstNameAndLastName_whenFindByFistLastNameNativeIndexParams_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameNativeIndexParams(employee.getFirstName(), employee.getLastName());

        // then (or Assert)
        assertThat(savedEmployee).isNotNull(); // Using AssertJ for assertions
        assertNotNull(savedEmployee);   // Using JUnit for assertions
    }

    @Test
    @DisplayName("JUnit test for find By FirstName and LastName Native Query Named Param")
    @Order(12)
    void givenFirstNameAndLastName_whenFindByFistLastNameNativeNamedParams_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameNativeNamedParams(employee.getFirstName(), employee.getLastName());

        //then (or Assert)
        assertThat(savedEmployee).isNotNull(); // Using AssertJ for assertions
        assertNotNull(savedEmployee);   // Using JUnit for assertions
    }



}
