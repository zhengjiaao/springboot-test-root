package com.zja.sensitive.word.custom.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-10-15 13:50
 */
class TrieNode2 {
    char data;
    boolean isEndingChar = false;
    Map<Character, TrieNode2> children = new HashMap<>();

    public TrieNode2(char data) {
        this.data = data;
    }
}