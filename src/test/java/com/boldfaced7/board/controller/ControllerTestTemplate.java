package com.boldfaced7.board.controller;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.GivenAndThen;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ControllerTestTemplate<S> {
    MockMvc mvc;
    Gson gson;
    MockHttpSession session;
    S service;

    public void doMultipart(Context<S> context, String url, List<MockMultipartFile> files, List<ResultMatcher> resultMatchers) throws Exception {

        GivenAndThen givenAndThen = context != null ? context.convert(service) : null;

        // Given
        if (givenAndThen != null && givenAndThen.getGiven() != null) {
            givenAndThen.getGiven().run();
        }

        // When
        MockMultipartHttpServletRequestBuilder multipart = multipart(url);
        for (MockMultipartFile file : files) {
            multipart = multipart.file(file);
        }

        ResultActions resultActions = mvc.perform(multipart.session(session)).andDo(print());

        // Then
        for (ResultMatcher resultMatcher: resultMatchers) {
            resultActions.andExpect(resultMatcher);
        }

        if (givenAndThen != null && givenAndThen.getThen() != null) {
            givenAndThen.getThen().run();
        }
    }

    public void doGet(Context<S> context, String url, List<ResultMatcher> validators) throws Exception {
        performRequest(context, get(url), null, validators);
    }
    public <T> void doPost(Context<S> context, String url, T request, List<ResultMatcher> validators) throws Exception {
        performRequest(context, post(url), request, validators);
    }
    public <T> void doPatch(Context<S> context, String url, T request, List<ResultMatcher> validators) throws Exception {
        performRequest(context, patch(url), request, validators);
    }
    public void doDelete(Context<S> context, String url, List<ResultMatcher> validators) throws Exception {
        performRequest(context, delete(url), null, validators);
    }
    private <T> void performRequest(Context<S> context, MockHttpServletRequestBuilder requestBuilder, T request, List<ResultMatcher> resultMatchers) throws Exception {
        GivenAndThen givenAndThen = context != null ? context.convert(service) : null;

        // Given
        if (givenAndThen != null && givenAndThen.getGiven() != null) {
            givenAndThen.getGiven().run();
        }

        // When
        ResultActions resultActions = mvcPerform(requestBuilder, request);

        // Then
        for (ResultMatcher resultMatcher: resultMatchers) {
            resultActions.andExpect(resultMatcher);
        }

        if (givenAndThen != null && givenAndThen.getThen() != null) {
            givenAndThen.getThen().run();
        }
    }
    private <T> ResultActions mvcPerform(MockHttpServletRequestBuilder requestBuilder, T request) throws Exception {
        return mvc.perform(requestBuilder
                        .session(session)
                        .content(gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}