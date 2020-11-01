package com.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.study.*")
public class BootReferencePrj1Application {
	
	private static Logger log = LoggerFactory.getLogger(BootReferencePrj1Application.class);

	public static void main(String[] args) {
		SpringApplication.run(BootReferencePrj1Application.class, args);
		log.info("Starter application Started!");
	}

}
