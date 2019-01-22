package com.boying.cpapi.service.pdata;

import io.lottery.common.config.LotteryDict;
import io.lottery.common.utils.ExceptionUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;
import com.boying.cpapi.util.Yilou.K3YilouHelp;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @desc 江苏快3
 * @author abo
 * @date 2018年7月3日 下午3:09:48
 *
 */
@Slf4j
@Service
public class LotteryJsk3Service {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * 江苏快3业务处理
	 * 
	 * @author ms
	 * @param lottId
	 */
	public void execute(String lottId) {
		// 江苏快3开奖记录
		this.getKjjl(lottId, null);
		// 江苏快3开奖记录
		this.getZhlz();
		// 江苏快三 遗漏数据
		this.getK3Yilou(lottId);
		// 开奖视频
		this.lotteryVideo();
	}

	/**
	 * 
	 * @desc 江苏快3开奖记录
	 * @author abo
	 * @param date
	 * @date 2018年6月27日 下午3:45:00
	 */
	public void getKjjl(String lottId, String date) {
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 查询数据库数据
		// List<Map<String,Object>> list =
		// lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 100);
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
			String zhdx = "";
			// 如果是豹子就是通吃，不然就是判断大小
			if (num1 == num2 && num1 == num3) {
				zhdx = LotteryConstant.TC;
			} else {
				if (sum > 10) {
					zhdx = LotteryConstant.DA;
				} else if (sum < 11) {
					zhdx = LotteryConstant.XIAO;
				}
			}
			Map<String, Object> resmap = new HashMap<String, Object>();
			List<Object> numberlist = new ArrayList<Object>();
			for (String position : numStr.split(",")) {
				numberlist.add(m.get(position));
			}
			resmap.put("rank", numberlist);
			// resmap.put("periods",m.get("periods").toString().substring(2));
			resmap.put("periods", m.get("periods").toString());
			resmap.put("starttime", m.get("starttime"));
			resmap.put("zhSum", sum);
			resmap.put("zhDx", zhdx);
			JSONObject resobj = new JSONObject(resmap);
			// System.out.println(resobj);
			redisservice.setHashMap(lottId + "_" + LotteryDict.kjjl, resmap.get("periods").toString(), resobj.toJSONString());
		}
	}

	/**
	 * @desc 总和路珠 江苏快3
	 * @author xg
	 * @author 2018年7月05日
	 */
	public void getZhlz() {
		String lottId = LotteryConstant.JSK3;
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
		// 变量 0小 单 1大 双 2默认 用来记录上次记录
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		for (Map<String, Object> map : list) {
			// 总和
			int total = 0;
			for (int i = 1; i <= num; i++) {
				Integer haoma = (Integer) map.get("num" + i);
				total += haoma;
			}
			// 统计大小
			out: if (aMap.get("continueXD") == null) {
				List<String> list2 = (List<String>) bigMap.get("xd");
				if (list2 == null) {
					list2 = new ArrayList<String>();
					list2.add("");
				}
				// 0为小 1为大 2通吃 3定义默认
				Integer xd = aMap.get("xd");
				if (xd == null) {
					xd = 3;
				}
				// 如果为通吃
				if (Integer.parseInt(map.get("num1").toString()) == Integer.parseInt(map.get("num2").toString())
						&& Integer.parseInt(map.get("num2").toString()) == Integer.parseInt(map.get("num3").toString())) {
					if (xd == 2 || xd == 3) {
						String string = list2.get(list2.size() - 1);
						list2.set(list2.size() - 1, string + "通吃");
					} else {
						if (list2.size() >= 55) {
							// 列已经满标记
							aMap.put("continueXD", 1);
							break out;
						}
						list2.add("通吃");
					}
					aMap.put("xd", 2);
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "xdtongchi", lottId);
					break out;
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
				bigMap.put("xd", (ArrayList<String>) list2);
			} else if (aMap.get("continueXD") != null && aMap.get("continueDS") != null) {
				continue;
			}
			// 统计单双
			if (aMap.get("continueDS") == null) {
				List<String> list3 = (List<String>) bigMap.get("ds");
				if (list3 == null) {
					list3 = new ArrayList<String>();
					list3.add("");
				}
				// 0为单 1为双 2为通吃 3定义默认
				Integer ds = aMap.get("ds");
				if (ds == null) {
					ds = 3;
				}
				// 如果为通吃
				if (Integer.parseInt(map.get("num1").toString()) == Integer.parseInt(map.get("num2").toString())
						&& Integer.parseInt(map.get("num2").toString()) == Integer.parseInt(map.get("num3").toString())) {
					if (ds == 2 || ds == 3) {
						String string = list3.get(list3.size() - 1);
						list3.set(list3.size() - 1, string + "通吃");
					} else {
						if (list3.size() >= 55) {
							// 列已经满标记
							aMap.put("continueDS", 1);
							continue;
						}
						list3.add("通吃");
					}
					aMap.put("ds", 2);
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "dstongchi", lottId);
					continue;
				}
				if (total % 2 == 1) {
					if (ds == 0 || ds == 3) {
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
					aMap.put("ds", 0);
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "dan", lottId);
				} else {
					if (ds == 1 || ds == 3) {
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
					aMap.put("ds", 1);
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "shuang", lottId);
				}
				bigMap.put("ds", (ArrayList<String>) list3);
			} else if (aMap.get("continueXD") != null && aMap.get("continueDS") != null) {
				continue;
			}
		}
		// 统计默认为0
		LotteryUtil.initNum("num", bigMap, "xdtongchi");
		LotteryUtil.initNum("num", bigMap, "dstongchi");
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
//		System.out.println(bigMap);
		JSONObject json = new JSONObject(new HashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.zhlz, json.toString());
	}

	/**
	 * 江苏快三 遗漏数据
	 */
	public void getK3Yilou(String lottId) {
		// 今天
		Date td = new Date();
		String currentDate = DateUtil.date2String(td, DateUtil.PATTERN_DATE);
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
		// 总和大小遗漏数据
		JSONObject zhdaxiaoobj = K3YilouHelp.k3zhdaxiao(clist, wlist, mlist);
		// 总和单双
		JSONObject zhdanshuangobj = K3YilouHelp.k3zhdanshuang(clist, wlist, mlist);
		// 总和点数
		JSONObject zhdianshuobj = K3YilouHelp.k3zhdianshu(clist, wlist, mlist);
		// 短牌
		JSONObject duanpaiobj = K3YilouHelp.k3duanpai(clist, wlist, mlist);
		// 长牌
		JSONObject changpaiobj = K3YilouHelp.k3changpai(clist, wlist, mlist);
		// 三军
		JSONObject sanjunobj = K3YilouHelp.k3sanjun(clist, wlist, mlist);

		JSONObject resobj = new JSONObject();
		resobj.put("zhdaxiao", zhdaxiaoobj);
		resobj.put("zhdanshuang", zhdanshuangobj);
		resobj.put("zhdianshu", zhdianshuobj);
		resobj.put("duanpai", duanpaiobj);
		resobj.put("changpai", changpaiobj);
		resobj.put("sanjun", sanjunobj);

		// 江苏快三 号码遗漏数据
		redisservice.set(lottId + "_" + LotteryDict.hmyl, resobj.toString());

	}

	/**
	 * @desc 彩票直播开奖
	 * @author xg
	 * @param lottId
	 * @author 2018年7月14日
	 */
	public void lotteryVideo() {
		String lottId = LotteryConstant.JSK3;
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
		Long nPrePeriod = cPeriod;
		Long nPeriod = Long.parseLong(JSONObject.parseObject(redisservice.get(lottId + "_" + LotteryDict.zxkj)).getString("xqqs").toString());
		String nPreDrawTime = "";
		Long nNextPeriod = nPeriod;
		String nDrawDate = nPeriod.toString();
		String nDrawTime = "";
		int nInterval = 0;
		int nAwardInterval = 0;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cDrawTime = formatter.format(map.get("starttime"));

		for (int i = 1; i <= LotteryConstant.lotteryNumMap.get(lottId); i++) {
			cDrawNumbers += map.get("num" + i) + ",";
		}
		cDrawNumbers = cDrawNumbers.substring(0, cDrawNumbers.length() - 1);
		
		HashMap<String, Object> aMap = new HashMap<String,Object>();
		HashMap<String, Object> currentMap = new HashMap<String,Object>();
		HashMap<String, Object> nextMap = new HashMap<String,Object>();
		aMap.put("Current", currentMap);
		aMap.put("Next", nextMap);
		currentMap.put("Period", cPeriod);
		currentMap.put("DrawDate", cDrawDate);
		currentMap.put("DrawTime", cDrawTime);
		currentMap.put("DrawNumbers", cDrawNumbers);
		nextMap.put("PrePeriod", nPrePeriod);
		nextMap.put("PreDrawTime", nPreDrawTime);
		nextMap.put("Period",nPeriod);
		nextMap.put("NextPeriod", nNextPeriod);
		nextMap.put("DrawTime", nDrawTime);
		nextMap.put("Interval", nInterval);
		nextMap.put("AwardInterval", nAwardInterval);
		nextMap.put("DelayInterval", 30);
		/*String a = "{\"Current\":{\"Period\":\"" + cPeriod + "\",\"DrawDate\":\"" + cDrawDate + "\",\"DrawTime\":\"" + cDrawTime + "\",\"DrawNumbers\":\"" + cDrawNumbers
				+ "\"},\"Next\":{\"PrePeriod\":\"" + nPrePeriod + "\",\"PreDrawTime\":\"" + nPreDrawTime + "\",\"Period\":\"" + nPeriod + "\",\"NextPeriod\":\"" + nNextPeriod
				+ "\",\"DrawDate\":\"" + nDrawDate + "\",\"DrawTime\":\"" + nDrawTime + "\",\"Interval\":" + nInterval + ",\"AwardInterval\":" + nAwardInterval
				+ ",\"DelayInterval\":30}}";*/

		cPeriod = Long.parseLong(cPeriod.toString().substring(cPeriod.toString().length() - 2));
		nPrePeriod = cPeriod;
		nPeriod = Long.parseLong(nPeriod.toString().substring(nPeriod.toString().length() - 2));
		nNextPeriod = nPeriod;
		HashMap<String, Object> bMap = new HashMap<String,Object>();
		HashMap<String, Object> currentMapB = new HashMap<String,Object>();
		HashMap<String, Object> nextMapB = new HashMap<String,Object>();
		bMap.put("current", currentMapB);
		bMap.put("next", nextMapB);
		bMap.put("time", 1534396981014L);
		currentMapB.put("periodNumber", cDrawDate);
		currentMapB.put("awardTime", cDrawTime);
		currentMapB.put("awardNumbers", cDrawNumbers);
		nextMapB.put("periodNumber", nDrawDate);
		nextMapB.put("awardTime",nPreDrawTime);
		nextMapB.put("awardTimeInterval", nAwardInterval);
		nextMapB.put("delayTimeInterval", 25);
		/*String b = "{\"time\":131760282081093857,\"current\":{\"periodNumber\":" + cDrawDate + ",\"awardTime\":\"" + cDrawTime + "\",\"awardNumbers\":\"" + cDrawNumbers
				+ "\"},\"next\":{\"periodNumber\":" + nDrawDate + ",\"awardTime\":\"" + nPreDrawTime + "\",\"awardTimeInterval\":" + nAwardInterval + ",\"delayTimeInterval\":25}}";*/

		HashMap<String, Object> hMap = new HashMap<String, Object>();
		// 查询数据库数据
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numstr, 10);
		for (Map<String, Object> m : list) {
			int num1 = Integer.parseInt(m.get("num1").toString());
			int num2 = Integer.parseInt(m.get("num2").toString());
			int num3 = Integer.parseInt(m.get("num3").toString());
			// 总和
			int sum = num1 + num2 + num3;
			String numbers = num1 + "," + num2 + "," + num3;
			// 总和大小
			String zhdx = "";
			// 如果是豹子就是通吃，不然就是判断大小
			if (num1 == num2 && num1 == num3) {
				zhdx = LotteryConstant.TC;
			} else {
				if (sum > 10) {
					zhdx = LotteryConstant.DA;
				} else if (sum < 11) {
					zhdx = LotteryConstant.XIAO;
				}
			}
			String periods = m.get("periods").toString();
			m.clear();
			m.put("total", String.valueOf(sum));
			m.put("result", String.valueOf(zhdx));
			m.put("numbers", numbers);
			m.put("periodNumber", periods.substring(2));

		}
		hMap.put("drawHistories", list);

		int num = LotteryConstant.lotteryNumMap.get(lottId);
		List<Map<String, Object>> list2 = lotteryProcessMapper.findLotteryByToday(tablename, numstr, LotteryUtil.countDayOfNow(lottId));
		if (list == null) {
			return;
		}
		// put进号码范围初始值为0
		Map<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
		for (int i = LotteryConstant.lotteryNumberRangeMap.get(lottId + "S"); i <= LotteryConstant.lotteryNumberRangeMap.get(lottId + "B"); i++) {
			Map<String, Object> hashMap1 = new HashMap<String, Object>();
			hashMap1.put("number", String.valueOf(i));
			hashMap1.put("count", "0");
			linkedHashMap.put("num" + i, hashMap1);
		}
		Map<String, Object> hashMap2 = new HashMap<String, Object>();
		hashMap2.put("number", "大");
		hashMap2.put("count", 0);
		Map<String, Object> hashMap3 = new HashMap<String, Object>();
		hashMap3.put("number", "小");
		hashMap3.put("count", "0");
		linkedHashMap.put("da", hashMap2);
		linkedHashMap.put("xiao", hashMap3);

		// 统计数量
		for (Map<String, Object> map2 : list2) {
			// 统计大小单双-0-9
			for (int i = 1; i <= num; i++) {
				// 得到号码
				Integer haoma = (Integer) map2.get("num" + i);
				for (int j = LotteryConstant.lotteryNumberRangeMap.get(lottId + "S"); j <= LotteryConstant.lotteryNumberRangeMap.get(lottId + "B"); j++) {
					if (haoma == j) {
						Map gmap = (Map) linkedHashMap.get("num" + j);
						gmap.put("count", String.valueOf(Integer.parseInt(gmap.get("count").toString()) + 1));
						gmap.put("num" + j, gmap);
					}
				}
				if (haoma >= LotteryConstant.lotteryBigBorderMap.get(lottId)) {
					Map gmap = (Map) linkedHashMap.get("da");
					gmap.put("count", String.valueOf(Integer.parseInt(gmap.get("count").toString()) + 1));
					gmap.put("da", gmap);
				} else {
					Map gmap = (Map) linkedHashMap.get("xiao");
					gmap.put("count", String.valueOf(Integer.parseInt(gmap.get("count").toString()) + 1));
					gmap.put("xiao", gmap);
				}
			}
		}
		List toList = mapToList(linkedHashMap);
		hMap.put("numberStatistics", toList);
		JSONObject json = new JSONObject(hMap);
		redisservice.setHashMap("video", lottId + "_a", JSONObject.toJSON(aMap).toString());
		redisservice.setHashMap("video", lottId + "_b", JSONObject.toJSON(bMap).toString());
		redisservice.setHashMap("video", lottId + "_c", json.toString());

		// icpapi/GetLotteryTime 接口数据
		try {
			Map<String, Map> gMap = new ObjectMapper().readValue(redisservice.getHashMap("video", "GetLotteryTime").toString(), Map.class);
			gMap.remove(lottId);
			gMap.put(lottId, aMap);
			JSONObject resobj = new JSONObject(new HashMap<String, Object>(gMap));
			redisservice.setHashMap("video", "GetLotteryTime", resobj.toString());
		} catch (IOException e) {
			e.printStackTrace();
			log.error(ExceptionUtil.getExceptionStackTrace(e));
		}

		Object str = redisservice.getHashMap("video", "GetLotteryTime");
		if (str == null) {
			redisservice.setHashMap("video", "GetLotteryTime", new HashMap<String, Object>().toString());
			str = redisservice.getHashMap("video", "GetLotteryTime");
		}
		Map gMap = (Map) JSONObject.parse(str.toString());
		gMap.remove(lottId);
		gMap.put(lottId, aMap);
		JSONObject resobj = new JSONObject(new HashMap<String, Object>(gMap));
		redisservice.setHashMap("video", "GetLotteryTime", resobj.toJSONString());
	}

	// map转list
	public List mapToList(Map map) {
		List listValue = new ArrayList();
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			HashMap<String, Object> a = (HashMap<String, Object>) map.get(key);
			listValue.add(a);
		}
		return listValue;
	}

}
