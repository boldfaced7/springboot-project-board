package com.boldfaced7.board.member.domain;

import com.boldfaced7.board.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
public class Member extends BaseTimeEntity {

    public static final int MAX_EMAIL_LENGTH = 254;
    public static final int MAX_PASSWORD_LENGTH = 64;
    public static final int MAX_NICKNAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = MAX_EMAIL_LENGTH)
    private String email;

    @Column(nullable = false, length = MAX_PASSWORD_LENGTH)
    private String password;

    @Column(nullable = false, length = MAX_NICKNAME_LENGTH)
    private String nickname;

    private boolean active = true;

    protected Member() {}

    @Builder
    public Member(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void update(Member member) {
        if (member.getNickname() != null) updateNickname(member.getNickname());
        if (member.getPassword() != null) updatePassword(member.getPassword());
    }

    public void deactivate() {
        active = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
