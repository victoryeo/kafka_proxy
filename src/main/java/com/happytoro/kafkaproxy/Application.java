package com.happytoro.kafkaproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer { // 1. Extend the initializer

	@Override // 2. Override the configure method
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    // 3. Tell Spring Boot which is your primary application class
    return application.sources(Application.class);
  }

	// 4. Keep the main method for optional standalone JAR execution
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("Kafka Proxy running");
	}

}
