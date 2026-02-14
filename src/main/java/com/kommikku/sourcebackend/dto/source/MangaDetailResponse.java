package com.kommikku.sourcebackend.dto.source;

import java.util.List;

public record MangaDetailResponse(
        String id,
        String title,
        String author,
        String status,
        String synopsis,
        String coverUrl,
        List<String> genres
) {
}
