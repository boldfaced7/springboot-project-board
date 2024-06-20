package com.boldfaced7.board.auth.interceptor.jwt;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.auth.CookieUtil;
import com.boldfaced7.board.auth.interceptor.LoginSuccessInterceptor;
import com.boldfaced7.board.auth.jwt.JwtProperties;
import com.boldfaced7.board.auth.jwt.JwtProvider;
import com.boldfaced7.board.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessInterceptor implements LoginSuccessInterceptor {
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        String refreshToken = jwtProvider.generateRefreshToken(authInfo);
        String accessToken = jwtProvider.generateAccessToken(authInfo);

        addRefreshTokenToCookie(request, response, refreshToken);
        response.addHeader(JwtProvider.HEADER_AUTHORIZATION, accessToken);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) jwtProperties.getRefreshExpiration();
        CookieUtil.addCookie(response, JwtProvider.REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }
}
