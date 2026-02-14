package com.kommikku.sourcebackend.controller;

import com.kommikku.sourcebackend.dto.user.BookmarkRequest;
import com.kommikku.sourcebackend.dto.user.BookmarkResponse;
import com.kommikku.sourcebackend.dto.user.HistoryRequest;
import com.kommikku.sourcebackend.dto.user.HistoryResponse;
import com.kommikku.sourcebackend.security.FirebaseUserPrincipal;
import com.kommikku.sourcebackend.service.UserLibraryService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/me")
public class UserLibraryController {

    private final UserLibraryService userLibraryService;

    public UserLibraryController(UserLibraryService userLibraryService) {
        this.userLibraryService = userLibraryService;
    }

    @GetMapping("/bookmarks")
    public List<BookmarkResponse> listBookmarks(@AuthenticationPrincipal FirebaseUserPrincipal principal) {
        return userLibraryService.listBookmarks(principal.uid());
    }

    @PostMapping("/bookmarks")
    public BookmarkResponse upsertBookmark(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @Valid @RequestBody BookmarkRequest request
    ) {
        return userLibraryService.upsertBookmark(principal.uid(), request.mangaId());
    }

    @GetMapping("/history")
    public List<HistoryResponse> listHistory(@AuthenticationPrincipal FirebaseUserPrincipal principal) {
        return userLibraryService.listHistory(principal.uid());
    }

    @PostMapping("/history")
    public HistoryResponse upsertHistory(
            @AuthenticationPrincipal FirebaseUserPrincipal principal,
            @Valid @RequestBody HistoryRequest request
    ) {
        return userLibraryService.upsertHistory(principal.uid(), request.chapterId(), request.pageIndex());
    }
}
