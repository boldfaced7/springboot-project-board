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