package com.authentication.task.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for user registration. Carries both auth credentials
 * and the initial employee profile data.
 */
@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String department;

    private String position;

    private Double salary;

    @NotNull(message = "Date of joining is required")
    private LocalDate dateOfJoining;

    private String role;
}
