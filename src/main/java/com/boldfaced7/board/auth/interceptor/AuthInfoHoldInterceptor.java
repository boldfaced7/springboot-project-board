package com.boldfaced7.board.auth.interceptor;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInfoHoldInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session != null) {
            AuthInfoHolder.releaseAuthInfo();
            AuthInfoHolder.setAuthInfo((AuthResponse) session.getAttribute(SessionConst.AUTH_RESPONSE));
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthInfoHolder.releaseAuthInfo();
    }
}
