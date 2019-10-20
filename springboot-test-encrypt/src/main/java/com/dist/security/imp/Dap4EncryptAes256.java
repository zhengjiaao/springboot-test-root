package com.dist.security.imp;

import com.dist.security.IEncrypt;
import com.dist.utils.DecoderUtil;
import com.dist.utils.EncoderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yangmin
 * @date 2018/6/29.
 */
public class Dap4EncryptAes256 implements IEncrypt {

    Logger logger = LoggerFactory.getLogger(Dap4EncryptAes256.class);

    @Override
    public String encrypt(String text) {
        if(null == text){
            return text;
        }
        String returnValue = "";
        try {
            returnValue = EncoderUtil.aes256(text);

        } catch (Exception ex) {
            logger.error("密码加密失败：");
            ex.printStackTrace();
        }
        return returnValue;
    }

    @Override
    public String decrypt(String text) {
        if(null == text){
            return text;
        }
        String returnValue = "";
        try {
            returnValue = DecoderUtil.aes256(text);

        } catch (Exception ex) {
            logger.error("密码解密失败：");
            ex.printStackTrace();
        }
        return returnValue;
    }
}
