package com.zja.hutool;

import cn.hutool.core.bean.BeanUtil;
import com.zja.dto.Pes;
import com.zja.dto.TestDO;
import com.zja.dto.TestDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/11/27 17:22
 */
public class BeanUtilTest {

    @Test
    public void test() {
        TestDTO testDTO = new TestDTO();
        testDTO.setAge(1);
        testDTO.setCreateDate("Fri May 19 15:59:09 CST 2023");
        testDTO.setUpdateDate(new Date());
        testDTO.setName("tom");
        testDTO.setNo("1001");
        testDTO.setSubjects(Arrays.asList("a", "b"));
        Pes pes = new Pes();
        pes.setName("people");
        testDTO.setPeople(pes);

        List<Pes> p = new ArrayList<>();
        Pes pes1 = new Pes();
        pes1.setName("people1");
        Pes pes2 = new Pes();
        pes2.setName("people2");
        p.add(pes1);
        p.add(pes2);
        testDTO.setPes(p);

        TestDO testDO = new TestDO();
        BeanUtil.copyProperties(testDTO, testDO); // hutool BeanUtil

        // 输出对比
        System.out.println(testDTO);
        System.out.println(testDO);

        // 总结：
        // hutool 可以copy不同类型的字段，只要字段的名称相同，对于字符串copy为date需要特定的格式
        // copy对象也是浅拷贝
    }
}
