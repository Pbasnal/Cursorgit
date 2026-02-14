package com.kommikku.sourcebackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record HistoryRequest(
        @NotBlank String chapterId,
        @PositiveOrZero int pageIndex
) {
}
