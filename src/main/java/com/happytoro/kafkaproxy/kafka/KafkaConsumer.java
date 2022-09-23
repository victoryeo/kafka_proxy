package com.happytoro.kafkaproxy.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.happytoro.kafkaproxy.firebase.FirebaseMessagingService;
import com.happytoro.kafkaproxy.database.PriceService;
import com.happytoro.kafkaproxy.model.Price;

@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired private PriceService priceService;
  
    @Autowired
    private FirebaseMessagingService firebaseService;

    @Value(value = "${fcm.device.token}")
    private String deviceToken;

    public void sendPushMessage(String title, String msg) throws FirebaseMessagingException {
        firebaseService.sendNotification(title, msg, deviceToken);
    }

    @KafkaListener(topics = "#{'${message.topic.consumer_name}'}",id = "myGroup")
    public void consume(String message) throws Exception{ 
        logger.info(String.format("Trade received: %s ", message ));

        String[] pricearray = message.split(" ");
        String timestampTrade = pricearray[5] + " " + pricearray[6];
        Price price = new Price(pricearray[1], pricearray[2], Integer.parseInt(pricearray[3]), timestampTrade);
        System.out.println(price);
        priceService.savePrice(price);

        sendPushMessage("Trade matched", message);
    }
}
