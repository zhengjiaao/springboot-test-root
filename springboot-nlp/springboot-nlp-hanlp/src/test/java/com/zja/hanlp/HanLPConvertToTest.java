package com.zja.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.corpus.dictionary.StringDictionary;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.seg.Other.CommonAhoCorasickDoubleArrayTrieSegment;
import com.hankcs.hanlp.seg.Other.CommonAhoCorasickSegmentUtil;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * HanLP 转换 (汉字转拼音、拼音转汉字、简繁体字转换)
 *
 * @author: zhengja
 * @since: 2024/02/20 16:29
 */
public class HanLPConvertToTest {

    // 汉字转拼音
    @Test
    public void convertToPinyin_test() {
        String text = "重载不是重任！";
        List<Pinyin> pinyinList = HanLP.convertToPinyinList(text);
        System.out.print("原文,");
        for (char c : text.toCharArray()) {
            System.out.printf("%c,", c);
        }
        System.out.println();

        System.out.print("拼音（数字音调）,");
        for (Pinyin pinyin : pinyinList) {
            System.out.printf("%s,", pinyin);
        }
        System.out.println();

        System.out.print("拼音（符号音调）,");
        for (Pinyin pinyin : pinyinList) {
            System.out.printf("%s,", pinyin.getPinyinWithToneMark());
        }
        System.out.println();

        System.out.print("拼音（无音调）,");
        for (Pinyin pinyin : pinyinList) {
            System.out.printf("%s,", pinyin.getPinyinWithoutTone());
        }
        System.out.println();

        System.out.print("声调,");
        for (Pinyin pinyin : pinyinList) {
            System.out.printf("%s,", pinyin.getTone());
        }
        System.out.println();

        System.out.print("声母,");
        for (Pinyin pinyin : pinyinList) {
            System.out.printf("%s,", pinyin.getShengmu());
        }
        System.out.println();

        System.out.print("韵母,");
        for (Pinyin pinyin : pinyinList) {
            System.out.printf("%s,", pinyin.getYunmu());
        }
        System.out.println();

        System.out.print("输入法头,");
        for (Pinyin pinyin : pinyinList) {
            System.out.printf("%s,", pinyin.getHead());
        }
        System.out.println();

        // 拼音转换可选保留无拼音的原字符
        System.out.println(HanLP.convertToPinyinString("截至2012年，", " ", true));
        System.out.println(HanLP.convertToPinyinString("截至2012年，", " ", false));
    }

    // 拼音转汉字
    @Test
    public void PinyinToChinese_test() {
        StringDictionary dictionary = new StringDictionary("=");
        dictionary.load(HanLP.Config.PinyinDictionaryPath);
        TreeMap<String, Set<String>> map = new TreeMap<String, Set<String>>();
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            String pinyins = entry.getValue().replaceAll("[\\d,]", "");
            Set<String> words = map.get(pinyins);
            if (words == null) {
                words = new TreeSet<String>();
                map.put(pinyins, words);
            }
            words.add(entry.getKey());
        }
        Set<String> words = new TreeSet<String>();
        words.add("绿色");
        words.add("滤色");
        map.put("lvse", words);

        // 1.5.2及以下版本
        AhoCorasickDoubleArrayTrie<Set<String>> trie = new AhoCorasickDoubleArrayTrie<Set<String>>();
        trie.build(map);
        System.out.println(CommonAhoCorasickSegmentUtil.segment("renmenrenweiyalujiangbujianlvse", trie));

        // 1.5.3及以上版本
        CommonAhoCorasickDoubleArrayTrieSegment<Set<String>> segment = new CommonAhoCorasickDoubleArrayTrieSegment<Set<String>>(map);
        System.out.println(segment.segment("renmenrenweiyalujiangbujianlvse"));
    }


    // 简繁体字转换
    @Test
    public void TraditionalChinese2SimplifiedChinese_test() {

        System.out.println(HanLP.convertToTraditionalChinese("“以后等你当上皇后，就能买草莓庆祝了”。发现一根白头发"));
        System.out.println(HanLP.convertToSimplifiedChinese("憑藉筆記簿型電腦寫程式HanLP"));
        // 简体转台湾繁体
        System.out.println(HanLP.s2tw("hankcs在台湾写代码"));
        // 台湾繁体转简体
        System.out.println(HanLP.tw2s("hankcs在臺灣寫程式碼"));
        // 简体转香港繁体
        System.out.println(HanLP.s2hk("hankcs在香港写代码"));
        // 香港繁体转简体
        System.out.println(HanLP.hk2s("hankcs在香港寫代碼"));
        // 香港繁体转台湾繁体
        System.out.println(HanLP.hk2tw("hankcs在臺灣寫代碼"));
        // 台湾繁体转香港繁体
        System.out.println(HanLP.tw2hk("hankcs在香港寫程式碼"));

        // 香港/台湾繁体和HanLP标准繁体的互转
        System.out.println(HanLP.t2tw("hankcs在臺灣寫代碼"));
        System.out.println(HanLP.t2hk("hankcs在臺灣寫代碼"));

        System.out.println(HanLP.tw2t("hankcs在臺灣寫程式碼"));
        System.out.println(HanLP.hk2t("hankcs在台灣寫代碼"));
    }

}
