package com.zja.dto;

import com.zja.config.validator.groupSequenceProvider.PersonGroupSequenceProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-17 18:29
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：自定义分组和级联校验
 */
@GroupSequenceProvider(PersonGroupSequenceProvider.class)
@Getter
@Setter
@ToString
public class PersonDTO {

    @NotNull
    private String name;
    @NotNull
    @Range(min = 10, max = 40)
    private Integer age;

    /*@NotNull
    @Size(min = 3, max = 5)*/
    @NotNull(groups = {WhenAge20And30Group.class, WhenAge30And40Group.class})
    @Size(min = 1, max = 2, groups = WhenAge20And30Group.class)
    @Size(min = 3, max = 5, groups = WhenAge30And40Group.class)
    private List<String> hobbies;



    /**
     * 定义专属的业务逻辑分组
     */
    public interface WhenAge20And30Group {
    }
    public interface WhenAge30And40Group {
    }
}
