--postgresql建表语句

--表作用说明：
create table userentity
(
	ID numeric(19) not null
		primary key,
	username VARCHAR(255),
	createtime TIMESTAMP(6),
	age numeric(10)
);
