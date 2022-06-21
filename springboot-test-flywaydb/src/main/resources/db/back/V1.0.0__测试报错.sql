
-- 两个版本文件`V1.0.0__描述.sql`、`V1.0.1__描述.sql`，当`1.0.0`中存在报错，则`1.0.1`不会被执行。
-- 若当前sql语句执行失败，上面的sql语句会正常执行，下面的sql语句不会执行，但是当前sql文件会标识为未成功执行。

-- 创建person表
CREATE TABLE IF NOT EXISTS `v_person_a` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `lastname` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 新增，不存在的列 会被执行
alter table v_person_a add lastname_a varchar(16);
-- 新增，已存在的列 会报错
alter table v_person_a add lastname_c varchar(16);
-- 新增，不存在的列 不会被执行
alter table v_person_a add lastname_b varchar(16);

