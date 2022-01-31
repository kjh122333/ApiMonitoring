package com.duzon.dbp.apimonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ApiMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiMonitoringApplication.class, args);
	}

	@Bean // BEAN 등록 : PasswordEncoder
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	// http://15.165.25.145:9500/swagger-ui.html#!
	
	// http://localhost:9500/swagger-ui.html#!
}
