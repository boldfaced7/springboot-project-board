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