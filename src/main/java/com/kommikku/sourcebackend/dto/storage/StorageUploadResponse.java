package com.kommikku.sourcebackend.dto.storage;

import java.time.Instant;

public record StorageUploadResponse(
        String path,
        String url,
        Instant uploadedAt
) {
}
