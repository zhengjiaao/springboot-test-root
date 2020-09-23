package com.zja.config.validator.groupSequenceProvider;

import com.zja.dto.PersonDTO;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-17 18:28
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：自定义分组顺序执行
 * 1. 若20 <= age < 30，那么hobbies的size需介于1和2之间
 * 2. 若30 <= age < 40，那么hobbies的size需介于3和5之间
 * 3. age其余值，hobbies无校验逻辑
 */
public class PersonGroupSequenceProvider implements DefaultGroupSequenceProvider<PersonDTO> {

    @Override
    public List<Class<?>> getValidationGroups(PersonDTO personDTO) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(PersonDTO.class); // 这一步不能省,否则Default分组都不会执行了，会抛错的

        if (personDTO != null) { // 这块判空请务必要做
            Integer age = personDTO.getAge();
            System.err.println("年龄为：" + age + "，执行对应校验逻辑");
            if (age >= 20 && age < 30) {
                defaultGroupSequence.add(PersonDTO.WhenAge20And30Group.class);
            } else if (age >= 30 && age < 40) {
                defaultGroupSequence.add(PersonDTO.WhenAge30And40Group.class);
            }
        }
        return defaultGroupSequence;
    }
}
