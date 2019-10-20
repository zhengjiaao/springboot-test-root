package com.dist.utils.json;


import com.dist.constant.DateContants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.PropertyFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 进行JSON转换的工具类
 * 
 * @author Administrator
 * 
 */
public class JSONUtil {

	protected final static Logger logger = LoggerFactory.getLogger(JSONUtil.class);
	public static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * 排除bean之间的关系字段
	 * 
	 * @param bean
	 * @return
	 */
	public static String[] getExcludeFields(Object bean) {
		Set<String> list = new HashSet<String>();
		list.add("handler");
		list.add("fieldHandler");
		list.add("hibernateLazyInitializer");
		for (Class<?> superClass = bean.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {

			Field[] fields = superClass.getDeclaredFields();
			for (Field field : fields) {
				if (field.getAnnotation(OneToOne.class) != null || field.getAnnotation(OneToMany.class) != null
						|| field.getAnnotation(ManyToOne.class) != null
						|| field.getAnnotation(ManyToMany.class) != null) {
					list.add(field.getName());
				}
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 将java对象转换成JSON字符串，若对象为null，那么返回null
	 * 
	 * @param o
	 *            需要被转换的java对象
	 * @return json格式的字符串
	 */
	public static final String toJSONString(Object o) {
		if (o == null) {
			return null;
		}
		try {
			return mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将java对象转换成JSON字符串，并排除标注为Lazy的属性，若对象为null，那么返回null
	 * 
	 * @param o
	 * @return
	 * @author lqy
	 * @see 2015-08-03 modified by lqy,
	 * @see 2015-08-05 modified by lqy,增加对Date转换的支持，将JSONARRAY改为JSONObject（避免解析时最外面多了一对[]） 
	 * @see 2015-08-06 modifyed by lqy, 将Entity中的标记为级联的Entity过滤掉
	 */
	public static final String toJSONStringExcludeLazy(Object o) {
		//		logger.info(">>>>toJSONStringExcludeLazy(Object o=[{}]) start", o);
		if (o == null) {
			return null;
		}
		try {
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT/* .STRICT */);
			jsonConfig.setExcludes(new String[] { "handler", "fieldHandler", "hibernateLazyInitializer" });
			// jsonConfig.setExcludes(getExcludeFields(o));
			jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
				@Override
				public boolean apply(Object source/* 属性的拥有者 */, String name /* 属性名字 */, Object value/* 属性值 */ ) {
					try {
						logger.debug("Object=" + source + ",name=" + name);
						// 使用反射根据属性名称获得对应的Field（注意不能用getDeclaredField，只能获得直接的Field，无法获得继承的Field）
						Field f = source.getClass().getDeclaredField(name);
						// 如果该Field的注解（直接注解和继承注解）包括OneToMany、ManyToMany、OneToOneManyToOne，即该Field级联实体，过滤滤掉
						if (f.getAnnotation(OneToMany.class) != null
								|| f.getAnnotation(ManyToMany.class) != null && f.getAnnotation(OneToOne.class) != null
								|| f.getAnnotation(ManyToOne.class) != null) {
							logger.debug("将被过滤");
							return true;// return true to skip name
						}
					} catch (Exception e) {
						logger.debug("未在Field中找到,不会过滤");
						return false;
					}
					logger.debug("不会过滤");
					return false;
				}
			});

			jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
				private SimpleDateFormat sd = new SimpleDateFormat(DateContants.DATE_FORMAT);
				@Override
				public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
					return value == null ? "" : sd.format(value);
				}
				@Override
				public Object processArrayValue(Object value, JsonConfig jsonConfig) {
					return null;
				}
			});
			JSONObject json = JSONObject.fromObject(o, jsonConfig);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将java对象转换成JSON字符串，若对象为null，那么返回null
	 * 
	 * @param o
	 * @param cycleDetectionStrategy
	 *            转换成json的级别设置
	 * @return created by weifj, 2015-06-09
	 */
	public static final String toJSONString(Object o, CycleDetectionStrategy cycleDetectionStrategy) {
		if (o == null) {
			return null;
		}
		try {
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setCycleDetectionStrategy(cycleDetectionStrategy);
			JSONArray json = JSONArray.fromObject(o, jsonConfig);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取泛型的Collection Type
	 * 
	 * @param collectionClass
	 *            泛型的Collection
	 * @param elementClasses
	 *            元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 *
	 * json字符串转集合对象。
	 * 
	 * @author 何顺
	 * @param str
	 *            json字符串
	 * @param collectionClass
	 *            集合class
	 * @param elementClasses
	 *            对象class
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static final Object toCollection(String str, Class<?> collectionClass, Class<?>... elementClasses) {
		JavaType javaType = getCollectionType(collectionClass, elementClasses);
		Collection<?> coll = null;
		try {
			coll = mapper.readValue(str, javaType);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coll;
	}

	/**
	 * json字符串转对象
	 * 
	 * @author 何顺
	 * @param str
	 *            json字符串
	 * @param clazz
	 *            对象的类
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static final Object toObject(String str, Class<?> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		// 设置时间格式, modified by weifj, 2015-06-10
		SimpleDateFormat sdf = new SimpleDateFormat(DateContants.DATE_FORMAT);
		mapper.setDateFormat(sdf);
		return mapper.readValue(str, clazz);
	}

	/**
	 * json数组转成对象数组
	 * 
	 * @param str
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static final Object[] toArray(String str, Class<?> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		JSONArray jsonArray = JSONArray.fromObject(str);
		Collection coll = JSONArray.toCollection(jsonArray, clazz);
		return coll.toArray();
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj.getClass() == String.class) {
			return (String) obj;
		}
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			logger.error("json序列化出错：" + obj, e);
			return null;
		}
	}

	public static <T> T toBean(String json, Class<T> tClass) {
		try {
			return mapper.readValue(json, tClass);
		} catch (IOException e) {
			logger.error("json解析出错：" + json, e);
			return null;
		}
	}

	public static <E> List<E> toList(String json, Class<E> eClass) {
		try {
			return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
		} catch (IOException e) {
			logger.error("json解析出错：" + json, e);
			return null;
		}
	}

	public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
		try {
			return mapper.readValue(json, mapper.getTypeFactory().constructMapType(HashMap.class, kClass, vClass));
		} catch (IOException e) {
			logger.error("json解析出错：" + json, e);
			return null;
		}
	}

	public static <K, V> Map<K, V> toLinkedMap(String json, Class<K> kClass, Class<V> vClass) {
		try {
			return mapper.readValue(json, mapper.getTypeFactory().constructMapType(LinkedHashMap.class, kClass, vClass));
		} catch (IOException e) {
			logger.error("json解析出错：" + json, e);
			return null;
		}
	}

	public static <T> T nativeRead(String json, TypeReference<T> type) {
		try {
			return mapper.readValue(json, type);
		} catch (IOException e) {
			logger.error("json解析出错：" + json, e);
			return null;
		}
	}

}
