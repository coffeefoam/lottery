package com.boying.cpapi.service.zsdata;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;
import com.boying.cpapi.util.Yilou.YilouUtil;

import io.lottery.common.config.LotteryDict;

/**
 * 
 * @desc app前端数据对比接口数据
 * @author abo
 * @date 2018年6月27日 下午2:58:59
 *
 */
@Service
public class LotteryAppSjdbService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * 
	 * @desc 生成当天app前端数据对比接口数据
	 * @author abo
	 * @date 2018年8月2日 下午3:53:55
	 * @param lottId
	 */
	public void execute(String lottId) {
		if (lottId.equals(LotteryConstant.BJSC)) {
			getSjdb(LotteryConstant.BJSC);
		} else if (lottId.equals(LotteryConstant.CQSSC)) {
			getSjdb(LotteryConstant.CQSSC);
		} else if (lottId.equals(LotteryConstant.TJSSC)) {
			getSjdb(LotteryConstant.TJSSC);
		} else if (lottId.equals(LotteryConstant.XJSSC)) {
			getSjdb(LotteryConstant.XJSSC);
		} else if (lottId.equals(LotteryConstant.GD11X5)) {
			getSjdb(LotteryConstant.GD11X5);
		} else if (lottId.equals(LotteryConstant.JX11X5)) {
			getSjdb(LotteryConstant.JX11X5);
		} else if (lottId.equals(LotteryConstant.XYFT)) {
			getSjdb(LotteryConstant.XYFT);
		} else if (lottId.equals(LotteryConstant.JSK3)) {
			getSjdb(LotteryConstant.JSK3);
		} else if (lottId.equals(LotteryConstant.XYNC)) {
			getSjdb(LotteryConstant.XYNC);
		} else if (lottId.equals(LotteryConstant.GDKLSF)) {
			getSjdb(LotteryConstant.GDKLSF);
		}
	}

	/**
	 * 
	 * @desc app数据对比，北京赛车，所有时时彩，所有11选5，幸运飞艇，江苏快3，幸运农场,广东快乐十分
	 * @author abo
	 * @date 2018年8月13日 下午7:27:49
	 * @param lottId
	 */
	public void getSjdb(String lottId) {
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 十天的数据
		List<Map<String, Object>> list10Day = lotteryProcessMapper.selectBjTrend(tablename, numstr, "10dayall",LotteryUtil.countDayOfNow(lottId));
		// 选择40期数
		List<Map<String, Object>> currentList = lotteryProcessMapper.selectBjTrend(tablename, numstr, "currenDay",LotteryUtil.countDayOfNow(lottId));
		List<Map<String, Object>> currentList1 = new ArrayList<>();// 今日期数+1
		List<Map<String, Object>> temp = new ArrayList<>();// 江苏快三临时的一个集合，因为江苏快三是需要3个号码的和进行计算
		// 江苏快三临时的一个集合，因为江苏快三是需要3个号码的和进行计算
		Map<String, Object> tempMap = null;
		String periods = "";
		int num1;
		// 取今日+1期
		for (int i = 0; i < currentList.size() + 1; i++) {
			if (lottId.equals(LotteryConstant.JSK3)) {// 江苏快三临时的一个集合，因为江苏快三是需要3个号码的和进行计算
				tempMap = list10Day.get(i);
				periods = list10Day.get(i).get("periods") + "";
				num1 = Integer.parseInt(list10Day.get(i).get("num1").toString()) + Integer.parseInt(list10Day.get(i).get("num2").toString())
						+ Integer.parseInt(list10Day.get(i).get("num3").toString());
				tempMap.put("periods", periods);
				tempMap.put("num1", num1);
				currentList1.add(tempMap);
				if (i < currentList.size()) {
					temp.add(tempMap);
				}
			} else {
				currentList1.add(list10Day.get(i));
			}
		}
		String[] positionArray;
		String[] numberArray;
		if (!lottId.equals(LotteryConstant.JSK3)) {
			positionArray = numstr.split(",");
			numberArray = LotteryUtil.numArrayByLottId(lottId);
		} else {// 江苏快三临时的一个集合，因为江苏快三是需要3个号码的和进行计算
			positionArray = new String[] { "num1" };
			numberArray = new String[] { "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18" };
			currentList = temp;
		}
		// 每期
		Map<String, Object> mqMap = new TreeMap<String, Object>();
		Map<String, Object> map40 = null;
		mqMap = new LinkedHashMap<>();
		for (String position : positionArray) {
			map40 = makeMq(position, numberArray, currentList, currentList1);
			mqMap.put(position, map40);
		}

		Map<String, Object> m = currentList.get(0);
		String currentDate = m.get("time").toString();
		// String currentDate = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE);
		JSONObject mqObj = new JSONObject(mqMap);
//		System.out.println(lottId + "_" + LotteryDict.sjdb + "====" + mqObj);
		redisservice.setHashMap(lottId + "_" + LotteryDict.sjdb, currentDate, mqObj.toJSONString());
	}

	/**
	 * 
	 * @desc
	 * @author abo
	 * @date 2018年8月14日 上午9:16:15
	 * @param position
	 * @param numberArray
	 * @param currentList
	 * @param currentList1
	 * @return
	 */
	public static Map<String, Object> makeMq(String position, String[] numberArray, List<Map<String, Object>> currentList, List<Map<String, Object>> currentList1) {
		JSONObject json = new JSONObject();
		// 遗漏
		for (String number : numberArray) {
			JSONArray temp = new JSONArray();
			// 遺漏次數
			int ylCount = YilouUtil.hmzdyl2(currentList, number, position);
			// 开出
			int kcCount = YilouUtil.hmcxcs(currentList, number, position);
			// 正向
			int zxCount = YilouUtil.appSjdbZfcsByNumber(currentList1, false, number, position);
			// 反向
			int fxCount = YilouUtil.appSjdbZfcsByNumber(currentList1, true, number, position);
			// 重复
			int cfCount = YilouUtil.cfcsByNumber(currentList1, number, position);
			temp.add(cfCount);
			temp.add(fxCount);
			temp.add(zxCount);
			temp.add(kcCount);
			temp.add(ylCount);
			json.put(number, temp);
		}
		return json;
	}
}