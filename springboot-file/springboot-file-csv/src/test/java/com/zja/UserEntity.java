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

    @CsvBindByName(column = "名称")
    private String name;
    // todo 注，没有@CsvBindByName注解的属性将不会参与导入导出
    private int age;
    @CsvBindByName(column = "密码")
    private String pwd;

    public UserEntity() {
    }

    public UserEntity(String name) {
        this.name = name;
    }

    public UserEntity(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public UserEntity(String name, int age, String pwd) {
        this.name = name;
        this.age = age;
        this.pwd = pwd;
    }
}
