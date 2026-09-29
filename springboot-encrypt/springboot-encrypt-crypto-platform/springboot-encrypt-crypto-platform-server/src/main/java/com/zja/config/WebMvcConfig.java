package com.zja.config;

import com.zja.security.PlatformAuthInterceptor;
import com.zja.security.RequestBodyCachingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置：注册请求体缓存过滤器与在线接入认证拦截器
 *
 * <p>拦截范围：/api/key/**、/api/crypto/** 需要接入签名；
 * /api/app/**（应用注册/管理）与 /api/audit/**（审计查询）为管理端接口，演示环境不拦截。</p>
 *
 * <p>/api/** 请求体由缓存过滤器统一做大小限制（max-body-size，默认1MB），超限返回413。</p>
 *
 * @author: zhengja
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final PlatformAuthInterceptor platformAuthInterceptor;
	/** 请求体大小上限（字节） */
	private final long maxBodySize;

	public WebMvcConfig(PlatformAuthInterceptor platformAuthInterceptor,
	                    @Value("${crypto.platform.max-body-size:1048576}") long maxBodySize) {
		this.platformAuthInterceptor = platformAuthInterceptor;
		this.maxBodySize = maxBodySize;
	}

	@Bean
	public FilterRegistrationBean<RequestBodyCachingFilter> requestBodyCachingFilter() {
		FilterRegistrationBean<RequestBodyCachingFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new RequestBodyCachingFilter(maxBodySize));
		registration.addUrlPatterns("/api/*");
		registration.setOrder(1);
		registration.setName("requestBodyCachingFilter");
		return registration;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(platformAuthInterceptor)
				.addPathPatterns("/api/key/**", "/api/crypto/**");
	}
}
