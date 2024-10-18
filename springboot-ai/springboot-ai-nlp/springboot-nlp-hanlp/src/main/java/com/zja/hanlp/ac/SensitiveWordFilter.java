package com.zja.hanlp.ac;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: zhengja
 * @Date: 2024-10-18 10:25
 */
public class SensitiveWordFilter {
    private final AhoCorasickDoubleArrayTrie<String> acdat;
    private final Set<String> sensitiveWordsSet;

    public SensitiveWordFilter() {
        acdat = new AhoCorasickDoubleArrayTrie<>();
        sensitiveWordsSet = new HashSet<>();
    }

    public SensitiveWordFilter(List<String> sensitiveWords) {
        acdat = new AhoCorasickDoubleArrayTrie<>();
        sensitiveWordsSet = new HashSet<>();

        addSensitiveWord(sensitiveWords);
    }

    public SensitiveWordFilter(String[] sensitiveWords) {
        acdat = new AhoCorasickDoubleArrayTrie<>();
        sensitiveWordsSet = new HashSet<>();

        addSensitiveWord(sensitiveWords);
    }

    /**
     * 重新构建 AhoCorasickDoubleArrayTrie
     */
    private void buildTrie() {
        // 将敏感词集合转换为数组并按长度降序排序
        String[] sortedSensitiveWords = sensitiveWordsSet.toArray(new String[0]);
        Arrays.sort(sortedSensitiveWords, Comparator.comparingInt(String::length).reversed());

        Map<String, String> wordMap = new HashMap<>(sensitiveWordsSet.size());
        for (String word : sortedSensitiveWords) {
            wordMap.put(word, word);
        }

        acdat.build(wordMap);
    }

    /**
     * 检测文本中是否包含敏感词
     *
     * @param text 待检测的文本
     * @return 是否包含敏感词
     */
    public boolean containsSensitiveWord(String text) {
        final AtomicInteger count = new AtomicInteger(0);
        acdat.parseText(text.toCharArray(), (begin, end, value, hit) -> {
            count.incrementAndGet();
        });
        return count.get() > 0;
    }

    /**
     * 获取文本中包含的敏感词列表
     *
     * @param text 待检测的文本
     * @return 敏感词列表
     */
    public Set<String> getSensitiveWords(String text) {
        Set<String> foundSensitiveWords = new HashSet<>();

        acdat.parseText(text.toCharArray(), (begin, end, value, hit) -> {
            String sensitiveWord = text.substring(begin, end);
            foundSensitiveWords.add(sensitiveWord);
        });

        return foundSensitiveWords;
    }

    /**
     * 获取敏感词集合
     *
     * @return 敏感词集合
     */
    public Set<String> getSensitiveWords() {
        return sensitiveWordsSet;
    }

    /**
     * 添加敏感词
     *
     * @param word 要添加的敏感词
     */
    public void addSensitiveWord(String word) {
        sensitiveWordsSet.add(word);
        buildTrie();
    }

    /**
     * 添加多个敏感词（数组形式）
     *
     * @param words 要添加的敏感词数组
     */
    public void addSensitiveWord(String[] words) {
        Collections.addAll(sensitiveWordsSet, words);
        buildTrie();
    }

    /**
     * 添加多个敏感词（列表形式）
     *
     * @param words 要添加的敏感词列表
     */
    public void addSensitiveWord(List<String> words) {
        sensitiveWordsSet.addAll(words);
        buildTrie();
    }

    /**
     * 移除敏感词
     *
     * @param word 要移除的敏感词
     */
    public void removeSensitiveWord(String word) {
        sensitiveWordsSet.remove(word);
        buildTrie();
    }

    /**
     * 移除多个敏感词（列表形式）
     *
     * @param words 要移除的敏感词列表
     */
    public void removeSensitiveWord(List<String> words) {
        words.forEach(sensitiveWordsSet::remove);
        buildTrie();
    }

    /**
     * 替换文本中的敏感词
     *
     * @param text        待处理的文本
     * @param replacement 替换字符
     * @return 替换后的文本
     */
    public String replaceSensitiveWords(String text, char replacement) {
        StringBuilder sb = new StringBuilder();
        int[] lastMatchEnd = {0}; // 使用数组来封装 lastMatchEnd
        Set<Integer> processedPositions = new HashSet<>(); // 记录已处理的位置
        TreeMap<Integer, Integer> matches = new TreeMap<>(); // 记录匹配的开始和结束位置

        acdat.parseText(text.toCharArray(), (begin, end, value, hit) -> {
            matches.put(begin, end);
        });

        for (Map.Entry<Integer, Integer> entry : matches.entrySet()) {
            int begin = entry.getKey();
            int end = entry.getValue();

            if (lastMatchEnd[0] > begin) {
                lastMatchEnd[0] = begin; // 确保 lastMatchEnd[0] 不大于 begin
            }
            sb.append(text, lastMatchEnd[0], begin);

            for (int i = begin; i < end; i++) {
                if (!processedPositions.contains(i)) {
                    sb.append(replacement);
                    processedPositions.add(i);
                } else {
                    // 如果已经处理过，直接跳过
                    continue;
                }
            }
            lastMatchEnd[0] = end;
        }

        sb.append(text.substring(lastMatchEnd[0]));
        return sb.toString();
    }


    public static void main(String[] args) {
        // 定义敏感词列表
        String[] sensitiveWords = {"敏感", "敏感词", "敏感词1", "敏感词2", "敏感词3", "新敏感词"};

        // 创建敏感词过滤器
        SensitiveWordFilter filter = new SensitiveWordFilter(sensitiveWords);

        // 测试文本
        String text = "敏感词测试文本，这是一个包含敏感词1的测试文本，其中还有敏感词2，这是一个包含新敏感词的测试文本。敏感";

        // 检测敏感词
        boolean containsSensitiveWord = filter.containsSensitiveWord(text);
        System.out.println("文本中是否包含敏感词: " + containsSensitiveWord);

        // 替换敏感词
        String replacedText = filter.replaceSensitiveWords(text, '*');
        System.out.println("替换后的文本: " + replacedText);

        // 获取文本中包含的敏感词列表
        Set<String> foundSensitiveWords = filter.getSensitiveWords(text);
        System.out.println("文本中包含的敏感词列表: " + foundSensitiveWords);

        filter.addSensitiveWord("敏感词3");

        // 再次替换敏感词
        replacedText = filter.replaceSensitiveWords(text, '*');
        System.out.println("添加新敏感词后，替换后的文本: " + replacedText);

        // 再次获取文本中包含的敏感词列表
        foundSensitiveWords = filter.getSensitiveWords(text);
        System.out.println("添加新敏感词后，文本中包含的敏感词列表: " + foundSensitiveWords);

        // 移除多个敏感词（列表形式）
        filter.removeSensitiveWord(Arrays.asList("敏感词1", "新敏感词2"));

        // 再次替换敏感词
        replacedText = filter.replaceSensitiveWords(text, '*');
        System.out.println("移除敏感词1和新敏感词2后，替换后的文本: " + replacedText);

        // 再次获取文本中包含的敏感词列表
        foundSensitiveWords = filter.getSensitiveWords(text);
        System.out.println("移除敏感词1和新敏感词2后，文本中包含的敏感词列表: " + foundSensitiveWords);
    }
}