package com.happytoro.kafkaproxy.openOrders.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("application.properties")
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "openOrderEntityManagerFactory",
    transactionManagerRef = "openOrderTransactionManager",
    basePackages = {"com.happytoro.kafkaproxy.openOrders.repository"}
    )
public class OpenOrderConfig {
    
    @Bean(name = "openOrderDataSource")
    @ConfigurationProperties("spring.datasource2")
    public DataSource openOrderDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "openOrderEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean openOrderEntityManagerFactory(
        EntityManagerFactoryBuilder builder, @Qualifier("openOrderDataSource") DataSource openOrderDataSource
    ) {
        return builder
           .dataSource(openOrderDataSource)
           .packages("com.happytoro.kafkaproxy.openOrders.model")
           .persistenceUnit("openOrderDB")
           .build();
    }

    @Bean(name = "openOrderTransactionManager")
    public PlatformTransactionManager openOrderTransactionManager(
        @Qualifier("openOrderEntityManagerFactory") EntityManagerFactory openOrderEntityManagerFactory
    ) {
        return new JpaTransactionManager(openOrderEntityManagerFactory);
    }
}
