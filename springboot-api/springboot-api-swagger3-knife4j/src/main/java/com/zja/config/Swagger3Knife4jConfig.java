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
 */
@EnableKnife4j  //仅需加入此注解就可以了，swagger配置不变
@Configuration
public class Swagger3Knife4jConfig {
/*

    @Bean
    public Docket initDocket(Environment env) {
        //设置要暴漏接口文档的配置环境
        //设置要显示的Swagger环境
        Profiles profile = Profiles.of("test", "dev");
        //获取项目的环境：
        //通过environment.acceptsProfiles判断是否处在自己设定的环境当中
        boolean flag = env.acceptsProfiles(profile);
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(myApiInfo())
                .enable(flag)//是否启动swagger 默认为true ,如果为false，则Swagger不能再浏览器中访问
                .select()
                //RequestHandlerSelectors,配置要扫描接口的方式
                .apis(RequestHandlerSelectors.basePackage("com.zja.web")) //指定要扫描的包
//                .apis(RequestHandlerSelectors.any())   //扫描全部
                //.apis(RequestHandlerSelectors.none()):不扫描
                //.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)):扫描类上的注解，参数是一个注解的反射对象
                //.apis(RequestHandlerSelectors.withMethodAnnotation(GetMapping.class)):扫描方法上的注解
//                .apis(RequestHandlerSelectors.basePackage("com.zhao.controller"))
                //paths()过滤什么路径(url)
                //paths(PathSelectors.ant("/zhao/**")) 就是在localhost:8080/zhao  后面的路径
//                .paths(PathSelectors.ant("/zhao/**"))
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                //右上角 组（有几个Docket，有几个组）
                .groupName("第一组");
    }

    private ApiInfo myApiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger3-接口文档")
                .description("第一组")
                .contact(new Contact("zhengja", "https://www.baidu.cn", "123@qq.com "))
                .version("V1.0")
//                .license("Apache 2.0")
                .build();
    }
*/

}
