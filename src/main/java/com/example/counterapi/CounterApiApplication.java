package com.example.counterapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CounterApiApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(CounterApiApplication.class, args);
    }
}
