/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-10 9:42
 * @Since:
 */
package com.zja.interpreter.sqlExpression;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/10/10 9:42
 */
@Data
public class SqlContext {
    private List<String> selectFields;
    private String tableName;
    private String whereCondition;

    public void addField(String field) {
        if (CollectionUtils.isEmpty(selectFields)) {
            selectFields = new ArrayList<>();
        }
        selectFields.add(field);
    }

}