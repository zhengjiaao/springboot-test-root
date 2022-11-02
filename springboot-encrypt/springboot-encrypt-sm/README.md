# springboot-encrypt-sm

- [hutool 国密算法工具 SmUtil](https://hutool.cn/docs/#/crypto/国密算法工具-SmUtil)

## 国密算法简介

> 国密算法是我国自主研发创新的一套数据加密处理系列算法。从SM1-SM4分别实现了对称、非对称、摘要等算法功能。

国密即国家密码局认定的国产密码算法。主要有SM1，SM2，SM3，SM4。密钥长度和分组长度均为 128 位。

- **SM1:** 对称加密。算法 不公开 ，加密强度与AES相当。调用该算法时，需要通过加密芯片的接口进行调用。目前在软件开发过程中使用较少。
- **SM2:** 非对称加密。该算法已公开。算法基于ECC，签名速度与秘钥生成速度都快于RSA。ECC 256位（SM2采用的就是ECC 256位的一种）安全强度比RSA 2048位高，但运算速度快于RSA。软件开发过程中应用广泛。
- **SM3:** 消息摘要。该算法已公开，校验结果为256位。算法的安全性要高于 MD5 算法和 SHA-1 算法。在报文签名方面应用广泛。
- **SM4:** 无线局域网标准的分组数据算法。软件开发领域应用较少。


## 依赖引入

> java 开源社区的 bouncycastle 提供了大量的哈希算法和加密算法;  
> hutool 的 hutool-crypto 模块对加解密方法做了很好的封装，使用起来非常的方便;

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
String text = "我是一段测试aaaa";

SM2 sm2 = SmUtil.sm2();
// 公钥加密，私钥解密
String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey);
String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
```

2、使用自定义密钥对加密或解密

```java
String text = "我是一段测试aaaa";

KeyPair pair = SecureUtil.generateKeyPair("SM2");
byte[] privateKey = pair.getPrivate().getEncoded();
byte[] publicKey = pair.getPublic().getEncoded();

SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
// 公钥加密，私钥解密
String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey);
String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
```

### SM3 摘要算法

```java
//结果为：136ce3c86e4ed909b76082055a61586af20b4dab674732ebd4b599eef080c9be
String digestHex = SmUtil.sm3("aaaaa");
```

### SM4 对称加密

```java
String content = "test中文";
SymmetricCrypto sm4 = SmUtil.sm4();

String encryptHex = sm4.encryptHex(content);
String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
```
