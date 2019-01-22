package com.boying.cpapi.service.zsdata;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.Yilou.YilouUtil;

/**
 * 
 * @desc 走势图总计和每期数据公共方法
 * @author abo
 * @date 2018年8月3日 下午7:53:19
 *
 */
public class ZstUtil {

	/**
	 * 
	 * @desc 制造每期数据
	 * @author abo
	 * @date 2018年8月2日 下午5:41:53
	 * @param list
	 * @param listAdd1
	 * @param list10Day
	 * @param position
	 * @param numberArray
	 * @return
	 */
	public static Map<String, Object> makeMq(List<Map<String, Object>> list, List<Map<String, Object>> listAdd1, List<Map<String, Object>> list10Day, String position, String[] numberArray) {
		Map<String, Object> ojb2 = null;// 第二层
		int s = numberArray.length / 2 - 1;
		if (listAdd1 != null) {
			ojb2 = new TreeMap<String, Object>();
			for (String number : numberArray) {
				// 遺漏次數
				int jrylmax = YilouUtil.hmzdyl2(list10Day, number, position);
				ojb2.put(number, jrylmax);
			}
			// 单双遗漏
			int jrdcxcs = YilouUtil.dscxcs1(listAdd1, true, position);
			int jrscxcs = YilouUtil.dscxcs1(listAdd1, false, position);
			ojb2.put("a4dan", jrdcxcs == 0 ? LotteryConstant.DAN : jrdcxcs);
			ojb2.put("a5shuang", jrscxcs == 0 ? LotteryConstant.SHUANG : jrscxcs);
			// 大小遗漏
			int jrdcs = YilouUtil.dxcxcs1(listAdd1, false, position, Integer.parseInt(numberArray[s]));
			int jrxcs = YilouUtil.dxcxcs1(listAdd1, true, position, Integer.parseInt(numberArray[s]));
			ojb2.put("a6da", jrdcs == 0 ? LotteryConstant.DA : jrdcs);
			ojb2.put("a7xiao", jrxcs == 0 ? LotteryConstant.XIAO : jrxcs);
			// 反向次数
			int fxcs = YilouUtil.zfcs1(listAdd1, true, position);
			// 正向次数
			int zxcs = YilouUtil.zfcs1(listAdd1, false, position);
			int cfcs = YilouUtil.cfcs1(listAdd1, position);
			ojb2.put("a1fan", fxcs == 0 ? LotteryConstant.FAN : fxcs);// 反向次数
			ojb2.put("a2chong", cfcs == 0 ? LotteryConstant.CHONG : cfcs);// 重号
			ojb2.put("a3zheng", zxcs == 0 ? LotteryConstant.ZHENG : zxcs);// 正向次数
		}
		return ojb2;
	}

	/**
	 * 
	 * @desc 位置走势图汇总
	 * @author abo
	 * @date 2018年8月2日 下午4:34:24
	 * @param list
	 * @param listAdd1
	 * @param list10Day
	 * @param positionArray
	 * @param numberArray
	 * @return
	 */
	public static JSONObject makeWzzstSum(List<Map<String, Object>> list, List<Map<String, Object>> listAdd1, List<Map<String, Object>> list10Day, String position, String[] numberArray) {
		JSONObject ojb = new JSONObject();
		int s = numberArray.length / 2 - 1;
		if (null != list) {
			Map<String, Object> zcs = new TreeMap<String, Object>();// 总次数
			// 单双次数
			int jrdcxcs = YilouUtil.dscxcs(list, true, position);
			int jrscxcs = YilouUtil.dscxcs(list, false, position);
			int jrdcs = YilouUtil.dxcxcs(list, false, position, Integer.parseInt(numberArray[s]));
			int jrxcs = YilouUtil.dxcxcs(list, true, position, Integer.parseInt(numberArray[s]));
			// 反向次数
			int fxcs = YilouUtil.zfcs(listAdd1, true, position);
			// 正向次数
			int zxcs = YilouUtil.zfcs(listAdd1, false, position);
			int cfcs = YilouUtil.cfcs(listAdd1, position);
			for (String number : numberArray) {
				// 某個號碼在 某個位置出現的 次數
				int jrcxmax = YilouUtil.hmcxcs(list, number, position);
				zcs.put(number, jrcxmax);
			}
			zcs.put("a4dan", jrdcxcs);
			zcs.put("a5shuang", jrscxcs);
			zcs.put("a6da", jrdcs);
			zcs.put("a7xiao", jrxcs);
			zcs.put("a1fan", fxcs);// 反向次数
			zcs.put("a3zheng", zxcs);// 正向次数
			zcs.put("a2chong", cfcs);// 重号

			Map<String, Object> zdyl = new TreeMap<String, Object>();// 最大遗漏
			for (String number : numberArray) {
				// 遺漏次數
				int jrylmax = YilouUtil.hmzdyl1(list10Day, number, position, list.size());
				zdyl.put(number, jrylmax);
			}
			jrdcxcs = YilouUtil.dscxcsSum(list10Day, true, position, list.size());
			zdyl.put("a4dan", jrdcxcs);
			jrscxcs = YilouUtil.dscxcsSum(list10Day, false, position, list.size());
			zdyl.put("a5shuang", jrscxcs);

			jrdcs = YilouUtil.dxcxcsSum(list10Day, false, position, Integer.parseInt(numberArray[s]), list.size());
			jrxcs = YilouUtil.dxcxcsSum(list10Day, true, position, Integer.parseInt(numberArray[s]), list.size());
			zdyl.put("a6da", jrdcs);
			zdyl.put("a7xiao", jrxcs);
			// 反向次数
			fxcs = YilouUtil.zfcsSum(list10Day, true, position, list.size());
			zdyl.put("a1fan", fxcs);// 反向次数
			// 正向次数
			zxcs = YilouUtil.zfcsSum(list10Day, false, position, list.size());
			zdyl.put("a3zheng", zxcs);// 正向次数
			cfcs = YilouUtil.zhylSum(list10Day, position, list.size());
			zdyl.put("a2chong", cfcs);// 重号
			ojb.put("zdyl", zdyl);
			ojb.put("zcs", zcs);
		}
		return ojb;
	}
}
