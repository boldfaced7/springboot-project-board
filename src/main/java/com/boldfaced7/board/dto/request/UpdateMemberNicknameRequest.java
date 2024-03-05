package com.boldfaced7.board.dto.request;

import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.MemberDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMemberNicknameRequest {

    @NotBlank
    @Size(max = Member.MAX_PASSWORD_LENGTH)
    private String nickname;

    public MemberDto toDto(Long targetId) {
        return MemberDto.builder()
                .memberId(targetId)
                .nickname(nickname)
                .build();
    }
}
