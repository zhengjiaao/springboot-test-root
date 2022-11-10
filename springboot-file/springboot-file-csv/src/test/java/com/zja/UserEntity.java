/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-10 9:23
 * @Since:
 */
package com.zja;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class UserEntity {

    private String name;
    private int age;
    //注意，需要一旦加了一个注解，其他没有添加注解的属性将不会参与导入导出
    @CsvBindByName(column = "密码")
    private String pwd;

    public UserEntity() {
    }

    public UserEntity(String name) {
        this.name = name;
    }

    public UserEntity(String name, int age, String pwd) {
        this.name = name;
        this.age = age;
        this.pwd = pwd;
    }
}
