package com.amblessed.springboottesting.repository;



/*
 * @Project Name: springboot-testing
 * @Author: Okechukwu Bright Onwumere
 * @Created: 19-Feb-25
 */


import com.amblessed.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
}
