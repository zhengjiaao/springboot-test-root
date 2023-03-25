/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-10-26 10:07
 * @Since:
 */
package com.zja.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 虚拟映射：映射本地路径和资源路径
         */
        //访问地址：http://127.0.0.1:19000/public/file
        registry.addResourceHandler("/public/file/**").addResourceLocations("file:D:/picture/");
        //访问地址：http://127.0.0.1:19000/file
        registry.addResourceHandler("/file/**").addResourceLocations("classpath:/file/");

        /**
         * 代理静态地址
         */
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
