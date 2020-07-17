package com.dist.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-17 11:07
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
@Document(indexName = "book_index", type = "book_type", indexStoreType = "fs", shards = 5, replicas = 1, refreshInterval = "-1")
public class BookEntity {
    @Id
    private String id;
    private String title;
    private String content;
    private int userId;
    private int weight;
}
