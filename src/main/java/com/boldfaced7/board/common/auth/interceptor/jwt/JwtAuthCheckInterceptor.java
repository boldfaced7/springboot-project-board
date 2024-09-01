package com.boldfaced7.board.common.auth.interceptor.jwt;

import com.boldfaced7.board.common.auth.AuthInfoHolder;
import com.boldfaced7.board.common.auth.interceptor.AuthCheckInterceptor;
import com.boldfaced7.board.common.exception.exception.auth.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthCheckInterceptor implements AuthCheckInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (AuthInfoHolder.isEmpty()) {
            throw new UnauthorizedException();
        }
        return true;
    }
}
