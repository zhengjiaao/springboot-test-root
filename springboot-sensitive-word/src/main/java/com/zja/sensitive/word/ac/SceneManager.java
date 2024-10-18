package com.zja.sensitive.word.ac;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-10-18 16:29
 */
@Slf4j
@Component
public class SceneManager {
    private final Map<String, SensitiveWordFilter> sceneFilters = new HashMap<>();

    /**
     * 获取一个场景的敏感词过滤器，不存在时，则创建一个过滤器
     *
     * @param sceneName 场景名称
     * @return SensitiveWordFilter 对象
     */
    public synchronized SensitiveWordFilter getSceneFilter(String sceneName) {
        SensitiveWordFilter filter = sceneFilters.get(sceneName);
        if (filter == null) {
            filter = new SensitiveWordFilter();
            sceneFilters.put(sceneName, filter);
        }
        return filter;
    }

    public synchronized SensitiveWordFilter getSceneFilter(String wordBlackPath, String wordWhitePath) {
        String sceneName = (wordBlackPath + wordWhitePath).hashCode() + "";
        SensitiveWordFilter filter = sceneFilters.get(sceneName);
        if (filter == null) {
            filter = new SensitiveWordFilter();
            filter.addSensitiveWord(getWordsList(wordBlackPath, false));
            filter.removeSensitiveWord(getWordsList(wordWhitePath, true));
            sceneFilters.put(sceneName, filter);
        }
        return filter;
    }

    // 读取敏感词库，资源路径：resources/word_blacklist.txt
    private List<String> getWordsList(String wordPath, boolean isWhite) {
        List<String> wordList = new ArrayList<>();

        // 检查文件是否存在
        ClassPathResource resource = new ClassPathResource(wordPath);
        if (!resource.exists() && isWhite) {
            log.warn("白名单敏感词库文件不存在，跳过处理: " + wordPath);
            return wordList;
        }

        try (InputStream inputStream = resource.getInputStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        wordList.add(line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("读取敏感词库失败: " + e.getMessage(), e);
        }
        return wordList;
    }
}
