-- 删除测试的表和序列
declare
    countCol number;
    countTab number;
    countSeq number;
begin
--===============20191203==================start
    -- 删除无用表  upper：小写字符转化成大写的函数
    select count(*) into countTab from user_tables where table_name = upper('userentity');
    if countTab = 1 then
        execute immediate 'drop table userentity';
    end if;
    select count(*) into countTab from user_tables where table_name = upper('orders');
    if countTab = 1 then
        execute immediate 'drop table orders';
    end if;
    select count(*) into countTab from user_tables where table_name = upper('ugroup');
    if countTab = 1 then
        execute immediate 'drop table ugroup';
    end if;
    select count(*) into countTab from user_tables where table_name = upper('usergroup');
    if countTab = 1 then
        execute immediate 'drop table usergroup';
    end if;

    -- 删除无用序列 名称区分大小写
    select count(*) into countSeq from user_sequences where sequence_name = 'SEQ_MY_USER';
    if countSeq = 1 then
        execute immediate 'DROP SEQUENCE SEQ_MY_USER';
    end if;
    select count(*) into countSeq from user_sequences where sequence_name = 'SEQ_MY_GROUP';
    if countSeq = 1 then
        execute immediate 'DROP SEQUENCE SEQ_MY_GROUP';
    end if;
    select count(*) into countSeq from user_sequences where sequence_name = 'SEQ_MY_ORDERS';
    if countSeq = 1 then
        execute immediate 'DROP SEQUENCE SEQ_MY_ORDERS';
    end if;
--===============20191203==================end
end;$$