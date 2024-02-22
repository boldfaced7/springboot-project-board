package com.boldfaced7.board.config;

import com.boldfaced7.board.auth.argumentresolver.AuthInfoArgumentResolver;
import com.boldfaced7.board.auth.interceptor.AuthCheckInterceptor;
import com.boldfaced7.board.auth.interceptor.AuthInfoHoldInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthCheckInterceptor())
                .order(1)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login", "/api/signUp");

        registry.addInterceptor(new AuthInfoHoldInterceptor())
                .order(2)
                .addPathPatterns("/api/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthInfoArgumentResolver());
    }

}
