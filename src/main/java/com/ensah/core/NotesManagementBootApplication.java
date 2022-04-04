package com.ensah.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class NotesManagementBootApplication {



	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NotesManagementBootApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(NotesManagementBootApplication.class, args);
	}

}
