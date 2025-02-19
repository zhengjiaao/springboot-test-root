package com.zja.sensitive.word.houbb;

import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.api.IWordTag;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.check.WordChecks;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.github.houbb.sensitive.word.support.tag.WordTags;
import com.zja.sensitive.word.houbb.custom.MyWordAllow;
import com.zja.sensitive.word.houbb.custom.MyWordDeny;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @Author: zhengja
 * @Date: 2025-02-19 13:17
 */
public class SensitiveWordTest {

    // 获取资源文件路径
    private String getResourcePath(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            throw new IOException("Resource file [" + path + "] not found.");
        }

        return resource.getURI().getPath();
    }

    // 动态加载（用户自定义）:可以通过 wordDeny() 指定敏感词，wordAllow() 指定非敏感词，通过 init() 初始化敏感词字典。
    @Test
    public void test_init() {

        // 系统默认配置
        SensitiveWordBs wordBs = SensitiveWordBs.newInstance()
                .wordDeny(WordDenys.defaults())
                .wordAllow(WordAllows.defaults())
                .init();

        final String text = "五星红旗迎风飘扬，毛主席的画像屹立在天安门前。";
        Assertions.assertTrue(wordBs.contains(text));


        //  指定自己的实现
        String text2 = "这是一个测试，我的自定义敏感词。";

        SensitiveWordBs wordBs2 = SensitiveWordBs.newInstance()
                .wordDeny(new MyWordDeny())
                .wordAllow(new MyWordAllow())
                .init();

        // 这里只有 我的自定义敏感词 是敏感词，而 测试 不是敏感词。
        Assertions.assertEquals("[我的自定义敏感词]", wordBs2.findAll(text2).toString());


        // 系统+自定义一起使用，多个白名单、黑名单配置

        IWordDeny wordDeny = WordDenys.chains(WordDenys.defaults(), new MyWordDeny());
        IWordAllow wordAllow = WordAllows.chains(WordAllows.defaults(), new MyWordAllow());

        SensitiveWordBs wordBs3 = SensitiveWordBs.newInstance()
                .wordDeny(wordDeny)
                .wordAllow(wordAllow)
                .init();

        Assertions.assertEquals("[我的自定义敏感词]", wordBs3.findAll(text2).toString());
    }

    /**
     * 敏感词检测：默认启用。
     *
     * @since 0.25.0
     */
    @Test
    public void test_WordCheck() {
        final String text = "五星红旗迎风飘扬，毛主席的画像屹立在天安门前。";

        // 写法1：
        SensitiveWordBs wordBs = SensitiveWordBs.newInstance()
                .enableWordCheck(true)
                .init();
        List<String> wordList = wordBs.findAll(text);

        // 写法2：
        // List<String> wordList = SensitiveWordBs.newInstance()
        //         .enableWordCheck(true)
        //         .init().findAll(text);


        Assertions.assertEquals("[五星红旗, 毛主席, 天安门]", wordList.toString());
        Assertions.assertEquals("****迎风飘扬，***的画像屹立在***前。", wordBs.replace(text));
    }

    /**
     * 邮箱检测：邮箱等个人信息，默认未启用。
     *
     * @since 0.25.0
     */
    @Test
    public void test_emailCheck() {
        final String text = "楼主好人，邮箱 sensitiveword@xx.com";
        List<String> wordList = SensitiveWordBs.newInstance().enableEmailCheck(true).init().findAll(text);
        Assertions.assertEquals("[sensitiveword@xx.com]", wordList.toString());
    }

    /**
     * 连续数字检测：一般用于过滤手机号/QQ等广告信息，默认未启用。
     *
     * @since 0.25.0
     */
    @Test
    public void test_numCheckLen() {
        final String text = "你懂得：12345678";

        // 默认检测 8 位
        List<String> wordList = SensitiveWordBs.newInstance().enableNumCheck(true).init().findAll(text);
        Assertions.assertEquals("[12345678]", wordList.toString());

        // 指定数字的长度，避免误杀
        List<String> wordList2 = SensitiveWordBs.newInstance().enableNumCheck(true).numCheckLen(9).init().findAll(text);
        Assertions.assertEquals("[]", wordList2.toString());
    }


    /**
     * 网址检测：用于过滤常见的网址信息，默认未启用。
     *
     * @since 0.25.0
     */
    @Test
    public void test_UrlCheck() {
        final String text = "点击链接 https://www.baidu.com 查看答案，当然也可以是 baidu.com、www.baidu.com";

        final SensitiveWordBs sensitiveWordBs = SensitiveWordBs.newInstance().enableUrlCheck(true) // 启用URL检测
                .wordCheckUrl(WordChecks.urlNoPrefix()) // 指定检测的方式
                .init();
        List<String> wordList = sensitiveWordBs.findAll(text);
        Assertions.assertEquals("[www.baidu.com, baidu.com, www.baidu.com]", wordList.toString());

        Assertions.assertEquals("点击链接 https://************* 查看答案，当然也可以是 *********、*************", sensitiveWordBs.replace(text));
    }

    /**
     * IPV4 检测：避免用户通过 ip 绕过网址检测等，默认未启用。
     *
     * @since 0.25.0
     */
    @Test
    public void test_IPV4Check() {
        final String text = "个人网站，如果网址打不开可以访问 127.0.0.1。";
        final SensitiveWordBs sensitiveWordBs = SensitiveWordBs.newInstance().enableIpv4Check(true).init();
        List<String> wordList = sensitiveWordBs.findAll(text);
        Assertions.assertEquals("[127.0.0.1]", wordList.toString());
    }

    /**
     * 敏感词标签：
     * 有时候我们希望对敏感词加一个分类标签：比如社情、暴/力等等。
     * 这样后续可以按照标签等进行更多特性操作，比如只处理某一类的标签。
     */
    @Test
    public void test_WordTags() throws IOException {
        // 零售 广告,网络
        String path = getResourcePath("dict_tag_test.txt");

        // 演示默认方法
        IWordTag wordTag = WordTags.file(path);
        SensitiveWordBs sensitiveWordBs = SensitiveWordBs.newInstance().wordTag(wordTag).init();

        Set<String> tagSet = sensitiveWordBs.tags("零售");
        Assertions.assertEquals("[广告, 网络]", tagSet.toString());

        // 演示指定分隔符
        IWordTag wordTag2 = WordTags.file(path, " ", ",");
        SensitiveWordBs sensitiveWordBs2 = SensitiveWordBs.newInstance().wordTag(wordTag2).init();
        Set<String> tagSet2 = sensitiveWordBs2.tags("零售");
        Assertions.assertEquals("[广告, 网络]", tagSet2.toString());
    }

}
