package com.kommikku.sourcebackend.dto.source;

import java.util.List;

public record SearchResponse(
        String query,
        int page,
        int size,
        long totalElements,
        List<SearchResultItem> items
) {
}
