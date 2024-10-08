package com.boldfaced7.board.member.infrastructure;

import com.boldfaced7.board.common.config.JpaConfig;
import com.boldfaced7.board.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MemberRepository 테스트")
@Import(JpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired private MemberRepository memberRepository;

    @DisplayName("findAll()이 모든 Member 객체를 반환하는지 확인")
    @Test
    void givenMember_whenSelecting_thenWorksFine() {
        //Given
        Member member = createMember();
        memberRepository.save(member);

        // When
        List<Member> members = memberRepository.findAll();

        // Then
        assertThat(members).isNotEmpty();

    }

    @DisplayName("findAll(boolean IsActive)이 매개변수에 따른 Member 객체를 반환하는지 확인")
    @Test
    void givenActiveMemberAndInactiveMember_whenSelecting_thenWorksFine() {
        //Given
        PageRequest pageable = PageRequest.of(0, 0);

        Member member1 = createMember();
        memberRepository.save(member1);

        Member member2 = createMember();
        memberRepository.save(member2);
        member2.deactivate();

        // When
        Page<Member> activeMembers = memberRepository.findAll(true, pageable);
        Page<Member> inactiveMembers = memberRepository.findAll(false, pageable);

        // Then
        assertThat(activeMembers.getContent().get(0)).isEqualTo(member1);
        assertThat(inactiveMembers.getContent().get(0)).isEqualTo(member2);
    }

    @DisplayName("findById()가 해당하는 Member 객체를 반환하는지 확인")
    @Test
    void given_when_then() {
        //Given
        Member member = createMember();
        memberRepository.save(member);

        // When
        Member foundMember = memberRepository.findById(member.getId()).get();

        // Then
        assertThat(foundMember).isEqualTo(member);
    }

    /*
    @DisplayName("")
    @Test
    void given_when_then() {
        //Given


        // When
        memberRepository.

        // Then

    }
     */

    private Member createMember() {
        return Member.builder()
                .email("boldfaced7@email.com")
                .nickname("nickname")
                .password("password")
                .build();
    }
}