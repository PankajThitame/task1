package com.authentication.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.authentication.task.dto.LoginRequest;
import com.authentication.task.entity.Employee;
import com.authentication.task.entity.User;
import com.authentication.task.repository.EmployeeRepository;
import com.authentication.task.repository.UserRepository;
import com.authentication.task.serviceImpl.JwtService;

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

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody com.authentication.task.dto.User dto) {

        User user = new User();
        Employee employee = new Employee();

        user.setUsername(dto.getEmail());
        user.setPassword(
                passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        
        employee.setDepartment(dto.getDepartment());
        employee.setEmail(dto.getEmail());
        employee.setPosition(dto.getPosition());
        employee.setName(dto.getName());
        employee.setSalary(dto.getSalary());

        employeeRepository.save(employee);
        userRepository.save(user);

        return ResponseEntity.ok(
                "User Registered Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        String token =
                jwtService.generateToken(
                        request.getUsername());

        return ResponseEntity.ok(token);
    }
}