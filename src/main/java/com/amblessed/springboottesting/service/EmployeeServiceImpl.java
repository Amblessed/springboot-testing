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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceAlreadyExistsException("Employee with given email: " + employee.getEmail() + " already exists");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return employeeRepository.save(updatedEmployee);
        }
        throw new ResourceNotFoundException("Employee with id: " + id + " not found");
    }

    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }
}
