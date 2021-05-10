package com.sap.sfsf.reshuffle.applicants.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:envConfig.properties")
public class EnvConfig {
    private Logger logger = LoggerFactory.getLogger(EnvConfig.class);
    private final int DEFAULT_INT = -1;
    
    @Value("${btp.destination}")
    private String destinationName;
    
    @Bean
    public String getDestinationName() {
        return destinationName;
    }
}