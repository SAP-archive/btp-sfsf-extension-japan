package com.sap.sfsf.reshuffle.applicants.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:envConfig.properties")
public class EnvConfig {
    private final int DEFAULT_INT = -1;

    @Value("${scp.destination}")
    private String destinationName;

    @Value("${sfsf.terminationcode}")
    private String terminationCode;

    @Value("${default.timezone}")
    private String timezone;

    @Value("${default.exception.int}")
    private int exceptionalInt = DEFAULT_INT;

    @Value("${sfsf.servicepath}")
    private String servicePath;

    @Bean
    public String getDestinationName() {
        return destinationName;
    }

    @Bean
    public String getTerminationCode() {
        return terminationCode;
    }

    @Bean
    public String getTimezone() {
        return timezone;
    }

    @Bean
    public int getExceptinalInt() {
        return exceptionalInt;
    }

    @Bean
    public String getServicePath() {
        return servicePath;
    }
}
