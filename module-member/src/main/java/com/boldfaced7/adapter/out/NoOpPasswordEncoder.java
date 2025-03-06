package com.boldfaced7.adapter.out;

import org.springframework.security.crypto.password.PasswordEncoder;

public class NoOpPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }
}
