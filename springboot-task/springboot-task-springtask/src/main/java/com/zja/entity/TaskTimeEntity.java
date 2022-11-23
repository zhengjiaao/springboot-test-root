package com.zja.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@ApiModel(value = "动态配置定时任务")
public class TaskTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "TASKKEY", nullable = false)
    @ApiModelProperty(value = "任务key值 唯一")
    private String taskKey;

    @Column(name = "TASKDESC")
    @ApiModelProperty(value = "任务描述")
    private String taskDesc;

    @Column(name = "TASKCRON", nullable = false)
    @ApiModelProperty(value = "任务表达式：执行周期")
    private String taskCron;

    @Column(name = "INITSTARTFLAG")
    @ApiModelProperty(value = "程序初始化是否启动 1 是 0 否")
    private Integer initStartFlag;

    @Column(name = "STARTFLAG")
    @ApiModelProperty(value = "当前是否已启动")
    private boolean startFlag;

    @Column(name = "CREATETIME")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Column(name = "UPDATETIME")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
