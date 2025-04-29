package com.example.servlet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.example")
@Import(DatabaseConfig.class)
public class AppConfig {

    @Bean
    public String welcomeMessage() {
        return "Welcome to our Servlet API Application!";
    }
}
