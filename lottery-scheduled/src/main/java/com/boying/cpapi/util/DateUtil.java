package com.boying.cpapi.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.util.Assert;

public class DateUtil {

	public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";

	public static final String PATTERN_STANDARD_2 = "yyyyMMddHHmmss";

	public static final String PATTERN_DATE = "yyyy-MM-dd";

	public static final String PATTERN_DATE2 = "yyyyMMdd";

	public static final String PATTERN_TIME = "HH:mm:ss";

	public static final String PATTERN_HOUR = "HH";

	public static final String PATTERN_MINUTE = "mm";

	/**
	 * 带时区的日期
	 */
	public static final String PATTERN_STANDARD_3 = "yyyy-MM-dd'T'HH:mm:ssZ";

	public static final String PATTERN_STANDARD_4 = "yyyy-MM-dd'T'HH:mm:ssX";

	public static final String PATTERN_STANDARD_5 = "yyyyMMdd HH:mm:ss";

	public static final String PATTERN_STANDARD_6 = "yyyy/MM/dd HH:mm:ss";

	public static final String PATTERN_STANDARD_7 = "yyyy-MM-dd'T'HH:mm:ss.S";// 例如2018-02-08T10:56:38.007

	public static String timestamp2String(Timestamp timestamp, String pattern) {
		if (timestamp == null) {
			throw new java.lang.IllegalArgumentException("timestamp null illegal");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = PATTERN_STANDARD;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date(timestamp.getTime()));
	}

	public static String date2String(java.util.Date date, String pattern) {
		if (date == null) {
			throw new java.lang.IllegalArgumentException("timestamp null illegal");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = PATTERN_STANDARD;
			;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static String dateString(java.util.Date date) {
		if (date == null) {
			throw new java.lang.IllegalArgumentException("timestamp null illegal");
		}
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_STANDARD);
		return sdf.format(date);
	}

	public static Timestamp currentTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	public static String currentTimestamp2String(String pattern) {
		return timestamp2String(currentTimestamp(), pattern);
	}

	public static Timestamp string2Timestamp(String strDateTime, String pattern) {
		if (strDateTime == null || strDateTime.equals("")) {
			throw new java.lang.IllegalArgumentException("Date Time Null Illegal");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = PATTERN_STANDARD;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(strDateTime);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return new Timestamp(date.getTime());
	}

	public static Date string2Date(String strDate, String pattern) {
		if (strDate == null || strDate.equals("")) {
			throw new RuntimeException("str date null");
		}
		if (pattern == null || pattern.equals("")) {
			pattern = DateUtil.PATTERN_DATE;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;

		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return date;
	}

	public static String stringToYear(String strDest) {
		if (strDest == null || strDest.equals("")) {
			throw new java.lang.IllegalArgumentException("str dest null");
		}

		Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return String.valueOf(c.get(Calendar.YEAR));
	}

	public static String stringToMonth(String strDest) {
		if (strDest == null || strDest.equals("")) {
			throw new java.lang.IllegalArgumentException("str dest null");
		}

		Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// return String.valueOf(c.get(Calendar.MONTH));
		int month = c.get(Calendar.MONTH);
		month = month + 1;
		if (month < 10) {
			return "0" + month;
		}
		return String.valueOf(month);
	}

	public static String stringToDay(String strDest) {
		if (strDest == null || strDest.equals("")) {
			throw new java.lang.IllegalArgumentException("str dest null");
		}

		Date date = string2Date(strDest, DateUtil.PATTERN_DATE);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// return String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (day < 10) {
			return "0" + day;
		}
		return "" + day;
	}
	/**
	 * 本月第一天
	 * @param c
	 * @return
	 */
	public static Date getFirstDayOfMonth() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = 1;
		c.set(year, month, day, 0, 0, 0);
		return c.getTime();
	}
	/**
	 * 本月最后一天
	 * @param c
	 * @return
	 */
	public static Date getLastDayOfMonth(Calendar c) {
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = 1;
		if (month > 11) {
			month = 0;
			year = year + 1;
		}
		c.set(year, month, day - 1, 0, 0, 0);
		return c.getTime();
	}
	/**
	 * 本周第一天
	 * @return
	 */
	public static Date getWeekStartDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date date = cal.getTime();
		return date;
	}

	public static String date2GregorianCalendarString(Date date) {
		if (date == null) {
			throw new java.lang.IllegalArgumentException("Date is null");
		}
		long tmp = date.getTime();
		GregorianCalendar ca = new GregorianCalendar();
		ca.setTimeInMillis(tmp);
		try {
			XMLGregorianCalendar t_XMLGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(ca);
			return t_XMLGregorianCalendar.normalize().toString();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new java.lang.IllegalArgumentException("Date is null");
		}

	}

	public static boolean compareDate(Date firstDate, Date secondDate) {
		if (firstDate == null || secondDate == null) {
			throw new java.lang.RuntimeException();
		}

		String strFirstDate = date2String(firstDate, "yyyy-MM-dd");
		String strSecondDate = date2String(secondDate, "yyyy-MM-dd");
		if (strFirstDate.equals(strSecondDate)) {
			return true;
		}
		return false;
	}

	public static Date getStartTimeOfDate(Date currentDate) {
		Assert.notNull(currentDate);
		String strDateTime = date2String(currentDate, "yyyy-MM-dd") + " 00:00:00";
		return string2Date(strDateTime, "yyyy-MM-dd hh:mm:ss");
	}

	public static Date getEndTimeOfDate(Date currentDate) {
		Assert.notNull(currentDate);
		String strDateTime = date2String(currentDate, "yyyy-MM-dd") + " 59:59:59";
		return string2Date(strDateTime, "yyyy-MM-dd hh:mm:ss");
	}

	/**
	 * 获取日期格式的字符串 add by maosheng 参数为整数则往后推多少天，参数为负数则往前面推多少天
	 */
	public static String getDateString(Date date, Integer year, Integer month, Integer day, Integer hour,
			Integer minute, Integer second, String pattern) {// 正数表示往后，负数表示往前
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);// 取时间
		if (year != null)
			calendar.add(Calendar.YEAR, year);
		if (month != null)
			calendar.add(Calendar.MONTH, month);
		if (day != null)
			calendar.add(Calendar.DATE, day);
		if (hour != null)
			calendar.add(Calendar.HOUR_OF_DAY, hour);
		if (minute != null)
			calendar.add(Calendar.MINUTE, minute);
		if (second != null)
			calendar.add(Calendar.SECOND, second);
		date = calendar.getTime();
		return formatter.format(date);
	}

	/**
	 * 参数为整数则往后推多少天，参数为负数则往前面推多少天 Description:
	 * 
	 * @author abo
	 * @date 2018年3月22日
	 * @param date
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @param pattern
	 * @return
	 */
	public static Date getDate(Date date, Integer year, Integer month, Integer day, Integer hour, Integer minute,
			Integer second, String pattern) {// 正数表示往后，负数表示往前
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);// 取时间
		if (year != null)
			calendar.add(Calendar.YEAR, year);
		if (month != null)
			calendar.add(Calendar.MONTH, month);
		if (day != null)
			calendar.add(Calendar.DATE, day);
		if (hour != null)
			calendar.add(Calendar.HOUR_OF_DAY, hour);
		if (minute != null)
			calendar.add(Calendar.MINUTE, minute);
		if (second != null)
			calendar.add(Calendar.SECOND, second);
		date = calendar.getTime();
		return date;
	}

	// 获取当前日期字符串
	public static String geCurrentDate(String pattern) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 将带时区信息的时间字符串转换为当前系统的时间字符串
	 */
	public static String dateToStamp(String datestr, String srcpattern, String destpattern) {
		if (srcpattern == null || srcpattern.equals("")) {
			srcpattern = PATTERN_STANDARD_4;
		}
		if (destpattern == null || destpattern.equals("")) {
			destpattern = PATTERN_STANDARD;
		}
		try {
			SimpleDateFormat srcsdf = new SimpleDateFormat(srcpattern);
			Date date1 = srcsdf.parse(datestr);
			long ts = date1.getTime();
			SimpleDateFormat destsdf = new SimpleDateFormat(destpattern);
			return destsdf.format(new java.util.Date(ts));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将long型时间戳转换为时间，传出格式为format
	 * 
	 * @param s
	 * @param format
	 * @return
	 */
	public static String longToDate(long s, String format) {
		String result = null;
		Date date = new Date(s * 1000);
		SimpleDateFormat sd = new SimpleDateFormat(format);
		result = sd.format(date);
		return result;
	}

	/**
	 * 将日期字符串转为时间戳（long）
	 * 
	 * @param datestr
	 *            日期字符串
	 * @param format
	 *            日期格式
	 * @param divisor
	 *            除数
	 * @return
	 */
	public static long dateStrToLong(String datestr, String pattern, int divisor) {
		if (pattern == null || pattern.equals("")) {
			pattern = DateUtil.PATTERN_STANDARD;
		}
		if (divisor < 1) {
			divisor = 1;
		}
		Date date = string2Date(datestr, pattern);
		return date.getTime() / divisor;
	}

	/**
	 * 
	 * @desc 当前字符串日期的最大月天数
	 * @author abo
	 * @date 2018年6月25日 下午2:20:11
	 * @param dateStr
	 *            2018-06-01
	 * @return
	 */
	public static int currentDateStrMonthDays(String dateStr) {
		int year = Integer.parseInt(dateStr.substring(0, 4));
		int month = Integer.parseInt(dateStr.substring(5, 7));
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return Integer.parseInt(lastDayOfMonth);
	}

}