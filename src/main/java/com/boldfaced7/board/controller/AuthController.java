package com.boldfaced7.board.controller;

import com.boldfaced7.board.dto.AuthDto;
import com.boldfaced7.board.dto.request.AuthRequest;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Validated AuthRequest authRequest,
            HttpSession session) {

        AuthDto login = authService.login(authRequest.toDto());
        AuthResponse authResponse = new AuthResponse(login);

        session.setMaxInactiveInterval(1800);
        session.setAttribute(SessionConst.AUTH_RESPONSE, authResponse);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok().build();
    }
}
