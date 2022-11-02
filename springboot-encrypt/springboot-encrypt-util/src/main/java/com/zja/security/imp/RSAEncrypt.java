package com.zja.security.imp;


import com.zja.base.dto.RSAEncryptKeyPairDTO;
import com.zja.security.IEncrypt;
import com.zja.utils.DecoderUtil;
import com.zja.utils.EncoderUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @company: Dist
 * @date：2018/12/19
 * @author: xupp
 * desc：
 */
public class RSAEncrypt implements IEncrypt {

    // 获取容器中的 秘钥对
    @Autowired
    @Qualifier(value = "RSAEncryptKeyPair")
    private RSAEncryptKeyPairDTO RSAEncryptKeyPair;

    // 加密
    @Override
    public String encrypt(String str) {
        String returnValue = "";
        byte[] publicKey = RSAEncryptKeyPair.getPublicKey().getEncoded();
        try {
            returnValue = EncoderUtil.rsaEncryptByPublicKey(str.getBytes(), publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    //解密
    @Override
    public String decrypt(String str) {
        byte[] privateKey = RSAEncryptKeyPair.getPrivateKey().getEncoded();
        privateKey= Base64.decodeBase64(Base64.encodeBase64String(privateKey));
        String returnValue = "";
        try {
            returnValue= DecoderUtil.rsaDecryptByPrivateKey(Base64.decodeBase64(str)
                    , privateKey);
            /*returnValue= DecoderUtil.rsaDecryptByPrivateKey( Hex.decodeHex(str.toCharArray())
                    , privateKey);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
