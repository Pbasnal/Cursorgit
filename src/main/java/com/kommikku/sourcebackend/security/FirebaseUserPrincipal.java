package com.kommikku.sourcebackend.security;

import java.util.List;

public record FirebaseUserPrincipal(
        String uid,
        String email,
        String displayName,
        List<String> roles
) {
}
