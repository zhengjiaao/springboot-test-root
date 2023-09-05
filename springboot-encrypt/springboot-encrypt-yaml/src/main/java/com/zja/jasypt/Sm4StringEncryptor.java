/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-05 15:51
 * @Since:
 */
package com.zja.jasypt;

import com.zja.util.Sm4Util;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @author: zhengja
 * @since: 2023/09/05 15:51
 */
@Component("sm4StringEncryptor")
public class Sm4StringEncryptor implements StringEncryptor {
    
    @Value("${jasypt.encryptor.password:''}")
    private String key;

    @Override
    public String encrypt(String message) {
        if (!StringUtils.isEmpty(message)) {
            try {
                Sm4Util.encrypt(message, key);
            } catch (Sm4Util.EncryptionException e) {
                throw new RuntimeException(e);
            }
        }
        return message;
    }

    @Override
    public String decrypt(String encryptedMessage) {
        if (!StringUtils.isEmpty(encryptedMessage)) {
            try {
                Sm4Util.decrypt(encryptedMessage, key);
            } catch (Sm4Util.DecryptionException e) {
                throw new RuntimeException(e);
            }
        }
        return encryptedMessage;
    }
}
