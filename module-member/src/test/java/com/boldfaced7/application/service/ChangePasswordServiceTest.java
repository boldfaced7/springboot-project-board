package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.ChangePasswordCommand;
import com.boldfaced7.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.boldfaced7.MemberTestUtil.*;

class ChangePasswordServiceTest {

    @DisplayName("[changeNickname] 비밀번호 변경 정보를 보내면, 수정된 회원 정보를 반환")
    @Test
    void givenChangeNicknameCommand_whenChanging_thenReturnsSavedMember() {
        // given
        var dummy = member(ID, EMAIL, NEW_PASSWORD, NICKNAME);
        var sut = new ChangePasswordService(
                Member.Password::new,
                (memberId, updater) -> dummy
        );
        var command = new ChangePasswordCommand(ID, ID, NICKNAME);

        // when
        var result = sut.changePassword(command);

        // then
        assertThat(result, ID, EMAIL, NEW_PASSWORD, NICKNAME);
    }
}