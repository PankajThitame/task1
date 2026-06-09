package com.authentication.task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.task.entity.Employee;
import com.authentication.task.entity.User;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<User> findByEmail(String email);
}
