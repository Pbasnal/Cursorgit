package com.kommikku.sourcebackend.service;

import com.kommikku.sourcebackend.dto.user.BookmarkResponse;
import com.kommikku.sourcebackend.dto.user.HistoryResponse;

import java.util.List;

public interface UserLibraryService {

    List<BookmarkResponse> listBookmarks(String uid);

    BookmarkResponse upsertBookmark(String uid, String mangaId);

    List<HistoryResponse> listHistory(String uid);

    HistoryResponse upsertHistory(String uid, String chapterId, int pageIndex);
}
