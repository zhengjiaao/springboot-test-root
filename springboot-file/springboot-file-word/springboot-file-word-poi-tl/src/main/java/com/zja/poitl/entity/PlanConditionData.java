package com.zja.poitl.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

/**
 * @author: zhengja
 * @since: 2024/04/02 10:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanConditionData {
    private JSONObject data;
}
