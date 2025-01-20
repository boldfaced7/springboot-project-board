package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record RegisterMemberCommand(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String nickname
) {}
