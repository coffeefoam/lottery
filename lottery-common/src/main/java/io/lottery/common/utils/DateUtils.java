/**
 * Copyright 2018 
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.lottery.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 日期处理
 * 
 * @author chenshun
 * @email
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
	/** 时间格式(yyyy) */
	public final static String DATE_YEAR = "yyyy";
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期格式化 日期格式为：yyyy-MM-dd
	 * 
	 * @param date
	 *            日期
	 * @return 返回yyyy-MM-dd格式日期
	 */
	public static String format(Date date) {
		return format(date, DATE_PATTERN);
	}

	/**
	 * 日期格式化 日期格式为：yyyy-MM-dd
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式，如：DateUtils.DATE_TIME_PATTERN
	 * @return 返回yyyy-MM-dd格式日期
	 */
	public static String format(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return null;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param strDate
	 *            日期字符串
	 * @param pattern
	 *            日期的格式，如：DateUtils.DATE_TIME_PATTERN
	 */
	public static Date stringToDate(String strDate, String pattern) {
		if (StringUtils.isBlank(strDate)) {
			return null;
		}

		DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
		return fmt.parseLocalDateTime(strDate).toDate();
	}

	/**
	 * 根据周数，获取开始日期、结束日期
	 * 
	 * @param week
	 *            周期 0本周，-1上周，-2上上周，1下周，2下下周
	 * @return 返回date[0]开始日期、date[1]结束日期
	 */
	public static Date[] getWeekStartAndEnd(int week) {
		DateTime dateTime = new DateTime();
		LocalDate date = new LocalDate(dateTime.plusWeeks(week));

		date = date.dayOfWeek().withMinimumValue();
		Date beginDate = date.toDate();
		Date endDate = date.plusDays(6).toDate();
		return new Date[] { beginDate, endDate };
	}

	/**
	 * 对日期的【秒】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param seconds
	 *            秒数，负数为减
	 * @return 加/减几秒后的日期
	 */
	public static Date addDateSeconds(Date date, int seconds) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusSeconds(seconds).toDate();
	}

	/**
	 * 对日期的【分钟】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param minutes
	 *            分钟数，负数为减
	 * @return 加/减几分钟后的日期
	 */
	public static Date addDateMinutes(Date date, int minutes) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusMinutes(minutes).toDate();
	}

	/**
	 * 对日期的【小时】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param hours
	 *            小时数，负数为减
	 * @return 加/减几小时后的日期
	 */
	public static Date addDateHours(Date date, int hours) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusHours(hours).toDate();
	}

	/**
	 * 对日期的【天】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param days
	 *            天数，负数为减
	 * @return 加/减几天后的日期
	 */
	public static Date addDateDays(Date date, int days) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusDays(days).toDate();
	}

	/**
	 * 对日期的【周】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param weeks
	 *            周数，负数为减
	 * @return 加/减几周后的日期
	 */
	public static Date addDateWeeks(Date date, int weeks) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusWeeks(weeks).toDate();
	}

	/**
	 * 对日期的【月】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param months
	 *            月数，负数为减
	 * @return 加/减几月后的日期
	 */
	public static Date addDateMonths(Date date, int months) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusMonths(months).toDate();
	}

	/**
	 * 对日期的【年】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param years
	 *            年数，负数为减
	 * @return 加/减几年后的日期
	 */
	public static Date addDateYears(Date date, int years) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusYears(years).toDate();
	}

	/*
	 * 将时间转换为时间戳
	 */
	public static long dateToStamp(String s) throws ParseException {
		if (StringUtils.isBlank(s)) {
			return 0;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		return ts;
	}

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	/**
	 * 验证，是否是有效的时间格式,默认格式化格式为 yyyy-MM-dd
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidDate(String str, String... formtter) {
		boolean convertSuccess = true;
		SimpleDateFormat format = null;
		if (formtter != null && formtter.length > 0) {
			format = new SimpleDateFormat(formtter[0]);
		} else {
			format = new SimpleDateFormat("yyyy-MM-dd");
		}

		try {
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}
	
	
	/**
	  * 获取当前日期是星期几<br>
     * 
     * @param dt
	 * @return 
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }



}
