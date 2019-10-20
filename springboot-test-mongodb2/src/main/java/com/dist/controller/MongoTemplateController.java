package com.dist.controller;

import com.dist.dto.ShopDTO;
import com.dist.dto.UserDTO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** mongoTemplate 文档数据操作
 * @author zhengja@dist.com.cn
 * @data 2019/9/2 14:13
 */
@RestController
@RequestMapping(value = "template/mongo")
@Api(tags = {"MongoTemplateController"},description = "MongoTemplate-文档数据操作-双数据源测试")
public class MongoTemplateController {

    @Autowired
    @Qualifier(value = "firstMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Autowired
    @Qualifier(value = "secondMongoTemplate")
    private MongoTemplate mongoTemplate2;

    /*************************************** 数据源1 的测试****************************************/

    @ApiOperation(value = "保存文档数据到test1Mongo",notes = "数据源1",httpMethod = "GET")
    @RequestMapping(value = "save/document1",method = RequestMethod.GET)
    public void saveDocument1(){
        UserDTO user = new UserDTO();
        user.setId("1");
        user.setUsername("数据源1");

        ShopDTO shopDTO = new ShopDTO("1","数据源1",23);
        ShopDTO shopDTO2 = new ShopDTO("2","数据源1",23);
        ShopDTO shopDTO3 = new ShopDTO("3","数据源1",23);
        ShopDTO shopDTO4 = new ShopDTO("4","数据源1",23);
        mongoTemplate.save(shopDTO,"shops");
        mongoTemplate.save(shopDTO2,"shops");
        mongoTemplate.save(shopDTO3,"shops");
        mongoTemplate.save(shopDTO4,"shops");

        mongoTemplate.save(user);
    }

    @ApiOperation(value = "单条件查询获取文档数据-test1Mongo",notes = "数据源1",httpMethod = "GET")
    @RequestMapping(value = "get/document1/id",method = RequestMethod.GET)
    public Object getDocument1(){
        //id 可以改成其它字段做条件查询
        Query query = new Query(Criteria.where("id").is("1"));
        List<UserDTO> users = mongoTemplate.find(query, UserDTO.class);
        List<ShopDTO> shops = mongoTemplate.find(query, ShopDTO.class, "shops");

        Map map = new HashMap();
        map.put("users",users);
        map.put("shops",shops);
        return map;
    }

    @ApiOperation(value = "获取集合中所有文档数据-test1Mongo",notes = "获取users的所有文档的方式",httpMethod = "GET")
    @RequestMapping(value = "get/all/document1",method = RequestMethod.GET)
    public Object getAllDocument1(){
        List<UserDTO> userDTOS = mongoTemplate.findAll(UserDTO.class);
        List<ShopDTO> shopDTOS = mongoTemplate.findAll(ShopDTO.class, "shops");

        //如果集合名不存在，会在第一次插入文档时创建
        MongoCollection<Document> shops = mongoTemplate.getCollection("shops");

        Map map = new HashMap();
        map.put("userDTOS",userDTOS);
        map.put("shopDTOS",shopDTOS);
        return map;
    }

    /*************************************** 数据源2 的测试****************************************/

    @ApiOperation(value = "保存文档数据到test2Mongo",notes = "数据源2",httpMethod = "GET")
    @RequestMapping(value = "save/document2",method = RequestMethod.GET)
    public void saveDocument2(){
        UserDTO user = new UserDTO();
        user.setId("1");
        user.setUsername("数据源2");
        user.setAge(24);
        ShopDTO shopDTO = new ShopDTO("1","数据源2",21);
        mongoTemplate2.save(shopDTO,"shops");
        mongoTemplate2.save(user);
    }

    @ApiOperation(value = "单条件查询获取文档数据-test2Mongo-users",notes = "数据源2",httpMethod = "GET")
    @RequestMapping(value = "get/document2/id",method = RequestMethod.GET)
    public Object getDocument2(){
        //id 可以改成其它字段做条件查询
        Query query = new Query(Criteria.where("_id").is("1"));
        List<UserDTO> users = mongoTemplate2.find(query, UserDTO.class);
        List<ShopDTO> shops = mongoTemplate2.find(query, ShopDTO.class, "shops");

        Map map = new HashMap();
        map.put("users",users);
        map.put("shops",shops);
        return map;
    }

    @ApiOperation(value = "获取集合中所有文档数据-test2Mongo-users",notes = "获取users的所有文档的方式",httpMethod = "GET")
    @RequestMapping(value = "get/all/document2",method = RequestMethod.GET)
    public Object getAllDocument2(){
        List<UserDTO> userDTOS = mongoTemplate2.findAll(UserDTO.class);
        List<ShopDTO> shopDTOS = mongoTemplate2.findAll(ShopDTO.class, "shops");
        //操作集合对象
        MongoCollection<Document> shops = mongoTemplate2.getCollection("shops");

        Map map = new HashMap();
        map.put("userDTOS",userDTOS);
        map.put("shopDTOS",shopDTOS);
        return map;
    }


    /*********** 以下演示是以 数据源1(test1Mongo) 操作(shops) ==== 数据源2是一样的,这里不重复演示  *************/


    @ApiOperation(value = "复合条件查询,排序-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/conditionalQuery",method = RequestMethod.GET)
    public Object conditionalQuery(){

        Criteria criteria = new Criteria();
        criteria.orOperator(
                Criteria.where("name").is("数据源1"),
                Criteria.where("age").is(23));
        Query query = new Query(criteria);   //组合查询放入query
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC,"id"));  //结果集进行排序
        query.with(sort);
        List<ShopDTO> shopDTOS = mongoTemplate.find(query,ShopDTO.class,"shops");
        //mongoDB组合查询数据成功,集合为shops,文档为：shopDTOS
        return shopDTOS;
    }

    @ApiOperation(value = "分页条件查询-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/pageQuery",method = RequestMethod.GET)
    public Object pageQuery(@ApiParam(value = "skip",defaultValue = "1") @RequestParam Integer skip,
                            @ApiParam(value = "limit",defaultValue = "3") @RequestParam Integer limit){

        Query query = new Query();
        //mongoDB分页查询下标为1开始总共3行数据
        query.skip(skip).limit(limit); //// 从那条记录开始， 取多少条记录
        List<ShopDTO> shopDTOS = mongoTemplate.find(query,ShopDTO.class,"shops");
        //mongoDB分页查询数据成功,集合为shops,文档为：shopDTOS
        return shopDTOS;
    }

    @ApiOperation(value = "模糊(regex)查询-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/regexQuery",method = RequestMethod.GET)
    public Object regexQuery(){
        //模糊查询 regex，满足条件直接更新
        Query query = new Query(Criteria.where("name").regex("数据"));
        List<ShopDTO> shopDTOS = mongoTemplate.find(query,ShopDTO.class,"shops");
        for (ShopDTO shopDO : shopDTOS){
            System.out.println(shopDO.getId()+"/ "+shopDO.getName()+"/ "+shopDO.getAge());
        }
        Update update = new Update();
        update.set("name","数据源1-更新");
        update.set("age",20);
        UpdateResult wr = mongoTemplate.updateMulti(query,update,"shops");
        return wr;
    }

    @ApiOperation(value = "更新文档-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/updaateDocument",method = RequestMethod.GET)
    public Object updaateDocument1(@ApiParam(value = "id",defaultValue = "1") @RequestParam String id){
        //记住一定要是 "_id"
        Query query = new Query(Criteria.where("_id").is(id));
        List<ShopDTO> list2 = mongoTemplate.find(query,ShopDTO.class,"shops");
        System.out.println("mongoDB查询主键为1数据成功,集合为shops,文档为：");
        for (ShopDTO shopDO:list2){
            System.out.println(shopDO.getId()+"/ "+shopDO.getName()+"/ "+shopDO.getAge());
        }
        Update update = new Update();
        update.set("name","数据源1-根据id更新");
        update.set("age",18);
        UpdateResult wr = mongoTemplate.updateFirst(query,update,"shops");
        System.out.println("mongoDB更新数据成功,集合为shops,行数为：" + wr.getModifiedCount());
        return wr;
    }

    @ApiOperation(value = "根据_id删除文档-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/deleteDocument",method = RequestMethod.GET)
    public Object deleteDocument1(@ApiParam(value = "_id",defaultValue = "1") @RequestParam String id){

        //记住一定要是 "_id"
        Query query = new Query(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, "shops");
        System.out.println("mongoDB删除数据成功,集合为shops,行数为：" + result.getDeletedCount() + "删除的ID为"+id);
        return result;
    }

    @ApiOperation(value = "删除所有文档-test1Mongo-shops",httpMethod = "GET")
    @RequestMapping(value = "v1/deleteAllDocument",method = RequestMethod.GET)
    public Object deleteAllDocument1(){
        List<UserDTO> andRemove = mongoTemplate.remove(UserDTO.class).findAndRemove();
        return andRemove;
    }



}
