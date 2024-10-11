package com.services.group4.snippet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnippetApplication {

	public static void main(String[] args) {
		DotenvConfig.loadEnv();
		SpringApplication.run(SnippetApplication.class, args);
	}

}
