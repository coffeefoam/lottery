package com.boying.cpapi.service.zsdata;

import java.util.ArrayList;
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
 * @desc 江苏快三走势图service，江苏快3没有球号位置的说法，他是做3个骰子的总和进行大小单双判断
 * @author abo
 * @date 2018年7月30日 上午10:57:53
 *
 */
@Service
public class LotteryJsk3ZstService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * 
	 * @desc
	 * @author abo
	 * @date 2018年7月30日 上午11:05:12
	 * @param lottId
	 */
	public void execute() {
		// 江苏快3
		this.getZjzst(LotteryConstant.JSK3);
		this.getMqzst(LotteryConstant.JSK3);
	}

	/**
	 * 
	 * @desc 每期走势图
	 * @author abo
	 * @date 2018年7月31日 上午9:31:33
	 * @param lottId
	 */
	public void getMqzst(String lottId) {
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		List<Map<String, Object>> listAll = lotteryProcessMapper.selectBjTrend(tablename, numstr, "all",LotteryUtil.countDayOfNow(lottId));
		List<Map<String, Object>> list41 = new ArrayList<Map<String, Object>>();
		// 向前推十天的数据，用于计算最大遗漏
		List<Map<String, Object>> list10Day = lotteryProcessMapper.selectBjTrend(tablename, numstr, "10dayall",LotteryUtil.countDayOfNow(lottId));
		Map<String, Object> m = null;
		// 获取最新一期期号
		// if (listAll != null && listAll.size() > 0) {
		// m = listAll.get(0);
		// }
		// 取十天的数据最后一条，取当天的可能没数据
		if (list10Day != null && list10Day.size() > 0) {
			m = list10Day.get(0);
		}
		for (int i = 0; i < 41; i++) {
			list41.add(list10Day.get(i));
		}
		String position = "num1";
		String[] number = new String[] { "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18" };
		JSONObject wzhmobj = MakeMqzst(list41, listAll, list10Day, position, number);
		redisservice.setHashMap(lottId + "_" + LotteryDict.wzzsmq, m.get("periods").toString(), wzhmobj.toJSONString());
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
	 * @param position
	 * @param numberArray
	 * @return
	 */
	private JSONObject MakeMqzst(List<Map<String, Object>> list41, List<Map<String, Object>> listAll, List<Map<String, Object>> list4Day, String position, String[] numberArray) {
		JSONObject resultobj = new JSONObject(true);
		JSONObject ojb0 = null;
		Map<String, Object> ojb2 = null;// 第二层
		int fxcs = 0;
		int zxcs = 0;
		int cfcs = 0;
		int jrdcs = 0;
		int jrxcs = 0;
		int jrylmax = 0;
		int jrdcxcs = 0;
		int jrscxcs = 0;
		if (list41 != null) {
			ojb0 = new JSONObject();
			ojb2 = new TreeMap<String, Object>();
			for (String number : numberArray) {
				// 遺漏次數
				jrylmax = YilouUtil.hmzdyl2ByJsk3(list4Day, number);
				ojb2.put(number, jrylmax);
			}
			// 单双遗漏
			jrdcxcs = YilouUtil.dscxcs1ByJsk3(list41, true);
			jrscxcs = YilouUtil.dscxcs1ByJsk3(list41, false);
			ojb2.put("a4dan", jrdcxcs == 0 ? LotteryConstant.DAN : jrdcxcs);
			ojb2.put("a5shuang", jrscxcs == 0 ? LotteryConstant.SHUANG : jrscxcs);
			// 大小遗漏
			jrdcs = YilouUtil.dxcxcs1ByJsk3(list41, false);
			jrxcs = YilouUtil.dxcxcs1ByJsk3(list41, true);
			ojb2.put("a6da", jrdcs == 0 ? LotteryConstant.DA : jrdcs);
			ojb2.put("a7xiao", jrxcs == 0 ? LotteryConstant.XIAO : jrxcs);
			// 反向次数
			fxcs = YilouUtil.zfcs1ByJsk3(list41, true);
			// 正向次数
			zxcs = YilouUtil.zfcs1ByJsk3(list41, false);
			cfcs = YilouUtil.cfcs1ByJsk3(list41);
			ojb2.put("a1fan", fxcs == 0 ? LotteryConstant.FAN : fxcs);// 反向次数
			ojb2.put("a2chong", cfcs == 0 ? LotteryConstant.CHONG : cfcs);// 重号
			ojb2.put("a3zheng", zxcs == 0 ? LotteryConstant.ZHENG : zxcs);// 正向次数
		}
		ojb0.put("40", ojb2);
		resultobj.put(position, ojb0);
		return resultobj;
	}

	/**
	 * 
	 * @desc 总计走势图
	 * @author abo
	 * @date 2018年7月30日 上午10:58:05
	 * @param lottId
	 */
	public void getZjzst(String lottId) {
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		List<Map<String, Object>> currentList = lotteryProcessMapper.selectBjTrend(tablename, numstr, "all",LotteryUtil.countDayOfNow(lottId));// 今日期数
		List<Map<String, Object>> list40 = new ArrayList<>();// 40期
		List<Map<String, Object>> list41 = new ArrayList<>();// 41期
		List<Map<String, Object>> currentList1 = new ArrayList<>();// 今日期数+1
		// 向前推四天的数据，用于计算最大遗漏
		List<Map<String, Object>> list4Day = lotteryProcessMapper.selectBjTrend(tablename, numstr, "10dayall",LotteryUtil.countDayOfNow(lottId));
		for (int i = 0; i < 40; i++) {
			list40.add(list4Day.get(i));
		}
		for (int i = 0; i < 41; i++) {
			list41.add(list4Day.get(i));
		}
		for (int i = 0; i < currentList.size() + 1; i++) {
			currentList1.add(list4Day.get(i));
		}
		String position = "num1";
		String[] number = new String[] { "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18" };
		JSONObject wzhmobj = makeSumZjzst(list40, list41, currentList, currentList1, list4Day, position, number);
		redisservice.set(lottId + "_" + LotteryDict.wzzszj + "", wzhmobj.toJSONString());
	}

	/**
	 * 
	 * @desc
	 * @author abo
	 * @date 2018年8月1日 下午3:26:06
	 * @param list40
	 *            40期
	 * @param list41
	 *            41期
	 * @param currentList
	 *            今日所有
	 * @param currentList1
	 *            今日所有+1
	 * @param list4Day
	 *            之前4天的数据
	 * @param positionArray
	 * @param numberArray
	 * @return
	 */
	public static JSONObject makeSumZjzst(List<Map<String, Object>> list40, List<Map<String, Object>> list41, List<Map<String, Object>> currentList, List<Map<String, Object>> currentList1,
			List<Map<String, Object>> list4Day, String position, String[] numberArray) {
		JSONObject resultobj = new JSONObject(true);
		Map<String, Object> ojb = null;
		JSONObject ojb1 = null;
		JSONObject bigObj = null;
		int jrdcxcs = 0;
		int jrscxcs = 0;
		int jrdcs = 0;
		int jrxcs = 0;
		int fxcs = 0;
		int zxcs = 0;
		int cfcs = 0;
		bigObj = new JSONObject(true);
		// 40期
		if (null != list40) {
			ojb = new TreeMap<String, Object>();// 总次数
			ojb1 = new JSONObject(true);
			// 单双次数
			jrdcxcs = YilouUtil.dscxcsByJsk3(list40, true);
			jrscxcs = YilouUtil.dscxcsByJsk3(list40, false);
			// 大小
			jrdcs = YilouUtil.dxcxcsByJsk3(list40, false);
			jrxcs = YilouUtil.dxcxcsByJsk3(list40, true);
			// 反向次数
			fxcs = YilouUtil.zfcsByJsk3(list41, false);
			// 正向次数
			zxcs = YilouUtil.zfcsByJsk3(list41, true);
			cfcs = YilouUtil.cfcsByJsk3(list41);
			for (String number : numberArray) {
				ojb.put(number, 0);
			}
			// 某個號碼在 某個位置出現的 次數
			ojb = YilouUtil.hmcxcsByJsk3(list40, ojb);
			ojb.put("a1fan", fxcs);// 反向次数
			ojb.put("a2chong", cfcs);// 重号
			ojb.put("a3zheng", zxcs);// 正向次数
			ojb.put("a4dan", jrdcxcs);
			ojb.put("a5shuang", jrscxcs);
			ojb.put("a6da", jrdcs);
			ojb.put("a7xiao", jrxcs);
			ojb1.put("zcs", ojb);
			ojb = new TreeMap<String, Object>();// 总次数
			for (String number : numberArray) {
				// 某個號碼在 某個位置出現的 次數
				int num = YilouUtil.hmzdyl1ByJsk3(list4Day, number, list40.size());
				ojb.put(number, num);
			}
			jrdcxcs = YilouUtil.dscxcsSumByJsk3(list4Day, true, list40.size());
			ojb.put("a4dan", jrdcxcs);
			jrscxcs = YilouUtil.dscxcsSumByJsk3(list4Day, false, list40.size());
			ojb.put("a5shuang", jrscxcs);

			jrdcs = YilouUtil.dxcxcsSumByJsk3(list4Day, false, list40.size());
			jrxcs = YilouUtil.dxcxcsSumByJsk3(list4Day, true, list40.size());
			ojb.put("a6da", jrdcs);
			ojb.put("a7xiao", jrxcs);
			// 反向次数
			fxcs = YilouUtil.zfcsSumByJsk3(list4Day, true, list40.size());
			ojb.put("a1fan", fxcs);// 反向次数
			// 正向次数
			zxcs = YilouUtil.zfcsSumByJsk3(list4Day, false, list40.size());
			ojb.put("a3zheng", zxcs);// 正向次数
			cfcs = YilouUtil.zhylSumByJsk3(list4Day, list40.size());
			ojb.put("a2chong", cfcs);// 重号

			ojb1.put("zdyl", ojb);
			bigObj.put("40", ojb1);
		}
		// 今日全部期数
		if (null != currentList) {
			ojb = new TreeMap<String, Object>();// 总次数
			ojb1 = new JSONObject(true);
			// 单双次数
			jrdcxcs = YilouUtil.dscxcsByJsk3(currentList, true);
			jrscxcs = YilouUtil.dscxcsByJsk3(currentList, false);
			// 今日大出现次数
			jrdcs = YilouUtil.dxcxcsByJsk3(currentList, false);
			jrxcs = YilouUtil.dxcxcsByJsk3(currentList, true);
			// 反向次数
			fxcs = YilouUtil.zfcsByJsk3(currentList1, true);
			// 正向次数
			zxcs = YilouUtil.zfcsByJsk3(currentList1, false);
			cfcs = YilouUtil.cfcsByJsk3(currentList1);
			for (String number : numberArray) {
				ojb.put(number, 0);
			}
			// 某個號碼在 某個位置出現的 次數
			ojb = YilouUtil.hmcxcsByJsk3(currentList, ojb);

			ojb.put("a1fan", fxcs);// 反向次数
			ojb.put("a2chong", cfcs);// 重号
			ojb.put("a3zheng", zxcs);// 正向次数
			ojb.put("a4dan", jrdcxcs);
			ojb.put("a5shuang", jrscxcs);
			ojb.put("a6da", jrdcs);
			ojb.put("a7xiao", jrxcs);
			ojb1.put("zcs", ojb);
			ojb = new TreeMap<String, Object>();// 总次数
			for (String number : numberArray) {
				// 某個號碼在 某個位置出現的 次數
				int num = YilouUtil.hmzdyl1ByJsk3(list4Day, number, currentList.size());
				ojb.put(number, num);
			}
			jrdcxcs = YilouUtil.dscxcsSumByJsk3(list4Day, true, currentList.size());
			ojb.put("a4dan", jrdcxcs);
			jrscxcs = YilouUtil.dscxcsSumByJsk3(list4Day, false, currentList.size());
			ojb.put("a5shuang", jrscxcs);

			jrdcs = YilouUtil.dxcxcsSumByJsk3(list4Day, false, currentList.size());
			jrxcs = YilouUtil.dxcxcsSumByJsk3(list4Day, true, currentList.size());
			ojb.put("a6da", jrdcs);
			ojb.put("a7xiao", jrxcs);
			// 反向次数
			fxcs = YilouUtil.zfcsSumByJsk3(list4Day, true, currentList.size());
			ojb.put("a1fan", fxcs);// 反向次数
			// 正向次数
			zxcs = YilouUtil.zfcsSumByJsk3(list4Day, false, currentList.size());
			ojb.put("a3zheng", zxcs);// 正向次数
			cfcs = YilouUtil.zhylSumByJsk3(list4Day, currentList.size());
			ojb.put("a2chong", cfcs);// 重号

			ojb1.put("zdyl", ojb);
			bigObj.put("all", ojb1);
		}
		resultobj.put(position, bigObj);
		return resultobj;
	}
}