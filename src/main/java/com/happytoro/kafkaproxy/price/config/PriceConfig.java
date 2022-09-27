package com.happytoro.kafkaproxy.price.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application.properties")
public class PriceConfig {
    
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource priceDataSource() {
        return DataSourceBuilder.create().build();
    }
}
