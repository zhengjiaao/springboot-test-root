package com.zja.spring;

import com.zja.dto.Pes;
import com.zja.dto.TestDO;
import com.zja.dto.TestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

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
        testDTO.setCreateDate("2009");
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
        BeanUtils.copyProperties(testDTO, testDO); // spring BeanUtils

        // 输出对比
        System.out.println(testDTO);
        System.out.println(testDO);

        // 总结：
        // 字段名不一致，属性无法复制
        // 类型不一致，属性无法复制，但是注意，如果类型为基本类型以及基本类型的包装类，这种可以转化
        // 嵌套对象字段，将会与源对象使用同一对象，即使用浅拷贝
    }
}
