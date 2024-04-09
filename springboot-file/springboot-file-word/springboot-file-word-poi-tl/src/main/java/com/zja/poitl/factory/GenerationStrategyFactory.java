package com.zja.poitl.factory;

/**
 * @author: zhengja
 * @since: 2024/04/02 10:03
 */
// 生成策略工厂
public class GenerationStrategyFactory {
    public static GenerationStrategy createGenerationStrategy(String fileType) {
        switch (fileType) {
            case "产品":
                return new WordGenerationStrategy();
            case "衢州":
                return new QuzhouWordGenerationStrategy();
            default:
                throw new IllegalArgumentException("Invalid file type: " + fileType);
        }
    }
}