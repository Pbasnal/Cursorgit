package com.kommikku.sourcebackend.dto.source;

import java.util.List;

public record SearchResultItem(
        String id,
        String title,
        String synopsis,
        String coverUrl,
        List<String> genres
) {
}
