/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-10-26 10:07
 * @Since:
 */
package com.zja.mvc.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * WebMvcConfig 配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 虚拟映射：映射本地路径和资源路径
         */
        // 访问地址：http://127.0.0.1:19000/public/file
        registry.addResourceHandler("/public/file/**").addResourceLocations("file:D:/picture/");
        // 访问地址：http://127.0.0.1:19000/file
        registry.addResourceHandler("/file/**").addResourceLocations("classpath:/file/");

        /**
         * 代理静态地址
         */
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }


    /**
     * 返回给前端时json数据处理
     * 使用阿里 fastjson 作为JSON MessageConverter
     *
     * @param converters 消息转换器
     */
    // @Override
    // public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    //     FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    //     FastJsonConfig config = new FastJsonConfig();
    //     config.setSerializerFeatures(
    //             //全局修改日期格式,如果时间是data、时间戳类型，按照这种格式初始化时间 "yyyy-MM-dd HH:mm:ss"
    //             SerializerFeature.WriteDateUseDateFormat,
    //             // 保留map空的字段
    //             SerializerFeature.WriteMapNullValue,
    //             // 将String类型的null转成""
    //             SerializerFeature.WriteNullStringAsEmpty,
    //             // 将Number类型的null转成0
    //             SerializerFeature.WriteNullNumberAsZero,
    //             // 将List类型的null转成[]
    //             SerializerFeature.WriteNullListAsEmpty,
    //             // 将Boolean类型的null转成false
    //             SerializerFeature.WriteNullBooleanAsFalse,
    //             // 避免循环引用
    //             SerializerFeature.DisableCircularReferenceDetect);
    //
    //     converter.setFastJsonConfig(config);
    //     converter.setDefaultCharset(Charset.forName("UTF-8"));
    //     List<MediaType> mediaTypeList = new ArrayList<>();
    //     // 解决中文乱码问题，相当于在Controller上的@RequestMapping中加了个属性produces = "application/json"
    //     mediaTypeList.add(MediaType.APPLICATION_JSON);
    //     converter.setSupportedMediaTypes(mediaTypeList);
    //     converters.add(converter);
    // }

    /**
     * 启动完成后，重定向到某个页面
     */
    // @Override
    // public void addViewControllers(ViewControllerRegistry registry) {
    //     // 启动时页面重定向到swagger页面
    //     registry.addRedirectViewController("/", "/swagger-ui.html");
    // }


    /**
     * 添加不被拦截的资源: 例如，静态不被springmvc拦截
     */
    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     //开放所有资源路径
    //     registry.addResourceHandler("/**").addResourceLocations("/");
    //     // 添加 swagger和index页面
    //     registry.addResourceHandler("swagger-ui.html", "index.html").addResourceLocations("classpath:/META-INF/resources/");
    //     registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
    //     registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    //     registry.addResourceHandler("/static/**").addResourceLocations("classpath:/META-INF/resources/static/");
    //     // 自定义静态资源文件目录 "项目根目录/rest/public/resource/",不被springmvc拦截
    //     registry.addResourceHandler("/rest/public/resource/**").addResourceLocations("/rest/public/resource/");
    // }


    /**
     * 解决Cors跨域问题
     */
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //             .allowedOrigins("*")
    //             .allowCredentials(true)
    //             .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    //             .maxAge(3600);
    // }
}
