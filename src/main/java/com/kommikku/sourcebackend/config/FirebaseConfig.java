package com.kommikku.sourcebackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@ConditionalOnProperty(prefix = "firebase", name = "enabled", havingValue = "true")
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp(FirebaseProperties firebaseProperties) throws IOException {
        if (!StringUtils.hasText(firebaseProperties.getCredentialsPath())) {
            throw new IllegalStateException(
                    "firebase.credentials-path must be set when firebase.enabled=true"
            );
        }

        Path credentialPath = Path.of(firebaseProperties.getCredentialsPath());
        try (InputStream credentials = Files.newInputStream(credentialPath)) {
            FirebaseOptions.Builder builder = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentials));

            if (StringUtils.hasText(firebaseProperties.getProjectId())) {
                builder.setProjectId(firebaseProperties.getProjectId());
            }
            if (StringUtils.hasText(firebaseProperties.getStorageBucket())) {
                builder.setStorageBucket(firebaseProperties.getStorageBucket());
            }

            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(builder.build());
            }
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
