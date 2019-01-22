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
 * @desc 走势图公共service
 * @author abo
 * @date 2018年7月30日 上午10:57:53
 *
 */
@Service
public class LotteryZstCommonService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * 
	 * @desc
	 * @author abo
	 * @date 2018年7月30日 上午11:05:12
	 */
	public void execute(String lottId) {
		// 重庆时时彩
		if (lottId.equals(LotteryConstant.CQSSC)) {
			this.getZjzst(LotteryConstant.CQSSC);
			this.getMqzst(LotteryConstant.CQSSC);
		} else if (lottId.equals(LotteryConstant.GDKLSF)) {
			// 广东快乐十分
			this.getZjzst(LotteryConstant.GDKLSF);
			this.getMqzst(LotteryConstant.GDKLSF);
		} else if (lottId.equals(LotteryConstant.XYNC)) {
			// 幸运农场
			this.getZjzst(LotteryConstant.XYNC);
			this.getMqzst(LotteryConstant.XYNC);
		} else if (lottId.equals(LotteryConstant.GD11X5)) {
			// 广东11选5
			this.getZjzst(LotteryConstant.GD11X5);
			this.getMqzst(LotteryConstant.GD11X5);
		} else if (lottId.equals(LotteryConstant.XJSSC)) {
			// 新疆时时彩
			this.getZjzst(LotteryConstant.XJSSC);
			this.getMqzst(LotteryConstant.XJSSC);
		} else if (lottId.equals(LotteryConstant.TJSSC)) {
			// 天津时时彩
			this.getZjzst(LotteryConstant.TJSSC);
			this.getMqzst(LotteryConstant.TJSSC);
		} else if (lottId.equals(LotteryConstant.JX11X5)) {
			// 江西11选5
			this.getZjzst(LotteryConstant.JX11X5);
			this.getMqzst(LotteryConstant.JX11X5);
		}
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
		String[] pk10position = LotteryConstant.lotteryColumnStrMap.get(lottId).split(",");
		String[] pk10number = LotteryUtil.numArrayByLottId(lottId);
		JSONObject wzhmobj = MakeMqzst(list41, listAll, list10Day, pk10position, pk10number);
		if (m != null) {
			redisservice.setHashMap(lottId + "_" + LotteryDict.wzzsmq, m.get("periods").toString(), wzhmobj.toJSONString());
		}
	}

	/**
	 * 
	 * @desc 制作每期的走势图行数据，暂时不返回今日全部的数据
	 * @author abo
	 * @date 2018年7月31日 下午2:24:38
	 * @param list41
	 *            41期
	 * @param listAll
	 *            今日全部
	 * @param list4Day
	 *            10天期数
	 * @param positionArray
	 * @param numberArray
	 * @return
	 */
	private JSONObject MakeMqzst(List<Map<String, Object>> list41, List<Map<String, Object>> listAll, List<Map<String, Object>> list4Day, String[] positionArray, String[] numberArray) {
		JSONObject resultobj = new JSONObject(true);
		JSONObject ojb0 = null;
		Map<String, Object> ojb2 = null;// 第二层
		int s = numberArray.length / 2 - 1;
		int fxcs = 0;
		int zxcs = 0;
		int cfcs = 0;
		int jrdcs = 0;
		int jrxcs = 0;
		int jrylmax = 0;
		int jrdcxcs = 0;
		int jrscxcs = 0;
		for (String position : positionArray) {
			if (list41 != null) {
				ojb0 = new JSONObject();
				ojb2 = new TreeMap<String, Object>();
				for (String number : numberArray) {
					// 遺漏次數
					jrylmax = YilouUtil.hmzdyl2(list4Day, number, position);
					ojb2.put(number, jrylmax);
				}
				// 单双遗漏
				jrdcxcs = YilouUtil.dscxcs1(list41, true, position);
				jrscxcs = YilouUtil.dscxcs1(list41, false, position);
				ojb2.put("a4dan", jrdcxcs == 0 ? LotteryConstant.DAN : jrdcxcs);
				ojb2.put("a5shuang", jrscxcs == 0 ? LotteryConstant.SHUANG : jrscxcs);
				// 大小遗漏
				jrdcs = YilouUtil.dxcxcs1(list41, false, position, Integer.parseInt(numberArray[s]));
				jrxcs = YilouUtil.dxcxcs1(list41, true, position, Integer.parseInt(numberArray[s]));
				ojb2.put("a6da", jrdcs == 0 ? LotteryConstant.DA : jrdcs);
				ojb2.put("a7xiao", jrxcs == 0 ? LotteryConstant.XIAO : jrxcs);
				// 反向次数
				fxcs = YilouUtil.zfcs1(list41, true, position);
				// 正向次数
				zxcs = YilouUtil.zfcs1(list41, false, position);
				cfcs = YilouUtil.cfcs1(list41, position);
				ojb2.put("a1fan", fxcs == 0 ? LotteryConstant.FAN : fxcs);// 反向次数
				ojb2.put("a2chong", cfcs == 0 ? LotteryConstant.CHONG : cfcs);// 重号
				ojb2.put("a3zheng", zxcs == 0 ? LotteryConstant.ZHENG : zxcs);// 正向次数
			}
			ojb0.put("40", ojb2);
			resultobj.put(position, ojb0);
		}
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
		String[] pk10position = LotteryConstant.lotteryColumnStrMap.get(lottId).split(",");
		String[] pk10number = LotteryUtil.numArrayByLottId(lottId);
		JSONObject wzhmobj = makeSumZjzst(list40, list41, currentList, currentList1, list4Day, pk10position, pk10number);
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
			List<Map<String, Object>> list4Day, String[] positionArray, String[] numberArray) {
		JSONObject resultobj = new JSONObject(true);
		Map<String, Object> ojb = null;
		JSONObject ojb1 = null;
		JSONObject bigObj = null;
		int s = numberArray.length / 2 - 1;
		int jrdcxcs = 0;
		int jrscxcs = 0;
		int jrdcs = 0;
		int jrxcs = 0;
		int fxcs = 0;
		int zxcs = 0;
		int cfcs = 0;
		for (String position : positionArray) {
			bigObj = new JSONObject(true);
			// 40期
			if (null != list40) {
				ojb = new TreeMap<String, Object>();// 总次数
				ojb1 = new JSONObject(true);
				// 单双次数
				jrdcxcs = YilouUtil.dscxcs(list40, true, position);
				jrscxcs = YilouUtil.dscxcs(list40, false, position);
				jrdcs = YilouUtil.dxcxcs(list40, false, position, Integer.parseInt(numberArray[s]));
				jrxcs = YilouUtil.dxcxcs(list40, true, position, Integer.parseInt(numberArray[s]));
				// 反向次数
				fxcs = YilouUtil.zfcs(list41, true, position);
				// 正向次数
				zxcs = YilouUtil.zfcs(list41, false, position);
				cfcs = YilouUtil.cfcs(list41, position);
				for (String number : numberArray) {
					// 某個號碼在 某個位置出現的 次數
					int jrcxmax = YilouUtil.hmcxcs(list40, number, position);
					ojb.put(number, jrcxmax);
				}
				ojb.put("a4dan", jrdcxcs);
				ojb.put("a5shuang", jrscxcs);
				ojb.put("a6da", jrdcs);
				ojb.put("a7xiao", jrxcs);
				ojb.put("a1fan", fxcs);// 反向次数
				ojb.put("a3zheng", zxcs);// 正向次数
				ojb.put("a2chong", cfcs);// 重号
				ojb1.put("zcs", ojb);
				ojb = new TreeMap<String, Object>();// 总次数
				for (String number : numberArray) {
					// 遺漏次數
					int jrylmax = YilouUtil.hmzdyl1(list4Day, number, position, list40.size());
					ojb.put(number, jrylmax);
				}
				jrdcxcs = YilouUtil.dscxcsSum(list4Day, true, position, list40.size());
				ojb.put("a4dan", jrdcxcs);
				jrscxcs = YilouUtil.dscxcsSum(list4Day, false, position, list40.size());
				ojb.put("a5shuang", jrscxcs);

				jrdcs = YilouUtil.dxcxcsSum(list4Day, false, position, Integer.parseInt(numberArray[s]), list40.size());
				jrxcs = YilouUtil.dxcxcsSum(list4Day, true, position, Integer.parseInt(numberArray[s]), list40.size());
				ojb.put("a6da", jrdcs);
				ojb.put("a7xiao", jrxcs);
				// 反向次数
				fxcs = YilouUtil.zfcsSum(list4Day, true, position, list40.size());
				ojb.put("a1fan", fxcs);// 反向次数
				// 正向次数
				zxcs = YilouUtil.zfcsSum(list4Day, false, position, list40.size());
				ojb.put("a3zheng", zxcs);// 正向次数
				cfcs = YilouUtil.zhylSum(list4Day, position, list40.size());
				ojb.put("a2chong", cfcs);// 重号

				ojb1.put("zdyl", ojb);
				bigObj.put("40", ojb1);
			}
			// 今日全部期数
			if (null != currentList) {
				ojb = new TreeMap<String, Object>();// 总次数
				ojb1 = new JSONObject(true);
				// 单双次数
				jrdcxcs = YilouUtil.dscxcs(currentList, true, position);
				jrscxcs = YilouUtil.dscxcs(currentList, false, position);
				// 今日大出现次数
				jrdcs = YilouUtil.dxcxcs(currentList, false, position, Integer.parseInt(numberArray[s]));
				jrxcs = YilouUtil.dxcxcs(currentList, true, position, Integer.parseInt(numberArray[s]));
				// 反向次数
				fxcs = YilouUtil.zfcs(currentList1, true, position);
				// 正向次数
				zxcs = YilouUtil.zfcs(currentList1, false, position);
				cfcs = YilouUtil.cfcs(currentList1, position);
				for (String number : numberArray) {
					// 某個號碼在 某個位置出現的 次數
					int jrcxmax = YilouUtil.hmcxcs(currentList, number, position);
					ojb.put(number, jrcxmax);
				}
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
					// 遺漏次數
					int jrylmax = YilouUtil.hmzdyl1(list4Day, number, position, currentList.size());
					ojb.put(number, jrylmax);
				}
				fxcs = YilouUtil.zfcsSum(list4Day, true, position, currentList.size());// 反向次数
				zxcs = YilouUtil.zfcsSum(list4Day, false, position, currentList.size());// 正向次数
				cfcs = YilouUtil.zhylSum(list4Day, position, currentList.size());
				jrdcxcs = YilouUtil.dscxcsSum(list4Day, true, position, currentList.size());
				jrscxcs = YilouUtil.dscxcsSum(list4Day, false, position, currentList.size());
				ojb.put("a1fan", fxcs);
				ojb.put("a2chong", cfcs);// 重号
				ojb.put("a3zheng", zxcs);// 正向次数
				ojb.put("a4dan", jrdcxcs);
				ojb.put("a5shuang", jrscxcs);
				jrdcs = YilouUtil.dxcxcsSum(list4Day, false, position, Integer.parseInt(numberArray[s]), currentList.size());
				jrxcs = YilouUtil.dxcxcsSum(list4Day, true, position, Integer.parseInt(numberArray[s]), currentList.size());
				ojb.put("a6da", jrdcs);
				ojb.put("a7xiao", jrxcs);

				ojb1.put("zdyl", ojb);
				bigObj.put("all", ojb1);
			}
			resultobj.put(position, bigObj);
		}
		return resultobj;
	}
}