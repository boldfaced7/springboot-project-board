package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DeleteAttachmentsCommand(
        @NotEmpty List<String> attachmentIds,
        @NotBlank String memberId
) {}
