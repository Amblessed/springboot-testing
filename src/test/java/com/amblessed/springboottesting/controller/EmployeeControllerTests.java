package com.amblessed.springboottesting.controller;



/*
 * @Project Name: springboot-testing
 * @Author: Okechukwu Bright Onwumere
 * @Created: 21-Feb-25
 */


import com.amblessed.springboottesting.model.Employee;
import com.amblessed.springboottesting.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    @DisplayName("JUnit test for injected component are not null")
    @Order(1)
    void injectedComponentAreNotNull(){
        assertThat(employeeService).isNotNull();
        assertThat(objectMapper).isNotNull();
        assertThat(mockMvc).isNotNull();
        assertNotNull(objectMapper);
        assertNotNull(employeeService);
        assertNotNull(mockMvc);
    }


    @Test
    @DisplayName("JUnit test for Create Employee ")
    @Order(2)
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given (or Arrange) - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();

        given(employeeService.saveEmployee(any(Employee.class))).willReturn(employee);
        // given(employeeService.saveEmployee(employee)).willAnswer(invocation -> invocation.getArgument(0))


        //when (or Act) - action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees")
                .content(objectMapper.writeValueAsString(employee))
                .contentType(MediaType.APPLICATION_JSON));

        //then (or Assert) - verify the result using assertions
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value(employee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(employee.getLastName()))
                .andExpect(jsonPath("$.email").value(employee.getEmail()));


        System.out.println("*********************************************************************");

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", CoreMatchers.is(employee.getId().intValue())))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    @DisplayName("JUnit test for Get All Employees")
    void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception {
        //given (or Arrange) - precondition or setup
        List<Employee> employeesList = new ArrayList<>();
        for (int i = 1; i <= 5; i++){
            employeesList.add(new Employee((long) i, faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress()));
        }
        given(employeeService.getAllEmployees()).willReturn(employeesList);

        //when (or Act) - action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees"));

        //then (or Assert)
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(5));
    }

    @Test
    @DisplayName("JUnit test for GET Employee By Id - Positive Scenario")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given (or Arrange) - precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when (or Act) - action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees/{id}", employeeId));

        //then (or Assert)
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeId))
                .andExpect(jsonPath("$.firstName").value(employee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(employee.getLastName()))
                .andExpect(jsonPath("$.email").value(employee.getEmail()));
    }

    @Test
    @DisplayName("JUnit test for GET Employee By Id - Negative Scenario")
    void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given (or Arrange) - precondition or setup
        Long invalidEmployeeId = 989898L;
        given(employeeService.getEmployeeById(invalidEmployeeId)).willReturn(Optional.empty());

        //when (or Act) - action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(get("/api/v1/employees/{id}", invalidEmployeeId));

        //then (or Assert)
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("JUnit test for update Employee - Positive Scenario")
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        //given (or Arrange) - precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(anyLong(), any(Employee.class))).willReturn(updatedEmployee);

        //when (or Act) - action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(put("/api/v1/employees/{id}", employeeId)
                .content(objectMapper.writeValueAsString(updatedEmployee))
                .contentType(MediaType.APPLICATION_JSON));

        //then (or Assert)
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updatedEmployee.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedEmployee.getLastName()))
                .andExpect(jsonPath("$.email").value(updatedEmployee.getEmail()));
    }

    @Test
    @DisplayName("JUnit test for update Employee - Negative Scenario")
    void givenInvalidUpdatedEmployeeId_whenUpdateEmployee_thenReturnStatusCode404() throws Exception {
        //given (or Arrange) - precondition or setup
        Long invalidEmployeeId = 989898989L;

        Employee updatedEmployee = Employee.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();

        given(employeeService.getEmployeeById(invalidEmployeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(anyLong(), any(Employee.class))).willReturn(updatedEmployee);

        //when (or Act) - action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(put("/api/v1/employees/{id}", invalidEmployeeId)
                .content(objectMapper.writeValueAsString(updatedEmployee))
                .contentType(MediaType.APPLICATION_JSON));

        //then (or Assert)
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("JUnit test for Delete Employee REST API - Positive Scenario")
    void givenValidEmployeeId_whenDeleteEmployee_thenReturnStatusCode200() throws Exception {
        //given (or Arrange) - precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        willDoNothing().given(employeeService).deleteEmployeeById(employeeId);

        //when (or Act) - action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/employees/{id}", employeeId));

        //then (or Assert)
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("JUnit test for Delete Employee REST API - Negative Scenario")
    void givenInvalidEmployeeId_whenDeleteEmployee_thenReturnStatusCode404() throws Exception {
        //given (or Arrange) - precondition or setup
        Long invalidEmployeeId = 98989898L;

        given(employeeService.getEmployeeById(invalidEmployeeId)).willReturn(Optional.empty());
        willDoNothing().given(employeeService).deleteEmployeeById(invalidEmployeeId);

        //when (or Act) - action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/employees/{id}", invalidEmployeeId));

        //then (or Assert)
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }


}
