package com.happytoro.kafkaproxy.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.happytoro.kafkaproxy.kafka.KafkaMessageConfig.MessageProducer;
import com.happytoro.kafkaproxy.model.Order;

@RestController
@RequestMapping("/api/")
public class ProxyController {
	@Value("${spring.application.name}")
	String appName;

    Logger logger = LogManager.getLogger(RestController.class);
    private ApplicationContext context;

    @Autowired
    public void context(ApplicationContext context) { this.context = context; }

    @GetMapping("/appname")
    public String appname() {
        logger.info(appName);
        return appName;
    }

    @PostMapping("/order")
    public String createOrder(@RequestBody Order order) {
        logger.info("createOrder");
        MessageProducer producer = this.context.getBean(MessageProducer.class);
        String orderStr = "Order " + order.getOrderType() + " " + order.getPrice() + " " + order.getQuantity();
		producer.sendMessage(orderStr);
        return "ok";
    }
}