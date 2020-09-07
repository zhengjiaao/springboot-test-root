--插入数据语句

-- 增加USERENTITY表数据
insert into USERENTITY(id,username,age)
values (nextval('SEQ_MY_HIBERNATE'),'小明',21);

insert into USERENTITY(id,username,age)
values (nextval('SEQ_MY_HIBERNATE'),'小刘',22);

insert into USERENTITY(id,username,age)
values (nextval('SEQ_MY_HIBERNATE'),'小王',20);
COMMIT;