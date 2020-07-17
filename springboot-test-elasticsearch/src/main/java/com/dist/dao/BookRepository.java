package com.dist.dao;

import com.dist.entity.BookEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-17 14:13
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Component
public interface BookRepository extends ElasticsearchRepository<BookEntity, String> {
}
