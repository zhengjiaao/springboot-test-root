package com.dist.base;

import java.text.MessageFormat;

/**
 * 业务逻辑异常，在抛出该异常的时候，可以将异常信息放入该异常。
 * <p>
 * 用于在业务逻辑方法中出现问题时让Controller方法获取错误信息
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -6076157176820605186L;

	/**
	 * 用于产生业务逻辑异常时，将业务逻辑异常信息放入该异常中，方便捕获到异常之后获取异常信息。
	 * 
	 * @param message
	 *            业务逻辑异常信息
	 */
	public BusinessException(String message) {
		super(message);
	}

	/**
	 * 将异常信息和参数进行直接字符串拼接是效率较为低下的方式，采用StringBuffer的话，较为繁琐，所以此处提供一个多参数绑定的方法
	 * 
	 * @param message
	 *            带有格式化内容的异常信息，如"Your name is {0}, your age is {1}"
	 * @param params
	 *            需要插入到异常信息中的内容，可以有一个以上，以逗号分隔，将按参数顺序替换message中的{n}。
	 *            <p>
	 *            示例： new
	 *            BusinessException("Your name is {0}, your age is {1}","ShenYuTing"
	 *            ,100);
	 *            <p>
	 *            注意： pattern中不能使用单引号'，否则会导致无法绑定参数，直接返回pattern串，如果必须使用的话，
	 *            需要转义；不能像logger.debug()一样可以使用{}，必须使用{0}、{1}等，否则就会报错
	 *            （java.lang.IllegalArgumentException: can't parse argument
	 *            number）
	 */
	public BusinessException(String pattern, Object... params) {
		super(MessageFormat.format(pattern, params));
	}

	/**
	 * 创建一个包含其他异常的实例
	 * 
	 * @param e
	 */
	public BusinessException(Throwable e) {
		super(e);
	}

}
