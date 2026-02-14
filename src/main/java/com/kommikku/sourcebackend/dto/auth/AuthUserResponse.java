package com.kommikku.sourcebackend.dto.auth;

import java.util.List;

public record AuthUserResponse(
        String uid,
        String email,
        String displayName,
        List<String> roles
) {
}
