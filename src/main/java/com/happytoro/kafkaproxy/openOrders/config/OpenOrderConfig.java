package com.happytoro.kafkaproxy.openOrders.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class OpenOrderConfig {
    
    @Bean
    @ConfigurationProperties("spring.datasource2")
    public DataSource openOrderDataSource() {
        return DataSourceBuilder.create().build();
    }
}
