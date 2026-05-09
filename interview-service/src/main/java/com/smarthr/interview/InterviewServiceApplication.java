package com.smarthr.interview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.smarthr.interview.repository")
@ComponentScan(basePackages = {"com.smarthr.interview", "com.smarthr.common"})
public class InterviewServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterviewServiceApplication.class, args);
    }
}