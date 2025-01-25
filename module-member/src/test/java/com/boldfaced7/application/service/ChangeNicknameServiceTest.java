package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.ChangeNicknameCommand;
import com.boldfaced7.exception.member.MemberNicknameDuplicatedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.boldfaced7.MemberTestUtil.*;

class ChangeNicknameServiceTest {

    @DisplayName("[changeNickname] 변경 닉네임을 보내면, 수정된 회원 정보를 반환")
    @Test
    void givenChangeNicknameCommand_whenChanging_thenReturnsSavedMember() {
        // given
        var dummy = member(ID, EMAIL, PASSWORD, NEW_NICKNAME);
        var sut = new ChangeNicknameService(
                nickname -> false,
                (memberId, updater) -> dummy
        );
        var command = new ChangeNicknameCommand(ID, ID, NEW_NICKNAME);

        // when
        var result = sut.changeNickname(command);

        // then
        assertThat(result, ID, EMAIL, PASSWORD, NEW_NICKNAME);
    }

    @DisplayName("[changeNickname] 중복된 닉네임을 보내면, 예외를 던짐")
    @Test
    void givenDuplicatedNickname_whenChanging_thenThrowsException() {
        // given
        var sut = new ChangeNicknameService(
                nickname -> true,
                null
        );
        var command = new ChangeNicknameCommand(ID, ID, NEW_NICKNAME);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.changeNickname(command)
        );
        // then
        thrown.isInstanceOf(MemberNicknameDuplicatedException.class);
    }
}