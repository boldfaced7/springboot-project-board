package com.boldfaced7;

import com.boldfaced7.adapter.out.persistence.MemberJpaEntity;
import com.boldfaced7.domain.Member;
import org.assertj.core.api.Assertions;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class MemberTestUtil {
    public final static String ID = "1";
    public final static String EMAIL = "boldfaced7@naver.com";
    public final static String PASSWORD = "password";
    public final static String NEW_PASSWORD = "password";
    public final static String INVALID_MEMBER_ID = "2";
    public final static String NICKNAME = "nickname";
    public final static String NEW_NICKNAME = "new nickname";
    public final static int PAGE_NUMBER = 0;
    public final static int PAGE_SIZE = 20;
    public final static boolean VALID = true;

    public static Member member(
            String id,
            String email,
            String password,
            String nickname
    ) {
        return Member.generate(
                new Member.Id(id),
                new Member.Email(email),
                new Member.Password(password),
                new Member.Nickname(nickname),
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                null
        );
    }

    public static MemberJpaEntity jpaEntity(
            String id,
            String email,
            String password,
            String nickname
    ) {
        return new MemberJpaEntity(
                Long.parseLong(id),
                email,
                password,
                nickname,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                null
        );
    }

    public static <T> T setField(T target, String name, Object value) {
        ReflectionTestUtils.setField(target, name, value);
        return target;
    }


    public static void assertThat(
            Member result,
            String id,
            String email,
            String password,
            String nickname
    ) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
        Assertions.assertThat(result.getEmail()).isEqualTo(email);
        Assertions.assertThat(result.getPassword()).isEqualTo(password);
        Assertions.assertThat(result.getNickname()).isEqualTo(nickname);
    }
}
