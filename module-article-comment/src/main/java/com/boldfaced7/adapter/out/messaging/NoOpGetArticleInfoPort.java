package com.boldfaced7.adapter.out.messaging;

import com.boldfaced7.MessagingAdapter;
import com.boldfaced7.application.port.out.GetArticleInfoPort;
import com.boldfaced7.application.port.out.GetArticleInfoRequest;
import com.boldfaced7.application.port.out.GetArticleInfoResponse;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@MessagingAdapter
@RequiredArgsConstructor
public class NoOpGetArticleInfoPort implements GetArticleInfoPort {
    @Override
    public Optional<GetArticleInfoResponse> findArticle(GetArticleInfoRequest request) {
        return Optional.empty();
    }
}
