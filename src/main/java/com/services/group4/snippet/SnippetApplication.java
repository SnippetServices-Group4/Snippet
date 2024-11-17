package com.services.group4.snippet;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Generated
@SpringBootApplication
@EnableFeignClients
public class SnippetApplication {

  public static void main(String[] args) {
    DotenvConfig.loadEnv();
    SpringApplication.run(SnippetApplication.class, args);
  }
}
