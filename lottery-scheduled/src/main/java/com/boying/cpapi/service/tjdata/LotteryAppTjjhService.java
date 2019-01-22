package com.boying.cpapi.service.tjdata;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;

import io.lottery.common.config.LotteryDict;

/**
 * 
 * @desc app端推荐计划
 * @author abo
 * @date 2018年8月22日 下午5:30:21
 *
 */
@Service
public class LotteryAppTjjhService {

	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * @author ms
	 * @param lottId
	 */
	public void execute(String lottId) {
		// app端号码推荐计划
		this.appHmTjjh(lottId);
		// app端两面推荐计划
		this.appLmTjjh(lottId);
	}

	/**
	 * 
	 * @desc 两面推荐计划.北京赛车27种两面推荐：十位单双，十位大小，五位龙虎，冠亚和大小，冠亚和单双
	 *       幸运飞艇27种两面推荐：十位单双，十位大小，五位龙虎，冠亚和大小，冠亚和单双
	 *       重庆时时彩13种两面推荐：5位单双，5位大小，1位龙虎，总和大小，总和单双
	 *       广东快乐十分39种两面推荐：8位单双，8位大小，8位合单双，8位尾大小，4位龙虎，总和大小，总和单双，总和尾大小
	 * @author abo
	 * @date 2018年8月27日 上午9:15:32
	 * @param lottId
	 */
	public void appLmTjjh(String lottId) {
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT) || lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.GDKLSF)) {
			// 今天
			String currentDate = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE);
			String numberStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			List<Map<String, Object>> list = this.lotteryProcessMapper.selectBjTrend(tablename, numberStr, "1", currentDate);
			Map<String, Object> map = list.get(0);
			JSONArray ja = new JSONArray();
			// 当前期号
			Long periods = Long.parseLong(map.get("periods").toString());
			// 获取最新一期的开奖期号
			JSONObject json = JSONObject.parseObject(redisservice.get(lottId + "_" + LotteryDict.zxkj));
			// 完成当前期的数据
			Object object = redisservice.getHashMap(lottId + "_" + LotteryDict.lmtjjh, periods + "");
			JSONObject json1 = null;
			if (object != null) {
				json1 = complateLmtjCurrenPeriodsRandom(object, map, json, lottId);
				redisservice.setHashMap(lottId + "_" + LotteryDict.lmtjjh, periods + "", json1.toJSONString());
//				System.out.println(lottId + "推荐计划完成本期数据：" + periods + "-----" + json1);
			}
			Long newPeriods = json.getLong("xqqs");
			// 生成下期的推荐号
			json1 = getNextPeriodsLmRandom(newPeriods, numberStr, lottId);
			// System.out.println(json1);
			redisservice.setHashMap(lottId + "_" + LotteryDict.lmtjjh, (newPeriods) + "", json1.toJSONString());
//			System.out.println(lottId + "推荐计划完成下期数据：" + (newPeriods));
		}
	}

	/**
	 * 
	 * @desc 完成两面推荐计划数据
	 * @author abo
	 * @date 2018年8月27日 上午10:48:30
	 * @param object
	 * @param map
	 * @param json
	 * @param lottId
	 * @return
	 */
	private JSONObject complateLmtjCurrenPeriodsRandom(Object object, Map<String, Object> map, JSONObject json, String lottId) {
//		System.out.println(object);
		JSONObject m = JSONObject.parseObject(object.toString());
		JSONArray ja1 = new JSONArray();
		JSONObject json1 = new JSONObject(true);
		for (Entry<String, Object> entry : m.entrySet()) {
			String key = entry.getKey();
			JSONArray ja = (JSONArray) entry.getValue();
			// 位置大小
			if (key.equals("wzdx")) {
				ja1 = complateLmtjWzdx(ja, map, json, lottId);
				json1.put("wzdx", ja);
			}
			// 位置单双
			if (key.equals("wzds")) {
				ja1 = complateLmtjWzDs(ja, map, json, lottId);
				json1.put("wzds", ja);
			}
			// 位置龙虎
			if (key.equals("wzlh")) {
				ja1 = complateLmtjWzLh(ja, map, json, lottId);
				json1.put("wzlh", ja);
			}
			if (key.equals("hdx")) {
				ja1 = complateLmtjHdx(ja, map, json, lottId);
				json1.put("hdx", ja);

			}
			if (key.equals("hds")) {
				ja1 = complateLmtjHds(ja, map, json, lottId);
				json1.put("hds", ja);
			}
			// 广东快乐十分
			if (lottId.equals(LotteryConstant.GDKLSF)) {
				if (key.equals("wzhds")) {// 广东快乐十分位置合单双
					ja1 = complateLmtjWzhds(ja, map, json, lottId);
					json1.put("wzhds", ja);
				} else if (key.equals("wzhdx")) {// 广东快乐十分位置尾大小
					ja1 = complateLmtjWzhdx(ja, map, json, lottId);
					json1.put("wzhdx", ja);
				} else if (key.equals("hwdx")) {// 总和尾大小
					ja1 = complateLmtjHwdx(ja, map, json, lottId);
					json1.put("hwdx", ja);
				}
			}
		}
		return json1;
	}

	private JSONArray complateLmtjHwdx(JSONArray ja, Map<String, Object> map, JSONObject zxkjJson, String lottId) {
		JSONArray ja1 = new JSONArray();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject json = (JSONObject) ja.get(i);
			String kjhm = "";
			String tjnum = json.get("tjnum").toString();
			String num = json.getString("num");
			// 统计输赢
			int tj = countHwdxsy(map, tjnum, lottId, num);
			json.put("shuying", tj);// 输赢，0是为开奖，1是赢，-1是输
			json.put("kjhm", kjhm);
			// 开奖号码数组
			json.put("kjhmArray", zxkjJson.get("dqhm"));
			ja1.add(json);
		}
		return ja1;
	}

	private int countHwdxsy(Map<String, Object> map, String tjnum, String lottId, String num) {
		int sum = 0;
		String[] numberArray = LotteryConstant.lotteryColumnStrMap.get(lottId).split(",");
		for (int i = 0; i < numberArray.length; i++) {
			sum += Integer.parseInt(map.get(numberArray[i]).toString());
		}
		String dx = "小";
		sum = sum % 10;
		if (sum > 4) {
			dx = "大";
		}
		if (dx.equals(tjnum)) {
			return 1;
		}
		return -1;
	}

	/**
	 * 
	 * @desc 广东快乐十分位置尾大小
	 * @author abo
	 * @date 2018年8月27日 下午3:51:25
	 * @param ja
	 * @param map
	 * @param json
	 * @param lottId
	 * @return
	 */
	private JSONArray complateLmtjWzhdx(JSONArray ja, Map<String, Object> map, JSONObject zxkjJson, String lottId) {
		JSONArray ja1 = new JSONArray();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject json = (JSONObject) ja.get(i);
			String tjnum = json.get("tjnum").toString();
			String num = json.getString("num");
			int tj = -1;
			int kjhm = Integer.parseInt(map.get(num).toString());
			kjhm = kjhm % 10;
			String dxStr = "小";
			if (kjhm > 4) {
				dxStr = "大";
			}
			if (dxStr.equals(tjnum)) {
				tj = 1;
			}
			json.put("shuying", tj);// 输赢，0是为开奖，1是赢，-1是输
			json.put("kjhm", kjhm);
			// 开奖号码数组
			json.put("kjhmArray", zxkjJson.get("dqhm"));
			ja1.add(json);
		}
		return ja1;
	}

	/**
	 * 
	 * @desc 两面计划，位置合单双
	 * @author abo
	 * @date 2018年8月27日 下午3:34:19
	 * @param ja
	 * @param map
	 * @param zxkjJson
	 * @param lottId
	 * @return
	 */
	private JSONArray complateLmtjWzhds(JSONArray ja, Map<String, Object> map, JSONObject zxkjJson, String lottId) {
		JSONArray ja1 = new JSONArray();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject json = (JSONObject) ja.get(i);
			String kjhm = "";
			String tjnum = json.get("tjnum").toString();
			String num = json.getString("num");
			// 统计输赢
			int tj = countWzhdssy(map, tjnum, lottId, num);
			json.put("shuying", tj);// 输赢，0是为开奖，1是赢，-1是输
			json.put("kjhm", kjhm);
			// 开奖号码数组
			json.put("kjhmArray", zxkjJson.get("dqhm"));
			ja1.add(json);
		}
		return ja1;
	}

	/**
	 * 
	 * @desc 两面计划，计算位置合单双输赢
	 * @author abo
	 * @date 2018年8月27日 下午3:40:09
	 * @param map
	 * @param tjnum
	 * @param lottId
	 * @param num
	 * @return
	 */
	private int countWzhdssy(Map<String, Object> map, String tjnum, String lottId, String num) {
		int sum = Integer.parseInt(map.get(num).toString());
		// 小于10的话就直接作为合，否则就要十位加上各位
		if (sum > 9) {
			sum = sum / 10 + (sum - 10);
		}
		String ds = "单";
		if (sum % 2 == 0) {
			ds = "双";
		}
		if (ds.equals(tjnum)) {
			return 1;
		}
		return -1;
	}

	private JSONArray complateLmtjHds(JSONArray ja, Map<String, Object> map, JSONObject zxkjJson, String lottId) {
		JSONArray ja1 = new JSONArray();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject json = (JSONObject) ja.get(i);
			String kjhm = "";
			String tjnum = json.get("tjnum").toString();
			String num = json.getString("num");
			// 统计输赢
			int tj = countHdssy(map, tjnum, lottId, num);
			json.put("shuying", tj);// 输赢，0是为开奖，1是赢，-1是输
			json.put("kjhm", kjhm);
			// 开奖号码数组
			json.put("kjhmArray", zxkjJson.get("dqhm"));
			ja1.add(json);
		}
		return ja1;
	}

	/**
	 * 
	 * @desc 计算总和单双输赢
	 * @author abo
	 * @date 2018年8月27日 下午2:15:25
	 * @param map
	 * @param tjnum
	 * @param lottId
	 * @param num
	 * @return
	 */
	private int countHdssy(Map<String, Object> map, String tjnum, String lottId, String num) {
		int sum = 0;
		// 北京赛车、幸运飞艇是5对，1和10,2和9,3和8,4和7,5和6比
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			sum = Integer.parseInt(map.get("num1").toString()) + Integer.parseInt(map.get("num2").toString());
		} else {
			String[] numberArray = LotteryConstant.lotteryColumnStrMap.get(lottId).split(",");
			for (int i = 0; i < numberArray.length; i++) {
				sum += Integer.parseInt(map.get(numberArray[i]).toString());
			}
		}
		String ds = "单";
		if (sum % 2 == 0) {
			ds = "双";
		}
		if (ds.equals(tjnum)) {
			return 1;
		}
		return -1;
	}

	/**
	 * 
	 * @desc 完成两面推荐位置和数据，北京赛车、飞艇是冠军和亚军的和，重庆时时彩是5个位置的总和，广东快乐十分是8个位置的总和
	 * @author abo
	 * @date 2018年8月27日 下午1:01:04
	 * @param ja
	 * @param map
	 * @param json
	 * @param lottId
	 * @return
	 */
	private JSONArray complateLmtjHdx(JSONArray ja, Map<String, Object> map, JSONObject zxkjJson, String lottId) {
		JSONArray ja1 = new JSONArray();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject json = (JSONObject) ja.get(i);
			String kjhm = "";
			String tjnum = json.get("tjnum").toString();
			String num = json.getString("num");
			// 统计输赢
			int tj = countWzhdxsy(map, tjnum, lottId, num);
			json.put("shuying", tj);// 输赢，0是为开奖，1是赢，-1是输
			json.put("kjhm", kjhm);
			// 开奖号码数组
			json.put("kjhmArray", zxkjJson.get("dqhm"));
			ja1.add(json);
		}
		return ja1;
	}

	private int countWzhdxsy(Map<String, Object> map, String tjnum, String lottId, String num) {
		int sum = 0;
		int value = 0;// 大小的阈值
		// 北京赛车、幸运飞艇是5对，1和10,2和9,3和8,4和7,5和6比
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			sum = Integer.parseInt(map.get("num1").toString()) + Integer.parseInt(map.get("num2").toString());
			value = 11;
		} else {
			String[] numberArray = LotteryConstant.lotteryColumnStrMap.get(lottId).split(",");
			for (int i = 0; i < numberArray.length; i++) {
				sum += Integer.parseInt(map.get(numberArray[i]).toString());
			}

			if (lottId.equals(LotteryConstant.CQSSC)) {
				value = 22;
			} else {
				value = 84;
			}
		}
		String dx = "小";
		if (sum > value) {
			dx = "大";
		}
		if (dx.equals(tjnum)) {
			return 1;
		}
		return -1;
	}

	/**
	 * 
	 * @desc 完成两面推荐位置龙虎数据，北京赛车5个位置龙虎，重庆时时彩1个位置，飞艇5个位置，广东快乐十分4个位置
	 * @author abo
	 * @date 2018年8月27日 下午1:01:04
	 * @param ja
	 * @param map
	 * @param json
	 * @param lottId
	 * @return
	 */
	private JSONArray complateLmtjWzLh(JSONArray ja, Map<String, Object> map, JSONObject zxkjJson, String lottId) {
		JSONArray ja1 = new JSONArray();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject json = (JSONObject) ja.get(i);
			String kjhm = "";
			String tjnum = json.get("tjnum").toString();
			String num = json.getString("num");
			// 统计输赢
			int tj = countWzlhsy(map, tjnum, lottId, num);
			json.put("shuying", tj);// 输赢，0是为开奖，1是赢，-1是输
			json.put("kjhm", kjhm);
			// 开奖号码数组
			json.put("kjhmArray", zxkjJson.get("dqhm"));
			ja1.add(json);
		}
		return ja1;
	}

	/**
	 * 
	 * @desc 计算龙虎输赢
	 * @author abo
	 * @date 2018年8月27日 下午1:30:02
	 * @param map
	 * @param tjnum
	 * @param lottId
	 * @param num
	 * @return
	 */
	private int countWzlhsy(Map<String, Object> map, String tjnum, String lottId, String num) {
		int startNum = Integer.parseInt(num.replace("num", ""));
		int endNum = 0;
		// 北京赛车、幸运飞艇是5对，1和10,2和9,3和8,4和7,5和6比
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			startNum = Integer.parseInt(num.replace("num", ""));
			endNum = 11 - startNum;
		}
		// 重庆时时彩是1对，1和5比
		if (lottId.equals(LotteryConstant.CQSSC)) {
			endNum = 5;
		}
		// 广东快乐十分是4对，1和8,2和7,3和6,4和5比
		if (lottId.equals(LotteryConstant.GDKLSF)) {
			endNum = 9 - startNum;
		}
		// 头数字
		int s = Integer.parseInt(map.get("num" + startNum) + "");
		// 尾数字
		int e = Integer.parseInt(map.get("num" + endNum) + "");
		String lh = "虎";
		// 头大于尾就是龙，否则就是虎
		if (s > e) {
			lh = "龍";
		}
		if (lh.equals(tjnum)) {
			return 1;
		}
		return -1;
	}

	/**
	 * 
	 * @desc 完成两面推荐位置单双数据
	 * @author abo
	 * @date 2018年8月27日 下午1:01:04
	 * @param ja
	 * @param map
	 * @param json
	 * @param lottId
	 * @return
	 */
	private JSONArray complateLmtjWzDs(JSONArray ja, Map<String, Object> map, JSONObject zxkjJson, String lottId) {
		JSONArray ja1 = new JSONArray();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject json = (JSONObject) ja.get(i);
			String kjhm = map.get(json.get("num")).toString();
			String tjnum = json.get("tjnum").toString();
			// 统计输赢
			int tj = countWzdssy(kjhm, tjnum, lottId);
			json.put("shuying", tj);// 输赢，0是为开奖，1是赢，-1是输
			json.put("kjhm", kjhm);
			// 开奖号码数组
			json.put("kjhmArray", zxkjJson.get("dqhm"));
			ja1.add(json);
		}
		return ja1;
	}

	/**
	 * 
	 * @desc 统计位置大小输赢
	 * @author abo
	 * @param lottId
	 * @date 2018年8月27日 上午11:10:05
	 * @param kjhm
	 * @param arraystr
	 * @return
	 */
	private int countWzdssy(String kjhmStr, String str, String lottId) {
		int kjhm = Integer.parseInt(kjhmStr);
		String dsStr = "单";
		if (kjhm % 2 == 0) {
			dsStr = "双";
		}
		if (dsStr.equals(str)) {
			return 1;
		}
		return -1;
	}

	/**
	 * 
	 * @desc 完成两面推荐位置大小数据
	 * @author abo
	 * @date 2018年8月27日 上午11:06:22
	 * @param object
	 * @param map
	 * @param lottId
	 * @param json2
	 * @return
	 */
	private JSONArray complateLmtjWzdx(JSONArray ja, Map<String, Object> map, JSONObject zxkjJson, String lottId) {
		JSONArray ja1 = new JSONArray();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject json = (JSONObject) ja.get(i);
			String kjhm = map.get(json.get("num")).toString();
			String tjnum = json.get("tjnum").toString();
			// 统计输赢
			int tj = countWzdxsy(kjhm, tjnum, lottId);
			json.put("shuying", tj);// 输赢，0是为开奖，1是赢，-1是输
			json.put("kjhm", kjhm);
			// 开奖号码数组
			json.put("kjhmArray", zxkjJson.get("dqhm"));
			ja1.add(json);
		}
		return ja1;
	}

	/**
	 * 
	 * @desc 统计位置大小输赢
	 * @author abo
	 * @param lottId
	 * @date 2018年8月27日 上午11:10:05
	 * @param kjhm
	 * @param arraystr
	 * @return
	 */
	private int countWzdxsy(String kjhmStr, String str, String lottId) {
		int kjhm = Integer.parseInt(kjhmStr);
		String dxStr = "小";
		if (kjhm > 5) {
			dxStr = "大";
		}
		if (dxStr.equals(str)) {
			return 1;
		}
		return -1;
	}

	/**
	 * 
	 * @desc 北京赛车27种两面推荐：十位单双，十位大小，五位龙虎，冠亚和大小，冠亚和单双
	 *       幸运飞艇27种两面推荐：十位单双，十位大小，五位龙虎，冠亚和大小，冠亚和单双
	 *       重庆时时彩13种两面推荐：5位单双，5位大小，1位龙虎，总和大小，总和单双
	 *       广东快乐十分39种两面推荐：8位单双，8位大小，8位合单双，8位尾大小，4位龙虎，总和大小，总和单双，总和尾大小
	 * @author abo
	 * @date 2018年8月27日 上午9:19:46
	 * @param newPeriods
	 * @param numberStr
	 * @param lottId
	 * @return
	 */
	private JSONObject getNextPeriodsLmRandom(Long periods, String numberStr, String lottId) {
		String[] numberArray = numberStr.split(",");
		JSONArray ja = null;
		JSONObject json = new JSONObject(true);
		// 位置单双
		ja = getPositionDstj(periods, numberArray, lottId);
		json.put("wzds", ja);
		// 位置大小
		ja = getPositionDxtj(periods, numberArray, lottId);
		json.put("wzdx", ja);
		// 位置龙虎，北京赛车和幸运飞艇是5对，时时彩是1对，广东快乐十分是4对
		ja = getPositionLhtj(periods, numberArray, lottId);
		json.put("wzlh", ja);
		String[] gyhNumberArray = { "num0" };
		if (lottId.equals(LotteryConstant.GDKLSF)) {
			// 广东快乐十分位置合单双
			ja = getPositionDstj(periods, numberArray, lottId);
			json.put("wzhds", ja);
			// 广东快乐十分位置尾大小
			ja = getPositionDxtj(periods, numberArray, lottId);
			json.put("wzhdx", ja);
			// 总和尾大小
			ja = getPositionDxtj(periods, gyhNumberArray, lottId);
			json.put("hwdx", ja);
		}
		// 冠亚和大小/总和大小
		ja = getPositionDstj(periods, gyhNumberArray, lottId);
		json.put("hds", ja);
		// 冠亚和单双/总和单双
		ja = getPositionDxtj(periods, gyhNumberArray, lottId);
		json.put("hdx", ja);

		return json;
	}

	private JSONArray getPositionWzhdstj(Long periods, String[] numberArray, String lottId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @desc 位置龙虎，北京赛车和幸运飞艇是5对，时时彩是1对，广东快乐十分是4对
	 * @author abo
	 * @date 2018年8月27日 上午9:55:42
	 * @param periods
	 * @param numberArray
	 * @param lottId
	 * @return
	 */
	private JSONArray getPositionLhtj(Long periods, String[] numberArray, String lottId) {
		JSONObject jo = null;
		JSONArray ja = new JSONArray();
		String num = "";
		int breakCount = 0;
		// 位置龙虎，北京赛车和幸运飞艇是5对，时时彩是1对，广东快乐十分是4对
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			breakCount = 5;
		} else if (lottId.equals(LotteryConstant.CQSSC)) {
			breakCount = 1;
		} else if (lottId.equals(LotteryConstant.GDKLSF)) {
			breakCount = 4;
		}
		for (int i = 0; i < numberArray.length; i++) {
			if (breakCount == i) {
				break;
			}
			jo = new JSONObject();
			num = numberArray[i];
			jo.put("qishu", periods);
			jo.put("tjnum", randomLh());
			jo.put("shuying", 0);// 输赢，0是为开奖，1是赢，-1是输
			jo.put("num", num);
			ja.add(jo);
		}
		return ja;
	}

	/**
	 * 
	 * @desc 随机龙虎
	 * @author abo
	 * @date 2018年8月27日 上午9:50:45
	 * @return
	 */
	private Object randomLh() {
		Random rd = new Random();
		if (rd.nextInt(10) % 2 == 0) {
			return "龍";
		}
		return "虎";
	}

	private JSONArray getPositionDxtj(Long periods, String[] numberArray, String lottId) {
		JSONObject jo = null;
		JSONArray ja = new JSONArray();
		String num = "";
		for (int i = 0; i < numberArray.length; i++) {
			jo = new JSONObject();
			num = numberArray[i];
			jo.put("qishu", periods);
			jo.put("tjnum", randomDx());
			jo.put("shuying", 0);// 输赢，0是为开奖，1是赢，-1是输
			jo.put("num", num);
			ja.add(jo);
		}
		return ja;
	}

	/**
	 * 
	 * @desc 随机单双
	 * @author abo
	 * @date 2018年8月27日 上午9:40:54
	 * @return
	 */
	private String randomDx() {
		Random rd = new Random();
		if (rd.nextInt(10) % 2 == 0) {
			return "大";
		}
		return "小";
	}

	/**
	 * 
	 * @desc 位置单双推荐
	 * @author abo
	 * @date 2018年8月27日 上午9:44:01
	 * @param periods
	 * @param numberArray
	 * @param lottId
	 * @return
	 */
	private JSONArray getPositionDstj(Long periods, String[] numberArray, String lottId) {
		JSONObject jo = null;
		JSONArray ja = new JSONArray();
		String num = "";
		for (int i = 0; i < numberArray.length; i++) {
			jo = new JSONObject();
			num = numberArray[i];
			jo.put("qishu", periods);
			jo.put("tjnum", randomDs());
			jo.put("shuying", 0);// 输赢，0是为开奖，1是赢，-1是输
			jo.put("num", num);
			ja.add(jo);
		}
		return ja;
	}

	/**
	 * 
	 * @desc 随机单双
	 * @author abo
	 * @date 2018年8月27日 上午9:40:54
	 * @return
	 */
	private String randomDs() {
		Random rd = new Random();
		if (rd.nextInt(10) % 2 == 0) {
			return "双";
		}
		return "单";
	}

	/**
	 * 
	 * @desc 号码推荐计划
	 * @author abo
	 * @date 2018年8月25日 上午10:33:58
	 * @param lottId
	 */
	public void appHmTjjh(String lottId) {
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT) || lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.GDKLSF)) {
			// 今天
			String currentDate = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE);
			String numberStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			List<Map<String, Object>> list = this.lotteryProcessMapper.selectBjTrend(tablename, numberStr, "1", currentDate);
			Map<String, Object> map = list.get(0);
			JSONArray ja = new JSONArray();
			// 当前期号
			Long periods = Long.parseLong(map.get("periods").toString());
			// 获取最新一期的开奖期号
			JSONObject json = JSONObject.parseObject(redisservice.get(lottId + "_" + LotteryDict.zxkj));
			// 完成当前期的数据
			Object object = redisservice.getHashMap(lottId + "_" + LotteryDict.hmtjjh, periods + "");
			if (object != null) {
				ja = complateCurrenPeriodsRandom(object, map, json);
				redisservice.setHashMap(lottId + "_" + LotteryDict.hmtjjh, periods + "", ja.toJSONString());
//				System.out.println(lottId + "推荐计划完成本期数据：" + periods + "-----" + ja);
			}
			Long newPeriods = json.getLong("xqqs");
			// 生成下期的推荐号
			ja = getNextPeriodsRandom(newPeriods, numberStr, lottId);
			redisservice.setHashMap(lottId + "_" + LotteryDict.hmtjjh, (newPeriods) + "", ja.toJSONString());
//			System.out.println(lottId + "推荐计划完成下期数据：" + (newPeriods));
		}

	}

	private JSONArray complateCurrenPeriodsRandom(Object object, Map<String, Object> map, JSONObject zxkjJson) {
		JSONArray ja1 = new JSONArray();
		JSONArray ja = JSONArray.parseArray(object.toString());
		for (int i = 0; i < ja.size(); i++) {
			JSONObject json = (JSONObject) ja.get(i);
			String kjhm = map.get(json.get("num")).toString();
			String arraystr = json.get("tjnum").toString();
			// 统计输赢
			int tj = countTjNum(kjhm, arraystr);
			json.put("shuying", tj);// 输赢，0是为开奖，1是赢，-1是输
			json.put("kjhm", kjhm);
			// 开奖号码数组
			json.put("kjhmArray", zxkjJson.get("dqhm"));
			ja1.add(json);
		}
		return ja1;
	}

	/**
	 * 
	 * @desc 判断推荐号码有没有命中
	 * @author abo
	 * @date 2018年8月25日 上午10:06:39
	 * @param kjhm
	 * @param arraystr
	 * @return
	 */
	private int countTjNum(String kjhm, String arraystr) {
		arraystr = arraystr.replaceAll("\\[", "");
		arraystr = arraystr.replaceAll("\\]", "");
		String[] array = arraystr.split(",");
		boolean result = Arrays.asList(array).contains(kjhm);
		if (result) {
			return 1;
		}
		return -1;
	}

	private Set<Integer> getRandomNumArray(String lottId) {
		Random r = new Random();
		Set<Integer> set = new HashSet<Integer>();
		int num = 10;
		int i = 1;
		int ballnum = 6;// 推荐的个数
		if (lottId.equals(LotteryConstant.GDKLSF)) {
			num = 20;
			ballnum = 12;
		}
		if (lottId.equals(LotteryConstant.CQSSC)) {
			i = 0;
		}
		while (set.size() < ballnum) {
			int v = r.nextInt(num) + i;
			set.add(v);
		}
		return set;
	}

	/**
	 * 
	 * @desc 完成下期狙击杀号基础数据
	 * @author abo
	 * @date 2018年8月21日 下午5:16:50
	 * @param periods
	 * @param numberStr
	 * @param lottId
	 * @return
	 */
	private JSONArray getNextPeriodsRandom(Long periods, String numberStr, String lottId) {
		String[] numberArray = numberStr.split(",");
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		String num = "";
		Set<Integer> set = null;
		for (int i = 0; i < numberArray.length; i++) {
			jo = new JSONObject();
			num = numberArray[i];
			set = getRandomNumArray(lottId);
			jo.put("qishu", periods);
			jo.put("tjnum", set);
			jo.put("shuying", 0);// 输赢，0是为开奖，1是赢，-1是输
			jo.put("num", num);
			ja.add(jo);
		}
		// System.out.println(ja);
		return ja;
	}
}
