package com.boying.cpapi.util;

import io.lottery.common.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @desc 彩票工具类
 * @author abo
 * @date 2018年6月27日 下午1:42:43
 *
 */
public class LotteryUtil {

	/**
	 * 
	 * @desc 根据球数修改插入的字段值
	 * @author abo
	 * @date 2018年6月27日 下午1:31:25
	 * @param openCode
	 * @param lottId
	 * @return 例如：彩种如果是3个球，那么插入的字段值就是3,1,2
	 */
	public static String getInsertNumByLottIdAndOpenCode(String openCode, String lottId) {
		// 快乐8是20+1，中间最后一个数字是用+号隔开所以这里做了一下处理
		if (lottId.equals(LotteryConstant.KL8)) {
			openCode = openCode.replace("+", ",");
		}
		String[] array = openCode.split(",");
		String insertNumValueStr = "";
		int num = LotteryConstant.lotteryNumMap.get(lottId);
		if (num > 0) {
			for (int i = 0; i < array.length; i++) {
				insertNumValueStr += array[i] + ',';
			}
			// 去掉最后的逗号
			if (StringUtils.isNotBlank(insertNumValueStr)) {
				insertNumValueStr = insertNumValueStr.substring(0, insertNumValueStr.length() - 1);
			}
		}
		return insertNumValueStr;
	}

	/**
	 * 
	 * Description: 修改彩种编码
	 * 
	 * @author abo
	 * @date 2018年5月2日
	 * @param lottId
	 * @return
	 */
	public static String updateLottId(String lottId) {
		if (lottId.equals("bjpk10")) {
			lottId = "bjsc";
		} else if (lottId.equals("mlaft")) {
			lottId = "xyft";
		} else if (lottId.equals("fc3d")) {
			lottId = "fcsd";
		} else if (lottId.equals("kspk10")) {
			lottId = "kssc";
		} else if (lottId.equals("cqklsf")) {
			lottId = "xync";
		} else if (lottId.equals("bjkl8")) {
			lottId = "kl8";
		}
		return lottId;
	}

	/**
	 * 
	 * Description: 反向修改彩种编码
	 * 
	 * @author abo
	 * @date 2018年5月2日
	 * @param lottId
	 * @return
	 */
	public static String backpdateLottId(String lottId) {
		if (lottId.equals("bjsc")) {
			lottId = "bjpk10";
		} else if (lottId.equals("xyft")) {
			lottId = "mlaft";
		} else if (lottId.equals("xync")) {
			lottId = "cqklsf";
		} else if (lottId.equals("kl8")) {
			lottId = "bjkl8";
		}
		return lottId;
	}

	/**
	 * @desc 路珠统计今天次数
	 * @author xg
	 * @param strNum
	 * @param map
	 * @param time
	 * @param type
	 * @author 2018年7月7日
	 */
	public static void countNum(String strNum, Map map, String time, String type, String lottId) {
		Date date = new Date();
		String nowTime = countDayOfNow(lottId);
		if (nowTime.equals(time)) {
			if (map.get(strNum + "-" + type) == null) {
				map.put(strNum + "-" + type, 0);
			}
			map.put(strNum + "-" + type, Integer.parseInt(map.get(strNum + "-" + type).toString()) + 1);
		} else {
			if (map.get(strNum + "-" + type) == null) {
				map.put(strNum + "-" + type, 0);
			}
		}
	}

	/**
	 * @desc 统计初始化
	 * @author xg
	 * @param strNum
	 * @param map
	 * @param type
	 * @author 2018年8月15日
	 */
	public static void initNum(String strNum, Map map, String type) {
		if (map.get(strNum + "-" + type) == null) {
			map.put(strNum + "-" + type, 0);
		}
	}

	/**
	 * 
	 * @desc 数据库查询出来的开奖号码封装成一个list对象
	 * @author abo
	 * @date 2018年7月6日 下午7:06:41
	 * @param currentDayList
	 * @param num
	 * @return
	 */
	public static List<List<Integer>> mapToList(List<Map<String, Object>> currentDayList, int num) {
		List<List<Integer>> intArrays = new ArrayList<List<Integer>>();
		List<Integer> temp = null;
		for (Map m : currentDayList) {
			temp = new ArrayList<Integer>();
			for (int i = 0; i < num; i++) {
				temp.add(Integer.parseInt(m.get("num" + (i + 1)).toString()));
			}
			intArrays.add(temp);
		}
		return intArrays;
	}

	/**
	 * 获取某形态出现的次数 ps:必须从该形态开始，并且出现三次以上才返回
	 * 
	 * @author ms
	 * @date 2018年7月31日 下午12:39:22
	 */
	public static int getDataShape(List list, String key) {
		int toalcount = 0;
		int index = key.length() * 3 - 1;
		boolean flag = true;
		do {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				if (i > index)
					break;
				sb.append(list.get(i));
			}
			if (sb.toString().startsWith(key)) {
				int count = getSubContineCount(sb.toString(), key);
				if (count <= toalcount) {
					break;
				} else {
					toalcount = count;
					index = index + 2;
				}
			} else {
				break;
			}
		} while (flag);
		return toalcount;
	}

	/**
	 * @Title: 获取字符在某字符串中连续出现的次数（而且必须从第一位开始计算）
	 * @author ms
	 * @date 2018年7月31日 下午12:37:40
	 */
	public static int getSubContineCount(String str, String key) {
		int count = 0;
		int index = 0;
		int nextindex = 0;
		while ((index = str.indexOf(key, index)) != -1) {
			index = index + key.length();
			nextindex = nextindex + key.length();
			if (index == nextindex)
				count++;
			else
				break;
		}
		return count;
	}

	/**
	 * @Title: 获取字符在某字符串中出现的次数
	 * @author ms
	 * @date 2018年7月31日 下午12:37:40
	 */
	public static int getSubCount(String str, String key) {
		int count = 0;
		int index = 0;
		while ((index = str.indexOf(key, index)) != -1) {
			index = index + key.length();
			count++;
		}
		return count;
	}

	/**
	 * 
	 * @desc 根据彩票id获取彩种的开奖号码球的数组
	 * @author abo
	 * @date 2018年7月30日 下午2:41:01
	 * @param lottId
	 * @return
	 */
	public static String[] numArrayByLottId(String lottId) {
		int min = LotteryConstant.lotteryNumberRangeMap.get(lottId + "S");
		int max = LotteryConstant.lotteryNumberRangeMap.get(lottId + "B");
		int length = max;
		String[] array = new String[length];
		if (lottId.contains("ssc")) {
			length += 1;
		} else if (lottId.equals(LotteryConstant.JSK3)) {
			length = length * 3;
		}
		array = new String[length];
		for (int i = 0; i < length; i++) {
			array[i] = min + i + "";
		}
		return array;
	}

	/**
	 * 如果今天的奖开完了，获取下期开奖的期数是多少
	 * 
	 * @author ms
	 * @date 2018年8月15日 下午1:04:39
	 */
	public static Long getNextPeriods(String lottId, String dqqs) {
		String today = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE2);
		String nextday = DateUtil.getDateString(new Date(), null, null, 1, null, null, null, DateUtil.PATTERN_DATE2);
		Long xqqs = Long.valueOf(nextday + "001");
		;
		// 北京赛车和快乐8的期号不是根据日期来计算
		if (LotteryConstant.BJSC.equals(lottId)) {
			xqqs = Long.valueOf(dqqs) + 1;
		}
		if (LotteryConstant.PCDD.equals(lottId)) {
			xqqs = Long.valueOf(dqqs) + 1;
		}
		if (LotteryConstant.KL8.equals(lottId)) {
			xqqs = Long.valueOf(dqqs) + 1;
		}
		// 11选5从01开始
		if (LotteryConstant.GD11X5.equals(lottId)) {
			xqqs = Long.valueOf(nextday + "01");
		}
		if (LotteryConstant.JX11X5.equals(lottId)) {
			xqqs = Long.valueOf(nextday + "01");
		}
		// 新疆时时彩和幸运飞艇的开奖有跨天
		if (LotteryConstant.XJSSC.equals(lottId)) {
			xqqs = Long.valueOf(today + "001");
		}
		if (LotteryConstant.XYFT.equals(lottId)) {
			xqqs = Long.valueOf(today + "001");
		}
		// 其他从001开始
		if (LotteryConstant.CQSSC.equals(lottId)) {
			xqqs = Long.valueOf(nextday + "001");
		}
		if (LotteryConstant.TJSSC.equals(lottId)) {
			xqqs = Long.valueOf(nextday + "001");
		}
		if (LotteryConstant.GDKLSF.equals(lottId)) {
			xqqs = Long.valueOf(nextday + "001");
		}
		if (LotteryConstant.JSK3.equals(lottId)) {
			xqqs = Long.valueOf(nextday + "001");
		}
		if (LotteryConstant.XYNC.equals(lottId)) {
			xqqs = Long.valueOf(nextday + "001");
		}
		return xqqs;
	}

	/**
	 * 根据彩中获取彩中开奖的间隔时间毫秒数
	 * @author ms
	 * @date 2018年8月23日 上午10:34:44
	 */
	public static Long getLotteryIntervalTime(String lottId) {
		if (LotteryConstant.BJSC.equals(lottId)) {
			return TimeDict.Five_Minutes;
		} else if (LotteryConstant.KL8.equals(lottId)) {
			return TimeDict.Five_Minutes;
		} else if (LotteryConstant.GD11X5.equals(lottId)) {
			return TimeDict.Ten_Minutes;
		} else if (LotteryConstant.JX11X5.equals(lottId)) {
			return TimeDict.Ten_Minutes;
		} else if (LotteryConstant.XJSSC.equals(lottId)) {
			return TimeDict.Ten_Minutes;
		} else if (LotteryConstant.CQSSC.equals(lottId)) {
			// 重庆时时彩规则特殊 全天120期,上午10:00—22:00,十分钟一期 22:00-02:00 五分钟一期
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if (hour > 1 && hour < 22) {
				return TimeDict.Ten_Minutes;
			} else {
				return TimeDict.Five_Minutes;
			}
		} else if (LotteryConstant.TJSSC.equals(lottId)) {
			return TimeDict.Ten_Minutes;
		} else if (LotteryConstant.XYFT.equals(lottId)) {
			return TimeDict.Five_Minutes;
		} else if (LotteryConstant.GDKLSF.equals(lottId)) {
			return TimeDict.Ten_Minutes;
		} else if (LotteryConstant.JSK3.equals(lottId)) {
			return TimeDict.Ten_Minutes;
		} else if (LotteryConstant.XYNC.equals(lottId)) {
			return TimeDict.Ten_Minutes;
		} else {
			return TimeDict.Ten_Minutes;
		}
	}

	/**
	 * 如果今天的奖开完了，获取下期开奖时间
	 * 
	 * @author ms
	 * @date 2018年8月15日 下午1:04:39
	 */
	public static Long getNextStarttime(String lottId) {
		Calendar cal = Calendar.getInstance();
		// 北京赛车9点7分30秒
		if (LotteryConstant.BJSC.equals(lottId)) {
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 7);
			cal.set(Calendar.SECOND, 30);
		}
		// 快乐8 9点5分10秒
		if (LotteryConstant.KL8.equals(lottId)) {
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 5);
			cal.set(Calendar.SECOND, 10);
		}
		// 广东11选5 9点11分30秒
		if (LotteryConstant.GD11X5.equals(lottId)) {
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 11);
			cal.set(Calendar.SECOND, 30);
		}
		// 江西11选5 9点11分30秒
		if (LotteryConstant.JX11X5.equals(lottId)) {
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 7);
			cal.set(Calendar.SECOND, 30);
		}
		// 天津时时彩 09点10分30秒
		if (LotteryConstant.TJSSC.equals(lottId)) {
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 10);
			cal.set(Calendar.SECOND, 30);
		}
		// 广东快乐10分 09:12
		if (LotteryConstant.GDKLSF.equals(lottId)) {
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 12);
		}
		// 江苏快3 08:40:20
		if (LotteryConstant.JSK3.equals(lottId)) {
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 8);
			cal.set(Calendar.MINUTE, 40);
			cal.set(Calendar.SECOND, 20);
		}

		// 开奖有跨天：重庆时时彩、新疆时时彩、幸运飞艇、幸运农场
		// 重庆时时彩 10点00分50秒
		if (LotteryConstant.CQSSC.equals(lottId)) {
			cal.set(Calendar.HOUR_OF_DAY, 10);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 50);
		}
		// 幸运农场 10:04:26
		if (LotteryConstant.XYNC.equals(lottId)) {
			cal.set(Calendar.HOUR_OF_DAY, 10);
			cal.set(Calendar.MINUTE, 4);
			cal.set(Calendar.SECOND, 30);
		}
		// 新疆时时彩 10:10:30
		if (LotteryConstant.XJSSC.equals(lottId)) {
			cal.set(Calendar.HOUR_OF_DAY, 10);
			cal.set(Calendar.MINUTE, 10);
			cal.set(Calendar.SECOND, 30);
		}
		// 幸运飞艇 13:09:40
		if (LotteryConstant.XYFT.equals(lottId)) {
			cal.set(Calendar.HOUR_OF_DAY, 13);
			cal.set(Calendar.MINUTE, 9);
			cal.set(Calendar.SECOND, 40);
		}
		return cal.getTimeInMillis();
	}

	/**
	 * @desc 查询彩种now是哪天
	 * @author xg
	 * @return
	 * @author 2018年8月18日
	 */
	public static String countDayOfNow(String lottId) {
		Date date = new Date();
		String date2String = DateUtil.date2String(date, DateUtil.PATTERN_DATE);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter3 = new SimpleDateFormat("HHmm");
		int h = Integer.parseInt(formatter3.format(date));
		// 每个原有时间上加上3分钟
		if (lottId.equals(LotteryConstant.CQSSC)) {
			if (h < 3) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date2String = DateUtil.date2String(calendar.getTime(), DateUtil.PATTERN_DATE);
			}
		} else if (lottId.equals(LotteryConstant.XYFT)) {
			if (h < 407) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date2String = DateUtil.date2String(calendar.getTime(), DateUtil.PATTERN_DATE);
			}
		} else if (lottId.equals(LotteryConstant.XYNC)) {
			if (h < 206) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date2String = DateUtil.date2String(calendar.getTime(), DateUtil.PATTERN_DATE);
			}
		} else if (lottId.equals(LotteryConstant.SDC)) {
			if (h < 206) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date2String = DateUtil.date2String(calendar.getTime(), DateUtil.PATTERN_DATE);
			}
		} else if (lottId.equals(LotteryConstant.XJSSC)) {
			if (h < 203) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date2String = DateUtil.date2String(calendar.getTime(), DateUtil.PATTERN_DATE);
			}
		}
		return date2String;
	};

	/**
	 * @desc 查询彩种now是哪天
	 * @author xg
	 * @return
	 * @author 2018年8月18日
	 */
	public static String countDayOfNow(String lottId, String date2String) {
		// String date2String = DateUtil.date2String(date,
		// DateUtil.PATTERN_DATE);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter3 = new SimpleDateFormat("HHmm");
		int h = Integer.parseInt(formatter3.format(DateUtils.stringToDate(date2String, DateUtils.DATE_PATTERN)));
		// 每个原有时间上加上3分钟
		if (lottId.equals(LotteryConstant.CQSSC)) {
			if (h < 3) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date2String = DateUtil.date2String(calendar.getTime(), DateUtil.PATTERN_DATE);
			}
		} else if (lottId.equals(LotteryConstant.XYFT)) {
			if (h < 407) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date2String = DateUtil.date2String(calendar.getTime(), DateUtil.PATTERN_DATE);
			}
		} else if (lottId.equals(LotteryConstant.XYNC)) {
			if (h < 206) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date2String = DateUtil.date2String(calendar.getTime(), DateUtil.PATTERN_DATE);
			}
		} else if (lottId.equals(LotteryConstant.SDC)) {
			if (h < 206) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date2String = DateUtil.date2String(calendar.getTime(), DateUtil.PATTERN_DATE);
			}
		} else if (lottId.equals(LotteryConstant.XJSSC)) {
			if (h < 203) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date2String = DateUtil.date2String(calendar.getTime(), DateUtil.PATTERN_DATE);
			}
		}
		return date2String;
	};

	public static void main(String[] args) {
		/*
		 * System.out.println("cqssc=" +
		 * Arrays.toString(LotteryUtil.numArrayByLottId("cqssc")));
		 * System.out.println("gdklsf=" +
		 * Arrays.toString(LotteryUtil.numArrayByLottId("gdklsf")));//
		 * System.out.println("xync=" +
		 * Arrays.toString(LotteryUtil.numArrayByLottId("xync")));//
		 * System.out.println("gd11x5=" +
		 * Arrays.toString(LotteryUtil.numArrayByLottId("gd11x5")));//
		 * System.out.println("jsk3=" +
		 * Arrays.toString(LotteryUtil.numArrayByLottId("jsk3")));//
		 * System.out.println("xjssc=" +
		 * Arrays.toString(LotteryUtil.numArrayByLottId("xjssc")));
		 * System.out.println("cqssc=" +
		 * Arrays.toString(LotteryUtil.numArrayByLottId("tjssc")));
		 * System.out.println("cqssc=" +
		 * Arrays.toString(LotteryUtil.numArrayByLottId("jx11x5")));
		 */
		//
		// String td = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE2);
		// String nd = DateUtil.getDateString(new Date(), null, null, 1, null,
		// null, null, DateUtil.PATTERN_DATE2);
		//
		// System.err.println(td);
		// System.err.println(nd);

		String str = countDayOfNow(LotteryConstant.XYNC, "2018-08-29");
		System.out.println(str);

	}
}
