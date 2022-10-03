package com.happytoro.kafkaproxy.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import com.happytoro.kafkaproxy.model.OrderItem;
import com.happytoro.kafkaproxy.model.TradeMatch;

@Configuration 
public class KafkaMessageConfig {
    @Bean
    public MessageProducer messageProducer() {
        System.out.println("call messageProducer");
        return new MessageProducer();
    }

    public class MessageProducer {

      @Autowired
      private KafkaTemplate<String, String> kafkaTemplate;

      @Autowired
      private KafkaTemplate<String, Object> kafkaOrderTemplate;
      
      @Value(value = "${message.topic.producer_name}")
      private String topicName;

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
        System.out.println(String.format("Order %s", message));
        ListenableFuture<SendResult<String, Object>> future = kafkaOrderTemplate.send(topicName, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                    .offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
      }

      public void sendTradeMessage(TradeMatch message) {
        System.out.println(String.format("Order %s", message));
        ListenableFuture<SendResult<String, Object>> future = kafkaOrderTemplate.send(topicName, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
    
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }
    
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
       }
    }
}