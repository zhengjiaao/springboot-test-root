package com.dist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**spring-data-jpa-extra
 * Create by IntelliJ Idea 2018.2
 *  配置sftl文件所在的位置
 * @author: qyp
 * Date: 2019-07-02 0:11
 */
@ConfigurationProperties(prefix = "spring.jpa.extra")
@Configuration
public class SpringJpaExtraProperties {

    /**
     * 源码看 FreemarkerSqlTemplates.resolveSqlResource
     * 例如 templateLocation：classpath:/sqltemplates 那么 扫描路径为 classpath:/sqltemplates/** /*.sftl
     *  templateLocation：classpath:/sqltemplates/Test.sftl 那么将只扫描这个一个文件
     *  例如  templateBasePackage：sqltemplates.mysql 那么扫描路径为 classpath*: sqltemplates/sql/** /*.sftl
     *
     *  两个属性可以共存
     */
    private String templateLocation;

    private String templateBasePackage;


    public String getTemplateLocation() {
        return templateLocation;
    }

    public void setTemplateLocation(String templateLocation) {
        this.templateLocation = templateLocation;
    }

    public String getTemplateBasePackage() {
        return templateBasePackage;
    }

    public void setTemplateBasePackage(String templateBasePackage) {
        this.templateBasePackage = templateBasePackage;
    }
}