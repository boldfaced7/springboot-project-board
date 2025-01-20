package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.GetMemberCommand;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.MemberTestUtil.*;

class GetMemberServiceTest {

    @DisplayName("[getMember] 회원 id를 보내면, 회원 정보를 반환")
    @Test
    void givenGetMemberCommand_whenRetrieving_thenReturnsResolvedMember() {
        // given
        var dummy = Optional.of(member(ID, EMAIL, PASSWORD, NICKNAME));
        var sut = new GetMemberService(id -> dummy);
        var command = new GetMemberCommand(ID);

        // when
        var result = sut.getMember(command);

        // then
        assertThat(result, ID, EMAIL, PASSWORD, NICKNAME);
    }

    @DisplayName("[getMember] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenRetrieving_thenThrowsException() {
        // given
        var sut = new GetMemberService(id -> Optional.empty());
        var command = new GetMemberCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getMember(command)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }
}