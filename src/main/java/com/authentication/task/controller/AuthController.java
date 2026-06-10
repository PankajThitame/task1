package com.authentication.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.authentication.task.dto.AuthResponseDTO;
import com.authentication.task.dto.LoginRequestDTO;
import com.authentication.task.dto.RegisterRequestDTO;
import com.authentication.task.entity.Employee;
import com.authentication.task.entity.User;
import com.authentication.task.repository.EmployeeRepository;
import com.authentication.task.repository.UserRepository;
import com.authentication.task.security.JwtService;
import com.authentication.task.services.AuthService;

import jakarta.validation.Valid;

/**
 * Handles user registration and login.
 * These endpoints are publicly accessible (no JWT required).
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequestDTO request) {

        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody LoginRequestDTO request) {

        return ResponseEntity.ok(
                authService.login(request));
    }
}