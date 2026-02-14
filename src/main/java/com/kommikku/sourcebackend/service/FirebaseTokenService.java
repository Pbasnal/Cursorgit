package com.kommikku.sourcebackend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.kommikku.sourcebackend.config.FirebaseProperties;
import com.kommikku.sourcebackend.security.FirebaseUserPrincipal;
import com.kommikku.sourcebackend.security.TokenVerificationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FirebaseTokenService {

    private final Optional<FirebaseAuth> firebaseAuth;
    private final FirebaseProperties firebaseProperties;

    public FirebaseTokenService(Optional<FirebaseAuth> firebaseAuth, FirebaseProperties firebaseProperties) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseProperties = firebaseProperties;
    }

    public FirebaseUserPrincipal verifyIdToken(String idToken) {
        if (!StringUtils.hasText(idToken)) {
            throw new TokenVerificationException("Bearer token is empty");
        }

        if (firebaseProperties.isEnabled()) {
            return verifyAgainstFirebase(idToken);
        }

        return verifyInDevMode(idToken);
    }

    private FirebaseUserPrincipal verifyAgainstFirebase(String idToken) {
        FirebaseAuth auth = firebaseAuth.orElseThrow(() ->
                new TokenVerificationException("FirebaseAuth bean is unavailable. Check firebase.enabled and credentials.")
        );

        try {
            FirebaseToken decoded = auth.verifyIdToken(idToken);
            return new FirebaseUserPrincipal(
                    decoded.getUid(),
                    decoded.getEmail(),
                    decoded.getName(),
                    extractRoles(decoded.getClaims())
            );
        } catch (FirebaseAuthException ex) {
            throw new TokenVerificationException("Firebase token verification failed", ex);
        }
    }

    private FirebaseUserPrincipal verifyInDevMode(String idToken) {
        if ("dev-token".equals(idToken)) {
            return new FirebaseUserPrincipal(
                    "dev-user",
                    "dev.user@example.com",
                    "Dev User",
                    List.of("ROLE_USER", "ROLE_ADMIN")
            );
        }
        throw new TokenVerificationException("Invalid development token. Use 'dev-token' when firebase.enabled=false.");
    }

    private List<String> extractRoles(Map<String, Object> claims) {
        Object roles = claims.getOrDefault("roles", Collections.singletonList("ROLE_USER"));
        if (roles instanceof List<?> roleList) {
            return roleList.stream().map(String::valueOf).toList();
        }
        return List.of(String.valueOf(roles));
    }
}
