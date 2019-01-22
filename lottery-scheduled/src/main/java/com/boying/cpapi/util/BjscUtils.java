package com.boying.cpapi.util;

import io.lottery.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: BjscUtils
 * @Description: 基本的一些依赖算法
 * @author 老王
 * @date 2018年7月12日 下午5:11:00
 *
 */
public class BjscUtils {

	/**
	 * @Description :计算对比正确的结果
	 * @author 老王
	 * 
	 */
	public static Map<String, Object> genRightData(Map<String, Object> newOne, Map<String, Object> oldOne, int count) {

		String v = oldOne.get("plan1").toString();
		String[] v_array = v.split(",");
		String plan1 = compareOne(checkType(v), newOne, Integer.parseInt(v_array[0]), count);
		String result1 = v_array[1].equals(plan1) ? "赢" : "输";
		newOne.put("plan1", v + "," + result1);

		v = oldOne.get("plan2").toString();
		v_array = v.split(",");
		String plan2 = compareOne(checkType(v), newOne, Integer.parseInt(v_array[0]), count);
		String result2 = v_array[1].equals(plan2) ? "赢" : "输";
		newOne.put("plan2", v + "," + result2);

		v = oldOne.get("plan3").toString();
		v_array = v.split(",");
		String plan3 = compareOne(checkType(v), newOne, Integer.parseInt(v_array[0]), count);
		String result3 = v_array[1].equals(plan3) ? "赢" : "输";
		newOne.put("plan3", v + "," + result1);
		String[] result = { result1, result2, result3 };
		// 更新最后一个字段
		newOne.put("result", checkResult(result));

		return newOne;

	}

	/**
	 * @Description :判断输赢
	 * @author 老王
	 * 
	 */
	public static String checkResult(String[] result) {
		int count = 0;
		for (String string : result) {
			if ("输".equals(string)) {
				count++;
			}
		}
		return count > 1 ? "输" : "赢";

	}

	/**
	 * @Description :检查是那种类型
	 * @author 老王
	 * 
	 */
	public static int checkType(String data) {
		if (data.contains("大") || data.contains("小")) {
			return 0;
		}
		if (data.contains("单") || data.contains("双")) {
			return 1;
		}
		if (data.contains("龙") || data.contains("虎")) {
			return 2;
		}
		return -1;
	}

	/**
	 * @Description :随机生成对的数据
	 * @author 老王
	 * 
	 */
	public static Map<String, Object> genRightData(Map<String, Object> newOne, int count) {

		int[] num = getRandomArrayByIndex(3, count);
		int type = new java.util.Random().nextInt(2);// 1:longhu,2:daxiao
														// 3:danshuang
		if ((num[0] < 5 && count == 10) || num[0] == 1 && count == 5) {
			type = new java.util.Random().nextInt(3);// 1:longhu,2:daxiao
														// 3:danshuang
		}

		String num1 = newOne.get("num" + num[0]).toString();// 号码
		String result = compareOne(type, newOne, num[0], count);
		newOne.put("plan1", num[0] + "," + result + ",赢");

		if ((num[1] < 5 && count == 10) || num[1] == 1 && count == 5) {
			type = new java.util.Random().nextInt(3);// 1:longhu,2:daxiao
														// 3:danshuang
		}
		String num2 = newOne.get("num" + num[1]).toString();// 号码
		result = compareOne(type, newOne, num[1], count);
		newOne.put("plan2", num[1] + "," + result + ",赢");

		if ((num[2] < 5 && count == 10) || num[2] == 1 && count == 5) {
			type = new java.util.Random().nextInt(3);// 1:longhu,2:daxiao
														// 3:danshuang
		}
		String num3 = newOne.get("num" + num[2]).toString();// 号码
		result = compareOne(type, newOne, num[2], count);
		newOne.put("plan3", num[2] + "," + result + ",赢");
		newOne.put("result", "赢");
		newOne.put("ying", "1");
		newOne.put("shu", "0");

		return newOne;
	}

	public static void main(String[] args) {

		String[] test = { "赢", "输", "输", };
		System.out.println(checkResult(test));

		Map<String, Object> newOne = new HashMap<>();

		newOne.put("num1", "2");
		newOne.put("num2", "3");
		newOne.put("num3", "4");
		newOne.put("num4", "7");
		newOne.put("num5", "8");
		newOne.put("num6", "9");
		newOne.put("num7", "1");
		newOne.put("num8", "5");
		newOne.put("num9", "6");
		newOne.put("num10", "10");
		newOne.put("periods", "692397");
		newOne.put("time", "1531324800000");

		String a = "20180831050";
		String year = a.substring(0, 4);
		String month = a.substring(4, 6);
		String day = a.substring(6, 8);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.parseInt(year));
		calendar.set(Calendar.MONTH, Integer.parseInt(month));
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day) + 1);
		Date time = calendar.getTime();

	}

	public static int countData(Map<Object, Object> data, String key) {
		int count = 0;
		for (Object obj : data.values()) {
			Map<String, Object> temp = JSON.parseObject(obj.toString());// 预测数据
			if (key.equals(temp.get("result"))) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @Description :比较是否正确
	 * @author 老王
	 */
	public static String compareOne(int type, Map<String, Object> newOne, int poz, int count) {

		// 1:longhu,2:daxiao 3:danshuang
		// 1-10 2-9 3-8 4-7 5-6
		switch (type) {
		case 0:
			if (count == 5) {
				return Integer.parseInt(newOne.get("num" + poz).toString()) > 4 ? "大" : "小";
			}
			return Integer.parseInt(newOne.get("num" + poz).toString()) > 5 ? "大" : "小";
		case 1:
			return Integer.parseInt(newOne.get("num" + poz).toString()) % 2 == 0 ? "双" : "单";
		case 2:
			return compareLongHu(newOne, poz, count);
		default:
			break;
		}
		return null;

	}

	/**
	 * @Description :比较龙虎
	 * @author 老王
	 * 
	 */
	public static String compareLongHu(Map<String, Object> newOne, int poz, int count) {
		int num = Integer.parseInt(newOne.get("num" + poz).toString());
		if (count == 5) {
			return num > Integer.parseInt(newOne.get("num5").toString()) ? "龙" : "虎";
		}

		switch (poz) {
		case 1:
			return num > Integer.parseInt(newOne.get("num10").toString()) ? "龙" : "虎";
		case 2:
			return num > Integer.parseInt(newOne.get("num9").toString()) ? "龙" : "虎";
		case 3:
			return num > Integer.parseInt(newOne.get("num8").toString()) ? "龙" : "虎";
		case 4:
			return num > Integer.parseInt(newOne.get("num7").toString()) ? "龙" : "虎";
		case 5:
			return num > Integer.parseInt(newOne.get("num6").toString()) ? "龙" : "虎";
		default:
			break;
		}

		return null;

	}

	/**
	 * @Description :隨機龙虎单双大小
	 * @author 老王
	 *         <p>
	 * 
	 *         <p>
	 * 
	 *         <P>
	 * 
	 */
	public static String getRandomByType(int type) {
		String[] longhu = { "龙", "虎" };
		String[] daxiao = { "大", "小" };
		String[] danshuang = { "单", "双" };
		switch (type) {
		case 0:
			return danshuang[new java.util.Random().nextInt(2)];
		case 1:
			return daxiao[new java.util.Random().nextInt(2)];
		case 2:
			return longhu[new java.util.Random().nextInt(2)];
		default:
			break;
		}

		return null;
	}

	/**
	 * @Description :随机范围内不重复的数字
	 * @author 老王
	 * 
	 */
	public static int[] getRandomArrayByIndex(int num, int scope) {
		int[] randomArray = new int[scope];
		for (int i = 0; i < randomArray.length; i++) {
			randomArray[i] = i + 1;
		}

		int[] numArray = new int[num];// 存储num个随机数
		int i = 0;
		while (i < numArray.length) {
			int index = (int) (Math.random() * scope);
			if (randomArray[index] != -1) {
				numArray[i] = randomArray[index];
				randomArray[index] = -1;
				i++;
			}
		}

		return numArray;
	}

	/**
	 * 获取下一期
	 * 
	 * @param periods
	 * @param lottId
	 * @return
	 */
	public static String getNextPeriod(String periods, String lottId) {
		String subPeriods = periods.substring(periods.length() - 3, periods.length());
		String periods2 = Long.parseLong(periods) + 1 + "";
		if (subPeriods.equals("120") && lottId.equals(LotteryConstant.CQSSC)) {
			// 重庆时时彩隔天第一期
			String year = periods.substring(0, 4);
			String month = periods.substring(4, 6);
			String day = periods.substring(6, 8);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, Integer.parseInt(year));
			calendar.set(Calendar.MONTH, Integer.parseInt(month));
			calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day) - 1);
			Date time = calendar.getTime();
			String date = DateUtil.date2String(time, DateUtil.PATTERN_DATE2);
			periods2 = date + "001";
		} else if (subPeriods.equals("180") && lottId.equals(LotteryConstant.XYFT)) {
			// 幸运飞艇隔天第一期
			String year = periods.substring(0, 4);
			String month = periods.substring(4, 6);
			String day = periods.substring(6, 8);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, Integer.parseInt(year));
			calendar.set(Calendar.MONTH, Integer.parseInt(month));
			calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day) - 1);
			Date time = calendar.getTime();
			String date = DateUtil.date2String(time, DateUtil.PATTERN_DATE2);
			periods2 = date + "001";
		}
		return periods2;
	}

	/**
	 * @Description :处理计划数据,支持时时彩以及幸运飞艇、北京赛车
	 * @author 老王
	 *         <p>
	 */
	// public static Map<String, Object> genPlan(String periods, int count,
	// String lottId) {
	// Map<String, Object> result = new HashMap<>();
	// String subPeriods = periods.substring(periods.length() - 3,
	// periods.length());
	// String prePeriods = periods.substring(0, 2);
	// result.put("periods", Long.parseLong(periods) + 1 + "");
	// if (subPeriods.equals("120") && lottId.equals(LotteryConstant.CQSSC)) {
	// // 重庆时时彩隔天第一期
	// String year = periods.substring(0, 4);
	// String month = periods.substring(4, 6);
	// String day = periods.substring(6, 8);
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(Calendar.YEAR, Integer.parseInt(year));
	// calendar.set(Calendar.MONTH, Integer.parseInt(month));
	// calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day) + 1);
	// Date time = calendar.getTime();
	// String date = DateUtil.date2String(time, DateUtil.PATTERN_DATE2);
	// result.put("periods", date + "001");
	// } else if (subPeriods.equals("180") &&
	// lottId.equals(LotteryConstant.XYFT)) {
	// // 幸运飞艇隔天第一期
	// String year = periods.substring(0, 4);
	// String month = periods.substring(4, 6);
	// String day = periods.substring(6, 8);
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(Calendar.YEAR, Integer.parseInt(year));
	// calendar.set(Calendar.MONTH, Integer.parseInt(month));
	// calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day) + 1);
	// Date time = calendar.getTime();
	// String date = DateUtil.date2String(time, DateUtil.PATTERN_DATE2);
	// result.put("periods", date + "001");
	// }
	//
	// int[] num = getRandomArrayByIndex(3, count);
	// String[] str = new String[3];
	// for (int i = 0; i < num.length; i++) {
	// int type = new java.util.Random().nextInt(2);// 1:longhu,2:daxiao
	// // 3:danshuang
	// if ((num[i] < 5 && count == 10) || num[i] == 1 && count == 5) {
	// type = new java.util.Random().nextInt(3);// 1:longhu,2:daxiao
	// // 3:danshuang
	// }
	// str[i] = getRandomByType(type);
	// }
	// result.put("plan1", num[0] + "," + str[0]);
	// result.put("plan2", num[1] + "," + str[1]);
	// result.put("plan3", num[2] + "," + str[2]);
	// return result;
	// }

	public static Map<String, Object> genPlan(String tjperiods, String lottId, Object obj) {
		if (obj != null) {			
			JSONObject newTuijian = JSON.parseObject(obj.toString());
			JSONArray wzdxArr = newTuijian.getJSONArray("wzdx");
			JSONArray wzdsArr = newTuijian.getJSONArray("wzds");
			List<Object> allarr = new ArrayList<Object>();
			if (wzdxArr != null && wzdxArr.size() > 0) {
				Iterator<Object> it = wzdxArr.iterator();
				while (it.hasNext()) {
					Object o = it.next();
					allarr.add(o);
				}
			}
			if (wzdsArr != null && wzdsArr.size() > 0) {
				Iterator<Object> it = wzdsArr.iterator();
				while (it.hasNext()) {
					Object o = it.next();
					allarr.add(o);
				}
			}
			// 随机获取三组不同数据
			int len = allarr.size();
			List<Object> tuijianList = new ArrayList<Object>();
			Object tj = null;
			for (int i = 0; i < 3; i++) {
				do {
					tj = allarr.get(NumberUtils.randomInt(0, len));
				} while (tuijianList.contains(tj));
				tuijianList.add(tj);
			}

			// 生成返回的推荐结果数据
			Map<String, Object> tuijianResult = new HashMap<>();
			tuijianResult.put("periods", tjperiods);
			int planIndex = 1;
			for (Object o : tuijianList) {
				JSONObject json = JSON.parseObject(o.toString());
				String num = json.getString("num");
				if (StringUtils.isNotBlank(num)) {
					num = num.substring(3);
				}
				String tjnum = json.getString("tjnum");
				// 将推荐数据添加到map
				tuijianResult.put("plan" + planIndex, num + "," + tjnum);
				planIndex++;
			}
			return tuijianResult;
		}
		return null;
	}
	
	
	/**
	 * @author xg
	 * 构建推荐数据
	 * @param map
	 * @return
	 */
	public static Map changeData(Map<Object, Object> map) {
		  List<Entry<Object, Object>> list = new ArrayList<Map.Entry<Object, Object>>(map.entrySet());
		  Collections.sort(list, new Comparator<Map.Entry<Object, Object>>() {
		      public int compare(Map.Entry<Object, Object> o1, Map.Entry<Object, Object> o2) {
		          return (o2.getKey()).toString().compareTo(o1.getKey().toString());
		      }
		  });
		  int a0 = 0 ;
		  int a1 = 0 ;
		  int a2 = 0 ;
		  for (Entry<Object, Object> entry : list) {
			   List list2 = (List) JSONUtils.parse(entry.getValue().toString());
			   int shuying0 = Integer.parseInt(((Map)list2.get(0)).get("shuying").toString());
			   if (shuying0==1) {
				   a0+=shuying0;
			   }
			   int shuying1 = Integer.parseInt(((Map)list2.get(1)).get("shuying").toString());
			   if (shuying1==1) {
				   a1+=shuying1;
			   }
			   int shuying2 = Integer.parseInt(((Map)list2.get(2)).get("shuying").toString());
			   if (shuying2==1) {
				   a2+=shuying2;
			   }
		  }
		  HashMap<String, Object> hashMap = new HashMap<String,Object>();
		  hashMap.put("a0", a0);
		  hashMap.put("a1", a1);
		  hashMap.put("a2", a2);
		  hashMap.put("list", list);
		return hashMap;
	}
	

}
