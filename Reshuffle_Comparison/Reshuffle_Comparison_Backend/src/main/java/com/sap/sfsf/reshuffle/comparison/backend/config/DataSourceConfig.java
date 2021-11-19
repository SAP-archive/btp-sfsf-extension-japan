package com.sap.sfsf.reshuffle.comparison.backend.config;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    private HikariDataSource hikariDataSource;

    @Bean(destroyMethod = "")
    public DataSource dataSource() {

        JSONObject obj = new JSONObject(System.getenv("VCAP_SERVICES"));
        JSONArray arr = obj.getJSONArray("hana");

        String url = arr.getJSONObject(0).getJSONObject("credentials").getString("url");
        String user = arr.getJSONObject(0).getJSONObject("credentials").getString("user");
        String password = arr.getJSONObject(0).getJSONObject("credentials").getString("password");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(20);
        config.setIdleTimeout(60 * 1000);

        hikariDataSource = new HikariDataSource(config);
        return DataSourceBuilder
                .create()
                .type(hikariDataSource.getClass())
                .driverClassName(com.sap.db.jdbc.Driver.class.getName())
                .url(url)
                .username(user)
                .password(password)
                .build();
    }
}