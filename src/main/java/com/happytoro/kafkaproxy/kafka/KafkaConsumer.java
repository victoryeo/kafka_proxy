package com.happytoro.kafkaproxy.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.happytoro.kafkaproxy.firebase.FirebaseMessagingService;
import com.happytoro.kafkaproxy.database.PriceService;
import com.happytoro.kafkaproxy.model.Price;


@Component
public class KafkaConsumer {
  private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
  
  @Autowired 
  private PriceService priceService;
  
  //@Autowired
  //private FirebaseMessagingService firebaseService;

  @Value(value = "${fcm.device.token}")
  private String deviceToken;

  /*public void sendPushMessage(String title, String msg) throws FirebaseMessagingException {
      firebaseService.sendNotification(title, msg, deviceToken);
  }*/

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
    System.out.println(price);
    priceService.savePrice(price);

    //sendPushMessage("Trade matched", message);
	}
}
