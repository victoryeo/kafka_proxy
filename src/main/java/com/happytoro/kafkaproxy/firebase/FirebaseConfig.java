package com.happytoro.kafkaproxy.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

public class FirebaseConfig {

  @Bean
  FirebaseMessaging firebaseMessaging() throws IOException {
      GoogleCredentials googleCredentials = GoogleCredentials
              .fromStream(new ClassPathResource("firebase-service-account.json")
              .getInputStream());
      FirebaseOptions firebaseOptions = FirebaseOptions
              .builder()
              .setCredentials(googleCredentials)
              .build();
      FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "token_trading_app");
      return FirebaseMessaging.getInstance(app);
  }
}


