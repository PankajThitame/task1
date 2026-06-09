package com.authentication.task.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String password;
    
    private String name;
    private String email;
    private String department;
    private String position;
    private Double salary;
    
    private String role;

    // getters setters
}