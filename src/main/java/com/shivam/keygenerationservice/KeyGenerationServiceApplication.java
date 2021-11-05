package com.shivam.keygenerationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages =  "com.shivam.keygenerationservice")
public class KeyGenerationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyGenerationServiceApplication.class, args);
	}

}
