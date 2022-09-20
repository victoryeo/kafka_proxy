package com.happytoro.kafkaproxy.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

@Configuration 
public class FirebaseConfig {

  @Bean
  FirebaseMessaging firebaseMessaging() throws IOException {
      System.out.println("call firebaseMessaging");
      GoogleCredentials googleCredentials = GoogleCredentials
              .fromStream(new ClassPathResource("firebase-service-account.json")
              .getInputStream());
      FirebaseOptions firebaseOptions = FirebaseOptions
              .builder()
              .setCredentials(googleCredentials)
              .build();
      FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "token-trading-app");
      return FirebaseMessaging.getInstance(app);
  }
}


