package com.boldfaced7.application.service;

import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.MemberTestUtil.*;
import static com.boldfaced7.application.port.in.UpdateMemberHelper.MemberUpdater;
import static com.boldfaced7.domain.Member.Nickname;

class DefaultUpdateMemberHelperTest {

    @DisplayName("[updateMember] 회원 id와 함수형 인터페이스 구현체를 보내면, 수정된 회원 정보를 반환")
    @Test
    void givenUpdateMemberCommand_whenUpdating_thenReturnsSavedMember() {
        // given
        var dummy = Optional.of(member(ID, EMAIL, PASSWORD, NICKNAME));
        var sut = new DefaultUpdateMemberHelper(
                id -> dummy,
                member -> member
        );
        MemberUpdater updater = member -> member.changeNickname(new Nickname(NEW_NICKNAME));


        // when
        var result = sut.updateMember(ID, updater);

        // then
        assertThat(result, ID, EMAIL, PASSWORD, NEW_NICKNAME);
        Assertions.assertThat(result.getUpdatedAt()).isNotNull();
    }

        @DisplayName("[updateMember] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenUpdating_thenThrowsException() {
        // given
        var sut = new DefaultUpdateMemberHelper(
                id -> Optional.empty(),
                null
        );

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.updateMember(ID, null)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }
}