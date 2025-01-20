package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.Member;

public interface ChangeNicknameUseCase {
    Member changeNickname(ChangeNicknameCommand command);
}
