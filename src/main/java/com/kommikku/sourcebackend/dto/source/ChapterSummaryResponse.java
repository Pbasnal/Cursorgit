package com.kommikku.sourcebackend.dto.source;

import java.time.Instant;

public record ChapterSummaryResponse(
        String id,
        String mangaId,
        String title,
        int chapterNumber,
        Instant releaseDate
) {
}
