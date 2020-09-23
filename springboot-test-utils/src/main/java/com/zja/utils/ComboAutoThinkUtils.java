package com.zja.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComboAutoThinkUtils {

	/**
	 * 用正则表达式进行匹配,只处理*
	 * 
	 * @param list
	 *            包含所有需要进行验证的字符串集合
	 * @param text
	 *            用户在combo组件中输入的文本
	 * @return 符合用户要求的集合
	 */
	public static List<String> validate(List<String> list, String text) {
		String val_str = null;

		if ('*' != text.charAt(text.length() - 1)) {
			text = text + "*";
		}

		if (text.indexOf("*") > -1) {
			// 此处进行替换*o
			val_str = text.replace("*", "[\\w/_;-\\.]*");
		}

		// 防止有.的出现，即不把.当做正则表达式中的.
		val_str = val_str.replace(".", "\\.");

		Pattern pattern = Pattern.compile(val_str);

		// 验证list中是否匹配
		List<String> temp_list = new ArrayList<String>();
		for (int k = 0; k < list.size(); k++) {
			Matcher mat = pattern.matcher(list.get(k).trim());
			if (mat.matches()) {
				temp_list.add(list.get(k));
			}
		}
		return temp_list;
	}

	/**
	 * 把一个Set变成一个List,为了保证集合的有序性
	 * 
	 * @param set
	 * @return
	 */
	public static List<String> fromSetToList(Set<String> set) {
		List<String> temp_list = new ArrayList<String>();
		if (set.size() > 0) {
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				temp_list.add(it.next());
			}
		}
		return temp_list;
	}

	/**
	 * 验证是否包含非法字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIllegal(String str) {
		String strng = "%?~@#$^&(){}[]<>";
		for (int k = 1; k <= strng.length(); k++) {
			if (str.contains(strng.substring(k - 1, k))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证是否包含非法字符（添加邮箱地址的时候）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIllegalForEmail(String str) {
		String strng = "%?~#$^&(){}[]<>";
		for (int k = 1; k <= strng.length(); k++) {
			if (str.contains(strng.substring(k - 1, k))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证是否合法
	 * 
	 * @param list
	 * @param str
	 * @return
	 */
	public static boolean IsResultIllegal(List<String> list, String str) {
		if (null != list && list.size() > 0) {
			if (list.contains(str)) {
				return true;
			}
		}
		return false;
	}
}
