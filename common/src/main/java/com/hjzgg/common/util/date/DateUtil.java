package com.hjzgg.common.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YY_MM_DD = "yy-MM-dd";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YY = "yy";
	public static final String MM = "MM";
	public static final String DD = "dd";

	/**
	 * 获取日期(yyyy-MM-dd)
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDate(String dateStr) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
			return format.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * 获取日期
	 * 
	 * @param dateStr
	 * @param dateFormat
	 * @return
	 */
	public static Date getDate(String dateStr, String dateFormat) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat);
			return format.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * 获取日期(yyyy-MM-dd)
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
		return format.format(date);
	}

	/**
	 * 获取格式化日期和时间
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateStr(Date date, String format) {
		SimpleDateFormat simple = new SimpleDateFormat(format);
		return simple.format(date);
	}

	/**
	 * 返回毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 减少天数
	 * 
	 * @param date
	 *            日期
	 * @return 返回相减后的日期
	 */
	public static Date diffDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) - ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 增加天数
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 减少小时
	 * 
	 * @param date
	 *            日期
	 * @return 返回相减后的日期
	 */
	public static Date diffDate4Hour(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) - ((long) hour) * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 增加小时
	 * 
	 * @param date
	 *            日期
	 * @param hour
	 *            小时
	 * @return 返回相加后的日期
	 */
	public static Date addDate4Hour(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) hour) * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 减少分钟
	 * 
	 * @param date
	 *            日期
	 * @return 返回相减后的日期
	 */
	public static Date diffDate4Minute(Date date, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) - ((long) minute) * 60 * 1000);
		return c.getTime();
	}

	/**
	 * 增加分钟
	 * 
	 * @param date
	 *            日期
	 * @param minute
	 * @return 返回相加后的日期
	 */
	public static Date addDate4Minute(Date date, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) minute) * 60 * 1000);
		return c.getTime();
	}

	/**
	 * 计算两个时间相差的秒数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long compareDate4Second(Date date1, Date date2) {
		long l = date2.getTime() - date1.getTime();
		return l / 1000;
	}

	/**
	 * 计算两个时间相差的分钟数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long compareDate4Minute(Date date1, Date date2) {
		long l = date2.getTime() - date1.getTime();
		return l / (60 * 1000);
	}

	/**
	 * 计算两个时间相差的小时数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long compareDate4Hour(Date date1, Date date2) {
		long l = date2.getTime() - date1.getTime();
		return l / (60 * 60 * 1000);
	}

	/**
	 * 计算两个时间差值的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long compareDate4Day(Date startDate, Date endDate) {
		long l = endDate.getTime() - startDate.getTime();
		long d = l / (24 * 60 * 60 * 1000);
		return d;
	}

	/**
	 * 获取本天最早时刻
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取本天最晚时刻
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	/**
	 * 根据毫秒数创建Date
	 * 
	 * @param time
	 * @return
	 */
	public static Date getDateByTime(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.getTime();
	}

	/**
	 * 获取年龄
	 * 
	 * @param birthday
	 * @param currentday
	 * @return
	 */
	public static int getAge(long birthday, long currentday) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(birthday);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(currentday);
		int y1 = cal.get(Calendar.YEAR);
		int y2 = cal2.get(Calendar.YEAR);
		int age = y2 - y1;
		cal.add(Calendar.YEAR, age);
		if (cal2.compareTo(cal) >= 0) {
			return age;
		} else {
			return age - 1;
		}
	}

	/**
	 * 根据身份证号获取生日
	 * 
	 * @param idCard
	 * @return
	 */
	public static Date getBirthdayByIdCard(String idCard) {
		String birthday = "";
		if (idCard.length() == 15) {
			birthday = "19" + idCard.substring(6, 12);
		} else if (idCard.length() == 18) {
			birthday = idCard.substring(6, 14);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
		try {
			return sdf.parse(birthday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
