package com.boldfaced7.board.auth.interceptor.session;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.auth.interceptor.LoginSuccessInterceptor;
import com.boldfaced7.board.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

//@Component
public class SessionLoginSuccessInterceptor implements LoginSuccessInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();

        session.setMaxInactiveInterval(1800);
        session.setAttribute(SessionConst.AUTH_RESPONSE, authInfo);
    }
}
