package com.boldfaced7.common.auth.auth.interceptor.session;

import com.boldfaced7.common.auth.auth.AuthInfoHolder;
import com.boldfaced7.common.auth.auth.interceptor.AuthCheckInterceptor;
import com.boldfaced7.common.exception.exception.auth.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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