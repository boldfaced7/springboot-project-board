package com.boldfaced7.board.common.auth.interceptor.jwt;

import com.boldfaced7.board.common.auth.AuthInfoHolder;
import com.boldfaced7.board.common.auth.interceptor.AuthInfoHoldInterceptor;
import com.boldfaced7.board.common.auth.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthInfoHoldInterceptor implements AuthInfoHoldInterceptor {
    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(JwtProvider.HEADER_AUTHORIZATION);
        String accessToken = JwtProvider.extractToken(header);

        if (jwtProvider.verifyToken(accessToken)) {
            AuthInfoHolder.releaseAuthInfo();
            AuthInfoHolder.setAuthInfo(jwtProvider.extractAuthInfo(accessToken));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthInfoHolder.releaseAuthInfo();
    }
}
