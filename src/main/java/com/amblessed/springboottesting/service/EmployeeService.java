package com.amblessed.springboottesting.service;



/*
 * @Project Name: springboot-testing
 * @Author: Okechukwu Bright Onwumere
 * @Created: 21-Feb-25
 */


import com.amblessed.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee updatedEmployee);
    void deleteEmployeeById(Long id);
}
