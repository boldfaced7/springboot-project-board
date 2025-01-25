package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SetAttachmentsArticleIdCommand(
        @NotBlank String memberId,
        @NotBlank String articleId,
        @NotEmpty List<String> attachmentIds
) {}
