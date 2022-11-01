# springboot-test-flywaydb



## flyway

- [GitHub](https://github.com/flyway/flyway)、[官网](https://flywaydb.org/) 

> SQL 版本管理 flywaydb，数据库的版本控制，支持基于SQL的迁移、基于Java的迁移、可重复迁移、占位符替换、回调、自定义迁移解析器和执行器等



## springboot flywaydb 

**简单示例 ** 

```xml
<!--spring-boot-dependencies 已内置-->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
    <version>${flyway-core.version}</version>
</dependency>
```

默认配置 `yaml`

```yaml
spring:
  flyway:
    enabled: true     # 默 true，启用flyway
    locations: classpath:db/migration   # 默 classpath:db/migration，sql脚本文件位置
```

目录下创建数据库脚本 `db/migration/*.sql ` 

> 需要先了解sql文件命名规则，必须遵循一种命名规则，`V<VERSION>__<NAME>.sql` 首先是 `V` ，然后是版本号，如果版本号有多个数字，使用`_`分隔，比如`1_0_0`版本号的后面是 2 个下划线，最后是 SQL 脚本的名称。
>
> - `V` 开头的sql脚本，仅执行一次，不可删除、修改内容，若删除or修改内容，则下次启动时会报错。
>- `R` 开头的sql脚本，可重复执行，若有更新内容，下次启动会执行，否则不执行。
> - 如果存在问题，找到 `flyway_schema_history` 表，手动删除对应的记录即可。
> 
> 正确示例：V1_0_0__init_20210702.sql

`db/migration/V1.0.0__仅执行一次的文件.sql` 

```sql
-- V 开头的sql脚本，只会执行一次，下次启动项目不会执行，不可以修改原始文件，否则下次启动会报错。

-- 创建 v_person_a 表
CREATE TABLE IF NOT EXISTS `v_person_a` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `firstname` varchar(16) NOT NULL,
  `lastname` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
```

`db/migration/R__不带版本号-每次更新文件都会执行一次.sql`

```sql
-- R 开头的sql脚本，每次更新文件内容都会被执行一次，不更新文件内容，下次不会被执行。

-- 创建 r_cat 表
CREATE TABLE IF NOT EXISTS `r_cat` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `firstname` varchar(16) NOT NULL,
  `lastname` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
```

- 执行顺序，`V` 要比 `R` 开头的sql脚本先执行。



## flywaydb 脚本版本管理

**1、`R` 和 `V` 区别和注意事项**

- **`R` 开头的sql文件，可重复执行**，若修改内容，下次则会执行，若不修改内容，下次不会执行。

- `R` 开头的sql文件，不带版本号会执行，带版本号不执行。
  - `R__描述.sql`  会执行
  - `R1.0.0__描述.sql` 不会执行，不知道是不是bug

- `R` 开头的sql文件，在 `V` 最新版本执行完成后才执行`R`。

- **`V` 开头的sql文件，不可重复执行**，仅执行一次，不可删除文件、修改内容，否则下次启动时校验会报错。

- `V`开头的sql文件，带版本号执行，不带版本号不执行。
  - `V1.0.0__描述.sql` 会执行
  - `V1.0.0_20220621_1650__描述.sql` 推荐带日期(减少冲突)，会执行
- `V__描述.sql` 未设置版本，不会执行
  - `V1.0.0_oms__描述.sql`  版本中存在字母，不会执行
  
- `V`开头的sql文件，执行顺序是按版本号大小执行
  - `V1.0.0__描述.sql`、`V1.0.1__描述.sql`、`V2.0.0__描述.sql` 依次执行
  - 注意，若当前执行过最高版本是`V2.0.0__描述.sql`，再添加低版本`V1.0.3__描述.sql`是不被执行的

2、当前`V1.0.0__描述.sql`文件中存在两条sql，会分开执行，若第二条sql执行失败，不会影响第一条sql。

> 两个版本文件`V1.0.0__描述.sql`、`V1.0.1__描述.sql`，当`1.0.0`中存在报错，则`1.0.1`不会被执行。

```sql
-- 创建person表
CREATE TABLE IF NOT EXISTS `v_person_a` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `lastname` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 新增，不存在的列 会被执行
alter table v_person_a add lastname_a varchar(16);
-- 新增，已存在的列 会报错
alter table v_person_a add lastname varchar(16);
-- 新增，不存在的列 不会被执行
alter table v_person_a add lastname_b varchar(16);
```

解决方式：

当第一次执行，故意 `新增重复列` 报错，当前`V1.0.0__描述.sql` 会被标识执行失败，修改正确后启动执行还会报错`Detected failed migration to version 1.0.0 (描述)`，必须手动删除`flyway_schema_history`表中的失败记录。

3、当`flyway_schema_history`表不存在时，数据库必须是空的，不能存在其它表、序列等，否则启动报错。

4、多个系统共用一个`schema` 版本管理配置方式

比如，实施监督业务主系统，多个子系统共用一个业务系统库`schema`时，怎么解决子系统之间的sql版本管理问题。

修改配置 `flyway_schema_history` 记录表，例 `flyway_ars_history` or `flyway_bms_history` 此时可以共存。

```yaml
spring:
  flyway:
    table: flyway_schema_history # 默 flyway_schema_history，用于记录所有的版本变化记录
    baseline-on-migrate: true    # 默 false，若发现schema数据库非空时，当存在flyway_schema_history表，会执行sql文件，不存在，则不执行sql文件并启动时抛出异常
```



## flyway 配置清单

```
flyway.baseline-description	对执行迁移时基准版本的描述.
flyway.baseline-on-migrate	当迁移时发现目标schema非空，而且带有没有元数据的表时，是否自动执行基准迁移，默认false.
flyway.baseline-version	开始执行基准迁移时对现有的schema的版本打标签，默认值为1.
flyway.check-location	检查迁移脚本的位置是否存在，默认false.
flyway.clean-on-validation-error	当发现校验错误时是否自动调用clean，默认false.
flyway.enabled	是否开启flywary，默认true.
flyway.encoding	设置迁移时的编码，默认UTF-8.
flyway.ignore-failed-future-migration	当读取元数据表时是否忽略错误的迁移，默认false.
flyway.init-sqls	当初始化好连接时要执行的SQL.
flyway.locations	迁移脚本的位置，默认db/migration.
flyway.out-of-order	是否允许无序的迁移，默认false.
flyway.password	目标数据库的密码.
flyway.placeholder-prefix	设置每个placeholder的前缀，默认${.
flyway.placeholder-replacementplaceholders	是否要被替换，默认true.
flyway.placeholder-suffix	设置每个placeholder的后缀，默认}.
flyway.placeholders.[placeholder name]	设置placeholder的value
flyway.schemas	设定需要flywary迁移的schema，大小写敏感，默认为连接默认的schema.
flyway.sql-migration-prefix	迁移文件的前缀，默认为V.
flyway.sql-migration-separator	迁移脚本的文件名分隔符，默认__
flyway.sql-migration-suffix	迁移脚本的后缀，默认为.sql
flyway.tableflyway	使用的元数据表名，默认为schema_version
flyway.target	迁移时使用的目标版本，默认为latest version
flyway.url	迁移时使用的JDBC URL，如果没有指定的话，将使用配置的主数据源
flyway.user	迁移数据库的用户名
flyway.validate-on-migrate	迁移时是否校验，默认为true
```







