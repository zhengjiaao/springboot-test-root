package com.zja;

import com.zja.dto.PersonDTO;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Arrays;
import java.util.Set;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-17 18:24
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：测试级联和组顺序
 */
public class PersonTest {

    /**
     * 自定义分组-推荐
     */
    @Test
    public void test(){
        PersonDTO person = new PersonDTO();
        person.setName("fsx");
        person.setAge(2); // 5 25 35 修改值进行测试
        person.setHobbies(Arrays.asList("足球","篮球"));

        Set<ConstraintViolation<PersonDTO>> result = Validation.buildDefaultValidatorFactory().getValidator().validate(person);

        // 对结果进行遍历输出
        result.stream().map(v -> v.getPropertyPath() + " " + v.getMessage() + ": " + v.getInvalidValue()).forEach(System.out::println);

        //输出结果:
        //Age=5
        //age 需要在10和40之间: 5

        //Age=25
        //通过

        //Age=35
        //hobbies 个数必须在3和5之间: [足球, 篮球]
    }
}
