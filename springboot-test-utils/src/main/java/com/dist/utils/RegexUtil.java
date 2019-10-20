package com.dist.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 正则表达式的匹配共用方法
 * @author fangkeliu
 * @Date:2010-12-28
 * @version:1.0.0
 *
 */
public class RegexUtil {

	/*
	 *  Regex表示正则表达式，
	 *  input表示要匹配或验证的文本或字符串
	 *   
	 */
	private String regex = "", input = "";
	
	/*
	 * regexError表示当正则表达式出现语法错误时，
	 * 出现相关的错误信息 
	 * results表示匹配后的结果
	 */
	private String regexError = ""; 
	private String results = "";
	
	private Pattern pattern;
	private Matcher matcher;

	public RegexUtil() {
		this("", "");
	}

	public RegexUtil(String regex, String input) {
		setRegex(regex);
		setInput(input);
	}

	/**
	 * 初始化
	 * @return
	 */
	private boolean initialize() {
		try {
			/*
			 * 使用Pattern类的静态compile方法，接收一个代表正则 表达式的字符串(变量:regex)的参数，并返回一个指定的
			 * 正则表达式的Pattern对象的pattern
			 */
			pattern = Pattern.compile(regex);
			/*
			 * 利用刚刚返回的Pattern类的实例pattern，并调用pattern
			 * 的matcher方法。该方法接收一个用于匹配的实现了CharSequence
			 * 接口的对象input，并将返回一个Matcher对象赋值给变量matcher。
			 */
			matcher = pattern.matcher(input);
			
			return true;
		} catch (PatternSyntaxException pse) {
			regexError = "\n正则表达式语法错误,错误的相关信息如下：" + "\n当前的正则表达式是："
					+ pse.getPattern() + "\n错误描述：" + pse.getDescription()
					+ "\n错误信息：" + pse.getMessage() + "\n错误索引：" + pse.getIndex();
			return false;
		}
	}

	/**
	 * find方法匹配
	 * 
	 * 根据正则表达式来匹配输入的部分文本，并得到匹配结果<br>
	 * 本方法不返回任何数据，但是调用的是matcher的方法find,
	 * find方法根据给定的正则表达式来匹配的文本中的子字符串。
	 * find 和 matches方法不同之处在于，只有被匹配的文本中的部分内容 符合给定的正则表达式时,
	 * find方法才返回真值。
	 */
	public List<String> validatePartText(String input, String regex) {
		this.setInput(input);
		this.setRegex(regex);
		List<String> _temp = new ArrayList<String>();
		if (initialize()) {
			while (matcher.find()) {
				results += "find some regex text\"" + matcher.group() + "\"\t start index ："
						+ matcher.start() + "\t end index：" + matcher.end() + "。\n";
				String _t = matcher.group();
				if(!"i".equals(_t))
				  _temp.add(_t);
			}
		} 
		
		return _temp;
	}

	public Set<String> findMatchText(String input, String regex) {
		this.setInput(input);
		this.setRegex(regex);
		Set<String> _temp = new HashSet<String>();
		if (initialize()) {
			while (matcher.find()) {
				String s = matcher.group();
				_temp.add(s);
			}
		}
		return _temp;
	}

	public String getResults() {
		return this.results;
	}

	public String getRegexError() {
		return this.regexError;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public void setInput(String input) {
		this.input = input;
	}

}

