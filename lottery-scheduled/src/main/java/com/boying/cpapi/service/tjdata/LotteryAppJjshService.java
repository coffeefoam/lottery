package com.boying.cpapi.service.tjdata;

import java.util.List;
import java.util.Map;
import java.util.Random;

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
 * @desc 计算app端的狙击杀号
 * @author abo
 * @date 2018年8月22日 下午3:07:33
 *
 */
@Service
public class LotteryAppJjshService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * @author ms
	 * @param lottId
	 */
	public void execute(String lottId) {
		// 狙击杀号
		this.appJjsh(lottId);
	}

	/**
	 * 
	 * @desc app端狙击杀号，北京赛车，幸运飞艇
	 * @author abo
	 * @date 2018年8月21日 上午11:00:09
	 * @param lottId
	 */
	public void appJjsh(String lottId) {
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT) || lottId.equals(LotteryConstant.CQSSC)) {
			// 今天
			String currentDate = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE);
			String numberStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			List<Map<String, Object>> list = this.lotteryProcessMapper.selectBjTrend(tablename, numberStr, "1", currentDate);
			Map<String, Object> map = list.get(0);
			// 当前期号
			Long periods = Long.parseLong(map.get("periods").toString());
			JSONArray ja = null;
			// 完成当前期数据
			Object object = redisservice.getHashMap(lottId + "_" + LotteryDict.jjsh, periods + "");
			if (object != null) {
				ja = complateCurrenPeriodsRandom(object, map);
				redisservice.setHashMap(lottId + "_" + LotteryDict.jjsh, periods + "", ja.toJSONString());
//				System.out.println(lottId + "狙击杀号完成本期数据：" + periods + "-----" + ja);
			}
			// 获取最新一期的开奖期号
			JSONObject json = JSONObject.parseObject(redisservice.get(lottId + "_" + LotteryDict.zxkj));
			String newPeriods  = json.getString("xqqs");
			// 生成下期的推荐号
			ja = getNextPeriodsRandom(Long.parseLong(newPeriods), numberStr);
			redisservice.setHashMap(lottId + "_" + LotteryDict.jjsh, newPeriods, ja.toJSONString());
//			System.out.println(lottId + "狙击杀号完成下期数据：" + newPeriods);
		}
	}

	/**
	 * 
	 * @desc 完成本期狙击杀号基础数据
	 * @author abo
	 * @date 2018年8月21日 下午5:22:19
	 * @param object
	 * @param map
	 * @return
	 */
	private JSONArray complateCurrenPeriodsRandom(Object object, Map<String, Object> map) {
		JSONArray ja1 = new JSONArray();
		JSONArray ja = JSONArray.parseArray(object.toString());
		for (int i = 0; i < ja.size(); i++) {
			JSONObject json = (JSONObject) ja.get(i);
			String kjhm = map.get(json.get("num")).toString();
			String arraystr = json.get("tjarray").toString();
			String tj = countTjNum(kjhm, arraystr);
			json.put("tongji", tj);
			json.put("kjhm", kjhm);
			ja1.add(json);
		}
		return ja1;
	}

	/**
	 * 
	 * @desc 计算命中次数
	 * @author abo
	 * @date 2018年8月21日 下午5:16:37
	 * @param kjhm
	 * @param tjarray
	 * @return
	 */
	private String countTjNum(String kjhm, String tjarray) {
		int count = 0;
		tjarray = tjarray.replaceAll("\\[", "");
		tjarray = tjarray.replaceAll("\\]", "");
		String[] array = tjarray.split(",");
		for (int i = 0; i < array.length; i++) {
			Object s = array[i];
			if (!kjhm.equals(s.toString())) {
				count++;
			}
		}
		return count + "";
	}

	/**
	 * 
	 * @desc 完成下期狙击杀号基础数据
	 * @author abo
	 * @date 2018年8月21日 下午5:16:50
	 * @param periods
	 * @param numberStr
	 * @return
	 */
	private JSONArray getNextPeriodsRandom(Long periods, String numberStr) {
		String[] array = numberStr.split(",");
		JSONObject jo = null;
		JSONArray ja = new JSONArray();
		JSONArray ja1 = null;
		Random r = new Random();
		for (int i = 0; i < array.length; i++) {
			ja1 = new JSONArray();
			jo = new JSONObject();
			// 预测十组
			for (int j = 0; j < 10; j++) {
				int n = r.nextInt(10) + 1;
				ja1.add(n);
			}
			jo.put("num", array[i]);
			jo.put("tjarray", ja1);
			jo.put("kjhm", "");
			jo.put("qishu", periods);
			jo.put("tongji", "");
			ja.add(jo);
		}
		return ja;
	}

}
