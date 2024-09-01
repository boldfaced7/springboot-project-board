package com.boldfaced7.board.common.config;

import com.boldfaced7.board.common.auth.interceptor.AuthCheckInterceptor;
import com.boldfaced7.board.common.auth.interceptor.AuthInfoHoldInterceptor;
import com.boldfaced7.board.common.auth.interceptor.LoginSuccessInterceptor;
import com.boldfaced7.board.common.auth.interceptor.LogoutSuccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthCheckInterceptor authCheckInterceptor;
    private final AuthInfoHoldInterceptor authInfoHoldInterceptor;
    private final LoginSuccessInterceptor loginSuccessInterceptor;
    private final LogoutSuccessInterceptor logoutSuccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInfoHoldInterceptor)
                .order(1)
                .addPathPatterns("/api/**");

        registry.addInterceptor(authCheckInterceptor)
                .order(2)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login", "/api/signUp", "/api/jwt");

        registry.addInterceptor(loginSuccessInterceptor)
                .order(3)
                .addPathPatterns("/api/login");

        registry.addInterceptor(logoutSuccessInterceptor)
                .order(4)
                .addPathPatterns("/api/logout");
    }
}
