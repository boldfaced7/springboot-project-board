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
public class SaveMemberRequest {

    @Email
    @NotBlank
    @Size(max = Member.MAX_EMAIL_LENGTH)
    private String email;

    @NotBlank
    @Size(max = Member.MAX_PASSWORD_LENGTH)
    private String password;

    @NotBlank
    @Size(max = Member.MAX_NICKNAME_LENGTH)
    private String nickname;

    public MemberDto toDto() {
        return MemberDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }

    public MemberDto toDto(Long targetId) {
        return MemberDto.builder()
                .memberId(targetId)
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
