package com.authentication.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.authentication.task.dto.EmployeeDTO;
import com.authentication.task.services.EmployeeService;

import jakarta.validation.Valid;

/**
 * REST controller for Employee CRUD operations.
 * All endpoints are protected and require a valid JWT token.
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    /**
     * Create a new employee record.
     * POST /employees
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> save(
            @Valid @RequestBody EmployeeDTO dto) {

        EmployeeDTO saved = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * List all employees with pagination and sorting.
     * GET /employees?page=0&size=5&sortBy=name&direction=asc
     */
    @GetMapping
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0")    int    page,
            @RequestParam(defaultValue = "5")    int    size,
            @RequestParam(defaultValue = "id")   String sortBy,
            @RequestParam(defaultValue = "asc")  String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(service.getEmployees(pageable));
    }

    /**
     * Get a single employee by ID.
     * GET /employees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    /**
     * Update an existing employee.
     * PUT /employees/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Delete an employee by ID.
     * DELETE /employees/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}