package io.lottery.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.lottery.common.config.LotteryDict;
import io.lottery.common.config.RedisService;
import io.lottery.common.utils.DateUtils;
import io.lottery.common.utils.LiuheUtils;
import io.lottery.service.LiuheService;

/**
 * 六合彩处理类
 * 
 * @desc
 * @author xg
 * @author 2018年9月12日
 */
@Service
public class LiuheServiceImpl implements LiuheService {

	@Autowired
	private RedisService redisservice;

	@Override
	public Map<Object, Object> sjkjjl(String date) {
		String year = null;
		if (StringUtils.isNotBlank(date)) {
			year = date.substring(0, 4);
		} else {
			Calendar cal = Calendar.getInstance();
			year = cal.get(Calendar.YEAR) + "";
		}
		return getKjjlData(year);
	}

	@Override
	public Object kjjl(String type, String year) {
		// 先获取一年的数据
		Map<Object, Object> map = getKjjlData(year);
		JSONArray array = new JSONArray();
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			JSONObject obj = JSON.parseObject(entry.getValue() == null ? "" : entry.getValue() + "");
			JSONObject o = new JSONObject();

			JSONArray ja = obj.getJSONArray("rank");
			String preDrawCode = "";
			// 特码
			String tema = String.valueOf(ja.get(6));
			for (Object jo : ja) {
				preDrawCode += "," + String.valueOf(jo);
			}
			if (StringUtils.isNotBlank(preDrawCode) && preDrawCode.startsWith(",")) {
				preDrawCode = preDrawCode.substring(1);
			}
			o.put("preDrawCode", preDrawCode);// 号码排序

			// 开奖时间
			Long starttime = obj.getLong("starttime");
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(starttime);
			Date date = cal.getTime();
			o.put("preDrawDate", DateUtils.format(date, DateUtils.DATE_PATTERN));

			o.put("czAndFeSeven", "");

			// 总数
			o.put("sumTotal", obj.get("zhsum"));
			// 总和大小
			String zhdx = obj.getString("zhdx");
			o.put("totalBigSmall", zhdx.indexOf("小") > -1 ? 1 : 0);
			// 总和单双
			String zhds = obj.getString("zhds");
			o.put("totalSingleDouble", zhds.indexOf("单") > -1 ? 0 : 1);

			// 特码单双
			String tmds = obj.getString("tmds");
			o.put("seventhSingleDouble", tmds.indexOf("单") > -1 ? 0 : 1);
			// 特码大小
			String tmdx = obj.getString("tmdx");
			o.put("seventhBigSmall", tmdx.indexOf("小") > -1 ? 1 : 0);
			// 特码计算
			if (tema.length() == 1) {
				tema += "0" + tema;
			}
			String[] temaArr = tema.split("");
			Integer gewei = Integer.valueOf(temaArr[1]);
			Integer shiwei = Integer.valueOf(temaArr[0]);
			Integer heshu = gewei + shiwei;
			// 特码尾数大小
			o.put("seventhMantissaBig", gewei > 4 ? 0 : 1);
			// 合数大小
			o.put("seventhCompositeBig", heshu > 7 ? 0 : 1);
			// 特码合数单双
			o.put("seventhCompositeDouble", heshu % 2 == 0 ? 1 : 0);

			// 期数
			o.put("issue", entry.getKey());

			// 计算七色波
			Map<String, Integer> bosemap = new HashMap<String, Integer>();
			bosemap.put("red", 0);
			bosemap.put("blue", 0);
			bosemap.put("green", 0);

			// 球的颜色
			List<Integer> color = new ArrayList<Integer>();
			JSONArray colorArr = obj.getJSONArray("boserank");
			String temaColor = LiuheUtils.getBoSe(tema);
			for (Object ca : colorArr) {
				String caStr = String.valueOf(ca);
				color.add(LiuheUtils.getBoSeIndex(caStr));
				// 波色统计
				if ("red".equals(caStr)) {
					bosemap.put("red", bosemap.get("red") + 1);
				} else if ("blue".equals(caStr)) {
					bosemap.put("blue", bosemap.get("blue") + 1);
				} else if ("green".equals(caStr)) {
					bosemap.put("green", bosemap.get("green") + 1);
				}
			}
			o.put("color", color);

			// 降序比较器
			Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String, Integer>>() {
				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return o2.getValue() - o1.getValue();
				}
			};

			// map转换成list进行排序
			List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(bosemap.entrySet());
			// 排序
			Collections.sort(list, valueComparator);

			// 计算前两个的值，看是否相等
			List<String> colorList = new ArrayList<String>();
			List<Integer> numList = new ArrayList<Integer>();
			// 默认情况下，TreeMap对key进行升序排序
			for (Map.Entry<String, Integer> entry2 : list) {
				colorList.add(entry2.getKey());
				numList.add(entry2.getValue());
			}

			String nanairo = "";
			if (numList.get(0) == numList.get(1)) {// 判断两种相等的情况
				if (temaColor.equals(colorList.get(0)) || temaColor.equals(colorList.get(1))) {
					nanairo = temaColor;
				} else {
					nanairo = "和局";
				}
			} else {
				nanairo = colorList.get(0);
			}
			// 换算成具体的七色波颜色
			o.put("nanairo", LiuheUtils.getBoSeNanairoIndex(nanairo));

			// 该类型下的对应数值
			List<Integer> czAndFe = new ArrayList<Integer>();
			switch (Integer.valueOf(type)) {
			case 1:// 生肖
				JSONArray arr = obj.getJSONArray("sxrank");
				for (Object ca : arr) {
					czAndFe.add(LiuheUtils.getSxIndex(String.valueOf(ca)));
				}
				break;
			case 2:// 五行
				JSONArray arr2 = obj.getJSONArray("wxrank");
				for (Object ca : arr2) {
					czAndFe.add(LiuheUtils.getWxIndex(String.valueOf(ca)));
				}
				break;
			case 3:// 家禽野兽
				JSONArray jqysrank = obj.getJSONArray("jqysrank");
				for (Object ca : jqysrank) {
					czAndFe.add(LiuheUtils.getJqysIndex(String.valueOf(ca)));
				}
				break;
			case 4:// 男女生肖
				JSONArray nnsxrank = obj.getJSONArray("nnsxrank");
				for (Object ca : nnsxrank) {
					czAndFe.add(LiuheUtils.getNnsxIndex(String.valueOf(ca)));
				}
				break;
			case 5:// 天地生肖
				JSONArray tdsxrank = obj.getJSONArray("tdsxrank");
				for (Object ca : tdsxrank) {
					czAndFe.add(LiuheUtils.getTdsxIndex(String.valueOf(ca)));
				}
				break;
			case 6:// 四级生肖
				JSONArray sjsxrank = obj.getJSONArray("sjsxrank");
				for (Object ca : sjsxrank) {
					czAndFe.add(LiuheUtils.getSjsxIndex(String.valueOf(ca)));
				}
				break;
			case 7:// 琴棋书画
				JSONArray qqshrank = obj.getJSONArray("qqshrank");
				for (Object ca : qqshrank) {
					czAndFe.add(LiuheUtils.getQqshIndex(String.valueOf(ca)));
				}
				break;
			case 8:// 三色生肖
				JSONArray sssxrank = obj.getJSONArray("sssxrank");
				for (Object ca : sssxrank) {
					czAndFe.add(LiuheUtils.getSssxIndex(String.valueOf(ca)));
				}
				break;
			}
			o.put("czAndFe", czAndFe);

			array.add(o);
		}

		return array;
	}

	/**
	 * 获取开奖数据
	 * 
	 * @desc
	 * @author xg
	 * @param year
	 * @return
	 * @author 2018年9月13日
	 */
	public Map<Object, Object> getKjjlData(String year) {
		Map<Object, Object> json = redisservice.getHashMaps(LotteryDict.LHC + "_" + LotteryDict.kjjl + "_" + year);
		Map<Object, Object> treeMap = new TreeMap<Object, Object>(new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				return o2.toString().compareTo(o1.toString());
			}
		});
		treeMap.putAll(json);
		return treeMap;
	}

	public static void main(String[] args) {
		String[] numlist = { "48", "49", "17", "14", "30", "46", "36" };
		List<String> boselist = new ArrayList<String>();
		for (String str : numlist) {
			boselist.add(LiuheUtils.getBoSe(str));
		}

		String temaColor = LiuheUtils.getBoSe(numlist[6]);
		// 计算七色波
		Map<String, Integer> bosemap = new HashMap<String, Integer>();
		bosemap.put("red", 0);
		bosemap.put("blue", 0);
		bosemap.put("green", 0);

		// 球的颜色
		List<Integer> color = new ArrayList<Integer>();
		for (Object ca : boselist) {
			String caStr = String.valueOf(ca);
			color.add(LiuheUtils.getBoSeIndex(caStr));
			// 波色统计
			if ("red".equals(caStr)) {
				bosemap.put("red", bosemap.get("red") + 1);
			} else if ("blue".equals(caStr)) {
				bosemap.put("blue", bosemap.get("blue") + 1);
			} else if ("green".equals(caStr)) {
				bosemap.put("green", bosemap.get("green") + 1);
			}
		}

		// 计算排序值
		// TreeMap<String, Integer> bosemap2 = new TreeMap<String, Integer>(new
		// Comparator<String>() {
		// @Override
		// public int compare(String o1, String o2) {
		// return o2.compareTo(o1);
		// }
		//
		// });
		// Map<String, Integer> bosemap2 = new TreeMap<String, Integer>();

		// 升序比较器
		Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}
		};

		// map转换成list进行排序
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(bosemap.entrySet());
		// 排序
		Collections.sort(list, valueComparator);

		// 计算前两个的值，看是否相等
		List<String> colorList = new ArrayList<String>();
		List<Integer> numList = new ArrayList<Integer>();
		// 默认情况下，TreeMap对key进行升序排序
		System.out.println("------------map按照value升序排序--------------------");
		for (Map.Entry<String, Integer> entry : list) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
			// bosemap2.put(entry.getKey(), entry.getValue());
			colorList.add(entry.getKey());
			numList.add(entry.getValue());

		}

		String nanairo = "";
		if (numList.get(0) == numList.get(1)) {// 判断两种相等的情况
			if (temaColor.equals(colorList.get(0)) || temaColor.equals(colorList.get(1))) {
				nanairo = temaColor;
			} else {
				nanairo = "和局";
			}
		} else {
			nanairo = colorList.get(0);
		}

		Integer nanairo2 = LiuheUtils.getBoSeNanairoIndex(nanairo);
		System.out.println(nanairo);
		System.out.println(nanairo2);

	}

}
