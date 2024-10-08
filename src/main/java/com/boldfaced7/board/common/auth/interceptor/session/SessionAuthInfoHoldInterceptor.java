package com.boldfaced7.board.common.auth.interceptor.session;

import com.boldfaced7.board.common.auth.AuthInfoHolder;
import com.boldfaced7.board.common.auth.SessionConst;
import com.boldfaced7.board.common.auth.interceptor.AuthInfoHoldInterceptor;
import com.boldfaced7.board.common.auth.presentation.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//@Component
public class SessionAuthInfoHoldInterceptor implements AuthInfoHoldInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute(SessionConst.AUTH_RESPONSE) != null) {
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
