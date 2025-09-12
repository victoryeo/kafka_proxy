package com.happytoro.kafkaproxy.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.happytoro.kafkaproxy.model.OrderItem;
import com.happytoro.kafkaproxy.model.TradeMatch;

import java.util.HashMap;
import java.util.Map;

@Configuration 
public class KafkaMessageConfig {
    
    @Value(value = "${spring.kafka.bootstrapAddress}")
    private String bootstrapAddress;
    
    @Bean
    public ProducerFactory<String, OrderItem> orderProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    
    @Bean
    public ProducerFactory<String, TradeMatch> tradeProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    
    @Bean
    public KafkaTemplate<String, OrderItem> orderKafkaTemplate() {
        return new KafkaTemplate<>(orderProducerFactory());
    }
    
    @Bean
    public KafkaTemplate<String, TradeMatch> tradeKafkaTemplate() {
        return new KafkaTemplate<>(tradeProducerFactory());
    }
    
    @Bean
    public MessageProducer messageProducer() {
        System.out.println("call messageProducer");
        return new MessageProducer();
    }

    public class MessageProducer {

      @Autowired
      private KafkaTemplate<String, String> kafkaTemplate;

      @Autowired
      private KafkaTemplate<String, OrderItem> orderKafkaTemplate;
      
      @Autowired
      private KafkaTemplate<String, TradeMatch> tradeKafkaTemplate;
      
      @Value(value = "${message.topic.producer_name}")
      private String topicName;

      @Value(value = "${message.topic.user_trade}")
      private String userTradeTopic;

      public void sendMessage(String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                    .offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
      }

      public void sendOrderMessage(OrderItem message) {
        System.out.println(String.format("Sending Order: %s", message));
        ListenableFuture<SendResult<String, OrderItem>> future = orderKafkaTemplate.send(topicName, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, OrderItem>>() {
            @Override
            public void onSuccess(SendResult<String, OrderItem> result) {
                System.out.println("Successfully sent order=[" + message + "] with offset=[" + 
                    result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println("Unable to send order=[" + message + "] due to: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
      }

      public void sendTradeMessage(TradeMatch message) {
        System.out.println(String.format("Sending Trade: %s", message));
        ListenableFuture<SendResult<String, TradeMatch>> future = tradeKafkaTemplate.send(userTradeTopic, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, TradeMatch>>() {
            @Override
            public void onSuccess(SendResult<String, TradeMatch> result) {
                System.out.println("Successfully sent trade=[" + message + "] with offset=[" + 
                    result.getRecordMetadata().offset() + "]");
            }
    
            @Override
            public void onFailure(Throwable ex) {
                System.err.println("Unable to send trade=[" + message + "] due to: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
      }
    }
}