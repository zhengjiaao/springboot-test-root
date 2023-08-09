# springboot-encrypt-sm

- [hutool 国密算法工具 SmUtil](https://hutool.cn/docs/#/crypto/国密算法工具-SmUtil)

## 国密算法简介

> 国密算法是我国自主研发创新的一套数据加密处理系列算法。从SM1-SM4分别实现了对称、非对称、摘要等算法功能。

国密即国家密码局认定的国产密码算法。主要有SM1，SM2，SM3，SM4。密钥长度和分组长度均为 128 位。

- **SM1:** 对称加密。算法 不公开 ，加密强度与AES相当。调用该算法时，需要通过加密芯片的接口进行调用。目前在软件开发过程中使用较少。
- **SM2:** 非对称加密和签名。该算法已公开。算法基于ECC，签名速度与秘钥生成速度都快于RSA。ECC 256位（SM2采用的就是ECC
  256位的一种）安全强度比RSA 2048位高，但运算速度快于RSA。软件开发过程中应用广泛。
- **SM3:** 摘要签名(Hash)算法，消息摘要。该算法已公开，校验结果为256位。算法的安全性要高于 MD5 算法和 SHA-1 算法。在报文签名方面应用广泛。
- **SM4:** 对称(分组)加密算法。该算法已公开，无线局域网标准的分组数据算法。软件开发领域应用较少。

## 依赖引入

> java 开源社区的 [bouncycastle.org](https://www.bouncycastle.org/java.html) 提供了大量的哈希算法和加密算法;  
> hutool 的 [hutool-crypto](https://doc.hutool.cn/pages/SmUtil/#%E4%BB%8B%E7%BB%8D) 模块对加解密方法做了很好的封装，使用起来非常的方便;

```xml

<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15to18</artifactId>
    <version>1.72</version>
</dependency>
<dependency>
<groupId>cn.hutool</groupId>
<artifactId>hutool-crypto</artifactId>
<version>5.8.9</version>
</dependency>
```

## 使用示例

[hutool 国密算法工具 SmUtil](https://hutool.cn/docs/#/crypto/国密算法工具-SmUtil)

### SM2 非对称加解密

1、使用随机生成的密钥对加密或解密

```java
String text="我是一段测试aaaa";

        SM2 sm2=SmUtil.sm2();
// 公钥加密，私钥解密
        String encryptStr=sm2.encryptBcd(text,KeyType.PublicKey);
        String decryptStr=StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr,KeyType.PrivateKey));
```

2、使用自定义密钥对加密或解密

```java
String text="我是一段测试aaaa";

        KeyPair pair=SecureUtil.generateKeyPair("SM2");
        byte[]privateKey=pair.getPrivate().getEncoded();
        byte[]publicKey=pair.getPublic().getEncoded();

        SM2 sm2=SmUtil.sm2(privateKey,publicKey);
// 公钥加密，私钥解密
        String encryptStr=sm2.encryptBcd(text,KeyType.PublicKey);
        String decryptStr=StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr,KeyType.PrivateKey));
```

### SM3 摘要算法

```java
//结果为：136ce3c86e4ed909b76082055a61586af20b4dab674732ebd4b599eef080c9be
String digestHex=SmUtil.sm3("aaaaa");
```

### SM4 对称加密

```java
String content="test中文";
        SymmetricCrypto sm4=SmUtil.sm4();

        String encryptHex=sm4.encryptHex(content);
        String decryptStr=sm4.decryptStr(encryptHex,CharsetUtil.CHARSET_UTF_8);
```

## 算法对比

- SM2（非对称加密）与其它加密算法比较
    - 国际上通用的非对称加密算法有RSA、D-H算法；
    - SM2属于椭圆曲线加密算法（ECC）
- SM3(hash算法)与其它hash算法比较
    - 国际上通用的hash算法为SHA系列算法，MD4 MD5算法
    - SM3实在SHA-256基础上改进的算法
- SM4（对称）与其它算法的比较
    - 国际上通过的对称加密算法是DES/AES算法
    - SM4在进行分组加密的时候进行了非线性变换

### SM1 与 AES 的比较

> SM1是国家密码管理部门审批的分组密码算法，分组长度和密钥长度都为128比特，算法安全保密强度及相关软硬件实现性能与AES相当，该算法不公开，仅以
> IP 核的形式存在于芯片中。

|          | SM1          | AES          |
|----------|--------------|--------------|
| 算法结构     | 基本圆曲线( ECC ) | 基于特殊的可逆模幂运算  |  
| 区块长度     | 128bit       | 128bit       |  
| 存储空间(长度) | 128bit       | 2048-4096bit |  
| 秘钥生成速度   | 快            | 好            |  
| 解密加密速度   | 快            | 快            |  

### SM2 与 RSA 的比较

> SM2算法和RSA算法都是公钥密码算法，SM2算法是一种更先进安全的算法，在我们国家商用密码体系中被用来替换RSA算法。   
> SM2性能更优更安全：密码复杂度高、处理速度快、硬件性能消耗更小。  
> 注：
> 1. 亚指数级算法复杂度不到指数级别的算法。
> 2. RSA秘钥生成慢，不建议使用。例：主频5G赫兹的话，RSA需要2-3秒的，车联网中根本无法接受，而SM2只需要几十毫秒。

|          | SM2         | RSA          |
|----------|-------------|--------------|
| 算法结构     | ECC的一种      | 基于特殊的可逆模幂运算  |  
| 计算复杂度    | **完全指数级**   | **亚指数级**     |  
| 存储空间(长度) | 192-256bit  | 2048-4096bit |  
| 秘钥生成速度   | 较RSA算法快百倍以上 | 慢            |  
| 加密速度     | 较快          | 快            |  
| 解密速度     | 较快          | 一般           |  

### SM3 与 SHA256 的比较

> SM3是摘要加密算法，该算法适用于商用密码应用中的数字签名和验证以及随机数的生成，是在SHA-256基础上改进实现的一种算法。  
> SM3算法采用Merkle-Damgard结构，消息分组长度为512位，摘要值长度为256位。

|      | SM2              | RSA         |
|------|------------------|-------------|
| 算法结构 | Merkle-Damgard结构 | 基于特殊的可逆模幂运算 |  
| 消息长度 | 2^64位            | <2^64位      |  
| 分组长度 | 512位             | 512位        |  
| 摘要长度 | 256位             | 256位        |  
| 计算步骤 | 64步              | 64步         |  
| 加密速度 | 快                | 快           |  

### SM4 与 3DES 的比较

> SM4分组密码算法是我国自主设计的分组对称密码算法，用于实现数据的加密/解密运算，以保证数据和信息的机密性，是专门为无线局域网产品设计的加密算法。

|      | SM4        | 3DES         |
|------|------------|--------------|
| 算法结构 | 非平衡Feistel | 使用标准的算术和逻辑运算 |  
| 计算轮数 | 32         | 48           |  
| 分组长度 | 128位       | 128位         |  
| 秘钥长度 | 128位       | 128位         |  
| 性能   | 快          | 中            |  
| 安全性  | 快          | 中            |  

另外，SM7、SM9、ZUC祖冲之算法都属于国密算法范畴，特殊说明SM1和SM9是非公开算法。