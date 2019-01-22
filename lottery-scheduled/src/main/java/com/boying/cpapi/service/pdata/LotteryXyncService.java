package com.boying.cpapi.service.pdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;

import io.lottery.common.config.LotteryDict;

/**
 * @desc 幸运农场业务处理
 * @author xg
 * @author 2018年7月2日
 */
@Service
public class LotteryXyncService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * 幸运农场业务处理
	 * 
	 * @param lottId
	 */
	public void execute(String lottId) {
		// 龙虎路珠
		this.getLhlz(lottId);
		// 开奖记录
		this.getKjjl(lottId, null);
		// 总和路珠
		this.getZhlz(lottId);

	}

	/**
	 * @desc 龙虎路珠
	 * @author xg
	 * @author 2018年7月02日
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
	 * 
	 * @desc 幸运农场开奖记录
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
		// lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 1);
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
			// 总和
			int sum = num1 + num2 + num3 + num4 + num5 + num6 + num7 + num8;
			// 总和大小
			String zhDx = LotteryConstant.HE;
			if (sum > 84) {
				zhDx = LotteryConstant.DA;
			} else if (sum < 84) {
				zhDx = LotteryConstant.XIAO;
			}
			// 尾大尾小
			String wDx = sum % 10 > 4 ? LotteryConstant.WDA : LotteryConstant.WXIAO;
			// 总和单双
			String zhDs = sum % 2 == 0 ? LotteryConstant.SHUANG : LotteryConstant.DAN;
			// 龙虎
			String lh1 = "";
			if (num1 > num8) {
				lh1 = LotteryConstant.LONG;
			} else if (num1 < num8) {
				lh1 = LotteryConstant.HU;
			}

			Map<String, Object> resmap = new HashMap<String, Object>();
			List<Object> numberlist = new ArrayList<Object>();
			for (String position : numStr.split(",")) {
				numberlist.add(m.get(position));
			}
			resmap.put("rank", numberlist);
			resmap.put("periods", m.get("periods"));
			resmap.put("starttime", m.get("starttime"));
			resmap.put("lh1", lh1);
			resmap.put("gyhSum", sum);
			resmap.put("zhDx", zhDx);
			resmap.put("zhDs", zhDs);
			resmap.put("wDx", wDx);
			JSONObject resobj = new JSONObject(resmap);
			redisservice.setHashMap(lottId + "_" + LotteryDict.kjjl, resmap.get("periods"), resobj.toJSONString());
		}
	}

	/**
	 * @desc 总和路珠
	 * @author xg
	 * @author 2018年7月06日
	 */
	public void getZhlz(String lottId) {
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
						if (list2.size() >= LotteryConstant.LZ_COLUMN_NUM) {
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
						if (list2.size() >= LotteryConstant.LZ_COLUMN_NUM) {
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
						if (list2.size() >= LotteryConstant.LZ_COLUMN_NUM) {
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

			} else if (aMap.get("continueXD") != null && aMap.get("continueDS") != null && aMap.get("continueZHWS") != null) {
				continue;
			}
			// 统计单双
			outB: if (aMap.get("continueDS") == null) {
				List<String> list3 = (List<String>) bigMap.get("ds");
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
						if (list3.size() >= LotteryConstant.LZ_COLUMN_NUM) {
							// 列已经满标记
							aMap.put("continueDS", 1);
							break outB;
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
						if (list3.size() >= LotteryConstant.LZ_COLUMN_NUM) {
							// 列已经满标记
							aMap.put("continueDS", 1);
							break outB;
						}
						list3.add("双");
					}
					LotteryUtil.countNum("num", bigMap, map.get("time").toString(), "shuang", lottId);
					aMap.put("ds", 1);
				}
				bigMap.put("ds", (ArrayList<String>) list3);
			} else if (aMap.get("continueXD") != null && aMap.get("continueDS") != null && aMap.get("continueZHWS") != null) {
				continue;
			}

			// 统计总和尾数
			if (aMap.get("continueZHWS") == null) {
				List<String> list4 = (List<String>) bigMap.get("zhwsdx");
				if (list4 == null) {
					list4 = new ArrayList<String>();
					list4.add("");
				}
				// 0为小 1为大 2为和 3定义默认
				Integer zhwsdx = aMap.get("zhwsdx");
				if (zhwsdx == null) {
					zhwsdx = 2;
				}
				int p = total % 10;
//				System.out.println(p);
				if (p <= 4) {
					if (zhwsdx == 0 || zhwsdx == 2) {
						String string = list4.get(list4.size() - 1);
						list4.set(list4.size() - 1, string + "小");
					} else {
						if (list4.size() >= LotteryConstant.LZ_COLUMN_NUM) {
							// 列已经满标记
							aMap.put("continueZHWS", 1);
							continue;
						}
						list4.add("小");
					}
					LotteryUtil.countNum("numzhws", bigMap, map.get("time").toString(), "xiao", lottId);
					aMap.put("zhwsdx", 0);
				} else {
					if (zhwsdx == 1 || zhwsdx == 2) {
						String string = list4.get(list4.size() - 1);
						list4.set(list4.size() - 1, string + "大");
					} else {
						if (list4.size() >= LotteryConstant.LZ_COLUMN_NUM) {
							// 列已经满标记
							aMap.put("continueZHWS", 1);
							continue;
						}
						list4.add("大");
					}
					LotteryUtil.countNum("numzhws", bigMap, map.get("time").toString(), "da", lottId);
					aMap.put("zhwsdx", 1);
				}
				bigMap.put("zhwsdx", (ArrayList<String>) list4);
			} else if (aMap.get("continueXD") != null && aMap.get("continueDS") != null && aMap.get("continueZHWS") != null) {
				continue;
			}
		}
		// 反转list顺序
		for (Entry<String, ArrayList<String>> entry : bigMap.entrySet()) {
			if (entry.getValue() instanceof List) {
				Collections.reverse(entry.getValue());
			}
		}
		JSONObject json = new JSONObject(new HashMap<String, Object>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.zhlz, json.toString());
	}
}
