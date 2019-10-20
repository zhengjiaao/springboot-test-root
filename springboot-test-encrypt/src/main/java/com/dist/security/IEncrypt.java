
package com.dist.security;
/**
 * @company: DIST
 * @date：2017/5/17
 * @author: ChenYanping
 * desc
 */
public interface IEncrypt {

    public String encrypt(String str);

    public String decrypt(String str) throws Exception;
}
