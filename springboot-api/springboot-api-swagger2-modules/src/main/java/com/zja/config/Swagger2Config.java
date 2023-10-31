/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-10-22 15:17
 * @Since:
 */
package com.zja.config;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * http://localhost:8080/swagger-ui.html
 */
@EnableSwagger2
@Configuration
public class Swagger2Config {

    @Value("${swagger.enable:true}")
    private Boolean enable;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zja"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket createRestApiWeb1() {
        return (new Docket(DocumentationType.SWAGGER_2))
                .enable(enable)
                .groupName("Web1 API")
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zja.controller.web1"))
                .paths(paths())
                .build()
                .apiInfo(apiInfo()).useDefaultResponseMessages(false);
    }

    @Bean
    public Docket createRestApiWeb2() {
        return (new Docket(DocumentationType.SWAGGER_2))
                .enable(enable)
                .groupName("Web2 API")
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zja.controller.web2"))
                .paths(paths())
                .build()
                .apiInfo(apiInfo()).useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("提供rest服务").description("我是描述").contact(new Contact("联系人", "www.baidu.com", "123@qq.com"))
                .version("1.0").build();
    }

    private Predicate<String> paths() {
        return PathSelectors.regex("^/(?!error).*$");
    }
}
