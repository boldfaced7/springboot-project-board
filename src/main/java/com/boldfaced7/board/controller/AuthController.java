package com.boldfaced7.board.controller;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.auth.jwt.JwtProvider;
import com.boldfaced7.board.dto.AuthDto;
import com.boldfaced7.board.dto.request.AuthRequest;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Validated AuthRequest authRequest) {
        AuthDto login = authService.login(authRequest.toDto());
        AuthResponse authResponse = new AuthResponse(login);
        AuthInfoHolder.setAuthInfo(authResponse);

        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/jwt")
    public ResponseEntity<Void> refreshAccessToken(
            @RequestHeader(JwtProvider.HEADER_AUTHORIZATION) String authorizationHeader,
            @CookieValue(JwtProvider.REFRESH_TOKEN_COOKIE_NAME) String refreshToken) {
        String accessToken = JwtProvider.extractToken(authorizationHeader);
        String newAccessToken = jwtProvider.refreshAccessToken(accessToken, refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(JwtProvider.HEADER_AUTHORIZATION, newAccessToken);

        return ResponseEntity.ok().headers(httpHeaders).build();
    }
}
