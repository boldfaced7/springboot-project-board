package com.boldfaced7.common.auth.auth.application;

import com.boldfaced7.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    private Long memberId;
    private String email;
    private String password;
    private String nickname;

    public AuthDto(Member member) {
        memberId = member.getId();
        email = member.getEmail();
        nickname = member.getNickname();
    }

}
