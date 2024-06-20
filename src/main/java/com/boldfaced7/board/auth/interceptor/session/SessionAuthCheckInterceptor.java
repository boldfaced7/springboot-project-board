package com.boldfaced7.board.auth.interceptor.session;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.auth.interceptor.AuthCheckInterceptor;
import com.boldfaced7.board.error.exception.auth.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

//@Component
public class SessionAuthCheckInterceptor implements AuthCheckInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (AuthInfoHolder.isEmpty()) {
            throw new UnauthorizedException();
        }
        return true;
    }
}
