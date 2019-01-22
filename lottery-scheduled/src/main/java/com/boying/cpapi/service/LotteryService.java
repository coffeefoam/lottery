package com.boying.cpapi.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.MyProps;
import com.boying.cpapi.http.HttpAPIService;
import com.boying.cpapi.mapper.idata.LotteryMapper;
import com.boying.cpapi.service.pdata.LotteryLhcService;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;

import io.lottery.common.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @desc 彩票数据抽取服务类
 * @author abo
 * @date 2018年6月22日 下午5:10:42
 *
 */
@Slf4j
@Service
public class LotteryService {
	@Resource
	private LotteryMapper lotteryMapper;
	@Autowired
	private MyProps myprops;
	@Resource
	private HttpAPIService httpapiservice;
	@Resource
	private AbstractServices abstractServices;
	@Resource
	private LotteryLhcService lotteryLhcService;

	/**
	 * @desc 获取当前最新一期的彩票开奖信息
	 * @author abo
	 * @date 2018年6月27日 下午1:36:05
	 */
	public void getLotteryData() {
		try {
			String responseStr = httpapiservice.doGetNoProxy(myprops.getApiplusurl());
			if (StringUtils.isNotBlank(responseStr)) {
				this.getLotteryOpenDataByApi(responseStr);
			}
		} catch (Exception e) {
			log.error(new Date() + "|LotteryService:203|" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @desc 处理相关业务
	 * @author abo
	 * @date 2018年7月10日 下午5:06:48
	 * @param responsStr
	 */
	public void getLotteryOpenDataByApi(String responsStr) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(responsStr);
			if (jsonObject != null) {
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						JSONObject ob = jsonArray.getJSONObject(i);
						String lottId = LotteryUtil.updateLottId(ob.getString("code"));
						// 根据彩种放到不同的表里面去
						String tableName = LotteryConstant.tableNameMap.get(lottId);
						// 如果此彩种在我们系统表里面没有，那么就不做采集
						if (!StringUtils.isNotBlank(tableName)) {
							continue;
						}
						map.put("tableName", tableName);// 表名，根据类型去字典获取
						map.put("periods", ob.getString("expect"));// 开奖期数
						// map.put("openCode", ob.getString("opencode"));
						Timestamp time = DateUtil.string2Timestamp(ob.getString("opentime"), DateUtil.PATTERN_DATE);
						// 由于幸运农场、重庆时时彩、新疆时时彩是跨天开奖的彩种，那么这3个彩种的查询时间根据期数做一下调整
						if (lottId.equals("xyft") || lottId.equals("cqssc") || lottId.equals("xjssc")) {
							String periods = ob.getString("expect");
							periods = periods.substring(0, 4) + "-" + periods.substring(4, 6) + "-"
									+ periods.substring(6, 8);
							time = DateUtil.string2Timestamp(periods, DateUtil.PATTERN_DATE);
						}
						map.put("time", time);
						map.put("starttime", ob.getString("opentime"));
						map.put("insertNum", LotteryConstant.lotteryColumnStrMap.get(lottId));// 根据球数修改插入的字段数
						map.put("insertNumValue",
								LotteryUtil.getInsertNumByLottIdAndOpenCode(ob.getString("opencode"), lottId));// 根据球数修改插入的字段值
						map.put("createtime", DateUtil.dateString(new Date()));
						// 处理是否插入数据库，并且是否处理相关数据到redis
						int count = lotteryMapper.findCountByPeriods(tableName, ob.getString("expect"));
						if (count < 1) {
							lotteryMapper.insertByMap(map);
							// 处理数据到redis
							abstractServices.handle(lottId);
							String date = DateUtil.date2String(new Date(), DateUtil.PATTERN_STANDARD);
							lotteryMapper.updateProcessTimeByPeriods(tableName, ob.getString("expect"), date);
						}
						// 处理pc蛋蛋的数据，pc蛋蛋来源于kl8
						if (lottId.equals("kl8")) {
							savePcdd(ob);
						}
					}
				}

			}
		} catch (Exception e) {
			log.error(responsStr);
			log.error(ExceptionUtil.getExceptionStackTrace(e));
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @desc 保存pcdd
	 * @author abo
	 * @date 2018年9月5日 上午10:16:54
	 * @param ob
	 */
	public void savePcdd(JSONObject ob) {
		// 根据彩种放到不同的表里面去
		String lottId = LotteryConstant.PCDD;
		String tableName = LotteryConstant.tableNameMap.get(lottId);
		// 如果此彩种在我们系统表里面没有，那么就不做采集
		if (!StringUtils.isNotBlank(tableName)) {
			return;
		}
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("tableName", tableName);// 表名，根据类型去字典获取
		map1.put("periods", ob.getString("expect"));// 开奖期数
		Timestamp time = DateUtil.string2Timestamp(ob.getString("opentime"), DateUtil.PATTERN_DATE);
		map1.put("time", time);
		map1.put("starttime", ob.getString("opentime"));
		map1.put("insertNum", LotteryConstant.lotteryColumnStrMap.get(lottId));// 根据球数修改插入的字段数

		String[] str = ob.getString("opencode").toString().split(",");
		// 1-6相加
		int hmPcd1 = Integer.parseInt(str[0]) + Integer.parseInt(str[1]) + Integer.parseInt(str[2])
				+ Integer.parseInt(str[3]) + Integer.parseInt(str[4]) + Integer.parseInt(str[5]);
		// 7-12
		int hmPcd2 = Integer.parseInt(str[6]) + Integer.parseInt(str[7]) + Integer.parseInt(str[8])
				+ Integer.parseInt(str[9]) + Integer.parseInt(str[10]) + Integer.parseInt(str[11]);
		// 13-18
		int hmPcd3 = Integer.parseInt(str[12]) + Integer.parseInt(str[13]) + Integer.parseInt(str[14])
				+ Integer.parseInt(str[15]) + Integer.parseInt(str[16]) + Integer.parseInt(str[17]);
		// 取3个数的个位数作为开奖号
		String openCode = hmPcd1 % 10 + "," + hmPcd2 % 10 + "," + hmPcd3 % 10;
		map1.put("insertNumValue", LotteryUtil.getInsertNumByLottIdAndOpenCode(openCode, lottId));// 根据球数修改插入的字段值
		map1.put("createtime", DateUtil.dateString(new Date()));
		// 处理是否插入数据库，并且是否处理相关数据到redis
		int count1 = lotteryMapper.findCountByPeriods(tableName, ob.getString("expect"));
		if (count1 < 1) {
			lotteryMapper.insertByMap(map1);
			// 处理数据到redis
			abstractServices.handle(lottId);
			String date = DateUtil.date2String(new Date(), DateUtil.PATTERN_STANDARD);
			lotteryMapper.updateProcessTimeByPeriods(tableName, ob.getString("expect"), date);
			log.info("pcdd补全数据：" + map1);
		}
	}

	/**
	 * 
	 * @desc 根据当前日期进行查询，查询当前日期当前时间所有的彩票信息
	 * @author abo
	 * @param currentDate
	 * @date 2018年6月25日 下午12:57:27
	 *
	 */
	public void getLotteryDataByDay(String currentDate) {
		List<Map<String, Object>> list = null;
		String url =null;
		try {
			for (Entry<String, Integer> map : LotteryConstant.lotteryNumMap.entrySet()) {
				String lottId = map.getKey();
				// url = myprops.getLottUrl().get("dateapi");
				// dateapi:
				// http://ho.apiplus.net/daily.do?token=t262ada14b15ddc84k&code=codevalue&format=json&date=datevalue
				// 把模版里面拿到的url进行预处理，换掉关键词，用于每次动态访问
				url = myprops.getDateapiurl();
				url = url.replace("codevalue", LotteryUtil.backpdateLottId(lottId));
				url = url.replace("datevalue", currentDate);
				String responseStr = httpapiservice.doGetNoProxy(url);
				System.out.println(responseStr);
				if (StringUtils.isNotBlank(responseStr)) {
					JSONObject jsonObject = JSONObject.parseObject(responseStr);
					if (jsonObject != null) {
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						if (jsonArray != null && jsonArray.size() > 0) {
							// 根据彩种放到不同的表里面去
							String tableName = LotteryConstant.tableNameMap.get(lottId);
							String insertNum = LotteryConstant.lotteryColumnStrMap.get(lottId);// 根据球数修改插入的字段数
							list = new ArrayList<Map<String, Object>>();
							for (int i = 0; i < jsonArray.size(); i++) {
								Map<String, Object> tempmap = new HashMap<String, Object>();
								JSONObject ob = jsonArray.getJSONObject(i);
								// 如果此彩种在我们系统表里面没有，那么就不做采集
								if (!StringUtils.isNotBlank(tableName)) {
									continue;
								}
								// tempmap.put("tableName", tableName);//
								// 表名，根据类型去字典获取
								tempmap.put("periods", ob.getString("expect"));// 开奖期数
								Timestamp time = DateUtil.string2Timestamp(ob.getString("opentime"),
										DateUtil.PATTERN_DATE);
								// 由于幸运农场、重庆时时彩、新疆时时彩是跨天开奖的彩种，那么这3个彩种的查询时间根据期数做一下调整
								if (lottId.equals("xyft") || lottId.equals("cqssc") || lottId.equals("xjssc")) {
									String periods = ob.getString("expect");
									periods = periods.substring(0, 4) + "-" + periods.substring(4, 6) + "-"
											+ periods.substring(6, 8);
									time = DateUtil.string2Timestamp(periods, DateUtil.PATTERN_DATE);
								}
								tempmap.put("time", time);
								tempmap.put("starttime", ob.getString("opentime"));
								// 快乐8是20+1，中间最后一个数字是用+号隔开所以这里做了一下处理
								String openCode = ob.getString("opencode");
								if (lottId.equals(LotteryConstant.KL8)) {
									openCode = openCode.replace("+", ",");
								}
								String[] codes = openCode.split(",");
								for (int j = 0; j < codes.length; j++) {
									tempmap.put("num" + (j + 1), codes[j]);
								}
								// 根据球数修改插入的字段值
								tempmap.put("createtime", DateUtil.dateString(new Date()));
								list.add(tempmap);
							}
							// 批量保存
							if (list != null && list.size() > 0) {
								lotteryMapper.batchInsertByListmap(list, tableName, insertNum);
								System.out.println(tableName + ":save:" + list.size());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("发生异常：" + e.getMessage() + ".异常的url：" + url);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @desc 获取当前全部记录，并且补齐后计算缺失的到redis
	 * @author abo
	 * @date 2018年8月13日 下午3:10:42
	 * @param currentDate
	 */
	public void getLotteryDataByDayAndExecute(String currentDate) {
		String url=null;
		try {
			for (Entry<String, Integer> map : LotteryConstant.lotteryNumMap.entrySet()) {
				String lottId = map.getKey();
				// url = myprops.getLottUrl().get("dateapi");
				// pc蛋蛋是快乐8的衍生彩种，接口是没有返回的，所以这里跳过
				if (lottId.equals(LotteryConstant.PCDD)) {
					continue;
				}
				// 把模版里面拿到的url进行预处理，换掉关键词，用于每次动态访问
				url = myprops.getDateapiurl();
				url = url.replace("codevalue", LotteryUtil.backpdateLottId(lottId));
				url = url.replace("datevalue", currentDate);
				String responseStr = httpapiservice.doGetNoProxy(url);
				// System.out.println(responseStr);
				if (StringUtils.isNotBlank(responseStr)) {
					JSONObject jsonObject = JSONObject.parseObject(responseStr);
					if (jsonObject != null) {
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						if (jsonArray != null && jsonArray.size() > 0) {
							// 根据彩种放到不同的表里面去
							String tableName = LotteryConstant.tableNameMap.get(lottId);
							// String insertNum = LotteryConstant.lotteryColumnStrMap.get(lottId);//
							// 根据球数修改插入的字段数
							for (int i = 0; i < jsonArray.size(); i++) {
								Map<String, Object> tempmap = new HashMap<String, Object>();
								JSONObject ob = jsonArray.getJSONObject(i);
								// 如果此彩种在我们系统表里面没有，那么就不做采集
								if (!StringUtils.isNotBlank(tableName)) {
									continue;
								}
								tempmap.put("tableName", tableName);// 表名，根据类型去字典获取
								tempmap.put("periods", ob.getString("expect"));// 开奖期数
								Timestamp time = DateUtil.string2Timestamp(ob.getString("opentime"),
										DateUtil.PATTERN_DATE);
								// 由于幸运农场、重庆时时彩、新疆时时彩是跨天开奖的彩种，那么这3个彩种的查询时间根据期数做一下调整
								if (lottId.equals("xyft") || lottId.equals("cqssc") || lottId.equals("xjssc")) {
									String periods = ob.getString("expect");
									periods = periods.substring(0, 4) + "-" + periods.substring(4, 6) + "-"
											+ periods.substring(6, 8);
									time = DateUtil.string2Timestamp(periods, DateUtil.PATTERN_DATE);
								}
								tempmap.put("time", time);
								tempmap.put("starttime", ob.getString("opentime"));
								// 快乐8是20+1，中间最后一个数字是用+号隔开所以这里做了一下处理
								String openCode = ob.getString("opencode");
								if (lottId.equals(LotteryConstant.KL8)) {
									openCode = openCode.replace("+", ",");
								}
								// String[] codes = openCode.split(",");
								// for (int j = 0; j < codes.length; j++) {
								// tempmap.put("num" + (j + 1), codes[j]);
								// }
								tempmap.put("insertNum", LotteryConstant.lotteryColumnStrMap.get(lottId));// 根据球数修改插入的字段数
								tempmap.put("insertNumValue",
										LotteryUtil.getInsertNumByLottIdAndOpenCode(ob.getString("opencode"), lottId));// 根据球数修改插入的字段值
								// 根据球数修改插入的字段值
								tempmap.put("createtime", DateUtil.dateString(new Date()));
								// 处理是否插入数据库，并且是否处理相关数据到redis
								int count = lotteryMapper.findCountByPeriods(tableName, ob.getString("expect"));
								// 1条条的存，然后处理到redis
								if (count < 1) {
									lotteryMapper.insertByMap(tempmap);
									// 处理数据到redis
									abstractServices.handle(lottId);
									String date = DateUtil.date2String(new Date(), DateUtil.PATTERN_STANDARD);
									lotteryMapper.updateProcessTimeByPeriods(tableName, ob.getString("expect"), date);
									log.info(lottId + "补全数据：" + tempmap);
								}
								// 处理pc蛋蛋的数据，pc蛋蛋来源于kl8
								if (lottId.equals("kl8")) {
									savePcdd(ob);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("发生异常：" + e.getMessage() + ".异常的url：" + url);
			e.printStackTrace();
		}
	}

	public Map<String, Object> getUser(String username) {
		return lotteryMapper.getUser(username);
	}

	/**
	 * 六合彩数据抓取
	 * 
	 * @desc
	 * @author lj
	 * @author 2018年9月24日
	 */
	public void getLhcData() {
		log.info("六合彩数据抓取开奖数据");
		try {
			Map<String, String> map = myprops.getLhcapi();
			if (map != null && map.size() > 0) {
				for (Entry<String, String> entry : map.entrySet()) {
					String key = entry.getKey();
					String url = entry.getValue();
					String responseStr = httpapiservice.doGetNoProxy(url);
					if (StringUtils.isNotBlank(responseStr)) {
						lotteryLhcService.getLhcKjData(key, responseStr);
					}
					Thread.sleep(10000);
				}
			}
		} catch (Exception e) {
			log.error("六合彩数据抓取出错：{}", e);
		}
	}

}
