package com.sap.sfsf.reshuffle.applicants.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.sap.cloud.sdk", "com.sap.sfsf.reshuffle.applicants.backend"})
@ServletComponentScan({"com.sap.cloud.sdk", "com.sap.sfsf.reshuffle.applicants.backend"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
