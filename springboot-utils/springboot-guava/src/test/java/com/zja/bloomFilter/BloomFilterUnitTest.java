/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 11:27
 * @Since:
 */
package com.zja.bloomFilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: zhengja
 * @since: 2023/11/07 11:27
 */
public class BloomFilterUnitTest {

    @Test
    public void testBloomFilter() {
        // 创建一个布隆过滤器，期望插入100个元素，期望误判率为0.01
        BloomFilter<String> bloomFilter = BloomFilter.create(
                Funnels.unencodedCharsFunnel(),
                100,
                0.01
        );

        // 向布隆过滤器中插入元素
        bloomFilter.put("apple");
        bloomFilter.put("banana");
        bloomFilter.put("orange");

        // 验证布隆过滤器中是否包含某个元素
        assertTrue(bloomFilter.mightContain("apple"));
        assertTrue(bloomFilter.mightContain("banana"));
        assertTrue(bloomFilter.mightContain("orange"));
        assertFalse(bloomFilter.mightContain("grape"));

        // 获取布隆过滤器的期望误判率
        double falsePositiveProbability = bloomFilter.expectedFpp();

        // 验证期望误判率为0.01
        assertEquals(0.01, falsePositiveProbability, 0.001);
    }
}
