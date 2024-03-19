package com.boldfaced7.board.controller;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.dto.AttachmentDto;
import com.boldfaced7.board.service.AttachmentService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.boldfaced7.board.ServiceMethod.*;
import static com.boldfaced7.board.TestUtil.*;

@DisplayName("AttachmentController 테스트")
@WebMvcTest({AttachmentController.class})
class AttachmentControllerTest {
    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @MockBean  AttachmentService attachmentService;
    ControllerTestTemplate<AttachmentService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, createAuthResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, gson, session, attachmentService);
    }

    @DisplayName("[POST] 첨부 파일 업로드")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    <T> void PostAttachmentTest(String ignoredMessage, Context<AttachmentService> context, List<MockMultipartFile> request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doMultipart(context, attachmentUrl(), request, resultMatchers);
    }
    static Stream<Arguments> createPostRequestTests() {
        List<MockMultipartFile> validRequest = List.of(createMultipartFile());
        List<AttachmentDto> validRequestDtos = validRequest.stream().map(AttachmentDto::new).toList();

        Map<String, Context<AttachmentService>> contexts = Map.of(
                VALID, new Context<>(saveAttachments, validRequestDtos, List.of(STORED_NAME+JPG))
        );
        List<ResultMatcher> exists = exists(List.of("attachmentNames"), "");
        List<ResultMatcher> resultMatchers = Stream.of(exists, created(), contentTypeJson()).flatMap(List::stream).toList();
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), validRequest, resultMatchers)
        );
    }
}