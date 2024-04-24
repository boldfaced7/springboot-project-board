package com.boldfaced7.board.service;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.AuthInfoHolder;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static com.boldfaced7.board.service.Facade.*;
import static com.boldfaced7.board.service.ServiceTestTemplate.doTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@DisplayName("AttachmentService 테스트")
@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {
    @InjectMocks AttachmentService attachmentService;
    @Mock ArticleRepository mockArticleRepository;
    @Mock AttachmentRepository mockAttachmentRepository;
    @Mock LocalFileStore mockFileStore;
    Facade facade;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(authResponse());
        facade = builder()
                .mockArticleRepository(mockArticleRepository)
                .mockAttachmentRepository(mockAttachmentRepository)
                .mockFileStore(mockFileStore)
                .build();
    }

    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }

    @DisplayName("첨부파일 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getAttachmentTest(Context<Facade, AttachmentDto> context, Long request) {
        doTest(() -> attachmentService.getAttachment(request), context, facade);
    }
    static Stream<Arguments> getAttachmentTest() {
        Context<Facade, AttachmentDto> valid = new Context<>("id를 입력하면, 첨부파일을 반환");
        valid.mocks(attachmentRepository, a -> a.findById(anyLong()), Optional.of(attachment()));
        valid.asserts(dto -> assertThat(dto.getAttachmentId()).isNotNull());

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 반환 없이 예외를 던짐");
        notFound.mocks(attachmentRepository, a -> a.findById(any()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(AttachmentNotFoundException.class));

        return Stream.of(Arguments.of(valid, 1L), Arguments.of(notFound, 2L));
    }

    @DisplayName("첨부파일 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getAttachmentsTest(Context<Facade, List<?>> context) {
        doTest(() -> attachmentService.getAttachments(), context, facade);
    }
    static Stream<Arguments> getAttachmentsTest() {
        Context<Facade,List<?>> valid = new Context<>("첨부파일 목록을 반환");
        valid.mocks(attachmentRepository, AttachmentRepository::findAll, attachments());
        valid.asserts(dtos -> assertThat(dtos).isNotEmpty());

        return Stream.of(Arguments.of(valid, pageable()));
    }

    @DisplayName("게시글의 첨부파일 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getAttachmentsOfArticleTest(Context<Facade,List<?>> context, ArticleDto request) {
        doTest(() -> attachmentService.getAttachments(request), context, facade);
    }
    static Stream<Arguments> getAttachmentsOfArticleTest() {
        Context<Facade,List<?>> valid = new Context<>("게시글 id를 입력하면, 관련 첨부파일 리스트를 반환");
        valid.mocks(articleRepository, a -> a.findById(anyLong()), Optional.of(article()));
        valid.mocks(attachmentRepository, a -> a.findAllByArticle(any()), attachments());
        valid.asserts(dtos -> assertThat(dtos).isNotEmpty());

        Context<Facade, ?> notFound = new Context<>("잘못된 게시글 id를 입력하면, 반환 없이 예외를 던짐");
        notFound.mocks(articleRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleNotFoundException.class));

        return Stream.of(Arguments.of(valid, articleDto(1L)), Arguments.of(notFound, articleDto(2L)));
    }

    @DisplayName("첨부파일 저장")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void postAttachmentTests(Context<Facade, String> context, AttachmentDto request) {
        doTest(() -> attachmentService.saveAttachment(request), context, facade);
    }
    static Stream<Arguments> postAttachmentTests() {
        Context<Facade, String> valid = new Context<>("첨부파일 작성 정보를 입력하면, 첨부파일을 저장");
        valid.mocks(fileStore, f -> f.storeFile(any()), attachment());
        valid.asserts(s -> assertThat(s).isNotNull());

        return Stream.of(Arguments.of(valid, attachmentDto()));
    }

    @DisplayName("첨부파일 리스트 저장")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void postAttachmentsTests(Context<Facade, List<?>> context, List<AttachmentDto> request) {
        doTest(() -> attachmentService.saveAttachments(request), context, facade);
    }

    static Stream<Arguments> postAttachmentsTests() {
        Context<Facade, String> valid = new Context<>("첨부파일 작성 정보 리스트를 입력하면, 첨부파일들을 저장");
        valid.mocks(fileStore, f -> f.storeFiles(any()), attachments());
        valid.asserts(s -> assertThat(s).isNotEmpty());

        return Stream.of(Arguments.of(valid, List.of(attachmentDto())));
    }

    @DisplayName("첨부파일 비활성화")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deactivateArticleCommentTests(Context<Facade, ?> context, AttachmentDto request) {
        doTest(() -> attachmentService.softDeleteAttachment(request), context, facade);
    }
    static Stream<Arguments> deactivateArticleCommentTests() {
        Attachment target = attachment();

        Context<Facade, ?> valid = new Context<>("id를 입력하면, 댓글을 비활성화");
        valid.mocks(attachmentRepository, a -> a.findById(anyLong()), Optional.of(target));
        valid.asserts(() -> assertThat(target).hasFieldOrPropertyWithValue("isActive", false));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 비활성화 없이 예외를 던짐");
        notFound.mocks(attachmentRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(AttachmentNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 비활성화 없이 예외를 던짐");
        forbidden.mocks(attachmentRepository, a -> a.findById(anyLong()), Optional.of(attachment(2L)));
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));

        return Stream.of(Arguments.of(valid, attachmentDto()), Arguments.of(notFound, attachmentDto()),
                Arguments.of(forbidden, attachmentDto()));
    }

    @DisplayName("첨부파일 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deleteAttachmentTest(Context<Facade, ?> context, AttachmentDto request) {
        doTest(() -> attachmentService.hardDeleteAttachment(request), context, facade);
    }
    static Stream<Arguments> deleteAttachmentTest() {
        Context<Facade, ?> valid = new Context<>("id를 입력하면, 댓글을 삭제");
        valid.mocks(attachmentRepository, a -> a.findById(anyLong()), Optional.of(attachment()));
        valid.mocks(attachmentRepository, a -> a.delete(any()));
        valid.mocks(fileStore, f -> f.removeFile(any()));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 삭제 없이 예외를 던짐");
        notFound.mocks(attachmentRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(AttachmentNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 삭제 없이 예외를 던짐");
        forbidden.mocks(attachmentRepository, a -> a.findById(anyLong()), Optional.of(attachment(2L)));
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));

        return Stream.of(Arguments.of(valid, attachmentDto()), Arguments.of(notFound, attachmentDto()),
                Arguments.of(forbidden, attachmentDto()));
    }
}