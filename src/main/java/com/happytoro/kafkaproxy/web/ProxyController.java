package com.happytoro.kafkaproxy.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.happytoro.kafkaproxy.kafka.KafkaMessageConfig.MessageProducer;
import com.happytoro.kafkaproxy.model.Order;
import com.happytoro.kafkaproxy.model.OrderItem;
import com.happytoro.kafkaproxy.openOrders.model.OpenOrder;
import com.happytoro.kafkaproxy.openOrders.service.OpenOrderService;

@RestController
@RequestMapping("/api/")
public class ProxyController {
	@Value("${spring.application.name}")
	String appName;

    Logger logger = LogManager.getLogger(RestController.class);
    private ApplicationContext context;

    @Autowired
    public void context(ApplicationContext context) { this.context = context; }

    @Autowired
    private OpenOrderService openOrderService;

    @GetMapping("/appname")
    public ResponseEntity<String> appname() {
        logger.info(appName);
        return ResponseEntity.status(HttpStatus.OK).body(appName);
    }

    @PostMapping("/order")
    public ResponseEntity<String> createOrder(@RequestBody Order order) throws Exception {
        MessageProducer producer = this.context.getBean(MessageProducer.class);

        // if there's an orderID and it's not unique, then throw error.
        if(order.getOrderID() != null && openOrderService.getOpenOrder(order.getOrderID()) != null) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OrderID already exists.");
        }

        String orderStr = "Order " +
          order.getTokenType() + " " + order.getTokenName() + " " +
          order.getOrderType() + " " + order.getPrice() + " " + order.getQuantity() + " " +
          order.getOrderID();
        logger.info("Received "+ orderStr);
    
        OrderItem orderItem = new OrderItem(order.getOrderID(), order.getTokenType(),
          order.getTokenName(), order.getOrderType(),
          order.getPrice(), order.getQuantity());
        producer.sendOrderMessage(orderItem);

        if (order.getOrderID() != null) {
          OpenOrder openOrder = new OpenOrder(order.getOrderID(), 
              order.getTokenType(), 
              order.getTokenName(), 
              order.getQuantity(), 
              order.getQuantity(), 
              order.getOrderType()
            );
          openOrderService.saveOpenOrder(openOrder);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}