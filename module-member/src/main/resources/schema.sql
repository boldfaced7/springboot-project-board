create table `board`.`memberJpa` (
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