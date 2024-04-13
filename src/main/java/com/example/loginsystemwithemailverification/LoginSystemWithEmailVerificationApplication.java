package com.example.loginsystemwithemailverification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class LoginSystemWithEmailVerificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginSystemWithEmailVerificationApplication.class, args);
    }
}
