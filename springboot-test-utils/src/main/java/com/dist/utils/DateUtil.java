package com.dist.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * @description 日期工具类
 * @author King_wangyao
 * @date 2010-5-20
 * @version 1.0.0
 * 
 */
public final class DateUtil {

	private static final Calendar cal = Calendar.getInstance();

	/**
	 * yyyy-MM-dd
	 */
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(
			"HH:mm:ss");
	
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final SimpleDateFormat DATE_TIME_FORMATTER_1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	/**
	 * yyyyMMdd
	 */
	public static final SimpleDateFormat COMPACT_DATE_FORMATTER = new SimpleDateFormat(
			"yyyyMMdd");
	/**
	 * yyyyMMddHHmmss
	 */
	public static final SimpleDateFormat COMPACT_DATE_TIME_FORMATTER = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	/**
	 * HHmmss
	 */
	public static final SimpleDateFormat COMPACT_TIME_FORMATTER = new SimpleDateFormat(
			"HHmmss");

	/**
	 * 构造一个Date对象
	 * 
	 * @see #newDate(int,int,int,int,int,int)
	 */
	public static Date newDate(int year, int month, int day) {
		return newDate(year, month, day, 0, 0, 0);
	}

	/**
	 * 构造一个Date对象
	 * 
	 * @param month
	 *            从0开始,表示一月份
	 * @param day
	 *            从1开始，表示第一天
	 */
	public static Date newDate(int year, int month, int day, int hour,
			int minute, int second) {
		synchronized (cal) {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, second);
			return cal.getTime();
		}
	}

	/**
	 * 返回当前时间
	 */
	public static Date now() {
		synchronized (cal) {
			cal.setTimeInMillis(System.currentTimeMillis());
			return cal.getTime();
		}
	}

	/**
	 * 返回当前时间，以2012-05-20 12:34:56格式输出
	 */
	public static String getSystemTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
	}

	/**
	 * 返回明天的日期对象
	 */
	public static Date tomorrow() {
		return tomorrow(now());
	}

	/**
	 * 取得date的下一天
	 */
	public static Date tomorrow(Date date) {
		synchronized (cal) {
			cal.setTime(date);
			cal.add(Calendar.DATE, 1);
			return cal.getTime();
		}
	}

	/**
	 * 返回昨天的日期对象
	 */
	public static Date yesterday() {
		return yesterday(now());
	}

	/**
	 * 取得date的前一天
	 */
	public static Date yesterday(Date date) {
		synchronized (cal) {
			cal.setTime(date);
			cal.add(Calendar.DATE, -1);
			return cal.getTime();
		}
	}

	/**
	 * 取得date的后n天
	 */
	public static Date afterDay(Date date, int n) {
		synchronized (cal) {
			cal.setTime(date);
			cal.add(Calendar.DATE, n);
			return cal.getTime();
		}
	}

	/**
	 * 取得日期的某个字段 如：getField(date,Calendar.YEAR)
	 */
	public static int getField(Date d, int field) {
		synchronized (cal) {
			cal.setTime(d);
			return cal.get(field);
		}
	}

	/**
	 * 设置日期的某个字段
	 */
	public static Date setField(Date d, int field, int value) {
		synchronized (cal) {
			cal.setTime(d);
			cal.set(field, value);
			return cal.getTime();
		}
	}

	/**
	 * 将日期对象包含的时间信息清除 即得到的时间为 00:00:00
	 */
	public static Date clearTime(Date d) {
		synchronized (cal) {
			cal.setTime(d);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		}
	}

	/**
	 * 解析format指定的格式的日期
	 */
	public static Date parseDate(String date, String format) {
		return parseDate(date, new SimpleDateFormat(format));
	}

	/**
	 * 解析format指定的格式的日期
	 */
	public static Date parseDate(String date, SimpleDateFormat format) {
		try {
			return format.parse(date);
		} catch (ParseException ex) {
			throw new RuntimeException("Date parse error: " + date
					+ " , expected format is " + format.toPattern(), ex);
		}
	}

	/**
	 * 将日期以format指定的格式输出
	 */
	public static String formatDate(Date date, SimpleDateFormat format) {
		if (date == null) {
			return "";
		}
		return format.format(date);
	}

	/**
	 * 将日期以format指定的格式输出
	 */
	public static String formatDate(Date date, String format) {
		if (date == null) {
			return "";
		}
		return formatDate(date, new SimpleDateFormat(format));
	}

	/**
	 * 将日期以2007-01-30的格式输出
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return DATE_FORMATTER.format(date);
	}

	/**
	 * 将日期以20070130的格式输出
	 */
	public static String formatCompactDate(Date date) {
		if (date == null) {
			return "";
		}
		return COMPACT_DATE_FORMATTER.format(date);
	}

	/**
	 * 解析2007-01-30的格式的日期
	 */
	public static Date parseDate(String date) {
		try {
			return DATE_FORMATTER.parse(date);
		} catch (ParseException ex) {
			throw new RuntimeException("Date parse error: " + date
					+ " , expected format is " + DATE_FORMATTER.toPattern(), ex);
		}
	}

	/**
	 * 解析20070130的格式的日期
	 */
	public static Date parseCompactDate(String date) {
		try {
			return COMPACT_DATE_FORMATTER.parse(date);
		} catch (ParseException ex) {
			throw new RuntimeException("Date parse error: " + date
					+ " , expected format is "
					+ COMPACT_DATE_FORMATTER.toPattern(), ex);
		}
	}

	/**
	 * 将日期以2007-01-30 08:30:59的格式输出
	 */
	public static String formatDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return DATE_TIME_FORMATTER.format(date);
	}

	/**
	 * 将日期以20070130083059的格式输出
	 */
	public static String formatCompactDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return COMPACT_DATE_TIME_FORMATTER.format(date);
	}

	/**
	 * 解析2007-01-30 08:30:59的格式的日期
	 */
	public static Date parseDateTime(String dateTime) {
		try {
			return DATE_TIME_FORMATTER.parse(dateTime);
		} catch (ParseException ex) {
			throw new RuntimeException("Date parse error: " + dateTime
					+ " , expected format is "
					+ DATE_TIME_FORMATTER.toPattern(), ex);
		}
	}

	/**
	 * 解析20070130083059的格式的日期
	 */
	public static Date parseCompactDateTime(String dateTime) {
		try {
			return COMPACT_DATE_TIME_FORMATTER.parse(dateTime);
		} catch (ParseException ex) {
			throw new RuntimeException("Date parse error: " + dateTime
					+ " , expected format is "
					+ COMPACT_DATE_TIME_FORMATTER.toPattern(), ex);
		}
	}

	/**
	 * 将日期以08:30:59的格式输出
	 */
	public static String formatTime(Date date) {
		if (date == null) {
			return "";
		}
		return TIME_FORMATTER.format(date);
	}

	/**
	 * 将日期以083059的格式输出
	 */
	public static String formatCompactTime(Date date) {
		if (date == null) {
			return "";
		}
		return COMPACT_TIME_FORMATTER.format(date);
	}

	/**
	 * 解析08:30:59的格式的日期
	 */
	public static Date parseTime(String time) {
		try {
			return TIME_FORMATTER.parse(time);
		} catch (ParseException ex) {
			throw new RuntimeException("Date parse error: " + time
					+ " , expected format is " + TIME_FORMATTER.toPattern(), ex);
		}
	}

	/**
	 * 解析083059的格式的日期
	 */
	public static Date parseCompactTime(String time) {
		try {
			return COMPACT_TIME_FORMATTER.parse(time);
		} catch (ParseException ex) {
			throw new RuntimeException("Date parse error: " + time
					+ " , expected format is "
					+ COMPACT_TIME_FORMATTER.toPattern(), ex);
		}
	}

	/**
	 * 比较现在的时间，是否比现在的时间大
	 */
	public static boolean compareDate(Date date) {
		Date nowdate = new Date();
		long time1 = dateConInt(nowdate);
		long time2 = dateConInt(date);
		if (time1 >= time2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 把时间转正INT型
	 */
	public static long dateConInt(Date date) {
		String time = DATE_TIME_FORMATTER.format(date);
		time = time.replaceAll("-", "");
		time = time.replaceAll(" ", "");
		time = time.replaceAll(":", "");
		return Long.parseLong(time);
	}

	/**
	 * 返回日期范围，含首尾日期 [beginDate,endDate]
	 */
	public static DateRange newDateRange(Date beginDate, Date endDate) {
		return new DateRange(beginDate, endDate);
	}

	/**
	 * 日期范围没有synchronized，不是线程安全的
	 */
	public static class DateRange {

		private final Calendar cal = Calendar.getInstance();
		private Date beginDate;
		private Date endDate;

		DateRange(Date beginDate, Date endDate) {
			this.beginDate = clearTime(beginDate);
			this.endDate = clearTime(endDate);
			cal.setTime(yesterday(this.beginDate));
		}

		/**
		 * 当前日期
		 */
		private Date current() {
			return cal.getTime();
		}

		/**
		 * 下一天 请先使用hasNext()进行判断
		 * 
		 * @exception RuntimeException
		 *                如果到达了日期范围结尾，则抛出RuntimeException。
		 */
		public Date next() {
			if (hasNext()) {
				cal.add(Calendar.DATE, 1);
				return cal.getTime();
			} else {
				throw new RuntimeException("over range!");
			}
		}

		/**
		 * 是否有下一天
		 */
		public boolean hasNext() {
			return current().before(endDate);
		}
	}

	/**
	 * 日期判断，判断当前date字符串是不是属于日期的格式标准 格式：2010-5-20
	 */
	public static boolean isDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
		try {
			format.parse(date);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 日期转换，将日期向下延迟一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateFormat(String date) {
		if (date != null && StringUtils.isNotEmpty(date)) {
			Date temp = parseDate(date);
			Date tempdate = tomorrow(temp);
			String result = formatDate(tempdate);
			return result;
		}
		return null;
	}

	/**
	 * 时间日期比较
	 */
	public static Boolean checkTimeout(String smsShangbanTime,
			String webShangbanTime, Integer timeout) {
		Calendar sms = Calendar.getInstance();
		sms.setTime(parseDateTime(smsShangbanTime));
		Long smsLong = sms.getTimeInMillis();

		Calendar web = Calendar.getInstance();
		web.setTime(parseDateTime(webShangbanTime));
		Long webLong = web.getTimeInMillis();

		int flag = webLong.intValue() - smsLong.intValue() - timeout * 60
				* 1000;
		if (flag > 0)
			return true;
		else
			return false;
	}

	public static Date C2D(String date) {
		if (StringUtils.isNotEmpty(date)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
						Locale.US);
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Date C2DS(String date) {
		if (StringUtils.isNotEmpty(date)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss", Locale.US);
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 转换2012-12-04 00:00:00.000时间类型为2012-12-04
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(String date) {
		String formatDate = "";
		if (!Utils.isEmpty(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				formatDate = sdf.format(new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss").parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return formatDate;

	}
	
	/**
	 * 用指定的格式转换
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(String date,SimpleDateFormat format) {
		String regex ="\\d{4}(\\-)\\d{1,2}\\1\\d{1,2}";
		String formatDate = "";
		if (!Utils.isEmpty(date)) {
			try {
				if(date.matches(regex)){
					//DATE_FORMATTER.format(date)
					return format.format(DATE_FORMATTER.parse(date));
				}else{
					return DATE_TIME_FORMATTER.format(DATE_TIME_FORMATTER.parse(date));
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return formatDate;
	}
	
	/**
	 * 转换2012-12-04时间类型为2012-12-04 00:00:00.000
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate1(String date) {
		String formatDate = "";
		if (!Utils.isEmpty(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				formatDate = sdf.format(new SimpleDateFormat(
						"yyyy-MM-dd").parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return formatDate;

	}
	
	/**
	 * 转换2012-12-04时间类型为2012/12/04
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate3(String date) {
		String formatDate = "";
		if (!Utils.isEmpty(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			try {
				formatDate = sdf.format(new SimpleDateFormat(
						"yyyy-MM-dd").parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return formatDate;

	}
	
	/**
	 * 转换15-11-2014时间类型为2014-11-15
	 * @param date
	 * @return
	 */
	public static String formatDate4(String date) {
		String formatDate = "";
		if (!Utils.isEmpty(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			try {
				formatDate = DATE_FORMATTER.format(sdf.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return formatDate;

	}
	
	/**
	 * 转换2012-12-04 00:00:00.000时间类型为想要的格式
	 * @param date
	 * @param format 指定的格式如（yyyy-MM-dd HH:mm）
	 * @return
	 */
	public static String formatDate(String date, String format) {
		String formatDate = "";
		if (!Utils.isEmpty(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				formatDate = sdf.format(new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss").parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return formatDate;

	}

	/**
	 * 求2个日期之间的天数
	 * 
	 * @param StartDay 开始日期
	 * @param endDay 结束日期
	 * @param IncludeEndFlag 是否把结束当天算在内,Y:是,N:否
	 * @return
	 * @throws Exception
	 */
	public static long DayCountBetweenDays(String StartDay, String endDay,
			String IncludeEndFlag) throws Exception {
		long DAY = 24L * 60L * 60L * 1000L;
		long CountBetweenDay = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = df.parse(StartDay);
		Date d2 = df.parse(endDay);
		CountBetweenDay = (d2.getTime() - d1.getTime()) / DAY;
		if (IncludeEndFlag == "Y") {
			CountBetweenDay++;
		}
		return CountBetweenDay;

	}

	/**
	 * 求2个时间之间的小时数
	 * 
	 * @param StartTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws Exception
	 */
	public static double hourCountBetweenDays(String StartTime, String endTime){
		final long HOUR = 60L * 60L * 1000L;
		double countBetweenDay = 0.0;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date d1 = df.parse(StartTime);
			Date d2 = df.parse(endTime);
			countBetweenDay = (d2.getTime() - d1.getTime()) * 1.0 / HOUR;
		} catch (Exception e) {
			System.out.println(e);
			countBetweenDay = 0.0;
		}
		return countBetweenDay;

	}
	
	
	/**
     *  求2个日期之间的相差几个月
     * @param date1 <String>
     * @param date2 <String>
     * @return int
     * @throws ParseException
     */
    public static long getMonthSpace(String startDate, String endDate)
            throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		long monthday;
		Date startDate1 = f.parse(startDate);
		// 开始时间与今天相比较
		Date endDate1 = f.parse(endDate);

		Calendar starCal = Calendar.getInstance();
		starCal.setTime(startDate1);

		int sYear = starCal.get(Calendar.YEAR);
		int sMonth = starCal.get(Calendar.MONTH);
		int sDay = starCal.get(Calendar.DATE);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate1);
		int eYear = endCal.get(Calendar.YEAR);
		int eMonth = endCal.get(Calendar.MONTH);
		int eDay = endCal.get(Calendar.DATE);

		monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));

		if (sDay < eDay) {
			monthday = monthday + 1;
		}
		return monthday;

    }
    
    /**
     * 根据小时取到天数  一天按8小时计算 
     * @param hours
     * @return
     */
    public static double getDayByHours(double hours){
    	if(hours<=0){
    		hours=-1*hours;
    	}
    	double day =0.0;
    	double yushu=hours%8.0;
    	if(yushu == 0){
    		day=hours/8.0;
    	}else{
    		day=(hours -yushu)/8.0;
    		if(yushu>4){
    			day+=1;
    		}else{
    			day+=0.5;
    		}
    	}
    	return  day;
    	
    }
    
	 /**
	  *  计算时间
	 * @param time
	 * @return
	 */
	public static String getTime(long time) {
		  String str = "" ;
		  time = time / 1000;
		  int s = (int) (time % 60);
		  int m = (int) (time / 60 % 60);
		  int h = (int) (time / 3600);
		  str = h + "小时" + m + "分" + s +"秒";  
		  return str ;
		 }

	/**
	 * 测试...
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws InterruptedException,
			ParseException {
		// String _inputName = "2012-05-05";
		// System.err.println(isDate(_inputName.trim()));
		// if (isDate(_inputName.trim())) {
		// System.err.println(parseDate(_inputName，.trim()));
		// }
		try {
			System.out.println(formatDate4("01-11-2014"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	  *  计算时间
	 * @param time
	 * @return
	 */
	public static String getTimeEn(long time) {
		  String str = "" ;
		  time = time / 1000;
		  int s = (int) (time % 60);
		  int m = (int) (time / 60 % 60);
		  int h = (int) (time / 3600);
		  str = h + "h " + m + "m " + s +"s";  
		  return str ;
	}
}
