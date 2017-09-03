package com.ixortalk.ixortalk.aws.cognito.jwt.sampleapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SampleCognitoApp {

	public static void main(String[] args) {
		SpringApplication.run(SampleCognitoApp.class, args);
	}
}
