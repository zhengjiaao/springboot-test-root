-- 插入数据语句

-- 增加用户表数据
insert into USERENTITY(id,username,age) values (SEQ_MY_USER.NEXTVAL,'小明',21) $$
insert into USERENTITY(id,username,age) values (SEQ_MY_USER.NEXTVAL,'小刘',22) $$
insert into USERENTITY(id,username,age) values (SEQ_MY_USER.NEXTVAL,'小王',20) $$
insert into USERENTITY(id,username,age) values (SEQ_MY_USER.NEXTVAL,'明月',20) $$
insert into USERENTITY(id,username,age) values (SEQ_MY_USER.NEXTVAL,'秦时',23) $$

-- 增加订单表数据
insert into orders(ordersid,ordersname,userid) values (SEQ_MY_ORDERS.NEXTVAL,'订单1',1) $$
insert into orders(ordersid,ordersname,userid) values (SEQ_MY_ORDERS.NEXTVAL,'订单2',2) $$
insert into orders(ordersid,ordersname,userid) values (SEQ_MY_ORDERS.NEXTVAL,'订单3',2) $$
insert into orders(ordersid,ordersname,userid) values (SEQ_MY_ORDERS.NEXTVAL,'订单4',2) $$
insert into orders(ordersid,ordersname,userid) values (SEQ_MY_ORDERS.NEXTVAL,'订单5',3) $$
insert into orders(ordersid,ordersname,userid) values (SEQ_MY_ORDERS.NEXTVAL,'订单6',3) $$

-- 用户组
insert into ugroup(groupid,groupname) values (SEQ_MY_GROUP.NEXTVAL,'用户组1') $$
insert into ugroup(groupid,groupname) values (SEQ_MY_GROUP.NEXTVAL,'用户组2') $$
insert into ugroup(groupid,groupname) values (SEQ_MY_GROUP.NEXTVAL,'用户组3') $$

-- 用户和用户组中间表
insert into usergroup(id,groupid,userid) values (1,1,1) $$
insert into usergroup(id,groupid,userid) values (2,2,2) $$
insert into usergroup(id,groupid,userid) values (3,3,2) $$
insert into usergroup(id,groupid,userid) values (4,3,3) $$
insert into usergroup(id,groupid,userid) values (5,2,3) $$

COMMIT $$