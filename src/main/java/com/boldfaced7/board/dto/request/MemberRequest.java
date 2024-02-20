package com.boldfaced7.board.dto.request;

import com.boldfaced7.board.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequest {
    private String email;
    private String password;
    private String nickname;

    public MemberDto toDto() {
        return MemberDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
