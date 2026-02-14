package com.kommikku.sourcebackend.dto.source;

import java.util.List;

public record ChaptersResponse(
        String mangaId,
        int page,
        int size,
        long totalElements,
        List<ChapterSummaryResponse> items
) {
}
