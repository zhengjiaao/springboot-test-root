package com.dist.utils;


import org.apache.commons.lang3.StringUtils;

import java.text.*;
import java.util.*;

/**
 * 日期工具
 */
public abstract class DateUtil {

	/**
	 * 一天的毫秒数
	 */
	public static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

	/**
	 * 默认的日期时间格式
	 */
	public static final String DATETIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 默认的日期格式
	 */
	public static final String DATE_FORMAT_STR = "yyyy-MM-dd";

	public static final String DATE_TIME_FORMAT_STR = "yyyy-MM-dd HH:mm";

	/**
	 * 日期格式
	 */
	public static final String DEF_FORMAT_STR = "yyyy/MM/dd";

	/**
	 * 默认的日期时间格式
	 */
	public static final String TIME_FORMAT_STR = "HH:mm:ss";

	/**
	 * 第一季度起始月份，0表示1月份。
	 */
	public static final int SPRING_FIRST_MONTH = 0;

	/**
	 * 第二季度起始月份，3表示4月份。
	 */
	public static final int SUMMER_FIRST_MONTH = 3;

	/**
	 * 第三季度起始月份，6表示7月份。
	 */
	public static final int AUTUMN_FIRST_MONTH = 6;

	/**
	 * 第四季度起始月份，9表示10月份。
	 */
	public static final int WINTER_FIRST_MONTH = 9;

	/**
	 * 第一季度起始月份，2表示3月份。
	 */
	public static final int SPRING_LAST_MONTH = 2;

	/**
	 * 第二季度起始月份，5表示6月份。
	 */
	public static final int SUMMER_LAST_MONTH = 5;

	/**
	 * 第三季度起始月份，8表示9月份。
	 */
	public static final int AUTUMN_LAST_MONTH = 8;

	/**
	 * 第四季度起始月份，11表示12月份。
	 */
	public static final int WINTER_LAST_MONTH = 11;

	/**
	 * 第一季度，默认为1
	 */
	public static final int QUARTER_FIRST = 1;

	/**
	 * 第二季度，默认为2
	 */
	public static final int QUARTER_SECOND = 2;

	/**
	 * 第三季度，默认为3
	 */
	public static final int QUARTER_THREE = 3;

	/**
	 * 第四季度，默认为4
	 */
	public static final int QUARTER_FOUR = 4;

	/**
	 * 月份最大数12
	 */
	public static final int MAX_MONTH_TWELVE = 12;

	/**
	 * 月份最小数1
	 */
	public static final int MIN_MONTH_ONE = 1;

	/**
	 * 季度数量，用于根据月份计算所在季度。
	 */
	public static final int QUARTER_NUM = 3;

	/**
	 * 获取当前日期。
	 * 修改标识：
	 * 1、把返回的时间类型java.util.Date改为java.sql.Date
	 *      2015-06-06 by weifj
	 * @return 当前日期对象。
	 */
	public static java.sql.Date now() {
		return new java.sql.Date(System.currentTimeMillis());
	}

	/**
	 * 将指定格式的字符串转换为日期类型,如果格式与字符串不匹配，则返回null。
	 * @param dateStr 待转换的字符串
	 * @param format 日期格式
	 * @return 转换后的日期对象
	 */
	public static Date strToDate(String dateStr, String format) {

		if (!StringUtils.isBlank(dateStr)) {
			//构造制定格式format的SimpleDateFormat对象。
			SimpleDateFormat formatter = new SimpleDateFormat(format);

			//进行转换,这里要传入new ParsePosition(0)，否则是抛出异常，而不是返回null
			return formatter.parse(dateStr, new ParsePosition(0));
		}
		return null;
	}

	/**
	 * 将指定格式的字符串转换为日期类型,如果格式与字符串不匹配，则返回null。
	 *
	 * @param dateStr 待转换的字符串	1970-01-01
	 * @param extend  是否扩展到23：59分 ,86400000为一天的毫秒形式
	 * 输入字符串日期格式  yyyy-MM-dd
	 * 输出日期格式  yyyy-MM-dd HH:mm:ss
	 * @return 转换后的日期对象
	 */
	public static Date str2Date(String dateStr,boolean extend) {
		if (!StringUtils.isBlank(dateStr)) {
			SimpleDateFormat fm=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (extend) {
					return fm.parse(fm.format(sdf.parse(dateStr).getTime()+86400000));
				}
				return fm.parse(fm.format(sdf.parse(dateStr).getTime()));
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss格式的字符串转换为日期类型。如果字符串不为yyyy-MM-dd HH:mm:ss，则返回null
	 * @param dateStr 待转换的日期字符串
	 * @return 转换后的日期对象
	 */
	public static Date strToDate(String dateStr) {
		return strToDate(dateStr, DATETIME_FORMAT_STR);
	}

	/**
	 * 获取格式为yyyy-MM-dd HH:mm:ss的当前日期字符串。
	 * @return 日期字符串
	 */
	public static String nowStrByFormat() {
		SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT_STR);
		return formatter.format(now()).toString();
	}

	/**
	 * 获取格式为yyyy-MM-dd 的当前日期字符串。
	 * @return 日期字符串
	 */
	public static String nowDateStrByFormat() {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_STR);
		return formatter.format(now()).toString();
	}

	/**
	 * 获取格式为yyyy-MM-dd 中yyyy的当前年份字符串。
	 * @return 日期字符串
	 */
	public static String nowYearStrByFormat() {
		return nowDateStrByFormat().split("-")[0];
	}

	/**
	 * 获取格式为yyyy-MM-dd 中MM的当前月份字符串
	 * @return 日期字符串
	 */
	public static String nowMonthStrByFormat() {
		return nowDateStrByFormat().split("-")[1];
	}

	/**
	 * 获取格式为yyyy-MM-dd 中dd的当前日期字符串。
	 * @return 日期字符串
	 */
	public static String nowDayStrByFormat() {
		return nowDateStrByFormat().split("-")[2];
	}


	/**
	 * 根据指定的日期格式获取当前的日期字符串。
	 * @param format 日期格式，如："yyyy-MM-dd HH:mm:ss:sss"。
	 * @return 日期字符串
	 */
	public static String nowStrByFormat(String format) {
		return toStrWithFormat(now(), format);
	}

	/**
	 * 日期转换为字符串
	 * <p>
	 * 如果日期为null，返回空串，否则按yyyy-MM-dd格式转换后返回。如果不满足格式，则会抛出异常。
	 * @param date 日期对象
	 * @return 格式后的日期字符串
	 */
	public static String toDateStr(Date date) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_STR);
			return formatter.format(date);
		}
		return "";
	}

	/**
	 * 日期转换为字符串
	 * <p>
	 * 如果日期为null，返回空串，否则按yyyy-MM-dd HH:mm:ss格式转换后返回。如果不满足格式，则会抛出异常。
	 * @param date 日期对象
	 * @return 格式后的日期字符串
	 */
	public static String toDateTimeStr(Date date) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT_STR);
			return formatter.format(date);
		}
		return "";
	}

	/**
	 * 日期转换为字符串。
	 * <p>
	 * 注意：如果日期为null，返回空串，否则按format指定的格式转换后返回。如果日期格式为空，则按照yyyy-MM-dd HH:mm:ss格式转换。
	 * @param date 日期对象
	 * @param format 指定的日期格式，形如yyyy-MM-dd HH:mm:ss
	 * @return 格式后的日期字符串
	 */
	public static String toStrWithFormat(Date date, String format) {
		String dateStr = "";

		//如果日期为空，则返回空字符串。
		if (null != date) {
			if (!StringUtils.isBlank(format)) {
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				dateStr = formatter.format(date);
			} else {
				//如果日期格式为空或空串，则用默认的yyyy-MM-dd HH:mm:ss格式转换。
				dateStr = toDateTimeStr(date);
			}
		}
		return dateStr;
	}

	/**
	 *日期各个字段的增量计算。<p>
	 *对于日期各个字段的算法都是调用Calendar的add方法，只是传入字段类型不一致，其处理过程几乎一致。
	 * @param date 日期对象。
	 * @param field 参与计算的字段，比如时、分、年，建议使用Calendar提供的对应常量。
	 * @param addValue 增量。
	 * @return 增量计算后日期对象。
	 */
	private static Date calcDateFields(Date date, int field, int addValue) {
		if (null != date) {
			Calendar calendar = toCalendar(date);

			//计算
			calendar.add(field, addValue);
			return calendar.getTime();
		}
		return null;
	}

	/**
	 * 毫秒增量方法<p>
	 * 根据给定的日期和相差的毫秒，得到另一个日期，天数为负表是在给定日期前，为正表是在给定日期后。
	 * @param date 日期对象
	 * @param milliseconds 毫秒
	 * @return 增量后的日期
	 */
	public static Date addMilliseconds(Date date, int milliseconds) {
		return calcDateFields(date, Calendar.MILLISECOND, milliseconds);
	}

	/**
	 * 秒增量方法
	 * <p>
	 * 根据给定的日期和相差的秒，得到另一个日期，秒数为负表是在给定日期前，为正表是在给定日期后。
	 * @param date 日期对象
	 * @param seconds 秒
	 * @return 增量后的日期
	 */
	public static Date addSeconds(Date date, int seconds) {
		return calcDateFields(date, Calendar.SECOND, seconds);
	}

	/**
	 * 分钟增量方法
	 * <p>
	 * 根据给定的日期和相差的分钟，得到另一个日期，分钟数为负表是在给定日期前，为正表是在给定日期后。
	 * </p>
	 * @param date 日期对象
	 * @param minutes 分钟增量
	 * @return 增量后的日期
	 */
	public static Date addMinutes(Date date, int minutes) {
		return calcDateFields(date, Calendar.MINUTE, minutes);
	}

	/**
	 * 小时增量方法
	 * <p>
	 * 根据给定的日期和相差的小时，得到另一个日期，小时数为负表是在给定日期前，为正表是在给定日期后。
	 * </p>
	 * @param date 日期对象
	 * @param hours 小时增量
	 * @return 增量后的日期
	 */
	public static Date addHours(Date date, int hours) {
		return calcDateFields(date, Calendar.HOUR_OF_DAY, hours);
	}

	/**
	 * 日期增量方法
	 * <p>
	 * 根据给定的日期和相差的天数，得到另一个日期，天数为负表是在给定日期前，为正表是在给定日期后。
	 * </p>
	 * @param date 日期对象
	 * @param day 天数
	 * @return 增量后的日期
	 */
	public static Date addDay(Date date, int day) {
		return calcDateFields(date, Calendar.DAY_OF_MONTH, day);
	}

	/**
	 * 月数增量方法
	 * <p>
	 * 根据给定的日期和相差的月数，得到另一个日期，月数为负表是在给定日期前，为正表是在给定日期后。
	 * </p>
	 * @param date 日期对象
	 * @param month 月数
	 * @return 增量后的日期
	 */
	public static Date addMonth(Date date, int month) {
		return calcDateFields(date, Calendar.MONTH, month);
	}

	/**
	 * 年数增量方法
	 * <p>
	 * 根据给定的日期和相差的年数，得到另一个日期，年数为负表是在给定日期前，为正表是在给定日期后。
	 * @param date 日期对象
	 * @param year 年数
	 * @return 增量后的日期
	 */
	public static Date addYear(Date date, int year) {
		return calcDateFields(date, Calendar.YEAR, year);
	}

	/**
	 * 指定日期所属月第一天的日期
	 * </pre>
	 * @param date 日期对象
	 * @return 指定日期所在月第一天的日期
	 */
	public static Date firstDayOfMonth(Date date) {
		Calendar calendar = toCalendar(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 指定日期所属月最后一天的日期
	 * @param date 日期对象
	 * @return 最后一天的日期
	 */
	public static Date lastDayOfMonth(Date date) {
		Calendar calendar = toCalendar(date);
		lastDayOfMonth(calendar);
		return calendar.getTime();
	}

	/**
	 *计算指定日期所属月最后一天的日期
	 * @param calendar 指定日期的日历对象
	 */
	private static void lastDayOfMonth(Calendar calendar) {

		//将月份加1，得到下一月份
		calendar.add(Calendar.MONTH, 1);

		//得到下一月份的第一天
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		//将下一月份的第一天减一天，就得到这个月的最后一天
		calendar.add(Calendar.DAY_OF_MONTH, -1);
	}

	/**
	 * 指定日期所属季度的第一天的日期。
	 * </pre>
	 * @param date 日期对象
	 * @return 日期日期所属季度的第一天的日期
	 */
	public static Date firstDayOfQuarter(Date date) {
		Calendar calendar = toCalendar(date);

		//取得月份，根据月份计算出所在季度。
		int month = calendar.get(Calendar.MONTH) + 1;
		int quarter = getQuarter(month);
		switch (quarter) {
		case QUARTER_FIRST:
			//第一季度，把该日期的月份设置为1月份，并设置天数为第一天。
			calendar.set(Calendar.MONTH, 0); // 0表示第一个月
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return calendar.getTime();
		case QUARTER_SECOND:
			//第2季度，把该日期的月份设置为4月份，并设置天数为第一天。
			calendar.set(Calendar.MONTH, SUMMER_FIRST_MONTH); // 3表示第四个月
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return calendar.getTime();
		case QUARTER_THREE:
			//第3季度，把该日期的月份设置为7月份，并设置天数为第一天。
			calendar.set(Calendar.MONTH, AUTUMN_FIRST_MONTH); // 6表示第七个月
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return calendar.getTime();
		case QUARTER_FOUR:
			//第4季度，把该日期的月份设置为10月份，并设置天数为第一天。
			calendar.set(Calendar.MONTH, WINTER_FIRST_MONTH); // 9表示第十个月
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			return calendar.getTime();
		default:
			return null;
		}
	}

	/**
	 *通过月份计算季度
	 * @param month 月份
	 * @return 月份所在季度
	 */
	private static int getQuarter(int month) {

		//先判断是否处于
		if (month < MIN_MONTH_ONE || month > MAX_MONTH_TWELVE) {
			throw new IllegalArgumentException("month is invalid.");
		}
		return (month - 1) / QUARTER_NUM + 1;
	}

	/**
	 * 指定日期所属季度的最后一天的日期
	 * @param date 日期对象
	 * @return 日期日期所属季度的最后一天的日期
	 */
	public static Date lastDayOfQuarter(Date date) {
		Calendar calendar = toCalendar(date);

		//取得月份，根据月份计算出所在季度。
		int month = calendar.get(Calendar.MONTH) + 1;
		int quarter = getQuarter(month);
		switch (quarter) {
		case QUARTER_FIRST:

			//第1季度，把该日期的月份设置为3月份，并设置天数为最后一天。
			calendar.set(Calendar.MONTH, SPRING_LAST_MONTH);
			lastDayOfMonth(calendar);
			return calendar.getTime();
		case QUARTER_SECOND:

			//第2季度，把该日期的月份设置为6月份，并设置天数为最后一天。
			calendar.set(Calendar.MONTH, SUMMER_LAST_MONTH);
			lastDayOfMonth(calendar);
			return calendar.getTime();
		case QUARTER_THREE:

			//第3季度，把该日期的月份设置为9月份，并设置天数为最后一天。
			calendar.set(Calendar.MONTH, AUTUMN_LAST_MONTH);
			lastDayOfMonth(calendar);
			return calendar.getTime();
		case QUARTER_FOUR:

			//第4季度，把该日期的月份设置为12月份，并设置天数为最后一天。
			calendar.set(Calendar.MONTH, WINTER_LAST_MONTH);
			lastDayOfMonth(calendar);
			return calendar.getTime();
		default:
			return null;
		}
	}

	/**
	 * 验证该字符串是否是合法的日期格式
	 * @param dateStr 日期字符串
	 * @param format 格式
	 * @return true：合法 ； false：不合法
	 */
	public static boolean isValidDate(String dateStr, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setLenient(false);
		try {

			//对dateStr进行转换，如果符合格式format，则表示dateStr合法。
			formatter.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * 将日期对象转成日历对象。<p>
	 * 注意：如果传入时间为空，会抛出IllegalArgumentException。
	 * @param date 日期对象
	 * @return 日历对象
	 */
	public static Calendar toCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (null == date) {
			throw new IllegalArgumentException("date is null");
		}
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 根据年月日转成日期对象
	 * @param year 创建日期的年份
	 * @param month 创建日期的月份 （1月份：1，并非0）
	 * @param day 创建日期的天
	 * @return 根据指定年月日创建的日期对象
	 */
	public static Date newDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();

		//注意，月份要减一
		calendar.set(year, month - 1, day);
		return calendar.getTime();
	}

	/**
	 * 根据年月日时分秒转成日期对象。
	 * @param year 创建日期的年份
	 * @param month 创建日期的月份
	 * @param day 创建日期的天
	 * @param hour 创建日期的小时
	 * @param minute 创建日期的分钟
	 * @param second 创建日期的秒
	 * @return 根据指定年月日创建的日期对象
	 */
	public static Date newDate(int year, int month, int day, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();

		//注意:月份要减一
		calendar.set(year, month - 1, day, hour, minute, second);
		return calendar.getTime();
	}

	/**
	 * 根据两次日期获取相差的天数。
	 * @param date1 日期
	 * @param date2 日期
	 * @return 两个日期相差的天数
	 */
	public static int getDays(Date date1, Date date2) {

		//通过formatter去掉日期中的时间，因为这里的比较如果有时间会出问题，如果两个时间相差不超过一天中的总毫秒数，
		//那么该算法获得的相差天数始终为0，比如当date1是2013-05-05 00:00:00，date2是2013-05-05 23:59:59，毫秒只
		//相差1000毫秒，但是天数却是相差的是一天。
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_STR);
		String date1Str = formatter.format(date1);
		String date2Str = formatter.format(date2);

		//用去掉时间的字符串构造日期对象，取得毫秒数，并计算毫秒数差值。
		double millis =
				DateUtil.strToDate(date1Str, DATE_FORMAT_STR).getTime() - DateUtil.strToDate(date2Str, DATE_FORMAT_STR)
						.getTime();

		//取相差毫秒数的取绝对值
		millis = Math.abs(millis);

		//经过向上取整即可以得到相差天数。
		int days = (int) Math.ceil(millis / MILLISECONDS_IN_DAY);
		return days;
	}

	/**
	 * 比较两个日期的大小。
	 * @param date1 参考日期
	 * @param date2 比较日期
	 * @return 比较结果，1：参考日期大；-1：参考日期小；0：两个日期相等。
	 */
	public static int compare(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return 0;
		}
		long millis = date1.getTime() - date2.getTime();
		if (millis > 0) {
			return 1;
		}
		if (millis == 0) {
			return 0;
		}
		return -1;
	}

	/**
	 * 判断该日期所属年份是否为闰年
	 * <pre>调用toCalendar()方法并传入日期对象date获取日历对象calendar；
	 * 通过calendar的get()方法并且传入Calendar的常量YEAR获取当前年份year。
	 * 判断year能否被4整除并且不能被100整除 或者 能被400整除，若满足返回true，否则返回false。
	 * </pre>
	 * @param date 日期
	 * @return true：闰年 ； false：非闰年
	 */
	public static boolean isLeapYear(Date date) {
		Calendar calendar = toCalendar(date);
		int year = calendar.get(Calendar.YEAR);
		return ((GregorianCalendar) calendar).isLeapYear(year);

	}

	/**
	 * 判断年份是否为闰年
	 * @param year 年份
	 * @return true：闰年 ； false：非闰年
	 */
	public static boolean isLeapYear(int year) {
		GregorianCalendar calendar = new GregorianCalendar();
		return calendar.isLeapYear(year);
	}

	/**
	 * 获取当前时间字符串，格式为:HH:mm:ss，如：14:25:32。
	 * @return 时间字符串
	 */
	public static String nowTime() {
		return toStrWithFormat(now(), TIME_FORMAT_STR);
	}

	/**
	 * 根据时间段获取时间数组
	 * @param datePeriod
	 * @return
	 */
	public static String[] getDateArrayByPeriod(String datePeriod) {
		if (!StringUtils.isBlank(datePeriod)) {
			String[] dateArray = datePeriod.split(",");
			if (dateArray.length > 1) {
				Date beginDate = strToDate(dateArray[0], DATE_FORMAT_STR);
				Date endDate = strToDate(dateArray[1], DATE_FORMAT_STR);
				List<Date> datePeriodList = getDatesBetweenTwoDate(beginDate, endDate);
				dateArray = new String[datePeriodList.size()];
				for (int i = 0; i < datePeriodList.size(); i++) {
					Date date = datePeriodList.get(i);
					String dateStr = toDateStr(date);
					dateArray[i] = dateStr;
				}
			}
			return dateArray;
		}

		return null;
	}

	/**
	 * 根据开始时间和结束时间返回时间段内的时间集合
	 * @param beginDate
	 * @param endDate
	 * @return List
	 */
	public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
		List<Date> lDate = new ArrayList<Date>();
		lDate.add(beginDate);//把开始时间加入集合
		Calendar cal = Calendar.getInstance();
		//使用给定的 Date 设置此 Calendar 的时间
		cal.setTime(beginDate);
		boolean bContinue = true;
		while (bContinue) {
			//根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);
			// 测试此日期是否在指定日期之后
			if (endDate.after(cal.getTime())) {
				lDate.add(cal.getTime());
			} else {
				break;
			}
		}
		lDate.add(endDate);//把结束时间加入集合
		return lDate;
	}

	/**
	 * 获取日期
	 * <p>
	 * 仅保留日期的日期
	 * </p>
	 *
	 * @param date
	 * @return
	 */
	public static Date getDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date StringToDate(String dateStr, String formatStr) {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 判断两个日期是否是同一天
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		int d1, d2;
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		d1 = calendar1.get(Calendar.DATE);
		d2 = calendar2.get(Calendar.DATE);
		return isSameMoth(date1, date2) && d1 == d2;
	}

	/**
	 * 判断两个日期是否是同一月
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameMoth(Date date1, Date date2) {
		int moth1, moth2;
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		moth1 = calendar1.get(Calendar.MONTH);
		moth2 = calendar2.get(Calendar.MONTH);
		return isSameYear(date1, date2) && moth1 == moth2;
	}

	/**
	 * 判断两个日期是否是同一年
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameYear(Date date1, Date date2) {
		int year1, year2;
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		year1 = calendar1.get(Calendar.YEAR);
		year2 = calendar2.get(Calendar.YEAR);
		return year1 == year2;
	}

	/**
	 * 根据两次日期获取相差的天数。
	 * @param date1 日期
	 * @param date2 日期
	 * @return 两个日期相差的天数
	 */
	public static int getMinute(Date date1, Date date2) {

		double millis = date1.getTime() - date2.getTime();

		//取相差毫秒数的取绝对值
		millis = Math.abs(millis);

		//经过向上取整即可以得到相差分钟数。6000 = 1000* 60
		int minute = (int) Math.ceil(millis / 60000);
		return minute;
	}

	/**
	 * 将long 转string
	 * @param ms
	 * @return
	 */
	public static String formatLong2Str(long ms) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (ms < 1000) {
			fileSizeString = df.format((double) ms) + "毫秒";
		} else if (ms < 1000 * 60) {
			fileSizeString = df.format((double) ms / 1000) + "秒";
		} else if (ms < 1000 * 60 * 60) {
			fileSizeString = df.format((double) ms / 1000 / 60) + "分钟";
		} else {
			fileSizeString = df.format((double) ms / 1000 / 60 / 60) + "小时";
		}
		return fileSizeString;
	}

	/**
	 * 判断当前日期是星期几<br>
	 * <br>
	 * @param pTime 修要判断的时间<br>
	 * @return dayForWeek 判断结果<br>
	 * @Exception 发生异常<br>
	 */
	public static int dayForWeek(String pTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

}
