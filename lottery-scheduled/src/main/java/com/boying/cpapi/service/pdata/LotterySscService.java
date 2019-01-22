package com.boying.cpapi.service.pdata;

import io.lottery.common.config.LotteryDict;
import io.lottery.common.utils.DateUtils;
import io.lottery.common.utils.ExceptionUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.BjscUtils;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;
import com.boying.cpapi.util.Yilou.SscYilouHelp;

/**
 * @desc 时时彩业务处理
 * @author xg
 * @author 2018年6月27日
 */
@Slf4j
@Service
public class LotterySscService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * 时时彩业务处理（包含重庆，新疆，天津）
	 * 
	 * @param lottId
	 */
	public void execute(String lottId) {
		// 时时彩开奖记录
		this.getKjjl(lottId, null);
		// 重庆时时彩每日长龙
		this.getMrcl(lottId);
		// 重庆时时彩 长龙连开
		this.getCllktx(lottId);
		// 双号提醒
		this.getShtj(lottId);
		// 时时彩遗漏数据
		this.getSscYilou(lottId);
		// // 重启时时彩推荐计划
		this.recommend(lottId);
		// 开奖视频
		this.lotteryVideo();
	}

	/**
	 * @Description :重庆时时彩推荐计划
	 * @author 老王
	 *         <p>
	 * 
	 *         <p>
	 * 
	 *         <P>
	 * 
	 */
	public void recommend(String lottId) {
		if (!lottId.equals(LotteryConstant.CQSSC)) {
			return;// 只做重庆时时彩
		}

		String tablename = LotteryConstant.tableNameMap.get(lottId);
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		String key = lottId + "_" + LotteryDict.tjjh;
		// 查询数据库数据
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 1);
		// 获取到最新的一条数据
		if (list == null || list.size() == 0) {
			return;
		}
		Map<String, Object> newOne = list.get(0);
		String periods = newOne.get("periods").toString();
		// 获取到redis里面最新一条数据。有可能为空。
		// 得到所有的数据，因为要做统计
		// 判断当前期的预测数据是否存在，存在就显示当前期数
		Object oldObj = redisservice.getHashMap(key, periods);
		if (oldObj != null) {
			Map<String, Object> oldData = this.genOld(newOne, key);
			if (oldData != null) {
				redisservice.setHashMap(key, oldData.get("periods").toString(), new JSONObject(oldData).toJSONString());
			}
		}

		// 获取到预测数据
		// String tjperiods = BjscUtils.getNextPeriod(periods, lottId);
		JSONObject json = JSONObject.parseObject(redisservice.get(lottId + "_" + LotteryDict.zxkj));
		String tjperiods = json.getString("xqqs");
		Object obj = redisservice.getHashMap(lottId + "_" + LotteryDict.lmtjjh, tjperiods);
		Map<String, Object> planData = BjscUtils.genPlan(tjperiods, lottId, obj);
		if (planData != null) {
			redisservice.setHashMap(key, tjperiods, new JSONObject(planData).toJSONString());
		}
	}

	/**
	 * @Description :处理老的数据
	 * @author 老王
	 *         <p>
	 * 
	 *         <p>
	 * 
	 *         <P>
	 * 
	 */
	public Map<String, Object> genOld(Map<String, Object> newOne, String key) {
		Map<Object, Object> temp = redisservice.getHashMaps(key);
		String time = newOne.get("time").toString();
		String periods = newOne.get("periods").toString();
		if (temp == null || time == null) {
			return null;
		}
		Object obj = temp.get(periods);
		// 假如没有预测，则自动生成对的数据
		if (obj == null) {
			newOne = BjscUtils.genRightData(newOne, 5);
			return newOne;
		}

		Map<String, Object> oldOne = JSON.parseObject(obj.toString());// 预测数据
		newOne = BjscUtils.genRightData(newOne, oldOne, 5);
		// @ FIXME 统计数据
		int countYing = BjscUtils.countData(temp, "赢");
		int countShu = BjscUtils.countData(temp, "输");
		if ("赢".equals(newOne.get("result"))) {
			countYing = countYing + 1;
		} else {
			countShu = countShu + 1;
		}
		newOne.put("ying", countYing);
		newOne.put("shu", countShu);
		return newOne;
	}

	/**
	 * 
	 * @desc 时时彩开奖记录（包含重庆，新疆，天津）
	 * @author abo
	 * @param date
	 * @date 2018年6月27日 下午3:45:00
	 */
	public void getKjjl(String lottId, String date) {
		/* for (String lottId : LotteryConstant.tableNameMap.keySet()) { */
		// 重庆，新疆，天津ssc
		if (lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.XJSSC) || lottId.equals(LotteryConstant.TJSSC)) {
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
				// 总和
				int sum = num1 + num2 + num3 + num4 + num5;
				// 总和大小
				String zhDx = sum > 22 ? LotteryConstant.DA : LotteryConstant.XIAO;
				// 总和单双
				String zhDs = sum % 2 == 0 ? LotteryConstant.SHUANG : LotteryConstant.DAN;
				// 龙虎和
				String lh1 = LotteryConstant.HE;
				if (num1 > num5) {
					lh1 = LotteryConstant.LONG;
				} else if (num1 < num5) {
					lh1 = LotteryConstant.HU;
				}

				int[] qs = { num1, num2, num3 };// 前三
				int[] zs = { num2, num3, num4 };// 中三
				int[] hs = { num3, num4, num5 };// 后三

				String qsStr = processIntArray(qs);
				String zsStr = processIntArray(zs);
				String hsStr = processIntArray(hs);

				Map<String, Object> resmap = new HashMap<String, Object>();
				List<Object> numberlist = new ArrayList<Object>();
				for (String position : numStr.split(",")) {
					numberlist.add(m.get(position));
				}
				resmap.put("rank", numberlist);
				resmap.put("periods", m.get("periods"));
				resmap.put("starttime", m.get("starttime"));
				resmap.put("lh1", lh1);
				resmap.put("gyhSum", sum + "");
				resmap.put("zhDx", zhDx);
				resmap.put("zhDs", zhDs);
				resmap.put("qs", qsStr);
				resmap.put("zs", zsStr);
				resmap.put("hs", hsStr);
				JSONObject resobj = new JSONObject(resmap);
				redisservice.setHashMap(lottId + "_" + LotteryDict.kjjl, resmap.get("periods"), resobj.toJSONString());
			}
		}
		// }
	}

	/**
	 * 
	 * @desc 处理前三、中三、后三类型的方法，数组三个一样的数字是包子，2个一样是对子，每个数字差1是顺子，什么都没有是杂六
	 * @author abo
	 * @date 2018年7月2日 下午6:48:36
	 * @param array
	 * @return
	 */
	private String processIntArray(int[] array) {
		String type = LotteryConstant.ZL;
		Arrays.sort(array);
		// 对子
		if (array[0] == array[1] || array[1] == array[2]) {
			type = LotteryConstant.DZ;
		} else if (array[0] == array[1] && array[0] == array[2]) {
			type = LotteryConstant.BZ;
		} else if (array[2] - 1 == array[1] && array[1] - 1 == array[0]) {
			type = LotteryConstant.SZ;
		} else if (array[2] - 1 == array[1] || array[1] - 1 == array[0]) {
			type = LotteryConstant.BS;
		}
		return type;
	}

	/**
	 * 
	 * @desc 重庆时时彩每日长龙
	 * @author abo
	 * @date 2018年6月27日 下午7:40:37
	 */
	public void getMrcl(String lottId) {
		if (lottId.equals(LotteryConstant.CQSSC)) {
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
			// 龙虎数据
			int[] longhuArrays = new int[list.size()];
			// 总和
			int[] zhArray = new int[list.size()];
			// 把每个号段重新封装成一个数组
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> m = list.get(i);
				num1Arrays[i] = Integer.parseInt(m.get("num1").toString());
				num2Arrays[i] = Integer.parseInt(m.get("num2").toString());
				num3Arrays[i] = Integer.parseInt(m.get("num3").toString());
				num4Arrays[i] = Integer.parseInt(m.get("num4").toString());
				num5Arrays[i] = Integer.parseInt(m.get("num5").toString());
				// 由于时时彩的龙虎存在和的情况。
				if (Integer.parseInt(m.get("num1").toString()) == Integer.parseInt(m.get("num5").toString())) {
					longhuArrays[i] = -1;
				} else {
					longhuArrays[i] = Integer.parseInt(m.get("num1").toString()) > Integer.parseInt(m.get("num5").toString()) ? 1 : 0;
				}
				// 和
				zhArray[i] = Integer.parseInt(m.get("num1").toString()) + Integer.parseInt(m.get("num2").toString()) + Integer.parseInt(m.get("num3").toString())
						+ Integer.parseInt(m.get("num4").toString()) + Integer.parseInt(m.get("num5").toString());
			}
			Map<String, int[]> maps = new HashMap<String, int[]>();
			maps.put("num1", num1Arrays);
			maps.put("num2", num2Arrays);
			maps.put("num3", num3Arrays);
			maps.put("num4", num4Arrays);
			maps.put("num5", num5Arrays);
			maps.put("num6", zhArray);// 和
			// 初始化公共数组
			int[] numCommonArray = new int[list.size()];
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
				// i等于6的时候是总和，总和的大小和普通大小的阈值不一样，所以分开写
				if (i < 6) {
					// 处理大小长龙
					returnMap = processMrcl(numCommonArray, returnMap, i, LotteryConstant.DA, LotteryConstant.XIAO);
				} else {
//					System.out.println(Arrays.toString(numCommonArray));
					// 处理总和大小
					returnMap = processMrcl(numCommonArray, returnMap, i, LotteryConstant.GYHDA, LotteryConstant.GYHXIAO);
					if (longhuArrays == null) {
						continue;
					}
					returnMap = processMrcl(longhuArrays, returnMap, i, LotteryConstant.LONG, LotteryConstant.HU);
				}
				// 处理单双长龙
				returnMap = processMrcl(numCommonArray, returnMap, i, LotteryConstant.SHUANG, LotteryConstant.DAN);
				map.put("num" + i, returnMap);
			}
			JSONObject resobj = new JSONObject(map);
			// System.out.println(resobj.toJSONString());
			redisservice.setHashMap(lottId + "_" + LotteryDict.mrcl, currentDate, resobj.toJSONString());
		}
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
			if (typeA.equals(LotteryConstant.DA)) {// 大于5就是大，否则是小
				numdx = numCommonArray[j] > 4 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.SHUANG)) {// 对2%，等于0就是双数，否者就是单数
				numdx = numCommonArray[j] % 2 == 0 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.LONG)) {// 等于1就是龙，0等于虎，-1是和
				// numdx = numCommonArray[j] == 1 ? typeA : typeB;
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
				numdx = numCommonArray[j] > 22 ? typeA : typeB;
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
					// returnMap.put(b, (returnMap.get(b) == null ? 0 :
					// returnMap.get(b)) + 1);
					// count = 1;// 保存完毕，计数器初始化
					map = (Map<String, Object>) returnMap.get(b);
					if (map == null) {
						map = new HashMap<String, Object>();
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
	 * @desc 重庆时时彩 长龙连开提醒：1-10号球大小单双，1龙虎，总和的大小单双
	 * @author abo
	 * @date 2018年6月30日 下午1:10:48
	 */
	public void getCllktx(String lottId) {
		if (lottId.equals(LotteryConstant.CQSSC)) {
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
			// 龙虎数据
			int[] longhuArrays = new int[list.size()];
			// 总和
			int[] zhArray = new int[list.size()];
			// 把每个号段重新封装成一个数组
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> m = list.get(i);
				num1Arrays[i] = Integer.parseInt(m.get("num1").toString());
				num2Arrays[i] = Integer.parseInt(m.get("num2").toString());
				num3Arrays[i] = Integer.parseInt(m.get("num3").toString());
				num4Arrays[i] = Integer.parseInt(m.get("num4").toString());
				num5Arrays[i] = Integer.parseInt(m.get("num5").toString());
				// 由于时时彩的龙虎存在和的情况。
				if (Integer.parseInt(m.get("num1").toString()) == Integer.parseInt(m.get("num5").toString())) {
					longhuArrays[i] = -1;
				} else {
					longhuArrays[i] = Integer.parseInt(m.get("num1").toString()) > Integer.parseInt(m.get("num5").toString()) ? 1 : 0;
				}
				// 和
				zhArray[i] = Integer.parseInt(m.get("num1").toString()) + Integer.parseInt(m.get("num2").toString()) + Integer.parseInt(m.get("num3").toString())
						+ Integer.parseInt(m.get("num4").toString()) + Integer.parseInt(m.get("num5").toString());
			}
			Map<String, int[]> maps = new HashMap<String, int[]>();
			maps.put("num1", num1Arrays);
			maps.put("num2", num2Arrays);
			maps.put("num3", num3Arrays);
			maps.put("num4", num4Arrays);
			maps.put("num5", num5Arrays);
			maps.put("num6", zhArray);// 总和
			// 初始化公共数组
			int[] numCommonArray = new int[list.size()];
			Map<String, Integer> returnMap = null;
			long s = System.currentTimeMillis();
			Map<String, Object> map = new HashMap<String, Object>();
			// 因为有总和，所以总球数需要加1
			// 当前彩票的球数
			int num = LotteryConstant.lotteryNumMap.get(lottId) + 1;
			// 循环当前彩种球数
			for (int i = 1; i <= num; i++) {
				returnMap = new HashMap<String, Integer>();
				numCommonArray = maps.get("num" + i);// 获取当前处理字段的数组。
				if (numCommonArray == null) {
					continue;
				}
				// 处理龙虎长龙，龙虎只需要1号球
				if (i < 2) {
					if (longhuArrays == null) {
						continue;
					}
					returnMap = processCllktx(longhuArrays, returnMap, i, LotteryConstant.LONG, LotteryConstant.HU);
				}
				// i等于6的时候是总和，总和的大小和普通大小的阈值不一样，所以分开写
				if (i < 6) {
					// 处理大小长龙
					returnMap = processCllktx(numCommonArray, returnMap, i, LotteryConstant.DA, LotteryConstant.XIAO);
				} else {
					// 处理总和大小
					returnMap = processCllktx(numCommonArray, returnMap, i, LotteryConstant.GYHDA, LotteryConstant.GYHXIAO);
				}
				// 处理单双长龙
				returnMap = processCllktx(numCommonArray, returnMap, i, LotteryConstant.SHUANG, LotteryConstant.DAN);
				map.put("num" + i, returnMap);
			}

			JSONObject resobj = new JSONObject(map);
			redisservice.set(lottId + "_" + LotteryDict.cllk, resobj.toJSONString());
		}
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
			if (typeA.equals(LotteryConstant.DA)) {// 大于5就是大，否则是小
				numdx = numCommonArray[j] > 4 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.SHUANG)) {// 对2%，等于0就是双数，否者就是单数
				numdx = numCommonArray[j] % 2 == 0 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.LONG)) {// 等于1就是龙，0等于虎，-1是和
				// numdx = numCommonArray[j] == 1 ? typeA : typeB;
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
				numdx = numCommonArray[j] > 22 ? typeA : typeB;
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
				// // 如果相等，计数器加一
				// if (numdx == preDxStr) {
				// count += 1;
				// }
				// // 存取条件1:当中断连续就会保存，2:当为中断连续，但是是数组最后一个数的情况
				// if ((numdx != preDxStr && count > 1) || (j ==
				// numCommonArray.length - 1 &&
				// count > 1)) {
				// b += count;
				// if (preDxStr.equals(typeA)) {
				// b += "_" + typeA;
				// } else if (preDxStr.equals(typeB)) {
				// b += "_" + typeB;
				// }
				// returnMap.put(b, (returnMap.get(b) == null ? 0 :
				// returnMap.get(b)) + 1);
				// count = 1;// 保存完毕，计数器初始化
				// }
				// preDxStr = numdx;
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
					} else if (preDxStr.equals(typeB)) {
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
	 * 
	 * @desc 双号提醒 重庆时时彩，天津时时彩，新疆时时彩
	 * @author abo
	 * @date 2018年7月4日 下午3:19:57
	 */
	public void getShtj(String lottId) {
		/* for (String lottId : LotteryConstant.tableNameMap.keySet()) { */
		if (lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.XJSSC) || lottId.equals(LotteryConstant.TJSSC)) {
			// 今天的日期
			String currentDate = LotteryUtil.countDayOfNow(lottId);
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			// 查询球列的字符串
			String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 查询数据库数据
			List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByDate(tablename, numStr, currentDate);
			Map<String, Object> map = new HashMap<String, Object>();
			// 本期双号
			String bqsh = "";
			// 今日开出双号期数
			int jrkcshqs = 0;
			List<int[]> l = new ArrayList<int[]>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> m = list.get(i);
				int num1 = Integer.parseInt(m.get("num1").toString());
				int num2 = Integer.parseInt(m.get("num2").toString());
				int num3 = Integer.parseInt(m.get("num3").toString());
				int num4 = Integer.parseInt(m.get("num4").toString());
				int num5 = Integer.parseInt(m.get("num5").toString());
				int[] temp = { num1, num2, num3, num4, num5 };
				// 重新声明一个数组，新的数组不用排序
				int[] temp1 = { num1, num2, num3, num4, num5 };
				l.add(temp1);
				Arrays.sort(temp);// 从小到大重新排序
				int prenum = -1;
				// 本期双号只在最新的一期才计算
				if (i == 0) {
					// System.out.println(Arrays.toString(temp));
					for (int n : temp) {
						if (prenum == n) {
							bqsh += n + ",";
							prenum = -1;
						} else {
							prenum = n;
						}
					}
				}
				// 计算今日开出双号期数
				for (int n : temp) {
					if (prenum == n) {
						jrkcshqs++;
						break;
					} else {
						prenum = n;
					}
				}

			}
			int[] preArray = null;
			// 下期重复开出期数：下期重复开出期数是指上一期和下一期有一个数字是一样的，位置也是一样的
			int xqcfkcqs = 0;
			for (int[] array : l) {
				if (preArray == null) {
					preArray = array;
					continue;
				}
				if (preArray[0] == array[0] || preArray[1] == array[1] || preArray[2] == array[2] || preArray[3] == array[3] || preArray[4] == array[4]) {
					xqcfkcqs++;
				}
				preArray = array;
			}
			// 当前重复开出遗漏
			int dqcfkcyl = 0;
			preArray = null;
			for (int[] array : l) {
				if (preArray == null) {
					preArray = array;
					continue;
				}
				if (preArray[0] == array[0] || preArray[1] == array[1] || preArray[2] == array[2] || preArray[3] == array[3] || preArray[4] == array[4]) {
					break;
				}
				dqcfkcyl++;
				preArray = array;
			}
			// 今日最大遗漏
			int jrzdyl = 0;
			int zdylTemp = 0;// 最大遗漏临时变量，存当前的遗漏数
			preArray = null;
			for (int[] array : l) {
				if (preArray == null) {
					preArray = array;
					continue;
				}
				if (preArray[0] == array[0] || preArray[1] == array[1] || preArray[2] == array[2] || preArray[3] == array[3] || preArray[4] == array[4]) {
					zdylTemp = 0;
				} else {
					zdylTemp++;
				}
				preArray = array;
				// 保存最大的
				if (zdylTemp > jrzdyl) {
					jrzdyl = zdylTemp;
				}
			}
			map.put("bqsh", bqsh);// 本期双号
			map.put("jrkcshqs", jrkcshqs + "");// 今日开出双号期数
			map.put("xqcfkcqs", xqcfkcqs + "");// 下期重复开出期数
			map.put("dqcfkcyl", dqcfkcyl + "");// 当前重复开出遗漏
			map.put("jrzdyl", jrzdyl + "");// 今日最大遗漏
			JSONObject resobj = new JSONObject(map);
			redisservice.set(lottId + "_" + LotteryDict.shtj, resobj.toJSONString());
			// }
		}
	}

	/**
	 * 三个时时彩遗漏数据
	 */
	public void getSscYilou(String lottId) {
		if (lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.XJSSC) || lottId.equals(LotteryConstant.TJSSC)) {
			Date tdate = new Date(System.currentTimeMillis());
			String currentDate = DateUtil.date2String(tdate, DateUtil.PATTERN_DATE);
			;
			// 新疆时时彩，全天96期，每天10:10—02:00,如果在凌晨2点之前的数据，认为是昨天的数据
			if (LotteryConstant.XJSSC.equals(lottId)) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, 2);
				cal.set(Calendar.MINUTE, 2);// 设置慢两分钟，确保时间准确
				if (new Date().before(cal.getTime())) {
					currentDate = DateUtil.getDateString(tdate, null, null, -1, null, null, null, DateUtil.PATTERN_DATE);
				}
			}
			// 本周第一天(星期一算第一天)
			Date wd = DateUtil.getWeekStartDate();
			// 本月第一天（1号）
			Date md = DateUtil.getFirstDayOfMonth();
			String monthfirstDate = DateUtil.date2String(md, DateUtil.PATTERN_DATE);
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			List<Map<String, Object>> mlist = lotteryProcessMapper.selectLotteryAfterDate(tablename, numStr, monthfirstDate);// 本月数据
			List<Map<String, Object>> wlist = new ArrayList<Map<String, Object>>();// 定义本周数据
			List<Map<String, Object>> clist = new ArrayList<Map<String, Object>>();// 定义当天数据
			for (Map<String, Object> m : mlist) {
				if (((Date) m.get("starttime")).after(wd)) {
					wlist.add(m);
				}
				if (currentDate.equals(m.get("time").toString())) {
					clist.add(m);
				}
			}
			// 时时彩所有位置遗漏数据
			JSONObject wzylobj = SscYilouHelp.sscwzyl(clist, wlist, mlist);
			// 时时彩所有号码遗漏数据
			JSONObject hmylobj = SscYilouHelp.sschmyl(clist, wlist, mlist);
			// 位置遗漏
			redisservice.set(lottId + "_" + LotteryDict.wzyl, wzylobj.toJSONString());
			// 号码遗漏
			redisservice.set(lottId + "_" + LotteryDict.hmyl, hmylobj.toJSONString());
		}
	}

	/**
	 * @desc 彩票直播开奖 新疆时时彩
	 * @author xg
	 * @param lottId
	 * @author 2018年7月14日
	 */
	public void lotteryVideo() {
		String lottId = LotteryConstant.XJSSC;
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		Map<String, Object> map = lotteryProcessMapper.selectLotteryLive(tablename, numstr, LotteryUtil.countDayOfNow(lottId));
		if (map == null) {
			return;
		}
		Long cPeriod = Long.parseLong(map.get("periods").toString());
		String cDrawTime = "";
		String cDrawNumbers = "";
		String cDrawDate = cPeriod.toString();
		Long nPrePeriod;
		Long nPeriod = Long.parseLong(JSONObject.parseObject(redisservice.get(lottId + "_" + LotteryDict.zxkj)).getString("xqqs"));
		String nPreDrawTime = "";
		Long nNextPeriod;
		String nDrawDate = nPeriod.toString();
		String nDrawTime = "";
		String nInterval = "";
		String nAwardInterval = "";
		String nDelayInterval = "30";
		int interval = 600000;
		int intervalMinute = 10;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cDrawTime = formatter.format(map.get("starttime"));
		nPreDrawTime = cDrawTime;

		for (int i = 1; i <= LotteryConstant.lotteryNumMap.get(lottId); i++) {
			cDrawNumbers += map.get("num" + i) + ",";
		}
		cDrawNumbers = cDrawNumbers.substring(0, cDrawNumbers.length() - 1);

		String strCurrentTime = formatter.format(new Date());
		long dateToStamp = 0;
		long currentTime = 0;
		try {
			dateToStamp = DateUtils.dateToStamp(cDrawTime);
			currentTime = DateUtils.dateToStamp(strCurrentTime);
		} catch (ParseException e) {
			e.printStackTrace();
			log.error(ExceptionUtil.getExceptionStackTrace(e));
		}

		nInterval = String.valueOf(interval - (currentTime - dateToStamp));
		nAwardInterval = nInterval;

		cPeriod = Long.parseLong(cPeriod.toString().substring(cPeriod.toString().length() - 2));
		nPrePeriod = cPeriod;
		nPeriod = cPeriod + 1;
		nNextPeriod = nPeriod;
		
		HashMap<String, Object> aMap = new HashMap<String,Object>();
		HashMap<String, Object> currentMap = new HashMap<String,Object>();
		HashMap<String, Object> nextMap = new HashMap<String,Object>();
		aMap.put("current", currentMap);
		aMap.put("next", nextMap);
		aMap.put("time", 1534396981014L);
		currentMap.put("periodNumber", cPeriod);
		currentMap.put("period", cDrawDate);
		currentMap.put("awardTime", cDrawTime);
		currentMap.put("awardNumbers", cDrawNumbers);
		nextMap.put("periodNumber", nPeriod);
		nextMap.put("period",nDrawDate);
		nextMap.put("awardTime", nDrawTime);
		nextMap.put("awardTimeInterval", nAwardInterval);
		nextMap.put("delayTimeInterval", nDelayInterval);
		/*String a = "{\"time\":131760243345324147,\"current\":{\"periodNumber\":" + cPeriod + ",\"period\":" + cDrawDate + ",\"awardTime\":\"" + cDrawTime
				+ "\",\"awardNumbers\":\"" + cDrawNumbers + "\"},\"next\":{\"periodNumber\":" + nPeriod + ",\"period\":" + nDrawDate + ",\"awardTime\":\"" + nDrawTime
				+ "\",\"awardTimeInterval\":" + nAwardInterval + ",\"delayTimeInterval\":" + nDelayInterval + "}}";*/
		// System.out.println(lottId+":"+a);
		redisservice.setHashMap("video", lottId + "_a", JSONObject.toJSON(aMap).toString());
		// icpapi/GetLotteryTime 接口数据
	}
}
