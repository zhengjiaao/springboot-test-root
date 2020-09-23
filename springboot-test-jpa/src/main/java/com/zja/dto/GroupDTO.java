package com.zja.dto;

import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotEmpty;
import javax.validation.groups.Default;
import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-17 18:16
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：按分组进行有序校验
 */
@Data
public class GroupDTO implements Serializable {

    @NotEmpty(message = "firstname may be empty")
    private String firstname;
    @NotEmpty(message = "middlename may be empty", groups = Default.class)
    private String middlename;
    @NotEmpty(message = "lastname may be empty", groups = GroupA.class)
    private String lastname;
    @NotEmpty(message = "country may be empty", groups = GroupB.class)
    private String country;


    public interface GroupA {
    }

    public interface GroupB {
    }

    // 组序列
    @GroupSequence({Default.class, GroupA.class, GroupB.class})
    public interface Group {
    }

}
