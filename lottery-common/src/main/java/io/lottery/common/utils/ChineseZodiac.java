package io.lottery.common.utils;

/**
 * 计算天干地支,12生肖
 * 
 * @desc
 * @author xg
 * @author 2018年9月11日
 */
public class ChineseZodiac {

	/**
	 * 五行对应年份
	 */
	private final static String[] WUXING_JIN = { "甲子", "乙丑", "甲午", "乙未", "壬申", "癸酉", "壬寅", "癸卯", "庚辰", "辛巳", "庚戌",
			"辛亥" };

	private final static String[] WUXING_MU = { "戊辰", "己巳", "戊戌", "己亥", "壬午", "癸未", "壬子", "癸丑", "庚寅", "辛卯", "庚申",
			"辛酉" };

	private final static String[] WUXING_SHUI = { "丙子", "丁丑", "丙午", "丁未", "甲申", "乙酉", "甲寅", "乙卯", "壬辰", "癸巳", "壬戌",
			"癸亥" };

	private final static String[] WUXING_HUO = { "丙寅", "丁卯", "丙申", "丁酉", "甲戌", "乙亥", "甲辰", "乙巳", "戊子", "己丑", "戊午",
			"己未" };

	private final static String[] WUXING_TU = { "庚午", "辛未", "庚子", "辛丑", "戊寅", "己卯", "戊申", "己酉", "丙戌", "丁亥", "丙辰",
			"丁巳" };

	private final static String[][] tgdz = new String[][] { { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" }, // 10天干
			{ "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" } // 12地支
	};

	// 12生肖，（注：12生肖对应12地支，即子鼠，丑牛,寅虎依此类推）
	private final static String[] animalYear = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗",
			"猪" };
	private final static int startYear = 1804;// 定义起始年，1804年为甲子年属鼠

	/** 获取当前年份与起始年之间的差值 **/
	public static int subtractYear(int year) {
		int jiaziYear = startYear;
		if (year < jiaziYear) {// 如果年份小于起始的甲子年(startYear = 1804),则起始甲子年往前偏移
			jiaziYear = jiaziYear - (60 + 60 * ((jiaziYear - year) / 60));// 60年一个周期
		}
		return year - jiaziYear;
	}

	/** 获取该年的天干名称 **/

	public static String getTianGanName(int year) {
		String name = tgdz[0][subtractYear(year) % 10];
		return name;
	}

	/** 获取该年的地支名称 **/

	public static String getDiZhiName(int year) {
		String name = tgdz[1][subtractYear(year) % 12];
		return name;
	}

	/**
	 * 
	 * 获取该年的天干、地支名称
	 * 
	 * @param year
	 *            年份
	 * 
	 * @return
	 * 
	 */

	public static String getTGDZName(int year) {
		String name = getTianGanName(year) + getDiZhiName(year);
		return name;
	}

	/**
	 * 
	 * 获取该年的生肖名称
	 * 
	 * @param year
	 *            年份
	 * 
	 * @return
	 * 
	 */

	public static String getAnimalYearName(int year) {
		String name = animalYear[subtractYear(year) % 12];
		return name;
	}

	/**
	 * 获取五行中的金木水火土
	 * 
	 * @desc
	 * @author xg
	 * @param year
	 * @return
	 * @author 2018年9月11日
	 */
	public static String getWuxingYearName(int year) {
		String str = getTGDZName(year);
		// 遍历不同五行
		for (String wx : WUXING_JIN) {
			if (wx.equals(str)) {
				return "金";
			}
		}
		for (String wx : WUXING_MU) {
			if (wx.equals(str)) {
				return "木";
			}
		}
		for (String wx : WUXING_SHUI) {
			if (wx.equals(str)) {
				return "水";
			}
		}
		for (String wx : WUXING_HUO) {
			if (wx.equals(str)) {
				return "火";
			}
		}
		for (String wx : WUXING_TU) {
			if (wx.equals(str)) {
				return "土";
			}
		}

		return "";
	}

	/**
	 * 
	 * @param args
	 * 
	 */

	public static void main(String[] args) {
		int i = 0;
		for (int k = startYear; k < 2050; k++) {
			System.out
					.print("  " + k + ":" + getTGDZName(k) + "年属" + getAnimalYearName(k) + ":" + getWuxingYearName(k));
			if (++i % tgdz[0].length == 0) {
				System.out.print("\n");
			}
		}
	}

}
