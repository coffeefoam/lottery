package com.boying.cpapi.service.pdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;
import com.boying.cpapi.util.TimeDict;

import io.lottery.common.config.LotteryDict;

/**
 * 
 * @desc 公共的彩票业务处理service
 * @author abo
 * @date 2018年6月27日 下午1:38:38
 *
 */
@Service
public class LotteryCommonService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * @desc 处理每个彩票都有的业务
	 * @author ms
	 * @param lottId
	 */
	public void execute(String lottId) {
		// 最新开奖
		this.getzxkj(lottId);
		// 不等于pc蛋蛋
		if (!lottId.equals(LotteryConstant.PCDD)) {
			// 号码走势图业务数据
			this.getWzzst(lottId);
			// 历史号码统计数据
			this.getLshmtj(lottId);
			// 两面投注参考
			this.getLmtzck(lottId, null);
			// 单双大小历史
			this.getDsdxls(lottId);
			// 大小路珠
			this.getDxlz(lottId);
			// 单双路珠
			this.getDslz(lottId);
			// 龙虎路珠
			this.getLhlz(lottId);
			// 中发白路珠
			this.getZfblz(lottId);
			// 东南西北路珠
			this.getDnxblz(lottId);
			// 尾数大小路珠
			this.getWsdxlz(lottId);
			// 合数单双路珠
			this.getHsdslz(lottId);
			// 总和路珠
			this.getZhlz(lottId);
			// 号码路珠
			this.getHmlz(lottId);
			// 幸运飞艇和北京赛车有号码走势图和冠亚和走势图
			if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
				this.getHmzst(lottId);
				this.getGyhzst(lottId);
			}
		}
		// 开奖视频
		this.lotteryVideo(lottId);
	}

	/**
	 * @Description: 最新开奖
	 * @author ms
	 * @date 2018年7月13日 下午6:20:05
	 */
	public void getzxkj(String lottId) {
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		String currentdate = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE);
		// 幸运飞艇下午13点05分开始到第二天凌晨04点05分结束，凌晨的开奖的数据库记录的是前一天的日期，因此特殊处理
		if (LotteryConstant.XYFT.equals(lottId)) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 4);
			cal.set(Calendar.MINUTE, 7);// 设置慢两分钟，确保时间准确
			if (new Date().before(cal.getTime())) {
				currentdate = DateUtil.getDateString(new Date(), null, null, -1, null, null, null, DateUtil.PATTERN_DATE);
			}
		}
		// 新疆时时彩全天96期，每天10:10到第二天凌晨02:00,十分钟一期，凌晨的开奖的数据库记录的是前一天的日期，因此特殊处理
		if (LotteryConstant.XJSSC.equals(lottId)) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 2);
			cal.set(Calendar.MINUTE, 2);// 设置慢两分钟，确保时间准确
			if (new Date().before(cal.getTime())) {
				currentdate = DateUtil.getDateString(new Date(), null, null, -1, null, null, null, DateUtil.PATTERN_DATE);
			}
		}
		List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByDate(tablename, numStr, currentdate);
		if (list != null && list.size() > 0) {
			Map<String, Object> m1 = list.get(0);
			// 本期期数
			String dqqs = m1.get("periods").toString();
			// 本期开奖号码
			List<Object> dqhm = new ArrayList<Object>();
			for (String position : numStr.split(",")) {
				dqhm.add(m1.get(position));
			}
			// 每日总期数
			int mrqs = LotteryConstant.lotteryTotalPeriodMap.get(lottId);
			// 今天已开出期数
			int ykqs = list.size();
			// 今天剩余期数
			int syqs = mrqs - ykqs;
			// 下期期数
			Long xqqs = Long.valueOf(dqqs) + 1;
			// 如果今天的奖开完了，获取下期开奖的期数是多少
			if (syqs == 0) {
				xqqs = LotteryUtil.getNextPeriods(lottId, dqqs);
			}
			// 下期开奖时间
			Long xqkjsj=null;
			String nextopentime=lotteryProcessMapper.getNextOpeningTime(LotteryConstant.lotteryIdOrderCode.get(lottId),list.size()+1);
			if(nextopentime!=null) {
				String nextopendatetime = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE)+" "+nextopentime;
				xqkjsj=DateUtil.string2Date(nextopendatetime,DateUtil.PATTERN_STANDARD).getTime();
			}else {
				//如果今天没有开奖了，查询第二天的第一期的开奖时间
				nextopentime=lotteryProcessMapper.getNextOpeningTime(LotteryConstant.lotteryIdOrderCode.get(lottId),1);
				String nextopendate=null;
				//新疆时时彩本来就是开到凌晨，因此取当天凌晨日期即可
				if (LotteryConstant.XJSSC.equals(lottId)||LotteryConstant.XYFT.equals(lottId)) {
					nextopendate=DateUtil.geCurrentDate(DateUtil.PATTERN_DATE);
				}else {
					nextopendate=DateUtil.getDateString(new Date(), null, null, 1, null, null, null, DateUtil.PATTERN_DATE);
				}
				String nextopendatetime = nextopendate+" "+nextopentime;
				xqkjsj=DateUtil.string2Date(nextopendatetime,DateUtil.PATTERN_STANDARD).getTime();
			}
			xqkjsj=xqkjsj-TimeDict.Fifteen_Seconds;
			//开始时间间隔
			Long jgsj=LotteryUtil.getLotteryIntervalTime(lottId);
			JSONObject resobj = new JSONObject(true);
			resobj.put("xqkjsj", xqkjsj);
			resobj.put("dqqs", dqqs);
			resobj.put("mrqs", mrqs);
			resobj.put("ykqs", ykqs);
			resobj.put("syqs", syqs);
			resobj.put("xqqs", xqqs);
			resobj.put("dqhm", dqhm);
			resobj.put("jgsj", jgsj);
			redisservice.set(lottId + "_" + LotteryDict.zxkj, resobj.toJSONString());
		}
	}

	/**
	 * @desc 处理位置走势图业务数据
	 * @author abo
	 * @date 2018年6月27日 下午1:27:20
	 */
	public void getWzzst(String lottId) {
		// 快乐8没有号码走势图，这里就跳过
		if (lottId.equals("kl8")) {
			return;
		}
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 50);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 江苏快3是总和的走势图
		if (LotteryConstant.JSK3.equals(lottId)) {
			Map<String, String> tempMap = new TreeMap<String, String>();
			for (Map<String, Object> m : list) {
				String numstr = Integer.parseInt(m.get("num1").toString()) + Integer.parseInt(m.get("num2").toString()) + Integer.parseInt(m.get("num3").toString()) + "";
				// 把当前期数和号码存入map
				tempMap.put(m.get("periods").toString(), numstr);
			}
			map.put("num1", tempMap);
		} else {
			for (int i = 1; i <= num; i++) {
				Map<String, String> tempMap = new TreeMap<String, String>();
				for (Map<String, Object> m : list) {
					String numstr = m.get("num" + i).toString();
					// 把当前期数和号码存入map
					tempMap.put(m.get("periods").toString(), numstr);
				}
				map.put("num" + i, tempMap);
			}
		}
		JSONObject resobj = new JSONObject(map);
		redisservice.set(lottId + "_" + LotteryDict.zst, resobj.toJSONString());
	}

	/**
	 * @desc 处理冠亚和走势图业务数据
	 * @author abo
	 * @date 2018年6月27日 下午1:27:20
	 */
	public void getGyhzst(String lottId) {
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 50);
		Map<String, Object> tempMap = new TreeMap<String, Object>();
		for (Map<String, Object> m : list) {
			int numstr = Integer.parseInt(m.get("num1").toString()) + Integer.parseInt(m.get("num2").toString());
			// 把当前期数和号码存入map
			tempMap.put(m.get("periods").toString(), numstr + "");
		}
		JSONObject resobj = new JSONObject(tempMap);
		redisservice.set(lottId + "_" + LotteryDict.gyhzst, resobj.toJSONString());
	}

	/**
	 * 
	 * @desc 号码走势图
	 * @author abo
	 * @date 2018年8月14日 上午11:23:23
	 * @param lottId
	 */
	public void getHmzst(String lottId) {
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 50);
		String[] posiArray = LotteryConstant.lotteryColumnStrMap.get(lottId).split(",");
		String[] numArray = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Map<String, Object> map1 = new TreeMap<String, Object>();
		for (int i = 0; i < numArray.length; i++) {
			String num = numArray[i];
			map1 = new TreeMap<String, Object>();
			for (Map<String, Object> m : list) {
				for (int j = 0; j < posiArray.length; j++) {
					String numstr = m.get(posiArray[j]).toString();
					if (num.equals(numstr)) {
						map1.put(m.get("periods").toString(), posiArray[j].replace("num", ""));
					}
				}
			}
			map.put(numArray[i], map1);
		}
		JSONObject resobj = new JSONObject(map);
		redisservice.set(lottId + "_" + LotteryDict.hmzst, resobj.toJSONString());
	}

	/**
	 * @desc 处理历史号码统计数据 重庆时时彩 广东快乐十分 广东11选5 江苏快3 新疆时时彩 天津时时彩 江西11选5 (无数据：圣地彩)
	 * @author xg
	 * @author 2018年6月27日
	 * @throws ParseException
	 */
	public void getLshmtj(String lottId) {
		// 这里就跳过
		if (lottId.equals(LotteryConstant.PCDD) || lottId.equals(LotteryConstant.SDC) || lottId.equals(LotteryConstant.BJSC) 
			|| lottId.equals(LotteryConstant.XYFT)|| lottId.equals(LotteryConstant.KL8)) {
			return;
		}
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);
		List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByToday(tablename, numStr, LotteryUtil.countDayOfNow(lottId));
		if (list == null) {
			return;
		}
		Map<String, Integer> hashMap = new HashMap<String, Integer>();
		// put进号码范围初始值为0
		for (int i = LotteryConstant.lotteryNumberRangeMap.get(lottId + "S"); i <= LotteryConstant.lotteryNumberRangeMap.get(lottId + "B"); i++) {
			hashMap.put("num" + i, 0);
		}
		hashMap.put("dan", 0);
		hashMap.put("shuang", 0);
		hashMap.put("da", 0);
		hashMap.put("xiao", 0);
		hashMap.put("long", 0);
		hashMap.put("hu", 0);
		hashMap.put("lfping", 0);
		// 统计数量
		for (Map<String, Object> map : list) {
			// 统计龙虎平
			Integer n1 = (Integer) map.get("num1");
			Integer n5 = (Integer) map.get("num" + num);
			if (n1 > n5) {
				hashMap.put("long", hashMap.get("long") + 1);
			} else if (n1 < n5) {
				hashMap.put("hu", hashMap.get("hu") + 1);
			} else {
				hashMap.put("lfping", hashMap.get("lfping") + 1);
			}
			// 统计大小单双-0-9
			for (int i = 1; i <= num; i++) {
				// 得到号码
				Integer haoma = (Integer) map.get("num" + i);
				for (int j = LotteryConstant.lotteryNumberRangeMap.get(lottId + "S"); j <= LotteryConstant.lotteryNumberRangeMap.get(lottId + "B"); j++) {
					if (haoma == j) {
						hashMap.put("num" + j, hashMap.get("num" + j) + 1);
					}
				}
				if (haoma % 2 == 1) {
					hashMap.put("dan", hashMap.get("dan") + 1);
				}
				if (haoma % 2 == 0) {
					hashMap.put("shuang", hashMap.get("shuang") + 1);
				}
				if (haoma >= LotteryConstant.lotteryBigBorderMap.get(lottId)) {
					hashMap.put("da", hashMap.get("da") + 1);
				} else {
					hashMap.put("xiao", hashMap.get("xiao") + 1);
				}
			}
		}
		JSONObject resobj = new JSONObject(new LinkedHashMap<String, Object>(hashMap));
		// 今天的日期
		String currentDate = LotteryUtil.countDayOfNow(lottId);
		redisservice.setHashMap(lottId + "_" + LotteryDict.lshmtj, currentDate, resobj.toJSONString());
		redisservice.set(lottId + "_" + LotteryDict.jrhmtj, resobj.toJSONString());
	}

	/**
	 * @desc 两面投注参考 重庆时时彩 广东快乐十分 幸运飞艇 幸运农场 广东11选5 (无数据：圣地彩) 新疆时时彩 天津时时彩 江西11选5
	 * @author xg
	 * @author 2018年6月28日
	 */
	public void getLmtzck(String lottId, String date) {
		// 这里就跳过
		if (lottId.equals(LotteryConstant.PCDD) || lottId.equals(LotteryConstant.SDC) || lottId.equals(LotteryConstant.KL8) 
			|| lottId.equals(LotteryConstant.JSK3)|| lottId.equals(LotteryConstant.BJSC)) {
			return;
		}
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);
		// 按照日期查询
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
		Long max = Long.valueOf(map.get("max").toString());
		// 今天最后1期期数从开始到后到倒数第1位的截取
		String strMaxPre = max.toString().substring(0, max.toString().length() - 1);
		// 今天最后1期数最后1位
		String strMaxSuf1 = max.toString().substring(max.toString().length() - 1, max.toString().length());
		String beginPeriods = null;
		String endPeriods = String.valueOf(max);
		if (strMaxSuf1.equals("0")) {
			beginPeriods = Long.parseLong(strMaxPre) - 1 + "1";
		} else {
			beginPeriods = strMaxPre + "1";
		}
		if (endPeriods != null && beginPeriods != null) {
			List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByPeriods(tablename, numStr, beginPeriods, endPeriods, LotteryUtil.countDayOfNow(lottId));
			if (list == null) {
				return;
			}
			Map<String, Map<String, Object>> bigMap = new LinkedHashMap<String, Map<String, Object>>();
			String sKey = "";
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
			redisservice.setHashMap(lottId + "_" + LotteryDict.lmtzck, sKey, resobj.toJSONString());
		}
		// }
	}

	/**
	 * @desc 单双大小历史 北京赛车pk10 重庆时时彩 广东快乐十分 幸运飞艇 幸运农场 圣地彩 广东11选5 新疆时时彩 天津时时彩
	 *       江西11选5
	 * @author xg
	 * @author 2018年6月30日
	 */
	public void getDsdxls(String lottId) {
		// 这里就跳过
		if (lottId.equals(LotteryConstant.PCDD) || lottId.equals(LotteryConstant.SDC) || lottId.equals(LotteryConstant.KL8) 
				|| lottId.equals(LotteryConstant.JSK3)) {
			return;
		}
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);
		List<Map<String, Object>> list = lotteryProcessMapper.findLotteryByToday(tablename, numstr, LotteryUtil.countDayOfNow(lottId));
		if (list == null) {
			return;
		}
		Map<String, Map<String, Integer>> bigMap = new LinkedHashMap<String, Map<String, Integer>>();
		for (Map<String, Object> mapData : list) {
			// 统计大小单双
			for (int i = 1; i <= num; i++) {
				Map<String, Integer> smap = bigMap.get("num" + i);
				if (smap == null) {
					smap = new HashMap<String, Integer>();
					smap.put("dan", 0);
					smap.put("shuang", 0);
					smap.put("da", 0);
					smap.put("xiao", 0);
				}
				Integer haoma = (Integer) mapData.get("num" + i);
				if (haoma >= LotteryConstant.lotteryBigBorderMap.get(lottId)) {
					smap.put("da", smap.get("da") + 1);
				} else {
					smap.put("xiao", smap.get("xiao") + 1);
				}
				if (haoma % 2 == 1) {
					smap.put("dan", smap.get("dan") + 1);
				}
				if (haoma % 2 == 0) {
					smap.put("shuang", smap.get("shuang") + 1);
				}
				bigMap.put("num" + i, smap);
			}
		}
		JSONObject resobj = new JSONObject(new HashMap<String, Object>(bigMap));
		// 今天的日期
		String currentDate = LotteryUtil.countDayOfNow(lottId);
		redisservice.setHashMap(lottId + "_" + LotteryDict.dsdxls, currentDate, resobj.toJSONString());
	}

	/**
	 * @desc 大小路珠 北京赛车pk10 重庆时时彩 广东快乐十分 幸运飞艇 幸运农场 圣地彩 广东11选5 新疆时时彩 天津时时彩 江西11选5
	 * @author xg
	 * @author 2018年7月02日
	 */
	public void getDxlz(String lottId) {
		// 这里就跳过
		if (lottId.equals(LotteryConstant.PCDD) || lottId.equals(LotteryConstant.SDC) || lottId.equals(LotteryConstant.KL8) || lottId.equals(LotteryConstant.JSK3)) {
			return;
		}
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
		Map<String, ArrayList<String>> bigMap = new HashMap<String, ArrayList<String>>();
		// 变量0 小 1 大 2默认 用来记录上次大小
		Map<String, Integer> aMap = new LinkedHashMap<String, Integer>();
		for (Map<String, Object> map : list) {
			// 统计大小
			for (int i = 1; i <= num; i++) {
				ArrayList<String> list2 = bigMap.get("num" + i);
				Integer a = aMap.get("num" + i);
				if (a == null) {
					a = 2;
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
				if (haoma >= LotteryConstant.lotteryBigBorderMap.get(lottId)) {
					// 如果上次为大
					if (a == 1 || a == 2) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "大");
					} else {
						// 如果上次为小
						// 如果大于55列就停下来
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("大");
					}
					aMap.put("num" + i, 1);
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "da", lottId);
				} else {
					// 如果上次为大
					if (a == 1) {
						// 如果大于55列就停下来
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("小");
					} else if (a == 2 || a == 0) {
						// 如果上次为小
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "小");
					}
					aMap.put("num" + i, 0);
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "xiao", lottId);
				}
				bigMap.put("num" + i, list2);
			}
		}
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		JSONObject resobj = new JSONObject(new HashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.dxlz, resobj.toJSONString());
		// }
	}

	/**
	 * @desc 单双路珠 北京赛车pk10 重庆时时彩 广东快乐十分 幸运飞艇 幸运农场 圣地彩 广东11选5 新疆时时彩 天津时时彩 江西11选5
	 * @author xg
	 * @author 2018年6月29日
	 */
	public void getDslz(String lottId) {
		// 这里就跳过
		if (lottId.equals(LotteryConstant.PCDD) || lottId.equals(LotteryConstant.SDC) || lottId.equals(LotteryConstant.KL8) 
				|| lottId.equals(LotteryConstant.JSK3)) {
			return;
		}
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
		Map<String, ArrayList<String>> bigMap = new LinkedHashMap<String, ArrayList<String>>();
		// 变量0 单 1 双 2默认 用来记录上次单双
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		for (Map<String, Object> map : list) {
			// 统计大小
			for (int i = 1; i <= num; i++) {
				ArrayList<String> list2 = bigMap.get("num" + i);
				Integer a = aMap.get("num" + i);
				if (a == null) {
					// a定义默认变量
					a = 2;
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
				if (haoma % 2 == 0) {
					// 如果上次为大
					if (a == 1 || a == 2) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "双");
					} else {
						// 如果上次为小
						// 如果大于55列就停下来
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("双");
					}
					aMap.put("num" + i, 1);
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "shuang", lottId);
				} else {
					// 如果上次为大
					if (a == 1) {
						// 如果大于55列就停下来
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("单");
					} else if (a == 2 || a == 0) {
						// 如果上次为小
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "单");
					}
					aMap.put("num" + i, 0);
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "dan", lottId);
				}
				bigMap.put("num" + i, list2);
			}
		}
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		// 单双路珠
		JSONObject resobj = new JSONObject(new HashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.dslz, resobj.toJSONString());
	}

	/**
	 * @desc 龙虎路珠 重庆时时彩 圣地彩 广东11选5 新疆时时彩 天津时时彩 江西11选5
	 * @author xg
	 * @author 2018年7月2日
	 */
	public void getLhlz(String lottId) {
		// 这里就跳过
		if (lottId.equals(LotteryConstant.PCDD) || lottId.equals(LotteryConstant.SDC) || lottId.equals(LotteryConstant.KL8) || lottId.equals(LotteryConstant.JSK3)
				|| lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.GDKLSF) || lottId.equals(LotteryConstant.XYFT)) {
			return;
		}
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
		Map<String, ArrayList<String>> bigMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> lhlist = new ArrayList<String>();
		lhlist.add("");
		// 变量0 龙 1 虎 2和 3默认 用来记录上次龙虎和
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		for (Map<String, Object> map : list) {
			// 统计大小
			Integer a = aMap.get("num1");
			if (a == null) {
				// a定义默认变量
				a = 3;
			}
			// 如果大于55列就停下来
			if (aMap.get("continue1") != null) {
				continue;
			}
			Integer haoma = (Integer) map.get("num1");
			Integer haomaB = (Integer) map.get("num" + num);
			if (haoma > haomaB) {
				if (a == 0 || a == 3) {
					String string = lhlist.get(lhlist.size() - 1);
					lhlist.set(lhlist.size() - 1, string + "龍");
				} else {
					if (lhlist.size() >= 55) {
						// 列已经满标记
						aMap.put("continue1", 1);
						continue;
					}
					lhlist.add("龍");
				}
				LotteryUtil.countNum("num1", bigMap, map.get("time").toString(), "long", lottId);
				aMap.put("num1", 0);
			} else if (haoma < haomaB) {
				if (a == 1 || a == 3) {
					String string = lhlist.get(lhlist.size() - 1);
					lhlist.set(lhlist.size() - 1, string + "虎");
				} else {
					if (lhlist.size() >= 55) {
						// 列已经满标记
						aMap.put("continue1", 1);
						continue;
					}
					lhlist.add("虎");
				}
				LotteryUtil.countNum("num1", bigMap, map.get("time").toString(), "hu", lottId);
				aMap.put("num1", 1);
			} else {
				if (a == 2 || a == 3) {
					String string = lhlist.get(lhlist.size() - 1);
					lhlist.set(lhlist.size() - 1, string + "和");
				} else {
					if (lhlist.size() >= 55) {
						// 列已经满标记
						aMap.put("continue1", 1);
						continue;
					}
					lhlist.add("和");
				}
				LotteryUtil.countNum("num1", bigMap, map.get("time").toString(), "he", lottId);
				aMap.put("num1", 2);
			}
		}
		// 统计默认为0
		LotteryUtil.initNum("num1", bigMap, "he");
		// 反转list顺序
		Collections.reverse(lhlist);
		bigMap.put("num1", lhlist);
		// 单双路珠
		JSONObject resobj = new JSONObject(new LinkedHashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.lhlz, resobj.toJSONString());
	}

	/**
	 * @desc 中发白路珠 广东快乐十分 幸运农场
	 * @author xg
	 * @author 2018年7月02日
	 */
	public void getZfblz(String lottId) {
		// 这里就跳过
		if (!(lottId.equals(LotteryConstant.GDKLSF) || lottId.equals(LotteryConstant.XYNC))) {
			return;
		}
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
		Map<String, ArrayList<String>> bigMap = new HashMap<String, ArrayList<String>>();
		// 变量 0中 1发 2白 3默认 用来记录上次记录
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		for (Map map : list) {
			// 统计大小
			for (int i = 1; i <= num; i++) {
				ArrayList<String> list2 = bigMap.get("num" + i);
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
				if (haoma <= 7) {
					// 如果上次为中或默认
					if (a == 0 || a == 3) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "中");
					} else {
						// 如果上次不为中或默认
						// 如果大于55列就停下来
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("中");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "zhong", lottId);
					aMap.put("num" + i, 0);
				} else if (haoma >= 8 && haoma <= 14) {
					// 如果上次为发或默认
					if (a == 1 || a == 3) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "发");
					} else {
						// 如果上次不为发或默认
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("发");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "fa", lottId);
					aMap.put("num" + i, 1);
				} else if (haoma >= 15) {
					// 如果上次为发或默认
					if (a == 2 || a == 3) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "白");
					} else {
						// 如果上次不为发或默认
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("白");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "bai", lottId);
					aMap.put("num" + i, 2);
				}
				bigMap.put("num" + i, list2);
			}
		}
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		JSONObject resobj = new JSONObject(new LinkedHashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.zfblz, resobj.toJSONString());
	}

	/**
	 * @desc 东南西北路珠 广东快乐十分 幸运农场
	 * @author xg
	 * @author 2018年7月02日
	 */
	public void getDnxblz(String lottId) {
		// 这里就跳过
		if (!(lottId.equals(LotteryConstant.GDKLSF) || lottId.equals(LotteryConstant.XYNC))) {
			return;
		}
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
		Map<String, ArrayList<String>> bigMap = new HashMap<String, ArrayList<String>>();
		// 变量 0东 1南 2西 3北 4默认 用来记录上次记录
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		for (Map<String, Object> map : list) {
			// 统计
			for (int i = 1; i <= num; i++) {
				ArrayList<String> list2 = bigMap.get("num" + i);
				Integer a = aMap.get("num" + i);
				if (a == null) {
					// a定义默认变量
					a = 4;
				}
				if (list2 == null) {
					list2 = new ArrayList<String>();
					list2.add("");
				}
				Integer haoma = (Integer) map.get("num" + i);
				// 如果大于55列就停下来
				if (aMap.get("continue" + i) != null) {
					continue;
				}
				if (haoma == 1 || haoma == 5 || haoma == 9 || haoma == 13 || haoma == 17) {
					// 如果上次为东或默认
					if (a == 0 || a == 4) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "东");
					} else {
						// 如果上次不为东或默认
						// 如果大于55列就停下来
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("东");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "dong", lottId);
					aMap.put("num" + i, 0);
				} else if (haoma == 2 || haoma == 6 || haoma == 10 || haoma == 14 || haoma == 18) {
					// 如果上次为南或默认
					if (a == 1 || a == 4) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "南");
					} else {
						// 如果上次不为南或默认
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("南");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "nan", lottId);
					aMap.put("num" + i, 1);
				} else if (haoma == 3 || haoma == 7 || haoma == 11 || haoma == 15 || haoma == 19) {
					// 如果上次为西或默认
					if (a == 2 || a == 4) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "西");
					} else {
						// 如果上次不为西或默认
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("西");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "xi", lottId);
					aMap.put("num" + i, 2);
				} else if (haoma == 4 || haoma == 8 || haoma == 12 || haoma == 16 || haoma == 20) {
					// 如果上次为发或默认
					if (a == 3 || a == 4) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "北");
					} else {
						// 如果上次不为发或默认
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("北");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "bei", lottId);
					aMap.put("num" + i, 3);
				}
				bigMap.put("num" + i, list2);
			}
		}
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		JSONObject resobj = new JSONObject(new LinkedHashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.dnxblz, resobj.toJSONString());
	}

	/**
	 * @desc 尾数大小路珠 广东快乐十分 幸运农场
	 * @author xg
	 * @author 2018年7月02日
	 */
	public void getWsdxlz(String lottId) {
		// 这里就跳过
		if (!(lottId.equals(LotteryConstant.GDKLSF) || lottId.equals(LotteryConstant.XYNC))) {
			return;
		}
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
		Map<String, ArrayList<String>> bigMap = new HashMap<String, ArrayList<String>>();
		// 变量 0小 1大 2默认 用来记录上次记录
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		for (Map<String, Object> map : list) {
			// 总和
			int total = 0;
			// 统计大小
			for (int i = 1; i <= num; i++) {
				ArrayList<String> list2 = bigMap.get("num" + i);
				Integer a = aMap.get("num" + i);
				if (a == null) {
					// a定义默认变量
					a = 2;
				}
				if (list2 == null) {
					list2 = new ArrayList<String>();
					list2.add("");
				}
				Integer haoma = (Integer) map.get("num" + i);
				total += haoma;
				haoma = haoma % 10;
				// 如果大于55列就停下来
				if (aMap.get("continue" + i) != null) {
					continue;
				}
				out: if (haoma >= 5) {
					// 如果上次为大或默认
					if (a == 1 || a == 2) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "大");
					} else {
						// 如果上次不为大或默认
						// 如果大于55列就停下来
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							break out;
						}
						list2.add("大");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "da", lottId);
					aMap.put("num" + i, 1);
				} else {
					// 如果上次为小或默认
					if (a == 0 || a == 2) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "小");
					} else {
						// 如果上次不为小或默认
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							break out;
						}
						list2.add("小");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "xiao", lottId);
					aMap.put("num" + i, 0);
				}
				bigMap.put("num" + i, list2);
			}

			// 统计尾数总和
			ArrayList<String> list3 = bigMap.get("total");
			if (list3 == null) {
				list3 = new ArrayList<String>();
				list3.add("");
			}
			// 0为小 1为大 定义默认
			Integer b = aMap.get("b");
			if (b == null) {
				b = 2;
			}
			int d = total % 10;
			if (aMap.get("c") != null) {
				continue;
			}
			if (d >= 5) {
				if (b == 1 || b == 2) {
					String string = list3.get(list3.size() - 1);
					list3.set(list3.size() - 1, string + "大");
				} else {
					if (list3.size() >= 55) {
						// 列已经满标记
						aMap.put("c", 1);
						continue;
					}
					list3.add("大");
				}
				LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "da", lottId);
				aMap.put("b", 1);
			} else {
				if (b == 0 || b == 2) {
					String string = list3.get(list3.size() - 1);
					list3.set(list3.size() - 1, string + "小");
				} else {
					if (list3.size() >= 55) {
						// 列已经满标记
						aMap.put("c", 1);
						continue;
					}
					list3.add("小");
				}
				LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "xiao", lottId);
				aMap.put("b", 0);
			}
			bigMap.put("total", list3);
		}
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		JSONObject resobj = new JSONObject(new LinkedHashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.wsdxlz, resobj.toJSONString());
	}

	/**
	 * @desc 合数单双路珠 广东快乐十分 幸运农场
	 * @author xg
	 * @author 2018年7月02日
	 */
	public void getHsdslz(String lottId) {
		// 这里就跳过
		if (!(lottId.equals(LotteryConstant.GDKLSF) || lottId.equals(LotteryConstant.XYNC))) {
			return;
		}
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
		Map<String, ArrayList<String>> bigMap = new HashMap<String, ArrayList<String>>();
		// 变量 0单 1双 3默认 用来记录上次记录
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		for (Map<String, Object> map : list) {
			// 统计单双
			for (int i = 1; i <= num; i++) {
				ArrayList<String> list2 = bigMap.get("num" + i);
				Integer a = aMap.get("num" + i);
				if (a == null) {
					// a定义默认变量
					a = 2;
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
				if (haoma.toString().length() == 2) {
					String[] array = haoma.toString().split("");
					haoma = Integer.parseInt(array[0]) + Integer.parseInt(array[1]);
				}
				if (haoma % 2 == 1) {
					// 如果上次为中或默认
					if (a == 0 || a == 2) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "单");
					} else {
						// 如果上次不为中或默认
						// 如果大于55列就停下来
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("单");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "dan", lottId);
					aMap.put("num" + i, 0);
				} else {
					// 如果上次为发或默认
					if (a == 1 || a == 2) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "双");
					} else {
						// 如果上次不为发或默认
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + i, 1);
							continue;
						}
						list2.add("双");
					}
					LotteryUtil.countNum("num" + i, bigMap, map.get("time").toString(), "shuang", lottId);
					aMap.put("num" + i, 1);
				}
				bigMap.put("num" + i, list2);
			}
		}
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		JSONObject resobj = new JSONObject(new LinkedHashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.hsdslz, resobj.toJSONString());
	}

	/**
	 * @desc 总和路珠 重庆时时彩 (圣地彩: 无数据) 广东11选5 (pc蛋蛋:无数据) 新疆时时彩 天津时时彩 江西11选5
	 * @author xg
	 * @author 2018年7月05日
	 */
	public void getZhlz(String lottId) {
		// 这里就跳过
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT) || lottId.equals(LotteryConstant.PCDD) || lottId.equals(LotteryConstant.SDC)
				|| lottId.equals(LotteryConstant.JSK3) || lottId.equals(LotteryConstant.KL8) || lottId.equals(LotteryConstant.XYNC)) {
			return;
		}
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
		HashMap<String, ArrayList<String>> bigMap = new HashMap<String, ArrayList<String>>();
		// 变量 0小 单 1大 双 2默认 用来记录上次记录
		HashMap<String, Integer> aMap = new HashMap<String, Integer>();
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

			} else if (aMap.get("continueXD") != null && aMap.get("continueDS") != null) {
				continue;
			}
			// 统计单双
			if (aMap.get("continueDS") == null) {
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
							continue;
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
							continue;
						}
						list3.add("双");
					}
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "shuang", lottId);
					aMap.put("ds", 1);
				}
				bigMap.put("ds", list3);
			} else if (aMap.get("continueXD") != null && aMap.get("continueDS") != null) {
				continue;
			}
		}
		// 统计默认为0
		LotteryUtil.initNum("num", bigMap, "he");
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		JSONObject resobj = new JSONObject(new LinkedHashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.zhlz, resobj.toJSONString());
	}

	/**
	 * @desc 号码路珠 重庆时时彩 广东快乐十分 幸运农场 (圣地彩 无数据) 广东11选5 (PC蛋蛋:无数据) 江苏快3 新疆时时彩 天津时时彩
	 *       江西11选5
	 * @author xg
	 * @author 2018年6月29日
	 */
	public void getHmlz(String lottId) {
		// 这里就跳过
		if (lottId.equals(LotteryConstant.PCDD) || lottId.equals(LotteryConstant.SDC) || lottId.equals(LotteryConstant.XYFT) || lottId.equals(LotteryConstant.BJSC)
				|| lottId.equals(LotteryConstant.KL8)) {
			return;
		}
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
		Map<String, ArrayList<String>> bigMap = new HashMap<String, ArrayList<String>>();
		// 变量 0为√ 1× 2默认 用来记录上次单双
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		int s = LotteryConstant.lotteryNumberRangeMap.get(lottId + "S");
		int b = LotteryConstant.lotteryNumberRangeMap.get(lottId + "B");
		// 统计这期未出现数字
		List<Integer> tList = new ArrayList<Integer>();
		// 统计所有号码
		for (int j = s; j <= b; j++) {
			tList.add(j);
		}
		for (Map<String, Object> map : list) {
			// 统计√×
			List<Integer> gList = new ArrayList<Integer>();
			List<Integer> oList = new ArrayList<Integer>();
			oList.addAll(tList);
			for (int i = 1; i <= num; i++) {
				gList.add(Integer.parseInt(map.get("num" + i).toString()));
			}
			HashSet<Integer> hashSet = new HashSet<Integer>(gList);
			gList.clear();
			gList.addAll(hashSet);
			oList.removeAll(gList);
			// 统计出现的号码
			for (int j = 0; j <= gList.size() - 1; j++) {
				if (aMap.get("continue" + gList.get(j)) == null) {
					Integer a = aMap.get("num" + gList.get(j));
					if (a == null) {
						a = 2;
					}
					ArrayList<String> list2 = bigMap.get("num" + gList.get(j).toString());
					if (list2 == null) {
						list2 = new ArrayList<String>();
						list2.add("");
					}
					if (a == 0 || a == 2) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "√");
					} else {
						// 如果上次不为中或默认
						// 如果大于55列就停下来
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + gList.get(j), 1);
							continue;
						}
						list2.add("√");
					}
					LotteryUtil.countNum("num" + gList.get(j), bigMap, map.get("time").toString(), "gou", lottId);
					aMap.put("num" + gList.get(j), 0);
					bigMap.put("num" + gList.get(j).toString(), list2);
				} else {
					continue;
				}
			}
			// 统计未出现的号码
			for (int j = 0; j <= oList.size() - 1; j++) {
				if (aMap.get("continue" + oList.get(j)) == null) {
					Integer a = aMap.get("num" + oList.get(j));
					if (a == null) {
						a = 2;
					}
					ArrayList<String> list2 = bigMap.get("num" + oList.get(j).toString());
					if (list2 == null) {
						list2 = new ArrayList<String>();
						list2.add("");
					}
					if (a == 1 || a == 2) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "×");
					} else {
						// 如果上次不为中或默认
						// 如果大于55列就停下来
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continue" + oList.get(j), 1);
							continue;
						}
						list2.add("×");
					}
					LotteryUtil.countNum("num" + oList.get(j), bigMap, map.get("time").toString(), "cha", lottId);
					aMap.put("num" + oList.get(j), 1);
					bigMap.put("num" + oList.get(j).toString(), list2);
				} else {
					continue;
				}
			}
		}
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		JSONObject resobj = new JSONObject(new LinkedHashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.hmlz, resobj.toJSONString());
	}

	/**
	 * @desc 彩票直播开奖
	 * @author xg
	 * @param lottId
	 * @author 2018年7月12日
	 */
	@SuppressWarnings("unchecked")
	public void lotteryVideo(String lottId) {
		// 这里跳过
		if ( lottId.equals(LotteryConstant.SDC) || lottId.equals(LotteryConstant.JSK3)) {
			return;
		}
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		Map<String, Object> map = lotteryProcessMapper.selectLotteryLive(tablename, numstr, LotteryUtil.countDayOfNow(lottId));
		if (map == null) {
			return;
		}
		String cPeriod = map.get("periods").toString();
		String cDrawTime = "";
		String cDrawNumbers = "";
		String cDrawDate = cPeriod;
		String nPrePeriod = cPeriod;
		String nPeriod = JSONObject.parseObject(redisservice.get(lottId + "_" + LotteryDict.zxkj)).getString("xqqs");
		String nPreDrawTime = "";
		String nNextPeriod = nPeriod;
		String nDrawDate = nPeriod;
		String nDrawTime = "";
		int nInterval = 0;
		int nAwardInterval = 0;
		int nDelayInterval = 0;
		int intervalMinute = 0;
		// 得到号码
		Integer num = LotteryConstant.lotteryNumMap.get(lottId);
		if (lottId.equals("kl8")) {
			num = 20;
		}
		for (int i = 1; i <= num; i++) {
			if (!lottId.equals(LotteryConstant.XYNC)) {
				cDrawNumbers += map.get("num" + i) + ",";
			} else {
				// 幸运农场十位补0
				cDrawNumbers += String.format("%02d", map.get("num" + i)) + ",";
			}
		}
		cDrawNumbers = cDrawNumbers.substring(0, cDrawNumbers.length() - 1);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cDrawTime = formatter.format(map.get("starttime"));
		nPreDrawTime = cDrawTime;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) map.get("starttime"));
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT) || lottId.equals(LotteryConstant.KL8) || lottId.equals(LotteryConstant.PCDD)) {
			intervalMinute = 5;
		} else if (lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.GDKLSF) || lottId.equals(LotteryConstant.XYNC) || lottId.equals(LotteryConstant.SDC)
				|| lottId.equals(LotteryConstant.GD11X5) || lottId.equals(LotteryConstant.JSK3) || lottId.equals(LotteryConstant.XJSSC) || lottId.equals(LotteryConstant.TJSSC)
				|| lottId.equals(LotteryConstant.JX11X5)) {
			intervalMinute = 10;
		}
		calendar.add(Calendar.MINUTE, intervalMinute);
		nDrawTime = formatter.format(calendar.getTime());

		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT) || lottId.equals(LotteryConstant.JSK3) || lottId.equals(LotteryConstant.JX11X5)
				|| lottId.equals(LotteryConstant.GD11X5)) {
			nDelayInterval = 30;
		} else if (lottId.equals(LotteryConstant.KL8) || lottId.equals(LotteryConstant.PCDD)) {
			nDelayInterval = 15;
		} else if (lottId.equals(LotteryConstant.XYNC)) {
			nDelayInterval = 40;
		} else if (lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.SDC) || lottId.equals(LotteryConstant.XJSSC) || lottId.equals(LotteryConstant.TJSSC)) {
			nDelayInterval = 25;
		} else if (lottId.equals(LotteryConstant.GDKLSF)) {
			nDelayInterval = 90;
		}

		HashMap<String, Object> aMap = new HashMap<String,Object>();
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT) || lottId.equals(LotteryConstant.PCDD)) {
			if(lottId.equals(LotteryConstant.XYFT)){
				cPeriod = cPeriod.substring(cPeriod.length() - 3,cPeriod.length());
			}
			HashMap<String, Object> currentMap = new HashMap<String,Object>();
			HashMap<String, Object> nextMap = new HashMap<String,Object>();
			aMap.put("Current", currentMap);
			aMap.put("Next", nextMap);
			aMap.put("FirstPeriod", map.get("firstPeriods"));
			currentMap.put("Period", Long.parseLong(cPeriod));
			currentMap.put("DrawTime", cDrawTime);
			currentMap.put("DrawNumbers", cDrawNumbers);
			nextMap.put("PrePeriod", Long.parseLong(nPrePeriod));
			nextMap.put("PreDrawTime", nPreDrawTime);
			nextMap.put("Period",Long.parseLong(nPeriod));
			nextMap.put("NextPeriod", Long.parseLong(nNextPeriod));
			nextMap.put("DrawTime", nDrawTime);
			nextMap.put("Interval", nInterval);
			nextMap.put("AwardInterval", nAwardInterval);
			nextMap.put("DelayInterval", nDelayInterval);
		} else if (lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.GDKLSF) || lottId.equals(LotteryConstant.XYNC) || lottId.equals(LotteryConstant.SDC)
				|| lottId.equals(LotteryConstant.GD11X5) || lottId.equals(LotteryConstant.JSK3) || lottId.equals(LotteryConstant.XJSSC) || lottId.equals(LotteryConstant.TJSSC)
				|| lottId.equals(LotteryConstant.JX11X5)) {
			if (!lottId.equals(LotteryConstant.XYNC)) {
				cPeriod = cPeriod.substring(cPeriod.length() - 2);
				nPrePeriod = cPeriod;
				nPeriod = nPeriod.substring(nPeriod.length() - 2);
				nNextPeriod = nPeriod;
				HashMap<String, Object> currentMap = new HashMap<String,Object>();
				HashMap<String, Object> nextMap = new HashMap<String,Object>();
				aMap.put("Current", currentMap);
				aMap.put("Next", nextMap);
				aMap.put("FirstPeriod", map.get("firstPeriods"));
				currentMap.put("Period", Long.parseLong(cPeriod));
				currentMap.put("DrawDate", cDrawDate);
				currentMap.put("DrawTime", cDrawTime);
				currentMap.put("DrawNumbers", cDrawNumbers);
				nextMap.put("PrePeriod", Long.parseLong(nPrePeriod));
				nextMap.put("PreDrawTime", nPreDrawTime);
				nextMap.put("Period",Long.parseLong(nPeriod));
				nextMap.put("NextPeriod", Long.parseLong(nNextPeriod));
				nextMap.put("DrawDate", nDrawDate);
				nextMap.put("DrawTime", nDrawTime);
				nextMap.put("Interval", nInterval);
				nextMap.put("AwardInterval", nAwardInterval);
				nextMap.put("DelayInterval", nDelayInterval);
			} else {
				cPeriod = cPeriod.substring(0, cPeriod.length() - 3) + cPeriod.substring(cPeriod.length() - 2, cPeriod.length());
				nPrePeriod = cPeriod;
				nNextPeriod = nPeriod;
				nPeriod = nPeriod.substring(0, cPeriod.length() - 3) + nPeriod.substring(nPeriod.length() - 2, nPeriod.length());
				nNextPeriod = nPeriod;
				cDrawDate = cPeriod;
				nDrawDate = nPeriod;
				
				HashMap<String, Object> currentMap = new HashMap<String,Object>();
				HashMap<String, Object> nextMap = new HashMap<String,Object>();
				aMap.put("Current", currentMap);
				aMap.put("Next", nextMap);
				currentMap.put("Period", Long.parseLong(cPeriod));
				currentMap.put("DrawDate", cDrawDate);
				currentMap.put("DrawTime", cDrawNumbers);
				currentMap.put("DrawNumbers", cDrawNumbers);
				nextMap.put("PrePeriod", Long.parseLong(nPrePeriod));
				nextMap.put("PreDrawTime", nPreDrawTime);
				nextMap.put("Period",Long.parseLong(nPeriod));
				nextMap.put("NextPeriod", Long.parseLong(nNextPeriod));
				nextMap.put("DrawDate", nDrawDate);
				nextMap.put("DrawTime", nDrawTime);
				nextMap.put("Interval", nInterval);
				nextMap.put("AwardInterval", nAwardInterval);
				nextMap.put("DelayInterval", nDelayInterval);
			}
		} else if (lottId.equals(LotteryConstant.KL8)) {
			String pan = map.get("num21").toString();
			if (pan.equals("0")) {
				pan = "1";
			}
			HashMap<String, Object> currentMap = new HashMap<String,Object>();
			HashMap<String, Object> nextMap = new HashMap<String,Object>();
			aMap.put("Current", currentMap);
			aMap.put("Next", nextMap);
			aMap.put("FirstPeriod", map.get("firstPeriods"));
			currentMap.put("Period", Long.parseLong(cPeriod));
			currentMap.put("DrawTime", cDrawTime);
			currentMap.put("DrawNumbers", cDrawNumbers);
			currentMap.put("Pan", pan);
			nextMap.put("PrePeriod", Long.parseLong(nPrePeriod));
			nextMap.put("PreDrawTime", nPreDrawTime);
			nextMap.put("Period",Long.parseLong(nPeriod));
			nextMap.put("NextPeriod", Long.parseLong(nNextPeriod));
			nextMap.put("DrawTime", nDrawTime);
			nextMap.put("Interval", nInterval);
			nextMap.put("AwardInterval", nAwardInterval);
			nextMap.put("DelayInterval", nDelayInterval);
		}
		redisservice.setHashMap(LotteryDict.video, lottId, JSONObject.toJSON(aMap).toString());

		// h5数据
		HashMap<String, Object> bMap = new HashMap<String,Object>();
		if (lottId.equals("bjsc")) {
			HashMap<String, Object> currentMap = new HashMap<String,Object>();
			HashMap<String, Object> nextMap = new HashMap<String,Object>();
			bMap.put("current", currentMap);
			bMap.put("next", nextMap);
			bMap.put("time", 1534396981014L);
			bMap.put("apiVersion", 1);
			currentMap.put("periodNumber", Long.parseLong(cPeriod));
			currentMap.put("fullPeriodNumber", Long.parseLong(cPeriod));
			currentMap.put("awardTime", cDrawTime);
			currentMap.put("awardNumbers", cDrawNumbers);
			currentMap.put("awardTimeInterval",0);
			currentMap.put("delayTimeInterval", 0);
			currentMap.put("pan", 0);
			currentMap.put("isEnd", null);
			currentMap.put("nextMinuteInterval", null);
			nextMap.put("awardTime",nDrawTime);
			nextMap.put("periodNumber", Long.parseLong(nPeriod));
			nextMap.put("fullPeriodNumber", Long.parseLong(nPeriod));
			nextMap.put("periodNumberStr", Long.parseLong(nPeriod));
			nextMap.put("awardTimeInterval", 238985);
			nextMap.put("awardNumbers", null);
			nextMap.put("delayTimeInterval", 20);
			nextMap.put("pan", null);
			nextMap.put("isEnd", null);
			nextMap.put("nextMinuteInterval", null);
			redisservice.setHashMap(LotteryDict.video, lottId + "_Mobile", JSONObject.toJSON(bMap).toString());
		} else if (lottId.equals("cqssc")) {
			HashMap<String, Object> currentMap = new HashMap<String,Object>();
			HashMap<String, Object> nextMap = new HashMap<String,Object>();
			bMap.put("current", currentMap);
			bMap.put("next", nextMap);
			bMap.put("time", 1534396981014L);
			currentMap.put("periodNumber", Long.parseLong(cPeriod));
			currentMap.put("periodDate", Long.parseLong(cPeriod));
			currentMap.put("awardTime", cDrawTime);
			currentMap.put("awardNumbers", cDrawNumbers);
			nextMap.put("periodNumber", Long.parseLong(nPeriod));
			nextMap.put("periodDate",Long.parseLong(nPeriod));
			nextMap.put("awardTime", nDrawTime);
			nextMap.put("awardTimeInterval", 398771);
			nextMap.put("delayTimeInterval", 25);
			redisservice.setHashMap(LotteryDict.video, lottId + "_Mobile", JSONObject.toJSON(bMap).toString());
		} else if (lottId.equals("xyft")) {
			HashMap<String, Object> currentMap = new HashMap<String,Object>();
			HashMap<String, Object> nextMap = new HashMap<String,Object>();
			bMap.put("current", currentMap);
			bMap.put("next", nextMap);
			bMap.put("time", 1534396981014L);
			currentMap.put("periodNumber", Long.parseLong(cPeriod));
			currentMap.put("awardTime", cDrawTime);
			currentMap.put("awardNumbers", cDrawNumbers);
			nextMap.put("periodNumber", Long.parseLong(nPeriod));
			nextMap.put("awardTime",nDrawTime);
			nextMap.put("awardTimeInterval", 238985);
			nextMap.put("delayTimeInterval", 15);
			redisservice.setHashMap(LotteryDict.video, lottId + "_Mobile", JSONObject.toJSON(bMap).toString());
		} else if (lottId.equals("xync")) {
			HashMap<String, Object> currentMap = new HashMap<String,Object>();
			HashMap<String, Object> nextMap = new HashMap<String,Object>();
			bMap.put("current", currentMap);
			bMap.put("next", nextMap);
			bMap.put("time", 1534396981014L);
			currentMap.put("periodNumber", Long.parseLong(cPeriod));
			currentMap.put("period", Long.parseLong(cPeriod));
			currentMap.put("awardTime", cDrawTime);
			currentMap.put("awardNumbers", cDrawNumbers);
			nextMap.put("periodNumber", Long.parseLong(nPeriod));
			nextMap.put("period",nDrawTime);
			nextMap.put("awardTime", 25654);
			nextMap.put("awardTimeInterval", 25654);
			nextMap.put("delayTimeInterval", 40);
			redisservice.setHashMap(LotteryDict.video, lottId + "_Mobile", JSONObject.toJSON(bMap).toString());
		}

		// icpapi/GetLotteryTime 接口数据
		// 存进去更名
		if (lottId.equals(LotteryConstant.BJSC)) {
			lottId = "pk10";
		} else if (lottId.equals(LotteryConstant.CQSSC)) {
			lottId = "shishicai";
		} else if (lottId.equals(LotteryConstant.GDKLSF)) {
			lottId = "gdkl10";
		}
		Object str = redisservice.getHashMap(LotteryDict.video, "GetLotteryTime");
		if (str == null) {
			redisservice.setHashMap(LotteryDict.video, "GetLotteryTime", new HashMap<String, Object>().toString());
			str = redisservice.getHashMap(LotteryDict.video, "GetLotteryTime");
		}
		Map gMap = (Map) JSONObject.parse(str.toString());
		gMap.remove(lottId);
		gMap.put(lottId, aMap);
		JSONObject resobj = new JSONObject(new HashMap<String, Object>(gMap));
		redisservice.setHashMap(LotteryDict.video, "GetLotteryTime", resobj.toJSONString());
	}

	/**
	 * 
	 * @desc 返回每个彩种限制条数的期数
	 * @author abo
	 * @date 2018年8月10日 下午3:28:47
	 */
	public void makeLimitPeriods(String lottId) {
		List<String> list = this.lotteryProcessMapper.findPeriodsByOrder(LotteryConstant.tableNameMap.get(lottId), LotteryConstant.limitListSize);
		redisservice.setHashMap(LotteryDict.limitperiods, lottId, list.toString());
	}

	/**
	 * 
	 * @desc 返回每个彩种每天的期数集合
	 * @author abo
	 * @param date2
	 * @date 2018年8月10日 下午3:32:05
	 */
	public void makePeriodsByDate(String lottId, String date) {
		if (StringUtils.isBlank(date)) {
			date = LotteryUtil.countDayOfNow(lottId);
		}
		List<String> list = this.lotteryProcessMapper.findPeriodsByDay(LotteryConstant.tableNameMap.get(lottId), date);
		redisservice.setHashMap(lottId + "_" + LotteryDict.limitperiodsbydate, date, list.toString());
	}
	
	
	public static void main(String[] args) {
		String substring = "20180904011".substring("20180904011".length() - 2);
		System.out.println(substring);
	}

}
