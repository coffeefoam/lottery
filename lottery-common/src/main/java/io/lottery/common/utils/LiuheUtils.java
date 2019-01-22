package io.lottery.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * 六合彩属性规则
 * 
 * @desc
 * @author xg
 * @author 2018年9月11日
 */
public class LiuheUtils {

	/**
	 * 12生肖
	 */
	public final static String[] SX_12 = { "猪", "狗", "鸡", "猴", "羊", "马", "蛇", "龙", "兔", "虎", "牛", "鼠" };

	/**
	 * 五行--金木水火土
	 */
	public final static String[] WUXING = { "金", "木", "水", "火", "土" };

	/**
	 * 家禽野兽--家禽
	 */
	public final static String[] SX_JQYS_JIAQIN = { "牛", "马", "羊", "鸡", "狗", "猪" };

	/**
	 * 家禽野兽--野兽
	 */
	public final static String[] SX_JQYS_YESHOU = { "鼠", "虎", "兔", "龙", "蛇", "猴" };

	/**
	 * 男女生肖--男
	 */
	public final static String[] SX_NVSX_NAN = { "鼠", "牛", "虎", "龙", "马", "猴", "狗" };

	/**
	 * 男女生肖--女
	 */
	public final static String[] SX_NVSX_NV = { "兔", "蛇", "羊", "鸡", "猪" };
	/**
	 * 天地生肖--天
	 */
	public final static String[] SX_TDSX_TIAN = { "牛", "兔", "龙", "马", "猴", "猪" };

	/**
	 * 天地生肖--地
	 */
	public final static String[] SX_TDSX_DI = { "鼠", "虎", "蛇", "羊", "鸡", "狗" };

	/**
	 * 四级生肖--春
	 */
	public final static String[] SX_SJSX_CHUN = { "虎", "兔", "龙" };

	/**
	 * 四级生肖--夏
	 */
	public final static String[] SX_SJSX_XIA = { "蛇", "马", "羊" };

	/**
	 * 四级生肖--秋
	 */
	public final static String[] SX_SJSX_QIU = { "猴", "鸡", "狗" };

	/**
	 * 四级生肖--冬
	 */
	public final static String[] SX_SJSX_DONG = { "鼠", "牛", "猪" };

	/**
	 * 琴棋书画--琴
	 */
	public final static String[] SX_QQSH_QIN = { "兔", "蛇", "鸡" };

	/**
	 * 琴棋书画--棋
	 */
	public final static String[] SX_QQSH_QI = { "鼠", "牛", "狗" };

	/**
	 * 琴棋书画--书
	 */
	public final static String[] SX_QQSH_SHU = { "虎", "龙", "马" };

	/**
	 * 琴棋书画--画
	 */
	public final static String[] SX_QQSH_HUA = { "羊", "猴", "猪" };

	/**
	 * 三色生肖--红
	 */
	public final static String[] SX_SSSX_HONG = { "鼠", "兔", "马", "鸡" };

	/**
	 * 三色生肖--蓝
	 */
	public final static String[] SX_SSSX_LAN = { "虎", "蛇", "猴", "猪" };

	/**
	 * 三色生肖--绿
	 */
	public final static String[] SX_SSSX_LV = { "牛", "龙", "羊", "狗" };

	/**
	 * 家禽
	 */
	public static List<String> JQList = Arrays.asList("牛", "马", "羊", "鸡", "狗", "猪");
	/**
	 * 野兽
	 */
	public static List<String> YSList = Arrays.asList("鼠", "虎", "兔", "龙", "蛇", "猴");
	/**
	 * 六合彩所有号码
	 */
	public final static String[] AllNumber = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
			"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
			"49" };
	/**
	 * 红波号码
	 */
	public static List<Integer> RedList = Arrays.asList(1, 2, 7, 8, 12, 13, 18, 19, 23, 24, 29, 30, 34, 35, 40, 45, 46);
	/**
	 * 蓝波号码
	 */
	public static List<Integer> BlueList = Arrays.asList(3, 4, 9, 10, 14, 15, 20, 25, 26, 31, 36, 37, 41, 42, 47, 48);
	/**
	 * 绿波号码
	 */
	public static List<Integer> GreenList = Arrays.asList(5, 6, 11, 16, 17, 21, 22, 27, 28, 32, 33, 38, 39, 43, 44, 49);
	/**
	 * 六合彩段位：一段号码
	 */
	public static List<Integer> OneSection = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

	/**
	 * 六合彩段位：二段号码
	 */
	public static List<Integer> TwoSection = Arrays.asList(8, 9, 1, 11, 12, 13, 14);
	/**
	 * 六合彩段位：三段号码
	 */
	public static List<Integer> ThreeSection = Arrays.asList(15, 16, 17, 18, 19, 2, 21);
	/**
	 * 六合彩段位：四段号码
	 */
	public static List<Integer> FourSection = Arrays.asList(22, 23, 24, 25, 26, 27, 28);
	/**
	 * 六合彩段位：五段号码
	 */
	public static List<Integer> FiveSection = Arrays.asList(29, 3, 31, 32, 33, 34, 35);
	/**
	 * 六合彩段位：六段号码
	 */
	public static List<Integer> SixSection = Arrays.asList(36, 37, 38, 39, 4, 41, 42);
	/**
	 * 六合彩段位：七段号码
	 */
	public static List<Integer> SevenSection = Arrays.asList(43, 44, 45, 46, 47, 48, 49);

	/**
	 * 获取六合彩号码的生肖
	 * 
	 * @desc
	 * @author xg
	 * @param num
	 * @param date
	 * @return
	 * @author 2018年9月12日
	 */
	public static String getNumSx(int num, Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(date);
		// 计算出农历年
		LunarCalendar c = LunarCalendar.solar2Lunar(cal);
		int year = c.getYear();
		Map<String, List<Integer>> map = getSx(year);
		for (String sx : map.keySet()) {
			List<Integer> list = map.get(sx);
			if (list.contains(num)) {
				return sx;
			}
		}
		return null;
	}

	/**
	 * 获取数字的五行
	 * 
	 * @desc
	 * @author xg
	 * @param num
	 * @param date
	 * @return
	 * @author 2018年9月12日
	 */
	public static String getNumWx(int num, Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(date);
		// 计算出农历年
		LunarCalendar c = LunarCalendar.solar2Lunar(cal);
		int year = c.getYear();
		Map<String, List<Integer>> map = getWuxing(year);
		for (String wx : map.keySet()) {
			List<Integer> list = map.get(wx);
			if (list.contains(num)) {
				return wx;
			}
		}
		return null;
	}

	/**
	 * 获取号码的家禽野兽
	 * 
	 * @desc
	 * @author xg
	 * @param sx
	 * @return
	 * @author 2018年9月12日
	 */
	public static String getNumJqys(String sx) {
		for (String str : SX_JQYS_JIAQIN) {
			if (sx.equals(str)) {
				return "家";
			}
		}
		for (String str : SX_JQYS_YESHOU) {
			if (sx.equals(str)) {
				return "野";
			}
		}
		return "";
	}

	/**
	 * 获取号码的男女生肖
	 * 
	 * @desc
	 * @author xg
	 * @param sx
	 * @return
	 * @author 2018年9月12日
	 */
	public static String getNumNnsx(String sx) {
		for (String str : SX_NVSX_NAN) {
			if (sx.equals(str)) {
				return "男";
			}
		}
		for (String str : SX_NVSX_NV) {
			if (sx.equals(str)) {
				return "女";
			}
		}
		return "";
	}

	/**
	 * 获取号码的天地生肖
	 * 
	 * @desc
	 * @author xg
	 * @param sx
	 * @return
	 * @author 2018年9月12日
	 */
	public static String getNumTdsx(String sx) {
		for (String str : SX_TDSX_TIAN) {
			if (sx.equals(str)) {
				return "天";
			}
		}
		for (String str : SX_TDSX_DI) {
			if (sx.equals(str)) {
				return "地";
			}
		}
		return "";
	}

	/**
	 * 获取号码的四级生肖
	 * 
	 * @desc
	 * @author xg
	 * @param sx
	 * @return
	 * @author 2018年9月12日
	 */
	public static String getNumSjsx(String sx) {
		for (String str : SX_SJSX_CHUN) {
			if (sx.equals(str)) {
				return "春";
			}
		}
		for (String str : SX_SJSX_XIA) {
			if (sx.equals(str)) {
				return "夏";
			}
		}
		for (String str : SX_SJSX_QIU) {
			if (sx.equals(str)) {
				return "秋";
			}
		}
		for (String str : SX_SJSX_DONG) {
			if (sx.equals(str)) {
				return "冬";
			}
		}
		return "";
	}

	/**
	 * 获取号码的琴棋书画
	 * 
	 * @desc
	 * @author xg
	 * @param sx
	 * @return
	 * @author 2018年9月12日
	 */
	public static String getNumQqsh(String sx) {
		for (String str : SX_QQSH_QIN) {
			if (sx.equals(str)) {
				return "琴";
			}
		}
		for (String str : SX_QQSH_QI) {
			if (sx.equals(str)) {
				return "棋";
			}
		}
		for (String str : SX_QQSH_SHU) {
			if (sx.equals(str)) {
				return "书";
			}
		}
		for (String str : SX_QQSH_HUA) {
			if (sx.equals(str)) {
				return "画";
			}
		}
		return "";
	}

	/**
	 * 获取号码的三色生肖
	 * 
	 * @desc
	 * @author xg
	 * @param sx
	 * @return
	 * @author 2018年9月12日
	 */
	public static String getNumSssx(String sx) {
		for (String str : SX_SSSX_HONG) {
			if (sx.equals(str)) {
				return "红";
			}
		}
		for (String str : SX_SSSX_LAN) {
			if (sx.equals(str)) {
				return "蓝";
			}
		}
		for (String str : SX_SSSX_LV) {
			if (sx.equals(str)) {
				return "绿";
			}
		}
		return "";
	}

	/**
	 * 通过日期获取生肖对应的数字map
	 * 
	 * @desc
	 * @author xg
	 * @param date
	 * @return
	 * @author 2018年9月12日
	 */
	public static Map<String, List<Integer>> getSx(Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(date);
		// 计算出农历年
		LunarCalendar c = LunarCalendar.solar2Lunar(cal);
		int year = c.getYear();
		return getSx(year);
	}

	/**
	 * 通过日期获取当年的五行map
	 * 
	 * @desc
	 * @author xg
	 * @param date
	 * @return
	 * @author 2018年9月12日
	 */
	public static Map<String, List<Integer>> getWuxing(Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.setTime(date);
		// 计算出农历年
		LunarCalendar c = LunarCalendar.solar2Lunar(cal);
		int year = c.getYear();
		return getWuxing(year);
	}

	/**
	 * 通过年份获取生肖号码
	 * 
	 * @desc
	 * @author xg
	 * @param year
	 * @return
	 * @author 2018年9月12日
	 */
	public static Map<String, List<Integer>> getSx(int year) {
		// 1.获取生肖
		String sx = ChineseZodiac.getAnimalYearName(year);
		List<String> sxList = Arrays.asList(SX_12);

		// 获取从今年生肖开始的生肖排序
		int index = sxList.indexOf(sx);
		List<String> list = new ArrayList<String>();
		List<String> list1 = sxList.subList(0, index);
		List<String> list2 = sxList.subList(index, sxList.size());

		if (list2 != null && list2.size() > 0) {
			for (String o : list2) {
				list.add(o);
			}
		}
		if (list1 != null && list1.size() > 0) {
			for (String o : list1) {
				list.add(o);
			}
		}

		Map<String, List<Integer>> maplist = new HashMap<String, List<Integer>>();
		// 2.查找是哪个生肖
		for (int i = 1; i <= list.size(); i++) {
			List<Integer> l = new ArrayList<Integer>();
			for (int j = i; j < 50; j = j + 12) {
				l.add(j);
			}
			String str = list.get(i - 1);
			maplist.put(str, l);
		}

		return maplist;
	}

	/**
	 * 通过年份计算农历年的五行对应的数字
	 * 
	 * @desc
	 * @author xg
	 * @param year
	 * @return
	 * @author 2018年9月12日
	 */
	public static Map<String, List<Integer>> getWuxing(int year) {
		Map<String, List<Integer>> maplist = new HashMap<String, List<Integer>>();
		for (int i = 0; i < 49; i++) {
			String wxStr = ChineseZodiac.getWuxingYearName(year - i);
			// 先获取是否存在
			List<Integer> list = maplist.get(wxStr);
			if (list == null) {
				list = new ArrayList<Integer>();
			}
			list.add(i + 1);
			maplist.put(wxStr, list);
		}
		return maplist;
	}

	/**
	 * 根据号码获取波色
	 * 
	 * @author ms
	 * @date 2018年9月13日 上午10:17:54
	 */
	public static String getBoSe(Object num) {
		if (num == null || "".equals(num))
			return null;
		Integer hm = Integer.valueOf(num.toString());
		if (RedList.contains(hm)) {
			return "red";
		} else if (BlueList.contains(hm)) {
			return "blue";
		} else if (GreenList.contains(hm)) {
			return "green";
		}
		return null;
	}

	public static void main(String[] args) {
		String[] numlist = { "48", "49", "17", "14", "30", "46", "36" };

		for (String str : numlist) {
			System.out.println(getBoSe(str));
		}
	}

	/**
	 * 判断返回颜色的下标
	 * 
	 * @desc
	 * @author xg
	 * @param color
	 * @return
	 * @author 2018年9月13日
	 */
	public static Integer getBoSeIndex(String color) {
		if ("red".equals(color)) {
			return 1;
		} else if ("green".equals(color)) {
			return 2;
		} else {
			return 3;
		}
	}

	/**
	 * 获取七色波下标
	 * 
	 * @desc
	 * @author xg
	 * @param color
	 * @return
	 * @author 2018年9月13日
	 */
	public static Integer getBoSeNanairoIndex(String color) {
		if ("red".equals(color)) {
			return 0;
		} else if ("green".equals(color)) {
			return 1;
		} else if ("blue".equals(color)) {
			return 2;
		} else {
			return 3;
		}
	}

	/**
	 * 获取生肖下标
	 * 
	 * @desc
	 * @author xg
	 * @param sx
	 * @return
	 * @author 2018年9月13日
	 */
	public static Integer getSxIndex(String sx) {
		List<String> list = Arrays.asList(SX_12);
		int index = list.indexOf(sx);
		return SX_12.length - index;
	}

	/**
	 * 获取五行的下标
	 * 
	 * @desc
	 * @author xg
	 * @param string
	 * @return
	 * @author 2018年9月13日
	 */
	public static Integer getWxIndex(String wx) {
		List<String> list = Arrays.asList(WUXING);
		int index = list.indexOf(wx);
		return index + 1;
	}

	/**
	 * 获取家禽野兽下标
	 * 
	 * @desc
	 * @author xg
	 * @param string
	 * @return
	 * @author 2018年9月13日
	 */
	public static Integer getJqysIndex(String string) {
		if ("家".equals(string)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 获取男女生肖下标
	 * 
	 * @desc
	 * @author xg
	 * @param string
	 * @return
	 * @author 2018年9月13日
	 */
	public static Integer getNnsxIndex(String string) {
		if ("男".equals(string)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 天地生肖下标
	 * 
	 * @desc
	 * @author xg
	 * @param string
	 * @return
	 * @author 2018年9月13日
	 */
	public static Integer getTdsxIndex(String string) {
		if ("天".equals(string)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 获取春夏秋冬下标
	 * 
	 * @desc
	 * @author xg
	 * @param string
	 * @return
	 * @author 2018年9月13日
	 */
	public static Integer getSjsxIndex(String string) {
		if ("春".equals(string)) {
			return 1;
		} else if ("夏".equals(string)) {
			return 2;
		} else if ("秋".equals(string)) {
			return 3;
		} else {
			return 4;
		}
	}

	/**
	 * 获取琴棋书画下标
	 * 
	 * @desc
	 * @author xg
	 * @param string
	 * @return
	 * @author 2018年9月13日
	 */
	public static Integer getQqshIndex(String string) {
		if ("琴".equals(string)) {
			return 1;
		} else if ("棋".equals(string)) {
			return 2;
		} else if ("书".equals(string)) {
			return 3;
		} else {
			return 4;
		}
	}

	/**
	 * 获取三色生肖下标
	 * 
	 * @desc
	 * @author xg
	 * @param string
	 * @return
	 * @author 2018年9月13日
	 */
	public static Integer getSssxIndex(String string) {
		if ("红".equals(string)) {
			return 1;
		} else if ("蓝".equals(string)) {
			return 2;
		} else {
			return 3;
		}
	}

}
