package com.sap.sfsf.reshuffle.applicants.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


//Why we specify not only "sfsf" but also "cloud.sdk"?
@SpringBootApplication
@ComponentScan({"com.sap.cloud.sdk", "com.sap.sfsf.reshuffle.applicants"})
@ServletComponentScan({"com.sap.cloud.sdk", "com.sap.sfsf.reshuffle.applicants"})
public class Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
