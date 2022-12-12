package com.zja.repositorys;

import com.zja.entity.node.CarNode;
import com.zja.entity.node.CustomerNode;
import com.zja.entity.node.PhoneNode;
import com.zja.entity.relationship.HaveRelationship;
import com.zja.repositorys.node.CarNodeRepository;
import com.zja.repositorys.node.CustomerNodeRepository;
import com.zja.repositorys.node.PhoneNodeRepository;
import com.zja.repositorys.relationship.HaveRelationshipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:17
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：自定义构建动态节点类型
 * HaveDynamic实现支持任何基于BaseNode的开始和结束节点类型，实现动态的构建节点和关系模型，对于同一关系类型，只需要编写一类关系构建的代码即可。
 *
 * 提示： 以上动态起始节点类型的实现比较适合OLTP的系统上存在复杂的节点和关系需要做CURD的时候，能够改善整个模型工程的复杂度。但是，在做类似ETL大批量数据插入的场景是不合适的，如果存在大批量数据插入来完成建模的情况，建议优先使用cypher schema来处理，并通过cypher-shell来完成导入，性能会更佳。
 */
@SpringBootTest
public class HaveRelationshipRepositoryTests {

    @Autowired
    private HaveRelationshipRepository haveRelationshipRepository;

    @Autowired
    PhoneNodeRepository phoneNodeRepsitory;
    @Autowired
    CustomerNodeRepository customerNodeRepository;
    @Autowired
    CarNodeRepository carNodeRepository;


    /**
     * 通过关系创建【节点--关系--节点】
     */
    @Test
    public void save_test() throws Exception {
        CustomerNode customerNode = new CustomerNode();
        customerNode.setCustomerName("李四");
        customerNode.setType(2);
        CarNode carNode = new CarNode();
        carNode.setCarBrand("奔驰");

        HaveRelationship<CustomerNode, CarNode> haveRelationship = new HaveRelationship<>();
        haveRelationship.setStartNode(customerNode);
        haveRelationship.setEndNode(carNode);
        //添加关系属性
        haveRelationship.setRelName("关系名称");
        haveRelationship.setRelType(3);
        haveRelationship.setCreateTime(LocalDateTime.now().toString());

        haveRelationshipRepository.save(haveRelationship);
    }

    @Test
    public void findAll_test() throws Exception {
        Iterable<HaveRelationship> iterable = haveRelationshipRepository.findAll();
        System.out.println(iterable);
    }

    @Test
    public void queryByCarBrand_test() throws Exception {
        List<HaveRelationship> list = haveRelationshipRepository.queryByCarBrand("奔驰");
        System.out.println(list);
    }

    @Test
    public void findAllById_test() throws Exception {
        Optional<HaveRelationship> optional = haveRelationshipRepository.findById(0L);
        System.out.println(optional.get());
    }

    @Test
    public void findByRelName_test() throws Exception {
        List<HaveRelationship> list = haveRelationshipRepository.findByRelName("关系名称");
        System.out.println(list);
    }

    @Test
    public void findByRelType_test() throws Exception {
        List<HaveRelationship> list = haveRelationshipRepository.findByRelType(3);
        System.out.println(list);
    }

    //----------------------------------------------------------------------------------------------

    /**
     * 根据已存在的节点【CustomerNode、PhoneNode、CarNode】进行创建关联关系
     */
    @Test
    public void save_rel_test() throws Exception {
        //先清除所有关系
        customerNodeRepository.deleteAll();
        phoneNodeRepsitory.deleteAll();
        carNodeRepository.deleteAll();
        haveRelationshipRepository.deleteAll();

        CustomerNode customerNode = new CustomerNode();
        customerNode.setCustomerName("李四");
        customerNode.setType(2);
        customerNodeRepository.save(customerNode);

        PhoneNode phoneNode = new PhoneNode();
        phoneNode.setName("小米13");
        phoneNode.setBrand("小米手机");
        phoneNodeRepsitory.save(phoneNode);

        CarNode carNode = new CarNode();
        carNode.setCarBrand("奔驰");
        carNodeRepository.save(carNode);

        CustomerNode customer = customerNodeRepository.findByCustomerName(customerNode.getCustomerName());
        PhoneNode phone = phoneNodeRepsitory.findByName("小米13");

        HaveRelationship<CustomerNode, PhoneNode> haveRelationship = new HaveRelationship<>();
        haveRelationship.setStartNode(customer);
        haveRelationship.setEndNode(phone);
        //添加关系属性
        haveRelationship.setRelName("拥有");
        haveRelationship.setRelType(3);
        haveRelationship.setCreateTime(LocalDateTime.now().toString());

        haveRelationshipRepository.save(haveRelationship);
        HaveRelationship<CustomerNode, CarNode> haveRelationship1 = new HaveRelationship<>();
        haveRelationship1.setStartNode(customer);
        haveRelationship1.setEndNode(carNode);
        //添加关系属性
        haveRelationship1.setRelName("拥有");
        haveRelationship1.setRelType(3);
        haveRelationship1.setCreateTime(LocalDateTime.now().toString());
        haveRelationshipRepository.save(haveRelationship1);
    }

    /**
     * 根据指定节点查询所有存在关系的节点
     */
    @Test
    public void queryAllByCustomerName_test() throws Exception {
        List<HaveRelationship> list = haveRelationshipRepository.queryAllByCustomerName("李四");
        System.out.println(list);
    }

    /**
     * 根据指定节点查询某一个类型存在关系的节点
     */
    @Test
    public void queryCarNodeAllByCustomerName_test() throws Exception {
        List<HaveRelationship> list = haveRelationshipRepository.queryCarNodeAllByCustomerName("李四");
        System.out.println(list);
    }

}
