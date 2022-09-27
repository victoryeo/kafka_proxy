package com.happytoro.kafkaproxy.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.happytoro.kafkaproxy.firebase.FirebaseMessagingService;
import com.happytoro.kafkaproxy.openOrders.service.OpenOrderService;
import com.happytoro.kafkaproxy.price.model.Price;
import com.happytoro.kafkaproxy.price.service.PriceService;

@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired private PriceService priceService;
  
    @Autowired
    private FirebaseMessagingService firebaseService;

    @Autowired
    private OpenOrderService openOrderService;

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
        String tokenType = pricearray[1];
        String tokenName = pricearray[2];
        float price = Float.parseFloat(pricearray[3]);
        float amount = Float.parseFloat(pricearray[4]);
        Long takerOrderID = Long.parseLong(pricearray[10]);
        Long makerOrderID = Long.parseLong(pricearray[11]);

        Price newPrice = new Price(tokenType, tokenName, price, timestampTrade);
        System.out.println(newPrice);
        priceService.savePrice(newPrice);

        openOrderService.updateOpenOrder(makerOrderID, takerOrderID, amount);

        sendPushMessage("Trade matched", message);
    }
}
