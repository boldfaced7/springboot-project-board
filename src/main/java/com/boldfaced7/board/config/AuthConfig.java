package com.boldfaced7.board.config;

import com.boldfaced7.board.auth.NoOpPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {

    @Profile("prod") @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Profile({"local", "test"}) @Bean
    public PasswordEncoder passwordEncoderForTest() {
        return new NoOpPasswordEncoder();
    }
}
