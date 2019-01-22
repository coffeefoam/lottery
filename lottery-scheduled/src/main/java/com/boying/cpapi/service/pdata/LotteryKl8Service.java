package com.boying.cpapi.service.pdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;

import io.lottery.common.config.LotteryDict;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @desc 快乐8业务处理
 * @author abo
 * @date 2018年7月2日 下午9:14:42
 *
 */
@Slf4j
@Service
public class LotteryKl8Service {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * @author ms
	 * @param lottId
	 */
	public void execute(String lottId) {
		// 快乐8开奖记录
		this.getKjjl(lottId, null);
		// 总和路珠
		this.getZhlz(lottId);
		// 快乐8 上中下路珠
		this.getSxlz(lottId);
		// 快乐8 奇偶路珠
		this.getJolz(lottId);
	}

	/**
	 * 
	 * @desc 快乐8开奖记录
	 * @author abo
	 * @param date
	 * @date 2018年6月27日 下午3:45:00
	 */
	public void getKjjl(String lottId, String date) {
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);
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
			// int num1 = Integer.parseInt(m.get("num1").toString());
			// int num2 = Integer.parseInt(m.get("num2").toString());
			// int num3 = Integer.parseInt(m.get("num3").toString());
			// int num4 = Integer.parseInt(m.get("num4").toString());
			// int num5 = Integer.parseInt(m.get("num5").toString());
			// int num6 = Integer.parseInt(m.get("num6").toString());
			// int num7 = Integer.parseInt(m.get("num7").toString());
			// int num8 = Integer.parseInt(m.get("num8").toString());
			// int num9 = Integer.parseInt(m.get("num9").toString());
			// int num10 = Integer.parseInt(m.get("num10").toString());
			// int num11 = Integer.parseInt(m.get("num11").toString());
			// int num12 = Integer.parseInt(m.get("num12").toString());
			// int num13 = Integer.parseInt(m.get("num13").toString());
			// int num14 = Integer.parseInt(m.get("num14").toString());
			// int num15 = Integer.parseInt(m.get("num15").toString());
			// int num16 = Integer.parseInt(m.get("num16").toString());
			// int num17 = Integer.parseInt(m.get("num17").toString());
			// int num18 = Integer.parseInt(m.get("num18").toString());
			// int num19 = Integer.parseInt(m.get("num19").toString());
			// int num20 = Integer.parseInt(m.get("num20").toString());

			// 飞盘
			int num21 = Integer.parseInt(m.get("num21").toString());

			// 总和
			// int sum = num1 + num2 + num3 + num4 + num5 + num6 + num7 + num8 +
			// num9 +
			// num10 + num11 + num12 + num13
			// + num14 + num15 + num16 + num17 + num18 + num19 + num20;
			int sum = 0;
			int sNum = 0;// 上的个数
			int xNum = 0;// 下的个数
			// 上下：上为小于40的数的个数大于大于40的数，中为上下的个数相等
			String sxStr = LotteryConstant.ZHONG;
			for (int i = 1; i < num; i++) {
				int temp = Integer.parseInt(m.get("num" + i).toString());
				sum += temp;
				if (temp > 40) {
					xNum++;
				} else {
					sNum++;
				}
			}
			if (sNum > xNum) {
				sxStr = LotteryConstant.SHANG;
			} else if (sNum < xNum) {
				sxStr = LotteryConstant.XIA;
			}
			// 总和大小
			String zhDx = LotteryConstant.HE;
			if (sum > 810) {
				zhDx = LotteryConstant.DA;
			} else if (sum < 810) {
				zhDx = LotteryConstant.XIAO;
			}
			// 总和单双
			String zhDs = sum % 2 == 0 ? LotteryConstant.SHUANG : LotteryConstant.DAN;
			// 把开出的20个号码的总和分在5个数段中，5个数段以金、木、水、火、土命名：金（210～695）、木（696～763）、水（764～855）、火（856～923）和土（924～1410）。
			String wxStr = "";
			if (sum >= 210 && sum <= 695) {
				wxStr = LotteryConstant.JIN;
			} else if (sum >= 696 && sum <= 763) {
				wxStr = LotteryConstant.MU;
			} else if (sum >= 764 && sum <= 855) {
				wxStr = LotteryConstant.SHUI;
			} else if (sum >= 856 && sum <= 923) {
				wxStr = LotteryConstant.HUO;
			} else if (sum >= 924 && sum <= 1410) {
				wxStr = LotteryConstant.TU;
			}

			Map<String, Object> resmap = new HashMap<String, Object>();
			List<Object> numberlist = new ArrayList<Object>();
			for (String position : numStr.split(",")) {
				numberlist.add(m.get(position));
			}
			resmap.put("rank", numberlist);
			resmap.put("periods", m.get("periods"));
			resmap.put("starttime", m.get("starttime"));
			resmap.put("zhSum", sum);// 总和
			resmap.put("zhDx", zhDx);// 总和大小
			resmap.put("zhDs", zhDs);// 总和单双
			resmap.put("sx", sxStr);// 上下
			resmap.put("fp", num21);// 飞盘号
			resmap.put("wx", wxStr);// 五行
			JSONObject resobj = new JSONObject(resmap);
			redisservice.setHashMap(lottId + "_" + LotteryDict.kjjl, resmap.get("periods"), resobj.toJSONString());
		}
	}

	/**
	 * @desc 总和路珠
	 * @author xg
	 * @author 2018年7月05日
	 */
	public void getZhlz(String lottId) {
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = 20;
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
				List list2 = (List) bigMap.get("xd");
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
						String string = list2.get(list2.size() - 1) + "";
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
						String string = list2.get(list2.size() - 1) + "";
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
						String string = list2.get(list2.size() - 1) + "";
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
				List list3 = (List) bigMap.get("ds");
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
						String string = list3.get(list3.size() - 1) + "";
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
						String string = list3.get(list3.size() - 1) + "";
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
				bigMap.put("ds", (ArrayList<String>) list3);
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
		JSONObject json = new JSONObject(new HashMap<>(bigMap));
		redisservice.set(lottId + "_" + LotteryDict.zhlz, json.toString());
	}

	/**
	 * 
	 * @desc 快乐8 上中下路珠
	 * @author abo
	 * @date 2018年7月5日 下午6:40:23
	 */
	public void getSxlz(String lottId) {
		// 当前时间
		String currentDate = LotteryUtil.countDayOfNow(lottId);
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);
		Map<String, String> maps = new HashMap<String, String>();
		List<Map<String, Object>> currentDayList = lotteryProcessMapper.findLotteryByDate(tablename, numstr, currentDate);
		int shangNum = 0;
		int zhongNum = 0;
		int xiaNum = 0;
		int sNum = 0;// 上的个数
		int xNum = 0;// 下的个数
		for (Map<String, Object> m : currentDayList) {
			sNum = 0;// 上的个数
			xNum = 0;// 下的个数
			// 上下：上为小于40的数的个数大于大于40的数，中为上下的个数相等
			for (int i = 1; i < num; i++) {
				int temp = Integer.parseInt(m.get("num" + i).toString());
				if (temp > 40) {
					xNum++;
				} else {
					sNum++;
				}
			}
			if (sNum > xNum) {
				shangNum++;
			} else if (sNum < xNum) {
				xiaNum++;
			} else if (sNum == xNum) {
				zhongNum++;
			}
		}
		maps.put(LotteryConstant.SHANG, shangNum + "");
		maps.put(LotteryConstant.XIA, xiaNum + "");
		maps.put(LotteryConstant.ZHONG, zhongNum + "");
		// 从数据库查询200条数据
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numstr, 200);
		String[] array = new String[200];
		Map<String, String> map = new LinkedHashMap<String, String>();
		String preStr = "";
		int r = 0;// 列数
		// 按照上中下重新生成数组
		for (int j = 0; j < list.size(); j++) {
			Map m = list.get(j);
			sNum = 0;// 上的个数
			xNum = 0;// 下的个数
			// 上下：上为小于40的数的个数大于大于40的数，中为上下的个数相等
			for (int i = 1; i < num; i++) {
				int temp = Integer.parseInt(m.get("num" + i).toString());
				if (temp > 40) {
					xNum++;
				} else {
					sNum++;
				}
			}
			if (sNum > xNum) {// 上区大于下区就是上
				array[j] = LotteryConstant.SHANG;
			} else if (sNum < xNum) {// 上区小于下区就是下
				array[j] = LotteryConstant.XIA;
			} else if (sNum == xNum) {// 上下一样就是中
				array[j] = LotteryConstant.ZHONG;
			}
			String strTemp = array[j];
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
		Map<String, Object> mm = new HashMap<String, Object>();
		mm.put("jrlj", maps);
		mm.put("sxplz", map);
		JSONObject resobj = new JSONObject(mm);
		redisservice.set(lottId + "_" + LotteryDict.sxlz, resobj.toJSONString());
	}

	/**
	 * 
	 * @desc 快乐8 奇偶路珠
	 * @author abo
	 * @date 2018年7月5日 下午6:40:23
	 */
	public void getJolz(String lottId) {
		// 当前时间
		String currentDate = LotteryUtil.countDayOfNow(lottId);
		// 表名
		String tablename = LotteryConstant.tableNameMap.get(lottId);
		// 查询球列的字符串
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		// 当前彩票的球数
		int num = LotteryConstant.lotteryNumMap.get(lottId);
		Map<String, String> maps = new HashMap<String, String>();
		List<Map<String, Object>> currentDayList = lotteryProcessMapper.findLotteryByDate(tablename, numstr, currentDate);
		int jiNum = 0;
		int heNum = 0;
		int xiaNum = 0;
		int jNum = 0;// 奇的个数
		int oNum = 0;// 偶的个数
		for (Map m : currentDayList) {
			jNum = 0;// 奇的个数
			oNum = 0;// 偶的个数
			// 奇偶个数。单数超过10个时为单盘，双数超过10个时为双盘，单双个数均为10时为和盘。
			for (int i = 1; i < num; i++) {
				int temp = Integer.parseInt(m.get("num" + i).toString());
				if (temp % 2 != 0) {
					jNum++;
				} else {
					oNum++;
				}
			}
			if (jNum > oNum) {
				jiNum++;
			} else if (jNum < oNum) {
				xiaNum++;
			} else if (jNum == oNum) {
				heNum++;
			}
		}
		maps.put(LotteryConstant.JI, jiNum + "");
		maps.put(LotteryConstant.OU, xiaNum + "");
		maps.put(LotteryConstant.HE, heNum + "");

		// 从数据库查询200条数据
		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tablename, numstr, 200);
		String[] array = new String[200];
		Map<String, String> map = new LinkedHashMap<String, String>();
		String preStr = "";
		int r = 0;// 列数
		// 按照上中下重新生成数组
		for (int j = 0; j < list.size(); j++) {
			Map m = list.get(j);
			jNum = 0;// 奇的个数
			oNum = 0;// 偶的个数
			// 奇偶个数。单数超过10个时为单盘，双数超过10个时为双盘，单双个数均为10时为和盘。
			for (int i = 1; i < num; i++) {
				int temp = Integer.parseInt(m.get("num" + i).toString());
				if (temp % 2 != 0) {
					jNum++;
				} else {
					oNum++;
				}
			}
			if (jNum > oNum) {
				array[j] = LotteryConstant.JI;
			} else if (jNum < oNum) {
				array[j] = LotteryConstant.OU;
			} else if (jNum == oNum) {
				array[j] = LotteryConstant.HE;
			}
			String strTemp = array[j];
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
		Map<String, Object> mm = new HashMap<String, Object>();
		mm.put("jrlj", maps);
		mm.put("sxplz", map);
		JSONObject resobj = new JSONObject(mm);
		redisservice.set(lottId + "_" + LotteryDict.jolz, resobj.toJSONString());
	}
}
