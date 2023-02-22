package com.project.cmn.http;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.project.cmn")
@MapperScan(basePackages = "com.project.cmn.http.persistence.conditioncoupon.mapper", sqlSessionTemplateRef = "conditioncoupon-template", lazyInitialization = "true")
@MapperScan(basePackages = "com.project.cmn.http.persistence.lmmdev11.mapper", sqlSessionTemplateRef = "lmmdev11-template", lazyInitialization = "true")
public class Applicaiton extends SpringBootServletInitializer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Applicaiton.class, args);
    }
}
