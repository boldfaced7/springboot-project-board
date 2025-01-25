package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.DeleteMemberCommand;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.MemberTestUtil.*;

class DeleteMemberServiceTest {

    @DisplayName("[deleteMember] 삭제할 회원 id를 보내면, 삭제된 회원 정보를 반환")
    @Test
    void givenUpdateMemberCommand_whenUpdating_thenReturnsSavedMember() {
        // given
        var dummy = Optional.of(member(ID, EMAIL, PASSWORD, NICKNAME));
        var sut = new DeleteMemberService(id -> dummy, member -> member);
        var command = new DeleteMemberCommand(ID, ID);

        // when
        var result = sut.deleteMember(command);

        // then
        assertThat(result, ID, EMAIL, PASSWORD, NICKNAME);
        Assertions.assertThat(result.getDeletedAt()).isNotNull();
    }
    
    @DisplayName("[deleteMember] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenUpdating_thenThrowsException() {
        // given
        var sut = new DeleteMemberService(id -> Optional.empty(), null);
        var command = new DeleteMemberCommand(ID, ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteMember(command)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }
}