package com.dist.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;

/**
 * @author yangmin
 * @date 2018/6/29.
 */
public class ParseUtil {
    public static int getInt(Object value) {
        if (value == null) {
            throw new NullPointerException();
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        } else {
            return Integer.parseInt(value.toString());
        }
    }

    public static int getInt(Object value, int defaultValue) {
        return getInteger(value, defaultValue);
    }

    public static Integer getInteger(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        } else {
            try {
                if (value.toString().indexOf(".") != -1) {
                    return getInteger(ParseUtil.getDouble(value, null), null);
                } else {
                    return Integer.parseInt(value.toString());
                }
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    public static long getLong(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        } else if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else if (obj instanceof Date) {
            return ((Date) obj).getTime();
        } else {
            return Long.parseLong(obj.toString());
        }
    }

    public static Long getLong(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        } else if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? 1L : 0L;
        } else if (value instanceof Date) {
            return ((Date) value).getTime();
        } else {
            try {
                if (value.toString().indexOf(".") != -1) {
                    return getLong(ParseUtil.getDouble(value, null), null);
                } else {
                    return Long.parseLong(value.toString());
                }
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

//	public static Long[] getLongArray(Object obj, Long[] defaultValue) {
//		Long[] result = defaultValue;
//		try {
//			if (obj == null) {
//
//			} else if (obj instanceof Long[]) {
//				result = (Long[]) obj;
//			} else if (obj.getClass().isArray()) {
//				int length = Array.getLength(obj);
//				result = new Long[length];
//				for (int i = 0; i < length; i++) {
//					result[i] = getLong(Array.get(obj, i), null);
//				}
//			} else if (obj instanceof Collection) {
//				Collection<?> col = (Collection<?>) obj;
//				result = new Long[col.size()];
//				Iterator<?> ite = col.iterator();
//				int index = 0;
//				while (ite.hasNext()) {
//					result[index] = getLong(ite.next(), null);
//					index++;
//				}
//			} else if (obj instanceof JSONArray) {
//				JSONArray ja = (JSONArray) obj;
//				result = new Long[ja.size()];
//				for (int i = 0; i < ja.size(); i++) {
//					result[i] = getLong(ja.get(i), null);
//				}
//			} else if (obj instanceof String) {
//				String str = (String) obj;
//				if (str.startsWith("[") && str.endsWith("]")) {
//					JSONArray ja = JSONArray.parseArray(str);
//					return getLongArray(ja, defaultValue);
//				} else {
//					String[] sa = str.split("[^0-9]+");
//					result = new Long[sa.length];
//					for (int i = 0; i < sa.length; i++) {
//						result[i] = getLong(sa[i].trim(), null);
//					}
//				}
//			}
//		} catch (Exception e) {
//			result = defaultValue;
//		}
//		return result;
//	}

    public static float getFloat(Object value) {
        if (value == null) {
            throw new NullPointerException();
        } else if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else {
            return Float.parseFloat(value.toString());
        }
    }

    public static Float getFloat(Object value, Float defaultValue) {
        if (value == null) {
            return defaultValue;
        } else if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else {
            try {
                return Float.parseFloat(value.toString());
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

//	public static Float[] getFloatArray(Object obj, Float[] defaultValue) {
//		Float[] result = defaultValue;
//		try {
//			if (obj == null) {
//
//			} else if (obj instanceof Float[]) {
//				result = (Float[]) obj;
//			} else if (obj.getClass().isArray()) {
//				int length = Array.getLength(obj);
//				result = new Float[length];
//				for (int i = 0; i < length; i++) {
//					result[i] = getFloat(Array.get(obj, i), null);
//				}
//			} else if (obj instanceof Collection) {
//				Collection<?> col = (Collection<?>) obj;
//				result = new Float[col.size()];
//				Iterator<?> ite = col.iterator();
//				int index = 0;
//				while (ite.hasNext()) {
//					result[index] = getFloat(ite.next(), null);
//					index++;
//				}
//			} else if (obj instanceof JSONArray) {
//				JSONArray ja = (JSONArray) obj;
//				result = new Float[ja.size()];
//				for (int i = 0; i < ja.size(); i++) {
//					result[i] = getFloat(ja.get(i), null);
//				}
//			} else if (obj instanceof String) {
//				String str = (String) obj;
//				if (str.startsWith("[") && str.endsWith("]")) {
//					JSONArray ja = JSONArray.parseArray(str);
//					return getFloatArray(ja, defaultValue);
//				} else {
//					String[] sa = str.split("[^0-9.]+");
//					result = new Float[sa.length];
//					for (int i = 0; i < sa.length; i++) {
//						result[i] = getFloat(sa[i].trim(), null);
//					}
//				}
//			}
//		} catch (Exception e) {
//			result = defaultValue;
//		}
//		return result;
//	}

    public static double getDouble(Object value) {
        if (value == null) {
            throw new NullPointerException();
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? 1D : 0D;
        } else {
            return Double.parseDouble(value.toString());
        }
    }

    public static Double getDouble(Object value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? 1D : 0D;
        } else {
            try {
                return Double.parseDouble(value.toString());
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

//	public static Double[] getDoubleArray(Object value, Double[] defaultValue) {
//		Double[] result = defaultValue;
//		try {
//			if (value == null) {
//
//			} else if (value instanceof Double[]) {
//				result = (Double[]) value;
//			} else if (value.getClass().isArray()) {
//				int length = Array.getLength(value);
//				result = new Double[length];
//				for (int i = 0; i < length; i++) {
//					result[i] = getDouble(Array.get(value, i), null);
//				}
//			} else if (value instanceof Collection) {
//				Collection<?> col = (Collection<?>) value;
//				result = new Double[col.size()];
//				Iterator<?> ite = col.iterator();
//				int index = 0;
//				while (ite.hasNext()) {
//					result[index] = getDouble(ite.next(), null);
//					index++;
//				}
//			} else if (value instanceof String) {
//				String str = (String) value;
//				if (str.startsWith("[") && str.endsWith("]")) {
//					JSONArray ja = JSONArray.parseArray(str);
//					return getDoubleArray(ja, defaultValue);
//				} else {
//					String[] sa = str.split("[^0-9.]+");
//					result = new Double[sa.length];
//					for (int i = 0; i < sa.length; i++) {
//						result[i] = getDouble(sa[i].trim(), null);
//					}
//				}
//			}
//		} catch (Exception e) {
//			result = defaultValue;
//		}
//		return result;
//	}

    public static BigDecimal getBigDecimal(Object value) {
        return getBigDecimal(value, null);
    }

    public static BigDecimal getBigDecimal(Object value, BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        } else if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        } else if (value instanceof Double) {
            return  BigDecimal.valueOf((Double) value);
        } else if (value instanceof Long) {
            return new BigDecimal((Long) value);
        } else if (value instanceof Number) {
            return  BigDecimal.valueOf(((Number) value).doubleValue());
        } else if (value instanceof String) {
            return new BigDecimal((String) value);
        } else {
            return defaultValue;
        }
    }

//	public static BigDecimal[] getBigDecimalArray(Object value,
//			BigDecimal[] defaultValue) {
//		BigDecimal[] result = defaultValue;
//		try {
//			if (value == null) {
//
//			} else if (value instanceof BigDecimal[]) {
//				result = (BigDecimal[]) value;
//			} else if (value.getClass().isArray()) {
//				int length = Array.getLength(value);
//				result = new BigDecimal[length];
//				for (int i = 0; i < length; i++) {
//					result[i] = getBigDecimal(Array.get(value, i), null);
//				}
//			} else if (value instanceof Collection) {
//				Collection<?> col = (Collection<?>) value;
//				result = new BigDecimal[col.size()];
//				Iterator<?> ite = col.iterator();
//				int index = 0;
//				while (ite.hasNext()) {
//					result[index] = getBigDecimal(ite.next(), null);
//					index++;
//				}
//			} else if (value instanceof String) {
//				String str = (String) value;
//				if (str.startsWith("[") && str.endsWith("]")) {
//					JSONArray ja = JSONArray.parseArray(str);;
//					return getBigDecimalArray(ja, defaultValue);
//				} else {
//					String[] sa = str.split("[^0-9.]+");
//					result = new BigDecimal[sa.length];
//					for (int i = 0; i < sa.length; i++) {
//						result[i] = getBigDecimal(sa[i].trim(), null);
//					}
//				}
//			}
//		} catch (Exception e) {
//			result = defaultValue;
//		}
//		return result;
//	}

    public static String[] splitString(String value, String splitRegex,
                                       boolean removeEmpty) {
        if (value == null) {
            return new String[0];
        }
        String[] strArray = value.split(splitRegex);
        if (!removeEmpty) {
            return strArray;
        }
        List<String> list = new ArrayList<String>();
        for (String str : strArray) {
            if (str.length() == 0) {
                continue;
            }
            list.add(str);
        }
        if (list.size() == 0) {
            return new String[0];
        }
        strArray = new String[list.size()];
        list.toArray(strArray);
        return strArray;
    }

    public static List<String> splitStringList(String value, String splitRegex,
                                               boolean removeEmpty) {
        List<String> list = new ArrayList<String>();
        if (value == null) {
            return list;
        }
        String[] strArray = value.split(splitRegex);
        if (removeEmpty) {
            for (String str : strArray) {
                if (str.length() == 0) {
                    continue;
                }
                list.add(str);
            }
        } else {
            for (String str : strArray) {
                list.add(str);
            }
        }
        return list;
    }

    public static <T> String join(String splitStr, T... array) {
        if (array == null || array.length == 0) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            for (Object obj : array) {
                sb.append(splitStr + obj.toString());
            }
            return sb.toString().substring(splitStr.length());
        }
    }

    public static byte[] hex2byte(String hex) {
        byte[] b = hex.getBytes();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static boolean getBoolean(Object value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else {
                String str = value.toString();
                if ("false".equals(str) || "0".equals(str)) {
                    return false;
                } else if ("true".equals(str) || "1".equals(str)) {
                    return true;
                }
                return Boolean.parseBoolean(value.toString());
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static <T> List<T> getList(T... array) {
        List<T> list = new ArrayList<T>();
        if (array != null) {
            for (T item : array) {
                list.add(item);
            }
        }
        return list;
    }

    public static String strDefVal(String val, String def) {
        if (val == null) {
            return def;
        }
        return val;
    }

    public static String byte2hex(byte[] bytes) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < bytes.length; n++) {
            stmp = (Integer.toHexString(bytes[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static String def2Value(Object obj, String notNull, String isNull) {
        return obj == null ? isNull : notNull;
    }

    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getMap(Object value) {
        if (value == null) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (value instanceof Map) {
            for (Object key : ((Map) value).keySet()) {
                map.put(key.toString(), ((Map) value).get(key));
            }
        } else {
            Method[] methods = value.getClass().getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                try {
                    if (!Modifier.isPublic(method.getModifiers())
                            || method.getParameterTypes().length > 0) {
                        continue;
                    }
                    String name = method.getName();
                    if (name.startsWith("get") && name.length() > 3) {
                        name = name.substring(3, 4).toLowerCase()
                                + name.substring(4, name.length());
                        map.put(name, method.invoke(value));
                    } else if (name.startsWith("is") && name.length() > 2) {
                        name = name.substring(2, 3).toLowerCase()
                                + name.substring(3, name.length());
                        map.put(name, method.invoke(value));
                    }
                } catch (Exception e) {

                }
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> getMap(Class<K> clsK, Class<V> clsV,
                                          List<V> list, String keyField) {
        Map<K, V> map = new HashMap<K, V>();
        if (list == null || list.size() == 0 || keyField == null
                || keyField.length() == 0) {
            return map;
        }
        String methodName = keyField.substring(0, 1).toUpperCase()
                + keyField.substring(1);
        Method method = null;
        Field field = null;
        try {
            method = clsV.getMethod("get" + methodName);
            for (Iterator<V> iterator = list.iterator(); iterator.hasNext();) {
                V value = iterator.next();
                map.put((K) method.invoke(value), value);
            }
        } catch (Exception e) {
        }
        if (method == null) {
            try {
                field = clsV.getField(keyField);
                for (Iterator<V> iterator = list.iterator(); iterator.hasNext();) {
                    V value = iterator.next();
                    map.put((K) field.get(value), value);
                }
            } catch (Exception e) {
            }
        }

        return map;
    }

    @SuppressWarnings("rawtypes")
    public static Object getProperty(Object obj, String propertyName,
                                     boolean ignoreCase) {
        if (obj == null || propertyName == null || propertyName.length() == 0) {
            return null;
        }
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            if (length > 0) {
                obj = Array.get(obj, 0);
            } else {
                obj = null;
            }
        } else if (obj instanceof Collection) {
            Collection col = (Collection) obj;
            if (col.size() > 0) {
                obj = col.iterator().next();
            } else {
                obj = null;
            }
        }
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.containsKey(propertyName)) {
                return map.get(propertyName);
            } else if (ignoreCase) {
                for (Object key : map.keySet()) {
                    if (!propertyName.equalsIgnoreCase(key.toString())) {
                        continue;
                    }
                    return map.get(key);
                }
            }
        } else {
            String methodName = propertyName.substring(0, 1).toUpperCase()
                    + propertyName.substring(1);
            Method method = null;
            Field field = null;
            try {
                method = obj.getClass().getMethod("get" + methodName);
                if (method != null && Modifier.isPublic(method.getModifiers())
                        && !Modifier.isStatic(method.getModifiers())) {
                    return method.invoke(obj);
                } else if (ignoreCase) {
                    Method[] methods = obj.getClass().getDeclaredMethods();
                    for (Method m : methods) {
                        Class<?>[] types = m.getParameterTypes();
                        if (types != null && types.length > 0) {
                            continue;
                        }
                        if (m.getName().equalsIgnoreCase("get" + methodName)
                                && Modifier.isPublic(m.getModifiers())
                                && !Modifier.isStatic(m.getModifiers())) {
                            method = m;
                            return m.invoke(obj);
                        }
                    }
                }
            } catch (Exception e) {
            }
            if (method == null) {
                try {
                    field = obj.getClass().getField(propertyName);
                    if (field != null
                            && Modifier.isPublic(field.getModifiers())
                            && !Modifier.isStatic(field.getModifiers())) {
                        return field.get(obj);
                    } else if (ignoreCase) {
                        Field[] fields = obj.getClass().getDeclaredFields();
                        for (Field f : fields) {
                            if (f.getName().equalsIgnoreCase(propertyName)
                                    && Modifier.isPublic(f.getModifiers())
                                    && !Modifier.isStatic(f.getModifiers())) {
                                field = f;
                                return f.get(obj);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    public static String clob2String(Clob clob) throws SQLException, IOException {
        String reString = "";
        // 得到流
        Reader is = clob.getCharacterStream();
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        // 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
        while (s != null) {
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        return reString;
    }

    public static byte[] blob2byte(Blob blob) {
        byte[] b = null;
        try {
            if (blob != null) {
                long in = 0;
                b = blob.getBytes(in, (int) (blob.length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
