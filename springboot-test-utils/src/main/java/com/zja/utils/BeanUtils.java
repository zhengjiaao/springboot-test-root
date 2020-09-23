package com.zja.utils;

import org.springframework.web.context.WebApplicationContext;

/**
 * @author fangkeliu
 *
 */
public class BeanUtils {

	private static WebApplicationContext context = null;
	
	public static <T> T getBean(String beanId,Class<T> clazz){
		return context.getBean(beanId,clazz);
	}
	
}
