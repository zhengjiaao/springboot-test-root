
package com.zja.security.imp;

import com.zja.security.IEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @company: DIST
 * @date：2017/5/17
 * @author: ChenYanping
 * desc
 */
public class DapEncrypt implements IEncrypt {

    Logger logger = LoggerFactory.getLogger(DapEncrypt.class);

    @Override
    public String encrypt(String str) {
        if (str == null) {
            return str;
        }
        String returnValue = "";
        try {
            Object instance = Class.forName("JDesEncrypt").newInstance();
            Class clazz = instance.getClass();

            Method method = clazz.getMethod("EncryptCipher", new Class[] { String.class });
            returnValue = (String)method.invoke(instance, new Object[] { str });
        } catch (Exception ex) {
            logger.error("密码加密失败：");
            ex.printStackTrace();
        }
        return returnValue;
    }

    @Override
    public String decrypt(String str) {
        if (str == null) {
            return str;
        }
        String returnValue = "";
        try {
            Object instance = Class.forName("JDesEncrypt").newInstance();
            Class clazz = instance.getClass();

            Method method = clazz.getMethod("DecryptCipher", new Class[] { String.class });
            returnValue = (String)method.invoke(instance, new Object[] { str });
        } catch (Exception ex) {
            logger.error("密码解密失败：");
            ex.printStackTrace();
        }
        return returnValue;
    }
}