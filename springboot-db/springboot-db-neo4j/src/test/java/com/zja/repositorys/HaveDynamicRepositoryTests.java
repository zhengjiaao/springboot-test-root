package com.zja.repositorys;

import com.zja.entity.Car;
import com.zja.entity.Customer;
import com.zja.entity.HaveDynamic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:17
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：自定义构建动态节点类型
 */
@SpringBootTest
public class HaveDynamicRepositoryTests {

    @Autowired
    private HaveDynamicRepository haveDynamicRepository;

    /**
     * HaveDynamic实现支持任何基于BaseNode的开始和结束节点类型，实现动态的构建节点和关系模型，对于同一关系类型，只需要编写一类关系构建的代码即可。
     *
     * 提示： 以上动态起始节点类型的实现比较适合OLTP的系统上存在复杂的节点和关系需要做CURD的时候，能够改善整个模型工程的复杂度。但是，在做类似ETL大批量数据插入的场景是不合适的，如果存在大批量数据插入来完成建模的情况，建议优先使用cypher schema来处理，并通过cypher-shell来完成导入，性能会更佳。
     */
    @Test
    public void contextLoads() throws Exception {
        Customer customer = new Customer();
        customer.setName("John");
        Car car = new Car();
        car.setBrand("Benz");

        HaveDynamic<Customer, Car> haveDynamic = new HaveDynamic<>();
        haveDynamic.setCreateTime(LocalDateTime.now().toString());
        haveDynamic.setStartNode(customer);
        haveDynamic.setEndNode(car);

        haveDynamicRepository.save(haveDynamic);
    }

}
