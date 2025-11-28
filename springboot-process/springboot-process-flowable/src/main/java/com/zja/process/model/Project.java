package com.zja.process.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @Author: zhengja
 * @Date: 2025-10-23 15:04
 */
@Entity
@Table(name = "project")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectId; // 关联项目ID，预留的字段
    private String projectName;

    private String processInstanceId;  // 关联的流程实例 ID
    private String taskId;  // 当前任务ID
    private String taskName;  // 当前任务名称
    private String level; // 当前所属层级：county, city, province
    private String status;  // 用于记录项目状态，项目状态：县级整改、省级退回、市级退回、市级审查、省级审核、审核通过

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
