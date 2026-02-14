package com.kommikku.sourcebackend.dto.user;

import java.time.Instant;

public record HistoryResponse(
        String chapterId,
        int pageIndex,
        Instant updatedAt
) {
}
