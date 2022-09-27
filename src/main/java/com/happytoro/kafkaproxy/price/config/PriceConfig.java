package com.happytoro.kafkaproxy.price.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "priceEntityManagerFactory",
    transactionManagerRef = "priceTransactionManager",
    basePackages = {"com.happytoro.kafkaproxy.price.repository"}
)
public class PriceConfig {
    
    @Primary
    @Bean(name = "priceDataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource priceDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "priceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean priceEntityManagerFactory(org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder builder, @Qualifier("priceDataSource") DataSource priceDataSource) {
        return builder.dataSource(priceDataSource).packages("com.happytoro.kafkaproxy.price.model")
           .persistenceUnit("priceDB").build();
    }

    @Primary
    @Bean(name = "priceTransactionManager")
    public PlatformTransactionManager priceTransactionManager(
        @Qualifier("priceEntityManagerFactory") EntityManagerFactory priceEntityManagerFactory
    ) {
        return new JpaTransactionManager(priceEntityManagerFactory);
    }
}
