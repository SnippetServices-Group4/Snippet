package com.services.group4.snippet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SnippetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnippetApplication.class, args);
	}

}
