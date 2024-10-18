package com.zja.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.collection.trie.DoubleArrayTrie;
import com.hankcs.hanlp.corpus.dictionary.DictionaryMaker;
import com.hankcs.hanlp.corpus.dictionary.item.Item;
import com.hankcs.hanlp.corpus.io.IOUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Double Array Trie实现了一个性能极高的Aho Corasick自动机，应用于分词可以取得1400万字每秒，约合27MB/s的分词速度。
 * <p>
 * 参考：https://www.hankcs.com/program/algorithm/double-array-trie-vs-aho-corasick-double-array-trie.html
 *
 * @Author: zhengja
 * @Date: 2024-10-15 11:07
 */
@Deprecated // todo 待完善
public class DoubleArrayTrieTest {

    @Test
    public void _test() throws IOException {
        // 加载词典
        TreeMap<String, String> map = new TreeMap<String, String>();
        String[] keyArray = new String[]
                {
                        "hers",
                        "his",
                        "she",
                        "he"
                };
        for (String key : keyArray) {
            map.put(key, key);
        }

        // 加载母文本
        String text = IOUtil.readTxt("《我的团长我的团》.txt");
        // 构建DAT
        DoubleArrayTrie<String> dat = new DoubleArrayTrie<>();
        dat.build(map);

        dat.parseText("uhers", new AhoCorasickDoubleArrayTrie.IHit<String>() {
            @Override
            public void hit(int begin, int end, String value) {
                System.out.printf("[%d:%d]=%s\n", begin, end, value);
                // [1:3]=he
                // [1:5]=hers
            }
        });
        // 或者System.out.println(act.parseText("uhers")); // [[1:3]=he, [1:5]=hers]
    }
}
