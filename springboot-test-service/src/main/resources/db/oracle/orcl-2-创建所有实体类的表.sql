-- oracle建表语句

-- 用户表
create table userentity
(
	id NUMBER(19) not null
		primary key,
	username VARCHAR2(255 char),
	createtime TIMESTAMP(6),
	age NUMBER(19)
) $$

-- 订单表
create table orders
(
	ordersid NUMBER(19) not null
		primary key,
	ordersname VARCHAR2(255 char),
	userid NUMBER(19)
) $$

-- 用户组表
create table ugroup
(
	groupid NUMBER(19) not null
		primary key,
	groupname VARCHAR2(255 char)
) $$

-- 用户组和用户关联关系表,中间表
create table usergroup
(
	id NUMBER(19) not null
		primary key,
	groupid NUMBER(19),
	userid NUMBER(19)
) $$

COMMIT $$