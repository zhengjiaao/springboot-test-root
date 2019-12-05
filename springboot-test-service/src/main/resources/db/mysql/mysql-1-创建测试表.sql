-- 创建表
-- 用户表，如果表不存在，则创建，id自增且是主键，username不能null
CREATE TABLE IF NOT EXISTS userentity(
   id INT UNSIGNED AUTO_INCREMENT,
   username VARCHAR(50) not null,
   age int,
   createtime DATE,
   PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 订单表
CREATE TABLE IF NOT EXISTS orders(
   ordersid INT UNSIGNED AUTO_INCREMENT,
   userid INT,
   ordersname VARCHAR(100) NOT NULL,
   PRIMARY KEY (ordersid)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 用户组表
CREATE TABLE IF NOT EXISTS ugroup(
   groupid INT UNSIGNED AUTO_INCREMENT,
   groupname VARCHAR(100) NOT NULL,
   PRIMARY KEY (groupid)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 用户组和用户关联关系表,中间表
CREATE TABLE IF NOT EXISTS `usergroup`(
   id INT UNSIGNED AUTO_INCREMENT,
   groupid INT,
   userid VARCHAR(100) NOT NULL,
   PRIMARY KEY (id)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

COMMIT;
