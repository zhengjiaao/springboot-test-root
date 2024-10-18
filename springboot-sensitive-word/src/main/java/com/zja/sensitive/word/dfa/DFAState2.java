package com.zja.sensitive.word.dfa;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-10-15 14:02
 */
public class DFAState2 {
    char data;
    boolean isEndingChar = false;
    int length = 0; // 记录从根节点到当前节点的路径长度
    boolean isBlackList = false;
    boolean isWhiteList = false;
    Map<Character, DFAState2> children = new HashMap<>();

    public DFAState2(char data) {
        this.data = data;
    }

    public void setLength(int length) {
        this.length = length;
    }
}