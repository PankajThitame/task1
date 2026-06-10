package com.authentication.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}
	
//	@Value("${jwt.secret}")
//	private String secret;

//	@PostConstruct
//	public void test() {
//	    System.out.println("MAIN SECRET = " + secret);
//	}

}
