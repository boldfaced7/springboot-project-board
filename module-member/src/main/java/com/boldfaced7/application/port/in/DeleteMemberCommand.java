package com.boldfaced7.application.port.in;

import com.boldfaced7.exception.auth.UnauthorizedException;
import jakarta.validation.constraints.NotBlank;

public record DeleteMemberCommand(
        @NotBlank String memberId,
        @NotBlank String requiringMemberId
) {

    public DeleteMemberCommand {
        if (!memberId.equals(requiringMemberId)) {
            throw new UnauthorizedException();
        }
    }
}
