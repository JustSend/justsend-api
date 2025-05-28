package com.justsend.api.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import java.io.InputStream

@Configuration
class FirebaseConfig {

  @PostConstruct
  fun init() {
    if (FirebaseApp.getApps().isEmpty()) {
      val serviceAccount: InputStream = javaClass.getResourceAsStream("/firebase-service-account.json")
        ?: throw RuntimeException("Firebase service account file not found")

      val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

      FirebaseApp.initializeApp(options)
    }
  }
}
