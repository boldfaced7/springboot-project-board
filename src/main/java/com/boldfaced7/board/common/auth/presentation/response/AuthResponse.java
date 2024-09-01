package com.boldfaced7.board.common.auth.presentation.response;

import com.boldfaced7.board.common.auth.application.AuthDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private Long memberId;
    private String email;
    private String nickname;

    public AuthResponse(AuthDto authDto) {
        memberId = authDto.getMemberId();
        email = authDto.getEmail();
        nickname = authDto.getNickname();
    }
}
