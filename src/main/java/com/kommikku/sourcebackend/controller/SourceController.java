package com.kommikku.sourcebackend.controller;

import com.kommikku.sourcebackend.dto.source.ChapterPagesResponse;
import com.kommikku.sourcebackend.dto.source.ChaptersResponse;
import com.kommikku.sourcebackend.dto.source.MangaDetailResponse;
import com.kommikku.sourcebackend.dto.source.SearchResponse;
import com.kommikku.sourcebackend.service.CatalogService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/source")
public class SourceController {

    private final CatalogService catalogService;

    public SourceController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/search")
    public SearchResponse search(
            @RequestParam @NotBlank String query,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return catalogService.search(query, page, size);
    }

    @GetMapping("/manga/{mangaId}")
    public MangaDetailResponse manga(@PathVariable String mangaId) {
        return catalogService.getManga(mangaId);
    }

    @GetMapping("/manga/{mangaId}/chapters")
    public ChaptersResponse chapters(
            @PathVariable String mangaId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return catalogService.getChapters(mangaId, page, size);
    }

    @GetMapping("/chapters/{chapterId}/pages")
    public ChapterPagesResponse pages(@PathVariable String chapterId) {
        return catalogService.getChapterPages(chapterId);
    }
}
