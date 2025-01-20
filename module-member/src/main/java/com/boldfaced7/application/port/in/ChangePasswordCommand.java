package com.boldfaced7.application.port.in;

import com.boldfaced7.exception.auth.UnauthorizedException;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordCommand(
        @NotBlank String memberId,
        @NotBlank String requiringMemberId,
        @NotBlank String password
) {
    public ChangePasswordCommand {
        if (!memberId.equals(requiringMemberId)) {
            throw new UnauthorizedException();
        }
    }
}
