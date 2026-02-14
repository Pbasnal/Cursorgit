package com.kommikku.sourcebackend.service;

import com.kommikku.sourcebackend.dto.user.BookmarkResponse;
import com.kommikku.sourcebackend.dto.user.HistoryResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryUserLibraryService implements UserLibraryService {

    private final Map<String, Map<String, BookmarkResponse>> bookmarksByUser = new ConcurrentHashMap<>();
    private final Map<String, Map<String, HistoryResponse>> historyByUser = new ConcurrentHashMap<>();

    @Override
    public List<BookmarkResponse> listBookmarks(String uid) {
        Map<String, BookmarkResponse> bookmarks = bookmarksByUser.getOrDefault(uid, Map.of());
        return bookmarks.values().stream()
                .sorted(Comparator.comparing(BookmarkResponse::updatedAt).reversed())
                .toList();
    }

    @Override
    public BookmarkResponse upsertBookmark(String uid, String mangaId) {
        BookmarkResponse response = new BookmarkResponse(mangaId, Instant.now());
        bookmarksByUser.computeIfAbsent(uid, key -> new ConcurrentHashMap<>()).put(mangaId, response);
        return response;
    }

    @Override
    public List<HistoryResponse> listHistory(String uid) {
        Map<String, HistoryResponse> history = historyByUser.getOrDefault(uid, Map.of());
        return history.values().stream()
                .sorted(Comparator.comparing(HistoryResponse::updatedAt).reversed())
                .toList();
    }

    @Override
    public HistoryResponse upsertHistory(String uid, String chapterId, int pageIndex) {
        HistoryResponse response = new HistoryResponse(chapterId, pageIndex, Instant.now());
        historyByUser.computeIfAbsent(uid, key -> new ConcurrentHashMap<>()).put(chapterId, response);
        return response;
    }
}
