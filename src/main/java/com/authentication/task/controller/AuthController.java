package com.authentication.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.authentication.task.dto.AuthResponse;
import com.authentication.task.dto.LoginRequest;
import com.authentication.task.dto.RegisterRequest;
import com.authentication.task.entity.Employee;
import com.authentication.task.entity.User;
import com.authentication.task.repository.EmployeeRepository;
import com.authentication.task.repository.UserRepository;
import com.authentication.task.serviceImpl.JwtService;

import jakarta.validation.Valid;

/**
 * Handles user registration and login.
 * These endpoints are publicly accessible (no JWT required).
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Register a new user and create their employee profile.
     *
     * POST /auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        // Check for duplicate username
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        // Persist auth credentials
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "USER");
        userRepository.save(user);

        // Persist employee profile
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setPosition(request.getPosition());
        employee.setSalary(request.getSalary());
        employee.setDateOfJoining(request.getDateOfJoining());
        employeeRepository.save(employee);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    /**
     * Authenticate a user and return a JWT token.
     *
     * POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        String token = jwtService.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}