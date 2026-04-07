package com.mphasis.employeeservice.repository;

import com.mphasis.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
