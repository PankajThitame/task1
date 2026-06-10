package com.authentication.task.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO used for creating and updating employee records.
 * Keeps the API contract decoupled from the entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "Employee name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String department;

    private String position;

    @Positive(message = "Salary must be a positive value")
    private Double salary;

    @NotNull(message = "Date of joining is required")
    private LocalDate dateOfJoining;
}
