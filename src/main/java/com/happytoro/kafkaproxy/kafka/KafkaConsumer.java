package com.happytoro.kafkaproxy.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.happytoro.kafkaproxy.firebase.FirebaseMessagingService;
import com.happytoro.kafkaproxy.kafka.KafkaMessageConfig.MessageProducer;
import com.happytoro.kafkaproxy.model.TradeMatch;
import com.happytoro.kafkaproxy.openOrders.service.OpenOrderService;
import com.happytoro.kafkaproxy.price.service.PriceService;
import com.happytoro.kafkaproxy.price.model.Price;

@Component
public class KafkaConsumer {
  private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
  private ApplicationContext context;

  @Autowired 
  private PriceService priceService;

  @Autowired
  public void context(ApplicationContext context) { this.context = context; }
  
  //@Autowired
  private FirebaseMessagingService firebaseService;
  KafkaConsumer(FirebaseMessagingService firebaseService) {
    this.firebaseService = firebaseService;
  }

  @Autowired
  private OpenOrderService openOrderService;

  @Value(value = "${fcm.device.token}")
  private String deviceToken;

  private CountDownLatch latch = new CountDownLatch(1);
  
  private String payload;

  public CountDownLatch getLatch() {
    return latch;
  }

  public void setLatch() {
    latch.countDown();
  }

  public String getPayload() {
    return payload;
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public void sendPushMessage(String title, String msg) throws FirebaseMessagingException {
      firebaseService.sendNotification(title, msg, deviceToken);
  }

	@KafkaListener(topics = "#{'${message.topic.consumer_name}'}", groupId = "myGroup")
	public void consume(String message) throws Exception {
    MessageProducer producer = this.context.getBean(MessageProducer.class);
    logger.info(String.format("Trade received: %s ", message ));

    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = mapper.readTree(message);

    String timestampTrade = rootNode.get("timestamp").asText();
    Price price = new Price(
      rootNode.get("tokenType").asText(),
      rootNode.get("tokenName").asText(), 
      Float.parseFloat(rootNode.get("price").asText()),
      timestampTrade);
    Price savedPrice = priceService.savePrice(price);
    System.out.println(savedPrice);

    String makerOrderID = rootNode.get("makerOrderID").asText();
    String takerOrderID = rootNode.get("takerOrderID").asText();
    Integer makerOrderType = 99;
    Integer takerOrderType = 99;
    if (makerOrderID.equals(takerOrderID)) {
      logger.info("makerOrderID is same as takerOrderID!");
    }
    else {
      makerOrderType = openOrderService.getOrderType(makerOrderID);
      takerOrderType = openOrderService.getOrderType(takerOrderID);
      openOrderService.updateOpenOrder(rootNode.get("makerOrderID").asText(), rootNode.get("takerOrderID").asText(), rootNode.get("quantity").asDouble());
    }

    // if there's an orderID, send notification to device based on orderStatus
    // from orderId, get from DB
    // if exists, calculate percentage and send notification
    // if not exist, 100% completion 

    if (!makerOrderID.isEmpty()) {
      String makerUID = "1";
      if (makerOrderID.contains("&uid")) {
        makerUID = makerOrderID.substring(makerOrderID.lastIndexOf("&uid") + 4);
      }
      Double completion = openOrderService.getOrderCompletion(makerOrderID);
      String orderQuantity;
      if (makerOrderType == 0) {
        orderQuantity = String.valueOf(rootNode.get("quantity").asInt() * -1);
      }
      else {
        orderQuantity = rootNode.get("quantity").asText();
      }
      String toSent = "{"
        + "\"orderID\":" + "\"" + rootNode.get("makerOrderID").asText() + "\","
        + "\"tokenType\":" + "\"" + rootNode.get("tokenType").asText() + "\","
        + "\"tokenName\":" + "\"" + rootNode.get("tokenName").asText() + "\","
        + "\"price\":" + rootNode.get("price").asText()  + ","
        + "\"quantity\":" + rootNode.get("quantity").asText()  + ","
        + "\"timestamp\":" + "\"" + rootNode.get("timestamp").asText() + "\""
        + "}";
      sendPushMessage(String.format("Trade %s%% matched", completion), toSent);

      TradeMatch tm = new TradeMatch(makerUID, rootNode.get("tradeID").asInt(), takerOrderID, makerOrderID,
      rootNode.get("tokenType").asText(),
      rootNode.get("tokenName").asText(),
      Float.parseFloat(rootNode.get("price").asText()),
      Float.parseFloat(orderQuantity), timestampTrade);
    
      producer.sendTradeMessage(tm);
    }

    if (!takerOrderID.isEmpty()) {
      String takerUID = "1";
      if (takerOrderID.contains("&uid")) {
        takerUID = takerOrderID.substring(takerOrderID.lastIndexOf("&uid") + 4);
      }
      Double completion = openOrderService.getOrderCompletion(takerOrderID);
      String orderQuantity;
      if (takerOrderType == 0) {
        orderQuantity = String.valueOf(rootNode.get("quantity").asDouble() * -1);
      }
      else {
        orderQuantity = rootNode.get("quantity").asText();
      }
      String toSent = "{"
        + "\"orderID\":" + "\"" + rootNode.get("takerOrderID").asText() + "\","
        + "\"tokenType\":" + "\"" + rootNode.get("tokenType").asText() + "\","
        + "\"tokenName\":" + "\"" + rootNode.get("tokenName").asText() + "\","
        + "\"price\":" + rootNode.get("price").asText() + ","
        + "\"quantity\":" + orderQuantity + ","
        + "\"timestamp\":" + "\"" + rootNode.get("timestamp").asText() + "\""
        + "}";
      logger.info(toSent); 
      sendPushMessage(String.format("Trade %s%% matched", completion), toSent);

      TradeMatch tm = new TradeMatch(takerUID, rootNode.get("tradeID").asInt(), takerOrderID, makerOrderID,
      rootNode.get("tokenType").asText(),
      rootNode.get("tokenName").asText(),
      Float.parseFloat(rootNode.get("price").asText()),
      Float.parseFloat(orderQuantity), timestampTrade);
    
      producer.sendTradeMessage(tm);
    }
	}
}

