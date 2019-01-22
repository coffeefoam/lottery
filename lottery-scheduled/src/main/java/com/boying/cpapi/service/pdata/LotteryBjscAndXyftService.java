package com.boying.cpapi.service.pdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.swing.text.html.parser.Entity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.BjscUtils;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;
import com.boying.cpapi.util.Yilou.PK10YilouHelp;

import io.lottery.common.config.LotteryDict;

/**
 * 
 * @desc 北京赛车和幸运飞艇service
 * @author abo
 * @date 2018年6月27日 下午2:58:59
 *
 */
@Service
public class LotteryBjscAndXyftService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * @author ms
	 * @param lottId
	 */
	public void execute(String lottId) {
		// 北京赛车和幸运飞艇开奖记录
		this.getKjjl(lottId, null);
		// 北京赛车今天的双面统计
		this.getJrsmtj(lottId);
		// 每日长龙
		this.getMrcl(lottId);
		// 长龙连开提醒
		this.getCllktx(lottId);
		// PK10 遗漏统计
		this.getPk10Yilou(lottId);
		// PK10 冠亚和遗漏统计
		this.getPk10GyhYilou(lottId);
		// 冠亚和历史
		this.gyhls(lottId);
		// 冠亚和历史
		this.lhls(lottId);
		// 前后路珠
		this.getHmqhlz(lottId);
		// 冠亚和路珠
		this.getGyhlz(lottId);
		// 两对投注参考 -北京赛车pk10
		this.getLmtzck(lottId, null);
		// 推荐计划
		this.recommend(lottId);
		// 龙虎路珠
		this.getLhlz(lottId);
		// 北京赛车h5视频分析数据封装
		this.lotteryVideo();
	}

	/**
	 * @Description :北京赛车和幸运飞艇推荐数据
	 * @author 老王
	 *         <p>
	 *         设置过期时间，key 在晚上12点的时候会自动删除掉
	 *         <p>
	 * 
	 *         <P>
	 * 
	 */
	// public void recommend(String lottId) {
	// if (lottId.equals(LotteryConstant.BJSC) ||
	// lottId.equals(LotteryConstant.XYFT)) {
	// String tablename = LotteryConstant.tableNameMap.get(lottId);
	// String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
	// String key = lottId + "_" + LotteryDict.tjjh;
	// // 查询数据库数据
	// List<Map<String, Object>> list =
	// lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 1);
	// // 获取到最新的一条数据
	// if (list == null || list.size() == 0) {
	// return;
	// }
	// Map<String, Object> newOne = list.get(0);
	// // 获取到redis里面最新一条数据。有可能为空。
	// // 得到所有的数据，因为要做统计
	// Map<String, Object> oldData = this.genOld(newOne, key);
	// if (oldData != null) {
	// redisservice.setHashMap(key, oldData.get("periods").toString(), new
	// JSONObject(oldData).toJSONString());
	// }
	//
	// // 获取到预测数据
	// Map<String, Object> planData = BjscUtils.genPlan((String)
	// newOne.get("periods"), 10,lottId);
	//
	// redisservice.setHashMap(key, planData.get("periods").toString(), new
	// JSONObject(planData).toJSONString());
	// }
	//
	// }

	public void recommend(String lottId) {
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
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

			// 计算当前期数数据
			// 获取到redis里面最新一条数据。有可能为空。
			// 得到所有的数据，因为要做统计
			Object oldObj = redisservice.getHashMap(key, periods);
			if (oldObj != null) {
				Map<String, Object> oldData = this.genOld(newOne, key);
				if (oldData != null) {
					redisservice.setHashMap(key, oldData.get("periods").toString(), new JSONObject(oldData).toJSONString());
				}
			}

			// 获取到预测数据
			// String tjperiods = BjscUtils.getNextPeriod(periods, lottId);
			// 获取最新一期的开奖期号
			JSONObject json = JSONObject.parseObject(redisservice.get(lottId + "_" + LotteryDict.zxkj));
			String tjperiods = json.getString("xqqs");
			Object obj = redisservice.getHashMap(lottId + "_" + LotteryDict.lmtjjh, tjperiods);
			Map<String, Object> planData = BjscUtils.genPlan(tjperiods, lottId, obj);
			if (planData != null) {
				redisservice.setHashMap(key, tjperiods, new JSONObject(planData).toJSONString());
//				System.out.println(tjperiods + ":" + new JSONObject(planData).toJSONString());

			}
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
		String time = newOne.get("time").toString();
		String periods = newOne.get("periods").toString();
		Map<Object, Object> temp = redisservice.getHashMaps(key);
		if (temp == null || time == null) {
			return null;
		}
		Object obj = temp.get(periods);
		// 假如没有预测，则自动生成对的数据
		if (obj == null) {
			newOne = BjscUtils.genRightData(newOne, 10);
			return newOne;
		}
		Map<String, Object> oldOne = JSON.parseObject(obj.toString());// 预测数据
		newOne = BjscUtils.genRightData(newOne, oldOne, 5);
		// @ FIXME 统计数据：两张方案1，每次都去统计，第二种，每次去取上一条的记录的记录值，然后进行增加，但是一旦缺一期数据，就无法统计
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

	// public Map<String, Object> genOld(Map<String, Object> newOne, String key)
	// {
	// Map<Object, Object> temp = redisservice.getHashMaps(key);
	// Map<Object, Map<String, Object>> today = new HashMap<>();
	// String time = newOne.get("time").toString();
	// String periods = newOne.get("periods").toString();
	// if (temp == null || time == null) {
	// return null;
	// }
	// Object obj = temp.get(periods);
	// // 假如没有预测，则自动生成对的数据
	// if (obj == null) {
	// newOne = BjscUtils.genRightData(newOne, 10);
	// return newOne;
	// }
	//
	// Map<String, Object> oldOne = JSON.parseObject(obj.toString());// 预测数据
	// Map<String, Object> kaijiang = JSON.parseObject(obj.toString());// 开奖数据
	//
	// newOne = BjscUtils.genRightData(newOne, oldOne, 5);
	// // @ FIXME 统计数据：两张方案1，每次都去统计，第二种，每次去取上一条的记录的记录值，然后进行增加，但是一旦缺一期数据，就无法统计
	// int countYing = BjscUtils.countData(temp, "赢");
	// int countShu = BjscUtils.countData(temp, "输");
	// if ("赢".equals(newOne.get("result"))) {
	// countYing = countYing + 1;
	// } else {
	// countShu = countShu + 1;
	// }
	// newOne.put("ying", countYing);
	// newOne.put("shu", countShu);
	// return newOne;
	//
	// }

	/**
	 * 
	 * @desc 北京赛车和幸运飞艇开奖记录
	 * @author abo
	 * @date 2018年6月27日 下午3:45:00
	 */
	public void getKjjl(String lottId, String date) {
		/* for (String lottId : LotteryConstant.tableNameMap.keySet()) { */
		// 北京赛车和新运飞艇
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			// 查询球列的字符串
			String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 当前彩票的球数
			int num = LotteryConstant.lotteryNumMap.get(lottId);
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
				int num9 = Integer.parseInt(m.get("num9").toString());
				int num10 = Integer.parseInt(m.get("num10").toString());
				// 冠亚和
				int gyhSum = num1 + num2;
				// 冠亚和大小
				String gyhDx = gyhSum >= 12 ? "大" : "小";
				// 冠亚和单双
				String gyhDs = gyhSum % 2 == 0 ? "双" : "单";
				// 龙虎1
				String lh1 = num1 > num10 ? "龙" : "虎";
				// 龙虎2
				String lh2 = num2 > num9 ? "龙" : "虎";
				// 龙虎3
				String lh3 = num3 > num8 ? "龙" : "虎";
				// 龙虎4
				String lh4 = num4 > num7 ? "龙" : "虎";
				// 龙虎5
				String lh5 = num5 > num6 ? "龙" : "虎";

				Map<String, Object> resmap = new HashMap<String, Object>();
				List<Object> numberlist = new ArrayList<Object>();
				for (String position : numStr.split(",")) {
					numberlist.add(m.get(position));
				}
				resmap.put("rank", numberlist);
				resmap.put("lh1", lh1);
				resmap.put("lh2", lh2);
				resmap.put("lh3", lh3);
				resmap.put("lh4", lh4);
				resmap.put("lh5", lh5);
				resmap.put("gyhSum", gyhSum);
				resmap.put("gyhDx", gyhDx);
				resmap.put("gyhDs", gyhDs);
				resmap.put("periods", m.get("periods"));
				resmap.put("starttime", m.get("starttime"));
				JSONObject resobj = new JSONObject(resmap);
				redisservice.setHashMap(lottId + "_" + LotteryDict.kjjl, resmap.get("periods"), resobj.toJSONString());
			}
			// }
		}
	}

	/**
	 * 
	 * @desc 北京赛车今天的双面统计
	 * @author abo
	 * @date 2018年6月27日 下午5:09:51
	 */
	public void getJrsmtj(String lottId) {
		if (lottId.equals(LotteryConstant.BJSC)) {
			// 今天的日期
			String currentDate = LotteryUtil.countDayOfNow(lottId);
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			// 查询球列的字符串
			String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 当前彩票的球数
			int num = LotteryConstant.lotteryNumMap.get(lottId);
			// 查询数据库数据
			List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByDate(tablename, numStr, currentDate);
			Map<String, Integer> tempMap = new LinkedHashMap<String, Integer>();
			for (Map m : list) {
				for (int i = 1; i <= num; i++) {
					int n = Integer.parseInt(m.get("num" + i).toString());
					String numda = "num" + i + "_da";
					String numxiao = "num" + i + "_xiao";
					String numdan = "num" + i + "_dan";
					String numshuang = "num" + i + "_shuang";
					// 大的集合数
					int nda = tempMap.get(numda) == null ? 0 : tempMap.get(numda);
					// 小的集合数
					int nxiao = tempMap.get(numxiao) == null ? 0 : tempMap.get(numxiao);
					// 单的集合数
					int ndan = tempMap.get(numdan) == null ? 0 : tempMap.get(numdan);
					// 双的集合数
					int nshuang = tempMap.get(numshuang) == null ? 0 : tempMap.get(numshuang);
					// 大于5以上算大
					if (n > 5) {
						tempMap.put(numda, nda + 1);
					} else {
						tempMap.put(numxiao, nxiao + 1);
					}
					// 对于2%
					if (n % 2 == 0) {
						tempMap.put(numshuang, nshuang + 1);
					} else {
						tempMap.put(numdan, ndan + 1);
					}
				}
			}
			// 按照号码进行分组
			Map<String, Integer> map = null;
			Map<String, Object> r = new LinkedHashMap<String, Object>();
			for (int i = 1; i <= num; i++) {
				map = new LinkedHashMap<String, Integer>();
				map.put("num" + i + "_da", tempMap.get("num" + i + "_da"));
				map.put("num" + i + "_xiao", tempMap.get("num" + i + "_xiao"));
				map.put("num" + i + "_dan", tempMap.get("num" + i + "_dan"));
				map.put("num" + i + "_shuang", tempMap.get("num" + i + "_shuang"));

				r.put("num" + i, map);
			}
			JSONObject resobj = new JSONObject(r);
			redisservice.set(lottId + "_" + LotteryDict.smtj, resobj.toJSONString());
		}
	}

	/**
	 * 
	 * @desc 每日长龙
	 * @author abo
	 * @date 2018年6月27日 下午7:40:37
	 */
	public void getMrcl(String lottId) {
		/* for (String lottId : LotteryConstant.tableNameMap.keySet()) { */
		// 北京赛车和新运飞艇
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
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
			int[] num9Arrays = new int[list.size()];
			int[] num10Arrays = new int[list.size()];
			// 龙虎数据
			int[] longhu1Arrays = new int[list.size()];
			int[] longhu2Arrays = new int[list.size()];
			int[] longhu3Arrays = new int[list.size()];
			int[] longhu4Arrays = new int[list.size()];
			int[] longhu5Arrays = new int[list.size()];
			// 冠亚和
			int[] gyhArray = new int[list.size()];

			// 把每个号段重新封装成一个数组
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> m = list.get(i);
				num1Arrays[i] = Integer.parseInt(m.get("num1").toString());
				num2Arrays[i] = Integer.parseInt(m.get("num2").toString());
				num3Arrays[i] = Integer.parseInt(m.get("num3").toString());
				num4Arrays[i] = Integer.parseInt(m.get("num4").toString());
				num5Arrays[i] = Integer.parseInt(m.get("num5").toString());
				num6Arrays[i] = Integer.parseInt(m.get("num6").toString());
				num7Arrays[i] = Integer.parseInt(m.get("num7").toString());
				num8Arrays[i] = Integer.parseInt(m.get("num8").toString());
				num9Arrays[i] = Integer.parseInt(m.get("num9").toString());
				num10Arrays[i] = Integer.parseInt(m.get("num10").toString());

				longhu1Arrays[i] = Integer.parseInt(m.get("num1").toString()) > Integer.parseInt(m.get("num10").toString()) ? 1 : 0;
				longhu2Arrays[i] = Integer.parseInt(m.get("num2").toString()) > Integer.parseInt(m.get("num9").toString()) ? 1 : 0;
				longhu3Arrays[i] = Integer.parseInt(m.get("num3").toString()) > Integer.parseInt(m.get("num8").toString()) ? 1 : 0;
				longhu4Arrays[i] = Integer.parseInt(m.get("num4").toString()) > Integer.parseInt(m.get("num7").toString()) ? 1 : 0;
				longhu5Arrays[i] = Integer.parseInt(m.get("num5").toString()) > Integer.parseInt(m.get("num6").toString()) ? 1 : 0;
				gyhArray[i] = Integer.parseInt(m.get("num1").toString()) + Integer.parseInt(m.get("num2").toString());

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
			maps.put("num9", num9Arrays);
			maps.put("num10", num10Arrays);
			// 冠亚和加入到mun11
			maps.put("num11", gyhArray);

			maps.put("num1lh", longhu1Arrays);
			maps.put("num2lh", longhu2Arrays);
			maps.put("num3lh", longhu3Arrays);
			maps.put("num4lh", longhu4Arrays);
			maps.put("num5lh", longhu5Arrays);

			// 初始化公共数组
			int[] numCommonArray = new int[list.size()];
			int[] numLhCommonArray = new int[list.size()];

			Map<String, Object> returnMap = null;
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			// 因为有冠亚和，所以总球数需要加1
			// 当前彩票的球数
			int num = LotteryConstant.lotteryNumMap.get(lottId) + 1;
			// 循环当前彩种球数
			for (int i = 1; i <= num; i++) {
				returnMap = new TreeMap<String, Object>();
				numCommonArray = maps.get("num" + i);// 获取当前处理字段的数组。
				if (numCommonArray == null) {
					continue;
				}
				// 处理龙虎长龙，龙虎只需要1-5号球
				if (i < 6) {
					numLhCommonArray = maps.get("num" + i + "lh");// 获取当前处理字段的数组。
					if (numLhCommonArray == null) {
						continue;
					}
					returnMap = processMrclDxOrDs(numLhCommonArray, i, returnMap, LotteryConstant.LONG, LotteryConstant.HU);
				}
				// i等于11的时候是冠亚和，冠亚和的大小和普通大小的阈值不一样，所以分开写
				if (i < 11) {
					// 处理大小长龙
					returnMap = processMrclDxOrDs(numCommonArray, i, returnMap, LotteryConstant.DA, LotteryConstant.XIAO);
				} else {
					// 处理冠亚和大小
					returnMap = processMrclDxOrDs(numCommonArray, i, returnMap, LotteryConstant.GYHDA, LotteryConstant.GYHXIAO);
				}
				// 处理单双长龙
				returnMap = processMrclDxOrDs(numCommonArray, i, returnMap, LotteryConstant.SHUANG, LotteryConstant.DAN);
				map.put("num" + i, returnMap);
			}
			JSONObject resobj = new JSONObject(map);
			// System.out.println(resobj.toJSONString());
			redisservice.setHashMap(lottId + "_" + LotteryDict.mrcl, currentDate, resobj.toJSONString());
		}
		// }
	}

	/**
	 * 
	 * @desc 根据类型处理每日长龙
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
	private Map<String, Object> processMrclDxOrDs(int[] numCommonArray, int i, Map<String, Object> returnMap, String typeA, String typeB) {
		int count = 1;// 连续的数，不连续了需要清零
		String preDxStr = "";// 上一个大小格式的数字
		String numdx = "";
		Map<String, Object> map = null;
		for (int j = 0; j < numCommonArray.length; j++) {
			if (typeA.equals(LotteryConstant.DA)) {// 大于5就是大，否则是小
				numdx = numCommonArray[j] > 5 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.SHUANG)) {// 对2%，等于0就是双数，否者就是单数
				numdx = numCommonArray[j] % 2 == 0 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.LONG)) {// 等于1就是龙，否则就是虎
				numdx = numCommonArray[j] == 1 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.GYHDA)) {// 冠亚和的大小就不是大于5了
				numdx = numCommonArray[j] > 11 ? typeA : typeB;
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
					// } else {
					// b += "_" + typeB;
					// }
					// returnMap.put(b, (returnMap.get(b) == null ? 0 :
					// returnMap.get(b)) + 1);
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
	 * @desc 长龙连开提醒：1-10号球大小单双，1-5龙虎，冠亚和的大小单双
	 * @author abo
	 * @date 2018年6月30日 下午1:10:48
	 */
	public void getCllktx(String lottId) {
		if (lottId.equals(LotteryConstant.BJSC)) {
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
			int[] num9Arrays = new int[list.size()];
			int[] num10Arrays = new int[list.size()];
			// 龙虎数据
			int[] longhu1Arrays = new int[list.size()];
			int[] longhu2Arrays = new int[list.size()];
			int[] longhu3Arrays = new int[list.size()];
			int[] longhu4Arrays = new int[list.size()];
			int[] longhu5Arrays = new int[list.size()];
			// 冠亚和
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
				num9Arrays[i] = Integer.parseInt(m.get("num9").toString());
				num10Arrays[i] = Integer.parseInt(m.get("num10").toString());

				longhu1Arrays[i] = Integer.parseInt(m.get("num1").toString()) > Integer.parseInt(m.get("num10").toString()) ? 1 : 0;
				longhu2Arrays[i] = Integer.parseInt(m.get("num2").toString()) > Integer.parseInt(m.get("num9").toString()) ? 1 : 0;
				longhu3Arrays[i] = Integer.parseInt(m.get("num3").toString()) > Integer.parseInt(m.get("num8").toString()) ? 1 : 0;
				longhu4Arrays[i] = Integer.parseInt(m.get("num4").toString()) > Integer.parseInt(m.get("num7").toString()) ? 1 : 0;
				longhu5Arrays[i] = Integer.parseInt(m.get("num5").toString()) > Integer.parseInt(m.get("num6").toString()) ? 1 : 0;
				gyhArray[i] = Integer.parseInt(m.get("num1").toString()) + Integer.parseInt(m.get("num2").toString());

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
			maps.put("num9", num9Arrays);
			maps.put("num10", num10Arrays);
			// 冠亚和加入到mun11
			maps.put("num11", gyhArray);

			maps.put("num1lh", longhu1Arrays);
			maps.put("num2lh", longhu2Arrays);
			maps.put("num3lh", longhu3Arrays);
			maps.put("num4lh", longhu4Arrays);
			maps.put("num5lh", longhu5Arrays);

			// 初始化公共数组
			int[] numCommonArray = new int[list.size()];
			int[] numLhCommonArray = new int[list.size()];

			Map<String, Integer> returnMap = null;
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			// 因为有冠亚和，所以总球数需要加1
			// 当前彩票的球数
			int num = LotteryConstant.lotteryNumMap.get(lottId) + 1;
			// 循环当前彩种球数
			for (int i = 1; i <= num; i++) {
				returnMap = new LinkedHashMap<String, Integer>();
				numCommonArray = maps.get("num" + i);// 获取当前处理字段的数组。
				if (numCommonArray == null) {
					continue;
				}
				// 处理龙虎长龙，龙虎只需要1-5号球
				if (i < 6) {
					numLhCommonArray = maps.get("num" + i + "lh");// 获取当前处理字段的数组。
					if (numLhCommonArray == null) {
						continue;
					}
					returnMap = processCllktx(numLhCommonArray, returnMap, i, LotteryConstant.LONG, LotteryConstant.HU);
				}
				// i等于11的时候是冠亚和，冠亚和的大小和普通大小的阈值不一样，所以分开写
				if (i < 11) {
					// 处理大小长龙
					returnMap = processCllktx(numCommonArray, returnMap, i, LotteryConstant.DA, LotteryConstant.XIAO);
				} else {
					// 处理冠亚和大小
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
				numdx = numCommonArray[j] > 5 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.SHUANG) || typeA.equals(LotteryConstant.GYHSHUANG)) {// 对2%，等于0就是双数，否者就是单数
				numdx = numCommonArray[j] % 2 == 0 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.LONG)) {// 等于1就是龙，否则就是虎
				numdx = numCommonArray[j] == 1 ? typeA : typeB;
			} else if (typeA.equals(LotteryConstant.GYHDA)) {// 冠亚和的大小就不是大于5了
				numdx = numCommonArray[j] > 11 ? typeA : typeB;
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
	 * 
	 * @desc PK10 遗漏统计
	 * @author maosheng
	 * @date 2018年7月9日 下午7:17:49
	 */
	public void getPk10Yilou(String lottId) {
		if (lottId.equals(LotteryConstant.BJSC)) {
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 今天
			String cd = DateUtil.date2String(new Date(), DateUtil.PATTERN_DATE);
			// 本周第一天(星期一算第一天)
			Date wd = DateUtil.getWeekStartDate();
			// 本月第一天（1号）
			Date md = DateUtil.getFirstDayOfMonth();
			String monthfirstDate = DateUtil.date2String(md, DateUtil.PATTERN_DATE);
			// 本月数据
			List<Map<String, Object>> mlist = lotteryProcessMapper.selectLotteryAfterDate(tablename, numStr, monthfirstDate);
			// 定义本周数据
			List<Map<String, Object>> wlist = new ArrayList<Map<String, Object>>();
			// 定义当天数据
			List<Map<String, Object>> clist = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> m : mlist) {
				if (((Date) m.get("starttime")).after(wd)) {
					wlist.add(m);
				}
				if (cd.equals(m.get("time").toString())) {
					clist.add(m);
				}
			}
			// PK10所有位置遗漏数据
			// 位置号码
			JSONObject wzhmobj = PK10YilouHelp.pk10wzhm(clist, wlist, mlist);
			redisservice.set(LotteryConstant.BJSC + "_" + LotteryDict.wzyl + "_" + "wzhm", wzhmobj.toJSONString());
			// 位置大小
			JSONObject wzdxobj = PK10YilouHelp.pk10wzdx(clist, wlist, mlist);
			redisservice.set(LotteryConstant.BJSC + "_" + LotteryDict.wzyl + "_" + "wzdx", wzdxobj.toJSONString());
			// 位置单双
			JSONObject wzdsobj = PK10YilouHelp.pk10wzds(clist, wlist, mlist);
			redisservice.set(LotteryConstant.BJSC + "_" + LotteryDict.wzyl + "_" + "wzds", wzdsobj.toJSONString());
			// 位置冠亚和大小
			JSONObject wzgyhdxobj = PK10YilouHelp.pk10wzgyhdx(clist, wlist, mlist);
			redisservice.set(LotteryConstant.BJSC + "_" + LotteryDict.wzyl + "_" + "wzgyhdx", wzgyhdxobj.toJSONString());
			// 位置冠亚和单双
			JSONObject wzgyhdsobj = PK10YilouHelp.pk10wzgyhds(clist, wlist, mlist);
			redisservice.set(LotteryConstant.BJSC + "_" + LotteryDict.wzyl + "_" + "wzgyhds", wzgyhdsobj.toJSONString());
			// 位置龙虎（pk10计算前五位龙虎）
			JSONObject wzlhobj = PK10YilouHelp.pk10wzlh(clist, wlist, mlist);
			redisservice.set(LotteryConstant.BJSC + "_" + LotteryDict.wzyl + "_" + "wzlh", wzlhobj.toJSONString());

			// PK10所有号码遗漏数据
			JSONObject hmylobj = PK10YilouHelp.pk10hmyl(clist, wlist, mlist);
			redisservice.set(LotteryConstant.BJSC + "_" + LotteryDict.hmyl, hmylobj.toJSONString());
		}

	}

	/**
	 * @desc PK10 冠亚和遗漏统计
	 * @author maosheng
	 * @date 2018年7月9日 下午7:27:54
	 */
	public void getPk10GyhYilou(String lottId) {
		if (lottId.equals(LotteryConstant.BJSC)) {
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 今天
			Date td = new Date();
			String currentDate = DateUtil.date2String(td, DateUtil.PATTERN_DATE);
			// 当天数据
			List<Map<String, Object>> clist = lotteryProcessMapper.selectLotteryAfterDate(tablename, numStr, currentDate);
			// 冠亚和遗漏统计
			JSONObject wzylobj = PK10YilouHelp.pk10gyhdianshu(clist);
			redisservice.set(lottId + "_" + LotteryDict.gyhyl, wzylobj.toJSONString());
		}

	}

	/**
	 * 
	 * @desc 冠亚和历史（北京赛车，幸运农场）
	 * @author abo
	 * @date 2018年7月3日 下午7:02:29
	 */
	public void gyhls(String lottId) {
		/* for (String lottId : LotteryConstant.tableNameMap.keySet()) { */
		// 北京赛车和新运飞艇
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			String currentDate = LotteryUtil.countDayOfNow(lottId);
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			// 查询球列的字符串
			String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 查询数据库数据
			List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByDate(tablename, numStr, currentDate);
			// 冠亚和基本数据
			int gyhDa = 0;// 冠亚和大
			int gyhXiao = 0;// 冠亚和小
			int gyhDan = 0;// 冠亚和单
			int gyhShuang = 0;// 冠亚和双
			// 把每个号段重新封装成一个数组
			List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
			Map<String, Object> map = new HashMap<String, Object>();
			// String date = "";
			for (int i = 0; i < list.size(); i++) {
				Map m = list.get(i);
				int gyh = Integer.parseInt(m.get("num1").toString()) + Integer.parseInt(m.get("num2").toString());
				// 冠亚和大小
				if (gyh > 11) {
					gyhDa++;
				} else {
					gyhXiao++;
				}
				// 冠亚和单双
				if (gyh % 2 == 0) {
					gyhShuang++;
				} else {
					gyhDan++;
				}
				// if (!StringUtils.isNoneBlank(date)) {
				// date = m.get("time") + "";
				// }
			}
			map.put("gyhDa", gyhDa + "");
			map.put("gyhXiao", gyhXiao + "");
			map.put("gyhDan", gyhDan + "");
			map.put("gyhShuang", gyhShuang + "");

			JSONObject json = new JSONObject(map);
			redisservice.setHashMap(lottId + "_" + LotteryDict.gyhls, currentDate, json.toString());
		}
		// }
	}

	/**
	 * 
	 * @desc 龙虎历史（北京赛车，幸运农场）
	 * @author abo
	 * @date 2018年7月3日 下午7:02:29
	 */
	public void lhls(String lottId) {
		/* for (String lottId : LotteryConstant.tableNameMap.keySet()) { */
		// 北京赛车和新运飞艇
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			String currentDate = LotteryUtil.countDayOfNow(lottId);
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			// 查询球列的字符串
			String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 查询数据库数据
			List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByDate(tablename, numStr, currentDate);
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			String date = "";
			// 龙虎数据
			int num1long = 0;
			int num1hu = 0;
			int num2long = 0;
			int num2hu = 0;
			int num3long = 0;
			int num3hu = 0;
			int num4long = 0;
			int num4hu = 0;
			int num5long = 0;
			int num5hu = 0;
			for (int i = 0; i < list.size(); i++) {
				Map m = list.get(i);
				if (Integer.parseInt(m.get("num1").toString()) > Integer.parseInt(m.get("num10").toString())) {
					num1long++;
				} else {
					num1hu++;
				}
				if (Integer.parseInt(m.get("num2").toString()) > Integer.parseInt(m.get("num9").toString())) {
					num2long++;
				} else {
					num2hu++;
				}
				if (Integer.parseInt(m.get("num3").toString()) > Integer.parseInt(m.get("num8").toString())) {
					num3long++;
				} else {
					num3hu++;
				}
				if (Integer.parseInt(m.get("num4").toString()) > Integer.parseInt(m.get("num7").toString())) {
					num4long++;
				} else {
					num4hu++;
				}
				if (Integer.parseInt(m.get("num5").toString()) > Integer.parseInt(m.get("num6").toString())) {
					num5long++;
				} else {
					num5hu++;
				}
				if (!StringUtils.isNoneBlank(date)) {
					date = m.get("time") + "";
				}
			}
			map.put("num1_" + LotteryConstant.LONG, num1long);
			map.put("num1_" + LotteryConstant.HU, num1hu);
			map.put("num2_" + LotteryConstant.LONG, num2long);
			map.put("num2_" + LotteryConstant.HU, num2hu);
			map.put("num3_" + LotteryConstant.LONG, num3long);
			map.put("num3_" + LotteryConstant.HU, num3hu);
			map.put("num4_" + LotteryConstant.LONG, num4long);
			map.put("num4_" + LotteryConstant.HU, num4hu);
			map.put("num5_" + LotteryConstant.LONG, num5long);
			map.put("num5_" + LotteryConstant.HU, num5hu);

			JSONObject json = new JSONObject(map);
			redisservice.setHashMap(lottId + "_" + LotteryDict.lhls, currentDate, json.toString());
		}
		// }
	}

	/**
	 * 
	 * @desc 北京赛车、幸运飞艇号码前后路珠
	 * @author abo
	 * @date 2018年7月6日 下午1:38:27
	 */
	public void getHmqhlz(String lottId) {

		/* for (String lottId : LotteryConstant.tableNameMap.keySet()) { */
		// 北京赛车和新运飞艇
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			// 当前时间
			String currentDate = LotteryUtil.countDayOfNow(lottId);
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			// 查询球列的字符串
			String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 当前彩票的球数
			int num = LotteryConstant.lotteryNumMap.get(lottId);
			// Map<String, String> maps = new HashMap<String, String>();
			List<Map<String, Object>> currentDayList = lotteryProcessMapper.findLotteryByDate(tablename, numstr, currentDate);
			// 10个号码的今日号码累计
			Map<String, String> jrhmljMap = new LinkedHashMap<String, String>();
			Map<String, Object> tempMap = new LinkedHashMap<String, Object>();
			int[] numArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			// 把每次的开奖号封装成一个list对象，便于后续使用indexof返回所在索引
			List<List<Integer>> intArrays = LotteryUtil.mapToList(currentDayList, num);
			for (int i = 0; i < numArray.length; i++) {
				int q = 0;
				int h = 0;
				int n = numArray[i];
				jrhmljMap = new LinkedHashMap<String, String>();
				for (List<Integer> m : intArrays) {
					int index = m.indexOf(n);
					index += 1;// 因为索引是0开始所以必须加1
					if (index > 5) {// 大于5就在后半区
						h++;
					} else {
						q++;
					}
				}
				jrhmljMap.put("num_" + n + "_h", h + "");
				jrhmljMap.put("num_" + n + "_q", q + "");
				tempMap.put("num_" + n, jrhmljMap);
			}
			Map<String, Object> mm = new HashMap<String, Object>();
			mm.put("jrlj", tempMap);
			// 从数据库查询200条数据
			List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numstr, 200);
			// 把每次的开奖号封装成一个list对象，便于后续使用indexof返回所在索引
			intArrays = LotteryUtil.mapToList(list, num);
			String[] array = null;
			String preStr = "";
			int r = 0;// 列数
			Map<String, Object> map = null;
			tempMap = new LinkedHashMap<String, Object>();
			// 封装55列数据
			for (int i = 0; i < numArray.length; i++) {
				int n = numArray[i];
				array = new String[200];
				map = new LinkedHashMap<String, Object>();
				r = 0;
				for (int j = 0; j < intArrays.size(); j++) {
					int index = intArrays.get(j).indexOf(n);
					index += 1;// 因为索引是0开始所以必须加1
					if (index > 5) {// 大于5就在后半区
						array[j] = LotteryConstant.HOU;
					} else {
						array[j] = LotteryConstant.QIAN;
					}
					String strTemp = array[j];
					if (preStr != strTemp) {
						r++;// 如果不相同就换一列
					}
					// 55列的时候就退出
					if (r > LotteryConstant.LZ_COLUMN_NUM) {
						break;
					}
					preStr = strTemp;
					map.put(r + "", map.get(r + "") == null ? "" + strTemp : map.get(r + "") + strTemp);
				}
				tempMap.put("num_" + n, map);
			}
			mm.put("sxplz", tempMap);
			JSONObject json = new JSONObject(mm);
			// redisservice.setHashMap(LotteryDict.hmqhlz, lottId + "_" +
			// LotteryDict.hmqhlz, json.toString());
			redisservice.set(lottId + "_" + LotteryDict.hmqhlz, json.toString());
//			System.out.println(lottId + "_" + LotteryDict.hmqhlz + "=" + mm);
		}
		// }
	}

	/**
	 * @desc 冠亚和路珠 北京赛车 幸运飞艇
	 * @author xg
	 * @author 2018年7月3日
	 */
	public void getGyhlz(String lottId) {
		/* for (String lottId : LotteryConstant.tableNameMap.keySet()) { */
		// 这里就跳过
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			// 大的最小数
			int big = 12;
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			// 查询球列的字符串
			String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numstr, 200);
			if (list == null) {
				return;
			}
			Map<String, ArrayList<String>> bigMap = new HashMap<String, ArrayList<String>>();
			ArrayList<String> dxlist = new ArrayList<String>();
			ArrayList<String> dslist = new ArrayList<String>();
			dxlist.add("");
			dslist.add("");
			Map<String, Integer> aMap = new HashMap<String, Integer>();
			for (Map map : list) {
				// 统计大小
				// 变量0 大 1 小 2默认 用来记录上次大小
				Integer a = aMap.get("num1");
				// 变量0 单 1 双 2默认 用来记录上次单双
				Integer b = aMap.get("num2");
				if (a == null) {
					// a定义默认变量
					a = 2;
				}
				if (b == null) {
					// b定义默认变量
					b = 2;
				}
				Integer haoma = (Integer) map.get("num1") + (Integer) map.get("num2");
				// 统计大小
				if (aMap.get("continue1") == null) {
					if (haoma >= big) {
						if (a == 0 || a == 2) {
							String string = dxlist.get(dxlist.size() - 1);
							dxlist.set(dxlist.size() - 1, string + "大");
						} else {
							if (dxlist.size() >= 55) {
								// 列已经满标记
								aMap.put("continue1", 1);
								continue;
							}
							dxlist.add("大");
						}
						LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "da", lottId);
						aMap.put("num1", 0);
					} else {
						if (a == 1 || a == 2) {
							String string = dxlist.get(dxlist.size() - 1);
							dxlist.set(dxlist.size() - 1, string + "小");
						} else {
							if (dxlist.size() >= 55) {
								// 列已经满标记
								aMap.put("continue1", 1);
								continue;
							}
							dxlist.add("小");
						}
						LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "xiao", lottId);
						aMap.put("num1", 1);
					}
				} else if (aMap.get("continue1") == null && aMap.get("continue2") == null) {
					// 如果大于55列就停下来
					continue;
				}
				// 统计单双
				if (aMap.get("continue2") == null) {
					if (haoma % 2 == 1) {
						if (b == 0 || b == 2) {
							String string = dslist.get(dslist.size() - 1);
							dslist.set(dslist.size() - 1, string + "单");
						} else {
							if (dslist.size() >= 55) {
								// 列已经满标记
								aMap.put("continue2", 1);
								continue;
							}
							dslist.add("单");
						}
						LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "dan", lottId);
						aMap.put("num2", 0);
					} else {
						if (b == 1 || b == 2) {
							String string = dslist.get(dslist.size() - 1);
							dslist.set(dslist.size() - 1, string + "双");
						} else {
							if (dslist.size() >= 55) {
								// 列已经满标记
								aMap.put("continue2", 1);
								continue;
							}
							dslist.add("双");
						}
						LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "shuang", lottId);
						aMap.put("num2", 1);
					}
				} else if (aMap.get("continue1") == null && aMap.get("continue2") == null) {
					// 如果大于55列就停下来
					continue;
				}
			}
			bigMap.put("dx", dxlist);
			bigMap.put("ds", dslist);
			// 反转list顺序
			for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
				if (entry.getValue() instanceof List) {
					Collections.reverse(entry.getValue());
				}
			}
			// System.out.println(lottId + ":" + bigMap);
			JSONObject json = new JSONObject(new HashMap<String, Object>(bigMap));
			redisservice.set(lottId + "_" + LotteryDict.gyhlz, json.toString());
		}
		// }
	}

	/**
	 * @desc 两面投注参考 北京赛车pk10
	 * @author xg
	 * @author 2018年7月11日
	 */
	public void getLmtzck(String lottId, String date) {
		if (!lottId.equals(LotteryConstant.BJSC)) {
			return;
		}
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);

		String dateStr = null;
		if (StringUtils.isNotBlank(date)) {
			dateStr = LotteryUtil.countDayOfNow(lottId, date);
		} else {
			dateStr = LotteryUtil.countDayOfNow(lottId);
		}
		Map<String, Object> map = lotteryProcessMapper.findLotteryMinAndMaxPeriodsByToday(tablename, dateStr);
		if (map == null) {
			return;
		}
		Long min = Long.valueOf(map.get("min").toString());
		Long max = Long.valueOf(map.get("max").toString());
		// 今天最后1期期数从开始到后到倒数第1位的截取
		String strMaxPre = max.toString().substring(0, max.toString().length() - 1);
		// 今天第1期期数最后1位
		String strMinSuf1 = min.toString().substring(min.toString().length() - 1, min.toString().length());
		// 今天最后1期数最后1位
		String strMaxSuf1 = max.toString().substring(max.toString().length() - 1, max.toString().length());
		Long minSuf1 = Long.valueOf(strMinSuf1);
		Long maxSuf1 = Long.valueOf(strMaxSuf1);
		String beginPeriods = null;
		String endPeriods = String.valueOf(max);
		if (maxSuf1 > minSuf1) {
			beginPeriods = strMaxPre + strMinSuf1;
		} else if (maxSuf1 <= minSuf1) {
			beginPeriods = String.valueOf(Long.valueOf(strMaxPre + strMinSuf1) - 10);
			if (maxSuf1 == minSuf1) {
				beginPeriods = endPeriods;
			}
		}
		if (endPeriods != null && beginPeriods != null) {
			List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByPeriods(tablename, numStr, beginPeriods, endPeriods, LotteryUtil.countDayOfNow(lottId));
			if (list == null) {
				return;
			}
			String sKey = "";
			Map<String, Map<String, Object>> bigMap = new HashMap<String, Map<String, Object>>();
			for (Map<String, Object> mapData : list) {
				// 统计大小单双
				for (int i = 1; i <= num; i++) {
					Map<String, Object> smap = bigMap.get("num" + i);
					if (smap == null) {
						smap = new HashMap<String, Object>();
						smap.put("dan", 0);
						smap.put("shuang", 0);
						smap.put("da", 0);
						smap.put("xiao", 0);
					}
					Integer haoma = (Integer) mapData.get("num" + i);
					if (haoma >= LotteryConstant.lotteryBigBorderMap.get(lottId)) {
						smap.put("da", Integer.parseInt(smap.get("da").toString()) + 1);
					} else {
						smap.put("xiao", Integer.parseInt(smap.get("xiao").toString()) + 1);
					}
					if (haoma % 2 == 1) {
						smap.put("dan", Integer.parseInt(smap.get("dan").toString()) + 1);
					}
					if (haoma % 2 == 0) {
						smap.put("shuang", Integer.parseInt(smap.get("shuang").toString()) + 1);
					}
					smap.put("time", mapData.get("time"));
					smap.put("beginPeriods", beginPeriods);
					smap.put("endPeriods", max);
					if (!StringUtils.isNoneBlank(sKey)) {
						sKey = mapData.get("time") + " " + beginPeriods;
					}
					bigMap.put("num" + i, smap);
				}
			}
			JSONObject resobj = new JSONObject(new HashMap<String, Object>(bigMap));
			// redisservice.set(lottId + "_" + LotteryDict.lmtzck,
			// resobj.toJSONString());
			redisservice.setHashMap(lottId + "_" + LotteryDict.lmtzck, sKey, resobj.toJSONString());
		}
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
				}
				// 统计默认为0
				LotteryUtil.initNum("num" + i, bigMap, "he");
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

	/**
	 * @desc 彩票直播开奖
	 * @author xg
	 * @param lottId
	 * @author 2018年7月12日
	 */
	public void lotteryVideo() {
 		String lottId = LotteryConstant.BJSC;
		ArrayList<Object> arrayList = new ArrayList<Object>();
		HashMap<String, Object> lotteryMap = new HashMap<String, Object>();
		// betData数据封装 
		HashMap<String, Object> betDataMap = new HashMap<String, Object>();
		ArrayList<Object> betItemsList = new ArrayList<Object>();
		HashMap<String, Object> betItemshMapA = new HashMap<String, Object>();
		HashMap<String, Object> betItemshMapB = new HashMap<String, Object>();
		HashMap<String, Object> betItemshMapC = new HashMap<String, Object>();
		betItemsList.add(betItemshMapA);
		betItemsList.add(betItemshMapB);
		betItemsList.add(betItemshMapC);
		
		Map<Object, Object> hashMaps = redisservice.getHashMaps(lottId+"_"+LotteryDict.hmtjjh);
		Map changeData = BjscUtils.changeData(hashMaps);
		List changeDatalist = (List) changeData.get("list");
		Integer a0 = (Integer) changeData.get("a0");
		Integer a1 = (Integer) changeData.get("a1");
		Integer a2 = (Integer) changeData.get("a2");
		List list0 = (List) JSONUtils.parse(((Entry) changeDatalist.get(0)).getValue().toString());
		
		Map map0 = (Map) list0.get(0);
		betItemshMapA.put("periodNumber", String.valueOf(map0.get("qishu")));
		betItemshMapA.put("betName", "冠军");
		List listA = (List) JSONUtils.parse(hashMaps.get(map0.get("qishu").toString()).toString());
		String strA = map0.get("tjnum").toString();
		betItemshMapA.put("betItem", strA.replaceAll("[\\[\\]]", ""));
		betItemshMapA.put("conWinOrLoss", String.valueOf(a0));
		
		Map map1 = (Map) list0.get(1);
		betItemshMapB.put("periodNumber", String.valueOf(map0.get("qishu")));
		betItemshMapB.put("betName", "亚军");
		String strB = map1.get("tjnum").toString();
		betItemshMapB.put("betItem", strB.replaceAll("[\\[\\]]", ""));
		betItemshMapB.put("conWinOrLoss", String.valueOf(a1));
		
		Map map2 = (Map) list0.get(2);
		betItemshMapC.put("periodNumber", String.valueOf(map0.get("qishu")));
		betItemshMapC.put("betName", "季军");
		String strC = map2.get("tjnum").toString();
		betItemshMapC.put("betItem", strC.replaceAll("[\\[\\]]", ""));
		betItemshMapC.put("conWinOrLoss", String.valueOf(a2));
		
		betDataMap.put("betItems", betItemsList);
		betDataMap.put("dataUrl", "/pk10/shipinmode");
		lotteryMap.put("betData", betDataMap);
		
		// ballStat数据封装
		ArrayList<Object> ballStat = new ArrayList<Object>();
		String currentDate = LotteryUtil.countDayOfNow(lottId);
		Object dsdxlsJson = redisservice.getHashMap(lottId + "_" + LotteryDict.dsdxls, currentDate);
		if (dsdxlsJson == null) {
			return;
		}
		Map dsdxlsMap = (Map) JSONObject.parse((String) dsdxlsJson);
		for (int i = 1; i <= 10; i++) {
			Map dsdxlsItemMap = (Map) dsdxlsMap.get("num" + i);
			HashMap<String, Object> ballStatItemMap = new HashMap<String, Object>();
			String name = "";
			if (i == 1) {
				name = "冠军";
			} else if (i == 2) {
				name = "亚军";
			} else if (i == 3) {
				name = "季军";
			} else if (i == 4) {
				name = "第四名";
			} else if (i == 5) {
				name = "第五名";
			} else if (i == 6) {
				name = "第六名";
			} else if (i == 7) {
				name = "第七名";
			} else if (i == 8) {
				name = "第八名";
			} else if (i == 9) {
				name = "第九名";
			} else if (i == 10) {
				name = "第十名";
			}
			if (dsdxlsItemMap == null) {
				dsdxlsItemMap = new HashMap<>();
			}
			ballStatItemMap.put("name", name);
			ballStatItemMap.put("big", dsdxlsItemMap.get("da"));
			ballStatItemMap.put("small", dsdxlsItemMap.get("xiao"));
			ballStatItemMap.put("odd", dsdxlsItemMap.get("dan"));
			ballStatItemMap.put("even", dsdxlsItemMap.get("shuang"));
			ballStat.add(ballStatItemMap);
		}
		lotteryMap.put("ballStat", ballStat);
		// numOmit数据封装
		ArrayList<Object> numOmit = new ArrayList<Object>();
		String wzhmJson = redisservice.get(LotteryConstant.BJSC + "_" + LotteryDict.wzyl + "_" + "wzhm");
		Map wzhmMap = (Map) JSONObject.parse(wzhmJson);
		for (int i = 1; i <= 10; i++) {
			Map wzhmItemMap = (Map) wzhmMap.get("num" + i);
			HashMap<String, Object> numOmitItemMap = new HashMap<String, Object>();
			String name = "";
			if (i == 1) {
				name = "冠军";
			} else if (i == 2) {
				name = "亚军";
			} else if (i == 3) {
				name = "季军";
			} else if (i == 4) {
				name = "第四名";
			} else if (i == 5) {
				name = "第五名";
			} else if (i == 6) {
				name = "第六名";
			} else if (i == 7) {
				name = "第七名";
			} else if (i == 8) {
				name = "第八名";
			} else if (i == 9) {
				name = "第九名";
			} else if (i == 10) {
				name = "第十名";
			}
			numOmitItemMap.put("name", name);
			ArrayList<Object> dataArray = new ArrayList<Object>();
			numOmitItemMap.put("data", dataArray);
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			for (int j = 1; j <= 10; j++) {
				Map numMap = (Map) wzhmItemMap.get(String.valueOf(j));
				HashMap<String, Object> dataItemMap = new HashMap<String, Object>();
				dataItemMap.put("num", String.valueOf(j));
				dataItemMap.put("coming", numMap.get("jrcx"));
				dataItemMap.put("uncoming", numMap.get("jryl"));
				dataArray.add(dataItemMap);
			}
			numOmit.add(numOmitItemMap);
		}
		lotteryMap.put("numOmit", numOmit);
		// drawHistories数据封装
		ArrayList<Object> drawHistories = new ArrayList<Object>();
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 40);
		for (Map<String, Object> map : list) {
			HashMap<String, Object> drawHistoriesMap = new HashMap<String, Object>();
			String nums = "";
			for (int i = 1; i <= 10; i++) {
				nums += map.get("num" + i) + ",";
			}
			String periods = map.get("periods").toString();
			drawHistoriesMap.put("periodNumber", periods.substring(periods.length() - 2, periods.length()));
			drawHistoriesMap.put("numbers", nums.substring(0, nums.length() - 1));
			drawHistories.add(drawHistoriesMap);
		}
		lotteryMap.put("drawHistories", drawHistories);
		arrayList.add(lotteryMap);
		redisservice.setHashMap(LotteryDict.video, "bjsc_MobileA", new JSONArray(arrayList).toJSONString());
	}


	
	

}