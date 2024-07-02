-- 创建项目表，注意：jpa默认创建的字段类型是按最大存储创建的，请根据需要修改为合理字段类型。
CREATE TABLE `t_project`
(
    `id`                 varchar(255) NOT NULL PRIMARY KEY,
    `config_json`        longblob,
    `config_text`        longtext,
    `create_time`        datetime     NOT NULL,
    `cycle`              int          NOT NULL,
    `internal`           bit(1)       DEFAULT NULL,
    `last_modified_date` datetime     DEFAULT NULL,
    `name`               varchar(100) NOT NULL,
    `remarks`            varchar(255) DEFAULT NULL,
    `sort`               bigint       NOT NULL UNIQUE,
    `state`              int          DEFAULT NULL
) ENGINE=InnoDB;