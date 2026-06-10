package com.authentication.task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.task.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
}
