package com.dist.controller;

import com.dist.entity.UserEntity;
import com.dist.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**Ehcache 缓存
 * @author zhengja@dist.com.cn
 * @data 2019/8/15 10:51
 */
@Api(tags = {"EhcacheController"},description = "Ehcache 缓存")
@RestController
@RequestMapping(value = "rest/ehcache")
public class EhcacheController {

    @Autowired
    @Qualifier(value = "userServiceImpl")
    private UserService userService;

    @ApiOperation(value = "保存缓存-users",httpMethod = "GET")
    @RequestMapping(value = "getuserbyid",method = RequestMethod.GET)
    public Object getUserById(@ApiParam(value = "查询字段：id") @RequestParam Integer id){

        //缓存存放的目录
        String userHome = System.getProperty("user.home");
        String userDir = System.getProperty("user.dir");
        String tmpDir = System.getProperty("java.io.tmpdir");

        System.out.println("userHome:"+userHome);
        System.out.println("userDir:"+userDir);
        System.out.println("tmpDir:"+tmpDir);

        return this.userService.getUserById(id);
    }

    @ApiOperation(value = "清除缓存-users",httpMethod = "POST")
    @RequestMapping(value = "saveuser",method = RequestMethod.POST)
    public void saveUser(@ApiParam(value = "参考：Model") @RequestBody UserEntity userEntity){
        this.userService.saveUser(userEntity);
    }


    @ApiOperation(value = "模拟数据库保存",httpMethod = "GET")
    @RequestMapping(value = "save",method = RequestMethod.GET)
    public String save(@ApiParam(value = "typeId") @RequestParam String typeId) {
        return this.userService.save(typeId);
    }


    @ApiOperation(value = "模拟数据库更新",httpMethod = "GET")
    @RequestMapping(value = "update",method = RequestMethod.GET)
    public String update(@ApiParam(value = "typeId") @RequestParam String typeId) {
        return this.userService.update(typeId);
    }

    @ApiOperation(value = "模拟数据库删除",httpMethod = "GET")
    @RequestMapping(value = "delete",method = RequestMethod.GET)
    public String delete(@ApiParam(value = "typeId") @RequestParam String typeId) {
        return this.userService.delete(typeId);
    }

    @ApiOperation(value = "模拟数据库查询",httpMethod = "GET")
    @RequestMapping(value = "select",method = RequestMethod.GET)
    public String select(@ApiParam(value = "typeId") @RequestParam String typeId) {
        return this.userService.select(typeId);
    }

    @ApiOperation(value = "复杂的缓存规则",httpMethod = "GET")
    @RequestMapping(value = "selectbyname",method = RequestMethod.GET)
    public UserEntity selectByName(@ApiParam(value = "name") @RequestParam String name) {
        return this.userService.selectByName(name);
    }

}
