package com.kommikku.sourcebackend.controller;

import com.kommikku.sourcebackend.dto.auth.AuthUserResponse;
import com.kommikku.sourcebackend.security.FirebaseUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/me")
    public AuthUserResponse me(@AuthenticationPrincipal FirebaseUserPrincipal principal) {
        return new AuthUserResponse(
                principal.uid(),
                principal.email(),
                principal.displayName(),
                principal.roles()
        );
    }
}
