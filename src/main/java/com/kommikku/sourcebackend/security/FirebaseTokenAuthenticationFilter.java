package com.kommikku.sourcebackend.security;

import com.kommikku.sourcebackend.service.FirebaseTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FirebaseTokenAuthenticationFilter extends OncePerRequestFilter {

    private final FirebaseTokenService firebaseTokenService;

    public FirebaseTokenAuthenticationFilter(FirebaseTokenService firebaseTokenService) {
        this.firebaseTokenService = firebaseTokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        try {
            FirebaseUserPrincipal principal = firebaseTokenService.verifyIdToken(token);
            List<SimpleGrantedAuthority> authorities = principal.roles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(principal, token, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (TokenVerificationException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\":\"invalid_token\",\"message\":\"" + sanitize(ex.getMessage()) + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String sanitize(String message) {
        if (!StringUtils.hasText(message)) {
            return "Token verification failed";
        }
        return message.replace("\"", "\\\"");
    }
}
