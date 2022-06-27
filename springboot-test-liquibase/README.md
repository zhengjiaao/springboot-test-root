# springboot-test-liquibase



## liquibase

- [GitHub](https://github.com/liquibase/liquibase)、[官网](https://www.liquibase.org/) 、[参考1](https://docs.liquibase.com/tools-integrations/springboot/using-springboot-with-maven.html?_ga=2.214327703.1066171688.1656033188-2035482074.1655706906) 、[参考2-基于SQL详解](https://blog.csdn.net/qq_39559641/article/details/125259272) 

> 跟踪、版本化和部署数据库更改，通过简化数据库架构更改来节省时间和烦恼。
>

- 了解你需要的功能：
  - 支持格式：SQL、XML、YAML、JSON
  - 自动生成SQL脚本
  - 可重复的迁移，执行支持可重新运行与不可重新运行
  - 自动或通过自定义回滚SQL撤销数据库更改
  - 使用上下文和前提条件来微调脚本执行
  - 适用于使用的工具和数据库平台



## springboot liquibase

### 简单示例

```xml
    <!--spring-boot-dependencies 已内置-->
    <dependency>
        <groupId>org.liquibase</groupId>
        <artifactId>liquibase-core</artifactId>
    </dependency>
```

默认配置 `yaml`

```yaml
spring:
  liquibase:
    enabled: true   # 默true 启用
    ## 以下都是默认配置[可选的]
    change-log: classpath:/db/changelog/db.changelog-master.yaml  # 默 classpath:/db/changelog/db.changelog-master.yaml，变更日志
```

配置`db.changelog-master.yaml` 

> 检测唯一 `id/author/filepath` 的组合，若已执行，则跳过，否则执行。

```YAML
# 同时也支持依赖外部SQL文件 TODO 个人比较喜欢这种
databaseChangeLog:
  - changeSet:
      id: 1
      author: Liquibase
      changes:
        - sqlFile:
            encoding: utf8    # 默 UTF-8 文件编码
            endDelimiter: ;   # 默 ; 结束分隔符
            path: classpath:db/changelog/sqlfile/V1.0.0.sql
```

SQL文件 `V1.0.0.sql` 

```SQL
-- liquibase formatted sql

-- 格式 changeset author:id attribute1:value1 attribute2:value2 [...]

-- changeset liquibase:1
CREATE TABLE IF NOT EXISTS v_person (
  id bigint NOT NULL AUTO_INCREMENT,
  firstname varchar(16) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- changeset liquibase:2
ALTER TABLE v_person ADD lastname varchar(16);

-- changeset liquibase:3 dbms:oracle,mysql
ALTER TABLE v_person ADD name varchar(16);
```

- 必须是以 `-- liquibase formatted sql`  为开头的SQL文件
-  `SQL` 文件中每个`changeSet`都以下方注释形式开头，`changeset`注释后跟一个或多个 `SQL` 语句，用分号（或`<endDelimiter>`属性的值）分隔。
  -  `-- changeset author:id attribute1:value1 attribute2:value2 [...]`   



### SQL 文件详解

#### **数据库`Change Log`文件**

每个 `SQL` 文件必须以以下注释开头：

> 格式化的 `SQL` 文件, 为 `Liquibase` 提供元数据

```sql
--liquibase formatted sql
```

#### **`changeset`标签**

`changeSet`标签是你用来分组数据库更改的。每个`changeSet`标签都由`id`标签、`author`标签和`changelog`的`classpath`名称的组合唯一标签。`id `标签仅用作标识符，它不指示更改运行的顺序，甚至不一定是整数。如果您不知道或不希望保存实际作者，只需使用占位符值，如`UNKNOW`。

当 Liquibase执行数据库ChangeLog时，它按顺序读取`changeSet`，并针对每个`changeSet`检查databasechangelog表，以查看是否运行了` id/author/filepath`的组合。如果已运行，则将跳过`changeSet`，除非存在真正的runAlways标签。运行changeSet中的所有更改后，Liquibase 将在databasechangelog中插入带有` id/author/filepath`的新行以及`changeSet`的`MD5Sum`。

Liquibase 尝试执行每个changeSet并在每次结束时提交事务，或者如果出现错误，则回滚。某些数据库将自动提交语句，这些语句会干扰此事务设置，并可能导致意外的数据库状态。因此，通常最好每个changeSet只进行一次更改，除非有一组非自动提交更改要应用为事务（如插入数据）。

**总结一下：`changeSet`标签的组合唯一值为 ` id/author/filepath`。**



#### **格式化的 SQL 文件**

格式化的 SQL 文件中的每个changeSet都以注释形式开头

```sql
--changeset author:id attribute1:value1 attribute2:value2 [...]
```

changeset注释后跟一个或多个 SQL 语句，用分号（或<endDelimiter>属性的值）分隔。

#### **可用属性**

- scriptComments: 设置为true可以在sql执行之前移除所有注释，false则相反，默认为true。

- splitStatements:设置为false时，在“s”和“go”上不会使用Liquibase 拆分语句，默认为true。

- endDelimiter:设置语句结尾的分隔符。默认为";“可以设置为”"

- runAlways:每次运行的时候都执行此changeSet，即使之前执行过。

- runOnChange:在第一次看到更改时以及每次更改集更改时执行更改

- context:如果在运行时传递了特定上下文，则执行更改。任何字符串都可用于上下文名称，并且它们处于不区分大小写状态。

- logicalFilePath:用于在创建changeSet的唯一标识符时覆盖文件名和路径。移动或重命名change logs时是必需的。 --和下方重复，有点问题

- labels:labels是将changeSet分到context的通用方法，但是与在运行时定义一组context，然后在changeSet中定义一个匹配表达式相反，是定义好context的一组labels后运行时匹配对应表达式。

- runInTransaction:changeSet是否应作为单个事务运行（如果可能的情况）？默认为true。警告：注意这个属性。如果设置为false，并且在运行包含多个语句的changeSet的过程中发生错误，Liquibase DatabaseChangeLog表将使它们处于无效状态。

- failOnError:如果在执行变更集时发生错误，是否认为此迁移失败？

- dbms:要用于changSet的数据库的类型。运行迁移步骤时，它会根据此属性检查数据库类型。



#### **可用子标签**

- comment:changeSet的说明。XML 注释将提供相同的好处，Liquibase 的未来版本可能能够利用<comment>标记注释来生成文档

- preConditions:将执行changeSet之前必须通过的前提条件。可用于在做不可恢复的内容（如自 1.7 起删除表）之前执行数据健全性检查 

- <AnyRefactoringTag(s)>:作为此changeSet的一部分运行的数据库更改（称为重构）

- validCheckSum:列出被认为对此更改有效的校验，而不考虑数据库中存储的内容。自 1.7 起,主要用于需要修改

- changeSet:并且不希望在已运行过此修改的数据库上引发错误（不是建议的步骤）。

- rollback:描述如何回滚changeSet的 SQL 语句或重构标签

#### **`preconditions`标签**

基于数据库状态的sql动态执行。

下面是使用preConditions的几个原因:

- 记录更改日志的编写者在创建changelog时的假设。

- 强制使运行change log的用户不会违反这些假设

- 在执行不可恢复的更改（如 drop_Table）之前执行数据检查

- 根据数据库的状态控制哪些changeSet运行

#### 处理失败和错误

自 1.8 版本起，Liquibase 区分了preConditions的"失败"（检查失败）和"错误"（在执行检查时引发的异常），并且可以通过<preConditions>标签上的onFail和onError属性控制对两者的反应。

**可用的属性**

- onFail: 当proConditions遇到失败的时候如何处理

- onError:当proConditions遇到错误的时候如何处理

- onUpdateSQL:自版本1.9.5后当proConditions遇到更新SQL模型的时候如何处理

- onFailMessage:自2.0起，在proConditions失败时要输出的自定义消息。

- onErrorMessage:在proConditions错误时要输出的自定义消息。

**onFail或者onError可能的取值**

- HALT:立即停止执行整个changelog。默认的值。

- CONTINUE:跳过changeset。将在下次更新时再次尝试执行changeset。继续changelog。

- MARK_RAN:跳过changeset，但将其标记为已执行。继续changelog。

- WARN:输出警告并继续正常执行changeset/changelog。

在changset之外（例如，在changelog的开头），可能的值只有 HALT和 WARN。

**示例**

可以为每个`changeSet`指定`preConditions`。目前，仅支持 `SQL`检查`preConditions`。

```sql
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM my_table
```

**`rollback`标签**

`changset`可能包括回滚`changeSet`时要应用的语句。回滚语句是窗体的注释

```sql
--rollback SQL STATEMENT
```



#### `Change Log` 完整示例

```sql
--liquibase formatted sql

--changeset hh:1
create table liquibase_test(
     `id` int primary key,
      `name` varchar(64) DEFAULT NULL
);
--rollback drop table liquibase_test;

--changeset hh:2
insert into liquibase_test(id, name) values (1, 'zhangsan');

--changeset hh:3
--preconditions onFail:CONTINUE onError:CONTINUE
--precondition-sql-check expectedResult:1 SELECT IF(COUNT(*) = 0,0,1) FROM `sys_user` WHERE REGION_CODE like '17%'
insert into liquibase_test(id, name) values (2, 'lisi');
```



参考：

- [官网 spring liquibase sql](https://docs.liquibase.com/tools-integrations/springboot/using-springboot-with-maven.html?__hstc=128893969.de903d2688fda3c55276496765088f31.1655707065198.1655710478866.1655781918816.3&__hssc=128893969.2.1655781918816&__hsfp=1078205735&_ga=2.120240904.1639746589.1655706906-2035482074.1655706906) 
- [springboot整合liqiubase](https://cloud.tencent.com/developer/article/1711374) 
- [参考2-基于SQL详解](https://blog.csdn.net/qq_39559641/article/details/125259272) 
