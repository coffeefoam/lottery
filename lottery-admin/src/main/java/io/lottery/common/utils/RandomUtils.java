package io.lottery.common.utils;

import java.util.Random;

/**
 * 产生随机数
 * 
 * @author R6
 *
 */
public class RandomUtils {

	/**
	 * 生成指定范围的随机整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomInt(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

}
