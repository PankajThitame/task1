package com.authentication.task.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.authentication.task.entity.Employee;

public interface EmployeeService {

    Employee save(Employee employee);

    List<Employee> getAll();
    
    public Page<Employee> getEmployees(Pageable pageable);

    Employee getById(Long id);

    Employee update(Long id, Employee employee);

    void delete(Long id);
}