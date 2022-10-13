package com.happytoro.kafkaproxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.happytoro.kafkaproxy.Config.KafkaProducer;
import com.happytoro.kafkaproxy.kafka.KafkaConsumer;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(SpringRunner.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9093", "port=9093" })
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class KafkaConsumerTests {

    private static final String TEST_TOPIC = "TokenTrade";

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @SpyBean
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    @Captor
    ArgumentCaptor<String> captor;

    @Test
    public void test_ReceiveKafkaEvent() throws Exception{
        String message = "{\"tradeID\":2,\"takerOrderID\":\"1665636557979\u0026uid1\",\"makerOrderID\":\"1665636543527\u0026uid1\",\"tokenType\":\"stock\",\"tokenName\":\"AAPL\",\"price\":100,\"quantity\":1,\"timestamp\":\"2022-10-13T12:49:33.21426+08:00\"}";

        producer.send(TEST_TOPIC, message);

        // consumer.setPayload(message);
        verify(consumer, timeout(1000).times(1)).consume(captor.capture());
        String payload = captor.getValue();

        System.out.println("payload = " + payload);
        assertNotNull(payload);
        assertTrue(payload.contains("tradeID"));

    }
}