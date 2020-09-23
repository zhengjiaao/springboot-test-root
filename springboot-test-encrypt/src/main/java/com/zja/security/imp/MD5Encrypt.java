
package com.zja.security.imp;

import com.zja.util.exception.DecryptException;
import com.zja.security.IEncrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @company: DIST
 * @date：2017/5/17
 * @author: ChenYanping
 * desc
 */
public class MD5Encrypt implements IEncrypt {

    @Override
    public String encrypt(String str) {
        if (str == null) {
            return str;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] b = md.digest();

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++)
            {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public String decrypt(String str) throws Exception {
        throw new DecryptException("MD5加密的内容无法解密:"+str);
    }
}
