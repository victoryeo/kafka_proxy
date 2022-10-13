package com.happytoro.kafkaproxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.happytoro.kafkaproxy.Config.KafkaProducer;
import com.happytoro.kafkaproxy.kafka.KafkaConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
public class KafkaConsumerTests {

    private static final String TEST_TOPIC = "TokenTrade";

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    @Test
    public void test_ReceiveKafkaEvent() throws Exception{
        String message = "{\"tradeID\":2,\"takerOrderID\":\"1665636557979\u0026uid1\",\"makerOrderID\":\"1665636543527\u0026uid1\",\"tokenType\":\"stock\",\"tokenName\":\"AAPL\",\"price\":100,\"quantity\":1,\"timestamp\":\"2022-10-13T12:49:33.21426+08:00\"}";

        producer.send(TEST_TOPIC, message);

        consumer.setPayload(message);

        System.out.println("payload = " + consumer.getPayload());
        assertTrue(consumer.getPayload().contains("tradeID"));

    }
}