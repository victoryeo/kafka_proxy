package com.happytoro.kafkaproxy.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;

import com.happytoro.kafkaproxy.kafka.KafkaMessageConfig.MessageProducer;
import com.happytoro.kafkaproxy.model.Order;
import com.happytoro.kafkaproxy.model.OrderItem;
import com.happytoro.kafkaproxy.model.PayloadJWT;
import com.happytoro.kafkaproxy.openOrders.model.OpenOrder;
import com.happytoro.kafkaproxy.openOrders.service.OpenOrderService;

import reactor.core.publisher.Mono;

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

    @Autowired
	private HttpServletRequest request;

    @Autowired
    WebClient createWebClient;

    @GetMapping("/appname")
    public ResponseEntity<String> appname() {
        logger.info(appName);
        return ResponseEntity.status(HttpStatus.OK).body(appName);
    }

    @PostMapping("/order")
    public ResponseEntity<String> createOrder(@RequestBody Order order) throws Exception {
        Map<String, String> bodyMap = new HashMap();
        String accessToken = request.getHeader("Access-Token");
        System.out.println(accessToken);
        bodyMap.put("access_token", accessToken);

        Mono<PayloadJWT> response = createWebClient.post()
                .uri("/userservice/validatetoken")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(bodyMap))
                .retrieve()
                .bodyToMono(PayloadJWT.class);

        PayloadJWT pJwt = response.block();
        System.out.println(pJwt);
        String email = pJwt.getEmail();
        String iat = pJwt.getIat();
        String exp = pJwt.getExp();

        if (email == null && iat == null && exp == null) {
            System.out.println("invalid access token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid access token");
        } else {
            MessageProducer producer = this.context.getBean(MessageProducer.class);

            // if there's an orderID and it's not unique, then throw error.
            if (order.getOrderID() != null && openOrderService.getOpenOrder(order.getOrderID()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OrderID already exists.");
            }

            String orderStr = "Order " +
                    order.getTokenType() + " " + order.getTokenName() + " " +
                    order.getOrderType() + " " + order.getPrice() + " " + order.getQuantity() + " " +
                    order.getOrderID();
            logger.info("Received " + orderStr);

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
                        order.getOrderType());
                openOrderService.saveOpenOrder(openOrder);
            }

            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
    }
}