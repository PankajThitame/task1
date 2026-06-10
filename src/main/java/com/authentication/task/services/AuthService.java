package com.authentication.task.services;

import com.authentication.task.dto.AuthResponseDTO;
import com.authentication.task.dto.LoginRequestDTO;
import com.authentication.task.dto.RegisterRequestDTO;

public interface AuthService {

    void register(RegisterRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);
}