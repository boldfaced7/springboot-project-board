package com.boldfaced7.board.service;

import com.boldfaced7.board.Assertion;
import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.Attachment;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.AttachmentDto;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.error.exception.attachment.AttachmentNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.AttachmentRepository;
import com.boldfaced7.board.repository.filestore.LocalFileStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boldfaced7.board.RepoMethod.*;
import static com.boldfaced7.board.TestUtil.*;

@DisplayName("AttachmentService 테스트")
@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {
    @InjectMocks AttachmentService attachmentService;
    @Mock ArticleRepository articleRepository;
    @Mock AttachmentRepository attachmentRepository;
    @Mock LocalFileStore fileStore;
    ServiceTestTemplate testTemplate;
    DependencyHolder dependencyHolder;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(createAuthResponse());
        dependencyHolder = DependencyHolder.builder().fileStore(fileStore)
                .attachmentRepository(attachmentRepository).articleRepository(articleRepository).build();

        testTemplate = new ServiceTestTemplate(dependencyHolder);
    }

    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }


    // 조회
    @DisplayName("첨부파일 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetAttachmentRequestTests")
    void getAttachmentTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, Long request, List<Assertion<AttachmentDto>> assertions) {
        testTemplate.performRequest(contexts, attachmentService::getAttachment, request, assertions);
    }
    static Stream<Arguments> createGetAttachmentRequestTests() {
        Attachment attachment = createAttachment();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findAttachmentById, 1L, Optional.of(attachment), attachmentRepoFunc)),
                NOT_FOUND, List.of(new Context<>(findAttachmentById, 1L, Optional.empty(), attachmentRepoFunc))
        );
        Map<String, List<Assertion<AttachmentDto>>> assertions = Map.of(
                VALID, List.of(new Assertion<>()),
                NOT_FOUND, List.of(new Assertion<>(AttachmentNotFoundException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 첨부파일 리스트를 반환", contexts.get(VALID), 1L, assertions.get(VALID)),
                Arguments.of("잘못된 id를 입력하면, 반환 없이 예외를 던짐", contexts.get(NOT_FOUND), 1L, assertions.get(NOT_FOUND))
        );
    }
    @DisplayName("첨부파일 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetAttachmentsRequestTests")
    void getAttachmentsTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, List<Assertion<List<AttachmentDto>>> assertions) {
        testTemplate.performRequest(contexts, attachmentService::getAttachments, assertions);
    }
    static Stream<Arguments> createGetAttachmentsRequestTests() {
        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findAttachments, List.of(createAttachment()), attachmentRepoFunc))
        );
        Map<String, List<Assertion<List<AttachmentDto>>>> assertions = Map.of(
                VALID, List.of(new Assertion<>())
        );
        return Stream.of(Arguments.of("첨부파일 목록을 반환", contexts.get(VALID), assertions.get(VALID)));
    }

    @DisplayName("게시글의 첨부파일 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetAttachmentsOfArticleRequestTests")
    void getAttachmentsOfArticleTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, ArticleDto request, List<Assertion<List<AttachmentDto>>> assertions) {
        testTemplate.performRequest(contexts, attachmentService::getAttachments, request, assertions);
    }
    static Stream<Arguments> createGetAttachmentsOfArticleRequestTests() {
        Article article = createArticle();
        ArticleDto requestDto = ArticleDto.builder().articleId(ARTICLE_ID).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findArticleById, requestDto.getArticleId(), Optional.of(article), articleRepoFunc),
                        new Context<>(findAttachmentsByArticle, article, List.of(createAttachment()), attachmentRepoFunc)
                ),
                NOT_FOUND, List.of(new Context<>(findArticleById, requestDto.getArticleId(), Optional.empty(), articleRepoFunc))
        );
        Map<String, List<Assertion<AttachmentDto>>> assertions = Map.of(
                VALID, List.of(new Assertion<>()),
                NOT_FOUND, List.of(new Assertion<>(ArticleNotFoundException.class))
        );
        return Stream.of(
                Arguments.of("회원 id를 입력하면, 게시글 관련 첨부파일 리스트를 반환", contexts.get(VALID), requestDto, assertions.get(VALID)),
                Arguments.of("잘못된 회원 id를 입력하면, 조회 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND))
        );
    }

    @DisplayName("첨부파일 저장")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostAttachmentRequestTests")
    void PostAttachmentTests(String ignoredMessage, List<Context<DependencyHolder>> contexts, AttachmentDto request, List<Assertion<String>> assertions) {
        testTemplate.performRequest(contexts, attachmentService::saveAttachment, request, assertions);
    }
    static Stream<Arguments> createPostAttachmentRequestTests() {
        MockMultipartFile multipartFile = new MockMultipartFile(UPLOADED_NAME, UPLOADED_NAME, "text/plain", UPLOADED_NAME.getBytes());
        AttachmentDto requestDto = AttachmentDto.builder().uploadedName(UPLOADED_NAME).articleId(ARTICLE_ID).multipartFile(multipartFile).build();
        Attachment attachment = createAttachment(1L);

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(storeFile, requestDto.getMultipartFile(), attachment, fileStoreFunc))
        );
        Map<String, List<Assertion<Optional<AttachmentDto>>>> assertions = Map.of(
                VALID, List.of(new Assertion<>()),
                ARTICLE_NOT_FOUND, List.of(new Assertion<>(ArticleNotFoundException.class))
                );
        return Stream.of(
                Arguments.of("첨부파일 작성 정보를 입력하면, 첨부파일을 저장", contexts.get(VALID), requestDto, assertions.get(VALID))
        );
    }

    @DisplayName("첨부파일 비활성화")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createInactiveAttachmentRequestTests")
    void deactivateAttachmentTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, AttachmentDto request, List<Assertion<Attachment>> assertions, Attachment target) {
        testTemplate.performRequest(contexts, attachmentService::softDeleteAttachment, request, assertions, target);
    }
    static Stream<Arguments> createInactiveAttachmentRequestTests() {
        Attachment attachment = createAttachment();
        Attachment forbiddenAttachment = createAttachment();
        ReflectionTestUtils.setField(forbiddenAttachment.getArticle().getMember(), "id", MEMBER_ID+1);

        AttachmentDto requestDto = AttachmentDto.builder().attachmentId(1L).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findAttachmentById, 1L, Optional.of(attachment), attachmentRepoFunc)),
                NOT_FOUND, List.of(new Context<>(findAttachmentById, 1L, Optional.empty(), attachmentRepoFunc)),
                FORBIDDEN, List.of(new Context<>(findAttachmentById, 1L, Optional.of(forbiddenAttachment), attachmentRepoFunc))
        );
        Map<String, List<Assertion<Attachment>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(Map.of("isActive", false))),
                NOT_FOUND, List.of(new Assertion<>(AttachmentNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 첨부파일을 비활성화", contexts.get(VALID), requestDto, assertions.get(VALID), attachment),
                Arguments.of("잘못된 id를 입력하면, 비활성화 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND), null),
                Arguments.of("잘못된 회원이 접근하면, 비활성화 없이 예외를 던짐", contexts.get(FORBIDDEN), requestDto, assertions.get(FORBIDDEN), null)
        );
    }

    @DisplayName("첨부파일 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteAttachmentRequestTests")
    void deleteAttachmentTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, AttachmentDto request, List<Assertion<Attachment>> assertions) {
        testTemplate.performRequest(contexts, attachmentService::hardDeleteAttachment, request, assertions, null);
    }
    static Stream<Arguments> createDeleteAttachmentRequestTests() {
        Attachment attachment = createAttachment();
        Attachment forbiddenAttachment = createAttachment();
        ReflectionTestUtils.setField(forbiddenAttachment.getArticle().getMember(), "id", MEMBER_ID+1);

        AttachmentDto requestDto = AttachmentDto.builder().attachmentId(1L).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findAttachmentById, 1L, Optional.of(attachment), attachmentRepoFunc),
                        new Context<>(deleteAttachment, attachment, attachmentRepoFunc),
                        new Context<>(removeFile, attachment, fileStoreFunc)
                ),
                NOT_FOUND, List.of(new Context<>(findAttachmentById, 1L, Optional.empty(), attachmentRepoFunc)),
                FORBIDDEN, List.of(new Context<>(findAttachmentById, 1L, Optional.of(forbiddenAttachment), attachmentRepoFunc))
        );
        Map<String, List<Assertion<Attachment>>> assertions = Map.of(
                VALID, List.of(),
                NOT_FOUND, List.of(new Assertion<>(AttachmentNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 첨부파일을 삭제", contexts.get(VALID), requestDto, assertions.get(VALID)),
                Arguments.of("잘못된 id를 입력하면, 삭제 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND)),
                Arguments.of("잘못된 회원이 접근하면, 삭제 없이 예외를 던짐", contexts.get(FORBIDDEN), requestDto, assertions.get(FORBIDDEN))
        );
    }
}