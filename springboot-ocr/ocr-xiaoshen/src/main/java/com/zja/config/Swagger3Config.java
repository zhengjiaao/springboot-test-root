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
 * http://localhost:port/swagger-ui/index.html#/
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
        return new ApiInfoBuilder().title("提供ocr服务")
                .description("目标，提取任何文档的内容及元数据信息：\n" +
                        "文本文件：txt、csv、xml、html、markdown等。\n" +
                        "Office文件：doc、docx、ppt、pptx、xls、xlsx等。\n" +
                        "PDF文件：pdf。\n" +
                        "超文本标记语言文件：htm、html。\n" +
                        "媒体文件：音频文件（mp3、wav、flac等）、视频文件（mp4、avi、mov等）。\n" +
                        "图像文件：jpg、png、gif、bmp等。\n" +
                        "归档文件：zip、tar、gzip、bzip2等。\n" +
                        "其他格式：包括电子书格式（如epub、mobi）、CAD文件格式（如dwg、dxf）、地理空间数据格式（如shp、kml）等.\n")
                .contact(new Contact("小神", "www.baidu.com", "zhengja@dist.com.cn "))
                .version("1.0").build();
    }
}
