package com.boldfaced7.board.auth.interceptor.jwt;

import com.boldfaced7.board.auth.CookieUtil;
import com.boldfaced7.board.auth.interceptor.LogoutSuccessInterceptor;
import com.boldfaced7.board.auth.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class JwtLogoutSuccessInterceptor implements LogoutSuccessInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CookieUtil.deleteCookie(request, response, JwtProvider.REFRESH_TOKEN_COOKIE_NAME);
        response.addHeader(JwtProvider.HEADER_AUTHORIZATION, "logout");

        return true;
    }
}
