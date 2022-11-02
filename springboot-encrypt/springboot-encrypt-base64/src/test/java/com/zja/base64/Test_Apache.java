/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-02 11:06
 * @Since:
 */
package com.zja.base64;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * apache 包下的 Base64
 * 比 sun 包更精简，实际执行效率高不少, 缺点是需要引用 Apache Commons Codec, 但 tomcat 容器下开发, 一般都自动引入可直接使用.
 */
public class Test_Apache {

    @Test
    public void test() throws UnsupportedEncodingException {
        final Base64 base64 = new Base64();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
        //编码
        final String encodedText = base64.encodeToString(textByte);
        System.out.println(encodedText);
        //解码
        System.out.println(new String(base64.decode(encodedText), "UTF-8"));
    }
}
