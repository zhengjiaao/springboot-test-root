package com.zja.sensitive.word.dfa;

/**
 * @Author: zhengja
 * @Date: 2024-10-15 13:47
 */
public class DFAFilter {
    private final DFAState root = new DFAState('/');

    public void insert(String word) {
        DFAState p = root;
        for (char c : word.toCharArray()) {
            if (!p.children.containsKey(c)) {
                p.children.put(c, new DFAState(c));
            }
            p = p.children.get(c);
        }
        p.isEndingChar = true;
    }

    public boolean filter(String text) {
        DFAState p = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (p.children.containsKey(c)) {
                p = p.children.get(c);
                if (p.isEndingChar) {
                    return true; // 敏感词匹配成功
                }
            } else {
                p = root;
            }
        }
        return false; // 未匹配到敏感词
    }

    public static void main(String[] args) {
        DFAFilter dfaFilter = new DFAFilter();
        dfaFilter.insert("敏感词1");
        dfaFilter.insert("敏感词2");
        dfaFilter.insert("敏感词3");

        String text = "这是一个包含敏感词1的文本，敏感词2也在其中";
        if (dfaFilter.filter(text)) {
            System.out.println("文本包含敏感词");
        } else {
            System.out.println("文本不包含敏感词");
        }
    }
}