package com.kanri.api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {
    @Bean
    @SneakyThrows
    public FirebaseAuth init() {
        /* The getApplicationDefault() expects the "GOOGLE_APPLICATION_CREDENTIALS" environment variable
         * to be populated with the path of JSON formatted private key of the ADMIN service account.
         */
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(options);
        return FirebaseAuth.getInstance(app);
    }
}
