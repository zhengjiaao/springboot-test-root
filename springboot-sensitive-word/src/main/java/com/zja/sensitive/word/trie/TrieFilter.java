package com.zja.sensitive.word.trie;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: zhengja
 * @Date: 2024-10-15 14:19
 */
public class TrieFilter {
    private TrieNode root = new TrieNode();

    public void addWord(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new TrieNode());
        }
        current.isEndOfWord = true;
    }

    public boolean containsWord(String text) {
        for (int i = 0; i < text.length(); i++) {
            TrieNode current = root;
            for (int j = i; j < text.length(); j++) {
                char c = text.charAt(j);
                if (current.children.containsKey(c)) {
                    current = current.children.get(c);
                    if (current.isEndOfWord) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
    }

    public String filterWords(String text) {
        StringBuilder filteredText = new StringBuilder();
        StringBuilder currentWord = new StringBuilder();

        TrieNode current = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (current.children.containsKey(c)) {
                current = current.children.get(c);
                currentWord.append(c);
                if (current.isEndOfWord) {
                    filteredText.append(StringUtils.repeat("*", currentWord.length()));
                    current = root;
                    currentWord.setLength(0);
                }
            } else {
                current = root;
                if (currentWord.length() > 0) {
                    filteredText.append(currentWord);
                    currentWord.setLength(0);
                }
                filteredText.append(c);
            }
        }

        if (currentWord.length() > 0) {
            filteredText.append(currentWord);
        }

        return filteredText.toString();
    }

    public static void main(String[] args) {
        TrieFilter filter = new TrieFilter();
        filter.addWord("敏感词1");
        filter.addWord("敏感词2");

        String text = "这是一个包含敏感词1的文本，敏感词2也在其中";
        boolean containsSensitiveWord = filter.containsWord(text);
        String filteredText = filter.filterWords(text);

        System.out.println("文本中是否包含敏感词: " + containsSensitiveWord);
        System.out.println("过滤后的文本: " + filteredText);
    }
}