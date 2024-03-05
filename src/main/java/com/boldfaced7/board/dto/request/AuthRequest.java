package com.boldfaced7.board.dto.request;

import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.AuthDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @Email
    @NotBlank
    @Size(max = Member.MAX_EMAIL_LENGTH)
    private String email;

    @NotBlank
    @Size(max = Member.MAX_PASSWORD_LENGTH)
    private String password;

    public AuthDto toDto() {
        return AuthDto.builder()
                .email(email)
                .password(password)
                .build();
    }
}
