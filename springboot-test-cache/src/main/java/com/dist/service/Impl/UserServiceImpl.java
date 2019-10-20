package com.dist.service.Impl;

import com.dist.entity.UserEntity;
import com.dist.service.UserService;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/15 10:36
 */
@Service(value = "userServiceImpl")
@Transactional     //事务，表示该类下所有的都受事务控制
@CacheConfig(cacheNames = "users2")
public class UserServiceImpl implements UserService {

    /**
     * 测试 ehcache 保存缓存
     *
     * @Cacheable 可以标记在一个方法上，也可以标记在一个类上
     * 流程：被调用后将其返回值缓存起来，以保证下次利用同样的参数来执行该方法时可以直接从缓存中获取结果，而不需要再次执行该方法
     * 如何缓存：缓存方法的返回值时是以键值对进行缓存的，值就是方法的返回结果
     * @Cacheable可以指定三个属性，value、key和condition
     *  value属性指定cache的名称（即选择ehcache.xml中哪种缓存方式存储）
     *  key（默认策略）属性是用来指定Spring缓存方法的返回结果时对应的key的，该属性支持SpringEL表达式;可使用“#参数名”或者“#p参数index”
     *  condition 缓存的条件，可以为空，使用 SpEL 编写，返回 true 或者 false，只有为 true 才进行缓存 如：condition = "#userName.length()>2"
     *
     * @param id
     * @return
     */
    //@Cacheable(value = "users")
    @Cacheable(value = {"users","users2"},key = "#id",condition = "#id=1")
    @Override
    public UserEntity getUserById(Integer id) {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName("张三");
        userEntity.setAge(23);

        System.out.println("id值："+id);

        return userEntity;
    }

    /**
     * 测试 ehcache 清楚缓存
     * @param userEntity
     */
    @CacheEvict(value = "users",allEntries = true)
    @Override
    public void saveUser(UserEntity userEntity){
        System.out.println(userEntity);
    }


    /**
     * 以下是key 实例
     * @param id
     * @return
     */
    @Cacheable(value="users", key="#id")
    public UserEntity find1(Integer id) {
        return null;
    }

    @Cacheable(value="users", key="#p0")
    public UserEntity find2(Integer id) {
        return null;
    }

    @Cacheable(value="users", key="#user.id")
    public UserEntity find3(UserEntity user) {
        return null;
    }

    @Cacheable(value="users", key="#p0.id")
    public UserEntity find4(UserEntity user) {
        return null;
    }


    /**
     * 以下是模拟测试
     * @param typeId
     * @return
     */
    @Cacheable
    @Override
    public String save(String typeId) {
        System.out.println("save()执行了=============");
        return "模拟数据库保存";
    }

    @CachePut
    @Override
    public String update(String typeId) {
        System.out.println("update()执行了=============");
        return "模拟数据库更新";
    }

    @CacheEvict
    @Override
    public String delete(String typeId) {
        System.out.println("delete()执行了=============");
        return "模拟数据库删除";
    }

    @Cacheable
    @Override
    public String select(String typeId) {
        System.out.println("select()执行了=============");
        return "模拟数据库查询";
    }

    /**
     * 复杂的缓存：高速缓存
     * @param name
     * @return
     */
    @Caching(
            cacheable = {
                    @Cacheable(cacheNames = {"users"},key="#name") //（1）根据name查询user
            },
            put = {
                    @CachePut(cacheNames = {"users"},key="#result.id") //(2) 根据id查询user 以另一种key将查询出的结果缓存到缓存中
            }
    )
    @Override
    public UserEntity selectByName(String name) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName(name);
        userEntity.setAge(23);
        System.out.println("进入方法："+name);
        return userEntity;
    }

}
