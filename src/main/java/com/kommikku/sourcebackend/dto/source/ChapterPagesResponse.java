package com.kommikku.sourcebackend.dto.source;

import java.util.List;

public record ChapterPagesResponse(
        String chapterId,
        List<ChapterPageResponse> pages
) {
}
