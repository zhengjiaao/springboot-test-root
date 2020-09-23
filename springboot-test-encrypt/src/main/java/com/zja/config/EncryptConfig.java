
package com.zja.config;

import com.zja.base.Constants;
import com.zja.base.dto.RSAEncryptKeyPairDTO;
import com.zja.security.*;
import com.zja.security.imp.Dap4EncryptAes256;
import com.zja.security.imp.DapEncrypt;
import com.zja.security.imp.MD5Encrypt;
import com.zja.security.imp.RSAEncrypt;
import com.zja.utils.ByteUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @company: DIST
 * @date：2017/5/17
 * @author: ChenYanping
 * desc
 */
@Configuration
public class EncryptConfig {

    @Value("${dist.encryptType}")
    public String encryptType = Constants.EncryptType.DAP;


    @Value("${dist.systemEncryptType}")
    public String systemEncryptType = Constants.EncryptType.RSA;


    @Value("${dist.isDynamicEncryption}")
    public String isDynamicEncryption = Constants.FALSE;

    /**
     * 最好的是1024位
     */
    private static final int KEY_SIZE = 1024;


    /**
     * 数据入库加密
     * @return
     */
    @Bean("dbEncrypt")
    public IEncrypt iEncrypt(){
        if (this.encryptType.equals(Constants.EncryptType.MD5)){
            return new MD5Encrypt();
        }else if(this.encryptType.equals(Constants.EncryptType.DAP4AES256)) {
            return new Dap4EncryptAes256();
        }else if(this.encryptType.equals(Constants.EncryptType.DAP)){
            return new DapEncrypt();
        }else if(this.encryptType.equals(Constants.EncryptType.RSA)){
            return new RSAEncrypt();
        }else {
            return null;
        }
    }

    /**
     * 系统数据加密
     * @return
     */
    @Bean("systemEncrypt")
    public IEncrypt systemIEncrypt(){
        if (this.systemEncryptType.equals(Constants.EncryptType.MD5)){
            return new MD5Encrypt();
        }else if(this.systemEncryptType.equals(Constants.EncryptType.DAP4AES256)) {
            return new Dap4EncryptAes256();
        }else if(this.systemEncryptType.equals(Constants.EncryptType.DAP)){
            return new DapEncrypt();
        }else if(this.systemEncryptType.equals(Constants.EncryptType.RSA)){
            return new RSAEncrypt();
        }else {
            return null;
        }
    }


    /**
     * 初始化密钥对 : 如果存在，不再创建密钥对，若不存在，则每次启动服务器都创建新的密钥对
     * @return Map 在系统初始化时候就是用单例模式生成一个密钥对
     */
    @Bean("RSAEncryptKeyPair")
    @Scope(value = "singleton")
    public RSAEncryptKeyPairDTO initKey() throws Exception {
        String webPath=this.getClass().getClassLoader().getResource("").getPath().replaceAll("%20"," ")+File.separator+"key";
        //如果动态获取被关了 那么就从文件中获取
        File priKeyFile=new File(webPath+"/priKey.key");
        File pubKeyFile=new File(webPath+"/pubKey.key");
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //控制全局秘钥生成
        if("false".equals(isDynamicEncryption)){
            if(!priKeyFile.exists()||!pubKeyFile.exists()){
          //如果任何一个文件不存在那么就重新创建
                priKeyFile.delete();pubKeyFile.delete();
                FileUtils.writeByteArrayToFile(priKeyFile,privateKey.getEncoded());
                FileUtils.writeByteArrayToFile(pubKeyFile,publicKey.getEncoded());
                return new RSAEncryptKeyPairDTO(privateKey,publicKey);
            }else{
                KeyFactory kf = KeyFactory.getInstance("RSA");
               //从文件中获取公钥以及私钥
                return new RSAEncryptKeyPairDTO(
                        (RSAPrivateKey)kf.generatePrivate(new PKCS8EncodedKeySpec(ByteUtil.streamToByteArray(new FileInputStream(priKeyFile),(int)priKeyFile.length()))),
                        (RSAPublicKey)kf.generatePublic(new X509EncodedKeySpec(ByteUtil.streamToByteArray(new FileInputStream(pubKeyFile),(int)pubKeyFile.length()))));
            }
        }
        //将密钥存储在map中
        return new RSAEncryptKeyPairDTO(privateKey,publicKey);
    }
}
