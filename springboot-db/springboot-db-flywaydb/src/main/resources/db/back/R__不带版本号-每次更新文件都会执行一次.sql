-- 每次更新文件内容都会被执行一次，不更新文件内容，下次不会被执行。

-- 创建cat表
CREATE TABLE IF NOT EXISTS `r_cat_a`
(
    `id`        bigint      NOT NULL AUTO_INCREMENT,
    `firstname` varchar(16) NOT NULL,
    `lastname`  varchar(16) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- 新增列
-- ALTER TABLE r_cat_a ADD COLUMN IF NOT EXISTS name varchar (16);
