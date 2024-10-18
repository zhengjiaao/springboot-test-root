package com.zja.sensitive.word.keyword;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: zhengja
 * @Date: 2024-10-15 13:32
 */
public class KeywordFilter {

    private Set<String> sensitiveWords;

    public KeywordFilter() {
        this.sensitiveWords = new HashSet<>();
        sensitiveWords.add("敏感词1");
        sensitiveWords.add("敏感词2");
        sensitiveWords.add("敏感词3");
    }

    public String filterSensitiveWords(String text) {
        String filteredText = text;
        for (String word : sensitiveWords) {
            if (filteredText.contains(word)) {
                filteredText = filteredText.replace(word, StringUtils.repeat("*", word.length())); // 将敏感词替换为*
            }
        }
        return filteredText;
    }

    public static void main(String[] args) {
        KeywordFilter filter = new KeywordFilter();
        String text = "这是一个包含敏感词1的文本，敏感词2也在其中";
        String filteredText = filter.filterSensitiveWords(text);
        System.out.println(filteredText);
    }
}
