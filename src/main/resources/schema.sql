create table `board`.`article` (
    `article_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT(20) NOT NULL COMMENT '회원 ID',
    `active` BIT NOT NULL COMMENT '게시글 데이터 상태',
    `title` VARCHAR(100) NOT NULL COMMENT '게시글 제목',
    `content` VARCHAR(10000) NOT NULL COMMENT '게시글 내용',
    `created_at` DATETIME(6) NOT NULL COMMENT '생성 일시',
    `modified_at` DATETIME(6) NOT NULL COMMENT '수정 일시',
    PRIMARY KEY (`article_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '게시글';

create table `board`.`article_comment` (
    `article_comment_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `article_id` BIGINT(20) NOT NULL COMMENT '게시글 ID',
    `member_id` BIGINT(20) NOT NULL COMMENT '회원 ID',
    `active` BIT NOT NULL COMMENT '댓글 데이터 상태',
    `content` VARCHAR(1000) NOT NULL COMMENT '댓글 내용',
    `created_at` DATETIME(6) NOT NULL COMMENT '생성 일시',
    `modified_at` DATETIME(6) NOT NULL COMMENT '수정 일시',
    PRIMARY KEY (`article_comment_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '댓글';

create table `board`.`article_ticket` (
    `article_ticket_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `used` BIT NOT NULL COMMENT '티켓 사용 여부',
    `member_id` BIGINT(20) NOT NULL COMMENT '회원 ID',
    `created_at` DATETIME(6) NOT NULL COMMENT '생성 일시',
    `modified_at` DATETIME(6) NOT NULL COMMENT '수정 일시',
    PRIMARY KEY (`article_ticket_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '게시글 티켓';

create table `board`.`attachment` (
    `attachment_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `article_id` BIGINT(20) NOT NULL COMMENT '게시글 ID',
    `active` BIT NOT NULL COMMENT '첨부파일 데이터 상태',
    `stored_name` VARCHAR(255) NOT NULL COMMENT '저장된 이름',
    `uploaded_name` VARCHAR(255) NOT NULL COMMENT '업로드된 이름',
    `created_at` DATETIME(6) NOT NULL COMMENT '생성 일시',
    `modified_at` DATETIME(6) NOT NULL COMMENT '수정 일시',
    PRIMARY KEY (`attachment_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '첨부파일';

create table `board`.`member` (
    `member_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `active` BIT NOT NULL COMMENT '회원 데이터 상태',
    `email` VARCHAR(254) NOT NULL COMMENT '이메일',
    `password` VARCHAR(64) NOT NULL COMMENT '비밀번호',
    `nickname` VARCHAR(20) NOT NULL COMMENT '닉네임',
    `created_at` DATETIME(6) NOT NULL COMMENT '생성 일시',
    `modified_at` DATETIME(6) NOT NULL COMMENT '수정 일시',
    PRIMARY KEY (`member_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '회원';