
-- 不会被执行，以R开头的sql脚本，不能带版本号。

-- 创建cat表
CREATE TABLE IF NOT EXISTS `r_cat_b` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `firstname` varchar(16) NOT NULL,
  `lastname` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
