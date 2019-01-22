package io.lottery.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import io.lottery.service.AppApiChangeDataService;
import io.lottery.util.AppChangeText;

/**
 * 
 * @desc app数据格式转换service
 * @author abo
 * @date 2018年8月7日 下午12:33:59
 *
 */
@Service
public class AppApiChangeDataServiceImpl implements AppApiChangeDataService {

	/**
	 * 
	 * @desc 修改走势图数据
	 * @author abo
	 * @date 2018年8月7日 上午11:08:14
	 * @param data
	 * @return
	 */
	@Override
	public JSONArray changeZstData(String data, String type) {
		Map<String, Object> map = null;
		JSONObject temp = null;
		JSONObject temp1 = null;
		// 因为冠亚和只有一个球，所以单独处理一下。这里默认
		if (type.equals("gyhzst")) {
			temp = new JSONObject();
			map = JSONObject.parseObject(data, Feature.OrderedField);
			temp.put("1", map);
			map = temp;
		} else {
			map = JSONObject.parseObject(data, Feature.OrderedField);
		}
		JSONArray ja = new JSONArray();// 第一次数组
		JSONArray ja1 = new JSONArray();// 第二层数组

		for (Map.Entry<String, Object> entity : map.entrySet()) {
			temp = new JSONObject();
			ja1 = new JSONArray();
			Map<String, Object> m = (Map<String, Object>) entity.getValue();
			for (Map.Entry<String, Object> e : m.entrySet()) {
				temp1 = new JSONObject();
				temp1.put("qishu", e.getKey());
				temp1.put("ball", e.getValue());
				ja1.add(temp1);
			}
			temp.put("num", entity.getKey());
			temp.put("balls", ja1);
			ja.add(temp);
		}
		return ja;
	}

	/**
	 * 
	 * @desc 修改开奖记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:15:58
	 * @param data
	 * @return
	 */
	@Override
	public JSONArray changeKjjlData(JSONArray data, String lottId) {
		JSONArray ja = new JSONArray();
		Map<String, Object> m = null;
		JSONObject ob = null;
		String gyhDs = null;
		String lh1 = null;
		String lh2 = null;
		String lh3 = null;
		String lh4 = null;
		String lh5 = null;
		String gyhDx = null;
		String gyhSum = null;
		// 修改北京赛车和幸运飞艇
		if (lottId.equals("bjsc") || lottId.equals("xyft")) {
			for (int i = 0; i < data.size(); i++) {
				m = data.getJSONObject(i);
				for (Map.Entry<String, Object> map : m.entrySet()) {
					ob = (JSONObject) map.getValue();
					gyhDs = ob.getString("gyhDs");
					lh1 = ob.getString("lh1");
					lh2 = ob.getString("lh2");
					lh3 = ob.getString("lh3");
					lh4 = ob.getString("lh4");
					lh5 = ob.getString("lh5");
					gyhDx = ob.getString("gyhDx");
					gyhSum = ob.getString("gyhSum");
					// 删除不要的对象
					ob.remove("lh1");
					ob.remove("lh2");
					ob.remove("lh3");
					ob.remove("lh4");
					ob.remove("lh5");
					ob.remove("gyhDs");
					ob.remove("gyhSum");
					ob.remove("gyhDx");
					// 重新封装新的对象
					String[] tempArray = { gyhSum, gyhDx, gyhDs, lh1, lh2, lh3, lh4, lh5 };
					ob.put("balls", tempArray);
					ja.add(ob);
				}
			}
		} else if (lottId.equals("cqssc") || lottId.equals("xjssc") || lottId.equals("tjssc")) {// 三种时时彩
			for (int i = 0; i < data.size(); i++) {
				m = data.getJSONObject(i);
				for (Map.Entry<String, Object> map : m.entrySet()) {
					ob = (JSONObject) map.getValue();
					lh1 = ob.getString("lh1");
					// 前三
					lh2 = ob.getString("qs");
					// 中三
					lh3 = ob.getString("zs");
					// 后三
					lh4 = ob.getString("hs");
					gyhDx = ob.getString("zhDx");
					gyhDs = ob.getString("zhDs");
					gyhSum = ob.getString("gyhSum");
					ob.remove("gyhSum");
					ob.remove("zhDs");
					ob.remove("zhDx");
					ob.remove("lh1");
					ob.remove("qs");
					ob.remove("zs");
					ob.remove("hs");
					// 重新封装新的对象
					String[] tempArray = { gyhSum, gyhDs, gyhDx, lh1, lh2, lh3, lh4 };
					ob.put("balls", tempArray);
					ja.add(ob);
				}
			}
		} else if (lottId.equals("gdklsf")) {// 广东快乐十分|| lottId.equals("xync")
			for (int i = 0; i < data.size(); i++) {
				m = data.getJSONObject(i);
				for (Map.Entry<String, Object> map : m.entrySet()) {
					ob = (JSONObject) map.getValue();
					lh1 = ob.getString("lh1");
					lh2 = ob.getString("lh2");
					lh3 = ob.getString("lh3");
					lh4 = ob.getString("lh4");
					lh5 = ob.getString("wDx");
					gyhDx = ob.getString("sumDx");
					gyhDs = ob.getString("sumDs");
					gyhSum = ob.getString("sum");
					ob.remove("sum");
					ob.remove("sumDs");
					ob.remove("sumDx");
					ob.remove("wDx");
					ob.remove("lh1");
					ob.remove("lh2");
					ob.remove("lh3");
					ob.remove("lh4");
					// 重新封装新的对象
					String[] tempArray = { gyhSum, gyhDs, gyhDx, lh5, lh1, lh2, lh3, lh4 };
					ob.put("balls", tempArray);
					ja.add(ob);
				}
			}
		} else if (lottId.equals("xync")) {// 幸运农场
			for (int i = 0; i < data.size(); i++) {
				m = data.getJSONObject(i);
				for (Map.Entry<String, Object> map : m.entrySet()) {
					ob = (JSONObject) map.getValue();
					lh1 = ob.getString("lh1");
					lh5 = ob.getString("wDx");
					gyhDx = ob.getString("zhDx");
					gyhDs = ob.getString("zhDs");
					gyhSum = ob.getString("gyhSum");
					ob.remove("gyhSum");
					ob.remove("zhDs");
					ob.remove("zhDx");
					ob.remove("wDx");
					ob.remove("lh1");
					// 重新封装新的对象
					String[] tempArray = { gyhSum, gyhDs, gyhDx, lh5, lh1 };
					ob.put("balls", tempArray);
					ja.add(ob);
				}
			}
		} else if (lottId.equals("gd11x5") || lottId.equals("jx11x5")) {// 广东11选5，江西11选5
			for (int i = 0; i < data.size(); i++) {
				m = data.getJSONObject(i);
				for (Map.Entry<String, Object> map : m.entrySet()) {
					ob = (JSONObject) map.getValue();
					lh1 = ob.getString("lh1");
					lh2 = ob.getString("qs");
					lh3 = ob.getString("zs");
					lh4 = ob.getString("hs");
					gyhDx = ob.getString("zhDx");
					gyhDs = ob.getString("zhDs");
					gyhSum = ob.getString("gyhSum");
					ob.remove("gyhSum");
					ob.remove("zhDs");
					ob.remove("zhDx");
					ob.remove("wDx");
					ob.remove("lh1");
					ob.remove("qs");
					ob.remove("zs");
					ob.remove("hs");
					// 重新封装新的对象
					String[] tempArray = { gyhSum, gyhDs, gyhDx, lh1, lh2, lh3, lh4 };
					ob.put("balls", tempArray);
					ja.add(ob);
				}
			}
		} else if (lottId.equals("kl8")) {// 快乐8
			for (int i = 0; i < data.size(); i++) {
				m = data.getJSONObject(i);
				for (Map.Entry<String, Object> map : m.entrySet()) {
					ob = (JSONObject) map.getValue();
					lh1 = ob.getString("sx");
					lh2 = ob.getString("wx");
					lh4 = ob.getString("fp");
					gyhDx = ob.getString("zhDx");
					gyhDs = ob.getString("zhDs");
					gyhSum = ob.getString("zhSum");
					ob.remove("zhSum");
					ob.remove("zhDs");
					ob.remove("zhDx");
					ob.remove("wx");
					ob.remove("sx");
					ob.remove("fp");
					// 重新封装新的对象
					String[] tempArray = { lh4, gyhSum, gyhDs, gyhDx, lh1, lh2 };
					ob.put("balls", tempArray);
					ja.add(ob);
				}
			}
		} else if (lottId.equals("jsk3")) {// 江苏快3
			for (int i = 0; i < data.size(); i++) {
				m = data.getJSONObject(i);
				for (Map.Entry<String, Object> map : m.entrySet()) {
					ob = (JSONObject) map.getValue();
					gyhDx = ob.getString("zhDx");
					gyhSum = ob.getString("zhSum");
					ob.remove("zhSum");
					ob.remove("zhDx");
					// 重新封装新的对象
					String[] tempArray = { gyhSum, gyhDx };
					ob.put("balls", tempArray);
					ja.add(ob);
				}
			}
		} else if (lottId.equals("pcdd")) {// pc蛋蛋
			for (int i = 0; i < data.size(); i++) {
				m = data.getJSONObject(i);
				for (Map.Entry<String, Object> map : m.entrySet()) {
					ob = (JSONObject) map.getValue();
					String zhDs = ob.getString("zhDs");
					String zhDx = ob.getString("zhDx");
					String zhDxDs = ob.getString("zhDxds");
					int zhSum = ob.getInteger("zhSum");
					JSONArray array = ob.getJSONArray("rank");
					array.add(zhSum);

					ob.remove("zhDs");
					ob.remove("zhDx");
					ob.remove("zhDxds");
					ob.remove("zhJdx");
					ob.remove("zhSum");
					// 重新封装新的对象
					String[] tempArray = { zhDs, zhDx, zhDxDs };
					ob.put("balls", tempArray);
					ja.add(ob);
				}
			}
		} else if (lottId.equals("lhc")) {// 六合彩
			for (int i = 0; i < data.size(); i++) {
				m = data.getJSONObject(i);
				for (Map.Entry<String, Object> map : m.entrySet()) {
					ob = (JSONObject) map.getValue();
					String qishu = map.getKey();
					ob.put("periods", qishu);

//					ob.remove("zhds");
//					ob.remove("zhdx");
//					ob.remove("tmdx");
//					ob.remove("tmds");
//					ob.remove("zhsum");

					ja.add(ob);
				}
			}
		}
		return ja;
	}

	@Override
	public JSONArray changeWzylHmylData(String data, String code) {
		Map<String, Object> map = JSONObject.parseObject(data);
		// System.out.println(map);
		JSONObject json = null;
		JSONObject json1 = new JSONObject();
		JSONObject json2 = null;
		JSONArray ja = new JSONArray();
		JSONArray ja1 = null;
		Map<Integer, Object> mm = new TreeMap<>();
		for (Entry<String, Object> temp : map.entrySet()) {
			ja1 = new JSONArray();
			json = new JSONObject();
			// System.out.println(temp.getKey() + "=====" + temp.getValue());
			// 位置遗漏龙虎、冠亚和例外
			if (code.contains("wzyl_wzlh") || code.contains("wzyl_wzgyhdx") || code.contains("wzyl_wzgyhds")) {
				json1 = (JSONObject) temp.getValue();
				int yrkc = json1.getInteger("jrcx");
				json1.put("ylsy", json1.get("ylsy"));
				json1.put("jrkc", yrkc);
				json1.put("jryl", json1.get("jryl"));
				json1.put("num", temp.getKey());
				json1.remove("byyl");
				json1.remove("bzyl");
				json1.remove("jrcx");
				ja1.add(json1);
			} else {
				Map<String, Object> m = (Map<String, Object>) temp.getValue();
				for (Map.Entry<String, Object> e : m.entrySet()) {
					json1 = new JSONObject();
					json2 = (JSONObject) e.getValue();
					json1.put("num", e.getKey());
					json1.put("ylsy", json2.get("ylsy"));
					json1.put("jrkc", json2.get("jrcx"));
					json1.put("jryl", json2.get("jryl"));
					ja1.add(json1);
				}
			}
			json.put("num", temp.getKey());
			json.put("item", ja1);
			if (code.contains("wzyl_wzlh") || code.contains("wzyl_wzgyhdx") || code.contains("wzyl_wzgyhds")
					|| code.contains("jsk3")) {
				ja.add(json);
			} else {
				String d = temp.getKey().replace("num", "");
				mm.put(Integer.parseInt(d), json);
			}
		}
		// 对非龙虎、总和大小、总和单双重新排序
		if (code.contains("wzyl_wzlh") || code.contains("wzyl_wzgyhdx") || code.contains("wzyl_wzgyhds")) {
			return ja;
		} else {
			for (Map.Entry<Integer, Object> e : mm.entrySet()) {
				ja.add(e.getValue());
			}
		}
		// System.out.println(ja);
		return ja;
	}

	/**
	 * 
	 * @desc 修改单双路珠记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:19:00
	 * @param data
	 * @return
	 */
	@Override
	public JSONArray changeDslzData(String data, String code) {
		Map<String, Object> map = JSONObject.parseObject(data);
		JSONObject jo = null;
		Map<String, Object> m = new HashMap<>();
		for (Entry<String, Object> temp : map.entrySet()) {
			String[] array = temp.getKey().split("-");
			jo = (JSONObject) m.get(array[0]);
			if (jo == null) {
				jo = new JSONObject();
			}
			m.put(array[0], jo);
			if (array.length > 1) {
				jo.put(array[1], temp.getValue());
			} else {
				jo.put("item", temp.getValue());
			}
			jo.put("num", array[0]);
			m.put(array[0], jo);
		}
		JSONArray ja = new JSONArray();
		Map<Object, Object> mm = new TreeMap<Object, Object>();
		for (Entry<String, Object> temp : m.entrySet()) {
			JSONObject jsono = (JSONObject) temp.getValue();
			int key = Integer.parseInt(jsono.getString("num").replace("num", ""));
			mm.put(key, jsono);
		}
		// 重新排序
		for (Entry<Object, Object> temp : mm.entrySet()) {
			Map<Object, Object> mmm = (Map<Object, Object>) temp.getValue();
			Map<Object, Object> jo1 = AppChangeText.changeWord(mmm, code);
			ja.add(jo1);
		}
		return ja;
	}

	@Override
	public JSONArray changeDsDxLsData(JSONArray data) {
		JSONArray ja = new JSONArray();
		JSONArray ja1 = new JSONArray();
		JSONArray ja2 = new JSONArray();
		JSONArray dxJa = new JSONArray();
		JSONArray dsJa = new JSONArray();
		Map<String, Object> m = null;
		String date = null;
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject1 = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		JSONObject jsonObject3 = new JSONObject();
		JSONObject dxJsonObject = new JSONObject();
		JSONObject dsJsonObject = new JSONObject();
		Map<String, Object> mm = null;
		Map<Integer, Object> mapSortdx = null;
		Map<Integer, Object> mapSortds = null;
		// 遍历数据，先封装到map
		for (int i = 0; i < data.size(); i++) {
			m = data.getJSONObject(i);
			for (Map.Entry<String, Object> temp : m.entrySet()) {
				date = temp.getKey();
				dxJa = new JSONArray();
				dsJa = new JSONArray();
				jsonObject = new JSONObject();
				jsonObject1 = new JSONObject();
				jsonObject2 = new JSONObject();
				jsonObject3 = new JSONObject();
				mm = (Map<String, Object>) temp.getValue();
				mapSortdx = new TreeMap<Integer, Object>();
				mapSortds = new TreeMap<Integer, Object>();
				for (Map.Entry<String, Object> tt : mm.entrySet()) {
					dxJsonObject = new JSONObject();
					dsJsonObject = new JSONObject();
					JSONObject t = (JSONObject) tt.getValue();
					dxJsonObject.put("da", t.get("da"));
					dxJsonObject.put("xiao", t.get("xiao"));
					// 放入map排序
					mapSortdx.put(Integer.parseInt(tt.getKey().replace("num", "")), dxJsonObject);
					dsJsonObject.put("dan", t.get("dan"));
					dsJsonObject.put("shuang", t.get("shuang"));
					// 放入map排序
					mapSortds.put(Integer.parseInt(tt.getKey().replace("num", "")), dsJsonObject);
				}
				for (Entry<Integer, Object> object : mapSortds.entrySet()) {
					dsJa.add(object.getValue());
				}
				for (Entry<Integer, Object> object : mapSortdx.entrySet()) {
					dxJa.add(object.getValue());
				}
				jsonObject1.put("date", date);
				jsonObject1.put("num", dxJa);
				ja1.add(jsonObject1);
				jsonObject3.put("date", date);
				jsonObject3.put("num", dsJa);
				ja2.add(jsonObject3);
			}
		}
		jsonObject.put("daxiao", ja1);
		jsonObject2.put("danshuang", ja2);
		ja.add(jsonObject2);
		ja.add(jsonObject);
		return ja;
	}

	/**
	 * 
	 * @desc 修改龙虎历史记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:22:11
	 * @param data
	 * @return
	 */
	@Override
	public JSONArray changeLhlsData(JSONArray data) {
		JSONArray ja = new JSONArray();
		JSONArray ja1 = null;
		Map<String, Object> m = null;
		String date = null;
		JSONObject jsonObject = null;
		JSONObject jsonObject1 = null;
		// 遍历数据，先封装到map
		for (int i = 0; i < data.size(); i++) {
			m = data.getJSONObject(i);
			jsonObject = new JSONObject();
			ja1 = new JSONArray();
			for (Map.Entry<String, Object> temp : m.entrySet()) {
				date = temp.getKey();
				JSONObject json = (JSONObject) temp.getValue();
				jsonObject1 = new JSONObject();
				jsonObject1.put("long", json.get("num1_龙"));
				jsonObject1.put("hu", json.get("num1_虎"));
				ja1.add(jsonObject1);
				jsonObject1 = new JSONObject();
				jsonObject1.put("long", json.get("num2_龙"));
				jsonObject1.put("hu", json.get("num2_虎"));
				ja1.add(jsonObject1);
				jsonObject1 = new JSONObject();
				jsonObject1.put("long", json.get("num3_龙"));
				jsonObject1.put("hu", json.get("num3_虎"));
				ja1.add(jsonObject1);
				jsonObject1 = new JSONObject();
				jsonObject1.put("long", json.get("num4_龙"));
				jsonObject1.put("hu", json.get("num4_虎"));
				ja1.add(jsonObject1);
				jsonObject1 = new JSONObject();
				jsonObject1.put("long", json.get("num5_龙"));
				jsonObject1.put("hu", json.get("num5_虎"));
				ja1.add(jsonObject1);
				jsonObject.put("num", ja1);
				jsonObject.put("date", date);
			}
			ja.add(jsonObject);
		}
		return ja;
	}

	@Override
	public JSONArray changeGyhlsData(JSONArray data) {
		System.out.println(data);
		JSONArray ja = new JSONArray();
		Map<String, Object> m = null;
		JSONObject json = null;
		JSONObject json1 = null;
		for (int i = 0; i < data.size(); i++) {
			m = data.getJSONObject(i);
			for (Map.Entry<String, Object> temp : m.entrySet()) {
				json = new JSONObject();
				String date = temp.getKey();
				json1 = (JSONObject) temp.getValue();
				json.put("date", date);
				json.put("num", json1);
				ja.add(json);
			}
		}
		return ja;
	}

	@Override
	public JSONArray changeGyhylData(String data) {
		Map<String, Object> map = JSONObject.parseObject(data);
		JSONObject json = null;
		JSONObject json1 = new JSONObject();
		JSONArray ja = new JSONArray();
		JSONArray ja1 = null;
		for (Entry<String, Object> temp : map.entrySet()) {
			ja1 = new JSONArray();
			json = new JSONObject();
			json1 = (JSONObject) temp.getValue();
			int yrkc = json1.getInteger("jrcx");
			json1.put("ylsy", json1.get("ylsy"));
			json1.put("jrkc", yrkc);
			json1.put("jryl", json1.get("jryl"));
			json1.remove("byyl");
			json1.remove("bzyl");
			json1.remove("jrcx");
			json1.put("gyh", temp.getKey());
			ja1.add(json1);
			json.put("num", temp.getKey());
			json.put("item", ja1);
			ja.add(json);
		}
		System.out.println(ja);
		return ja;
	}

	@Override
	public JSONArray changeQhlzData(String data, String code) {
		JSONObject json = JSONObject.parseObject(data);
		Map<String, Object> sxplzMap = (Map<String, Object>) json.get("sxplz");
		Map<String, Object> jrljMap = (Map<String, Object>) json.get("jrlj");
		Map<Integer, Object> map1 = new TreeMap<Integer, Object>();
		Map<String, Object> maptemp = new HashMap<>();
		JSONObject json1 = null;
		JSONArray ja = new JSONArray();
		JSONArray ja1 = null;
		for (Entry<String, Object> temp : sxplzMap.entrySet()) {
			ja1 = new JSONArray();
			maptemp = (Map<String, Object>) temp.getValue();
			// 转成treemap进行排序
			for (Entry<String, Object> t : maptemp.entrySet()) {
				map1.put(Integer.parseInt(t.getKey()), t.getValue());
			}
			// 然后重新封装jsonarray
			for (Entry<Integer, Object> t : map1.entrySet()) {
				ja1.add(t.getValue());
			}
			json1 = (JSONObject) jrljMap.get(temp.getKey());
			json1.put("qian", json1.get(temp.getKey() + "_q"));
			json1.put("hou", json1.get(temp.getKey() + "_h"));
			json1.put("num", temp.getKey().replace("_", ""));
			json1.put("item", ja1);
			json1.remove(temp.getKey() + "_h");
			json1.remove(temp.getKey() + "_q");

			ja.add(json1);
		}
		Map<Object, Object> mm = new TreeMap<Object, Object>();
		JSONArray ja2 = new JSONArray();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject js = (JSONObject) ja.get(i);
			System.out.println(js);
			int s = Integer.parseInt(js.get("num").toString().replace("num", ""));
			mm.put(s, js);
		}
		// for (Entry<Object, Object> temp : mm.entrySet()) {
		// ja2.add(temp.getValue());
		// }
		// 重新排序
		for (Entry<Object, Object> temp : mm.entrySet()) {
			Map<Object, Object> mmm = (Map<Object, Object>) temp.getValue();
			Map<Object, Object> jo1 = AppChangeText.changeWord(mmm, code);
			ja2.add(jo1);
		}
		return ja2;
	}

	@Override
	public JSONObject changeLmlzckData(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @desc 修改两面投注参考记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:28:50
	 * @param data
	 * @param lottId
	 * @return
	 */
	@Override
	public JSONArray changeLmtzckData(JSONArray data, String lottId) {
		JSONArray ja = new JSONArray();
		JSONArray ja1 = new JSONArray();
		Map<String, Object> m = null;
		Map<String, Object> ob = null;
		String date = null;
		JSONObject jsonObject = null;
		JSONObject jsonObject1 = null;
		Map<String, JSONArray> mmm = new LinkedHashMap<>();
		// 遍历数据，先封装到map
		for (int i = 0; i < data.size(); i++) {
			m = data.getJSONObject(i);
			for (Map.Entry<String, Object> map : m.entrySet()) {
				ob = (JSONObject) map.getValue();
				date = map.getKey().split(" ")[0];
				for (Map.Entry<String, Object> t : ob.entrySet()) {
					jsonObject = (JSONObject) t.getValue();
					jsonObject.remove("time");
					String endPeriods = jsonObject.getString("endPeriods");
					jsonObject.remove("endPeriods");// 之前存的是数字类型，在这里重新换成字符串存进去
					jsonObject.put("endPeriods", endPeriods);
					jsonObject.put("date", date);
					ja1 = mmm.get(t.getKey());
					if (ja1 == null) {
						ja1 = new JSONArray();
					}
					ja1.add(jsonObject);
					mmm.put(t.getKey(), ja1);
				}
			}
		}
		JSONArray item = null;
		String key = null;
		// 重新把map封装到json数组中
		for (Map.Entry<String, JSONArray> object : mmm.entrySet()) {
			jsonObject1 = new JSONObject();
			key = object.getKey();
			item = object.getValue();
			jsonObject1.put("num", key);
			jsonObject1.put("item", item);
			ja.add(jsonObject1);
		}
		return ja;
	}

	/**
	 * 
	 * @desc 修改冠亚和路珠记录数据
	 * @author abo
	 * @date 2018年8月8日 下午5:13:47
	 * @param data
	 * @return
	 */
	@Override
	public JSONArray changeGyhLzData(String data) {
		JSONArray ja = new JSONArray();
		Map<String, Object> map = JSONObject.parseObject(data);
		// 大小
		JSONObject dxJson = new JSONObject();
		dxJson.put("da", map.get("num-da"));
		dxJson.put("xiao", map.get("num-xiao"));
		dxJson.put("item", map.get("dx"));
		dxJson.put("type", "daxiao");

		dxJson.put("jrlj", "大(" + map.get("num-da") + ") 小(" + map.get("num-xiao") + ")");
		dxJson.put("title", "冠亞和大小");

		dxJson.put("type", "daxiao");
		// 单双
		JSONObject dsJson = new JSONObject();
		dsJson.put("dan", map.get("num-dan"));
		dsJson.put("shuang", map.get("num-shuang"));
		dsJson.put("item", map.get("ds"));
		dsJson.put("type", "danshuang");
		dsJson.put("jrlj", "单(" + map.get("num-dan") + ") 双(" + map.get("num-shuang") + ")");
		dsJson.put("title", "冠亞和单双");

		dxJson.remove("da");
		dxJson.remove("xiao");
		dsJson.remove("dan");
		dsJson.remove("shuang");
		ja.add(dxJson);
		ja.add(dsJson);
		return ja;
	}

	/**
	 * 
	 * @desc 修改时时彩号码遗漏记录数据(号码遗漏，单双，大小，龙虎，总和单双，总和大小)
	 * @author abo
	 * @date 2018年8月8日 下午5:13:47
	 * @param data
	 * @return
	 */
	@Override
	public JSONArray changeSscHmylData(String data, String code) {
		// System.out.println(data);
		Map<String, Object> map = JSONObject.parseObject(data);
		JSONObject json = null;
		JSONObject json1 = new JSONObject();
		JSONObject json3 = null;
		JSONArray ja = new JSONArray();
		JSONArray ja1 = null;
		Map<Integer, Object> temp1 = new TreeMap<>();
		for (Entry<String, Object> temp : map.entrySet()) {
			// System.out.println(temp.getKey() + "=====" + temp.getValue());
			String key = temp.getKey();
			Map<String, Object> m = (Map<String, Object>) temp.getValue();
			// 如果是总和大小、总和单双、龙虎就走此方法，其他的走else方法。
			if (code.contains(key) && (code.contains("wzlh") || code.contains("wzzhds") || code.contains("wzzhdx"))) {
				for (Map.Entry<String, Object> e : m.entrySet()) {
					json1 = (JSONObject) e.getValue();
					json1.remove("byyl");
					json1.remove("bzyl");
					json1.put("num", e.getKey());
					json1.put("jrkc", json1.get("jrcx"));
					json1.put("ylsy", "");// 暂时没数据
					json1.remove("jrcx");

					ja1 = new JSONArray();
					ja1.add(json1);

					json = new JSONObject();
					json.put("num", e.getKey());
					json.put("item", ja1);

					ja.add(json);
				}
			} else if (code.contains(key)) {
				for (Map.Entry<String, Object> e : m.entrySet()) {
					json3 = new JSONObject();
					ja1 = new JSONArray();
					Map<String, Object> mm = (Map<String, Object>) e.getValue();
					for (Map.Entry<String, Object> ee : mm.entrySet()) {
						json1 = (JSONObject) ee.getValue();
						json1.put("num", ee.getKey());
						json1.put("ylsy", "");
						json1.remove("byyl");// 删除本月
						json1.remove("bzyl");// 删除本周
						json1.put("jrkc", json1.get("jrcx"));
						json1.remove("jrcx");

						ja1.add(json1);
					}
					json3.put("num", e.getKey());
					json3.put("item", ja1);
					// ja.add(json3);
					String d = e.getKey().replace("num", "");
					temp1.put(Integer.parseInt(d), json3);
				}
			}
		}
		// 对非龙虎、总和大小、总和单双重新排序
		if ((code.contains("wzlh") || code.contains("wzzhds") || code.contains("wzzhdx"))) {
			return ja;
		} else {
			for (Map.Entry<Integer, Object> e : temp1.entrySet()) {
				ja.add(e.getValue());
			}
		}
		return ja;
	}

	@Override
	public JSONArray changeZhlzData(String data, String code) {
		JSONArray ja = new JSONArray();
		Map<String, Object> map = JSONObject.parseObject(data);
		// 大小
		JSONObject dxJson = new JSONObject();
		// dxJson.put("da", map.get("num-da"));
		// dxJson.put("xiao", map.get("num-xiao"));
		dxJson.put("item", map.get("xd"));
		if (code.contains("jsk3")) {
			String s = map.get("tongchi") == null ? "0" : map.get("tongchi").toString();
			dxJson.put("jrlj", "大(" + map.get("num-da") + ")小(" + map.get("num-xiao") + ")通吃(" + s + ")");
		} else if (code.contains("11x5")) {
			String s = map.get("num-he") == null ? "0" : map.get("num-he").toString();
			dxJson.put("jrlj", "大(" + map.get("num-da") + ")小(" + map.get("num-xiao") + ")和(" + s + ")");
		} else {
			dxJson.put("jrlj", "大(" + map.get("num-da") + ")lo小(" + map.get("num-xiao") + ") ");
		}
		dxJson.put("title", "总和大小");

		dxJson.put("type", "daxiao");
		// 单双
		JSONObject dsJson = new JSONObject();
		// dsJson.put("dan", map.get("num-dan"));
		// dsJson.put("shuang", map.get("num-shuang"));
		dsJson.put("item", map.get("ds"));
		dsJson.put("type", "danshuang");
		String s1 = map.get("tongchi") == null ? "0" : map.get("tongchi").toString();
		if (code.contains("jsk3")) {
			dsJson.put("jrlj", "单(" + map.get("num-dan") + ")双(" + map.get("num-shuang") + ")通吃(" + s1 + ")");
		} else {
			dsJson.put("jrlj", "单(" + map.get("num-dan") + ")双(" + map.get("num-shuang") + ")");
		}
		dsJson.put("title", "总和单双");

		// 总和
		JSONObject zhJson = new JSONObject();
		zhJson.put("item", map.get("zhwsdx"));
		zhJson.put("type", "zonghe");
		String s2 = map.get("tongchi") == null ? "0" : map.get("tongchi").toString();
		if (code.contains("jsk3")) {
			zhJson.put("jrlj", "大(" + map.get("numzhws-da") + ")小(" + map.get("numzhws-xiao") + ")通吃(" + s2 + ")");
		} else {
			zhJson.put("jrlj", "大(" + map.get("numzhws-da") + ")小(" + map.get("numzhws-xiao") + ")");
		}
		zhJson.put("title", "总和尾数");

		ja.add(dxJson);
		ja.add(dsJson);
		ja.add(zhJson);
		return ja;
	}

	@Override
	public JSONArray changeWsdxlzData(String data, String code) {
		data = data.replace("total", "num");
		Map<String, Object> map = JSONObject.parseObject(data);
		System.out.println(map);
		JSONObject jo = null;
		Map<String, Object> m = new HashMap<>();
		for (Entry<String, Object> temp : map.entrySet()) {
			String[] array = temp.getKey().split("-");
			jo = (JSONObject) m.get(array[0]);
			if (jo == null) {
				jo = new JSONObject();
			}
			m.put(array[0], jo);
			if (array.length > 1) {
				jo.put(array[1], temp.getValue());
			} else {
				jo.put("item", temp.getValue());
			}

			jo.put("num", array[0]);
			m.put(array[0], jo);
		}
		// JSONArray ja = new JSONArray();
		// for (Entry<String, Object> temp : m.entrySet()) {
		// ja.add(temp.getValue());
		// }
		JSONArray ja = new JSONArray();
		Map<Object, Object> mm = new TreeMap<Object, Object>();
		for (Entry<String, Object> temp : m.entrySet()) {
			JSONObject jsono = (JSONObject) temp.getValue();
			String str = jsono.getString("num").replace("num", "");
			int key = 9;// 合计默认是num9
			if (!StringUtils.isEmpty(str)) {
				key = Integer.parseInt(jsono.getString("num").replace("num", ""));
			}
			mm.put(key, jsono);
		}
		// 重新排序
		// for (Entry<Object, Object> temp : mm.entrySet()) {
		// ja.add(temp.getValue());
		// }
		// 重新排序
		for (Entry<Object, Object> temp : mm.entrySet()) {
			Map<Object, Object> mmm = (Map<Object, Object>) temp.getValue();
			Map<Object, Object> jo1 = AppChangeText.changeWord(mmm, code);
			ja.add(jo1);
		}
		return ja;
	}

	@Override
	public JSONArray changeJolzData(String data, String type) {
		JSONObject json = JSONObject.parseObject(data);
		Map<String, Object> sxplzMap = (Map<String, Object>) json.get("sxplz");
		Map<String, Object> jrljMap = (Map<String, Object>) json.get("jrlj");
		Map<Integer, Object> map1 = new TreeMap<Integer, Object>();
		JSONArray ja = new JSONArray();
		JSONArray ja1 = null;
		for (Entry<String, Object> temp : sxplzMap.entrySet()) {
			map1.put(Integer.parseInt(temp.getKey()), temp.getValue());
		}
		ja1 = new JSONArray();
		// 然后重新封装jsonarray
		for (Entry<Integer, Object> t : map1.entrySet()) {
			ja1.add(t.getValue());
		}
		jrljMap.put("item", ja1);
		String str = "";
		String title = "";
		if (type.equals("jolz")) {
			str += "奇(" + jrljMap.get("奇") + ")";
			str += "偶(" + jrljMap.get("偶") + ")";
			str += "和(" + jrljMap.get("和") + ")";
			jrljMap.remove("奇");
			jrljMap.remove("偶");
			jrljMap.remove("和");
			title = "总和奇偶";
		} else if (type.equals("sxlz")) {
			str += "上(" + jrljMap.get("上") + ")";
			str += "中(" + jrljMap.get("中") + ")";
			str += "下(" + jrljMap.get("下") + ")";
			jrljMap.remove("上");
			jrljMap.remove("中");
			jrljMap.remove("下");
			title = "上下盘上下";
		}
		jrljMap.put("title", title);
		jrljMap.put("jrlj", str);
		ja.add(jrljMap);
		return ja;
	}

	@Override
	public JSONArray changeLshmtjData(JSONArray data, String code) {
		System.out.println(data);
		JSONArray ja = new JSONArray();
		JSONArray ja1 = new JSONArray();
		JSONArray ja2 = new JSONArray();
		JSONArray dxJa = null;
		JSONArray dsJa = null;
		Map<String, Object> m = null;
		String date = null;
		JSONObject jsonObject1 = null;
		JSONObject jsonObject2 = null;
		JSONObject jsonObject3 = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		// 遍历数据，先封装到map
		for (int i = 0; i < data.size(); i++) {
			m = data.getJSONObject(i);
			for (Map.Entry<String, Object> temp : m.entrySet()) {
				date = temp.getKey();
				dxJa = new JSONArray();
				dsJa = new JSONArray();
				jsonObject1 = new JSONObject();
				jsonObject2 = new JSONObject();
				// jsonObject3 = new JSONObject();
				Map<String, Object> mm = (Map<String, Object>) temp.getValue();
				// 封装号码开始
				// 如果不是广东快乐十分，那么num索引从0开始，9结束
				if (code.contains("ssc_lshmtj")) {
					dxJa.add(mm.get("num0"));
					dxJa.add(mm.get("num1"));
					dxJa.add(mm.get("num2"));
					dxJa.add(mm.get("num3"));
					dxJa.add(mm.get("num4"));
					dxJa.add(mm.get("num5"));
					dxJa.add(mm.get("num6"));
					dxJa.add(mm.get("num7"));
					dxJa.add(mm.get("num8"));
					dxJa.add(mm.get("num9"));
					// 封装两面
					dsJa.add(mm.get("dan"));
					dsJa.add(mm.get("shuang"));
					dsJa.add(mm.get("da"));
					dsJa.add(mm.get("xiao"));
					dsJa.add(mm.get("long"));
					dsJa.add(mm.get("hu"));
					dsJa.add(mm.get("lfping"));
				} else if (code.contains("11x5_lshmtj")) {
					dxJa.add(mm.get("num1"));
					dxJa.add(mm.get("num2"));
					dxJa.add(mm.get("num3"));
					dxJa.add(mm.get("num4"));
					dxJa.add(mm.get("num5"));
					dxJa.add(mm.get("num6"));
					dxJa.add(mm.get("num7"));
					dxJa.add(mm.get("num8"));
					dxJa.add(mm.get("num9"));
					dxJa.add(mm.get("num10"));
					dxJa.add(mm.get("num11"));

					// 封装两面
					dsJa.add(mm.get("dan"));
					dsJa.add(mm.get("shuang"));
					dsJa.add(mm.get("da"));
					dsJa.add(mm.get("xiao"));
					dsJa.add(mm.get("long"));
					dsJa.add(mm.get("hu"));
				} else if ("gdklsf_lshmtj".equals(code) || "xync_lshmtj".equals(code)) {
					dxJa.add(mm.get("num1"));
					dxJa.add(mm.get("num2"));
					dxJa.add(mm.get("num3"));
					dxJa.add(mm.get("num4"));
					dxJa.add(mm.get("num5"));
					dxJa.add(mm.get("num6"));
					dxJa.add(mm.get("num7"));
					dxJa.add(mm.get("num8"));
					dxJa.add(mm.get("num9"));
					dxJa.add(mm.get("num10"));
					dxJa.add(mm.get("num11"));
					dxJa.add(mm.get("num12"));
					dxJa.add(mm.get("num13"));
					dxJa.add(mm.get("num14"));
					dxJa.add(mm.get("num15"));
					dxJa.add(mm.get("num16"));
					dxJa.add(mm.get("num17"));
					dxJa.add(mm.get("num18"));
					dxJa.add(mm.get("num19"));
					dxJa.add(mm.get("num20"));
					// 封装两面
					dsJa.add(mm.get("dan"));
					dsJa.add(mm.get("shuang"));
					dsJa.add(mm.get("da"));
					dsJa.add(mm.get("xiao"));
					dsJa.add(mm.get("long"));
					dsJa.add(mm.get("hu"));
				} else if ("jsk3_lshmtj".equals(code)) {
					dxJa.add(mm.get("num1"));
					dxJa.add(mm.get("num2"));
					dxJa.add(mm.get("num3"));
					dxJa.add(mm.get("num4"));
					dxJa.add(mm.get("num5"));
					dxJa.add(mm.get("num6"));

					dsJa.add(mm.get("da"));
					dsJa.add(mm.get("xiao"));
				}

				jsonObject1.put("date", date);
				jsonObject1.put("num", dxJa);
				ja1.add(jsonObject1);

				jsonObject2.put("date", date);
				jsonObject2.put("liangmian", dsJa);
				ja2.add(jsonObject2);
			}
			jsonObject.put("num", ja1);
			jsonObject3.put("liangmian", ja2);
		}

		ja.add(jsonObject3);
		ja.add(jsonObject);
		return ja;
	}

	/**
	 * 
	 * @desc 修改上下段位走势数据
	 * @author abo
	 * @date 2018年8月9日 下午2:02:48
	 * @param data
	 * @return
	 */
	@Override
	public JSONArray changeSxdwzsData(JSONArray data) {
		Map<String, Object> m = null;
		Map<Integer, Object> map = new TreeMap<Integer, Object>();
		JSONArray ja = new JSONArray();
		for (int i = 0; i < data.size(); i++) {
			m = data.getJSONObject(i);
			for (Map.Entry<String, Object> temp : m.entrySet()) {
				String key = temp.getKey();// 期号
				Map<String, Object> m1 = (Map<String, Object>) temp.getValue();// 球
				for (Map.Entry<String, Object> temp1 : m1.entrySet()) {
					String k = temp1.getKey().replace("num", "");// 球号

					JSONArray ja2 = (JSONArray) map.get(Integer.parseInt(k));
					if (ja2 == null) {
						ja2 = new JSONArray();
					}
					JSONObject jsonTemp = (JSONObject) temp1.getValue();
					Map<String, Object> map3 = (Map<String, Object>) jsonTemp.get("40");// 取四十期的对象，有可能后续会有90期等需求
					// 删掉不要的属性
					map3.remove("a1fan");
					map3.remove("a2chong");
					map3.remove("a3zheng");
					map3.remove("a4dan");
					map3.remove("a5shuang");
					map3.remove("a6da");
					map3.remove("a7xiao");
					JSONArray ja3 = new JSONArray();// 遗漏次数数组
					for (Map.Entry<String, Object> temp2 : map3.entrySet()) {
						ja3.add(temp2.getValue());
					}
					JSONObject jsonTemp1 = new JSONObject();
					jsonTemp1.put("qihao", key);
					jsonTemp1.put("num", ja3);
					ja2.add(jsonTemp1);
					map.put(Integer.parseInt(k), ja2);
				}
			}
		}
		// 重新把数据封装成json数组
		for (Map.Entry<Integer, Object> temp : map.entrySet()) {
			JSONObject jo = new JSONObject();
			jo.put("type", "num" + temp.getKey());
			jo.put("item", temp.getValue());
			ja.add(jo);
		}
		return ja;
	}

	/**
	 * 
	 * @desc 数据比对
	 * @author abo
	 * @date 2018年8月15日 下午2:53:51
	 * @param currentOb
	 * @param oldOb
	 * @return
	 */
	public JSONArray changeSjdbData(Object currentOb, Object oldOb) {
		JSONArray ja = new JSONArray();
		JSONArray ja1 = null;
		JSONObject json = null;
		Map<String, Object> currentMap = JSONObject.parseObject(currentOb + "");
		Map<String, Object> tempCurrentMap = null;
		Map<String, Object> oldMap = JSONObject.parseObject(oldOb + "");
		Map<Integer, Object> temp = new TreeMap<>();
		Map<Integer, Object> temp1 = null;
		// 如果今天未开奖，那么全部就显示0，0，0，0，0，这里用昨天的数据来代替一下循环
		if (currentMap == null) {
			currentMap = oldMap;
		}
		if (oldMap == null) {
			oldMap = currentMap;
		}
		for (Map.Entry<String, Object> m : currentMap.entrySet()) {
			String key = m.getKey();// 球位置，比如冠军，一号球等
			tempCurrentMap = (Map<String, Object>) m.getValue();
			json = new JSONObject();
			ja1 = new JSONArray();
			temp1 = new TreeMap<>();
			json.put("num", key);
			for (Map.Entry<String, Object> mm : tempCurrentMap.entrySet()) {
				String k = mm.getKey();// 球号
				String str = null;// 数据对比的数组
				if (currentOb == null) {
					str = "[0,0,0,0,0]";// 如果今天未开奖，那么全部就显示0，0，0，0，0
				} else {
					str = mm.getValue().toString();// 数据对比的数组
				}
				String str1 = null;
				JSONObject jo = (JSONObject) oldMap.get(key);
				if (oldOb == null) {
					str1 = "[0,0,0,0,0]";// 如果历史记录没有，那么全部就显示0，0，0，0，0
				} else {
					str1 = jo.get(k) + "";
				}
				str += "," + str1;
				str = str.replaceAll("\\[", "");
				str = str.replaceAll("\\]", "");
				temp1.put(Integer.parseInt(k), str);
			}
			for (Map.Entry<Integer, Object> mm : temp1.entrySet()) {
				ja1.add(mm.getValue());
			}
			json.put("item", ja1);
			temp.put(Integer.parseInt(key.replaceAll("num", "")), json);
		}
		for (Map.Entry<Integer, Object> m : temp.entrySet()) {
			ja.add(m.getValue());
		}
		return ja;
	}

	@Override
	public JSONArray changeJjshData(Map<Object, Object> map) {
		JSONArray ja = new JSONArray();
		JSONObject json = new JSONObject(16, true);
		JSONArray ja2 = null;
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			JSONArray ja1 = JSONArray.parseArray(entry.getValue().toString());
			String num = null;
			for (int i = 0; i < ja1.size(); i++) {
				JSONObject j = ja1.getJSONObject(i);
				num = j.getString("num");
				ja2 = json.getJSONArray(num);
				if (ja2 == null) {
					ja2 = new JSONArray();
				}
				JSONArray tempArray = j.getJSONArray("tjarray");
				// 把统计添加到推荐数组的末尾
				String str = j.get("tongji").toString().equals("10") ? "全中" : j.get("tongji").toString();
				tempArray.add(str);
				ja2.add(j);
				json.put(num, ja2);
			}
		}
		ja.add(json);
		return ja;
	}

	@Override
	public JSONArray changeHmtjjhData(Map<Object, Object> map, String position) {
		JSONArray ja = new JSONArray();
		// JSONObject jo = new JSONObject();
		Map<Long, Object> jo = new TreeMap<Long, Object>();
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			JSONArray ja1 = JSONArray.parseArray(entry.getValue() + "");
			// ja2 = new JSONArray();
			for (int i = 0; i < ja1.size(); i++) {
				JSONObject json = ja1.getJSONObject(i);
				String num = json.getString("num");// 球号
				if (!num.equals(position)) {
					continue;
				}
				jo.put(Long.parseLong(json.getString("qishu").toString()), json);
			}
		}
		JSONArray ja3 = new JSONArray();
		// 先把顺序的放入一个array，然后在倒序放入return的数组
		for (Entry<Long, Object> object : jo.entrySet()) {
			ja3.add(object.getValue());
		}
		JSONArray ja2 = new JSONArray();
		for (int i = ja3.size() - 1; i >= 0; i--) {
			ja2.add(ja3.get(i));
		}
		ja.add(ja2);
		return ja;
	}

	@Override
	public JSONArray changeHmtjjhlbData(Map<Object, Object> map, String lottId) {
		JSONArray ja = new JSONArray();
		JSONArray ja2 = null;
		JSONObject jo = new JSONObject(true);
		JSONObject jo1 = null;
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			JSONArray ja1 = JSONArray.parseArray(entry.getValue() + "");
			ja2 = new JSONArray();
			for (int i = 0; i < ja1.size(); i++) {
				JSONObject json = ja1.getJSONObject(i);
				String num = json.getString("num");// 球号
				ja2 = (JSONArray) jo.get(num);
				if (ja2 == null) {
					ja2 = new JSONArray();
				}
				ja2.add(json);
				jo.put(num, ja2);
			}
		}
		for (Entry<String, Object> entry : jo.entrySet()) {
			int winCount = 0;
			int loseCount = 0;
			int notOpen = 0;
			JSONObject jo2 = new JSONObject(true);
			String key = entry.getKey();
			ja2 = (JSONArray) entry.getValue();
			for (int i = 0; i < ja2.size(); i++) {
				jo1 = ja2.getJSONObject(i);
				int winOrLose = jo1.getIntValue("shuying");// 获取推荐输赢
				if (winOrLose == 1) {
					winCount++;
				} else if (winOrLose == -1) {
					loseCount++;
				} else {
					notOpen++;
				}
			}
			jo2.put("num", key);
			jo2.put("numStr", AppChangeText.changeWord(key, lottId));
			jo2.put("winCount", winCount);// 胜的次数
			jo2.put("loseCount", loseCount);// 输的次数
			jo2.put("notOpen", notOpen);// 未开奖
			int sum = winCount + loseCount;
			if (sum == 0) {
				jo2.put("winatio", 0);// 胜率，胜的次数除以总和
			} else {
				jo2.put("winatio", winCount * 100 / (winCount + loseCount));// 胜率，胜的次数除以总和
			}
			ja.add(jo2);
		}
		return ja;
	}

	@Override
	public JSONArray changeLmtjjhData(Map<Object, Object> map, String searchkey, String position) {
		System.out.println(map);
		JSONArray ja = new JSONArray();
		JSONArray ja2 = new JSONArray();
		JSONArray ja3 = null;
		// JSONObject jo = new JSONObject(true);
		Map<Long, Object> jo = new TreeMap<Long, Object>();
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			JSONObject jo1 = JSONObject.parseObject(entry.getValue() + "");
			// 先查询第一层玩法
			JSONArray ja1 = (JSONArray) jo1.get(searchkey);
			// ja2 = new JSONArray();
			for (int i = 0; i < ja1.size(); i++) {
				ja3 = new JSONArray();
				JSONObject json = ja1.getJSONObject(i);
				// 按app安卓端要求，改成数组传他
				ja3.add(json.get("tjnum"));
				json.put("tjnum", ja3);
				String num = json.getString("num");// 球号
				if (!num.equals(position)) {
					continue;
				}
				// ja2 = (JSONArray) jo.get(Long.parseLong(json.getString("qishu").toString()));
				// if (ja2 == null) {
				// ja2 = new JSONArray();
				// }
				// ja2.add(json);
				jo.put(Long.parseLong(json.getString("qishu").toString()), json);
			}
		}
		ja3 = new JSONArray();
		// 先把顺序的放入一个array，然后在倒序放入return的数组
		for (Entry<Long, Object> object : jo.entrySet()) {
			ja3.add(object.getValue());
		}

		for (int i = ja3.size() - 1; i >= 0; i--) {
			ja2.add(ja3.get(i));
		}

		// 根据球进行过滤
		ja.add(ja2);
		return ja;
	}

	@Override
	public JSONArray changelmtjjhlbData(Map<Object, Object> map, String lottId) {
		JSONArray ja = new JSONArray();
		JSONArray ja2 = null;
		JSONObject jo = new JSONObject(true);
		JSONObject jo1 = null;
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			// 因为两面计划多套了一层，所以在这里单独分解一层
			JSONObject jo2 = JSONObject.parseObject(entry.getValue() + "");
			for (Entry<String, Object> entry1 : jo2.entrySet()) {
				String key = entry1.getKey();
				JSONArray ja1 = JSONArray.parseArray(entry1.getValue() + "");
				// ja2 = new JSONArray();
				for (int i = 0; i < ja1.size(); i++) {
					JSONObject json = ja1.getJSONObject(i);
					String num = json.getString("num");// 球号
					ja2 = (JSONArray) jo.get(num + "_" + key);
					if (ja2 == null) {
						ja2 = new JSONArray();
					}
					ja2.add(json);
					jo.put(num + "_" + key, ja2);
				}
			}
		}
		for (Entry<String, Object> entry : jo.entrySet()) {
			int winCount = 0;
			int loseCount = 0;
			int notOpen = 0;
			JSONObject jo2 = new JSONObject(true);
			String key = entry.getKey();
			ja2 = (JSONArray) entry.getValue();
			for (int i = 0; i < ja2.size(); i++) {
				jo1 = ja2.getJSONObject(i);
				int winOrLose = jo1.getIntValue("shuying");// 获取推荐输赢
				if (winOrLose == 1) {
					winCount++;
				} else if (winOrLose == -1) {
					loseCount++;
				} else {
					notOpen++;
				}
			}

			String[] array = key.split("_");
			jo2.put("num", array[0]);
			jo2.put("numStr", AppChangeText.changeWord(array, lottId));
			jo2.put("key", key);
			jo2.put("winCount", winCount);// 胜的次数
			jo2.put("loseCount", loseCount);// 输的次数
			jo2.put("notOpen", notOpen);// 未开奖
			int sum = winCount + loseCount;
			if (sum == 0) {
				jo2.put("winatio", 0);// 胜率，胜的次数除以总和
			} else {
				jo2.put("winatio", winCount * 100 / (winCount + loseCount));// 胜率，胜的次数除以总和
			}
			ja.add(jo2);
		}
		return ja;
	}

	@Override
	public JSONArray changePcddZhlzData(String data) {
		JSONObject json = JSONObject.parseObject(data);
		JSONObject json1 = new JSONObject();
		json1.put("jrlj", json.get("jrljdx"));
		json1.put("title", json.get("jrljdxStr"));
		json1.put("type", "daxiao");
		JSONArray ja1 = new JSONArray();
		Map<String, Object> map = (Map<String, Object>) json.get("itemdx");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			ja1.add(entry.getValue());
		}
		json1.put("item", ja1);

		JSONArray ja = new JSONArray();
		ja.add(json1);

		JSONObject json2 = new JSONObject();
		json2.put("jrlj", json.get("jrljds"));
		json2.put("title", json.get("jrljdsStr"));
		json2.put("type", "danshuang");
		JSONArray ja2 = new JSONArray();
		Map<String, Object> map1 = (Map<String, Object>) json.get("itemds");
		for (Map.Entry<String, Object> entry : map1.entrySet()) {
			ja2.add(entry.getValue());
		}
		json2.put("item", ja2);

		ja.add(json2);
		return ja;
	}

}
