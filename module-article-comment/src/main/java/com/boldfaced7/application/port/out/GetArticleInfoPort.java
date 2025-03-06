package com.boldfaced7.application.port.out;

import java.util.Optional;

public interface GetArticleInfoPort {
    Optional<GetArticleInfoResponse> findArticle(GetArticleInfoRequest request);
}
