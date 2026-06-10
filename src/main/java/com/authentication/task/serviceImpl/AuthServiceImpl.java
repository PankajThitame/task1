package com.authentication.task.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authentication.task.dto.AuthResponseDTO;
import com.authentication.task.dto.LoginRequestDTO;
import com.authentication.task.dto.RegisterRequestDTO;
import com.authentication.task.entity.Employee;
import com.authentication.task.entity.User;
import com.authentication.task.repository.EmployeeRepository;
import com.authentication.task.repository.UserRepository;
import com.authentication.task.security.JwtService;
import com.authentication.task.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

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

    @Override
    public void register(RegisterRequestDTO request) {

        if (userRepository.findByUsername(request.getUsername())
                .isPresent()) {

            throw new RuntimeException(
                    "Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));
        user.setRole(
                request.getRole() != null
                        ? request.getRole()
                        : "USER");

        userRepository.save(user);

        Employee employee = new Employee();

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(
                request.getDepartment());
        employee.setPosition(
                request.getPosition());
        employee.setSalary(
                request.getSalary());
        employee.setDateOfJoining(
                request.getDateOfJoining());

        employeeRepository.save(employee);
    }

    @Override
    public AuthResponseDTO login(
            LoginRequestDTO request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

        } catch (BadCredentialsException ex) {

            throw new RuntimeException(
                    "Invalid username or password");
        }

        String token = jwtService.generateToken(
                request.getUsername());

        return new AuthResponseDTO(token);
    }
}