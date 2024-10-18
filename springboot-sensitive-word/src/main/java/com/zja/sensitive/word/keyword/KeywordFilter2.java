package com.zja.sensitive.word.keyword;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: zhengja
 * @Date: 2024-10-15 14:27
 */
public class KeywordFilter2 {

    private Set<String> whiteList = new HashSet<>();
    private Set<String> blackList = new HashSet<>();

    public void addKeyword(String keyword, boolean isBlackList) {
        if (isBlackList) {
            blackList.add(keyword);
        } else {
            whiteList.add(keyword);
        }
    }

    public String filter(String text) {
        StringBuilder filteredText = new StringBuilder();
        StringBuilder currentWord = new StringBuilder();
        int i = 0;

        while (i < text.length()) {
            char c = text.charAt(i);
            currentWord.append(c);

            // 检查是否匹配白名单
            if (whiteList.contains(currentWord.toString())) {
                filteredText.append(currentWord);
                currentWord.setLength(0);
            } else {
                // 检查是否匹配黑名单
                if (blackList.contains(currentWord.toString())) {
                    filteredText.append(StringUtils.repeat("*", currentWord.length()));
                    currentWord.setLength(0);
                    i += currentWord.length();
                } else {
                    // 如果当前子串不在黑名单中，则继续向后查找
                    boolean found = false;
                    for (int j = i + 1; j < text.length(); j++) {
                        currentWord.append(text.charAt(j));
                        if (blackList.contains(currentWord.toString())) {
                            filteredText.append(StringUtils.repeat("*", currentWord.length()));
                            currentWord.setLength(0);
                            i = j;
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        filteredText.append(c);
                        currentWord.setLength(0);
                    }
                }
            }
            i++;
        }

        if (currentWord.length() > 0) {
            filteredText.append(currentWord);
        }

        return filteredText.toString();
    }

    public static void main(String[] args) {
        KeywordFilter2 filter = new KeywordFilter2();
        filter.addKeyword("敏感词1", true); // 添加至黑名单
        filter.addKeyword("敏感词2", false); // 添加至白名单

        String text = "这是一个包含敏感词1的文本，敏感词2也在其中";
        String filteredText = filter.filter(text);

        System.out.println("过滤后的文本: " + filteredText);
    }
}
