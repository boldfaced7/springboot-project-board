package com.boldfaced7.board.common.config;

import com.boldfaced7.board.common.auth.NoOpPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {

    @Bean @Profile("prod")
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean @Profile({"local", "test"})
    public PasswordEncoder noOpPasswordEncoder() {
        return new NoOpPasswordEncoder();
    }
}
