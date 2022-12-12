/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-12 15:06
 * @Since:
 */
package com.zja.repositorys;

import com.zja.entity.BaseNode;
import com.zja.repositorys.node.BaseNodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BaseNodeRepositoryTests {

    @Autowired
    BaseNodeRepository baseNodeRepository;

    @Test
    public void findAll_test() throws Exception {
        Iterable<BaseNode> iterable = baseNodeRepository.findAll();
        System.out.println(iterable);
        //[CustomerNode(customerName=李四, type=2, test=null), PhoneNode(name=小米13, brand=小米手机), CarNode(carBrand=奔驰)]
    }

    @Test
    public void findByName_test() throws Exception {
        List<BaseNode> list = baseNodeRepository.findByName("小米13");
        System.out.println(list);
        //[PhoneNode(name=小米13, brand=小米手机)]
    }

}
