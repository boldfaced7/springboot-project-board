package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ListAttachmentsByMemberCommand(
        @NotBlank String memberId,
        @NotNull @Size Integer pageNumber
) {}
