package com.authentication.task.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.authentication.task.dto.EmployeeDTO;

public interface EmployeeService {

    EmployeeDTO save(EmployeeDTO dto);

    Page<EmployeeDTO> getEmployees(Pageable pageable);

    EmployeeDTO getById(Long id);

    EmployeeDTO update(Long id, EmployeeDTO dto);

    void delete(Long id);
}