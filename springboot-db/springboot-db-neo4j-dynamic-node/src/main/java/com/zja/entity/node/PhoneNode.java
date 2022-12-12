/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-12 13:43
 * @Since:
 */
package com.zja.entity.node;

import com.zja.entity.BaseNode;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity(label = "Phone")
public class PhoneNode extends BaseNode {
    /**
     * 手机名称
     */
    private String name;
    /**
     * 品牌：华为、小米、苹果
     */
    private String brand;
}
