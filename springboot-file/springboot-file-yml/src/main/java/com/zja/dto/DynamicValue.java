package com.zja.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-03 15:44
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
@Component
@ConfigurationProperties(prefix = "dynamicvalue")
public class DynamicValue {
    @Value("data")
    private String data;
    @Value("value")
    private String value;
}
