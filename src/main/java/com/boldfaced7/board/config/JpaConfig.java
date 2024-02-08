package com.boldfaced7.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean // TODO: 인증 기능이 구현되면 수정해야
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("boldfaced7");
    }
}
