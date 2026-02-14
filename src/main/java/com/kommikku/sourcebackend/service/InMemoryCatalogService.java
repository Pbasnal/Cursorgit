package com.kommikku.sourcebackend.service;

import com.kommikku.sourcebackend.dto.source.ChapterPageResponse;
import com.kommikku.sourcebackend.dto.source.ChapterPagesResponse;
import com.kommikku.sourcebackend.dto.source.ChapterSummaryResponse;
import com.kommikku.sourcebackend.dto.source.ChaptersResponse;
import com.kommikku.sourcebackend.dto.source.MangaDetailResponse;
import com.kommikku.sourcebackend.dto.source.SearchResponse;
import com.kommikku.sourcebackend.dto.source.SearchResultItem;
import com.kommikku.sourcebackend.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InMemoryCatalogService implements CatalogService {

    private final Map<String, MangaDetailResponse> mangaCatalog = Map.of(
            "solo-leveling", new MangaDetailResponse(
                    "solo-leveling",
                    "Solo Leveling",
                    "Chugong",
                    "COMPLETED",
                    "A weak hunter gains a leveling system and starts climbing rapidly.",
                    "https://storage.googleapis.com/kommikku-demo/covers/solo-leveling.jpg",
                    List.of("Action", "Fantasy", "Adventure")
            ),
            "one-piece", new MangaDetailResponse(
                    "one-piece",
                    "One Piece",
                    "Eiichiro Oda",
                    "ONGOING",
                    "Monkey D. Luffy sails the Grand Line to become the Pirate King.",
                    "https://storage.googleapis.com/kommikku-demo/covers/one-piece.jpg",
                    List.of("Action", "Adventure", "Comedy")
            ),
            "omniscient-reader", new MangaDetailResponse(
                    "omniscient-reader",
                    "Omniscient Reader",
                    "Sing Shong",
                    "ONGOING",
                    "A novel reader survives in a world that follows the novel's plot.",
                    "https://storage.googleapis.com/kommikku-demo/covers/omniscient-reader.jpg",
                    List.of("Action", "Fantasy", "Drama")
            )
    );

    private final Map<String, List<ChapterSummaryResponse>> chaptersByManga = Map.of(
            "solo-leveling", buildChapters("solo-leveling", 15),
            "one-piece", buildChapters("one-piece", 25),
            "omniscient-reader", buildChapters("omniscient-reader", 20)
    );

    @Override
    @Cacheable(cacheNames = "search", key = "{#query, #page, #size}")
    public SearchResponse search(String query, int page, int size) {
        String normalized = query.trim().toLowerCase(Locale.ROOT);
        List<SearchResultItem> matches = mangaCatalog.values().stream()
                .filter(m -> m.title().toLowerCase(Locale.ROOT).contains(normalized))
                .sorted(Comparator.comparing(MangaDetailResponse::title))
                .map(this::toSearchItem)
                .toList();

        long total = matches.size();
        List<SearchResultItem> paged = paginate(matches, page, size);
        return new SearchResponse(query, page, size, total, paged);
    }

    @Override
    @Cacheable(cacheNames = "manga", key = "#mangaId")
    public MangaDetailResponse getManga(String mangaId) {
        MangaDetailResponse manga = mangaCatalog.get(mangaId);
        if (manga == null) {
            throw new ResourceNotFoundException("Manga not found: " + mangaId);
        }
        return manga;
    }

    @Override
    @Cacheable(cacheNames = "chapters", key = "{#mangaId, #page, #size}")
    public ChaptersResponse getChapters(String mangaId, int page, int size) {
        getManga(mangaId);
        List<ChapterSummaryResponse> chapters = chaptersByManga.getOrDefault(mangaId, List.of());
        List<ChapterSummaryResponse> paged = paginate(chapters, page, size);
        return new ChaptersResponse(mangaId, page, size, chapters.size(), paged);
    }

    @Override
    @Cacheable(cacheNames = "chapter-pages", key = "#chapterId")
    public ChapterPagesResponse getChapterPages(String chapterId) {
        String mangaId = parseMangaId(chapterId);
        if (!chaptersByManga.containsKey(mangaId)) {
            throw new ResourceNotFoundException("Chapter not found: " + chapterId);
        }

        List<ChapterPageResponse> pages = List.of(
                new ChapterPageResponse(0, "https://storage.googleapis.com/kommikku-demo/pages/" + chapterId + "/1.jpg", 1080, 1600),
                new ChapterPageResponse(1, "https://storage.googleapis.com/kommikku-demo/pages/" + chapterId + "/2.jpg", 1080, 1600),
                new ChapterPageResponse(2, "https://storage.googleapis.com/kommikku-demo/pages/" + chapterId + "/3.jpg", 1080, 1600)
        );
        return new ChapterPagesResponse(chapterId, pages);
    }

    private List<ChapterSummaryResponse> buildChapters(String mangaId, int count) {
        return java.util.stream.IntStream.rangeClosed(1, count)
                .mapToObj(ch -> new ChapterSummaryResponse(
                        mangaId + "-ch-" + ch,
                        mangaId,
                        "Chapter " + ch,
                        ch,
                        Instant.now().minus(count - ch, ChronoUnit.DAYS)
                ))
                .sorted(Comparator.comparing(ChapterSummaryResponse::chapterNumber).reversed())
                .collect(Collectors.toList());
    }

    private SearchResultItem toSearchItem(MangaDetailResponse manga) {
        return new SearchResultItem(
                manga.id(),
                manga.title(),
                manga.synopsis(),
                manga.coverUrl(),
                manga.genres()
        );
    }

    private <T> List<T> paginate(List<T> values, int page, int size) {
        int from = Math.min(page * size, values.size());
        int to = Math.min(from + size, values.size());
        return values.subList(from, to);
    }

    private String parseMangaId(String chapterId) {
        int index = chapterId.indexOf("-ch-");
        if (index < 0) {
            throw new ResourceNotFoundException("Chapter not found: " + chapterId);
        }
        return chapterId.substring(0, index);
    }
}
