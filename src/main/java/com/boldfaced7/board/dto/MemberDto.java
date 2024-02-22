package com.boldfaced7.board.dto;

import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.response.AuthResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MemberDto(Long memberId) {
        this.memberId = memberId;
    }

    public MemberDto(Member member) {
        memberId = member.getId();
        email = member.getEmail();
        password = member.getPassword();
        nickname = member.getNickname();
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
