package com.boldfaced7.board.dto.request;

import com.boldfaced7.board.dto.AuthDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String email;
    private String password;

    public AuthDto toDto() {
        return AuthDto.builder()
                .email(email)
                .password(password)
                .build();
    }
}
