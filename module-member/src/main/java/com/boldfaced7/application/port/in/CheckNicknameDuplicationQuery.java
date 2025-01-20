package com.boldfaced7.application.port.in;

public interface CheckNicknameDuplicationQuery {
    boolean isDuplicated(CheckNicknameDuplicationCommand command);
}
