package com.happytoro.kafkaproxy.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private KafkaConsumer kafkaConsumer;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "TokenOrder",id = "myGroup")
    public void consume(String message){ 
        LOGGER.info(String.format("Order received: %s ", message ));
    }
}
