package com.zja.poitl.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/04/02 10:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanCondition {

    @Id
    private String id;
    /**
     * 项目名
     */
    private String name;
    /**
     * 创建时间
     */
    private String time;
    /**
     * 分地块数据
     */
    private List<PlanConditionData> data = new ArrayList<>();
    /**
     * 项目信息
     */
    private JSONObject info;
    /**
     * 报告路径
     */
    private String path;
    /**
     * 报告名称
     */
    private String reportName;
    /**
     * 图形信息
     */
    private String uploadGeometries;
    /**
     * 模版类型
     * PlanConditionTypeEnum
     */
    private String templateType;
}
