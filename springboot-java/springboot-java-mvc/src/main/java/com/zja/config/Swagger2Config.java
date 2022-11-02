package com.zja.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-25 10:53
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：swagger2 api配置
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket adminApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Admin API")
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(paths())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private Predicate<String> paths(){
        return PathSelectors.regex("^/(?!error).*$");
    }

    private ApiInfo apiInfo(){
        Contact contact = new Contact("Zhengja", "https://www.jianshu.com/u/70d69269bd09", " zhengja@dist.com.cn");
        return new ApiInfoBuilder()
                .title("SpringBoot系统")
                .description("开发API文档")
                .contact(contact)
                .version("1.0")
                .build();
    }

    /**
     * 关闭swagger页面 Models默认打开
     * @return
     */
    @Bean
    public UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder().defaultModelsExpandDepth(0)
                .build();
    }
}
