/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-10-22 15:17
 * @Since:
 */
package com.zja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * http://localhost:8080/swagger-ui/index.html#/
 */
@EnableOpenApi
@Configuration
public class Swagger3Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.zja")).paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("密码服务平台 REST API")
                .description("面向三方系统的统一密码服务：应用注册 → 创建密钥 → 加解密/签名验签；支持在线SDK、HTTP接口、离线SDK三种接入方式")
                .contact(new Contact("zhengja", "https://github.com/zhengjiaao/springboot-test-root", "zhengja@dist.com.cn"))
                .version("1.0").build();
    }
}
