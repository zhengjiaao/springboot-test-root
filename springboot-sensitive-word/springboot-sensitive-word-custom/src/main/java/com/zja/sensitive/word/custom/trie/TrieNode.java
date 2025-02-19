package com.zja.sensitive.word.custom.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-10-15 14:18
 */
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;
}