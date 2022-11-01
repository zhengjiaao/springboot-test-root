
-- 执行过 V3.0.0__新脚本的版本比当前版本低不会被执行.sql 文件
-- 再添加 V2.0.0__不会被执行.sql 不会被执行，并且报错

-- 创建person表
CREATE TABLE `v_person_b` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `firstname` varchar(16) NOT NULL,
  `lastname` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
