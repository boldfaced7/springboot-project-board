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