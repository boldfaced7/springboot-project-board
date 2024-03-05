package com.boldfaced7.board.auth.interceptor;

import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.error.exception.auth.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.AUTH_RESPONSE) == null) {
            throw new UnauthorizedException();
        }

        return true;
    }
}
