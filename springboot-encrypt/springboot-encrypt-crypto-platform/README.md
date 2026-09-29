# springboot-encrypt-crypto-platform 密码服务平台

> 面向三方系统的统一密码服务平台：集中密钥管理 + 加解密 + 签名验签 + 摘要 + 消息认证码，
> 支持 **在线SDK接入、HTTP接口接入、离线SDK接入** 三种方式，帮助三方系统快速完成商用密码改造（密改/密评）。

## 一、为什么需要密码服务平台

密改（密码应用改造）要求业务系统将国际算法替换为国密算法，且密钥需集中管控、运算需可审计。传统做法是每个系统各自集成密码库，存在三大痛点：

| 痛点 | 平台解决方案 |
|------|------------|
| 各系统重复集成密码算法，改造成本高、标准不一 | 统一算法服务，三种接入方式覆盖在线/离线场景 |
| 密钥分散在各业务系统，不满足密钥集中管理要求 | 密钥由平台托管，密钥材料不出平台，全生命周期管理 |
| 密码运算无审计，密评无法通过 | 每次密钥操作与运算均记录审计日志（操作/成败/耗时/IP） |

## 二、平台能力

### 算法矩阵

| 类别 | 国密算法 | 国际算法 | 说明 |
|------|---------|---------|------|
| 对称加密 | **SM4**（CBC/PKCS5Padding） | AES-128 / AES-256 | 密文 = Base64(随机IV(16字节) + 密文)，适合大批量数据 |
| 非对称加密 | **SM2** | RSA-2048（OAEP-SHA256） | 仅适合小段数据（会话密钥等）；大批量数据用数字信封 |
| 摘要 | **SM3** | MD5 / SHA-256 | 无需密钥 |
| 消息认证码 | **HMAC-SM3** | HMAC-SHA256 | 完整性 + 来源真实性校验 |
| 数字签名 | **SM3withSM2** | SHA256withRSA | 私钥签名、公钥验签，私钥不出平台 |

### 密改对标

| 待改造算法 | 国密替代 | 场景 |
|-----------|---------|------|
| DES / AES | SM4 | 数据存储加密、传输加密 |
| RSA | SM2 | 密钥交换、数字签名 |
| MD5 / SHA-256 | SM3 | 口令摘要、数据完整性 |
| HMAC-SHA256 | HMAC-SM3 | 接口防篡改、消息认证 |

### 三种接入方式

| 接入方式 | 密钥管理 | 是否需要网络 | 适用场景 |
|---------|---------|-------------|---------|
| **在线SDK** | 平台托管（推荐，满足密评集中密钥管理） | 需要 | 常规业务系统，平台与业务系统网络可达 |
| **HTTP接口** | 平台托管 | 需要 | 非Java技术栈（Go/Python/C#等按签名规范对接） |
| **离线SDK** | 三方自管 | 不需要 | 内网隔离/专网前置机等无法在线调用的场景 |

> 三种方式算法与编码格式完全一致：平台加密的数据可在离线端解密，反之亦然。

## 三、快速开始

工程为聚合结构，含三个子模块：**sdk**（三方SDK）、**server**（服务端）、**example**（接入示例与测试），在仓库根目录操作：

| 模块 | 打包 | 职责 |
|------|------|------|
| springboot-encrypt-crypto-platform-sdk | jar | 算法核心 + DTO + 接入签名工具 + 在线/离线SDK，可独立打包发布给三方 |
| springboot-encrypt-crypto-platform-server | war | 接入认证、应用/密钥/运算/审计服务、REST接口、Swagger与Web控制台（依赖sdk） |
| springboot-encrypt-crypto-platform-example | jar | 在线/离线SDK用法示例 + 全量回归测试（依赖sdk；测试依赖server的classes包） |

```bash
# 构建全部模块（war与SDK jar输出到各模块target目录；默认跳过测试）
mvn -f springboot-encrypt/springboot-encrypt-crypto-platform/pom.xml package

# 启动平台（server模块的可执行war，默认端口8086）
java -jar springboot-encrypt/springboot-encrypt-crypto-platform/springboot-encrypt-crypto-platform-server/target/springboot-encrypt-crypto-platform-server-2.0-SNAPSHOT.war

# 或在IDEA中直接运行server模块的 CryptoPlatformApplication（开发调试推荐）

# Web控制台（内置前端页面，浏览器端自动完成HMAC-SM3接入签名）
http://localhost:8086/

# Swagger文档
http://localhost:8086/swagger-ui/index.html

# 执行全部测试（21个用例：离线SDK/签名机制/在线接入全流程/防重放/请求体限制）
mvn -f springboot-encrypt/springboot-encrypt-crypto-platform/pom.xml test -Dmaven.test.skip=false
```

启动后自动初始化**演示应用**（凭证见 `application.yml`，仅限本地演示；生产环境务必关闭，启用但未配置凭证时平台会启动失败）：

| 配置项 | 演示值 |
|-------|--------|
| appKey | `demo-app` |
| appSecret | `demo-app-secret-123456` |

并预置两把演示密钥（SM4 对称密钥 + SM2 密钥对），开箱即可在 Swagger 中调试。

### Web 控制台（内置前端页面）

浏览器打开 `http://localhost:8086/` 即为内置控制台（纯静态页面，无任何外部依赖），支持：

- **平台概览**：应用/密钥/审计统计、防重放缓存水位、算法矩阵；
- **密码运算**：摘要、对称/非对称加解密、签名验签、MAC，全部可视化操作；
- **密钥与应用管理**：创建/停用/启用/销毁密钥，注册/停用/启用应用；
- **审计日志**：按应用过滤查询（密评审计可视化）；
- **接入签名调试**：浏览器端实时生成 HMAC-SM3 签名头与签名串，供 HTTP 对接方参考。

> 控制台在浏览器端用纯 JS 实现 SM3/HMAC-SM3 完成接入签名（页脚显示算法自检状态），appSecret 仅保存在本浏览器
> localStorage；生产环境请部署于内网，并为管理端接口补充管理员认证。

## 四、接入方式一：在线SDK（推荐）

SDK 自动完成接入签名（HMAC-SM3）、时间戳与防重放，业务代码只需关注密码运算本身：

```java
// 1. 初始化客户端（appKey/appSecret由平台管理员通过 /api/app/register 发放）
CryptoPlatformClient client = new CryptoPlatformClient(
        "http://localhost:8086", "demo-app", "demo-app-secret-123456");

// 2. 在平台创建托管密钥（密钥材料不出平台）
KeyCreateResponse key = client.createKey("SM4", "订单数据加密密钥");

// 3. 加解密
EncryptResponse encrypt = client.symmetricEncrypt(key.getKeyId(), "机密业务数据");
DecryptResponse decrypt = client.symmetricDecrypt(key.getKeyId(), encrypt.getCipherText());

// 4. 签名验签（SM2密钥 → SM3withSM2）
SignResponse sign = client.sign(sm2KeyId, "待签名报文");
VerifyResponse verify = client.verify(sm2KeyId, "待签名报文", sign.getSignature());

// 5. 摘要 / 消息认证码（无需密钥）
client.digest("SM3", "abc");
client.mac(hmacKeyId, "完整性校验数据");
```

完整方法清单见 [CryptoPlatformClient](./springboot-encrypt-crypto-platform-sdk/src/main/java/com/zja/sdk/online/CryptoPlatformClient.java)：
`createKey / listKeys / getKey / destroyKey / symmetricEncrypt / symmetricDecrypt / asymmetricEncrypt / asymmetricDecrypt / digest / mac / sign / verify`。

> 可运行示例：[OnlineSdkExample](./springboot-encrypt-crypto-platform-example/src/main/java/com/zja/example/OnlineSdkExample.java)（启动平台后运行main方法，自动完成签名与全流程演示）。

## 五、接入方式二：HTTP接口

适用于非Java技术栈，按以下签名规范自行对接（与在线SDK行为一致）。

### 接入签名规范

每个请求携带4个请求头：

| 请求头 | 说明 |
|-------|------|
| `X-App-Key` | 应用接入标识 |
| `X-Timestamp` | 毫秒时间戳，超出平台时间窗口（默认5分钟）拒绝 |
| `X-Nonce` | 请求随机串（UUID），窗口内重复视为重放攻击 |
| `X-Signature` | 请求签名，小写16进制 |

**签名算法（HMAC-SM3）**：

```text
stringToSign = appKey + "\n" + timestamp + "\n" + nonce + "\n"
             + HTTP方法(大写) + "\n" + 请求URI(含query) + "\n" + SM3(请求body原始字节).hex

signature = HMAC-SM3(appSecret, stringToSign) 的16进制小写
```

> GET/DELETE 请求 body 按 `SM3(空字节串)` 参与；body 为空时 POST 同理。

### 接口清单

| 分组 | 接口 | 认证 | 说明 |
|------|------|------|------|
| 平台概览 | GET `/api/platform/overview` | 无 | 应用/密钥/审计统计、防重放缓存水位、算法矩阵（控制台首页） |
| 应用管理 | POST `/api/app/register` | 无 | 注册应用，返回appKey/appSecret（仅一次） |
| | GET `/api/app/list`、GET `/api/app/{appKey}` | 无 | 应用查询 |
| | POST `/api/app/{appKey}/disable`、`/enable` | 无 | 应用停启用 |
| 密钥管理 | POST `/api/key/create` | 签名 | 创建托管密钥 |
| | GET `/api/key/list`、GET `/api/key/{keyId}` | 签名 | 密钥查询（不含密钥材料） |
| | POST `/api/key/{keyId}/disable`、`/enable` | 签名 | 密钥停启用（轮换过渡） |
| | DELETE `/api/key/{keyId}` | 签名 | 销毁密钥（材料抹除，不可恢复） |
| 密码运算 | POST `/api/crypto/symmetric/encrypt`、`/decrypt` | 签名 | SM4/AES 对称加解密 |
| | POST `/api/crypto/asymmetric/encrypt`、`/decrypt` | 签名 | SM2/RSA 非对称加解密 |
| | POST `/api/crypto/digest` | 签名 | SM3/MD5/SHA-256 摘要 |
| | POST `/api/crypto/mac` | 签名 | HMAC-SM3/HMAC-SHA256 |
| | POST `/api/crypto/sign`、`/verify` | 签名 | 数字签名 / 验签 |
| 审计 | GET `/api/audit/list?appKey=&limit=` | 无 | 审计日志查询（最新在前） |

统一响应格式：`{"code": 200, "message": "success", "data": {...}}`，`code != 200` 即为失败（400参数/401认证/403越权/404不存在/413请求体过大/500内部）。

## 六、接入方式三：离线SDK

网络不可达（内网隔离、专网前置机）时使用，密钥自管、本地运算：

```java
OfflineCryptoSdk sdk = new OfflineCryptoSdk();

// 本地生成密钥（或导入平台通过安全渠道分发的密钥材料）
OfflineKey key = sdk.createKey("SM4", "本地数据加密密钥");

// 本地运算（与平台同算法同格式，数据可互通）
String cipherText = sdk.symmetricEncrypt(key.getKeyId(), "机密数据").getCipherText();

// 密钥材料由自己保管，可导出持久化后重新导入
OfflineKey exported = sdk.getKey(key.getKeyId());   // 含密钥材料，注意保管
```

> 密评提示：离线模式密钥为自管，需自行满足密钥全生命周期管控要求；密评若要求集中密钥管理，请优先选择在线SDK/HTTP接入。详见 [OfflineCryptoSdk](./springboot-encrypt-crypto-platform-sdk/src/main/java/com/zja/sdk/offline/OfflineCryptoSdk.java)。

> 可运行示例：[OfflineSdkExample](./springboot-encrypt-crypto-platform-example/src/main/java/com/zja/example/OfflineSdkExample.java)（无需网络，本地运算全流程演示）。

## 七、安全设计要点

- **密钥不出平台**：对称密钥/MAC密钥/私钥仅在平台内部参与运算，任何接口不返回密钥材料（`@JsonIgnore` 双保险）；
- **应用隔离**：密钥归属应用，跨应用访问一律返回404，防止密钥标识遍历探测；
- **接入三要素**：HMAC-SM3签名（防篡改，常量时间比较防时序攻击）+ 时间戳窗口（默认5分钟）+ nonce防重放（签名通过后才登记，nonce缓存定时清理并有容量上限，满时fail-closed）；
- **资源防护**：`/api/**` 请求体大小上限（默认1MB，超限返回413，有界读取防内存耗尽）；审计日志容量有上下界保护；演示凭证使用公开值时启动输出安全告警，启用但未配置凭证则启动失败（fail-fast）；
- **密钥全生命周期**：创建 → 停用/启用（轮换过渡）→ 销毁（先抹除材料再删除记录）；
- **全程审计**：密钥操作与密码运算记录操作类型、成败、耗时、来源IP（内存有界队列演示，生产应落库留存）。

## 八、配置说明

```yaml
crypto:
  platform:
    init-demo-app: true                # 启动时初始化演示应用（生产必须false）
    demo-app-key: demo-app             # 演示应用凭证（公开演示凭证会输出安全告警）
    demo-app-secret: demo-app-secret-123456
    auth-timestamp-window: 5           # 接入签名时间戳校验窗口（分钟）
    nonce-cache-capacity: 100000       # nonce防重放缓存容量上限（条，满时fail-closed）
    max-body-size: 1048576             # 请求体大小上限（字节，超限413，默认1MB）
    audit-log-capacity: 10000          # 审计日志内存保留上限（条，范围1000-1000000）
```

## 九、工程结构

```text
springboot-encrypt-crypto-platform                       # 聚合POM
├── springboot-encrypt-crypto-platform-sdk               # SDK模块（发布给三方）
│   └── com.zja
│       ├── common        # 统一响应/常量/算法枚举（SDK与服务端共用）
│       ├── core          # 算法核心：SM2/SM3/SM4/AES/RSA/HMAC（平台与离线SDK共用）
│       ├── dto           # 请求/响应模型（在线SDK与HTTP接口协议）
│       ├── security      # SignatureUtil：接入签名工具（HMAC-SM3）
│       └── sdk
│           ├── online    # 在线SDK：CryptoPlatformClient（自动签名）
│           └── offline   # 离线SDK：OfflineCryptoSdk（本地运算，密钥自管）
├── springboot-encrypt-crypto-platform-server            # 服务端模块（war）
│   ├── com.zja
│   │   ├── common        # 业务异常处理
│   │   ├── config        # Swagger/WebMvc配置
│   │   ├── controller    # REST接口（Swagger注解）
│   │   ├── entity        # 应用、密钥、审计日志实体
│   │   ├── store         # 内存存储（生产替换为数据库+密码机）
│   │   ├── security      # Body缓存过滤器/防重放/认证拦截器
│   │   ├── service       # 应用管理/密钥管理/密码运算/审计服务
│   │   └── init          # 演示数据初始化
│   └── resources
│       ├── static        # Web控制台（index.html + 独立css/js，浏览器端SM3/HMAC-SM3签名）
│       └── application.yml
└── springboot-encrypt-crypto-platform-example           # 示例与测试模块
    ├── com.zja.example   # OnlineSdkExample / OfflineSdkExample（可运行示例）
    └── test              # 全量回归测试（离线SDK/签名/在线集成/防重放/请求体限制）
```

> SDK模块可独立打包发布（产出 `crypto-platform-sdk.jar` 供三方引入，仅依赖 bcprov/commons-codec/jackson）：
> `mvn -f springboot-encrypt/springboot-encrypt-crypto-platform/springboot-encrypt-crypto-platform-sdk/pom.xml package`
>
> 服务端通过 maven-war-plugin 的 attachClasses 额外产出 classes 分类包，供 example 模块的集成测试复用服务端类与配置。

## 十、测试

| 测试类 | 用例数 | 覆盖内容 |
|-------|-------|---------|
| [OfflineCryptoSdkTest](./springboot-encrypt-crypto-platform-example/src/test/java/com/zja/sdk/offline/OfflineCryptoSdkTest.java) | 7 | SM4/AES/SM2/RSA全算法往返、SM3标准向量、密钥导入导出互通 |
| [SignatureUtilTest](./springboot-encrypt-crypto-platform-example/src/test/java/com/zja/security/SignatureUtilTest.java) | 3 | 签名串构造、验签正反例、body防篡改绑定 |
| [CryptoPlatformOnlineSdkTest](./springboot-encrypt-crypto-platform-example/src/test/java/com/zja/sdk/online/CryptoPlatformOnlineSdkTest.java) | 5 | 在线全流程、注册新应用接入、错误密钥拒绝(401)、重放攻击拒绝(401)、跨应用密钥隔离(404) |
| [ReplayProtectorTest](./springboot-encrypt-crypto-platform-example/src/test/java/com/zja/security/ReplayProtectorTest.java) | 3 | nonce重复拒绝、过期清理、容量上限fail-closed |
| [RequestBodyLimitTest](./springboot-encrypt-crypto-platform-example/src/test/java/com/zja/security/RequestBodyLimitTest.java) | 3 | 请求体有界读取单测、超限413集成验证、上限内放行 |

## 十一、生产化建议

| 演示实现 | 生产建议 |
|---------|---------|
| 内存Map存储应用/密钥 | 数据库存储元数据；密钥材料托管至密码机(HSM)/加密机 |
| appSecret明文保存在平台 | 以加密形态存储（SM4/密钥加密密钥KEK），或对接统一密管 |
| nonce防重放缓存于JVM内存 | Redis `SETNX + TTL`，支持集群部署 |
| 审计日志内存队列 | 落库/上报审计平台，按等保要求留存（≥6个月，密评建议3年） |
| 管理端接口（app注册/审计查询）无鉴权 | 增加管理员认证与权限控制，与业务接入接口网络隔离 |
| 单实例部署 | 多实例 + 负载均衡（存储外置后天然支持水平扩展） |
