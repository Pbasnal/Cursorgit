package com.kommikku.sourcebackend.dto.user;

import java.time.Instant;

public record BookmarkResponse(
        String mangaId,
        Instant updatedAt
) {
}
