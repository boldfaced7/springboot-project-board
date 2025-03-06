package com.boldfaced7.application.port.out;

public interface MatchPasswordPort {
    boolean isMatched(String rawPassword, String encodedPassword);
}
