/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-10-22 15:17
 * @Since:
 */
package com.zja.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Configuration;

/**
 * http://localhost:19000/doc.html
 * http://localhost:19000/swagger-ui.html
 */
@EnableKnife4j  //仅需加入此注解就可以了，swagger配置不变
@Configuration
public class Swagger2Knife4jConfig {

   /* @Bean(value = "dockerBean")
    public Docket dockerBean() {
        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //描述字段支持Markdown语法
                        .description("# Knife4j RESTful APIs")
                        .termsOfServiceUrl("https://doc.xiaominfo.com/")
                        .contact(new Contact("zhengja", "https://www.baidu.cn", "123@.qq.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("Web服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.zja.web"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }*/

}
