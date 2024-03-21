package com.boldfaced7.board.controller.integration;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.controller.AttachmentController;
import com.boldfaced7.board.controller.ControllerTestTemplate;
import com.boldfaced7.board.dto.AttachmentDto;
import com.boldfaced7.board.service.AttachmentService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.boldfaced7.board.ServiceMethod.saveAttachments;
import static com.boldfaced7.board.TestUtil.*;

@DisplayName("AttachmentController 통합 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class AttachmentControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @Autowired AttachmentService attachmentService;
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
    <T> void PostAttachmentTest(String ignoredMessage, List<MockMultipartFile> request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doMultipart(null, attachmentUrl(), request, resultMatchers);
    }
    static Stream<Arguments> createPostRequestTests() {
        MockMultipartFile multipartFile = new MockMultipartFile(IMAGE, UPLOADED_NAME + JPG, "image/jpeg", UPLOADED_NAME.getBytes());

        List<ResultMatcher> exists = exists(List.of("attachmentNames"), "");
        List<ResultMatcher> resultMatchers = Stream.of(exists, created(), contentTypeJson()).flatMap(List::stream).toList();
        return Stream.of(
                Arguments.of("정상 호출", List.of(multipartFile), resultMatchers)
        );
    }
}