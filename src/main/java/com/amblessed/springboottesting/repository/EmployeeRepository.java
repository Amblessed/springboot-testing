package com.amblessed.springboottesting.repository;



/*
 * @Project Name: springboot-testing
 * @Author: Okechukwu Bright Onwumere
 * @Created: 19-Feb-25
 */


import com.amblessed.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    //JPQL Query uses the entity name and its properties to fetch data

    // define a custom query using JPQL with index params
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 and e.lastName = ?2")
    Employee findByFirstNameAndLastNameIndexParams(String firstName, String lastName);

    // define a custom query using JPQL with named params
    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName and e.lastName = :lastName")
    Employee findByFirstNameAndLastNameNamedParams(@Param("firstName")String firstName, @Param("lastName")String lastName);

    // define a custom query using native SQL with index params
    @Query(value = "SELECT * FROM tbl_employees e WHERE e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
    Employee findByFirstNameAndLastNameNativeIndexParams(String firstName, String lastName);

    // define a custom query using native SQL with index params
    @Query(value = "SELECT * FROM tbl_employees e WHERE e.first_name = :firstName and e.last_name = :lastName", nativeQuery = true)
    Employee findByFirstNameAndLastNameNativeNamedParams(@Param("firstName")String firstName, @Param("lastName")String lastName);
}
