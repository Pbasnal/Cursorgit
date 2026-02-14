package com.kommikku.sourcebackend.service;

import com.kommikku.sourcebackend.dto.source.ChapterPagesResponse;
import com.kommikku.sourcebackend.dto.source.ChaptersResponse;
import com.kommikku.sourcebackend.dto.source.MangaDetailResponse;
import com.kommikku.sourcebackend.dto.source.SearchResponse;

public interface CatalogService {

    SearchResponse search(String query, int page, int size);

    MangaDetailResponse getManga(String mangaId);

    ChaptersResponse getChapters(String mangaId, int page, int size);

    ChapterPagesResponse getChapterPages(String chapterId);
}
