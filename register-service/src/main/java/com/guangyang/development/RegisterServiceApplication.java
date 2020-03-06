package com.guangyang.development;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.guangyang.development.register.mapper")
public class RegisterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisterServiceApplication.class, args);
    }

}
