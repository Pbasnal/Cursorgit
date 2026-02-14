package com.kommikku.sourcebackend.dto.user;

import jakarta.validation.constraints.NotBlank;

public record BookmarkRequest(
        @NotBlank String mangaId
) {
}
