package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.RegisterMemberCommand;
import com.boldfaced7.exception.member.MemberEmailDuplicatedException;
import com.boldfaced7.exception.member.MemberNicknameDuplicatedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.boldfaced7.MemberTestUtil.*;

class RegisterMemberServiceTest {

    @DisplayName("[registerMember] 회원 작성 정보를 보내면, 저장된 회원 정보를 반환")
    @Test
    void givenRegisterMemberCommand_whenSaving_thenReturnsSavedMember() {
        // given
        var sut = new RegisterMemberService(
                request -> false,
                request -> false,
                member -> setField(member, "id", ID)
        );
        var command = new RegisterMemberCommand(EMAIL, PASSWORD, NICKNAME);

        // when
        var result = sut.registerMember(command);

        // then
        assertThat(result, ID, EMAIL, PASSWORD, NICKNAME);
    }

    @DisplayName("[registerMember] 이미 존재하는 이메일을 보내면, 저장된 회원 정보를 반환")
    @Test
    void givenDuplicatedEmail_whenSaving_thenThrowsException() {
        // given
        var sut = new RegisterMemberService(
                request -> true,
                null,
                null
        );
        var command = new RegisterMemberCommand(EMAIL, PASSWORD, NICKNAME);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.registerMember(command)
        );
        // then
        thrown.isInstanceOf(MemberEmailDuplicatedException.class);
    }

    @DisplayName("[registerMember] 이미 존재하는 닉네임을 보내면, 저장된 회원 정보를 반환")
    @Test
    void givenDuplicatedNickname_whenSaving_thenThrowsException() {
        // given
        var sut = new RegisterMemberService(
                request -> false,
                request -> true,
                null
        );
        var command = new RegisterMemberCommand(EMAIL, PASSWORD, NICKNAME);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.registerMember(command)
        );
        // then
        thrown.isInstanceOf(MemberNicknameDuplicatedException.class);
    }
}