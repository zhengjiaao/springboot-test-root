package com.zja.sensitive.word.custom.dfa;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-10-15 13:47
 */
class DFAState {
    char data;
    boolean isEndingChar = false;
    Map<Character, DFAState> children = new HashMap<>();

    public DFAState(char data) {
        this.data = data;
    }
}