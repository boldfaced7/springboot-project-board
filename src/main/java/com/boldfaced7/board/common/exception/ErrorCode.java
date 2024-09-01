package com.boldfaced7.board.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "ERROR001", "올바르지 않은 입력 값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "ERROR002", "지원하지 않는 HTTP 메소드입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "ERROR003", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "ERROR004", "접근 권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR005", "서버 내부 오류가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "ERROR006", "리소스를 찾을 수 없습니다."),

    INVALID_AUTH_VALUE(HttpStatus.UNAUTHORIZED, "AUTH001", "ID/PW가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH002", "다시 로그인해주세요."),

    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE001", "존재하지 않는 게시글입니다."),
    ARTICLE_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLECOMMNET001", "존재하지 않는 댓글입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER001", "존재하지 않는 회원입니다."),
    ATTACHMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ATTACHMENT001", "존재하지 않는 첨부파일입니다."),
    ARTICLE_TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLETICKET001", "존재하지 않는 티켓입니다."),
    MEMBER_EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "MEMBER002", "이미 존재하는 이메일입니다."),
    ARTICLE_TICKET_SOLD_OUT(HttpStatus.NOT_FOUND, "ARTICLETICKET002", "티켓이 모두 발급되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
