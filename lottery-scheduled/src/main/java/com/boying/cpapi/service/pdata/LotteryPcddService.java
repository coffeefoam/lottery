package com.boying.cpapi.service.pdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;

import io.lottery.common.config.LotteryDict;

/**
 * 
 * @desc pcdd业务处理
 * @author abo
 * @date 2018年7月2日 下午9:14:42
 *
 */
@Service
public class LotteryPcddService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * pcdd业务处理
	 * 
	 * @author ms
	 * @param lottId
	 */
	public void execute(String lottId) {
		// 历史开奖
		 getKjjl(lottId, null);
		 // 总和路珠
		 getZhlz(lottId);
		// 波色路珠
		getBslz(lottId);
	}

	/**
	 * 
	 * @desc pcdd开奖记录
	 * @author abo
	 * @param date
	 * @date 2018年6月27日 下午3:45:00
	 */
	public void getKjjl(String lottId, String date) {
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 查询数据库数据,添加了日期，如果有日期，则执行那一天的数据生成
		List<Map<String, Object>> list = null;
		if (StringUtils.isBlank(date)) {
			list = lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 100);
		} else {
			list = lotteryProcessMapper.findLotteryByDate(tablename, numStr, date);
		}
		for (Map<String, Object> m : list) {
			int num1 = Integer.parseInt(m.get("num1").toString());
			int num2 = Integer.parseInt(m.get("num2").toString());
			int num3 = Integer.parseInt(m.get("num3").toString());
			// 总和
			int sum = num1 + num2 + num3;
			// 总和大小
			String zhdx = LotteryConstant.XIAO;
			String zhds = LotteryConstant.DAN;
			String zhjdx = "";// 总和极大小
			// 单双判断
			if (sum % 2 == 0) {
				zhds = LotteryConstant.SHUANG;
			}
			// 极大极小判断
			if (sum < 6) {
				zhjdx = "极小";
			} else if (sum > 21) {
				zhjdx = "极大";
			}

			if (sum > 13) {
				zhdx = LotteryConstant.DA;
			}
			Map<String, Object> resmap = new HashMap<String, Object>();
			List<Object> numberlist = new ArrayList<Object>();
			numberlist.add(num1);
			numberlist.add(num2);
			numberlist.add(num3);

			resmap.put("rank", numberlist);
			resmap.put("periods", m.get("periods").toString());
			resmap.put("starttime", m.get("starttime"));

			resmap.put("zhSum", sum);

			resmap.put("zhJdx", zhjdx);// 总和极大小
			resmap.put("zhDs", zhds);// 总和单双
			resmap.put("zhDx", zhdx);// 总和大小
			resmap.put("zhDxds", zhdx + zhds);// 总和大小单双大单、大双，小单，小双
			JSONObject resobj = new JSONObject(resmap);
			// System.out.println(resobj);
			redisservice.setHashMap(lottId + "_" + LotteryDict.kjjl, resmap.get("periods").toString(), resobj.toJSONString());
		}
	}

	/**
	 * 
	 * @desc 总和路珠
	 * @author abo
	 * @date 2018年9月5日 下午12:39:19
	 * @param lottId
	 */
	public void getZhlz(String lottId) {
		// 当前时间
		String currentDate = LotteryUtil.countDayOfNow(lottId);
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		List<Map<String, Object>> currentDayList = lotteryProcessMapper.findLotteryByDate(tablename, numstr, currentDate);
		// 10个号码的今日号码累计
		Map<String, Object> tempMap = new LinkedHashMap<String, Object>();
		// 总和大
		int zhda = 0;
		// 总和小
		int zhxiao = 0;
		// 总和单
		int zhdan = 0;
		// 总和双
		int zhshuang = 0;
		for (Map<String, Object> map : currentDayList) {
			int num1 = Integer.parseInt(map.get("num1").toString());
			int num2 = Integer.parseInt(map.get("num2").toString());
			int num3 = Integer.parseInt(map.get("num3").toString());
			// 总和
			int sum = num1 + num2 + num3;
			if (sum > 13) {
				zhda++;
			} else {
				zhxiao++;
			}
			if (sum % 2 == 0) {
				zhshuang++;
			} else {
				zhdan++;
			}
		}
		tempMap.put("jrljdx", "大 (" + zhda + ") 小 (" + zhxiao + ")");
		tempMap.put("jrljdxStr", "总和大小");
		tempMap.put("jrljds", "单 (" + zhdan + ") 多 (" + zhshuang + ")");
		tempMap.put("jrljdsStr", "总和单双");
		// 从数据库查询200条数据
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numstr, 200);
		String preStr = "";
		int r = 0;// 列数
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String zhdx = "";
		for (Map<String, Object> map1 : list) {
			int num1 = Integer.parseInt(map1.get("num1").toString());
			int num2 = Integer.parseInt(map1.get("num2").toString());
			int num3 = Integer.parseInt(map1.get("num3").toString());
			// 总和
			int sum = num1 + num2 + num3;
			if (sum > 13) {
				zhdx = LotteryConstant.DA;
			} else {
				zhdx = LotteryConstant.XIAO;
			}
			String strTemp = zhdx;
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
		tempMap.put("itemdx", map);
		// 计算总和单双
		map = new LinkedHashMap<String, Object>();
		r = 0;
		String zhds = "";
		for (Map<String, Object> map1 : list) {
			int num1 = Integer.parseInt(map1.get("num1").toString());
			int num2 = Integer.parseInt(map1.get("num2").toString());
			int num3 = Integer.parseInt(map1.get("num3").toString());
			// 总和
			int sum = num1 + num2 + num3;
			if (sum % 2 == 0) {
				zhds = LotteryConstant.SHUANG;
			} else {
				zhds = LotteryConstant.DAN;
			}
			String strTemp = zhds;
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
		tempMap.put("itemds", map);
		JSONObject json = new JSONObject(tempMap);
		redisservice.set(lottId + "_" + LotteryDict.zhlz, json.toString());
		// System.out.println(lottId + "_" + LotteryDict.zhlz + "=" + tempMap);
	}

	/**
	 * 
	 * @desc 波色路珠
	 * @author abo
	 * @date 2018年9月5日 下午2:19:34
	 * @param lottId
	 */
	private void getBslz(String lottId) {
		// 当前时间
		String currentDate = LotteryUtil.countDayOfNow(lottId);
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		List<Map<String, Object>> currentDayList = lotteryProcessMapper.findLotteryByDate(tablename, numstr, currentDate);
		// 10个号码的今日号码累计
		JSONObject json = new JSONObject();
		int zhGreen = 0;// 总和绿
		int zhGray = 0;// 总和灰
		int zhRed = 0;// 总和红
		int zhBlue = 0;// 总和蓝
		for (Map<String, Object> map : currentDayList) {
			int num1 = Integer.parseInt(map.get("num1").toString());
			int num2 = Integer.parseInt(map.get("num2").toString());
			int num3 = Integer.parseInt(map.get("num3").toString());
			// 总和
			int sum = num1 + num2 + num3;
			String temp = this.getColorNum(sum);
			if (temp.equals(LotteryConstant.GRAY)) {
				zhGray++;
			}
			if (temp.equals(LotteryConstant.GREEN)) {
				zhGreen++;
			}
			if (temp.equals(LotteryConstant.RED)) {
				zhRed++;
			}
			if (temp.equals(LotteryConstant.BLUE)) {
				zhBlue++;
			}
		}
		json.put("jrlj", "红 (" + zhRed + ") 蓝 (" + zhBlue + ") 绿 (" + zhGreen + ") 灰 (" + zhGray + ")");
		json.put("title", "总和波色");
		// 从数据库查询200条数据
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numstr, 200);
		String preStr = "";
		int r = 0;// 列数
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (Map<String, Object> map1 : list) {
			int num1 = Integer.parseInt(map1.get("num1").toString());
			int num2 = Integer.parseInt(map1.get("num2").toString());
			int num3 = Integer.parseInt(map1.get("num3").toString());
			// 总和
			int sum = num1 + num2 + num3;
			String temp = this.getColorNum(sum);
			String strTemp = temp;
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
		JSONArray ja = new JSONArray();
		for (Entry<String, Object> entry : map.entrySet()) {
			ja.add(entry.getValue());
		}
		json.put("item", ja);
		json.put("type", "bose");
		redisservice.set(lottId + "_" + LotteryDict.bslz, json.toString());
		// System.out.println(lottId + "_" + LotteryDict.bslz + "=" + tempMap);
	}

	/**
	 * 
	 * @desc 转换波色
	 * @author abo
	 * @date 2018年9月5日 下午2:08:44
	 * @param num
	 * @return
	 */
	public String getColorNum(int num) {
		int[] grayArray = { 0, 13, 14, 27 };
		int[] greenArray = { 1, 4, 7, 10, 16, 19, 22, 25 };
		int[] blueArray = { 2, 5, 8, 11, 17, 20, 23, 26 };
		int[] redArray = { 3, 6, 9, 12, 15, 18, 21, 24 };
		if (Arrays.binarySearch(grayArray, num) > 0) {
			return LotteryConstant.GRAY;
		}
		if (Arrays.binarySearch(greenArray, num) > 0) {
			return LotteryConstant.GREEN;
		}
		if (Arrays.binarySearch(blueArray, num) > 0) {
			return LotteryConstant.BLUE;
		}
		if (Arrays.binarySearch(redArray, num) > 0) {
			return LotteryConstant.RED;
		}
		return "";
	}
}
