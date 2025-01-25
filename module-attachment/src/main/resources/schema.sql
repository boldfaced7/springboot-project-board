create table `board`.`attachmentJpaEntity` (
    `article_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT(20) NOT NULL COMMENT '회원 ID',
    `active` BIT NOT NULL COMMENT '게시글 데이터 상태',
    `articleId` VARCHAR(100) NOT NULL COMMENT '게시글 제목',
    `nickname` VARCHAR(10000) NOT NULL COMMENT '게시글 내용',
    `created_at` DATETIME(6) NOT NULL COMMENT '생성 일시',
    `modified_at` DATETIME(6) NOT NULL COMMENT '수정 일시',
    PRIMARY KEY (`article_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '게시글';