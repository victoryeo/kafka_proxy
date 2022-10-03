package com.happytoro.kafkaproxy.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.Format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.happytoro.kafkaproxy.firebase.FirebaseMessagingService;
import com.happytoro.kafkaproxy.model.TradeMatch;
import com.happytoro.kafkaproxy.openOrders.service.OpenOrderService;
import com.happytoro.kafkaproxy.price.service.PriceService;
import com.happytoro.kafkaproxy.price.model.Price;

@Component
public class KafkaConsumer {
  private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
  
  @Autowired 
  private PriceService priceService;

  @Autowired
  private KafkaTemplate<String, Object> kafkaTradeTemplate;

  @Value(value = "${message.topic.user_trade}")
  private String userTradeTopic;
  
  //@Autowired
  private FirebaseMessagingService firebaseService;
  KafkaConsumer(FirebaseMessagingService firebaseService) {
    this.firebaseService = firebaseService;
  }

  @Autowired
  private OpenOrderService openOrderService;

  @Value(value = "${fcm.device.token}")
  private String deviceToken;

  public void sendPushMessage(String title, String msg) throws FirebaseMessagingException {
      firebaseService.sendNotification(title, msg, deviceToken);
  }

	@KafkaListener(topics = "#{'${message.topic.consumer_name}'}", groupId = "myGroup")
	public void consume(String message) throws Exception {
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
    if (makerOrderID.equals(takerOrderID)) {
      logger.info("makerOrderID is same as takerOrderID!");
    }
    else {
      openOrderService.updateOpenOrder(rootNode.get("makerOrderID").asText(), rootNode.get("takerOrderID").asText(), rootNode.get("quantity").asDouble());
    }

    TradeMatch tm = new TradeMatch(1, rootNode.get("tradeID").asInt(), takerOrderID, makerOrderID,
      rootNode.get("tokenType").asText(),
      rootNode.get("tokenName").asText(),
      Float.parseFloat(rootNode.get("price").asText()),
      Float.parseFloat(rootNode.get("quantity").asText()), timestampTrade);

    kafkaTradeTemplate.send(userTradeTopic, tm);

    // if there's an orderID, send notification to device based on orderStatus
    // from orderId, get from DB
    // if exists, calculate percentage and send notification
    // if not exist, 100% completion 

    if (!makerOrderID.isEmpty()) {
      Double completion = openOrderService.getOrderCompletion(makerOrderID);
      String toSent = "{"
        + "\"orderID\":" + "\"" + rootNode.get("makerOrderID").asText() + "\","
        + "\"tokenType\":" + "\"" + rootNode.get("tokenType").asText() + "\","
        + "\"tokenName\":" + "\"" + rootNode.get("tokenName").asText() + "\","
        + "\"price\":" + rootNode.get("price").asText() + ","
        + "\"quantity\":" + rootNode.get("quantity").asText() + ","
        + "\"timestamp\":" + "\"" + rootNode.get("timestamp").asText() + "\""
        + "}";
      sendPushMessage(String.format("Trade %s%% matched", completion), toSent);
    }

    if (!takerOrderID.isEmpty()) {
      Double completion = openOrderService.getOrderCompletion(takerOrderID);
      String toSent = "{"
        + "\"orderID\":" + "\"" + rootNode.get("takerOrderID").asText() + "\","
        + "\"tokenType\":" + "\"" + rootNode.get("tokenType").asText() + "\","
        + "\"tokenName\":" + "\"" + rootNode.get("tokenName").asText() + "\","
        + "\"price\":" + rootNode.get("price").asText() + ","
        + "\"quantity\":" + rootNode.get("quantity").asText() + ","
        + "\"timestamp\":" + "\"" + rootNode.get("timestamp").asText() + "\""
        + "}";
      logger.info(toSent); 
      sendPushMessage(String.format("Trade %s%% matched", completion), toSent);
    }
	}
}

