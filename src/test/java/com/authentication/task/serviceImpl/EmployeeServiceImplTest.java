package com.authentication.task.serviceImpl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.authentication.task.dto.EmployeeDTO;
import com.authentication.task.entity.Employee;
import com.authentication.task.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Unit tests for {@link EmployeeServiceImpl}.
 * Uses Mockito to stub the repository so no DB is needed.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeServiceImpl service;

    private Employee sampleEmployee;
    private EmployeeDTO sampleDTO;

    @BeforeEach
    void setUp() {
        sampleEmployee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .position("Senior Developer")
                .salary(75000.0)
                .dateOfJoining(LocalDate.of(2023, 1, 15))
                .build();

        sampleDTO = EmployeeDTO.builder()
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .position("Senior Developer")
                .salary(75000.0)
                .dateOfJoining(LocalDate.of(2023, 1, 15))
                .build();
    }

    // ── save ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("save() – persists employee and returns DTO with generated id")
    void save_shouldPersistAndReturnDTO() {
        when(repository.save(any(Employee.class))).thenReturn(sampleEmployee);

        EmployeeDTO result = service.save(sampleDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getDateOfJoining()).isEqualTo(LocalDate.of(2023, 1, 15));
        verify(repository, times(1)).save(any(Employee.class));
    }

    // ── getById ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getById() – returns DTO when employee exists")
    void getById_shouldReturnDTO_whenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleEmployee));

        EmployeeDTO result = service.getById(1L);

        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getSalary()).isEqualTo(75000.0);
    }

    @Test
    @DisplayName("getById() – throws EntityNotFoundException when not found")
    void getById_shouldThrow_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── getEmployees (pagination) ────────────────────────────────────────────

    @Test
    @DisplayName("getEmployees() – returns paged results")
    void getEmployees_shouldReturnPage() {
        Page<Employee> page = new PageImpl<>(List.of(sampleEmployee));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<EmployeeDTO> result = service.getEmployees(PageRequest.of(0, 5));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEmail())
                .isEqualTo("john@example.com");
    }

    // ── update ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update() – updates and returns modified DTO")
    void update_shouldModifyAndReturnDTO() {
        EmployeeDTO updateDTO = EmployeeDTO.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .department("Product")
                .position("Manager")
                .salary(95000.0)
                .dateOfJoining(LocalDate.of(2022, 6, 1))
                .build();

        Employee updated = Employee.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane@example.com")
                .department("Product")
                .position("Manager")
                .salary(95000.0)
                .dateOfJoining(LocalDate.of(2022, 6, 1))
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        when(repository.save(any(Employee.class))).thenReturn(updated);

        EmployeeDTO result = service.update(1L, updateDTO);

        assertThat(result.getName()).isEqualTo("Jane Doe");
        assertThat(result.getSalary()).isEqualTo(95000.0);
    }

    @Test
    @DisplayName("update() – throws EntityNotFoundException when not found")
    void update_shouldThrow_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, sampleDTO))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ── delete ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete() – calls deleteById when employee exists")
    void delete_shouldCallDeleteById_whenFound() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete() – throws EntityNotFoundException when not found")
    void delete_shouldThrow_whenNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
