/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 13:32
 * @Since:
 */
package com.zja.tika;

import org.apache.tika.exception.TikaException;
import org.apache.tika.langdetect.tika.LanguageIdentifier;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/11/08 13:32
 */
public class LanguageDetection {

    public static void main(String args[]) throws IOException, TikaException {
        LanguageIdentifier identifier = new LanguageIdentifier("this is english ");
        String language = identifier.getLanguage();
        System.out.println("Language: " + language); // en—English

        LanguageIdentifier identifier2 = new LanguageIdentifier("这是一句中文。");
        String language2 = identifier2.getLanguage();
        System.out.println("Language2: " + language2); //lt=中文
    }
}
