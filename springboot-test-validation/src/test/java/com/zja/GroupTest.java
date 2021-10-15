package com.zja;

import com.zja.dto.GroupDTO;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-17 18:17
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class GroupTest {

    @Test
    public void test(){
        GroupDTO groupDTO = new GroupDTO();
        // 此处指定了校验组是：User.Group.class
        Set<ConstraintViolation<GroupDTO>> result = Validation.buildDefaultValidatorFactory().getValidator().validate(groupDTO, GroupDTO.Group.class);

        // 对结果进行遍历输出
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);

        //现象：只有Default这个Group的校验了，序列上其它组并没有执行校验，原因是默认Default组校验木有通过，GroupA/GroupB组的校验也就不执行了~
        //middlename middlename may be empty: null
        //firstname firstname may be empty: null
    }


    @Test
    public void test2(){
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setFirstname("f");
        groupDTO.setMiddlename("s");
        // 此处指定了校验组是：User.Group.class
        Set<ConstraintViolation<GroupDTO>> result = Validation.buildDefaultValidatorFactory().getValidator().validate(groupDTO, GroupDTO.Group.class);

        // 对结果进行遍历输出
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);

        //现象：Default组都校验通过后，执行了GroupA组的校验。但GroupA组校验木有通过，GroupB组的校验也就不执行了~
        //lastname lastname may be empty: null

    }


}
