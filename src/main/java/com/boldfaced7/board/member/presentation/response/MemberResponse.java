package com.boldfaced7.board.member.presentation.response;

import com.boldfaced7.board.member.application.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {

    private Long memberId;
    private String email;
    private String nickname;
    private LocalDateTime createdAt;

    public MemberResponse(MemberDto dto) {
        memberId = dto.getMemberId();
        email = dto.getEmail();
        nickname = dto.getNickname();
        createdAt = dto.getCreatedAt();
    }
}
