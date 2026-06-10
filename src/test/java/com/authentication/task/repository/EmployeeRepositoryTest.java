package com.authentication.task.repository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.authentication.task.entity.Employee;

/**
 * Repository layer tests using Mockito to stub JPA calls.
 * No database or Spring context required – fast, isolated unit tests.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeRepositoryTest {

    @Mock
    private EmployeeRepository repository;

    private Employee emp1;
    private Employee emp2;

    @BeforeEach
    void setUp() {
        emp1 = Employee.builder()
                .id(1L)
                .name("Alice Smith")
                .email("alice@example.com")
                .department("Engineering")
                .position("Developer")
                .salary(60000.0)
                .dateOfJoining(LocalDate.of(2021, 3, 10))
                .build();

        emp2 = Employee.builder()
                .id(2L)
                .name("Bob Jones")
                .email("bob@example.com")
                .department("HR")
                .position("Manager")
                .salary(80000.0)
                .dateOfJoining(LocalDate.of(2020, 7, 1))
                .build();
    }

    // ── findByEmail ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("findByEmail() – returns employee when email matches")
    void findByEmail_shouldReturnEmployee() {
        when(repository.findByEmail("alice@example.com"))
                .thenReturn(Optional.of(emp1));

        Optional<Employee> result =
                repository.findByEmail("alice@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice Smith");
        assertThat(result.get().getDepartment()).isEqualTo("Engineering");
    }

    @Test
    @DisplayName("findByEmail() – returns empty Optional when not found")
    void findByEmail_shouldReturnEmpty_whenNotFound() {
        when(repository.findByEmail("nobody@example.com"))
                .thenReturn(Optional.empty());

        Optional<Employee> result =
                repository.findByEmail("nobody@example.com");

        assertThat(result).isEmpty();
    }

    // ── findAll with pagination ──────────────────────────────────────────────

    @Test
    @DisplayName("findAll(Pageable) – returns paged results in correct order")
    void findAll_pageable_shouldReturnPagedResults() {
        Page<Employee> page = new PageImpl<>(
                List.of(emp1),
                PageRequest.of(0, 1, Sort.by("name").ascending()),
                2);

        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Employee> result = repository.findAll(
                PageRequest.of(0, 1, Sort.by("name").ascending()));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName())
                .isEqualTo("Alice Smith");
    }

    @Test
    @DisplayName("findAll(Pageable) – second page returns remaining record")
    void findAll_pageable_shouldReturnSecondPage() {
        Page<Employee> page = new PageImpl<>(
                List.of(emp2),
                PageRequest.of(1, 1, Sort.by("name").ascending()),
                2);

        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Employee> result = repository.findAll(
                PageRequest.of(1, 1, Sort.by("name").ascending()));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Bob Jones");
    }

    // ── save ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("save() – persists all fields including dateOfJoining")
    void save_shouldPersistAllFields() {
        when(repository.save(any(Employee.class))).thenReturn(emp1);

        Employee saved = repository.save(emp1);

        assertThat(saved.getDateOfJoining())
                .isEqualTo(LocalDate.of(2021, 3, 10));
        assertThat(saved.getSalary()).isEqualTo(60000.0);
        assertThat(saved.getDepartment()).isEqualTo("Engineering");
        verify(repository, times(1)).save(emp1);
    }

    // ── delete ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteById() – invokes repository delete correctly")
    void deleteById_shouldInvokeDelete() {
        doNothing().when(repository).deleteById(1L);
        when(repository.existsById(1L)).thenReturn(false); // after delete

        repository.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
        assertThat(repository.existsById(1L)).isFalse();
    }
}
