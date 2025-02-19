# springboot-sensitive-word-houbb

- [sensitive-word](https://github.com/houbb/sensitive-word)

## 介绍

sensitive-word 是基于 DFA 算法实现的高性能敏感词工具。目前敏感词库内容收录 6W+且不断优化更新。

特性：

* 6W+ 词库，且不断优化更新
* 基于 fluent-api 实现，使用优雅简洁
* 基于 DFA 算法，性能为 7W+ QPS，应用无感
* 支持敏感词的判断、返回、脱敏等常见操作
* 支持常见的格式转换
* 全角半角互换、英文大小写互换、数字常见形式的互换、中文繁简体互换、英文常见形式的互换、忽略重复词等
* 支持敏感词检测、邮箱检测、数字检测、网址检测等
* 支持自定义替换策略
* 支持用户自定义敏感词和白名单
* 支持数据的数据动态更新（用户自定义），实时生效
* 支持敏感词的标签接口
* 支持跳过一些特殊字符，让匹配更灵活

## 优缺点

敏感词工具类的优缺点总结

优点:

* 高效性： 敏感词的存储和检测采用了高效的数据结构和算法，能够在很短的时间内完成检测。
* 易扩展： Trie 树的结构使得添加、删除敏感词非常方便，同时 AC 自动机算法保证了高效的匹配。

缺点:

* 内存占用： 敏感词库的存储需要一定的内存，随着敏感词数量的增加，内存占用也会相应增加。

## 快速开始

#### 引入依赖

```xml
        <!--敏感词-->
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>sensitive-word</artifactId>
    <version>0.25.0</version>
</dependency>
```

#### 快速使用

```java
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
```