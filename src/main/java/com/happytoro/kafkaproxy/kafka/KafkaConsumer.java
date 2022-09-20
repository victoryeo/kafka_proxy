package com.happytoro.kafkaproxy.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.happytoro.kafkaproxy.firebase.FirebaseMessagingService;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private FirebaseMessagingService firebaseService;

    public void sendPushMessage(String msg) throws FirebaseMessagingException {
        firebaseService.sendNotification("Notification", msg, "Receiver device token");
    }

    @KafkaListener(topics = "#{'${message.topic.consumer_name}'}",id = "myGroup")
    public void consume(String message) throws Exception{ 
        LOGGER.info(String.format("Trade received: %s ", message ));
        sendPushMessage(message);
    }
}
