package com.zja.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.Date;

/** class 类中的字段上，没有使用 @Field,那么该部分字段值，不会存储到solr数据库中
 * @program: springbootdemo
 * @Date: 2018/12/26 10:03
 * @Author: Mr.Zheng
 * @Description:
 */
@ApiModel(value = "测试dto")
@Data
public class UserEntityDto implements Serializable {
    @ApiModelProperty(value = "id",required = true)
    @Field("id")
    private Long id;
    @ApiModelProperty(value = "guid")
    private String guid;
    @ApiModelProperty(value = "name")
    @Field("name")
    private String name;
    @ApiModelProperty(value = "age")
    @Field("age")
    private String age;
    @ApiModelProperty(value = "createTime")
    private Date createTime;
    @ApiModelProperty(value = "lastUpdateTime")
    private Date lastUpdateTime;
}
