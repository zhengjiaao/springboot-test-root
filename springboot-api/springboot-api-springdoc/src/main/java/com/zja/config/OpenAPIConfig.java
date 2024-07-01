package com.zja.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.License;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-06-28 10:18
 */
@Configuration
public class OpenAPIConfig {
    // 全局设置
/*    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("测试 title")
                .description("SpringBoot2 集成 springdoc-openapi-ui")
                .version("v1")).externalDocs(new ExternalDocumentation()
                .description("项目API文档")
                .url("/"));
    }*/

    // 配置认证
/*    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.api-docs.version}") String appVersion) {
        return new OpenAPI().components(new Components()
                .addSecuritySchemes("basicScheme", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"))
                .addParameters("myHeader1", new Parameter().in("header").schema(new StringSchema()).name("myHeader1")).addHeaders("myHeader2", new Header().description("myHeader2 header").schema(new StringSchema())))
                .info(new Info().title("Petstore API").version(appVersion).description("This is a sample server Petstore server. You can find out more about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/). For this sample, you can use the api key `special-key` to test the authorization filters.").termsOfService("http://swagger.io/terms/").license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }*/

    // ========== 分组 OpenAPI 配置 ==========

    // 配置分组
/*    @Bean
    public GroupedOpenApi usersGroup(@Value("${springdoc.version}") String appVersion) {
        return GroupedOpenApi.builder().group("xmls").addOperationCustomizer((operation, handlerMethod) -> {
                    operation.addSecurityItem(new SecurityRequirement().addList("basicScheme"));
                    return operation;
                }).addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Xmls API").version(appVersion)))
                .packagesToScan("com.zja.controller").pathsToMatch("/xml/**").build();
    }*/
}
