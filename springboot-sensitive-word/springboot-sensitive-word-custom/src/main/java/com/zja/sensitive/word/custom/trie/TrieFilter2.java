package com.zja.sensitive.word.custom.trie;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: zhengja
 * @Date: 2024-10-15 13:50
 */
public class TrieFilter2 {
    private TrieNode2 root = new TrieNode2('/');
    private Set<String> whiteList = new HashSet<>();
    private Set<String> blackList = new HashSet<>();

    public void addSensitiveWord(String word, boolean isBlackList) {
        TrieNode2 p = root;
        for (char c : word.toCharArray()) {
            if (!p.children.containsKey(c)) {
                p.children.put(c, new TrieNode2(c));
            }
            p = p.children.get(c);
        }
        p.isEndingChar = true;

        if (isBlackList) {
            blackList.add(word);
        } else {
            whiteList.add(word);
        }
    }

    public String filter(String text) {
        StringBuilder filteredText = new StringBuilder();
        int start = 0;
        int position = 0;
        TrieNode2 p = root;

        while (position < text.length()) {
            char c = text.charAt(position);
            if (p.children.containsKey(c)) {
                p = p.children.get(c);
                if (p.isEndingChar) {
                    if (whiteList.contains(text.substring(start, position + 1))) {
                        filteredText.append(text, start, position + 1);
                    } else if (blackList.contains(text.substring(start, position + 1))) {
                        // filteredText.append("*".repeat(position - start + 1));
                        filteredText.append(StringUtils.repeat("*", position - start + 1));
                    } else {
                        filteredText.append(text, start, position + 1);
                    }
                    start = position + 1;
                    p = root;
                }
                position++;
            } else {
                filteredText.append(text.charAt(start));
                start++;
                position = start;
                p = root;
            }
        }
        if (start < text.length()) {
            filteredText.append(text, start, text.length());
        }

        return filteredText.toString();
    }

    public static void main(String[] args) {
        TrieFilter2 filter = new TrieFilter2();
        filter.addSensitiveWord("敏感词1", true); // 添加至黑名单
        filter.addSensitiveWord("敏感词2", false); // 添加至白名单

        String text = "这是一个包含敏感词1的文本，敏感词2也在其中";
        String filteredText = filter.filter(text);
        System.out.println(filteredText);
    }
}
