package com.boldfaced7.api.adapter.in.web.request;

import com.boldfaced7.member.application.MemberDto;
import com.boldfaced7.member.domain.Member;
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
    @Size(max = Member.MAX_NICKNAME_LENGTH)
    private String nickname;

    public MemberDto toDto(Long targetId) {
        return MemberDto.builder()
                .memberId(targetId)
                .nickname(nickname)
                .build();
    }
}
