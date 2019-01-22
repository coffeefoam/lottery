package com.boying.cpapi.util.Yilou;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

import ch.qos.logback.core.net.SyslogOutputStream;

/**
 * 遗漏统计相关公共方法
 * 
 * @author ms
 */
public class YilouUtil {

	/**
	 * 计算号码出现次数
	 * 
	 * @param list
	 *            数据
	 * @param number
	 *            号码
	 * @param position
	 *            位置
	 * @return
	 */
	public static int hmcxcs(List<Map<String, Object>> list, String number, String position) {
		int i = 0;
		for (Map<String, Object> c : list) {
			if (number.equals(c.get(position).toString())) {
				i++;
			}
		}
		return i;
	}

	/**
	 * 
	 * @desc 江苏快三计算号码出现次数
	 * @author abo
	 * @date 2018年8月3日 下午8:00:31
	 * @param list
	 * @param map
	 * @return
	 */
	public static Map<String, Object> hmcxcsByJsk3(List<Map<String, Object>> list, Map<String, Object> map) {
		for (Map<String, Object> c : list) {
			int sum = (Integer) c.get("num1") + (Integer) c.get("num2") + (Integer) c.get("num3");
			int count = map.get(sum + "") == null ? 0 : (Integer) map.get(sum + "");
			map.put(sum + "", count + 1);
		}
		return map;
	}

	/**
	 * 
	 * @desc 北京赛车计算号码出现次数
	 * @author abo
	 * @date 2018年8月3日 下午8:00:31
	 * @param list
	 * @param position
	 * @param map
	 * @return
	 */
	public static Map<String, Object> gyhZcsZstByBjsc(List<Map<String, Object>> list, Map<String, Object> map) {
		for (Map<String, Object> c : list) {
			int sum = (Integer) c.get("num1");
			int count = map.get(sum + "") == null ? 0 : (Integer) map.get(sum + "");
			map.put(sum + "", count + 1);
		}
		return map;
	}

	/**
	 * 计算号码最大遗漏
	 * 
	 * @param list
	 *            数据
	 * @param number
	 *            号码
	 * @param position
	 *            位置
	 * @return
	 */
	public static int hmzdyl(List<Map<String, Object>> list, String number, String position) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			if (number.equals(list.get(i).get(position).toString())) {
				l.add(i);
			}
		}
		l.add(0, 0);
		l.add(l.size(), list.size() - 1);
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			if (j < l.size() - 1) {
				int bb = l.get(j + 1) - l.get(j);
				if (bb > max)
					max = bb;
			}
		}
		// 计算中间的间隔,需要减1
		return max - 1;
	}

	/**
	 * 
	 * @desc 江苏快3复写计算号码最大遗漏（几十期内，号码的最大遗漏期数）
	 * @author abo
	 * @date 2018年7月30日 下午4:33:24
	 * @param list
	 * @param number
	 * @param periods
	 * @return
	 */
	public static int hmzdyl1ByJsk3(List<Map<String, Object>> list, String number, int periods) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		// 是不是有效区域内一直连续遗漏的数据，比如当前40期数据，有一个号一直延续到前面70期
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		String currentNum;
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get("num1") + (Integer) list.get(i).get("num2") + (Integer) list.get(i).get("num3") + "";
			if (number.equals(currentNum)) {
				m.put(i - count, count);
				count = 0;
			} else {
				count++;
			}
			if (count > 0 && i == list.size() - 1) {
				m.put(i - count, count);
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 北京赛车冠亚和计算号码最大遗漏次数几十期内，号码的最大遗漏期数）
	 * @author abo
	 * @date 2018年7月30日 下午4:33:24
	 * @param list
	 * @param number
	 * @param periods
	 * @return
	 */
	public static int gyzZdylByBjsc(List<Map<String, Object>> list, String number, int periods) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		// 是不是有效区域内一直连续遗漏的数据，比如当前40期数据，有一个号一直延续到前面70期
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		String currentNum;
		for (int i = 0; i < list.size(); i++) {
			currentNum = list.get(i).get("num1").toString();
			if (number.equals(currentNum)) {
				m.put(i - count, count);
				count = 0;
			} else {
				count++;
			}
			if (count > 0 && i == list.size() - 1) {
				m.put(i - count, count);
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 复写计算号码最大遗漏
	 * @author abo
	 * @date 2018年7月30日 下午4:33:24
	 * @param list
	 * @param number
	 * @param position
	 * @return
	 */
	public static int hmzdyl1(List<Map<String, Object>> list, String number, String position) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			if (number.equals(list.get(i).get(position).toString())) {
				l.add(count);
				count = 0;
			} else {
				count++;
			}
			if (count > 0 && i == list.size() - 1) {
				l.add(count);
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		// 如果都没有的话就是最大list的遗漏
		if (l == null || l.size() == 0) {
			max = list.size();
		}
		return max;
	}

	/**
	 * 
	 * @desc 复写计算号码最大遗漏（几十期内，号码的最大遗漏期数）
	 * @author abo
	 * @date 2018年8月3日 下午8:01:04
	 * @param list
	 * @param number
	 * @param position
	 * @param periods
	 * @return
	 */
	public static int hmzdyl1(List<Map<String, Object>> list, String number, String position, int periods) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		// 是不是有效区域内一直连续遗漏的数据，比如当前40期数据，有一个号一直延续到前面70期
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < list.size(); i++) {
			if (number.equals(list.get(i).get(position).toString())) {
				m.put(i - count, count);
				count = 0;
			} else {
				count++;
			}
			if (count > 0 && i == list.size() - 1) {
				m.put(i - count, count);
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 北京赛车号码回摆重号最大遗漏统计
	 * @author abo
	 * @date 2018年8月3日 下午8:01:21
	 * @param list
	 * @param number
	 * @param positionArray
	 * @param periods
	 * @return
	 */
	public static int zhylSumByBjsc(List<Map<String, Object>> list, String number, String[] positionArray, int periods) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		// 是不是有效区域内一直连续遗漏的数据，比如当前40期数据，有一个号一直延续到前面70期
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		String preNum = "-1";
		for (int i = 0; i < list.size(); i++) {
			for (String position : positionArray) {
				String currenNum = list.get(i).get(position).toString();
				if (number.equals(currenNum)) {
					position = position.replace("num", "");
					if (preNum.equals("-1")) {
						preNum = position;
						continue;
					}
					if (preNum.equals(position)) {
						m.put(i - count, count);
						count = 0;
					} else {
						count++;
					}
					if (count > 0 && i == list.size() - 1) {
						m.put(i - count, count);
					}
					preNum = position;
				}
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 回摆重号最大遗漏统计
	 * @author abo
	 * @date 2018年8月3日 下午8:01:57
	 * @param list
	 * @param position
	 * @param periods
	 * @return
	 */
	public static int zhylSum(List<Map<String, Object>> list, String position, int periods) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		// 是不是有效区域内一直连续遗漏的数据，比如当前40期数据，有一个号一直延续到前面70期
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		String preNum = "-1";
		for (int i = 0; i < list.size(); i++) {
			String currenNum = list.get(i).get(position).toString();
			if (preNum.equals("-1")) {
				preNum = currenNum;
				continue;
			}
			if (preNum.equals(currenNum)) {
				m.put(i - count, count);
				count = 0;
			} else {
				count++;
			}
			if (count > 0 && i == list.size() - 1) {
				m.put(i - count, count);
			}
			preNum = currenNum;
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 江苏回摆重号最大遗漏统计
	 * @author abo
	 * @date 2018年7月30日 下午4:33:24
	 * @param list
	 * @param periods
	 * @return
	 */
	public static int zhylSumByJsk3(List<Map<String, Object>> list, int periods) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		// 是不是有效区域内一直连续遗漏的数据，比如当前40期数据，有一个号一直延续到前面70期
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		String preNum = "-1";
		String currentNum = "";
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get("num1") + (Integer) list.get(i).get("num2") + (Integer) list.get(i).get("num3") + "";
			if (preNum.equals("-1")) {
				preNum = currentNum;
				continue;
			}
			if (preNum.equals(currentNum)) {
				m.put(i - count, count);
				count = 0;
			} else {
				count++;
			}
			if (count > 0 && i == list.size() - 1) {
				m.put(i - count, count);
			}
			preNum = currentNum;
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 北京赛车最大遗漏回摆重号最大遗漏统计
	 * @author abo
	 * @date 2018年7月30日 下午4:33:24
	 * @param list
	 * @param periods
	 * @return
	 */
	public static int gyhZdyCfcsByBjsc(List<Map<String, Object>> list, int periods) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		// 是不是有效区域内一直连续遗漏的数据，比如当前40期数据，有一个号一直延续到前面70期
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		String preNum = "-1";
		String currentNum = "";
		for (int i = 0; i < list.size(); i++) {
			currentNum = list.get(i).get("num1").toString();
			if (preNum.equals("-1")) {
				preNum = currentNum;
				continue;
			}
			if (preNum.equals(currentNum)) {
				m.put(i - count, count);
				count = 0;
			} else {
				count++;
			}
			if (count > 0 && i == list.size() - 1) {
				m.put(i - count, count);
			}
			preNum = currentNum;
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 计算号码最大遗漏（几十期内，号码向前推遗漏，遇到同号就退出，返回遗漏期数）
	 * @author abo
	 * @date 2018年7月30日 下午4:33:24
	 * @param list
	 * @param number
	 * @return
	 */
	public static int hmzdyl2ByJsk3(List<Map<String, Object>> list, String number) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		String currentNum = "";
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get("num1") + (Integer) list.get(i).get("num2") + (Integer) list.get(i).get("num3") + "";
			if (number.equals(currentNum)) {
				l.add(count);
				break;
			} else {
				count++;
			}
			if (count > 0 && i == list.size() - 1) {
				l.add(count);
			}
			// 如果一直遗漏就继续往往期查询。
			if (i > 41 && (l != null && l.size() > 0)) {
				break;
			}
			// System.out.println(list.get(i).get(position));
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 计算号码最大遗漏（几十期内，号码向前推遗漏，遇到同号就退出，返回遗漏期数）
	 * @author abo
	 * @date 2018年7月30日 下午4:33:24
	 * @param list
	 * @param number
	 * @return
	 */
	public static int gyhMqZdylByBjsc(List<Map<String, Object>> list, String number) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		String currentNum = "";
		for (int i = 0; i < list.size(); i++) {
			currentNum = list.get(i).get("num1") + "";
			if (number.equals(currentNum)) {
				l.add(count);
				break;
			} else {
				count++;
			}
			if (count > 0 && i == list.size() - 1) {
				l.add(count);
			}
			// 如果一直遗漏就继续往往期查询。
			if (i > 41 && (l != null && l.size() > 0)) {
				break;
			}
			// System.out.println(list.get(i).get(position));
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 计算号码最大遗漏（几十期内，号码向前推遗漏，遇到同号就退出，返回遗漏期数）
	 * @author abo
	 * @date 2018年7月30日 下午4:33:24
	 * @param list
	 * @param number
	 * @param position
	 * @return
	 */
	public static int hmzdyl2(List<Map<String, Object>> list, String number, String position) {
		// 得到该号码出现的每一期的序号
		List<Integer> l = new ArrayList<Integer>();
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			if (number.equals(list.get(i).get(position).toString())) {
				l.add(count);
				break;
			} else {
				count++;
			}
			if (count > 0 && i == list.size() - 1) {
				l.add(count);
			}
			// 如果一直遗漏就继续往往期查询。
			if (i > 41 && (l != null && l.size() > 0)) {
				break;
			}
			// System.out.println(list.get(i).get(position));
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 北京赛车号码走势图，计算号码最大遗漏（几十期内，号码向前推遗漏，遇到同号就退出，返回遗漏期数）
	 * @author abo
	 * @date 2018年7月30日 下午4:33:24
	 * @param list
	 * @param number
	 * @param position
	 * @param positionArray
	 * @param listSize
	 * @return
	 */
	public static int hmzdyl2ByBjsc(List<Map<String, Object>> list, String number, String p, String[] positionArray, int listSize) {
		// 得到该号码出现的每一期的序号
		int count = 0;
		String preNumStr = "";
		String currentP = "";// 当前数字所在的位置
		for (int i = 0; i < list.size(); i++) {
			for (String position : positionArray) {
				if (number.equals(list.get(i).get(position).toString())) {
					currentP = position;
				}
			}
			if (StringUtils.isEmpty(preNumStr)) {
				preNumStr = currentP;
				continue;
			}
			if (preNumStr.equals(p)) {
				break;
			} else {
				count++;
			}
			preNumStr = currentP;
		}
		return count;
	}

	/**
	 * 计算大小出现次数
	 * 
	 * @param list
	 *            数据
	 * @param dxflag
	 *            turn: 返回小出现次数 false:返回大出现次数
	 * @param position
	 *            位置
	 * @param size
	 *            判断大小的值
	 * @return
	 */
	public static int dxcxcs(List<Map<String, Object>> list, boolean dxflag, String position, int size) {
		int bigcount = 0, smallcount = 0;
		for (Map<String, Object> c : list) {
			if (((Integer) c.get(position)) > size) {
				bigcount ++;
			} else {
				smallcount ++;
			}
		}
		if (dxflag)
			return smallcount;
		else
			return bigcount;
	}

	/**
	 * 
	 * @desc 北京赛车号码走势图前后汇总
	 * @author abo
	 * @date 2018年8月3日 上午11:25:00
	 * @param list
	 * @param dxflag
	 * @param number
	 * @param positionArray
	 * @param size
	 * @return
	 */
	public static int dxcxcsByBjsc(List<Map<String, Object>> list, boolean dxflag, String number, String[] positionArray, int size) {
		int qcount = 0;
		int hcount = 0;
		for (Map<String, Object> c : list) {
			for (String position : positionArray) {
				int currentNum = (Integer) c.get(position);
				if (currentNum == Integer.parseInt(number)) {
					int n = Integer.parseInt(position.replace("num", ""));
					if (n > size) {
						qcount ++;
					} else {
						hcount ++;
					}
				}
			}
		}
		if (dxflag)
			return hcount;
		else
			return qcount;
	}

	/**
	 * 
	 * @desc 计算江苏快三走势图总计次数大小
	 * @author abo
	 * @date 2018年8月2日 上午9:52:39
	 * @param list
	 * @param dxflag
	 *            turn: 返回小出现次数 false:返回大出现次数
	 * @return
	 */
	public static int dxcxcsByJsk3(List<Map<String, Object>> list, boolean dxflag) {
		int bigcount = 0, smallcount = 0;
		for (Map<String, Object> c : list) {
			Integer nowPosValue = (Integer) c.get("num1") + (Integer) c.get("num2") + (Integer) c.get("num3");
			if (nowPosValue > 10) {
				bigcount ++;
			} else {
				smallcount ++;
			}
		}
		if (dxflag)
			return smallcount;
		else
			return bigcount;
	}

	/**
	 * 
	 * @desc 计算北京赛车冠亚和大小走势图总计次数
	 * @author abo
	 * @date 2018年8月2日 上午9:52:39
	 * @param list
	 * @param dxflag
	 *            turn: 返回小出现次数 false:返回大出现次数
	 * @return
	 */
	public static int gyhZstDxZcsByBjsc(List<Map<String, Object>> list, boolean dxflag) {
		int bigcount = 0, smallcount = 0;
		for (Map<String, Object> c : list) {
			Integer nowPosValue = (Integer) c.get("num1");
			if (nowPosValue > 11) {
				bigcount = bigcount + 1;
			} else {
				smallcount = smallcount + 1;
			}
		}
		if (dxflag)
			return smallcount;
		else
			return bigcount;
	}

	/**
	 * 
	 * @desc 计算大小出现次数
	 * @author abo
	 * @date 2018年7月31日 上午10:51:31
	 * @param list
	 * @param dxflag
	 *            true: 返回小出现次数 false:返回大出现次数
	 * @param position
	 *            位置
	 * @param size
	 *            判断大小的值
	 * @return
	 */
	public static int dxcxcs1(List<Map<String, Object>> list, boolean dxflag, String position, int size) {
		int bigcount = 0;
		int smallcount = 0;
		int preNum = 0;
		int da = 1;
		int xiao = 0;
		for (Map<String, Object> c : list) {
			preNum = 0;// 默认为0小,1大
			if (((Integer) c.get(position)) > size) {
				preNum = 1;
			}
			if (dxflag) {
				if (da != preNum) {
					break;
				}
				bigcount++;
			} else {
				if (xiao != preNum) {
					break;
				}
				smallcount++;
			}
		}
		if (dxflag)
			return bigcount;
		else
			return smallcount;
	}

	/**
	 * 
	 * @desc 江苏快3计算大小出现次数和遗漏
	 * @author abo
	 * @date 2018年7月31日 上午10:51:31
	 * @param list
	 * @param dxflag
	 *            true: 返回小出现次数 false:返回大出现次数
	 * @param position
	 *            位置
	 * @param size
	 *            判断大小的值
	 * @return
	 */
	public static int dxcxcs1ByJsk3(List<Map<String, Object>> list, boolean dxflag) {
		int bigcount = 0;
		int smallcount = 0;
		int preNum = 0;// 默认为0小,1大
		int da = 1;
		int xiao = 0;
		int position = 0;
		for (Map<String, Object> c : list) {
			position = (Integer) c.get("num1") + (Integer) c.get("num2") + (Integer) c.get("num3");
			if (position > 10) {
				preNum = 1;
			}
			if (dxflag) {
				if (da != preNum) {
					break;
				}
				bigcount++;
			} else {
				if (xiao != preNum) {
					break;
				}
				smallcount++;
			}
		}
		if (dxflag)
			return bigcount;
		else
			return smallcount;
	}

	/**
	 * 
	 * @desc 北京赛车冠亚和走势图，每期数据大小遗漏
	 * @author abo
	 * @date 2018年7月31日 上午10:51:31
	 * @param list
	 * @param dxflag
	 *            true: 返回小出现次数 false:返回大出现次数
	 * @param position
	 *            位置
	 * @param size
	 *            判断大小的值
	 * @return
	 */
	public static int gyhMqDxByBjsc(List<Map<String, Object>> list, boolean dxflag) {
		int bigcount = 0;
		int smallcount = 0;
		int preNum = 0;// 默认为0小,1大
		int da = 1;
		int xiao = 0;
		int position = 0;
		for (Map<String, Object> c : list) {
			position = (Integer) c.get("num1");
			if (position > 11) {
				preNum = 1;
			}
			if (dxflag) {
				if (da != preNum) {
					break;
				}
				bigcount++;
			} else {
				if (xiao != preNum) {
					break;
				}
				smallcount++;
			}
		}
		if (dxflag)
			return bigcount;
		else
			return smallcount;
	}

	/**
	 * 计算大小最大遗漏
	 * 
	 * @param list
	 * @param dxflag
	 *            turn: 返回小最大遗漏次数 false:返回大最大遗漏次数
	 * @param position
	 *            位置
	 * @param size
	 *            判断大小的值
	 * @return
	 */
	public static int dxzdyl(List<Map<String, Object>> list, boolean dxflag, String position, int size) {
		// 得到该号码出现的每一期的序号
		List<Integer> smalllist = new ArrayList<Integer>();
		List<Integer> biglist = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			if (dxflag) {
				if (((Integer) list.get(i).get(position)) <= size)
					smalllist.add(i);
			} else {
				if (((Integer) list.get(i).get(position)) > size)
					biglist.add(i);
			}
		}
		// 循环计算最大间隔
		int max = 0;
		if (dxflag) {
			smalllist.add(0, 0);
			smalllist.add(smalllist.size(), list.size() - 1);
			for (int j = 0; j < smalllist.size(); j++) {
				if (j < smalllist.size() - 1) {
					int bb = smalllist.get(j + 1) - smalllist.get(j);
					if (bb > max)
						max = bb;
				}
			}
		} else {
			biglist.add(0, 0);
			biglist.add(biglist.size(), list.size() - 1);
			for (int j = 0; j < biglist.size(); j++) {
				if (j < biglist.size() - 1) {
					int bb = biglist.get(j + 1) - biglist.get(j);
					if (bb > max)
						max = bb;
				}
			}
		}
		return max - 1;
	}

	/**
	 * 计算单双出现次数
	 * 
	 * @param list
	 *            数据
	 * @param dsflag
	 *            turn: 返回单出现次数 false:返回双出现次数
	 * @param position
	 *            位置
	 * @return
	 */
	public static int dscxcs(List<Map<String, Object>> list, boolean dsflag, String position) {
		int dcount = 0, scount = 0;
		for (Map<String, Object> c : list) {
			if (((Integer) c.get(position)) % 2 == 1) {
				dcount = dcount + 1;
			} else {
				scount = scount + 1;
			}
		}
		if (dsflag)
			return dcount;
		else
			return scount;
	}

	/**
	 * 
	 * @desc 计算单双出现次数
	 * @author abo
	 * @date 2018年8月2日 上午9:41:31
	 * @param list
	 * @param dsflag
	 * @param position
	 * @return
	 */
	public static int dscxcsByJsk3(List<Map<String, Object>> list, boolean dsflag) {
		int dcount = 0, scount = 0;
		for (Map<String, Object> c : list) {
			Integer nowPosValue = (Integer) c.get("num1") + (Integer) c.get("num2") + (Integer) c.get("num3");
			if (nowPosValue % 2 == 1) {
				dcount = dcount + 1;
			} else {
				scount = scount + 1;
			}
		}
		if (dsflag)
			return dcount;
		else
			return scount;
	}

	/**
	 * 
	 * @desc 计算单双出现次数
	 * @author abo
	 * @date 2018年8月2日 上午9:41:31
	 * @param list
	 * @param dsflag
	 * @param position
	 * @return
	 */
	public static int gyhZstDsZcsByBjsc(List<Map<String, Object>> list, boolean dsflag) {
		int dcount = 0, scount = 0;
		for (Map<String, Object> c : list) {
			Integer nowPosValue = (Integer) c.get("num1");
			if (nowPosValue % 2 == 1) {
				dcount = dcount + 1;
			} else {
				scount = scount + 1;
			}
		}
		if (dsflag)
			return dcount;
		else
			return scount;
	}

	/**
	 * 
	 * @desc 计算最大遗漏大小遗漏总计
	 * @author abo
	 * @date 2018年8月1日 下午2:44:49
	 * @param list
	 * @param dsflag
	 *            true为小遗漏，false为大遗漏
	 * @param position
	 * @param limitnum
	 *            大小中间值，比如时时彩大于4就是大
	 * @param periods
	 *            选中期数
	 * @return
	 */
	public static int dxcxcsSum(List<Map<String, Object>> list, boolean dsflag, String position, int limitnum, int periods) {
		int dcount = 0;
		int xcount = 0;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		// 重新把列表正反重的集合
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get(position);
			// 大于界限值就是大
			if (currentNum > limitnum) {
				l.add(1);// 大
			} else {
				l.add(0);// 小
			}
		}
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < l.size(); i++) {
			currentNum = (Integer) l.get(i);
			if (dsflag) {
				if (1 == currentNum) {
					xcount++;
				} else {
					m.put(i - xcount, xcount);
					xcount = 0;
				}
			} else {
				if (currentNum == 0) {
					dcount++;
				} else {
					m.put(i - dcount, dcount);
					dcount = 0;
				}
			}
		}
		l = new ArrayList<Integer>();
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= (periods)) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 江苏快3计算最大遗漏大小遗漏总计
	 * @author abo
	 * @date 2018年8月1日 下午2:44:49
	 * @param list
	 * @param dsflag
	 *            true为小遗漏，false为大遗漏
	 * @param position
	 * @param limitnum
	 *            大小中间值，比如时时彩大于4就是大
	 * @param periods
	 *            选中期数
	 * @return
	 */
	public static int dxcxcsSumByJsk3(List<Map<String, Object>> list, boolean dsflag, int periods) {
		int dcount = 0;
		int xcount = 0;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		// 重新把列表正反重的集合
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get("num1") + (Integer) list.get(i).get("num2") + (Integer) list.get(i).get("num3");
			// 大于界限值就是大
			if (currentNum > 10) {
				l.add(1);// 大
			} else {
				l.add(0);// 小
			}
		}
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < l.size(); i++) {
			currentNum = (Integer) l.get(i);
			if (dsflag) {
				if (1 == currentNum) {
					xcount++;
				} else {
					m.put(i - xcount, xcount);
					xcount = 0;
				}
			} else {
				if (currentNum == 0) {
					dcount++;
				} else {
					m.put(i - dcount, dcount);
					dcount = 0;
				}
			}
		}
		l = new ArrayList<Integer>();
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= (periods)) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 江苏快3计算最大遗漏大小遗漏总计
	 * @author abo
	 * @date 2018年8月1日 下午2:44:49
	 * @param list
	 * @param dsflag
	 *            true为小遗漏，false为大遗漏
	 * @param position
	 * @param limitnum
	 *            大小中间值，比如时时彩大于4就是大
	 * @param periods
	 *            选中期数
	 * @return
	 */
	public static int gyhZdyDxcsByBjsc(List<Map<String, Object>> list, boolean dsflag, int periods) {
		int dcount = 0;
		int xcount = 0;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		// 重新把列表正反重的集合
		// for (int i = 0; i < list.size(); i++) {
		// currentNum = (Integer) list.get(i).get("num1");
		// // 大于界限值就是大
		// if (currentNum > 11) {
		// l.add(1);// 大
		// } else {
		// l.add(0);// 小
		// }
		// }
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get("num1");
			if (dsflag) {
				if (currentNum > 11) {
					xcount++;
				} else {
					m.put(i - xcount, xcount);
					xcount = 0;
				}
			} else {
				if (currentNum < 12) {
					dcount++;
				} else {
					m.put(i - dcount, dcount);
					dcount = 0;
				}
			}
		}
		l = new ArrayList<Integer>();
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= (periods)) {
				l.add(entity.getValue());
			}
		}
		// 降序排序，然后拿去最大的值
		Collections.sort(l, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});
		return l.get(0);
		// 循环计算最大间隔
		// int max = 0;
		// for (int j = 0; j < l.size(); j++) {
		// int bb = l.get(j);
		// if (bb > max)
		// max = bb;
		// }
		// return max;
	}

	/**
	 * 
	 * @desc 最大遗漏单双总计
	 * @author abo
	 * @date 2018年8月1日 下午2:42:05
	 * @param list
	 * @param dsflag
	 * @param position
	 * @param periods
	 * @return
	 */
	public static int dscxcsSum(List<Map<String, Object>> list, boolean dsflag, String position, int periods) {
		int dcount = 0;
		int scount = 0;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		// 重新把列表正反重的集合
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get(position);
			if (currentNum % 2 == 0) {
				l.add(0);
			} else {
				l.add(1);
			}
		}
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < l.size(); i++) {
			currentNum = (Integer) l.get(i);
			if (!dsflag) {
				if (1 == currentNum) {
					dcount++;
				} else {
					m.put(i - dcount, dcount);
					dcount = 0;
				}
			} else {
				if (currentNum == 0) {
					scount++;
				} else {
					m.put(i - scount, scount);
					scount = 0;
				}
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 江苏快3最大遗漏单双总计
	 * @author abo
	 * @date 2018年8月1日 下午2:42:05
	 * @param list
	 * @param dsflag
	 * @param position
	 * @param periods
	 * @return
	 */
	public static int dscxcsSumByJsk3(List<Map<String, Object>> list, boolean dsflag, int periods) {
		int dcount = 0;
		int scount = 0;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		// 重新把列表正反重的集合
		for (int i = 0; i < list.size(); i++) {
			// currentNum = (Integer) list.get(i).get(position);
			currentNum = (Integer) list.get(i).get("num1") + (Integer) list.get(i).get("num2") + (Integer) list.get(i).get("num3");
			if (currentNum % 2 == 0) {
				l.add(0);
			} else {
				l.add(1);
			}
		}
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < l.size(); i++) {
			currentNum = (Integer) l.get(i);
			if (!dsflag) {
				if (1 == currentNum) {
					dcount++;
				} else {
					m.put(i - dcount, dcount);
					dcount = 0;
				}
			} else {
				if (currentNum == 0) {
					scount++;
				} else {
					m.put(i - scount, scount);
					scount = 0;
				}
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 北京赛车冠亚和单双最大遗漏总计
	 * @author abo
	 * @date 2018年8月1日 下午2:42:05
	 * @param list
	 * @param dsflag
	 * @param position
	 * @param periods
	 * @return
	 */
	public static int gyhZdyDscsByBjsc(List<Map<String, Object>> list, boolean dsflag, int periods) {
		int dcount = 0;
		int scount = 0;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		// // 重新把列表正反重的集合
		// for (int i = 0; i < list.size(); i++) {
		// currentNum = (Integer) list.get(i).get("num1");
		// if (currentNum % 2 == 0) {
		// l.add(0);
		// System.out.println(list.get(i).get("periods")+"=="+0);
		// } else {
		// l.add(1);
		// System.out.println(list.get(i).get("periods")+"=="+1);
		// }
		// }
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// System.out.println(l);
		// 得到最大遗漏次数
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get("num1");
			if (!dsflag) {
				if (1 == (currentNum % 2)) {
					dcount++;
				} else {
					m.put(i - dcount, dcount);
					dcount = 0;
				}
			} else {
				if ((currentNum % 2) == 0) {
					scount++;
				} else {
					m.put(i - scount, scount);
					scount = 0;
				}
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 降序排序，然后拿去最大的值
		Collections.sort(l, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});
		return l.get(0);
		// 循环计算最大间隔
		// int max = 0;
		// for (int j = 0; j < l.size(); j++) {
		// int bb = l.get(j);
		// if (bb > max)
		// max = bb;
		// }
		// return max;
	}

	/**
	 * 
	 * @desc 计算遗漏单双出现次数，往前推，遇到一样的就退出
	 * @author abo
	 * @date 2018年7月31日 上午10:20:11
	 * @param list
	 * @param dsflag
	 * @param position
	 * @return
	 */
	public static int dscxcs1(List<Map<String, Object>> list, boolean dsflag, String position) {
		int dcount = 0;
		int scount = 0;
		int preNum = 0;
		int dan = 1;
		int shuang = 0;
		for (Map<String, Object> c : list) {
			preNum = (Integer) (c.get(position)) % 2;
			if (!dsflag) {
				if (dan != preNum) {
					break;
				}
				dcount++;
			} else {
				if (shuang != preNum) {
					break;
				}
				scount++;
			}
		}
		if (!dsflag)
			return dcount;
		else
			return scount;
	}

	/**
	 * 
	 * @desc 计算遗漏单双出现次数，往前推，遇到一样的就退出
	 * @author abo
	 * @date 2018年7月31日 上午10:20:11
	 * @param list
	 * @param dsflag
	 * @param position
	 * @param positionArray
	 * @return
	 */
	public static int dscxcs1ByBjsc(List<Map<String, Object>> list, boolean qhflag, String number, int s, String[] positionArray) {
		int qcount = 0;
		int hcount = 0;
		int currentNum = 0;
		String currentStr = "";
		List<Integer> l = new ArrayList<Integer>();
		// 重新把列表正反重的集合
		for (int i = 0; i < list.size(); i++) {
			for (String position : positionArray) {
				currentStr = list.get(i).get(position).toString();
				// 当前期数号码和传入的号码一致
				if (number.equals(currentStr)) {
					position = position.replace("num", "");
					if (Integer.parseInt(position) > s) {
						l.add(-1);
					} else {
						l.add(1);
					}
				}
			}
		}
		// 得到最大遗漏次数
		for (int i = 0; i < l.size(); i++) {
			currentNum = (Integer) l.get(i);
			if (!qhflag) {
				if (1 == currentNum) {
					qcount++;
				} else {
					break;
				}
			} else {
				if (currentNum == -1) {
					hcount++;
				} else {
					break;
				}
			}
		}
		if (!qhflag) {
			return qcount;
		} else {
			return hcount;
		}
	}

	/**
	 * 
	 * @desc 江苏快3计算遗漏单双出现次数，往前推，遇到一样的就退出
	 * @author abo
	 * @date 2018年7月31日 上午10:20:11
	 * @param list
	 * @param dsflag
	 * @param position
	 * @return
	 */
	public static int dscxcs1ByJsk3(List<Map<String, Object>> list, boolean dsflag) {
		int dcount = 0;
		int scount = 0;
		int preNum = 0;
		int dan = 1;
		int shuang = 0;
		for (Map<String, Object> c : list) {
			preNum = ((Integer) c.get("num1") + (Integer) c.get("num2") + (Integer) c.get("num3"));
			preNum = preNum % 2;
			if (!dsflag) {
				if (dan != preNum) {
					break;
				}
				dcount++;
			} else {
				if (shuang != preNum) {
					break;
				}
				scount++;
			}
		}
		if (!dsflag)
			return dcount;
		else
			return scount;
	}

	/**
	 * 
	 * @desc 北京赛车冠亚和走势图，每期数据单双遗漏，往前推，遇到一样的就退出
	 * @author abo
	 * @date 2018年7月31日 上午10:20:11
	 * @param list
	 * @param dsflag
	 * @param position
	 * @return
	 */
	public static int gyhMqDsByBjsc(List<Map<String, Object>> list, boolean dsflag) {
		int dcount = 0;
		int scount = 0;
		int preNum = 0;
		int dan = 1;
		int shuang = 0;
		for (Map<String, Object> c : list) {
			preNum = (Integer) c.get("num1");
			preNum = preNum % 2;
			if (!dsflag) {
				if (dan != preNum) {
					break;
				}
				dcount++;
			} else {
				if (shuang != preNum) {
					break;
				}
				scount++;
			}
		}
		if (!dsflag)
			return dcount;
		else
			return scount;
	}

	/**
	 * 计算单双最大遗漏
	 * 
	 * @param list
	 *            数据
	 * @param dxflag
	 *            turn: 返回单最大遗漏次数 false:返回双最大遗漏次数
	 * @param position
	 * @param size
	 * @return
	 */
	public static int dszdyl(List<Map<String, Object>> list, boolean dxflag, String position) {
		// 得到该号码出现的每一期的序号
		List<Integer> dlist = new ArrayList<Integer>();
		List<Integer> slist = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			if (((Integer) list.get(i).get(position)) % 2 == 1)
				dlist.add(i);
			else
				slist.add(i);
		}
		// 循环计算最大间隔
		int max = 0;
		if (dxflag) {
			dlist.add(0, 0);
			dlist.add(dlist.size(), list.size() - 1);
			for (int j = 0; j < dlist.size(); j++) {
				if (j < dlist.size() - 1) {
					int bb = dlist.get(j + 1) - dlist.get(j);
					if (bb > max)
						max = bb;
				}
			}
		} else {
			slist.add(0, 0);
			slist.add(slist.size(), list.size() - 1);
			for (int j = 0; j < slist.size(); j++) {
				if (j < slist.size() - 1) {
					int bb = slist.get(j + 1) - slist.get(j);
					if (bb > max)
						max = bb;
				}
			}
		}
		return max - 1;
	}

	/**
	 * 计算 总和大小出现次数
	 * @param list  数据
	 * @param positionarray 需要计算的位置数组
	 * @param dxflag turn: 返回总和大出现次数 false:返回总和小出现次数
	 * @param number 对比值
	 * @return
	 */
	public static int zhdxcxcs(List<Map<String, Object>> list, String[] positionarray, boolean dxflag, int number) {
		int smallcount = 0, bigcount = 0;
		for (Map<String, Object> c : list) {
			Integer total = 0;
			for (String position : positionarray) {
				total = total + (Integer) c.get(position);
			}
			if (total > number) {
				bigcount++;
			} else {
				smallcount++;
			}
		}
		if (dxflag)
			return bigcount;
		else
			return smallcount;
	}

	/**
	 * 计算 总和大小最大遗漏值
	 * 
	 * @param list
	 *            数据
	 * @param positionarray
	 *            需要计算的位置数组
	 * @param dxflag
	 *            turn: 返回总和大最大遗漏次数 false:返回总和小最大遗漏次数
	 * @param number
	 * @return
	 */
	public static int zhdxzdyl(List<Map<String, Object>> list, String[] positionarray, boolean dxflag, int number) {
		List<Integer> dlist = new ArrayList<Integer>();// 大
		List<Integer> xlist = new ArrayList<Integer>();// 小
		for (int i = 0; i < list.size(); i++) {
			Integer total = 0;
			for (String position : positionarray) {
				total = total + (Integer) list.get(i).get(position);
			}
			if (total > number)
				dlist.add(i);
			else
				xlist.add(i);
		}
		// 循环计算最大间隔
		int max = 0;
		if (dxflag) {
			dlist.add(0, 0);
			dlist.add(dlist.size(), list.size() - 1);
			for (int j = 0; j < dlist.size(); j++) {
				if (j < dlist.size() - 1) {
					int bb = dlist.get(j + 1) - dlist.get(j);
					if (bb > max)
						max = bb;
				}
			}
		} else {
			xlist.add(0, 0);
			xlist.add(xlist.size(), list.size() - 1);
			for (int j = 0; j < xlist.size(); j++) {
				if (j < xlist.size() - 1) {
					int bb = xlist.get(j + 1) - xlist.get(j);
					if (bb > max)
						max = bb;
				}
			}
		}
		return max - 1;
	}

	/**
	 * 计算总和单双出现次数
	 * @param list 数据
	 * @param positionarray 需要计算的位置数组
	 * @param dsflag turn: 返回冠亚和单出现次数 false:返回冠亚和双出现次数
	 * @return
	 */
	public static int zhdscxcs(List<Map<String, Object>> list, String[] positionarray, boolean dsflag) {
		int dcount = 0, scount = 0;
		for (Map<String, Object> c : list) {
			Integer total = 0;
			for (String position : positionarray) {
				total = total + (Integer) c.get(position);
			}
			if (total % 2 == 1) {
				dcount++;
			} else {
				scount++;
			}
		}
		if (dsflag)
			return dcount;
		else
			return scount;
	}

	/**
	 * 计算总和单双最大遗漏值
	 * 
	 * @param list
	 *            数据
	 * @param dsflag
	 *            turn: 返回亚和单最大遗漏次数 false:返回亚和双最大遗漏次数
	 * @return
	 */
	public static int zhdszdyl(List<Map<String, Object>> list, String[] positionarray, boolean dsflag) {
		List<Integer> dlist = new ArrayList<Integer>();// 单
		List<Integer> slist = new ArrayList<Integer>();// 双
		for (int i = 0; i < list.size(); i++) {
			Integer total = 0;
			for (String position : positionarray) {
				total = total + (Integer) list.get(i).get(position);
			}
			if (total % 2 == 1)
				dlist.add(i);
			else
				slist.add(i);
		}
		// 循环计算最大间隔
		int max = 0;
		if (dsflag) {
			dlist.add(0, 0);
			dlist.add(dlist.size(), list.size() - 1);
			for (int j = 0; j < dlist.size(); j++) {
				if (j < dlist.size() - 1) {
					int bb = dlist.get(j + 1) - dlist.get(j);
					if (bb > max)
						max = bb;
				}
			}
		} else {
			slist.add(0, 0);
			slist.add(slist.size(), list.size() - 1);
			for (int j = 0; j < slist.size(); j++) {
				if (j < slist.size() - 1) {
					int bb = slist.get(j + 1) - slist.get(j);
					if (bb > max)
						max = bb;
				}
			}
		}
		return max - 1;
	}

	/**
	 * 计算 总和点数出现次数
	 * 
	 * @param list
	 *            数据
	 * @param positionarray
	 *            需要计算的位置数组
	 * @param number
	 *            对比值
	 * @return
	 */
	public static int zhdianshucxcs(List<Map<String, Object>> list, String[] positionarray, Integer number) {
		int count = 0;
		for (Map<String, Object> c : list) {
			Integer total = 0;
			for (String position : positionarray) {
				total = total + (Integer) c.get(position);
			}
			if (total == number) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 计算 总和点数最大遗漏值
	 * 
	 * @param list
	 *            数据
	 * @param positionarray
	 *            需要计算的位置数组
	 * @param dxflag
	 *            turn: 返回总和大最大遗漏次数 false:返回总和小最大遗漏次数
	 * @param number
	 * @return
	 */
	public static int zhdianshuzdyl(List<Map<String, Object>> list, String[] positionarray, Integer number) {
		List<Integer> dlist = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			Integer total = 0;
			for (String position : positionarray) {
				total = total + (Integer) list.get(i).get(position);
			}
			if (total == number)
				dlist.add(i);
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < dlist.size(); j++) {
			if (j < dlist.size() - 1) {
				int bb = dlist.get(j + 1) - dlist.get(j);
				if (bb > max)
					max = bb;
			}
		}
		return max;
	}

	/**
	 * 龙虎出现次数
	 * 
	 * @param list
	 * @param lhflag
	 *            turn: 返回龙出现次数 false:返回虎出现次数
	 * @param position
	 *            位置 下标从0开始
	 * @return
	 */
	public static int lhcxcs(List<List<Integer>> list, boolean lhflag, int position) {
		int lcount = 0, hcount = 0;
		for (List<Integer> array : list) {
			if (array.get(position) > array.get(array.size() - 1 - position)) {
				lcount++;
			} else {
				hcount++;
			}
		}
		if (lhflag)
			return lcount;
		else
			return hcount;
	}

	/**
	 * 返回龙虎最大遗漏值
	 * 
	 * @param list
	 *            数据
	 * @param lhflag
	 *            turn: 返回龙最大遗漏次数 false:返回虎最大遗漏次数
	 * @param position
	 *            位置 下标从0开始
	 * @return
	 */
	public static int lhzdyl(List<List<Integer>> list, boolean lhflag, int position) {
		List<Integer> llist = new ArrayList<Integer>();
		List<Integer> hlist = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			List<Integer> l = list.get(i);
			if (l.get(position) > l.get(l.size() - 1 - position)) {
				llist.add(i);
			} else {
				hlist.add(i);
			}
		}
		// 循环计算间隔,取最大值
		int max = 0;
		if (lhflag) {
			llist.add(0, 0);
			llist.add(llist.size(), list.size() - 1);
			for (int j = 0; j < llist.size(); j++) {
				if (j < llist.size() - 1) {
					int bb = llist.get(j + 1) - llist.get(j);
					if (bb > max)
						max = bb;
				}
			}
		} else {
			hlist.add(0, 0);
			hlist.add(hlist.size(), list.size() - 1);
			for (int j = 0; j < hlist.size(); j++) {
				if (j < hlist.size() - 1) {
					int bb = hlist.get(j + 1) - hlist.get(j);
					if (bb > max)
						max = bb;
				}
			}
		}
		return max - 1;
	}

	/**
	 * k3短牌出现次数 三个骰子包含短牌指定的两个相同骰子(不分顺序)
	 * 
	 * @param list
	 *            数据
	 * @param positionarray
	 *            所有位置
	 * @param number
	 *            短牌值
	 * @return
	 */
	public static int duanpaicxcs(List<Map<String, Object>> list, String[] positionarray, String number) {
		Integer total = 0;
		for (Map<String, Object> c : list) {
			int count = 0;
			for (String position : positionarray) {
				if (number.equals(c.get(position) + ""))
					count++;
			}
			if (count > 1) {
				total++;
			}
		}
		return total;
	}

	/**
	 * k3短牌最大遗漏 三个骰子包含短牌指定的两个相同骰子(不分顺序)
	 * 
	 * @param list
	 *            数据
	 * @param positionarray
	 *            所有位置
	 * @param number
	 *            短牌值
	 * @return
	 */
	public static int duanpaizdyl(List<Map<String, Object>> list, String[] positionarray, String number) {
		List<Integer> llist = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			int count = 0;
			Map<String, Object> map = list.get(i);
			for (String position : positionarray) {
				if (number.equals(map.get(position) + ""))
					count = count + 1;
			}
			if (count > 1) {
				llist.add(i);
			}
		}
		// 循环计算间隔,取最大值
		int max = 0;
		for (int j = 0; j < llist.size(); j++) {
			if (j < llist.size() - 1) {
				int bb = llist.get(j + 1) - llist.get(j);
				if (bb > max)
					max = bb;
			}
		}
		return max;
	}

	/**
	 * k3长牌出现次数 三个骰子包含长牌指定的两个骰子(不分顺序)
	 * 
	 * @param list
	 *            所有期开奖数字集合
	 * @param number
	 *            长牌值
	 * @return
	 */
	public static int changpaicxcs(List<List<Integer>> list, List<Integer> numberlist) {
		Integer total = 0;
		for (List<Integer> c : list) {
			if (c.containsAll(numberlist)) {
				total = total + 1;
			}
		}
		return total;
	}

	/**
	 * k3长牌最大遗漏 三个骰子包含长牌指定的两个骰子(不分顺序)
	 * 
	 * @param list
	 *            所有期开奖数字集合
	 * @param number
	 *            长牌值
	 * @return
	 */
	public static int changpaizdyl(List<List<Integer>> list, List<Integer> numberlist) {
		List<Integer> llist = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			List<Integer> l = list.get(i);
			if (l.containsAll(numberlist)) {
				llist.add(i);
			}
		}
		// 循环计算间隔,取最大值
		int max = 0;
		for (int j = 0; j < llist.size(); j++) {
			if (j < llist.size() - 1) {
				int bb = llist.get(j + 1) - llist.get(j);
				if (bb > max)
					max = bb;
			}
		}
		return max;
	}

	/**
	 * k3三军出现次数 根据三个骰子点数来判断，只要开出一个选定的点数即可
	 * 
	 * @param list
	 *            所有期开奖数字集合
	 * @param number
	 *            三军值
	 * @return
	 */
	public static int sanjuncxcs(List<List<Integer>> list, Integer number) {
		Integer total = 0;
		for (List<Integer> c : list) {
			if (c.contains(number)) {
				total++;
			}
		}
		return total;
	}

	/**
	 * k3三军最大遗漏
	 * 
	 * @param list
	 *            所有期开奖数字集合
	 * @param number
	 *            三军值
	 * @return
	 */
	public static int sanjunzdyl(List<List<Integer>> list, Integer number) {
		List<Integer> llist = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			List<Integer> l = list.get(i);
			if (l.contains(number)) {
				llist.add(i);
			}
		}
		// 循环计算间隔,取最大值
		int max = 0;
		for (int j = 0; j < llist.size(); j++) {
			if (j < llist.size() - 1) {
				int bb = llist.get(j + 1) - llist.get(j);
				if (bb > max)
					max = bb;
			}
		}
		return max;
	}

	/**
	 * 返回传入数据指定的数组字段的值的集合
	 * 
	 * @param olist 转换前数据
	 * @param positionarray 定义取转换前数据的字段数组
	 * @return
	 */
	public static List<List<Integer>> getListValue(List<Map<String, Object>> list, String[] positionarray) {
		List<List<Integer>> nlist = new ArrayList<List<Integer>>();
		for (Map<String, Object> m : list) {
			List<Integer> l = new ArrayList<Integer>();
			for (String position : positionarray) {
				l.add((Integer) m.get(position));
			}
			nlist.add(l);
		}
		return nlist;
	}

	/**
	 * 正反次数
	 * 
	 * @param list
	 * @param dxflag
	 * @param position
	 * @param size
	 * @return
	 */
	public static int zfcs(List<Map<String, Object>> list, boolean zfFlag, String position) {
		int countZ = 0;
		int countF = 0;
		for (int i = 0; i < list.size(); i++) {
			Integer nowPosValue = Integer.parseInt(list.get(i).get(position).toString());
			Integer oldPosValue = 0;
			if (i + 1 < list.size()) {
				oldPosValue = Integer.parseInt(list.get(i + 1).get(position).toString());
				if (nowPosValue != oldPosValue) {
					if (nowPosValue > oldPosValue) {
						countZ++;
					} else {
						countF++;
					}
				}
			}
		}
		if (zfFlag) {
			return countF;
		}
		return countZ;
	}

	/**
	 * app数据对比正反次数
	 * 
	 * @param list
	 * @param dxflag
	 * @param position
	 * @param size
	 * @return
	 */
	public static int appSjdbZfcsByNumber(List<Map<String, Object>> list, boolean zfFlag, String number, String position) {
		int countZ = 0;
		int countF = 0;
		for (int i = 0; i < list.size(); i++) {
			int nowPosValue = Integer.parseInt(list.get(i).get(position).toString());
			int oldPosValue = 0;
			if (nowPosValue == Integer.parseInt(number)) {
				if (i + 1 < list.size()) {
					oldPosValue = Integer.parseInt(list.get(i + 1).get(position).toString());
					if (nowPosValue != oldPosValue) {
						if (nowPosValue > oldPosValue) {
							countZ++;
						} else {
							countF++;
						}
					}
				}
			}
		}
		if (zfFlag) {
			return countF;
		}
		return countZ;
	}

	/**
	 * 正反次数
	 * 
	 * @param list
	 * @param dxflag
	 * @param position
	 * @param size
	 * @return
	 */
	public static int zfcsByJsk3(List<Map<String, Object>> list, boolean zfFlag) {
		int countZ = 0;
		int countF = 0;
		Integer oldPosValue = 0;
		for (Map<String, Object> c : list) {
			Integer nowPosValue = (Integer) c.get("num1") + (Integer) c.get("num2") + (Integer) c.get("num3");
			if (oldPosValue == 0) {
				oldPosValue = nowPosValue;
				continue;
			}
			if (nowPosValue != oldPosValue) {
				if (nowPosValue > oldPosValue) {
					countZ++;
				} else {
					countF++;
				}
			}
			oldPosValue = nowPosValue;
		}
		if (zfFlag) {
			return countF;
		}
		return countZ;
	}

	/**
	 * 正反次数
	 * 
	 * @param list
	 * @param dxflag
	 * @param position
	 * @param size
	 * @return
	 */
	public static int gyhZfZstZcsByBjsc(List<Map<String, Object>> list, boolean zfFlag) {
		int countZ = 0;
		int countF = 0;
		Integer oldPosValue = 0;
		for (Map<String, Object> c : list) {
			Integer nowPosValue = (Integer) c.get("num1");
			if (oldPosValue == 0) {
				oldPosValue = nowPosValue;
				continue;
			}
			if (nowPosValue != oldPosValue) {
				if (nowPosValue > oldPosValue) {
					countZ++;
				} else {
					countF++;
				}
			}
			oldPosValue = nowPosValue;
		}
		if (zfFlag) {
			return countF;
		}
		return countZ;
	}

	/**
	 * 
	 * @desc 前中后
	 * @author abo
	 * @date 2018年8月4日 上午10:49:09
	 * @param list
	 * @return
	 */
	public static Map<String, Integer> gyhQzhZstZcsByBjsc(List<Map<String, Object>> list) {
		int countQ = 0;// 前
		int countZ = 0;// 中
		int countH = 0;// 后
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		for (Map<String, Object> c : list) {
			Integer nowPosValue = (Integer) c.get("num1");
			if (nowPosValue < 9) {
				countQ++;
			} else if (nowPosValue > 14) {
				countH++;
			} else {
				countZ++;
			}
		}
		map.put("qian", countQ);
		map.put("zhong", countZ);
		map.put("hou", countH);
		return map;
	}

	/**
	 * 
	 * @desc 遗漏正反次数汇总
	 * @author abo
	 * @date 2018年8月1日 下午2:21:48
	 * @param list
	 * @param zfFlag
	 * @param position
	 * @param periods
	 * @return
	 */
	public static int zfcsSum(List<Map<String, Object>> list, boolean zfFlag, String position, int periods) {
		int countZ = 0;
		int countF = 0;
		int preNum = -1;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		// 重新把列表正反重的集合
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get(position);
			if (preNum == -1) {
				preNum = currentNum;
				continue;
			}
			if (preNum > currentNum) {
				l.add(1);
			} else if (preNum < currentNum) {
				l.add(-1);
			} else {
				l.add(0);
			}
			preNum = currentNum;
		}
		preNum = 0;
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < l.size(); i++) {
			currentNum = (Integer) l.get(i);
			if (zfFlag) {
				if (1 == currentNum || currentNum == 0) {
					countZ++;
				} else {
					m.put(i - countZ, countZ);
					countZ = 0;
				}
			} else {
				if (-1 == currentNum || currentNum == 0) {
					countF++;
				} else {
					m.put(i - countF, countF);
					countF = 0;
				}
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 遗漏正反次数汇总
	 * @author abo
	 * @date 2018年8月1日 下午2:21:48
	 * @param list
	 * @param zfFlag
	 * @param position
	 * @param positionArray
	 * @param periods
	 * @return
	 */
	public static int zfcsSumByBjsc(List<Map<String, Object>> list, boolean zfFlag, String number, String[] positionArray, int periods) {
		int countZ = 0;
		int countF = 0;
		int preNum = -1;
		String currentNumStr = "";
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		// 重新把列表正反重的集合
		for (int i = 0; i < list.size(); i++) {
			for (String position : positionArray) {
				currentNumStr = list.get(i).get(position).toString();
				// 当前期数号码和传入的号码一致
				if (number.equals(currentNumStr)) {
					position = position.replace("num", "");
					if (preNum == -1) {
						preNum = Integer.parseInt(position);
						continue;
					}
					if (preNum > Integer.parseInt(position)) {
						l.add(1);
					} else if (preNum < Integer.parseInt(position)) {
						l.add(-1);
					} else {
						l.add(0);
					}
					preNum = Integer.parseInt(position);
				}
			}
		}
		preNum = 0;
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < l.size(); i++) {
			currentNum = (Integer) l.get(i);
			if (zfFlag) {
				if (1 == currentNum || currentNum == 0) {
					countZ++;
				} else {
					m.put(i - countZ, countZ);
					countZ = 0;
				}
			} else {
				if (-1 == currentNum || currentNum == 0) {
					countF++;
				} else {
					m.put(i - countF, countF);
					countF = 0;
				}
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 江苏快三遗漏正反次数汇总
	 * @author abo
	 * @date 2018年8月2日 上午10:02:16
	 * @param list
	 * @param zfFlag
	 * @param periods
	 * @return
	 */
	public static int zfcsSumByJsk3(List<Map<String, Object>> list, boolean zfFlag, int periods) {
		int countZ = 0;
		int countF = 0;
		int preNum = -1;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		// 重新把列表正反重的集合
		for (int i = 0; i < list.size(); i++) {
			// currentNum = (Integer) list.get(i).get(position);
			currentNum = (Integer) list.get(i).get("num1") + (Integer) list.get(i).get("num2") + (Integer) list.get(i).get("num3");
			if (preNum == -1) {
				preNum = currentNum;
				continue;
			}
			if (preNum > currentNum) {
				l.add(1);
			} else if (preNum < currentNum) {
				l.add(-1);
			} else {
				l.add(0);
			}
			preNum = currentNum;
		}
		preNum = 0;
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < l.size(); i++) {
			currentNum = (Integer) l.get(i);
			if (zfFlag) {
				if (1 == currentNum || currentNum == 0) {
					countZ++;
				} else {
					m.put(i - countZ, countZ);
					countZ = 0;
				}
			} else {
				if (-1 == currentNum || currentNum == 0) {
					countF++;
				} else {
					m.put(i - countF, countF);
					countF = 0;
				}
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 北京赛车冠亚和最大遗漏 正反向汇总
	 * @author abo
	 * @date 2018年8月2日 上午10:02:16
	 * @param list
	 * @param zfFlag
	 * @param periods
	 * @return
	 */
	public static int gyhZdylZfcsByBjsc(List<Map<String, Object>> list, boolean zfFlag, int periods) {
		int countZ = 0;
		int countF = 0;
		int preNum = -1;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		// 重新把列表正反重的集合
		for (int i = 0; i < list.size(); i++) {
			// currentNum = (Integer) list.get(i).get(position);
			currentNum = (Integer) list.get(i).get("num1");
			if (preNum == -1) {
				preNum = currentNum;
				continue;
			}
			if (preNum > currentNum) {
				l.add(1);
			} else if (preNum < currentNum) {
				l.add(-1);
			} else {
				l.add(0);
			}
			preNum = currentNum;
		}
		preNum = 0;
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < l.size(); i++) {
			currentNum = (Integer) l.get(i);
			if (zfFlag) {
				if (1 == currentNum || currentNum == 0) {
					countZ++;
				} else {
					m.put(i - countZ, countZ);
					countZ = 0;
				}
			} else {
				if (-1 == currentNum || currentNum == 0) {
					countF++;
				} else {
					m.put(i - countF, countF);
					countF = 0;
				}
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		// 循环计算最大间隔
		int max = 0;
		for (int j = 0; j < l.size(); j++) {
			int bb = l.get(j);
			if (bb > max)
				max = bb;
		}
		return max;
	}

	/**
	 * 
	 * @desc 冠亚和回摆正反向遗漏，往前推，遇到相同的就返回累计期数，并退出
	 * @author abo
	 * @date 2018年7月31日 上午11:20:06
	 * @param list
	 * @param zfFlag
	 * @return
	 */
	public static int gyhMqZdylQzhByBjsc(List<Map<String, Object>> list, int qzhFlag) {
		int countQ = 0;
		int countH = 0;
		int countZ = 0;
		int preNum = 0;// 前一个号码
		List<Integer> l = new ArrayList<>();
		// 先做一个正反向的集合
		for (int i = 0; i < list.size(); i++) {
			int currentNum = (Integer) list.get(i).get("num1");
			// if (preNum == 0) {
			// preNum = currentNum;
			// continue;
			// }
			if (currentNum > 14) {
				l.add(-1);
			} else if (currentNum < 9) {
				l.add(1);
			} else {
				l.add(0);
			}
			preNum = currentNum;
		}
		// 然后在把集合遍历取有没有连续遗漏
		for (int i = 0; i < l.size(); i++) {
			int currentNum = l.get(i);
			if (preNum == 0) {
				preNum = currentNum;
				continue;
			}
			if (qzhFlag == 1) {// 计算前
				if (1 == currentNum) {
					break;
				}
				countQ++;
			} else if (qzhFlag == 0) {// 计算中
				if (0 == currentNum) {
					break;
				}
				countZ++;
			} else if (qzhFlag == -1) {// 计算后
				if (-1 == currentNum) {
					break;
				}
				countH++;
			}
		}
		if (qzhFlag == 0) {// 计算中
			return countZ;
		} else if (qzhFlag == -1) {// 计算中
			return countH;
		}
		return countQ;
	}

	/**
	 * 
	 * @desc 北京赛车冠亚和每期最大遗漏前中后
	 * @author abo
	 * @date 2018年8月2日 上午10:02:16
	 * @param list
	 * @param zfFlag
	 * @param periods
	 * @return
	 */
	public static int gyhMqZdylQzhByBjsc1(List<Map<String, Object>> list, int zfFlag, int periods) {
		int countQ = 0;
		int countZ = 0;
		int countH = 0;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get("num1");
			if (zfFlag == 1) {
				if (currentNum > 8) {
					countQ++;
				} else {
					m.put(i - countQ, countQ);
					countQ = 0;
				}
			} else if (zfFlag == -1) {
				if (currentNum < 15) {
					countH++;
				} else {
					m.put(i - countH, countH);
					countH = 0;
				}
			} else {
				if (currentNum < 9 || currentNum > 14) {
					countZ++;
				} else {
					m.put(i - countZ, countZ);
					countZ = 0;
				}
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		Collections.sort(l, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});
		return l.get(0);
	}

	/**
	 * 
	 * @desc 北京赛车冠亚和最大遗漏前中后汇总
	 * @author abo
	 * @date 2018年8月2日 上午10:02:16
	 * @param list
	 * @param zfFlag
	 * @param periods
	 * @return
	 */
	public static int gyhZdylQzhcsByBjsc(List<Map<String, Object>> list, int zfFlag, int periods) {
		int countQ = 0;
		int countZ = 0;
		int countH = 0;
		int currentNum = 0;
		List<Integer> l = new ArrayList<Integer>();
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get("num1");
			if (zfFlag == 1) {
				if (currentNum > 8) {
					countQ++;
				} else {
					m.put(i - countQ, countQ);
					countQ = 0;
				}
			} else if (zfFlag == -1) {
				if (currentNum < 15) {
					countH++;
				} else {
					m.put(i - countH, countH);
					countH = 0;
				}
			} else {
				if (currentNum < 9 || currentNum > 14) {
					countZ++;
				} else {
					m.put(i - countZ, countZ);
					countZ = 0;
				}
			}
		}
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}
		Collections.sort(l, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});
		return l.get(0);
		// if (zfFlag == 1) {
		// return countQ;
		// }else if(zfFlag == -1) {
		// return countZ;
		// }else {
		// return countH;
		// }
	}

	/**
	 * 
	 * @desc 计算正向反向遗漏，往前推，遇到相同的就返回累计期数，并退出
	 * @author abo
	 * @date 2018年7月31日 上午11:20:06
	 * @param list
	 * @param zfFlag
	 * @param position
	 * @return
	 */
	public static int zfcs1(List<Map<String, Object>> list, boolean zfFlag, String position) {
		int countZ = 0;
		int countF = 0;
		int zNum = 1;// 正向num
		int fNum = -1;// 反向num
		int preNum = 0;// 前一个号码
		List<Integer> l = new ArrayList<>();
		int temp = zNum;// 默认匹配模板是正向
		if (zfFlag) {
			temp = fNum;
		}
		// 先做一个正反向的集合
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i).get(position));
			int currentNum = (Integer) (list.get(i).get(position));
			if (preNum == 0) {
				preNum = currentNum;
				continue;
			}
			if (currentNum > preNum) {
				l.add(fNum);
			} else if (currentNum < preNum) {
				l.add(zNum);
			} else {
				l.add(0);
			}
			preNum = currentNum;
		}
		// System.out.println(l);
		// 然后在把集合遍历取有没有连续遗漏
		for (int i = 0; i < l.size(); i++) {
			int currentNum = l.get(i);
			if (preNum == 0) {
				preNum = currentNum;
				continue;
			}
			if (!zfFlag) {
				if (temp == currentNum) {
					break;
				}
				countZ++;
			} else {
				if (temp == currentNum) {
					break;
				}
				countF++;
			}
		}
		if (zfFlag) {
			return countF;
		}
		return countZ;
	}

	/**
	 * 
	 * @desc 计算正向反向遗漏，往前推，遇到相同的就返回累计期数，并退出
	 * @author abo
	 * @date 2018年7月31日 上午11:20:06
	 * @param list
	 * @param zfFlag
	 *            正向为true反向为false
	 * @param position
	 * @param positionArray
	 * @return
	 */
	public static int zfcs1ByBjsc(List<Map<String, Object>> list, boolean zfFlag, String number, String[] positionArray) {
		int countZ = 0;
		int countF = 0;
		int zNum = 1;// 正向num
		int fNum = -1;// 反向num
		List<Integer> l = new ArrayList<>();
		int temp = fNum;// 默认匹配模板是正向
		if (zfFlag) {
			temp = zNum;
		}
		String currentP = "";
		String preP = "";// 前一个位置
		// 先做一个正反向的集合
		for (int i = 0; i < list.size(); i++) {
			for (String position : positionArray) {
				// 获取当前号码所在的位置
				if (number.equals(list.get(i).get(position).toString())) {
					currentP = position.replace("num", "");
				}
			}
			if (StringUtils.isEmpty(preP)) {
				preP = currentP;
				continue;
			}
			if (Integer.parseInt(currentP) > Integer.parseInt(preP)) {
				l.add(fNum);
			} else if (Integer.parseInt(currentP) < Integer.parseInt(preP)) {
				l.add(zNum);
			} else {
				l.add(0);
			}
			preP = currentP;
		}
		// 然后在把集合遍历取有没有连续遗漏
		for (int i = 0; i < l.size(); i++) {
			int currentNum = l.get(i);
			if (zfFlag) {
				if (temp == currentNum) {
					break;
				} else {
					countF++;
				}
			} else {
				if (temp == currentNum) {
					break;
				} else {
					countZ++;
				}
			}
		}
		if (!zfFlag) {
			return countZ;
		}
		return countF;
	}

	/**
	 * 
	 * @desc 江苏快3计算正向反向遗漏，往前推，遇到相同的就返回累计期数，并退出
	 * @author abo
	 * @date 2018年7月31日 上午11:20:06
	 * @param list
	 * @param zfFlag
	 * @param position
	 * @return
	 */
	public static int zfcs1ByJsk3(List<Map<String, Object>> list, boolean zfFlag) {
		int countZ = 0;
		int countF = 0;
		int zNum = 1;// 正向num
		int fNum = -1;// 反向num
		int preNum = 0;// 前一个号码
		List<Integer> l = new ArrayList<>();
		int temp = zNum;// 默认匹配模板是正向
		if (zfFlag) {
			temp = fNum;
		}
		// 先做一个正反向的集合
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i).get(position));
			int currentNum = (Integer) list.get(i).get("num1") + (Integer) list.get(i).get("num2") + (Integer) list.get(i).get("num3");
			if (preNum == 0) {
				preNum = currentNum;
				continue;
			}
			if (currentNum > preNum) {
				l.add(fNum);
			} else if (currentNum < preNum) {
				l.add(zNum);
			} else {
				l.add(0);
			}
			preNum = currentNum;
		}
		// 然后在把集合遍历取有没有连续遗漏
		for (int i = 0; i < l.size(); i++) {
			int currentNum = l.get(i);
			if (preNum == 0) {
				preNum = currentNum;
				continue;
			}
			if (!zfFlag) {
				if (temp == currentNum) {
					break;
				}
				countZ++;
			} else {
				if (temp == currentNum) {
					break;
				}
				countF++;
			}
		}
		if (zfFlag) {
			return countF;
		}
		return countZ;
	}

	/**
	 * 
	 * @desc 冠亚和回摆正反向遗漏，往前推，遇到相同的就返回累计期数，并退出
	 * @author abo
	 * @date 2018年7月31日 上午11:20:06
	 * @param list
	 * @param zfFlag
	 * @return
	 */
	public static int gyhMqZcfByBjsc(List<Map<String, Object>> list, boolean zfFlag) {
		int countZ = 0;
		int countF = 0;
		int zNum = 1;// 正向num
		int fNum = -1;// 反向num
		int preNum = 0;// 前一个号码
		List<Integer> l = new ArrayList<>();
		int temp = zNum;// 默认匹配模板是正向
		if (zfFlag) {
			temp = fNum;
		}
		// 先做一个正反向的集合
		for (int i = 0; i < list.size(); i++) {
			int currentNum = (Integer) list.get(i).get("num1");
			if (preNum == 0) {
				preNum = currentNum;
				continue;
			}
			if (currentNum > preNum) {
				l.add(fNum);
			} else if (currentNum < preNum) {
				l.add(zNum);
			} else {
				l.add(0);
			}
			preNum = currentNum;
		}
		// 然后在把集合遍历取有没有连续遗漏
		for (int i = 0; i < l.size(); i++) {
			int currentNum = l.get(i);
			if (preNum == 0) {
				preNum = currentNum;
				continue;
			}
			if (!zfFlag) {
				if (temp == currentNum) {
					break;
				}
				countZ++;
			} else {
				if (temp == currentNum) {
					break;
				}
				countF++;
			}
		}
		if (zfFlag) {
			return countF;
		}
		return countZ;
	}

	public static int cfcs(List<Map<String, Object>> list, String position) {
		int resCount = 0;
		for (int i = 0; i < list.size(); i++) {
			Integer nowPosValue = Integer.parseInt(list.get(i).get(position).toString());
			Integer oldPosValue = 0;
			if (i + 1 < list.size()) {
				oldPosValue = Integer.parseInt(list.get(i + 1).get(position).toString());
			}
			if (nowPosValue.equals(oldPosValue)) {
				resCount ++;
			}
		}
		return resCount;
	}

	/**
	 * 
	 * @desc
	 * @author abo
	 * @date 2018年8月3日 下午12:38:38
	 * @param list
	 * @param position
	 * @param positionArray
	 * @return
	 */
	public static int cfcsByBjsc(List<Map<String, Object>> list, String number, String[] positionArray) {
		int resCount = 0;
		String prePosition = "";
		for (int i = 0; i < list.size(); i++) {
			for (String position : positionArray) {
				String currentNum = list.get(i).get(position).toString();
				// 当前期数号码和传入的号码一致
				if (number.equals(currentNum)) {
					if (StringUtils.isEmpty(prePosition)) {
						prePosition = position;
						continue;
					}
					if (prePosition == position) {
						resCount++;
					}
					prePosition = position;
				}
			}
		}
		return resCount;
	}

	/**
	 * 
	 * @desc 重号按照数字返回
	 * @author abo
	 * @date 2018年8月13日 下午5:31:26
	 * @param list
	 * @param number
	 * @param positionArray
	 * @return
	 */
	public static int cfcsByNumber(List<Map<String, Object>> list, String number, String position) {
		int resCount = 0;
		String oldPeriods = "";// 老的期数
		for (int i = 0; i < list.size(); i++) {
			String currentNum = list.get(i).get(position).toString();
			String newPeriods = list.get(i).get("periods").toString();
			// 当前期数号码和传入的号码一致
			if (number.equals(currentNum)) {
				if (StringUtils.isEmpty(oldPeriods)) {
					oldPeriods = newPeriods;
					continue;
				}
				// 相差为1就是重号
				if ((Long.parseLong(oldPeriods) - Long.parseLong(newPeriods)) == 1) {
					resCount++;
				}
				oldPeriods = newPeriods;
			}
		}
		return resCount;
	}

	/**
	 * 
	 * @desc 江苏快3正反向总次数
	 * @author abo
	 * @date 2018年8月1日 下午5:55:07
	 * @param list
	 * @param position
	 * @return
	 */
	public static int cfcsByJsk3(List<Map<String, Object>> list) {
		int resCount = 0;
		Integer oldPosValue = 0;
		Integer nowPosValue = 0;
		for (Map<String, Object> c : list) {
			nowPosValue = (Integer) c.get("num1") + (Integer) c.get("num2") + (Integer) c.get("num3");
			if (oldPosValue == 0) {
				oldPosValue = nowPosValue;
				continue;
			}
			if (nowPosValue.equals(oldPosValue)) {
				resCount ++;
			}
			oldPosValue = nowPosValue;
		}
		return resCount;
	}

	/**
	 * 
	 * @desc 江苏快3正反向总次数
	 * @author abo
	 * @date 2018年8月1日 下午5:55:07
	 * @param list
	 * @param position
	 * @return
	 */
	public static int gyhCfZstZcsByBjsc(List<Map<String, Object>> list) {
		int resCount = 0;
		Integer oldPosValue = 0;
		Integer nowPosValue = 0;
		for (Map<String, Object> c : list) {
			nowPosValue = (Integer) c.get("num1");
			if (oldPosValue == 0) {
				oldPosValue = nowPosValue;
				continue;
			}
			if (nowPosValue.equals(oldPosValue)) {
				resCount ++;
			}
			oldPosValue = nowPosValue;
		}
		return resCount;
	}

	/**
	 * 
	 * @desc 重号遗漏，往前推，遇到一样的就退出
	 * @author abo
	 * @date 2018年7月31日 上午11:03:34
	 * @param list
	 * @param position
	 * @return
	 */
	public static int cfcs1(List<Map<String, Object>> list, String position) {
		int resCount = 0;
		int preNum = -1;
		for (int i = 0; i < list.size(); i++) {
			int currentNum = (Integer) list.get(i).get(position);
			// System.out.println(currentNum);
			if (preNum == -1) {
				preNum = currentNum;
				continue;
			}
			if (preNum == currentNum) {
				break;
			}
			resCount++;
			preNum = currentNum;
		}
		return resCount;
	}

	/**
	 * 
	 * @desc 北京赛车号码走势图，每期走势，重号遗漏，往前推，遇到一样的就退出
	 * @author abo
	 * @date 2018年7月31日 上午11:03:34
	 * @param list
	 * @param position
	 * @param positionArray
	 * @return
	 */
	public static int cfcs1ByBjsc(List<Map<String, Object>> list, String number, String[] positionArray) {
		int resCount = 0;
		String currentP = "";
		String preP = "";
		for (int i = 0; i < list.size(); i++) {
			for (String position : positionArray) {
				// 获取当前号码所在的位置
				if (number.equals(list.get(i).get(position).toString())) {
					currentP = position;
				}
			}
			if (StringUtils.isEmpty(preP)) {
				preP = currentP;
				continue;
			}
			if (preP.equals(currentP)) {
				break;
			} else {
				resCount++;
			}
			preP = currentP;
		}
		return resCount;
	}

	/**
	 * 
	 * @desc 江苏快3重号遗漏，往前推，遇到一样的就退出
	 * @author abo
	 * @date 2018年7月31日 上午11:03:34
	 * @param list
	 * @param position
	 * @return
	 */
	public static int cfcs1ByJsk3(List<Map<String, Object>> list) {
		int resCount = 0;
		int preNum = -1;
		int currentNum;
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get("num1") + (Integer) list.get(i).get("num2") + (Integer) list.get(i).get("num3");
			if (preNum == -1) {
				preNum = currentNum;
				continue;
			}
			if (preNum == currentNum) {
				break;
			}
			resCount++;
			preNum = currentNum;
		}
		return resCount;
	}

	/**
	 * 
	 * @desc 北京赛车冠亚和走势图每期回摆重号遗漏，往前推，遇到一样的就退出
	 * @author abo
	 * @date 2018年7月31日 上午11:03:34
	 * @param list
	 * @param position
	 * @return
	 */
	public static int gyhMqcfcsByBjsc(List<Map<String, Object>> list) {
		int resCount = 0;
		int preNum = -1;
		int currentNum;
		for (int i = 0; i < list.size(); i++) {
			currentNum = (Integer) list.get(i).get("num1");
			if (preNum == -1) {
				preNum = currentNum;
				continue;
			}
			if (preNum == currentNum) {
				break;
			}
			resCount++;
			preNum = currentNum;
		}
		return resCount;
	}

	/**
	 * 
	 * @desc 北京赛车正反向总次数
	 * @author abo
	 * @date 2018年8月3日 下午1:38:32
	 * @param list
	 * @param zfFlag
	 * @param number
	 * @param positionArray
	 * @return
	 */
	public static int zfcsByBjsc(List<Map<String, Object>> list, boolean zfFlag, String number, String[] positionArray) {
		int countZ = 0;
		int countF = 0;
		int prePositionNum = 0;
		for (int i = 0; i < list.size(); i++) {
			for (String position : positionArray) {
				String currentNum = list.get(i).get(position).toString();
				// 当前期数号码和传入的号码一致
				if (number.equals(currentNum)) {
					position = position.replace("num", "");
					if (prePositionNum == 0) {
						prePositionNum = Integer.parseInt(position);
						continue;
					}
					if (prePositionNum > Integer.parseInt(position)) {
						countZ++;
					} else if (prePositionNum < Integer.parseInt(position)) {
						countF++;
					}
					prePositionNum = Integer.parseInt(position);
				}
			}
		}
		if (zfFlag) {
			return countF;
		}
		return countZ;
	}

	/**
	 * 
	 * @desc 北京赛车前后最大遗漏总计
	 * @author abo
	 * @date 2018年8月3日 下午2:58:59
	 * @param list
	 * @param dsflag
	 * @param number
	 * @param positionArray
	 * @param periods
	 * @param size
	 * @return
	 */
	public static int qhZdylSumByBjsc(List<Map<String, Object>> list, boolean dsflag, String number, String[] positionArray, int s, int periods) {
		int qcount = 0;
		int hcount = 0;
		int currentNum = 0;
		String currentStr = "";
		List<Integer> l = new ArrayList<Integer>();
		int preNum = -1;
		// 重新把列表正反重的集合
		for (int i = 0; i < list.size(); i++) {
			// System.out.println(list.get(i).get("periods")+"===="+i);
			for (String position : positionArray) {
				currentStr = list.get(i).get(position).toString();
				// 当前期数号码和传入的号码一致
				if (number.equals(currentStr)) {
					// System.out.println(position);
					position = position.replace("num", "");
					if (preNum == -1) {
						preNum = Integer.parseInt(position);
						continue;
					}
					if (Integer.parseInt(position) > s) {
						l.add(-1);
					} else {
						l.add(1);
					}
					preNum = Integer.parseInt(position);
				}
			}
		}
		// System.out.println(l);
		Map<Integer, Integer> m = new LinkedHashMap<Integer, Integer>();
		// 得到最大遗漏次数
		for (int i = 0; i < l.size(); i++) {
			currentNum = (Integer) l.get(i);
			if (!dsflag) {
				if (1 == currentNum) {
					qcount++;
				} else {
					m.put(i - qcount, qcount);
					qcount = 0;
				}
			} else {
				if (currentNum == -1) {
					hcount++;
				} else if (1 == currentNum) {
					m.put(i - hcount, hcount);
					hcount = 0;
				}
			}
		}
		// System.out.println(m);
		// 去掉不在期数条件以外的遗漏，比如有的遗漏数挺大，但是不在期数条件里面，比如昨天的遗漏
		for (Entry<Integer, Integer> entity : m.entrySet()) {
			if (entity.getKey() <= periods) {
				l.add(entity.getValue());
			}
		}

		Collections.sort(l, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});
		return l.get(0);
	}
}