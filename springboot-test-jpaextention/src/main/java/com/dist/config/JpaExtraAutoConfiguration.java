package com.dist.config;

import com.slyak.spring.jpa.FreemarkerSqlTemplates;
import com.slyak.spring.jpa.SqlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**spring-data-jpa-extra
 * Create by IntelliJ Idea 2018.2
 *
 * @author: qyp
 * Date: 2019-07-01 23:12
 */
@Configuration
@EnableConfigurationProperties(SpringJpaExtraProperties.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class })
public class JpaExtraAutoConfiguration {

    @Autowired
    private SpringJpaExtraProperties springJpaExtraProperties;

    @Bean
    protected FreemarkerSqlTemplates freemarkerSqlTemplates() {
        FreemarkerSqlTemplates sqlTemplates = new FreemarkerSqlTemplates();
        String templateBasePackage = springJpaExtraProperties.getTemplateBasePackage();
        if (templateBasePackage != null) {
            sqlTemplates.setTemplateBasePackage(templateBasePackage);
        }
        String templateLocation = springJpaExtraProperties.getTemplateLocation();
        if (templateLocation != null) {
            sqlTemplates.setTemplateLocation(templateLocation);
        }
        sqlTemplates.setSuffix(".sftl");
        return sqlTemplates;
    }

    @Bean
    public SqlMapper sqlMapper() {
        return new SqlMapper();
    }
}
