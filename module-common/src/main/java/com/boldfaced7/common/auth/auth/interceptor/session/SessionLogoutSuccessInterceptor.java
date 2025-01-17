package com.boldfaced7.common.auth.auth.interceptor.session;

import com.boldfaced7.common.auth.auth.interceptor.LogoutSuccessInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
