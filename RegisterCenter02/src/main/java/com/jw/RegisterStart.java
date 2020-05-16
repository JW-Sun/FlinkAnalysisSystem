package com.jw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RegisterStart {
    public static void main(String[] args) {
        SpringApplication.run(RegisterStart.class, args);
    }
}
