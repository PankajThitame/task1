package com.authentication.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response body returned after a successful login.
 * Contains the JWT token.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
}