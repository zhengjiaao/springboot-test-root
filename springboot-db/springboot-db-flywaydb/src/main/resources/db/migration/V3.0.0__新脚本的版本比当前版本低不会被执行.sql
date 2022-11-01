
-- 创建 V2.0.0__因为存在最新版本V3.0.0不会被执行.sql
-- 创建person表
CREATE TABLE IF NOT EXISTS `v_person_c` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `firstname` varchar(16) NOT NULL,
  `lastname` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
