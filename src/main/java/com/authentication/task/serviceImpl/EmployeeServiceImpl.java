package com.authentication.task.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.authentication.task.dto.EmployeeDTO;
import com.authentication.task.entity.Employee;
import com.authentication.task.repository.EmployeeRepository;
import com.authentication.task.services.EmployeeService;

import jakarta.persistence.EntityNotFoundException;

/**
 * Implementation of {@link EmployeeService} providing full CRUD
 * with pagination and sorting via Spring Data JPA.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    // ---------------------------------------------------------------
    // Create
    // ---------------------------------------------------------------

    @Override
    public EmployeeDTO save(EmployeeDTO dto) {
        Employee employee = toEntity(dto);
        Employee saved = repository.save(employee);
        return toDTO(saved);
    }

    // ---------------------------------------------------------------
    // Read
    // ---------------------------------------------------------------

    @Override
    public Page<EmployeeDTO> getEmployees(Pageable pageable) {
        return repository.findAll(pageable)
                         .map(this::toDTO);
    }

    @Override
    public EmployeeDTO getById(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Employee not found with id: " + id));
        return toDTO(employee);
    }

    // ---------------------------------------------------------------
    // Update
    // ---------------------------------------------------------------

    @Override
    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Employee not found with id: " + id));

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setDepartment(dto.getDepartment());
        existing.setPosition(dto.getPosition());
        existing.setSalary(dto.getSalary());
        existing.setDateOfJoining(dto.getDateOfJoining());

        Employee updated = repository.save(existing);
        return toDTO(updated);
    }

    // ---------------------------------------------------------------
    // Delete
    // ---------------------------------------------------------------

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Employee not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // ---------------------------------------------------------------
    // Mapping Helpers
    // ---------------------------------------------------------------

    private Employee toEntity(EmployeeDTO dto) {
        return Employee.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .position(dto.getPosition())
                .salary(dto.getSalary())
                .dateOfJoining(dto.getDateOfJoining())
                .build();
    }

    private EmployeeDTO toDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .dateOfJoining(employee.getDateOfJoining())
                .build();
    }
}
