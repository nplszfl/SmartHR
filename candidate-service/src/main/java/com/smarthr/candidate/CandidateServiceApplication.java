package com.smarthr.candidate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.smarthr.candidate.repository")
@ComponentScan(basePackages = {"com.smarthr.candidate", "com.smarthr.common"})
public class CandidateServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CandidateServiceApplication.class, args);
    }
}