package com.boldfaced7.adapter.out.password;

import com.boldfaced7.application.port.out.EncodePasswordPort;
import com.boldfaced7.application.port.out.MatchPasswordPort;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.boldfaced7.domain.Member.*;

@Component
@RequiredArgsConstructor
public class NoOpPasswordEncoderAdapter implements
        EncodePasswordPort,
        MatchPasswordPort {
    @Override
    public Password encodePassword(String source) {
        return new Password(source);
    }

    @Override
    public boolean isMatched(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
