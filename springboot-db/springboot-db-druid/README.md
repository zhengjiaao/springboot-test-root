# springboot-db-druid

* [druid | github](https://github.com/alibaba/druid)
* [druid-spring-boot-starter | github](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter)

**druid 数据库连接池**

## 竞品对比

| 功能类别           | 功能              | Druid        | HikariCP    | DBCP | Tomcat-jdbc     | C3P0 |
|----------------|-----------------|--------------|-------------|------|-----------------|------|
| 性能             | PSCache         | 是            | 否           | 是    | 是               | 是    |
| LRU            | 是               | 否            | 是           | 是    | 是               |      |
| SLB负载均衡支持      | 是               | 否            | 否           | 否    | 否               |      |
| 稳定性            | ExceptionSorter | 是            | 否           | 否    | 否               | 否    |
| 扩展             | 扩展              | Filter       |             |      | JdbcIntercepter |      |
| 监控             | 监控方式            | jmx/log/http | jmx/metrics | jmx  | jmx             | jmx  |
| 支持SQL级监控       | 是               | 否            | 否           | 否    | 否               |      |
| Spring/Web关联监控 | 是               | 否            | 否           | 否    | 否               |      |
|                | 诊断支持            | LogFilter    | 否           | 否    | 否               | 否    |
| 连接泄露诊断         | logAbandoned    | 否            | 否           | 否    | 否               |      |
| 安全             | SQL防注入          | 是            | 无           | 无    | 无               | 无    |
| 支持配置加密         | 是               | 否            | 否           | 否    | 否               |      |

从上表可以看出，Druid连接池在性能、监控、诊断、安全、扩展性这些方面远远超出竞品。

## druid 常用功能

1、数据库密码加密

```java
import com.alibaba.druid.filter.config.ConfigTools;

/**
 * druid 数据库密码加密
 *
 * @author: zhengja
 * @since: 2023/08/08 9:43
 */
public class ConfigToolsTest {
    @Test
    public void test_password() throws Exception {
        String password = "pass";
        //采用 RSA秘钥算法，私钥加密，公钥解密
        String[] arr = ConfigTools.genKeyPair(512);
        //以下都要保存好，避免丢失
        System.out.println("privateKey:" + arr[0]);
        System.out.println("publicKey:" + arr[1]);
        System.out.println("password:" + ConfigTools.encrypt(arr[0], password));
    }
}
```

方式一(不推荐)：在yaml配置文件里面

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/test
    driver-class-name: org.postgresql.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    username: test
    #password: pass
    password: L5HAwCsRs0vhoJbvq/ZYTUJDIQaGkS3UZA35IN9ctHcc0OndVaiXiPueXU7L6UaxpSUwVPrP+hVfMcBrN0Tc6Q==
    publicKey: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIA3UajPIb4ElNP9bIXc3bN4J3x/IOZqfuFbdYKYDSFUMtb6ybLPmTXR0Xh2xkV982dG/ZprVP3XgIWsG31lsQsCAwEAAQ==
    druid:
      filter:
        config:
          enabled: true  # 启动配置，支持数据库密码加密
      # 通过connectProperties属性来配置数据库密码加密
      connection-properties: config.decrypt=true;config.decrypt.key=${spring.datasource.publicKey}
```

方式二(推荐)：启动程序参数配置

> 毕竟公钥在代码中存在，还是不安全的，我们可以把在启动程序参数中.

```shell
#启动程序参数配置
java -jar xxxx.jar --spring.datasource.publicKey=你的公钥
```

方式三（强推荐，安全性高些）：自定义数据库密码加解密方式

```java
import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;

/**
 * @author: zhengja
 * @since: 2023/08/08 10:01
 */
@Slf4j
public class DruidDBPasswordCallback extends DruidPasswordCallback {

    //私钥
    private static final String PRIVATE_KEY = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAgDdRqM8hvgSU0/1shdzds3gnfH8g5mp+4Vt1gpgNIVQy1vrJss+ZNdHReHbGRX3zZ0b9mmtU/deAhawbfWWxCwIDAQABAkA11IPhRs1Y1N2TPzPf48Hkxo51c35hntaUUOy+Ho5srib22IoNpHKkbTbNdQAUaDveu2nciFXxQ3vsuvsTzGupAiEA/ssIqdjnwI7YKJ7qvtbFFpFct3/EK2kCbLrMxJquBO8CIQCA0su8qy9+bq/xFHY76aTUGXWrnuhhEKCqgZhJUomtpQIgGpqilfGDCVUg9uTZCAIu5BNbhgF+PzYgva+nj+PCxdsCIGhSzR04AFlCKAdyy6EPQCVnjMxz/roEbihGlN3kEozlAiBIfLrQjtZ0XyYP8WoUPbAJ15dJMRw7OUjw8uUqI6ZyMA==";
    //公钥
    private static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIA3UajPIb4ElNP9bIXc3bN4J3x/IOZqfuFbdYKYDSFUMtb6ybLPmTXR0Xh2xkV982dG/ZprVP3XgIWsG31lsQsCAwEAAQ==";
    //私钥加密后的密码
    private static final String PASS = "L5HAwCsRs0vhoJbvq/ZYTUJDIQaGkS3UZA35IN9ctHcc0OndVaiXiPueXU7L6UaxpSUwVPrP+hVfMcBrN0Tc6Q==";

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        //获取配置文件中的已经加密的密码，和yaml文件中的connectionProperties属性配置相关
        String pwd = (String) properties.get("dbPassword");
        System.out.println("DruidDBPasswordCallback 数据库加密后的密码=" + pwd);
        if (!StringUtils.isEmpty(pwd)) {
            try {
                //密码解密，并设置
                String password = ConfigTools.decrypt(PUBLIC_KEY, pwd);
                System.out.println("DruidDBPasswordCallback 数据库解密后的密码：" + password);
                setPassword(password.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```

配置文件

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/test
    driver-class-name: org.postgresql.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    username: test
    #password: pass
    password: L5HAwCsRs0vhoJbvq/ZYTUJDIQaGkS3UZA35IN9ctHcc0OndVaiXiPueXU7L6UaxpSUwVPrP+hVfMcBrN0Tc6Q==
    publicKey: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIA3UajPIb4ElNP9bIXc3bN4J3x/IOZqfuFbdYKYDSFUMtb6ybLPmTXR0Xh2xkV982dG/ZprVP3XgIWsG31lsQsCAwEAAQ==
    druid:
      # 通过connectProperties属性来配置数据库密码加密
      connection-properties: dbPassword=${spring.datasource.password}
      password-callback-class-name: com.zja.config.DruidDBPasswordCallback
```

2、监控配置及密码加密

> 监控慢SQL等

访问：http://localhost:8080/druid/
账户密码：admin/admin

3、druid配置多数据源

> 度娘上有很多资料，暂时不搞demo了
