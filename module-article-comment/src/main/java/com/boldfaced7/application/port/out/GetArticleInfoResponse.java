package com.boldfaced7.application.port.out;

public record GetArticleInfoResponse(
        String ArticleId,
        boolean valid
) {}
