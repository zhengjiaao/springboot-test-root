-- 创建项目表，注意：jpa默认创建的字段类型是按最大存储创建的，请根据需要修改为合理字段类型。
create table if not exists t_project
(
    id                 varchar(255) NOT NULL PRIMARY KEY,
    --config_json        oid NULL, -- 错误的字段类型
    config_json        text NULL,
    config_text        text NULL,
    create_time        timestamp    NOT NULL,
    "cycle"            int4         NOT NULL,
    internal           bool NULL,
    last_modified_date timestamp NULL,
    "name"             varchar(100) NOT NULL,
    "type"             varchar(100) NULL,
    remarks            varchar(255) NULL,
    sort               int8         NOT NULL UNIQUE,
    "state"              int4 NULL
);