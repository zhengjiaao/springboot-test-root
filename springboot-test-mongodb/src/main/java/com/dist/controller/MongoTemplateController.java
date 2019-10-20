package com.dist.controller;

import com.dist.dto.ShopDTO;
import com.dist.dto.UserDTO;
import com.mongodb.client.result.DeleteResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/6/19 9:59
 */
@Api(tags = {"MongoTemplateController"}, description = "Mongodb 文档数据操作")
@RestController
@RequestMapping(value = "rest/mongo")
public class MongoTemplateController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @ApiOperation(value = "保存-查询", notes = "保存到mongo",httpMethod = "POST")
    @RequestMapping(value = "v1/shops/save",method = RequestMethod.POST)
    public Object saveShop(@ApiParam(value = "具体参考：Model") @RequestBody ShopDTO shopDTO) {

        //shopDTO 要保存文档的数据 ，shops集合
        mongoTemplate.save(shopDTO, "shops");

        Map map = new HashMap();

        map.put("集合shops",mongoTemplate.getCollection("shops"));
        map.put("List<ShopDTO>",mongoTemplate.findAll(ShopDTO.class, "shops"));

        return map;
    }

    @ApiOperation(value = "根据id查询shops文档数据", notes = "查询shops文档数据",httpMethod = "GET")
    @RequestMapping(value = "v1/shops/getShop",method = RequestMethod.GET)
    public Object getShop(@ApiParam(value = "文档id") @RequestParam Integer id) {
        //根据条件查询 注意是 _id
        Query query = new Query(Criteria.where("_id").is(id));

        List<ShopDTO> shopDTOS = mongoTemplate.find(query, ShopDTO.class,"shops");

        return shopDTOS;
    }

    @ApiOperation(value = "查询所有shops文档数据", notes = "查询所有shops文档数据",httpMethod = "GET")
    @RequestMapping(value = "v1/shops/getAllShop",method = RequestMethod.GET)
    public Object getAllShop() {

        List<ShopDTO> shopDTOS = mongoTemplate.findAll(ShopDTO.class,"shops");

        return shopDTOS;
    }

    @ApiOperation(value = "删除单条shops文档数据", notes = "删除单条文档数据",httpMethod = "DELETE")
    @RequestMapping(value = "v1/shops/delete",method = RequestMethod.DELETE)
    public Object deleteShop(@ApiParam(value = "文档id") @RequestParam Integer id) {
        //根据条件查询 注意是 _id
        Query query = new Query(Criteria.where("_id").is(id));

        DeleteResult deleteResult = mongoTemplate.remove(query, "shops");

        Map map =new HashMap();
        map.put("wasAcknowledged",deleteResult.wasAcknowledged());
        map.put("getDeletedCount",deleteResult.getDeletedCount());

        return map;
    }

    @ApiOperation(value = "删除所有shops文档数据", notes = "删除所有文档数据",httpMethod = "DELETE")
    @RequestMapping(value = "v1/shops/deleteAllShop",method = RequestMethod.DELETE)
    public Object deleteAllShop() {

        Map map = new HashMap();
        List<ShopDTO> allShop = (List<ShopDTO>) getAllShop();
        int count = 0;
        for (ShopDTO shopDTO : allShop){
            Object o = deleteShop(shopDTO.getId());
            map.put("删除的第几条数据-"+count,o);
            count++;
        }
        return map;
    }

    /***************************以下是UserDTO 测试 和ShopDTO区别在于类上是否有这个注解 @Document(collection = "users") ********************************/

    @ApiOperation(value = "保存saveUser-查询", notes = "保存到mongo",httpMethod = "POST")
    @RequestMapping(value = "v2/users/saveUser",method = RequestMethod.POST)
    public Object saveUser(@ApiParam(value = "具体参考：Model") @RequestBody UserDTO userDTO) {

        //userDTO 要保存文档的数据 ，users集合    由于UserDTO类上有注解：@Document(collection = "users")
        mongoTemplate.save(userDTO);

        Map map = new HashMap();

        map.put("集合users",mongoTemplate.getCollection("users"));
        map.put("List<UserDTO>",mongoTemplate.findAll(UserDTO.class));

        return map;
    }

    @ApiOperation(value = "根据id查询users文档数据", notes = "查询users文档数据",httpMethod = "GET")
    @RequestMapping(value = "v2/users/getUser",method = RequestMethod.GET)
    public Object getUser(@ApiParam(value = "文档id") @RequestParam String id) {
        //根据条件查询 注意是 _id  ，也可以用name等其它字段做条件
        Query query = new Query(Criteria.where("_id").is(id));

        List<UserDTO> userDTOS = mongoTemplate.find(query, UserDTO.class);

        return userDTOS;
    }

    @ApiOperation(value = "查询所有users文档数据", notes = "查询所有users文档数据",httpMethod = "GET")
    @RequestMapping(value = "v2/users/getAllUser",method = RequestMethod.GET)
    public Object getAllUser() {

        List<UserDTO> userDTOS = mongoTemplate.findAll(UserDTO.class);

        return userDTOS;
    }

    @ApiOperation(value = "删除单条users文档数据", notes = "删除单条文档数据",httpMethod = "DELETE")
    @RequestMapping(value = "v2/users/deleteUser",method = RequestMethod.DELETE)
    public Object deleteUser(@ApiParam(value = "文档id") @RequestParam String id) {
        //根据条件查询 注意是 _id  ，也可以用name等其它字段做条件
        Query query = new Query(Criteria.where("_id").is(id));

        DeleteResult deleteResult = mongoTemplate.remove(query, UserDTO.class);

        Map map =new HashMap();
        map.put("wasAcknowledged",deleteResult.wasAcknowledged());
        map.put("getDeletedCount",deleteResult.getDeletedCount());

        return map;
    }

    @ApiOperation(value = "删除所有users文档数据", notes = "删除所有文档数据",httpMethod = "DELETE")
    @RequestMapping(value = "v2/users/deleteAllUser",method = RequestMethod.DELETE)
    public Object deleteAllUser() {

        Map map = new HashMap();
        List<UserDTO> userDTOS = (List<UserDTO>) getAllUser();
        int count = 0;
        for (UserDTO userDTO : userDTOS){
            Object o = deleteUser(userDTO.getId());
            map.put("删除的第几条数据-"+count,o);
            count++;
        }
        return map;
    }

}

