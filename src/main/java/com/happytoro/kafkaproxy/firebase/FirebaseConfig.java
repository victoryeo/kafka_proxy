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

      FirebaseApp app = null;
      if(FirebaseApp.getApps().isEmpty()) { 
                app = FirebaseApp.initializeApp(firebaseOptions, "token-trading-app");
        }
        else {
                app = FirebaseApp.initializeApp(firebaseOptions);
        }
      
      return FirebaseMessaging.getInstance(app);
  }
}


