package com.boldfaced7.board.attachment.presentation;

import com.boldfaced7.board.common.auth.AuthInfoHolder;
import com.boldfaced7.noboilerplate.ControllerTestTemplate;
import com.boldfaced7.board.attachment.application.AttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static com.boldfaced7.noboilerplate.TestUtil.*;

@ActiveProfiles("test")
@DisplayName("AttachmentController 통합 테스트")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AttachmentControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired AttachmentService attachmentService;
    ControllerTestTemplate<AttachmentService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        AuthInfoHolder.setAuthInfo(authResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, session, attachmentService);
    }

    @DisplayName("[POST] 첨부 파일 업로드")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    <T> void PostAttachmentTest(String ignoredMessage, List<MockMultipartFile> request, T response, ResultMatcher status) throws Exception {
        testTemplate.doMultipart(attachmentUrl(), request, response, status);
    }
    static Stream<Arguments> createPostRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", multipartFiles(), null, CREATED)
        );
    }
}