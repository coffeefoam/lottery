package com.boying.cpapi.service.zsdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;
import com.boying.cpapi.util.Yilou.YilouUtil;

import io.lottery.common.config.LotteryDict;

/**
 * 
 * @desc 北京赛车和幸运飞艇service
 * @author abo
 * @date 2018年6月27日 下午2:58:59
 *
 */
@Service
public class LotteryBjscftZstService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * 
	 * @desc
	 * @author abo
	 * @date 2018年8月2日 下午3:53:55
	 * @param lottId
	 */
	public void execute(String lottId) {
		getWzzst(lottId);// 位置走势图
		getHmzst(lottId);// 号码走势图
		getGyhzst(lottId);// 冠亚和走势图
		
//		getWzzst(LotteryConstant.BJSC);// 位置走势图
//		getHmzst(LotteryConstant.BJSC);// 号码走势图
//		getGyhzst(LotteryConstant.BJSC);// 冠亚和走势图
//		// 幸运飞艇
//		getWzzst(LotteryConstant.XYFT);
//		getHmzst(LotteryConstant.XYFT);
//		getGyhzst(LotteryConstant.XYFT);
	}

	/**
	 * 
	 * @desc 冠亚和走势图
	 * @author abo
	 * @date 2018年8月4日 上午9:48:05
	 * @param lottId
	 */
	private void getGyhzst(String lottId) {
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 十天的数据
		List<Map<String, Object>> list10Day = lotteryProcessMapper.selectBjTrend(tablename, numstr, "10dayall",LotteryUtil.countDayOfNow(lottId));
		List<Map<String, Object>> list40 = new ArrayList<Map<String, Object>>();// 40期数据
		List<Map<String, Object>> list41 = new ArrayList<Map<String, Object>>();// 41期数据
		List<Map<String, Object>> list90 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list91 = new ArrayList<Map<String, Object>>();
		// 封装完的十天的数据
		List<Map<String, Object>> list10DayOk = new ArrayList<Map<String, Object>>();

		Map<String, Object> m = null;
		// 今日所有期数
		List<Map<String, Object>> currentTemp = lotteryProcessMapper.selectBjTrend(tablename, numstr, "all",LotteryUtil.countDayOfNow(lottId));
		List<Map<String, Object>> currentList = new ArrayList<>();// 今日期数
		List<Map<String, Object>> currentList1 = new ArrayList<>();// 今日期数+1
		Map<String, Object> map = null;
		for (int i = 0; i < list10Day.size(); i++) {
			map = new HashMap<String, Object>();
			int num1 = (Integer) list10Day.get(i).get("num1") + (Integer) list10Day.get(i).get("num2");
			String periods = list10Day.get(i).get("periods") + "";
			map.put("num1", num1);
			map.put("periods", periods);
			if (i < 40) {
				list40.add(map);
			}
			if (i < 41) {
				list41.add(map);
			}
			if (i < 90) {
				list90.add(map);
			}
			if (i < 91) {
				list91.add(map);
			}
			if (i < currentTemp.size()) {
				currentList.add(map);
			}
			if (i < currentTemp.size() + 1) {
				currentList1.add(map);
			}
			list10DayOk.add(map);
		}
		// if (list40 != null) {
		// m = list40.get(0);
		// }
		// 取十天的数据最后一条，取当天的可能没数据
		if (list10Day != null && list10Day.size() > 0) {
			m = list10Day.get(0);
		}
		String[] numberArray = new String[] { "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19" };
		JSONObject obj90 = null;
		// 总计汇总
		JSONObject obj2 = new JSONObject();
		// 40期的对象
		JSONObject obj40 = makeGyhZstSum(list40, list41, list10DayOk, numberArray);
		obj2.put("40", obj40);
		// 只有北京赛车才有
		if (!lottId.equals(LotteryConstant.XYFT)) {
			// 90期的对象
			obj90 = makeGyhZstSum(list90, list91, list10DayOk, numberArray);
			obj2.put("90", obj90);
		}
		// 今日全部期数对象
		JSONObject objall = makeGyhZstSum(currentList, currentList1, list10DayOk, numberArray);
		obj2.put("all", objall);
		redisservice.set(lottId + "_" + LotteryDict.gyhzszj, obj2.toJSONString());
		// 每期
		JSONObject map40 = makeMqzst(list40, list41, list10DayOk, numberArray);
		// System.out.println(map40);
		redisservice.setHashMap(lottId + "_" + LotteryDict.gyhzsmq, m.get("periods").toString(), map40.toJSONString());

	}

	/**
	 * 
	 * @desc 制作每期的走势图行数据，暂时不返回今日全部的数据
	 * @author abo
	 * @date 2018年8月2日 下午1:58:51
	 * @param list41
	 *            41期
	 * @param listAll
	 *            今日全部
	 * @param list4Day
	 *            10天期数
	 * @param numberArray
	 * @param position
	 * @param numberArray
	 * @return
	 */
	private JSONObject makeMqzst(List<Map<String, Object>> list40, List<Map<String, Object>> list41, List<Map<String, Object>> list10DayOk, String[] numberArray) {
		JSONObject ojb0 = new JSONObject();
		Map<String, Object> obj2 = null;// 第二层
		// JSONArray ja = new JSONArray();
		int jrylmax = 0;
		obj2 = new LinkedHashMap<String, Object>();
		for (String number : numberArray) {
			// 遺漏次數
			jrylmax = YilouUtil.gyhMqZdylByBjsc(list10DayOk, number);
			obj2.put(number, jrylmax);
		}
		YilouUtil.gyhMqZcfByBjsc(list41, true);
		int fxcs = YilouUtil.gyhMqZcfByBjsc(list41, true);
		// 正向次数
		int zxcs = YilouUtil.gyhMqZcfByBjsc(list41, false);
		int cfcs = YilouUtil.gyhMqcfcsByBjsc(list41);
		obj2.put("a1fan", fxcs == 0 ? LotteryConstant.FAN : fxcs);// 反向次数
		obj2.put("a2chong", cfcs == 0 ? LotteryConstant.CHONG : cfcs);// 重号
		obj2.put("a3zheng", zxcs == 0 ? LotteryConstant.ZHENG : zxcs);// 正向次数
		// 单双遗漏
		int dan = YilouUtil.gyhMqDsByBjsc(list41, true);
		int shuang = YilouUtil.gyhMqDsByBjsc(list41, false);
		obj2.put("a4dan", dan == 0 ? LotteryConstant.DAN : dan);
		obj2.put("a5shuang", shuang == 0 ? LotteryConstant.SHUANG : shuang);
		// // 大小遗漏
		int da = YilouUtil.gyhMqDxByBjsc(list41, false);
		int xiao = YilouUtil.gyhMqDxByBjsc(list41, true);
		obj2.put("a6da", da == 0 ? LotteryConstant.DA : da);
		obj2.put("a7xiao", xiao == 0 ? LotteryConstant.XIAO : xiao);
		// 前
		int qian = YilouUtil.gyhMqZdylQzhByBjsc(list10DayOk, 1);
		// 中
		int zhong = YilouUtil.gyhMqZdylQzhByBjsc(list10DayOk, 0);
		// 后
		int hou = YilouUtil.gyhMqZdylQzhByBjsc(list10DayOk, -1);

		obj2.put("a8qian", qian == 0 ? LotteryConstant.QIAN : qian);
		obj2.put("a9zhong", zhong == 0 ? LotteryConstant.ZHONG : zhong);
		obj2.put("a10hou", hou == 0 ? LotteryConstant.HOU : hou);
		ojb0.put("40", obj2);
		return ojb0;
	}

	/**
	 * 
	 * @desc 北京赛车冠亚和总计
	 * @author abo
	 * @date 2018年8月4日 下午3:20:09
	 * @param list
	 * @param list1
	 * @param list10Day
	 * @param numberArray
	 * @return
	 */
	private JSONObject makeGyhZstSum(List<Map<String, Object>> list, List<Map<String, Object>> list1, List<Map<String, Object>> list10Day, String[] numberArray) {
		Map<String, Object> ojb = null;
		JSONObject bigObj = null;
		bigObj = new JSONObject(true);
		if (null != list) {
			ojb = new LinkedHashMap<String, Object>();// 总次数
			// ojb1 = new JSONObject(true);
			// 单双次数
			int danZcs = YilouUtil.gyhZstDsZcsByBjsc(list, true);
			int shuangZcs = YilouUtil.gyhZstDsZcsByBjsc(list, false);
			// 大小
			int dzcs = YilouUtil.gyhZstDxZcsByBjsc(list, false);
			int xzcs = YilouUtil.gyhZstDxZcsByBjsc(list, true);
			// 反向次数
			int fxcs = YilouUtil.gyhZfZstZcsByBjsc(list1, false);
			// 正向次数
			int zxcs = YilouUtil.gyhZfZstZcsByBjsc(list1, true);
			// 重号次数
			int cfcs = YilouUtil.gyhCfZstZcsByBjsc(list1);
			Map<String, Integer> map = YilouUtil.gyhQzhZstZcsByBjsc(list);
			for (String number : numberArray) {
				ojb.put(number, 0);
			}
			// 某個號碼在 某個位置出現的 次數
			ojb = YilouUtil.gyhZcsZstByBjsc(list, ojb);
			ojb.put("a1fan", fxcs);// 反向次数
			ojb.put("a2chong", cfcs);// 重号
			ojb.put("a3zheng", zxcs);// 正向次数
			ojb.put("a4dan", danZcs);
			ojb.put("a5shuang", shuangZcs);
			ojb.put("a6da", dzcs);
			ojb.put("a7xiao", xzcs);
			ojb.put("a8qian", map.get("qian"));
			ojb.put("a9zhong", map.get("zhong"));
			ojb.put("a10hou", map.get("hou"));
			bigObj.put("zcs", ojb);
			ojb = new LinkedHashMap<String, Object>();// 总次数
			for (String number : numberArray) {
				// 某個號碼在 某個位置出現的 次數
				int num = YilouUtil.gyzZdylByBjsc(list10Day, number, list.size());
				ojb.put(number, num);
			}
			// 反向次数
			fxcs = YilouUtil.gyhZdylZfcsByBjsc(list10Day, true, list.size());
			ojb.put("a1fan", fxcs);// 反向次数
			cfcs = YilouUtil.gyhZdyCfcsByBjsc(list10Day, list.size());
			ojb.put("a2chong", cfcs);// 重号
			// 正向次数
			zxcs = YilouUtil.gyhZdylZfcsByBjsc(list10Day, false, list.size());
			ojb.put("a3zheng", zxcs);// 正向次数
			// 单双
			danZcs = YilouUtil.gyhZdyDscsByBjsc(list10Day, true, list.size());
			ojb.put("a4dan", danZcs);
			shuangZcs = YilouUtil.gyhZdyDscsByBjsc(list10Day, false, list.size());
			ojb.put("a5shuang", shuangZcs);
			// 大小
			dzcs = YilouUtil.gyhZdyDxcsByBjsc(list10Day, false, list.size());
			xzcs = YilouUtil.gyhZdyDxcsByBjsc(list10Day, true, list.size());
			ojb.put("a6da", dzcs);
			ojb.put("a7xiao", xzcs);
			// 前
			int qian = YilouUtil.gyhZdylQzhcsByBjsc(list10Day, 1, list.size());
			// 后
			int zhong = YilouUtil.gyhZdylQzhcsByBjsc(list10Day, 0, list.size());
			// 中
			int hou = YilouUtil.gyhZdylQzhcsByBjsc(list10Day, -1, list.size());
			ojb.put("a8qian", qian);
			ojb.put("a9zhong", zhong);
			ojb.put("a10hou", hou);
			bigObj.put("zdyl", ojb);
		}
		return bigObj;
	}

	/**
	 * 
	 * @desc 北京赛车And幸运飞艇号码走势
	 * @author abo
	 * @date 2018年8月3日 上午9:25:16
	 * @param bjsc
	 */
	private void getHmzst(String lottId) {
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 十天的数据
		List<Map<String, Object>> list10Day = lotteryProcessMapper.selectBjTrend(tablename, numstr, "10dayall",LotteryUtil.countDayOfNow(lottId));
		List<Map<String, Object>> list40 = new ArrayList<>();// 40期数据
		List<Map<String, Object>> list41 = new ArrayList<>();// 41期数据
		List<Map<String, Object>> list90 = new ArrayList<>();
		List<Map<String, Object>> list91 = new ArrayList<>();
		Map<String, Object> m = null;
		// 选择40期数
		List<Map<String, Object>> currentList = lotteryProcessMapper.selectBjTrend(tablename, numstr, "all",LotteryUtil.countDayOfNow(lottId));
		List<Map<String, Object>> currentList1 = new ArrayList<>();// 今日期数+1
		for (int i = 0; i < 91; i++) {
			if (i < 40) {
				list40.add(list10Day.get(i));
			}
			if (i < 41) {
				list41.add(list10Day.get(i));
			}
			if (i < 90) {
				list90.add(list10Day.get(i));
			}
			if (i < currentList.size() + 1) {
				currentList1.add(list10Day.get(i));
			}
			list91.add(list10Day.get(i));
		}
		// if (list40 != null) {
		// m = list40.get(0);
		// }
		// 取十天的数据最后一条，取当天的可能没数据
		if (list10Day != null && list10Day.size() > 0) {
			m = list10Day.get(0);
		}
		String[] positionArray = numstr.split(",");
		String[] numberArray = LotteryUtil.numArrayByLottId(lottId);
		Map<String, Object> zjMap = new TreeMap<String, Object>();
		JSONObject obj2 = null;
		JSONObject obj40 = null;
		JSONObject obj90 = null;
		JSONObject objall = null;
		// 总计汇总
		// 根据位置进行循环
		for (String number : numberArray) {
			obj2 = new JSONObject();
			// 40期的对象
			obj40 = makeWzzstSum(list40, list41, list10Day, positionArray, number);
			obj2.put("40", obj40);
			// 只有北京赛车才有
			if (!lottId.equals(LotteryConstant.XYFT)) {
				// 90期的对象
				obj90 = makeWzzstSum(list90, list91, list10Day, positionArray, number);
				obj2.put("90", obj90);
			}
			// 今日全部期数对象
			objall = makeWzzstSum(currentList, currentList1, list10Day, positionArray, number);
			obj2.put("all", objall);
			zjMap.put(number, obj2);
		}
		JSONObject zjObj = new JSONObject(zjMap);
		redisservice.set(lottId + "_" + LotteryDict.hmzszj, zjObj.toJSONString());
		// 每期
		JSONObject mqObj2 = null;
		Map<String, Object> mqMap = new TreeMap<String, Object>();
		Map<String, Object> map40 = null;
		for (String number : numberArray) {
			mqObj2 = new JSONObject();
			map40 = makeMq(list40, list41, list10Day, number, positionArray);
			mqObj2.put("40", map40);
			mqMap.put(number, mqObj2);
		}
		JSONObject mqObj = new JSONObject(mqMap);
		redisservice.setHashMap(lottId + "_" + LotteryDict.hmzsmq, m.get("periods").toString(), mqObj.toJSONString());
	}

	/**
	 * 
	 * @desc 每期遗漏
	 * @author abo
	 * @date 2018年8月2日 下午5:41:53
	 * @param list
	 * @param listAdd1
	 * @param list10Day
	 * @param position
	 * @param numberArray
	 * @return
	 */
	public static Map<String, Object> makeMq(List<Map<String, Object>> list, List<Map<String, Object>> listAdd1, List<Map<String, Object>> list10Day, String number, String[] positionArray) {
		Map<String, Object> ojb2 = null;// 第二层
		int s = positionArray.length / 2;
		if (listAdd1 != null) {
			ojb2 = new TreeMap<String, Object>();
			for (String position : positionArray) {
				// 遺漏次數
				int jrylmax = YilouUtil.hmzdyl2ByBjsc(list10Day, number, position, positionArray, list.size());
				// ojb2.put(position, jrylmax);
				ojb2.put(position.replace("num", ""), jrylmax);
			}
			// // 前后遗漏
			int qyl = YilouUtil.dscxcs1ByBjsc(listAdd1, true, number, s, positionArray);
			int hyl = YilouUtil.dscxcs1ByBjsc(listAdd1, false, number, s, positionArray);
			ojb2.put("a4qian", qyl == 0 ? LotteryConstant.QIAN : qyl);
			ojb2.put("a5hou", hyl == 0 ? LotteryConstant.HOU : hyl);
			// 反向次数
			int fxcs = YilouUtil.zfcs1ByBjsc(listAdd1, false, number, positionArray);
			// // 正向次数
			int zxcs = YilouUtil.zfcs1ByBjsc(listAdd1, true, number, positionArray);
			int cfcs = YilouUtil.cfcs1ByBjsc(listAdd1, number, positionArray);
			ojb2.put("a1fan", fxcs == 0 ? LotteryConstant.FAN : fxcs);// 反向次数
			ojb2.put("a2chong", cfcs == 0 ? LotteryConstant.CHONG : cfcs);// 重号
			ojb2.put("a3zheng", zxcs == 0 ? LotteryConstant.ZHENG : zxcs);// 正向次数
		}
		return ojb2;
	}

	/**
	 * 
	 * @desc
	 * @author abo
	 * @date 2018年8月2日 下午4:34:24
	 * @param list
	 * @param listAdd1
	 * @param list10Day
	 * @param positionArray
	 * @param numberArray
	 * @return
	 */
	public static JSONObject makeWzzstSum(List<Map<String, Object>> list, List<Map<String, Object>> listAdd1, List<Map<String, Object>> list10Day, String[] positionArray, String number) {
		JSONObject ojb = new JSONObject();
		int s = positionArray.length / 2;
		if (null != list) {
			Map<String, Object> zcs = new TreeMap<String, Object>();// 总次数
			int jrqcs = YilouUtil.dxcxcsByBjsc(list, true, number, positionArray, s);
			int jrhcs = YilouUtil.dxcxcsByBjsc(list, false, number, positionArray, s);
			// 反向次数
			int fxcs = YilouUtil.zfcsByBjsc(listAdd1, true, number, positionArray);
			// 正向次数
			int zxcs = YilouUtil.zfcsByBjsc(listAdd1, false, number, positionArray);
			int cfcs = YilouUtil.cfcsByBjsc(listAdd1, number, positionArray);
			for (String position : positionArray) {
				// 某個號碼在 某個位置出現的 次數
				int jrcxmax = YilouUtil.hmcxcs(list, number, position);
				zcs.put(position.replace("num", ""), jrcxmax);
			}
			zcs.put("a1fan", fxcs);// 反向次数
			zcs.put("a3zheng", zxcs);// 正向次数
			zcs.put("a2chong", cfcs);// 重号
			zcs.put("a4qian", jrqcs);
			zcs.put("a5hou", jrhcs);
			Map<String, Object> zdyl = new TreeMap<String, Object>();// 最大遗漏
			for (String position : positionArray) {
				// 遺漏次數
				int jrylmax = YilouUtil.hmzdyl1(list10Day, number, position, list.size());
				// zdyl.put(position, jrylmax);
				zdyl.put(position.replace("num", ""), jrylmax);
			}
			jrqcs = YilouUtil.qhZdylSumByBjsc(list10Day, true, number, positionArray, s, list.size());
			zdyl.put("a4qian", jrqcs);
			jrhcs = YilouUtil.qhZdylSumByBjsc(list10Day, false, number, positionArray, s, list.size());
			zdyl.put("a5hou", jrhcs);
			// 反向次数
			fxcs = YilouUtil.zfcsSumByBjsc(list10Day, true, number, positionArray, list.size());
			zdyl.put("a1fan", fxcs);// 反向次数
			// 正向次数
			zxcs = YilouUtil.zfcsSumByBjsc(list10Day, false, number, positionArray, list.size());
			zdyl.put("a3zheng", zxcs);// 正向次数
			cfcs = YilouUtil.zhylSumByBjsc(list10Day, number, positionArray, list.size());
			zdyl.put("a2chong", cfcs);// 重号

			ojb.put("zcs", zcs);
			ojb.put("zdyl", zdyl);
		}
		return ojb;
	}

	/**
	 * 
	 * @desc 北京赛车And幸运飞艇位置走势
	 * @author abo
	 * @date 2018年8月2日 下午3:53:39
	 * @param lottId
	 */
	public void getWzzst(String lottId) {
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 十天的数据
		List<Map<String, Object>> list10Day = lotteryProcessMapper.selectBjTrend(tablename, numstr, "10dayall",LotteryUtil.countDayOfNow(lottId));
		List<Map<String, Object>> list40 = new ArrayList<>();// 40期数据
		List<Map<String, Object>> list41 = new ArrayList<>();// 41期数据
		List<Map<String, Object>> list90 = new ArrayList<>();
		List<Map<String, Object>> list91 = new ArrayList<>();
		Map<String, Object> m = null;
		// 选择40期数
		List<Map<String, Object>> currentList = lotteryProcessMapper.selectBjTrend(tablename, numstr, "all",LotteryUtil.countDayOfNow(lottId));
		List<Map<String, Object>> currentList1 = new ArrayList<>();// 今日期数+1
		for (int i = 0; i < 91; i++) {
			if (i < 40) {
				list40.add(list10Day.get(i));
			}
			if (i < 41) {
				list41.add(list10Day.get(i));
			}
			if (i < 90) {
				list90.add(list10Day.get(i));
			}
			if (i < currentList.size() + 1) {
				currentList1.add(list10Day.get(i));
			}
			list91.add(list10Day.get(i));
		}
		// if (list40 != null) {
		// m = list40.get(0);
		// }
		// 取十天的数据最后一条，取当天的可能没数据
		if (list10Day != null && list10Day.size() > 0) {
			m = list10Day.get(0);
		}
		String[] positionArray = numstr.split(",");
		String[] numberArray = LotteryUtil.numArrayByLottId(lottId);
		Map<String, Object> zjMap = new TreeMap<String, Object>();
		JSONObject obj2 = null;
		JSONObject obj40 = null;
		JSONObject obj90 = null;
		JSONObject objall = null;
		// 总计汇总
		// 根据位置进行循环
		for (String position : positionArray) {
			obj2 = new JSONObject();
			// 40期的对象
			obj40 = ZstUtil.makeWzzstSum(list40, list41, list10Day, position, numberArray);
			obj2.put("40", obj40);
			if (!lottId.equals(LotteryConstant.XYFT)) {
				// 90期的对象
				obj90 = ZstUtil.makeWzzstSum(list90, list91, list10Day, position, numberArray);
				obj2.put("90", obj90);
			}
			// 今日全部期数对象
			objall = ZstUtil.makeWzzstSum(currentList, currentList1, list10Day, position, numberArray);
			obj2.put("all", objall);
			zjMap.put(position, obj2);
		}
		JSONObject zjObj = new JSONObject(zjMap);
		redisservice.set(lottId + "_" + LotteryDict.wzzszj, zjObj.toJSONString());
		// 每期
		JSONObject mqObj2 = null;
		Map<String, Object> mqMap = new TreeMap<String, Object>();
		Map<String, Object> map40 = null;
		for (String position : positionArray) {
			mqObj2 = new JSONObject();
			map40 = ZstUtil.makeMq(list40, list41, list10Day, position, numberArray);
			mqObj2.put("40", map40);
			mqMap.put(position, mqObj2);
		}
		JSONObject mqObj = new JSONObject(mqMap);
		redisservice.setHashMap(lottId + "_" + LotteryDict.wzzsmq, m.get("periods").toString(), mqObj.toJSONString());
	}
}