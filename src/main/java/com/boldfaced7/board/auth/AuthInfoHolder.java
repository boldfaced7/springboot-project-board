package com.boldfaced7.board.auth;

import com.boldfaced7.board.dto.response.AuthResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthInfoHolder {
    private static ThreadLocal<AuthResponse> authInfo = new ThreadLocal<>();

    public static void setAuthInfo(AuthResponse authResponse) {
        authInfo.set(authResponse);
    }

    public static AuthResponse getAuthInfo() {
        return authInfo.get();
    }

    public static void releaseAuthInfo() {
        authInfo.remove();
    }

    public static boolean isEmpty() {
        return authInfo.get() == null;
    }
}
