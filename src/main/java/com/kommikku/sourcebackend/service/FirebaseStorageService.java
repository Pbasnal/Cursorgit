package com.kommikku.sourcebackend.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.kommikku.sourcebackend.config.FirebaseProperties;
import com.kommikku.sourcebackend.dto.storage.StorageUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

@Service
public class FirebaseStorageService implements StorageService {

    private final Optional<FirebaseApp> firebaseApp;
    private final FirebaseProperties firebaseProperties;

    public FirebaseStorageService(Optional<FirebaseApp> firebaseApp, FirebaseProperties firebaseProperties) {
        this.firebaseApp = firebaseApp;
        this.firebaseProperties = firebaseProperties;
    }

    @Override
    public StorageUploadResponse upload(String objectPath, byte[] content, String contentType) {
        validatePath(objectPath);

        if (!firebaseProperties.isEnabled()) {
            return new StorageUploadResponse(
                    objectPath,
                    "https://storage.googleapis.com/kommikku-dev/" + UriUtils.encodePath(objectPath, StandardCharsets.UTF_8),
                    Instant.now()
            );
        }

        FirebaseApp app = firebaseApp.orElseThrow(() ->
                new IllegalStateException("FirebaseApp is unavailable. Check firebase credentials and config.")
        );
        Bucket bucket = StorageClient.getInstance(app).bucket();
        if (bucket == null) {
            throw new IllegalStateException("Firebase Storage bucket is not configured.");
        }

        String resolvedContentType = StringUtils.hasText(contentType) ? contentType : "application/octet-stream";
        Blob blob = bucket.create(objectPath, content, resolvedContentType);
        String encodedPath = UriUtils.encodePath(blob.getName(), StandardCharsets.UTF_8);
        String url = "https://storage.googleapis.com/%s/%s".formatted(bucket.getName(), encodedPath);
        return new StorageUploadResponse(objectPath, url, Instant.now());
    }

    @Override
    public void delete(String objectPath) {
        validatePath(objectPath);
        if (!firebaseProperties.isEnabled()) {
            return;
        }

        FirebaseApp app = firebaseApp.orElseThrow(() ->
                new IllegalStateException("FirebaseApp is unavailable. Check firebase credentials and config.")
        );
        Bucket bucket = StorageClient.getInstance(app).bucket();
        if (bucket == null) {
            throw new IllegalStateException("Firebase Storage bucket is not configured.");
        }

        Blob blob = bucket.get(objectPath);
        if (blob != null) {
            blob.delete();
        }
    }

    private void validatePath(String objectPath) {
        if (!StringUtils.hasText(objectPath)) {
            throw new IllegalArgumentException("objectPath is required");
        }
    }
}
