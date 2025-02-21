package com.amblessed.springboottesting.service;



/*
 * @Project Name: springboot-testing
 * @Author: Okechukwu Bright Onwumere
 * @Created: 21-Feb-25
 */

import com.amblessed.springboottesting.exception.ResourceAlreadyExistsException;
import com.amblessed.springboottesting.exception.ResourceNotFoundException;
import com.amblessed.springboottesting.model.Employee;
import com.amblessed.springboottesting.repository.EmployeeRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@EnabledForJreRange(min = JRE.JAVA_17) // This annotation is used to enable the test for Java 17 and above since we are using Spring Boot 3
@ExtendWith(MockitoExtension.class)  // This is needed when we want to use annotations to mock the dependencies
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Faker faker;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        // employeeRepository = Mockito.mock(EmployeeRepository.class)
        // employeeService = new EmployeeServiceImpl(employeeRepository)
        faker = new Faker();
        employee = Employee.builder()
                .id(1L)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();

    }

    @Test
    @DisplayName("JUnit test for injected component are not null")
    @Order(1)
    void injectedComponentAreNotNull(){
        assertThat(employeeRepository).isNotNull();
        assertThat(employeeService).isNotNull();
        assertNotNull(employeeRepository);   // Check that employeeRepository was successfully mocked
        assertNotNull(employeeService);      // Check that employeeService is not null
    }

    @Test
    @DisplayName("JUnit test for saveEmployee method")
    @Order(2)
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        //given - precondition or set-up
        // this is the mocking part
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behavior that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then: Using AssertJ for assertions
        assertThat(employee).isEqualTo(savedEmployee);
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for saveEmployee method - Throw Exception")
    @Order(3)
    void givenEmployeeObjectWithExistingEmail_whenSaveEmployee_thenThrowException() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // given(employeeRepository.save(employee)).willReturn(employee): this is not needed because we are testing the exception

        //when - action or the behaviour that we are going test
        assertThrows(ResourceAlreadyExistsException.class, () -> employeeService.saveEmployee(employee));
        assertThrows(RuntimeException.class, () -> employeeService.saveEmployee(employee)); // Would pass because RuntimeException is a superclass of ResourceAlreadyExistsException

        //assertThrowsExactly does not follow an inheritance hierarchy, it checks for exact match
        assertThrowsExactly(ResourceAlreadyExistsException.class, () -> employeeService.saveEmployee(employee));

        assertThatThrownBy(() -> employeeService.saveEmployee(employee)).isInstanceOf(ResourceAlreadyExistsException.class);

        //then: Using AssertJ for assertions
        verify(employeeRepository, never()).save(any(Employee.class)); // Verify that the save method was never called
    }

    @Test
    @DisplayName("JUnit test for Get All Employees method - Positive Scenario")
    @Order(4)
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList() {

        //given - precondition or setup
        List<Employee> employeesList = new ArrayList<>();
        for (int i = 1; i <= 5; i++){
            employeesList.add(new Employee((long) i, faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress()));
        }
        given(employeeRepository.findAll()).willReturn(employeesList);

        //when - action or the behaviour that we are going test
        List<Employee> employees = employeeService.getAllEmployees();
        assertEquals(5,  employees.size());

        //then: Using AssertJ for assertions
        assertThat(employees).isNotNull().hasSize(5).isNotEmpty();
    }

    @Test
    @DisplayName("JUnit test for Get All Employees method - Negative Scenario")
    @Order(5)
    void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {

        List<Employee> emptyEmployeesList = List.of();
        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(emptyEmployeesList);

        //when - action or the behaviour that we are going test
        List<Employee> employees = employeeService.getAllEmployees();

        //then: Using AssertJ for assertions
        assertThat(employees).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("JUnit test for Get Employee By Id")
    @Order(6)
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //then: Using AssertJ for assertions
        assertThat(savedEmployee).isNotNull();
        assertThat(employee).isEqualTo(savedEmployee);
        assertNotNull(savedEmployee);
        assertEquals(employee, savedEmployee);
    }

    @Test
    @DisplayName("JUnit test for Update Employee - Positive Scenario")
    @Order(7)
    void givenEmployeeIdAndEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject(){
        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        given(employeeRepository.save(employee)).willReturn(employee);

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeService.updateEmployee(employee.getId(), employee);

        //then: Using AssertJ for assertions
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo(firstName);
        assertThat(savedEmployee.getLastName()).isEqualTo(lastName);
        assertThat(savedEmployee.getEmail()).isEqualTo(email);

    }

    @Test
    @DisplayName("JUnit test for Update Employee - Negative Scenario")
    @Order(7)
    void givenInvalidEmployeeIdAndEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject(){
        //given - precondition or setup
        long leftLimit = 7878787878L;
        long rightLimit = 9898989898L;
        long generatedLongId = leftLimit + (new Random().nextLong() * (rightLimit - leftLimit));
        given(employeeRepository.findById(generatedLongId)).willReturn(Optional.empty());

        Employee updatedEmployee = Employee.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();


        //when - action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(generatedLongId, updatedEmployee));
        assertThrows(RuntimeException.class, () -> employeeService.updateEmployee(generatedLongId, updatedEmployee)); // Would pass because RuntimeException is a superclass of ResourceAlreadyExistsException

        //assertThrowsExactly does not follow an inheritance hierarchy, it checks for exact match
        assertThrowsExactly(ResourceNotFoundException.class, () -> employeeService.updateEmployee(generatedLongId, updatedEmployee));

        assertThatThrownBy(() -> employeeService.updateEmployee(generatedLongId, updatedEmployee)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("JUnit test for Delete Employee method")
    @Order(8)
    void givenEmployeeId_whenDeleteEmployee_thenReturnVoid(){
        Long id = 1L;

        //given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(id); // How we mock methods that are void

        //when - action or the behaviour that we are going test
        employeeService.deleteEmployeeById(id);

        verify(employeeRepository, times(1)).deleteById(id);
    }

}
