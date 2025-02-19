package com.zja.sensitive.word.custom.dfa;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: zhengja
 * @Date: 2024-10-15 14:02
 */
public class DFAFilter2 {
    private final DFAState2 root = new DFAState2('/');
    private final Set<String> whiteList = new HashSet<>();
    private final Set<String> blackList = new HashSet<>();

    public void addSensitiveWord(String word, boolean isBlackList) {
        DFAState2 p = root;
        int length = 0;
        for (char c : word.toCharArray()) {
            if (!p.children.containsKey(c)) {
                p.children.put(c, new DFAState2(c));
            }
            p = p.children.get(c);
            length++;
            p.setLength(length);
        }
        p.isEndingChar = true;

        if (isBlackList) {
            p.isBlackList = true;
            blackList.add(word);
        } else {
            p.isWhiteList = true;
            whiteList.add(word);
        }
    }

    public boolean containsSensitiveWord(String text) {
        DFAState2 p = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (p.children.containsKey(c)) {
                p = p.children.get(c);
                if (p.isEndingChar) {
                    int startPos = Math.max(0, i - p.length + 1);
                    String word = text.substring(startPos, i + 1);
                    if (p.isWhiteList && whiteList.contains(word)) {
                        return false;
                    } else if (p.isBlackList && blackList.contains(word)) {
                        return true;
                    }
                }
            } else {
                p = root;
            }
        }
        return false;
    }

    public String filter(String text) {
        StringBuilder filteredText = new StringBuilder();
        StringBuilder currentWord = new StringBuilder();
        DFAState2 p = root;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (p.children.containsKey(c)) {
                p = p.children.get(c);
                currentWord.append(c);
                if (p.isEndingChar) {
                    if (p.isWhiteList && whiteList.contains(currentWord.toString())) {
                        filteredText.append(currentWord);
                    } else if (p.isBlackList && blackList.contains(currentWord.toString())) {
                        filteredText.append(StringUtils.repeat("*", currentWord.length()));
                    } else {
                        filteredText.append(currentWord);
                    }
                    currentWord.setLength(0);
                    p = root;
                }
            } else {
                p = root;
                filteredText.append(currentWord).append(c);
                currentWord.setLength(0);
            }
        }

        if (currentWord.length() > 0) {
            filteredText.append(currentWord);
        }

        return filteredText.toString();
    }

    public static void main(String[] args) {
        DFAFilter2 filter = new DFAFilter2();
        filter.addSensitiveWord("敏感词1", true); // 添加至黑名单
        filter.addSensitiveWord("敏感词2", false); // 添加至白名单

        String text = "这是一个包含敏感词1的文本，敏感词2也在其中";
        boolean containsSensitiveWord = filter.containsSensitiveWord(text);
        String filteredText = filter.filter(text);

        System.out.println("文本中是否包含敏感词: " + containsSensitiveWord);
        System.out.println("过滤后的文本: " + filteredText);
    }
}