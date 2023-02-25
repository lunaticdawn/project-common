package com.project.cmn.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.project.cmn")
public class Applicaiton extends SpringBootServletInitializer {
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Applicaiton.class, args);
    }
}
