package com.boldfaced7.application.port.in;

public interface CheckEmailDuplicationQuery {
    boolean isDuplicated(CheckEmailDuplicationCommand command);
}
