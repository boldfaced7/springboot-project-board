package com.boldfaced7.board.member.application;

import com.boldfaced7.board.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long memberId;
    private String email;
    private String password;
    private String nickname;
    private boolean isActive;
    private Pageable pageable;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MemberDto(Long memberId, Pageable pageable) {
        this.memberId = memberId;
        this.pageable = pageable;
    }

    public MemberDto(boolean isActive, Pageable pageable) {
        this.isActive = isActive;
        this.pageable = pageable;
    }

    public MemberDto(Member member) {
        memberId = member.getId();
        email = member.getEmail();
        password = member.getPassword();
        nickname = member.getNickname();
        isActive = member.isActive();
        createdAt = member.getCreatedAt();
        modifiedAt = member.getModifiedAt();
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
