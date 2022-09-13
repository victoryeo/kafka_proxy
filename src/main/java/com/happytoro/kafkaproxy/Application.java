package com.happytoro.kafkaproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.happytoro.kafkaproxy.kafka.KafkaMessageConfig.MessageProducer;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		System.out.println("Kafka Proxy running");

		MessageProducer producer = context.getBean(MessageProducer.class);
		producer.sendMessage("Hello, World!");

		// shutdown
		context.close();
  }
}
