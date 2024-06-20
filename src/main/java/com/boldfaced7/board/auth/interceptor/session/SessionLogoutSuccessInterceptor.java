package com.boldfaced7.board.auth.interceptor.session;

import com.boldfaced7.board.auth.interceptor.LogoutSuccessInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

//@Component
public class SessionLogoutSuccessInterceptor implements LogoutSuccessInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return true;
    }
}
