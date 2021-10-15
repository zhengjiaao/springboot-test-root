package com.zja;

import com.zja.config.validator.groups.UserLoginGroup;
import com.zja.config.validator.util.ValidationUtil;
import com.zja.dto.Child;
import com.zja.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-17 9:37
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Slf4j
public class UserTest {

    @Test
    public void test(){

    }

    @Test
    public void Test(){
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword(""); //password:需要匹配正则表达式"^[a-zA-Z0-9]{6,16}$"
        //userDTO.setPassword("A123asd");
        userDTO.setEmail("126qq.com");
        //child:不能为null
        //userDTO.setChild(new Child());  //child.name:不能为null
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(userDTO);
        if(validResult.hasErrors()){
            String errors = validResult.getErrors();
            System.out.println(errors);
        }
    }


    @Test
    public void Test2(){
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("");
        userDTO.setEmail("126qq.com");
        userDTO.setChild(new Child());
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(userDTO, UserLoginGroup.class);
        if(validResult.hasErrors()){
            String errors = validResult.getErrors();
            System.out.println(errors);
        }
    }

}
