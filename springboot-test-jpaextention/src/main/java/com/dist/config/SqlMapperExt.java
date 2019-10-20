package com.dist.config;

import com.slyak.spring.jpa.SqlMapper;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.io.IOException;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/18 10:09
 */
@Component
public class SqlMapperExt extends SqlMapper {

    @Override
    public Object getResultList(String templateKey) throws IOException {
        Query query = this.getQuery(templateKey, null, String.class);
        return query.getResultList();
    }

}
