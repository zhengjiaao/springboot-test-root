package com.zja.luascript;

import com.zja.BaseTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Arrays;
import java.util.List;


public class LuaScriptTests extends BaseTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void get() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/get.lua")));
        redisScript.setResultType(Boolean.class);

        String redisKet = "name";

        List<String> keys = Arrays.asList(redisKet);
        Object execResult = (Object) redisTemplate.execute(redisScript, keys);
        System.out.println(execResult);
    }

    @Test
    public void set() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/set.lua")));
        redisScript.setResultType(Boolean.class);

        String redisKet = "name";
        int redisvalue = 1;

        List<String> keys = Arrays.asList(redisKet);

        Boolean execResult = (Boolean) redisTemplate.execute(redisScript, keys, redisvalue);
        System.out.println(execResult);
    }

    @Test
    public void del() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/del.lua")));
        redisScript.setResultType(Boolean.class);

        String redisKey = "name";

        List<String> keys = Arrays.asList(redisKey);

        Boolean execResult = (Boolean) redisTemplate.execute(redisScript, keys);
        System.out.println(execResult);
    }

    //设置递增
    @Test
    public void incrby() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/incrby.lua")));
        redisScript.setResultType(Boolean.class);

        String redisKet = "name";

        List<String> keys = Arrays.asList(redisKet);

        Boolean execResult = (Boolean) redisTemplate.execute(redisScript, keys);
        System.out.println(execResult);
    }

    //设置时间
    @Test
    public void expire() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/expire.lua")));
        redisScript.setResultType(Boolean.class);

        String redisKet = "name";
        int redisKeyTime = 500; //秒

        List<String> keys = Arrays.asList(redisKet);

        Boolean execResult = (Boolean) redisTemplate.execute(redisScript, keys, redisKeyTime);
        System.out.println(execResult);
    }

    //限流效果
    @Test
    public void Limiting() {
        //redis 提供的 DefaultRedisScript
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class); //返回值类型
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/Limiting.lua")));

        List<String> keys = Arrays.asList("aaa");
        // 10秒内小于或等于3次时返回1，否则返回0
        for (int i = 0; i < 4; i++) {
            Object execute = redisTemplate.execute(redisScript, keys, 10, 3);
            System.out.println(execute);
        }
    }

}
