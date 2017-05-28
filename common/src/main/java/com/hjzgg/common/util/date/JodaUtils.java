package com.hjzgg.common.util.date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * 日期工具类
 * 
 *
 */
public class JodaUtils {

	public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String HH_MM_SS = "HH:mm:ss";

	public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	public static final String YYMMDDHHMMSSSSS = "yyMMddHHmmssSSS";

	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String YYMMDDHHMMSS = "yyMMddHHmmss";

	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYMMDD = "yyMMdd";

	public static final String HHMMSSSSS = "HHmmssSSS";
	public static final String HHMMSS = "HHmmss";

	public static final String HOUR = "小时";
	public static final String MINITE = "分";

	/**
	 * 计算两个日期时间之间的相差天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer daysBetween(String startDate, String endDate) {
		try {
			DateTime sDate = new DateTime(JodaUtils.parseDate(startDate, JodaUtils.YYYY_MM_DD));
			DateTime eDate = new DateTime(JodaUtils.parseDate(endDate, JodaUtils.YYYY_MM_DD));

			Period p = new Period(sDate, eDate, PeriodType.days());
			return p.getDays();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 计算两个日期时间之间的相差小时数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int daysBetween(Date start, Date end) {
		DateTime startDateTime = new DateTime(start);
		DateTime endDateTime = new DateTime(end);
		Period p = new Period(startDateTime, endDateTime, PeriodType.days());
		return p.getDays();
	}

	/**
	 * 计算两个日期时间之间的相差小时数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int hoursBetween(Date start, Date end) {
		DateTime startDateTime = new DateTime(start);
		DateTime endDateTime = new DateTime(end);
		Period p = new Period(startDateTime, endDateTime, PeriodType.hours());
		return p.getHours();
	}

	/**
	 * 计算两个日期时间之间的相差分钟数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int minutesBetween(Date start, Date end) {
		DateTime startDateTime = new DateTime(start);
		DateTime endDateTime = new DateTime(end);
		Period p = new Period(startDateTime, endDateTime, PeriodType.minutes());
		return p.getMinutes();
	}

	/**
	 * 计算两个日期时间之间的差——精确到时分
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static String hoursAndMinutesBetween(Date start, Date end) {
		if (null == start || null == end) {
			return null;
		}

		// 计算剩余时间
		int minutesBetween = JodaUtils.minutesBetween(start, end);

		return JodaUtils.minutesToHourMinu(minutesBetween);
	}

	/**
	 * 将分钟转为标准格式
	 * 
	 * @param allMinutes
	 * @return
	 */
	public static String minutesToHourMinu(int allMinutes) {
		String remainTime = "";
		int hours = allMinutes / 60;
		int minutes = allMinutes % 60;
		if (hours == 0) {
			remainTime = minutes + MINITE;
		} else if (minutes == 0) {
			remainTime = hours + HOUR;
		} else {
			remainTime = hours + HOUR + minutes + MINITE;
		}
		return remainTime;
	}

	public static int monthsBetween(DateTime start, DateTime end) {
		Period p = new Period(start, end, PeriodType.months());
		return p.getMonths();
	}

	public static int daysBetween(DateTime start, DateTime end) {
		Period p = new Period(start, end, PeriodType.days());
		return p.getDays();
	}

	public static DateTime intToDateTime(Integer intDate) {
		return DateTimeFormat.forPattern(YYYYMMDD).parseDateTime(intDate.toString());
	}

	public static Integer dateTimeToInt(DateTime dateTime) {
		return Integer.valueOf(dateTime.toString(YYYYMMDD));
	}

	public static DateTime now() {
		return DateTime.now();
	}

	public static Date nowAsDate() {
		return new Date();
	}

	public static DateTime nowAsDateTime() {
		return DateTime.now();
	}

	public static LocalDate today() {
		return new LocalDate();
	}

	public static LocalDate parseLocalDate(String dateAsString, String format) {
		if (StringUtils.hasText(dateAsString)) {
			Date date = parseDate(dateAsString, format);
			return new LocalDate(date);
		}
		return null;
	}

	public static Date parseDate(String dateAsString, String format) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(dateAsString);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Date string can not be parsed:" + dateAsString, e);
		}
	}

	public static LocalDate parseLocalDate(String localDateAsString) {
		return parseLocalDate(localDateAsString, YYYY_MM_DD);
	}

	public static DateTime parseDateTime(String val) {
		return parseDateTime(val, YYYY_MM_DD_HH_MM_SS);
	}

	public static DateTime parseDateTime(String text, String patten) {
		if (!StringUtils.hasText(text)) {
			return now();
		}
		return DateTimeFormat.forPattern(patten).parseDateTime(text);
	}

	public static LocalDate yesterday() {
		return today().minusDays(1);
	}

	public static DateTime dayOfCurrentMonth(int day) {
		LocalDate today = JodaUtils.today();
		return new DateTime(today.getYear(), today.getMonthOfYear(), day, 0, 0, 0, 0);
	}

	/**
	 * 当前日期格式化后转换成String型
	 * 
	 * @param patten
	 * @return
	 */
	public static String nowDateToString(String patten) {
		try {
			return now().toString(patten);
		} catch (Exception e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + patten, e);
		}
	}

	/**
	 * 对时间进行转换为日期的字符串
	 * 
	 * @param date
	 * @param patten
	 * @return
	 */
	public static String dateToString(Date date, String patten) {
		try {
			if (null == date) {
				date = new Date();
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(patten);
			return dateFormat.format(date);
		} catch (Exception e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + patten, e);
		}
	}

	/**
	 * 当前日期格式化后转成long类型
	 * 
	 * @return
	 */
	public static Long nowDateToLong(String patten) {
		try {
			return Long.valueOf(now().toString(patten));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + patten, e);
		}
	}

	/**
	 * 当前日期格式化后转成Integer类型
	 * 
	 * @return
	 */
	public static Integer nowDateToInt(String patten) {
		try {
			return Integer.valueOf(now().toString(patten));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + patten, e);
		}
	}

	/**
	 * 当前时间格式化后转成Integer类型
	 * 
	 * @return
	 */
	public static Integer currentTimeToInt() {
		return nowDateToInt(JodaUtils.HHMMSS);
	}

	/**
	 * 当前日期格式化后转成Integer类型
	 * 
	 * @return
	 */
	public static Integer currentDateToInt() {
		return nowDateToInt(JodaUtils.YYYYMMDD);
	}

	/**
	 * 日期格式化转Long
	 * 
	 * @param date
	 * @param patten
	 * @return
	 */
	public static Long dateToLong(Date date, String patten) {
		try {
			return Long.valueOf(new DateTime(date).toString(patten));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + patten, e);
		}

	}

	/**
	 * 日期格式化转Integer
	 * 
	 * @param date
	 * @param patten
	 * @return
	 */
	public static Integer dateToInt(Date date, String patten) {
		try {
			return Integer.valueOf(new DateTime(date).toString(patten));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + patten, e);
		}
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static Integer dateToInt(Date date) {
		try {
			return Integer.valueOf(new DateTime(date).toString(YYYYMMDD));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + YYYYMMDD, e);
		}
	}

	/**
	 * 日期格式化转Integer
	 * 
	 * @param dateNum
	 * @return
	 */
	public static Date IntToDate(int dateNum) {
		try {

			SimpleDateFormat formatter = new SimpleDateFormat(YYYYMMDD);
			Date date1 = formatter.parse("" + dateNum);
			return date1;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + dateNum, e);
		} catch (ParseException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + dateNum, e);

		}
	}

	/**
	 * 日期格式化转Integer
	 * 
	 * @param dateNum
	 * @return
	 */
	public static Date IntToDate(int dateNum, String format) {
		try {

			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date date1 = formatter.parse("" + dateNum);
			return date1;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + dateNum, e);
		} catch (ParseException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + dateNum, e);

		}
	}

	/**
	 * 日期格式化转Integer
	 * 
	 * @param dateNum
	 * @return
	 */
	public static String IntToString(int dateNum, String format) {
		try {

			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date date = formatter.parse("" + dateNum);
			return new DateTime(date).toString(format);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + dateNum, e);
		} catch (ParseException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + dateNum, e);

		}
	}

	/**
	 * 日期格式化转Long
	 * 
	 * @param date
	 * @return
	 */
	public static Date LongTodate(Long date) {
		Date date1 = null;

		try {

			date1 = new DateTime(date).toDate();

		} catch (NumberFormatException e) {
		}
		return date1;
	}

	/**
	 * 当前日期格式化后转成Integer类型 默认日期格式为“yyyyMMdd”
	 * 
	 * @return
	 */
	public static Integer nowDateToInt() {
		try {
			return nowDateToInt(JodaUtils.YYYYMMDD);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + JodaUtils.YYYYMMDD);
		}
	}

	/**
	 * 返回当前日期加N天后的日期，在所在月是几号
	 * 
	 * @param days
	 * @return
	 */
	public static Integer currentTimePlusDayReturnDay(int days) {
		return now().plusDays(days).getDayOfMonth();
	}

	/**
	 * 日期加N天
	 * 
	 * @param text
	 * @param days
	 * @return
	 */
	public static String dateStrPlusDays(String text, int days) {
		return JodaUtils.parseDateTime(text, JodaUtils.YYYY_MM_DD).plusDays(days).toString(JodaUtils.YYYY_MM_DD);
	}

	/**
	 * 当前日期加N天的日期
	 * 
	 * @param days
	 * @param patten
	 * @return 按patten格式化，如20140505
	 */
	public static Integer currentTimePlusDay(int days, String patten) {
		return Integer.valueOf(now().plusDays(days).toString(patten));
	}

	/**
	 * 当前日期加N天的日期
	 * 
	 * @param days
	 * @return 按patten格式化，如20140505
	 */
	public static Integer currentTimePlusDay(int days) {
		return Integer.valueOf(now().plusDays(days).toString(JodaUtils.YYYYMMDD));
	}

	public static Date getCurrentDate() {
		LocalDate today = JodaUtils.today();
		return new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 0, 0, 0, 0).toDate();
	}

	public static Integer genLastRepayDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (day >= 1 && day < 29) {
			c.add(Calendar.MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			return dateToInt(c.getTime(), JodaUtils.YYYYMMDD);
		} else {
			c.add(Calendar.MONTH, 2);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			return dateToInt(c.getTime(), JodaUtils.YYYYMMDD);
		}
	}

	/**
	 * 
	 * 描述: 日期按月数增加计算通用方法
	 * <p>
	 * 创建人：jrzhangwei , 2014年5月20日 下午6:00:38
	 * </p>
	 * 
	 * @param dateAsInteger
	 *            日期
	 * @param plusMonths
	 *            需增加的月数
	 * @return
	 */
	public static Integer plusMonths(Integer dateAsInteger, Integer plusMonths) {
		if (null != dateAsInteger) {
			Date date = parseDate(String.valueOf(dateAsInteger), JodaUtils.YYYYMMDD);
			LocalDate localDate = new LocalDate(date);
			return Integer.valueOf(localDate.plusMonths(plusMonths).toString(JodaUtils.YYYYMMDD));
		}
		return null;
	}

	/**
	 * 获取指定时间是否在当前时间之后
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	public static boolean isAfterNow(Integer yyyyMMdd) {
		DateTime date = new DateTime(JodaUtils.parseDate(String.valueOf(yyyyMMdd), JodaUtils.YYYYMMDD));
		return date.isAfterNow();
	}

	/**
	 * 获取指定时间是否在当前时间之前
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	public static boolean isBeforeNow(Integer yyyyMMdd) {
		DateTime date = new DateTime(JodaUtils.parseDate(String.valueOf(yyyyMMdd), JodaUtils.YYYYMMDD));
		return date.isBeforeNow();
	}

	/**
	 * 获取rigger表的还款事件的触发日期
	 * 
	 * @return
	 */
	public static Integer getActivationDateForPayment() {
		return JodaUtils.getLastRepayDate(-2);
	}

	/**
	 * 获取最后还款日
	 * 
	 * @return
	 */
	public static Integer getLastRepayDate() {
		return JodaUtils.getLastRepayDate(0);
	}

	/**
	 * 获取下一个分期入账日期
	 * 
	 * @return
	 */
	public static Integer getNextBillingDate() {
		return JodaUtils.getLastRepayDate(1);
	}

	/**
	 * 获取最后还款日
	 * 
	 * @param ahead
	 *            提前量
	 * @return
	 */
	private static Integer getLastRepayDate(int ahead) {
		int DayOfMonth = JodaUtils.now().getDayOfMonth(); // 获取消费日期在所在月是几号
		Integer lastRepayDate = 0;
		if (DayOfMonth >= 1 && DayOfMonth <= 28) {// 1号到28号，还款日期加一个月
			lastRepayDate = Integer.valueOf(JodaUtils.now().plusMonths(1).plusDays(ahead).toString(JodaUtils.YYYYMMDD));
		} else if (DayOfMonth <= 31) {// 29，30，31号，还款日期为下下个月1号
			lastRepayDate = Integer.valueOf(JodaUtils.now().plusMonths(2).dayOfMonth().withMinimumValue().plusDays(ahead).toString(JodaUtils.YYYYMMDD));
		}
		return lastRepayDate;
	}

	/**
	 * 日期格式化转Integer
	 * 
	 * @param date
	 * @param patten
	 * @return
	 */
	public static Integer dateToInt(String date, String pattern) {
		try {
			if (date == null || pattern == null) {
				return null;
			}
			return Integer.valueOf(parseDateTime(date).toString(pattern));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + pattern, e);
		}
	}

	/**
	 * 当前日期格式化后转成Integer类型 默认日期格式为“yyyyMMdd”
	 * 
	 * @return
	 */
	public static Integer nowDateTimeToInt() {
		try {
			return Integer.valueOf(now().toString(JodaUtils.HHMMSS));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + JodaUtils.HHMMSS);
		}
	}

	/**
	 * 当前时间格式化后转成Integer类型
	 * 
	 * @param dateTime
	 *            时间
	 * @param pattern
	 *            格式类型
	 */
	public static Integer parseDateTimeToInt(DateTime dateTime, String pattern) {
		try {
			return Integer.valueOf(dateTime.toString(pattern));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("patten string can not be parsed:" + JodaUtils.HHMMSS);
		}
	}

	/**
	 * 
	 * <p>
	 * 获取日期所在月的天数
	 * 
	 * @param date
	 * @return
	 * @author Administrator
	 * @date 2016年7月18日 下午2:31:44
	 */
	public static int getDaysOfMonth(Date date) {
		return new DateTime(date).dayOfMonth().getMaximumValue();
	}

	/**
	 * 
	 * <p>
	 * 获取日期所在月的天数
	 * 
	 * @param date
	 * @return
	 * @author Administrator
	 * @date 2016年7月18日 下午2:31:44
	 */
	/*
	 * public static boolean isSameMonth(Date startDate, Date endDate) { new
	 * DateTime(startDate).getMonthOfYear() == }
	 */

}
