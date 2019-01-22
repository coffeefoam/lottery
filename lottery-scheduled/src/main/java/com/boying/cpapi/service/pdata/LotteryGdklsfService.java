package com.boying.cpapi.service.pdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;

import io.lottery.common.config.LotteryDict;

/**
 * @desc 广东快乐十分业务处理
 * @author xg
 * @author 2018年6月27日
 */
@Service
public class LotteryGdklsfService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * 广东快乐十分业务处理
	 * 
	 * @author ms
	 * @param lottId
	 */
	public void execute(String lottId) {
		// 开奖记录
		this.getKjjl(lottId, null);
		// 每日长龙
		this.getMrcl(lottId);
		// 长龙连开提醒
		this.getCllktx(lottId);
		// 总和路珠
		this.getZhlz(lottId);
		// 龙虎路珠
		this.getLhlz(lottId);
	}

	/**
	 * 
	 * @desc 广东快乐十分开奖记录
	 * @author abo
	 * @param date
	 * @date 2018年6月27日 下午3:45:00
	 */
	public void getKjjl(String lottId, String date) {
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);
		// 查询数据库数据
		// List<Map<String, Object>> list =
		// lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 1);
		// 查询数据库数据,添加了日期，如果有日期，则执行那一天的数据生成
		List<Map<String, Object>> list = null;
		if (StringUtils.isBlank(date)) {
			list = lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 1);
		} else {
			list = lotteryProcessMapper.findLotteryByDate(tablename, numStr, date);
		}
		for (Map<String, Object> m : list) {
			int num1 = Integer.parseInt(m.get("num1").toString());
			int num2 = Integer.parseInt(m.get("num2").toString());
			int num3 = Integer.parseInt(m.get("num3").toString());
			int num4 = Integer.parseInt(m.get("num4").toString());
			int num5 = Integer.parseInt(m.get("num5").toString());
			int num6 = Integer.parseInt(m.get("num6").toString());
			int num7 = Integer.parseInt(m.get("num7").toString());
			int num8 = Integer.parseInt(m.get("num8").toString());
			// 总和
			int sum = num1 + num2 + num3 + num4 + num5 + num6 + num7 + num8;
			String sumDx = LotteryConstant.HE;
			// 总和大小
			if (sum < 84) {
				sumDx = LotteryConstant.XIAO;
			} else if (sum > 84) {
				sumDx = LotteryConstant.DA;
			}

			// 尾大尾小
			String wDx = sum % 10 > 4 ? LotteryConstant.WDA : LotteryConstant.WXIAO;
			// 总和单双
			String sumDs = sum % 2 == 0 ? LotteryConstant.SHUANG : LotteryConstant.DAN;
			// 龙虎1
			String lh1 = num1 > num8 ? LotteryConstant.LONG : LotteryConstant.HU;
			// 龙虎2
			String lh2 = num2 > num7 ? LotteryConstant.LONG : LotteryConstant.HU;
			// 龙虎3
			String lh3 = num3 > num6 ? LotteryConstant.LONG : LotteryConstant.HU;
			// 龙虎4
			String lh4 = num4 > num5 ? LotteryConstant.LONG : LotteryConstant.HU;

			Map<String, Object> resmap = new HashMap<String, Object>();
			List<Object> numberlist = new ArrayList<Object>();
			for (String position : numStr.split(",")) {
				numberlist.add(m.get(position));
			}
			resmap.put("rank", numberlist);
			resmap.put("periods", m.get("periods"));
			resmap.put("starttime", m.get("starttime"));
			resmap.put("lh1", lh1);
			resmap.put("lh2", lh2);
			resmap.put("lh3", lh3);
			resmap.put("lh4", lh4);
			resmap.put("sum", sum);
			resmap.put("sumDx", sumDx);
			resmap.put("sumDs", sumDs);
			resmap.put("wDx", wDx);
			JSONObject resobj = new JSONObject(resmap);
			redisservice.setHashMap(lottId + "_" + LotteryDict.kjjl, resmap.get("periods"), resobj.toJSONString());
		}
	}

	/**
	 * 
	 * @desc 每日长龙
	 * @author abo
	 * @date 2018年6月27日 下午7:40:37
	 */
	public void getMrcl(String lottId) {
		// 今天的日期
		String currentDate = LotteryUtil.countDayOfNow(lottId);
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 查询数据库数据
		List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByDate(tablename, numStr, currentDate);
		// 单双和大小数据
		int[] num1Arrays = new int[list.size()];
		int[] num2Arrays = new int[list.size()];
		int[] num3Arrays = new int[list.size()];
		int[] num4Arrays = new int[list.size()];
		int[] num5Arrays = new int[list.size()];
		int[] num6Arrays = new int[list.size()];
		int[] num7Arrays = new int[list.size()];
		int[] num8Arrays = new int[list.size()];
		//
		// 合单双数据
		int[] hedanshuang1Arrays = new int[list.size()];
		int[] hedanshuang2Arrays = new int[list.size()];
		int[] hedanshuang3Arrays = new int[list.size()];
		int[] hedanshuang4Arrays = new int[list.size()];
		int[] hedanshuang5Arrays = new int[list.size()];
		int[] hedanshuang6Arrays = new int[list.size()];
		int[] hedanshuang7Arrays = new int[list.size()];
		int[] hedanshuang8Arrays = new int[list.size()];

		// 龙虎数据
		int[] longhu1Arrays = new int[list.size()];
		int[] longhu2Arrays = new int[list.size()];
		int[] longhu3Arrays = new int[list.size()];
		int[] longhu4Arrays = new int[list.size()];
		// 总和
		int[] zhArray = new int[list.size()];
		// 把每个号段重新封装成一个数组
		for (int i = 0; i < list.size(); i++) {
			Map m = list.get(i);
			num1Arrays[i] = Integer.parseInt(m.get("num1").toString());
			num2Arrays[i] = Integer.parseInt(m.get("num2").toString());
			num3Arrays[i] = Integer.parseInt(m.get("num3").toString());
			num4Arrays[i] = Integer.parseInt(m.get("num4").toString());
			num5Arrays[i] = Integer.parseInt(m.get("num5").toString());
			num6Arrays[i] = Integer.parseInt(m.get("num6").toString());
			num7Arrays[i] = Integer.parseInt(m.get("num7").toString());
			num8Arrays[i] = Integer.parseInt(m.get("num8").toString());

			longhu1Arrays[i] = Integer.parseInt(m.get("num1").toString()) > Integer.parseInt(m.get("num8").toString()) ? 1 : 0;
			longhu2Arrays[i] = Integer.parseInt(m.get("num2").toString()) > Integer.parseInt(m.get("num7").toString()) ? 1 : 0;
			longhu3Arrays[i] = Integer.parseInt(m.get("num3").toString()) > Integer.parseInt(m.get("num6").toString()) ? 1 : 0;
			longhu4Arrays[i] = Integer.parseInt(m.get("num4").toString()) > Integer.parseInt(m.get("num5").toString()) ? 1 : 0;
			// 开出号码个位与十位相加的合为单数时为合数单，反之为合数双。
			hedanshuang1Arrays[i] = Integer.parseInt(m.get("num1").toString()) / 10 + Integer.parseInt(m.get("num1").toString()) % 10;
			hedanshuang2Arrays[i] = Integer.parseInt(m.get("num2").toString()) / 10 + Integer.parseInt(m.get("num2").toString()) % 10;
			hedanshuang3Arrays[i] = Integer.parseInt(m.get("num3").toString()) / 10 + Integer.parseInt(m.get("num3").toString()) % 10;
			hedanshuang4Arrays[i] = Integer.parseInt(m.get("num4").toString()) / 10 + Integer.parseInt(m.get("num4").toString()) % 10;
			hedanshuang5Arrays[i] = Integer.parseInt(m.get("num5").toString()) / 10 + Integer.parseInt(m.get("num5").toString()) % 10;
			hedanshuang6Arrays[i] = Integer.parseInt(m.get("num6").toString()) / 10 + Integer.parseInt(m.get("num6").toString()) % 10;
			hedanshuang7Arrays[i] = Integer.parseInt(m.get("num7").toString()) / 10 + Integer.parseInt(m.get("num7").toString()) % 10;
			hedanshuang8Arrays[i] = Integer.parseInt(m.get("num8").toString()) / 10 + Integer.parseInt(m.get("num8").toString()) % 10;
			// 和
			zhArray[i] = Integer.parseInt(m.get("num1").toString()) + Integer.parseInt(m.get("num2").toString()) + Integer.parseInt(m.get("num3").toString())
					+ Integer.parseInt(m.get("num4").toString()) + Integer.parseInt(m.get("num5").toString()) + Integer.parseInt(m.get("num6").toString())
					+ Integer.parseInt(m.get("num7").toString()) + Integer.parseInt(m.get("num8").toString());
		}
		Map<String, int[]> maps = new HashMap<String, int[]>();
		// 大小单双数据
		maps.put("num1", num1Arrays);
		maps.put("num2", num2Arrays);
		maps.put("num3", num3Arrays);
		maps.put("num4", num4Arrays);
		maps.put("num5", num5Arrays);
		maps.put("num6", num6Arrays);
		maps.put("num7", num7Arrays);
		maps.put("num8", num8Arrays);

		maps.put("num9", zhArray);// 和

		maps.put("num1lh", longhu1Arrays);
		maps.put("num2lh", longhu2Arrays);
		maps.put("num3lh", longhu3Arrays);
		maps.put("num4lh", longhu4Arrays);

		maps.put("num1h", hedanshuang1Arrays);
		maps.put("num2h", hedanshuang2Arrays);
		maps.put("num3h", hedanshuang3Arrays);
		maps.put("num4h", hedanshuang4Arrays);
		maps.put("num5h", hedanshuang5Arrays);
		maps.put("num6h", hedanshuang6Arrays);
		maps.put("num7h", hedanshuang7Arrays);
		maps.put("num8h", hedanshuang8Arrays);

		// 初始化公共数组
		int[] numCommonArray = new int[list.size()];
		int[] numLhCommonArray = new int[list.size()];
		int[] numHCommonArray = new int[list.size()];
		Map<String, Object> returnMap = null;
		Map<String, Object> map = new TreeMap<String, Object>();
		// 因为有总和，所以总球数需要加1
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId) + 1;
		// 循环当前彩种球数
		for (int i = 1; i <= num; i++) {
			returnMap = new TreeMap<String, Object>();
			numCommonArray = maps.get("num" + i);// 获取当前处理字段的数组。
			if (numCommonArray == null) {
				continue;
			}
			// 处理龙虎长龙，龙虎只需要1-4号球
			if (i < 5) {
				numLhCommonArray = maps.get("num" + i + "lh");// 获取当前处理字段的数组。
				if (numLhCommonArray == null) {
					continue;
				}
				returnMap = processMrcl(numLhCommonArray, returnMap, i, LotteryConstant.LONG, LotteryConstant.HU);
			}
			// i等于6的时候是总和，总和的大小和普通大小的阈值不一样，所以分开写
			if (i < 9) {
				// 处理大小长龙
				returnMap = processMrcl(numCommonArray, returnMap, i, LotteryConstant.DA, LotteryConstant.XIAO);
				numHCommonArray = maps.get("num" + i + "h");// 获取当前处理字段的数组。
				// 处理合单双
				returnMap = processMrcl(numHCommonArray, returnMap, i, LotteryConstant.HESHUANG, LotteryConstant.HEDAN);
			} else {
				// 处理总和大小
				returnMap = processMrcl(numCommonArray, returnMap, i, LotteryConstant.GYHDA, LotteryConstant.GYHXIAO);
			}
			// 处理单双长龙
			returnMap = processMrcl(numCommonArray, returnMap, i, LotteryConstant.SHUANG, LotteryConstant.DAN);
			// 处理尾大小
			returnMap = processMrcl(numCommonArray, returnMap, i, LotteryConstant.WDA, LotteryConstant.WXIAO);
			map.put("num" + i, returnMap);
		}
		JSONObject resobj = new JSONObject(map);
		// System.out.println(resobj);
		redisservice.setHashMap(lottId + "_" + LotteryDict.mrcl, currentDate, resobj.toJSONString());
	}

	/**
	 * 
	 * @desc 根据类型处理长龙
	 * @author abo
	 * @date 2018年6月28日 下午4:09:49
	 * @param numCommonArray
	 *            号码数组
	 * @param returnMap
	 * @param i
	 *            当前是几号球的数组
	 * @param typeA
	 *            处理的类型，处理当前大小、单双、龙虎
	 * @param typeB
	 *            小或者单
	 * @return
	 */
	private Map<String, Object> processMrcl(int[] numCommonArray, Map<String, Object> returnMap, int i, String typeA, String typeB) {
		int count = 1;// 连续的数，不连续了需要清零
		String preDxStr = "";// 上一个大小格式的数字
		String numdx = "";
		Map<String, Object> map = null;
		for (int j = 0; j < numCommonArray.length; j++) {
			if (typeA.equals(LotteryConstant.DA)) {// 大于10就是大，否则是小
				numdx = numCommonArray[j] > 10 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.SHUANG) || typeA.equals(LotteryConstant.HESHUANG)) {// 对2%，等于0就是双数，否者就是单数
				numdx = numCommonArray[j] % 2 == 0 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.LONG)) {// 等于1就是龙，0等于虎，-1是和
				switch (numCommonArray[j]) {
				case 0:
					numdx = typeB;
					break;
				case 1:
					numdx = typeA;
					break;
				case -1:
					numdx = "和";
					break;
				}
			} else if (typeA.equals(LotteryConstant.GYHDA)) {// 总和大于22就是大，小于23就是小
				// 总和路珠指8个开出的号码的总和对应的大小、单双，其中总合为85至132为大，36至83为小，和值为84。
				if (numCommonArray[j] > 84) {
					numdx = typeA;
				} else if (numCommonArray[j] < 84) {
					numdx = typeB;
				} else if (numCommonArray[j] == 84) {
					numdx = "和";
				}
			} else if (typeA.equals(LotteryConstant.WDA)) {// 处理尾大尾小
				numdx = numCommonArray[j] % 10 > 4 ? typeA : typeB;
			}
			// if (j == numCommonArray.length - 1) {
			// System.out.println(numdx);
			// } else {
			// System.out.print(numdx);
			// }
			// 是否是第一个球，如果是第一个球那么初始化
			if (!StringUtils.isNoneBlank(preDxStr)) {
				preDxStr = numdx;
				continue;
			} else {
				// String b = "num" + i + "_";
				String b = "";
				// 如果相等，计数器加一
				if (numdx == preDxStr) {
					count += 1;
				}
				// 存取条件1:当中断连续就会保存，2:当为中断连续，但是是数组最后一个数的情况
				if ((numdx != preDxStr && count > 1) || (j == numCommonArray.length - 1 && count > 1)) {
					b += count;
					// if (preDxStr.equals(typeA)) {
					// b += "_" + typeA;
					// } else if (preDxStr.equals(typeB)) {
					// b += "_" + typeB;
					// }
					// else if (preDxStr.equals(typeB)) {
					// b += "_" + "和";
					// }
					// returnMap.put(b, (returnMap.get(b) == null ? 0 :
					// returnMap.get(b)) + 1);
					map = (Map<String, Object>) returnMap.get(b);
					if (map == null) {
						map = new TreeMap<String, Object>();
					}
					map.put(preDxStr, map.get(preDxStr) == null ? 1 : Integer.parseInt(map.get(preDxStr).toString()) + 1);
					returnMap.put(b, map);
					count = 1;// 保存完毕，计数器初始化
				}
				preDxStr = numdx;
			}
		}
		return returnMap;
	}

	/**
	 * 
	 * @desc 长龙连开提醒：1-8号球大小单双尾大小，1-4龙虎，总和的大小单双
	 * @author abo
	 * @date 2018年6月30日 下午1:10:48
	 */
	@SuppressWarnings("unchecked")
	public void getCllktx(String lottId) {
		// 今天
		String currentDate = LotteryUtil.countDayOfNow(lottId);
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 查询数据库数据
		List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByDate(tablename, numStr, currentDate);
		// 单双和大小数据
		int[] num1Arrays = new int[list.size()];
		int[] num2Arrays = new int[list.size()];
		int[] num3Arrays = new int[list.size()];
		int[] num4Arrays = new int[list.size()];
		int[] num5Arrays = new int[list.size()];
		int[] num6Arrays = new int[list.size()];
		int[] num7Arrays = new int[list.size()];
		int[] num8Arrays = new int[list.size()];
		// 合单双数据
		int[] hedanshuang1Arrays = new int[list.size()];
		int[] hedanshuang2Arrays = new int[list.size()];
		int[] hedanshuang3Arrays = new int[list.size()];
		int[] hedanshuang4Arrays = new int[list.size()];
		int[] hedanshuang5Arrays = new int[list.size()];
		int[] hedanshuang6Arrays = new int[list.size()];
		int[] hedanshuang7Arrays = new int[list.size()];
		int[] hedanshuang8Arrays = new int[list.size()];
		// 龙虎数据
		int[] longhu1Arrays = new int[list.size()];
		int[] longhu2Arrays = new int[list.size()];
		int[] longhu3Arrays = new int[list.size()];
		int[] longhu4Arrays = new int[list.size()];
		// 总和
		int[] gyhArray = new int[list.size()];

		// 把每个号段重新封装成一个数组
		for (int i = 0; i < list.size(); i++) {
			Map m = list.get(i);
			num1Arrays[i] = Integer.parseInt(m.get("num1").toString());
			num2Arrays[i] = Integer.parseInt(m.get("num2").toString());
			num3Arrays[i] = Integer.parseInt(m.get("num3").toString());
			num4Arrays[i] = Integer.parseInt(m.get("num4").toString());
			num5Arrays[i] = Integer.parseInt(m.get("num5").toString());
			num6Arrays[i] = Integer.parseInt(m.get("num6").toString());
			num7Arrays[i] = Integer.parseInt(m.get("num7").toString());
			num8Arrays[i] = Integer.parseInt(m.get("num8").toString());

			// 开出号码个位与十位相加的合为单数时为合数单，反之为合数双。
			hedanshuang1Arrays[i] = Integer.parseInt(m.get("num1").toString()) / 10 + Integer.parseInt(m.get("num1").toString()) % 10;
			hedanshuang2Arrays[i] = Integer.parseInt(m.get("num2").toString()) / 10 + Integer.parseInt(m.get("num2").toString()) % 10;
			hedanshuang3Arrays[i] = Integer.parseInt(m.get("num3").toString()) / 10 + Integer.parseInt(m.get("num3").toString()) % 10;
			hedanshuang4Arrays[i] = Integer.parseInt(m.get("num4").toString()) / 10 + Integer.parseInt(m.get("num4").toString()) % 10;
			hedanshuang5Arrays[i] = Integer.parseInt(m.get("num5").toString()) / 10 + Integer.parseInt(m.get("num5").toString()) % 10;
			hedanshuang6Arrays[i] = Integer.parseInt(m.get("num6").toString()) / 10 + Integer.parseInt(m.get("num6").toString()) % 10;
			hedanshuang7Arrays[i] = Integer.parseInt(m.get("num7").toString()) / 10 + Integer.parseInt(m.get("num7").toString()) % 10;
			hedanshuang8Arrays[i] = Integer.parseInt(m.get("num8").toString()) / 10 + Integer.parseInt(m.get("num8").toString()) % 10;

			longhu1Arrays[i] = Integer.parseInt(m.get("num1").toString()) > Integer.parseInt(m.get("num8").toString()) ? 1 : 0;
			longhu2Arrays[i] = Integer.parseInt(m.get("num2").toString()) > Integer.parseInt(m.get("num7").toString()) ? 1 : 0;
			longhu3Arrays[i] = Integer.parseInt(m.get("num3").toString()) > Integer.parseInt(m.get("num6").toString()) ? 1 : 0;
			longhu4Arrays[i] = Integer.parseInt(m.get("num4").toString()) > Integer.parseInt(m.get("num5").toString()) ? 1 : 0;
			gyhArray[i] = Integer.parseInt(m.get("num1").toString()) + Integer.parseInt(m.get("num2").toString()) + Integer.parseInt(m.get("num3").toString())
					+ Integer.parseInt(m.get("num4").toString()) + Integer.parseInt(m.get("num5").toString()) + Integer.parseInt(m.get("num6").toString())
					+ Integer.parseInt(m.get("num7").toString()) + Integer.parseInt(m.get("num8").toString());

		}
		Map<String, int[]> maps = new HashMap<String, int[]>();
		maps.put("num1", num1Arrays);
		maps.put("num2", num2Arrays);
		maps.put("num3", num3Arrays);
		maps.put("num4", num4Arrays);
		maps.put("num5", num5Arrays);
		maps.put("num6", num6Arrays);
		maps.put("num7", num7Arrays);
		maps.put("num8", num8Arrays);
		// 总和加入到mun11
		maps.put("num9", gyhArray);

		maps.put("num1lh", longhu1Arrays);
		maps.put("num2lh", longhu2Arrays);
		maps.put("num3lh", longhu3Arrays);
		maps.put("num4lh", longhu4Arrays);

		maps.put("num1h", hedanshuang1Arrays);
		maps.put("num2h", hedanshuang2Arrays);
		maps.put("num3h", hedanshuang3Arrays);
		maps.put("num4h", hedanshuang4Arrays);
		maps.put("num5h", hedanshuang5Arrays);
		maps.put("num6h", hedanshuang6Arrays);
		maps.put("num7h", hedanshuang7Arrays);
		maps.put("num8h", hedanshuang8Arrays);

		// 初始化公共数组
		int[] numCommonArray = new int[list.size()];
		int[] numLhCommonArray = new int[list.size()];
		int[] numHCommonArray = new int[list.size()];
		Map<String, Integer> returnMap = null;
		// 因为有冠亚和，所以总球数需要加1
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId) + 1;
		returnMap = new HashMap<String, Integer>();
		// 循环当前彩种球数
		for (int i = 1; i <= num; i++) {
			// returnMap = new HashMap<String, Integer>();
			numCommonArray = maps.get("num" + i);// 获取当前处理字段的数组。
			if (numCommonArray == null) {
				continue;
			}
			// 处理龙虎长龙，龙虎只需要1-5号球
			if (i < 5) {
				numLhCommonArray = maps.get("num" + i + "lh");// 获取当前处理字段的数组。
				if (numLhCommonArray == null) {
					continue;
				}
				returnMap = processCllktx(numLhCommonArray, returnMap, i, LotteryConstant.LONG, LotteryConstant.HU);
			}
			// i等于9的时候是总和，总和的大小和普通大小的阈值不一样，所以分开写
			if (i < 9) {
				// 处理大小长龙
				returnMap = processCllktx(numCommonArray, returnMap, i, LotteryConstant.DA, LotteryConstant.XIAO);
				numHCommonArray = maps.get("num" + i + "h");// 获取当前处理字段的数组。
				// 处理合单双
				returnMap = processCllktx(numHCommonArray, returnMap, i, LotteryConstant.HESHUANG, LotteryConstant.HEDAN);
			} else {
				// 处理总和大小
				returnMap = processCllktx(numCommonArray, returnMap, i, LotteryConstant.GYHDA, LotteryConstant.GYHXIAO);
			}
			// 处理单双长龙
			returnMap = processCllktx(numCommonArray, returnMap, i, LotteryConstant.SHUANG, LotteryConstant.DAN);
			// 处理尾大小
			returnMap = processCllktx(numCommonArray, returnMap, i, LotteryConstant.WDA, LotteryConstant.WXIAO);
			// map.put("num" + i, returnMap);
		}
		// 将Map转为List
		List ll = new ArrayList<Entry>(returnMap.entrySet());
		Collections.sort(ll, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		}); // 重新排序
		JSONObject resobj = new JSONObject();
		resobj.put("gdklsf_cllk", ll);
		redisservice.set(lottId + "_" + LotteryDict.cllk, resobj.toJSONString());

	}

	/**
	 * 
	 * @desc 根据类型处理长龙连开提醒：1-10号球大小单双，1-5龙虎，冠亚和的大小单双
	 * @author abo
	 * @date 2018年6月28日 下午4:09:49
	 * @param numCommonArray
	 *            号码数组
	 * @param returnMap
	 * @param i
	 *            当前是几号球的数组
	 * @param typeA
	 *            处理的类型，处理当前大小、单双、龙虎
	 * @param typeB
	 *            小或者单
	 * @return
	 */
	private Map<String, Integer> processCllktx(int[] numCommonArray, Map<String, Integer> returnMap, int i, String typeA, String typeB) {
		int count = 1;// 连续的数，不连续了需要清零
		String preDxStr = "";// 上一个大小格式的数字
		String numdx = "";
		for (int j = 0; j < numCommonArray.length; j++) {
			if (typeA.equals(LotteryConstant.DA)) {// 大于10就是大，否则是小
				numdx = numCommonArray[j] > 10 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.SHUANG) || typeA.equals(LotteryConstant.HESHUANG)) {// 对2%，等于0就是双数，否者就是单数
				numdx = numCommonArray[j] % 2 == 0 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.LONG)) {// 等于1就是龙，否则就是虎
				numdx = numCommonArray[j] == 1 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.GYHDA)) {// 总和的大小
				// 总和路珠指8个开出的号码的总和对应的大小、单双，其中总合为85至132为大，36至83为小，和值为84。
				if (numCommonArray[j] > 84) {
					numdx = typeA;
				} else if (numCommonArray[j] < 84) {
					numdx = typeB;
				} else if (numCommonArray[j] == 84) {
					numdx = "和";
				}
			} else if (typeA.equals(LotteryConstant.WDA)) {// 处理尾大尾小
				numdx = numCommonArray[j] % 10 > 4 ? typeA : typeB;
			}
			// System.out.print(numCommonArray[j]);
			// if (j == numCommonArray.length - 1) {
			// System.out.println(numdx);
			// } else {
			// System.out.print(numdx);
			// }
			// 是否是第一个球，如果是第一个球那么初始化
			if (!StringUtils.isNoneBlank(preDxStr)) {
				preDxStr = numdx;
				continue;
			} else {
				String b = "num" + i;
				// 如果相等，计数器加一
				if (numdx == preDxStr) {
					count += 1;
					continue;
				}
				// 存取条件1:当中断连续就会保存，2:当为中断连续，但是是数组最后一个数的情况
				if ((numdx != preDxStr && count > 1) || (j == numCommonArray.length - 1 && count > 1)) {
					if (preDxStr.equals(typeA)) {
						b += "_" + typeA;
					} else {
						b += "_" + typeB;
					}
					returnMap.put(b, count);
					count = 1;// 保存完毕，计数器初始化
				}
				// 如果不一样的就中断结束
				break;
			}
		}
		return returnMap;
	}

	/**
	 * @desc 总和路珠
	 * @author xg
	 * @author 2018年7月06日
	 */
	public void getZhlz(String lottId) {
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numstr, 200);
		if (list == null) {
			return;
		}
		ConcurrentHashMap<String, ArrayList<String>> bigMap = new ConcurrentHashMap<String, ArrayList<String>>();
		// 变量 0小 单 1大 双 2默认 用来记录上次记录
		ConcurrentHashMap<String, Integer> aMap = new ConcurrentHashMap<String, Integer>();
		for (Map map : list) {
			// 总和
			int total = 0;
			for (int i = 1; i <= num; i++) {
				Integer haoma = (Integer) map.get("num" + i);
				total += haoma;
			}
			// 统计大小
			out: if (aMap.get("continueXD") == null) {
				ArrayList<String> list2 = bigMap.get("xd");
				if (list2 == null) {
					list2 = new ArrayList<String>();
					list2.add("");
				}
				// 0为小 1为大 2为和 3定义默认
				Integer xd = aMap.get("xd");
				if (xd == null) {
					xd = 3;
				}
				Integer border = LotteryConstant.lotteryTotalBorderMap.get(lottId);
				Integer t = LotteryConstant.lotteryTotalBorderMap.get(lottId + "T");
				// 小
				Integer s;
				// 大
				Integer b;
				if (t == 0) {
					s = border - 1;
					b = border + 1;
				} else {
					s = border - 1;
					b = border;
				}
				if (total >= b) {
					if (xd == 1 || xd == 3) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "大");
					} else {
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continueXD", 1);
							break out;
						}
						list2.add("大");
					}
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "da", lottId);
					aMap.put("xd", 1);
				} else if (total <= s) {
					if (xd == 0 || xd == 3) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "小");
					} else {
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continueXD", 1);
							break out;
						}
						list2.add("小");
					}
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "xiao", lottId);
					aMap.put("xd", 0);
				} else {
					if (xd == 2 || xd == 3) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "和");
					} else {
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continueXD", 1);
							break out;
						}
						list2.add("和");
					}
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "he", lottId);
					aMap.put("xd", 2);
				}
				bigMap.put("xd", list2);

			} else if (aMap.get("continueXD") != null && aMap.get("continueDS") != null && aMap.get("continueZHWS") != null) {
				continue;
			}
			// 统计单双
			outB: if (aMap.get("continueDS") == null) {
				ArrayList<String> list3 = bigMap.get("ds");
				if (list3 == null) {
					list3 = new ArrayList<String>();
					list3.add("");
				}
				// 0为小 1为大 2为和 3定义默认
				Integer ds = aMap.get("ds");
				if (ds == null) {
					ds = 2;
				}
				if (total % 2 == 1) {
					if (ds == 0 || ds == 2) {
						String string = list3.get(list3.size() - 1);
						list3.set(list3.size() - 1, string + "单");
					} else {
						if (list3.size() >= 55) {
							// 列已经满标记
							aMap.put("continueDS", 1);
							break outB;
						}
						list3.add("单");
					}
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "dan", lottId);
					aMap.put("ds", 0);
				} else {
					if (ds == 1 || ds == 2) {
						String string = list3.get(list3.size() - 1);
						list3.set(list3.size() - 1, string + "双");
					} else {
						if (list3.size() >= 55) {
							// 列已经满标记
							aMap.put("continueDS", 1);
							break outB;
						}
						list3.add("双");
					}
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "shuang", lottId);
					aMap.put("ds", 1);
				}
				bigMap.put("ds", list3);
			} else if (aMap.get("continueXD") != null && aMap.get("continueDS") != null && aMap.get("continueZHWS") != null) {
				continue;
			}

			// 统计总和尾数
			if (aMap.get("continueZHWS") == null) {
				ArrayList<String> list4 = bigMap.get("zhwsdx");
				if (list4 == null) {
					list4 = new ArrayList<String>();
					list4.add("");
				}
				// 0为小 1为大 2为和 3定义默认
				Integer zhwsdx = aMap.get("zhwsdx");
				if (zhwsdx == null) {
					zhwsdx = 2;
				}
				int p = total % 10;
				if (p <= 4) {
					if (zhwsdx == 0 || zhwsdx == 2) {
						String string = list4.get(list4.size() - 1);
						list4.set(list4.size() - 1, string + "小");
					} else {
						if (list4.size() >= 55) {
							// 列已经满标记
							aMap.put("continueZHWS", 1);
							continue;
						}
						list4.add("小");
					}
					LotteryUtil.countNum("numzhws", bigMap, map.get("time").toString(), "xiao", lottId);
					aMap.put("zhwsdx", 0);
				} else {
					if (zhwsdx == 1 || zhwsdx == 2) {
						String string = list4.get(list4.size() - 1);
						list4.set(list4.size() - 1, string + "大");
					} else {
						if (list4.size() >= 55) {
							// 列已经满标记
							aMap.put("continueZHWS", 1);
							continue;
						}
						list4.add("大");
					}
					LotteryUtil.countNum("numzhws", bigMap, map.get("time").toString(), "da", lottId);
					aMap.put("zhwsdx", 1);
				}
				bigMap.put("zhwsdx", list4);
			} else if (aMap.get("continueXD") != null && aMap.get("continueDS") != null && aMap.get("continueZHWS") != null) {
				continue;
			}
		}
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		// System.out.println(lottId+":"+bigMap);
		JSONObject resobj = new JSONObject(new HashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.zhlz, resobj.toJSONString());

	}

	/**
	 * @desc 龙虎路珠
	 * @author xg
	 * @author 2018年7月17日
	 */
	public void getLhlz(String lottId) {
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numstr, 200);
		if (list == null) {
			return;
		}
		// 变量0 单 1 双 2默认 用来记录上次单双
		Map<String, ArrayList<String>> bigMap = new HashMap<String, ArrayList<String>>();
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		for (Map<String, Object> map : list) {
			// 统计大小
			for (int i = 1; i <= num / 2; i++) {
				List list2 = (List) bigMap.get("num" + i);
				Integer a = aMap.get("num" + i);
				if (a == null) {
					// a定义默认变量
					a = 3;
				}
				if (list2 == null) {
					list2 = new ArrayList<String>();
					list2.add("");
				}
				// 如果大于55列就停下来
				if (aMap.get("continue" + i) != null) {
					continue;
				}
				Integer haoma = (Integer) map.get("num" + i);
				Integer haomaB = (Integer) map.get("num" + (num + 1 - i));
				if (haoma > haomaB) {
					if (a == 0 || a == 3) {
						String string = (String) list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "龍");
					} else {
						if (list2.size() >= LotteryConstant.LZ_COLUMN_NUM) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("龍");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "long", lottId);
					aMap.put("num" + i, 0);
				} else if (haoma < haomaB) {
					if (a == 1 || a == 3) {
						String string = (String) list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "虎");
					} else {
						if (list2.size() >= LotteryConstant.LZ_COLUMN_NUM) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("虎");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "hu", lottId);
					aMap.put("num" + i, 1);
				} else {
					if (a == 2 || a == 3) {
						String string = (String) list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "和");
					} else {
						if (list2.size() >= LotteryConstant.LZ_COLUMN_NUM) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("和");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "he", lottId);
					aMap.put("num" + i, 2);
					// 统计默认为0
					LotteryUtil.initNum("num" + i, bigMap, "he");
				}
				bigMap.put("num" + i, (ArrayList<String>) list2);
			}
		}

		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		// System.out.println(lottId + ":" + bigMap);
		JSONObject resobj = new JSONObject(new HashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.lhlz, resobj.toJSONString());
	}
}
