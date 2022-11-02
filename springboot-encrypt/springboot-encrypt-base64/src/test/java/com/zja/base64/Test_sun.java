/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-02 11:04
 * @Since:
 */
package com.zja.base64;

/**
 * sun 包下的 BASE64Encoder,缺点是编码和解码的效率不高
 */
public class Test_sun {

    //强烈不推荐使用
    /*@Test
    public void test() throws IOException {
        final BASE64Encoder encoder = new BASE64Encoder();
        final BASE64Decoder decoder = new BASE64Decoder();
        final String text = "字串文字";
        final byte[] textByte = text.getBytes("UTF-8");
        //编码
        final String encodedText = encoder.encode(textByte);
        System.out.println(encodedText);
        //解码
        System.out.println(new String(decoder.decodeBuffer(encodedText), "UTF-8"));
    }*/

}
