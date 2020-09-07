-- 插入数据语句

-- 增加用户表数据
insert into userentity(username,age) values ('小明',21);
insert into userentity(username,age) values ('小刘',22);
insert into userentity(username,age) values ('小王',20);
insert into userentity(username,age) values ('明月',16);
insert into userentity(username,age) values ('秦时',18);

-- 增加订单表数据
insert into orders(ordersname,userid) values ('订单1',1);
insert into orders(ordersname,userid) values ('订单2',2);
insert into orders(ordersname,userid) values ('订单3',2);
insert into orders(ordersname,userid) values ('订单4',2);
insert into orders(ordersname,userid) values ('订单5',3);
insert into orders(ordersname,userid) values ('订单6',3);

-- 用户组
insert into ugroup(groupname) values ('用户组1');
insert into ugroup(groupname) values ('用户组2');
insert into ugroup(groupname) values ('用户组3');

-- 用户和用户组中间表
insert into usergroup(groupid,userid) values (1,1);
insert into usergroup(groupid,userid) values (2,2);
insert into usergroup(groupid,userid) values (3,2);
insert into usergroup(groupid,userid) values (3,3);
insert into usergroup(groupid,userid) values (2,3);

COMMIT;