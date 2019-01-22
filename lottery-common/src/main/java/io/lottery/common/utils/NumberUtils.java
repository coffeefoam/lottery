package io.lottery.common.utils;

import java.util.regex.Pattern;

/**
 * 数字操作使用的工具类
 * 
 * @author R6
 *
 */
public class NumberUtils {

	/**
	 * 随机产生数字,不包括最大值max
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomInt(int min, int max) {
		return (int) (min + Math.random() * (max - min));
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(randomInt(0, 10));
		}
	}

	/**
	 * 
	 * @desc
	 * @author xg
	 * @param str
	 * @return
	 * @author 2018年9月14日
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

}
