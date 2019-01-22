package com.boying.cpapi.service.pdata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.domain.LiuheVo;
import com.boying.cpapi.mapper.idata.LotteryMapper;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;

import io.lottery.common.config.LotteryDict;
import io.lottery.common.utils.DateMatcherAssist;
import io.lottery.common.utils.DateUtils;
import io.lottery.common.utils.LiuheUtils;

/**
 * 六合彩处理类
 * 
 * @desc
 * @author lj
 * @author 2018年9月19日
 */
@Service
public class LotteryLhcService {

	private Logger logger = LoggerFactory.getLogger(LotteryLhcService.class);

	@Resource
	private RedisService redisservice;

	@Resource
	private LotteryMapper lotteryMapper;

	@Resource
	private LotteryProcessMapper lotteryProcessMapper;

	/**
	 * 处理六合彩数据
	 * 
	 * @desc
	 * @author lj
	 * @param key
	 * @param responseStr
	 * @author 2018年9月19日
	 */
	public void getLhcKjData(String key, String responseStr) {
		Map<String, Object> map = null;
		if ("lhcapi1680660".equals(key)) {
			map = get1680660KjData(responseStr);
		} else if ("lhcapi6hkb".equals(key)) {
			map = get6hkbKjData(responseStr);
		}

		if (map != null) {
			// 当前期数
			String dqqs = (String) map.get("dqqs");
			// 当前开奖时间
			String dqkjsj = (String) map.get("dqkjsj");
			// 下期开奖时间
			Long xqkjsj = Long.valueOf(String.valueOf(map.get("xqkjsj")));
			Date xqkjDate = new Date(xqkjsj);
			String xqkjDateStr = DateUtil.dateString(xqkjDate);

			String lottId = LotteryConstant.LHC;
			// 表名
			String tableName = LotteryConstant.tableNameMap.get(lottId);
			// 查询最新一期是否存在
			int count = lotteryMapper.findCountByTimePeriods(tableName, dqqs, dqkjsj);

			// 判断是否为当前期数
			if (count < 1) {
				@SuppressWarnings("unchecked")
				List<String> numList = (List<String>) map.get("dqhm");
				// 设置开奖时间
				Date kjDate = DateUtils.stringToDate(dqkjsj, DateUtils.DATE_PATTERN);

				StringBuilder numsb = new StringBuilder();
				// 更新缓存中的数据
				// 球的颜色
				List<String> boseList = new ArrayList<String>();
				// 12生肖
				List<String> sxlist = new ArrayList<String>();
				// 获取五行
				List<String> wxlist = new ArrayList<String>();
				for (String num : numList) {
					boseList.add(LiuheUtils.getBoSe(num));
					sxlist.add(LiuheUtils.getNumSx(Integer.valueOf(num), kjDate));
					wxlist.add(LiuheUtils.getNumWx(Integer.valueOf(num), kjDate));
					// 判断是否为单个字符，如果单个字符，补全成两位
					if (num.length() == 1) {
						num = "0" + num;
					}
					numsb.append(",'").append(num).append("'");
				}
				// 截取字符串
				String numStr = numsb.toString();
				if (numStr != null && StringUtils.isNotBlank(numStr) && numStr.startsWith(",")) {
					numStr = numStr.substring(1);
				}
				map.put("boserank", boseList);
				map.put("sxrank", sxlist);
				map.put("wxrank", wxlist);

				// 保存数据到数据库
				Map<String, Object> sqlmap = new HashMap<String, Object>();
				// 如果此彩种在我们系统表里面没有，那么就不做采集
				if (StringUtils.isBlank(tableName)) {
					return;
				}
				sqlmap.put("tableName", tableName);// 表名，根据类型去字典获取
				sqlmap.put("periods", dqqs);// 开奖期数

				Timestamp time = DateUtil.string2Timestamp(dqkjsj, DateUtil.PATTERN_DATE);
				sqlmap.put("time", time);// 开奖时间
				sqlmap.put("starttime", xqkjDateStr);
				sqlmap.put("insertNum", LotteryConstant.lotteryColumnStrMap.get(lottId));// 根据球数修改插入的字段数
				// sqlmap.put("insertNumValue",
				// LotteryUtil.getInsertNumByLottIdAndOpenCode(numStr, lottId));// 根据球数修改插入的字段值
				sqlmap.put("insertNumValue", numStr);
				sqlmap.put("createtime", DateUtil.dateString(new Date()));

				// 在查询一次是否已经保存
				int count2 = lotteryMapper.findCountByTimePeriods(tableName, dqqs, dqkjsj);
				if (count2 < 1) {
					// 保存数据到数据库中
					lotteryMapper.insertByMap(sqlmap);
				}

				// 放到redis中
				redisservice.set(LotteryDict.LHC + "_" + LotteryDict.zxkj, JSON.toJSONString(map));

				// 执行数据更新
				this.execute();
			} else {
				// 如果爬取数据后，发现下期开奖时间不一致，则进行时间更新
				// 将最近的开奖时间更新上去
				LiuheVo vo = getTheLastOne();
				// 本期开奖时间
				String dqkjsjStr = DateUtils.format(vo.getTime(), DateUtils.DATE_PATTERN);
				// 下期开奖时间
				String xqkjsjStr = DateUtils.format(vo.getStarttime(), DateUtils.DATE_TIME_PATTERN);

				// 如果数据库下期开奖时间不一致
				if (vo.getPeriods().equals(dqqs) && dqkjsjStr.equals(dqkjsj) && !xqkjDateStr.equals(xqkjsjStr)) {
					// 保存到数据库
					lotteryMapper.updateProcessStarttimeByTimePeriods(tableName, dqqs, dqkjsj, xqkjDateStr);
					String str = redisservice.get(LotteryDict.LHC + "_" + LotteryDict.zxkj);
					JSONObject obj = JSON.parseObject(str);
					obj.put("xqkjsj", xqkjDate.getTime());
					// 更新下期开奖时间
					redisservice.set(LotteryDict.LHC + "_" + LotteryDict.zxkj, JSON.toJSONString(obj));
				}
			}

		}
	}

	/**
	 * 从www.6hkb.com中获取数据
	 * 
	 * @desc
	 * @author lj
	 * @author 2018年9月19日
	 */
	public Map<String, Object> get6hkbKjData(String responseStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		Document doc = Jsoup.parse(responseStr);
		// 最新开奖
		Element latest = doc.getElementById("j-latest-lottery");
		Elements nextTimes = latest.getElementsByClass("next-time");
		Elements word = nextTimes.select("div.word");
		// 下期开奖时间
		String nextDateEl = word.text();
		String nextDateStr = DateMatcherAssist.getDate(nextDateEl, "-");
		nextDateStr += " 21:30:00";// 每次开奖时间为晚上9:30
		Date nextDate = DateUtils.stringToDate(nextDateStr, DateUtils.DATE_TIME_PATTERN);
		map.put("xqkjsj", nextDate.getTime());
		// 下期期数
		Elements nextperiodEls = word.select("span.period");
		String nextperiod = nextperiodEls.get(0).text();
		map.put("xqqs", nextperiod);
		// 最新开奖
		Elements lastperiod = latest.getElementsByClass("last-period");
		// 最新开奖期数
		Elements lastperiodEls = lastperiod.select("span.j-latest-period");
		String lastperiodStr = lastperiodEls.get(0).text();
		map.put("dqqs", lastperiodStr);
		// 已开期数
		map.put("ykqs", Integer.valueOf(lastperiodStr));
		// 开奖日期
		Elements lastdateEls = lastperiod.select("p.j-latest-date");
		String lastdateStr = lastdateEls.get(0).text();
		map.put("dqkjsj", lastdateStr);

		// 获取最新开奖号码
		List<String> numList = new ArrayList<String>();
		Elements numEls = latest.select("div.last-num").select("span.ball-lg");
		if (numEls != null && numEls.size() > 0) {
			Iterator<Element> numIt = numEls.iterator();
			while (numIt.hasNext()) {
				Element e = numIt.next();
				String str = e.text();
				// 一位数字补全成两位数字
				if (str.length() == 1) {
					str = "0" + str;
				}
				numList.add(str);
			}
		}
		map.put("dqhm", numList);
		return map;

	}

	/**
	 * 从https://1680660.com/smallSix/findSmallSixInfo.do获取开奖数据 解析获取的数据
	 * 
	 * @desc
	 * @author lj
	 * @return
	 * @author 2018年9月19日
	 */
	public Map<String, Object> get1680660KjData(String responseStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject json = JSON.parseObject(responseStr);
		JSONObject result = json.getJSONObject("result");
		JSONObject data = result.getJSONObject("data");

		// 当前期数
		String dqqs = data.getString("preDrawIssue");
		dqqs = dqqs.substring(dqqs.length() - 3, dqqs.length());
		map.put("dqqs", dqqs);
		// 已开期数
		map.put("ykqs", Integer.valueOf(dqqs));

		// 下期期数
		String xqqs = data.getString("drawIssue");
		xqqs = xqqs.substring(xqqs.length() - 3, xqqs.length());
		map.put("xqqs", xqqs);

		// 当前开奖时间
		String dqkjsj = data.getString("preDrawTime");
		map.put("dqkjsj", dqkjsj.split(" ")[0]);
		// 开奖号码
		String haoma = data.getString("preDrawCode");
		String[] hmArr = haoma.split(",");
		// 补全号码为两位
		if (hmArr != null && hmArr.length > 0) {
			for (int i = 0; i < hmArr.length; i++) {
				if (hmArr[i].length() == 1) {
					hmArr[i] = "0" + hmArr[i];
				}
			}
		}

		map.put("dqhm", Arrays.asList(hmArr));
		// 一年的总期数
		Integer myqs = data.getInteger("sumTotal");
		map.put("myqs", myqs);
		// 下期开奖时间
		String xqkjsj = data.getString("drawTime");
		Date nextDate = DateUtils.stringToDate(xqkjsj, DateUtils.DATE_TIME_PATTERN);
		map.put("xqkjsj", nextDate.getTime());

		return map;
	}

	/**
	 * 生成前端接口需要数据
	 * 
	 * @author ms
	 * @date 2018年9月11日 上午10:52:27
	 */
	public void execute() {
		this.lotteryVideo();// 视频数据
		this.getYears();// 生成年份
		// 表名
		String tableName = LotteryConstant.tableNameMap.get(LotteryConstant.LHC);
		// 将map中的数据组装成实体对象
		List<LiuheVo> volist = this.findPage(tableName, 0, 100);
		// 倒序一次是为了遗漏统计数的排序
		Collections.reverse(volist);
		this.hmzs(volist);
		this.tm_bszs(volist);
		this.tm_sxzs(volist);
		this.tm_dszs(volist);
		this.tm_dwzs(volist);
		this.tm_tszs(volist);
		this.tm_wszs(volist);
		this.tm_wxzs(volist);
		this.genKjjl(volist);

	}

	/**
	 * 组合vo数据
	 * 
	 * @desc
	 * @author lj
	 * @param map
	 * @return
	 * @author 2018年9月20日
	 */
	private LiuheVo buildLiuheVo(Map<String, Object> map) {
		LiuheVo vo = new LiuheVo();
		// vo.setId(Integer.valueOf(String.valueOf(map.get("id"))));
		vo.setNum1(String.valueOf(map.get("num1")));
		vo.setNum2(String.valueOf(map.get("num2")));
		vo.setNum3(String.valueOf(map.get("num3")));
		vo.setNum4(String.valueOf(map.get("num4")));
		vo.setNum5(String.valueOf(map.get("num5")));
		vo.setNum6(String.valueOf(map.get("num6")));
		vo.setNum7(String.valueOf(map.get("num7")));
		vo.setPeriods(String.valueOf(map.get("periods")));

		// 开奖时间
		if (map.get("time") != null) {
			Date time = (Date) map.get("time");
			vo.setTime(time);
		}
		if (map.get("starttime") != null) {
			Date starttime = (Date) map.get("starttime");
			vo.setStarttime(starttime);
		}
		if (map.get("processtime") != null) {
			Date processtime = (Date) map.get("processtime");
			vo.setProcesstime(processtime);
		}

		return vo;
	}

	/**
	 * 生成最新100期号码走势
	 */
	public void hmzs(List<LiuheVo> list) {
		JSONArray rearray = new JSONArray();
		for (LiuheVo entity : list) {
			JSONObject obj = new JSONObject();
			String year = DateUtils.format(entity.getTime(), DateUtils.DATE_YEAR);
			// 期数
			obj.put("qs", year + "/" + entity.getPeriods() + "期");
			// 特码
			obj.put("tm", entity.getNum7());
			obj.put("tmbs", LiuheUtils.getBoSe(entity.getNum7()));// 特码波色
			obj.put("num1", entity.getNum1());
			obj.put("num1bs", LiuheUtils.getBoSe(entity.getNum1()));
			obj.put("num2", entity.getNum2());
			obj.put("num2bs", LiuheUtils.getBoSe(entity.getNum2()));
			obj.put("num3", entity.getNum3());
			obj.put("num3bs", LiuheUtils.getBoSe(entity.getNum3()));
			obj.put("num4", entity.getNum4());
			obj.put("num4bs", LiuheUtils.getBoSe(entity.getNum4()));
			obj.put("num5", entity.getNum5());
			obj.put("num5bs", LiuheUtils.getBoSe(entity.getNum5()));
			obj.put("num6", entity.getNum6());
			obj.put("num6bs", LiuheUtils.getBoSe(entity.getNum6()));
			obj.put("num7", entity.getNum7());
			obj.put("num7bs", LiuheUtils.getBoSe(entity.getNum7()));
			rearray.add(obj);
		}
		// 出现次数
		JSONArray countarray = new JSONArray();
		for (String num : LiuheUtils.AllNumber) {
			int count = 0;
			for (LiuheVo entity : list) {
				if (num.equals(entity.getNum1()))
					++count;
				if (num.equals(entity.getNum2()))
					++count;
				if (num.equals(entity.getNum3()))
					++count;
				if (num.equals(entity.getNum4()))
					++count;
				if (num.equals(entity.getNum5()))
					++count;
				if (num.equals(entity.getNum6()))
					++count;
				if (num.equals(entity.getNum7()))
					++count;
			}
			countarray.add(count);
		}
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("hmzs", rearray);// 号码走势
			resobj.put("hmcs", countarray);// 号码出现次数
			redisservice.set(LotteryDict.LHC + "_" + LotteryDict.hmzs, resobj.toJSONString());
		}
	}

	/**
	 * 生成最新100期特码波色走势
	 */
	public void tm_bszs(List<LiuheVo> list) {
		Map<String, Integer> countmap = new LinkedHashMap<String, Integer>();
		countmap.put("红", 0);
		countmap.put("蓝", 0);
		countmap.put("绿", 0);
		countmap.put("红单", 0);
		countmap.put("蓝单", 0);
		countmap.put("绿单", 0);
		countmap.put("红双", 0);
		countmap.put("蓝双", 0);
		countmap.put("绿双", 0);
		int redyl = 0, blueyl = 0, greenyl = 0, redsigyl = 0, bluesigyl = 0, greensigyl = 0, reddoubyl = 0,
				bluedoubyl = 0, greendoubyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheVo entity : list) {
			JSONObject obj = new JSONObject();
			Integer tm = Integer.valueOf(entity.getNum7());
			// 特码
			obj.put("tm", entity.getNum7());
			String year = DateUtils.format(entity.getTime(), DateUtils.DATE_YEAR);
			if (tm % 2 == 1) {
				obj.put("ds", "单");
			} else {
				obj.put("ds", "双");
			}
			// 期数
			obj.put("qs", year + "/" + entity.getPeriods() + "期");
			// 波色
			if (LiuheUtils.RedList.contains(tm)) {
				obj.put("bs", "red");
				obj.put("ys", "红");
				redyl = 0;
				++blueyl;
				++greenyl;
				countmap.put("红", 1 + countmap.get("红"));
				if (tm % 2 == 1) {
					countmap.put("红单", 1 + countmap.get("红单"));
					redsigyl = 0;
					++bluesigyl;
					++greensigyl;
					++reddoubyl;
					++bluedoubyl;
					++greendoubyl;
				} else {
					countmap.put("红双", 1 + countmap.get("红双"));
					++redsigyl;
					++bluesigyl;
					++greensigyl;
					reddoubyl = 0;
					++bluedoubyl;
					++greendoubyl;
				}
			} else if (LiuheUtils.GreenList.contains(tm)) {
				obj.put("bs", "green");
				obj.put("ys", "绿");
				greenyl = 0;
				++blueyl;
				++redyl;
				countmap.put("绿", 1 + countmap.get("绿"));
				if (tm % 2 == 1) {
					countmap.put("绿单", 1 + countmap.get("绿单"));
					++redsigyl;
					++bluesigyl;
					greensigyl = 0;
					++reddoubyl;
					++bluedoubyl;
					++greendoubyl;
				} else {
					countmap.put("绿双", 1 + countmap.get("绿双"));
					++redsigyl;
					++bluesigyl;
					++greensigyl;
					++reddoubyl;
					bluedoubyl = 0;
					++greendoubyl;
				}
			} else if (LiuheUtils.BlueList.contains(tm)) {
				obj.put("bs", "blue");
				obj.put("ys", "蓝");
				blueyl = 0;
				++redyl;
				++greenyl;
				countmap.put("蓝", 1 + countmap.get("蓝"));
				if (tm % 2 == 1) {
					countmap.put("蓝单", 1 + countmap.get("蓝单"));
					++redsigyl;
					bluesigyl = 0;
					++greensigyl;
					++reddoubyl;
					++bluedoubyl;
					++greendoubyl;
				} else {
					countmap.put("蓝双", 1 + countmap.get("蓝双"));
					++redsigyl;
					++bluesigyl;
					++greensigyl;
					++reddoubyl;
					++bluedoubyl;
					greendoubyl = 0;
				}
			}
			// 遗漏
			obj.put("redyl", redyl);
			obj.put("blueyl", blueyl);
			obj.put("greenyl", greenyl);
			obj.put("redsigyl", redsigyl);
			obj.put("bluesigyl", bluesigyl);
			obj.put("greensigyl", greensigyl);
			obj.put("reddoubyl", reddoubyl);
			obj.put("bluedoubyl", bluedoubyl);
			obj.put("greendoubyl", greendoubyl);
			rearray.add(obj);
		}

		JSONArray countarray = new JSONArray();
		for (Object count : countmap.values()) {
			countarray.add(count);
		}
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("bszs", rearray);// 波色走势
			resobj.put("bscs", countarray);// 波色出现次数
			// resobj.put("bscs", new JSONObject(countmap));// 波色出现次数
			redisservice.set(LotteryDict.LHC + "_" + LotteryDict.bszs, resobj.toJSONString());
		}

	}

	/**
	 * 生成最新100期特码生肖+家野走势
	 */
	public void tm_sxzs(List<LiuheVo> list) {
		Map<String, Integer> countmap = new LinkedHashMap<String, Integer>();
		countmap.put("鼠", 0);
		countmap.put("牛", 0);
		countmap.put("虎", 0);
		countmap.put("兔", 0);
		countmap.put("龙", 0);
		countmap.put("蛇", 0);
		countmap.put("马", 0);
		countmap.put("羊", 0);
		countmap.put("猴", 0);
		countmap.put("鸡", 0);
		countmap.put("狗", 0);
		countmap.put("猪", 0);
		countmap.put("家禽", 0);
		countmap.put("野兽", 0);
		int shuyl = 0, niuyl = 0, huyl = 0, tuyl = 0, longyl = 0, sheyl = 0, mayl = 0, yangyl = 0, houyl = 0, jiyl = 0,
				gouyl = 0, zhuyl = 0, jiaqinyl = 0, yeshouyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheVo entity : list) {
			JSONObject obj = new JSONObject();
			Integer tm = Integer.valueOf(entity.getNum7());
			String year = DateUtils.format(entity.getTime(), DateUtils.DATE_YEAR);
			// 期数
			obj.put("qs", year + "/" + entity.getPeriods() + "期");
			// 特码
			obj.put("tm", entity.getNum7());
			obj.put("bs", LiuheUtils.getBoSe(entity.getNum7()));
			// 生肖
			// Map<String,List<Integer>> sxmap=LiuheConfig.LiuheSxMap.get(year);
			Map<String, List<Integer>> sxmap = LiuheUtils.getSx(Integer.valueOf(year));
			for (Map.Entry<String, List<Integer>> entry : sxmap.entrySet()) {
				if (entry.getValue().contains(tm)) {
					String key = entry.getKey();
					obj.put("sx", key);
					// 生肖出现次数+1
					countmap.put(key, 1 + countmap.get(key));
					// 遗漏值计算
					if ("鼠".equals(key)) {
						shuyl = 0;
						++niuyl;
						++huyl;
						++tuyl;
						++longyl;
						++sheyl;
						++mayl;
						++yangyl;
						++houyl;
						++jiyl;
						++gouyl;
						++zhuyl;
					} else if ("牛".equals(key)) {
						niuyl = 0;
						++shuyl;
						++huyl;
						++tuyl;
						++longyl;
						++sheyl;
						++mayl;
						++yangyl;
						++houyl;
						++jiyl;
						++gouyl;
						++zhuyl;
					} else if ("虎".equals(key)) {
						huyl = 0;
						++shuyl;
						++niuyl;
						++tuyl;
						++longyl;
						++sheyl;
						++mayl;
						++yangyl;
						++houyl;
						++jiyl;
						++gouyl;
						++zhuyl;
					} else if ("兔".equals(key)) {
						tuyl = 0;
						++shuyl;
						++niuyl;
						++huyl;
						++longyl;
						++sheyl;
						++mayl;
						++yangyl;
						++houyl;
						++jiyl;
						++gouyl;
						++zhuyl;
					} else if ("龙".equals(key)) {
						longyl = 0;
						++shuyl;
						++niuyl;
						++huyl;
						++tuyl;
						++sheyl;
						++mayl;
						++yangyl;
						++houyl;
						++jiyl;
						++gouyl;
						++zhuyl;
					} else if ("蛇".equals(key)) {
						sheyl = 0;
						++shuyl;
						++niuyl;
						++huyl;
						++tuyl;
						++longyl;
						++mayl;
						++yangyl;
						++houyl;
						++jiyl;
						++gouyl;
						++zhuyl;
					} else if ("马".equals(key)) {
						mayl = 0;
						++shuyl;
						++niuyl;
						++huyl;
						++tuyl;
						++longyl;
						++sheyl;
						++yangyl;
						++houyl;
						++jiyl;
						++gouyl;
						++zhuyl;
					} else if ("羊".equals(key)) {
						yangyl = 0;
						++shuyl;
						++niuyl;
						++huyl;
						++tuyl;
						++longyl;
						++sheyl;
						++mayl;
						++houyl;
						++jiyl;
						++gouyl;
						++zhuyl;
					} else if ("猴".equals(key)) {
						houyl = 0;
						++shuyl;
						++niuyl;
						++huyl;
						++tuyl;
						++longyl;
						++sheyl;
						++mayl;
						++yangyl;
						++jiyl;
						++gouyl;
						++zhuyl;
					} else if ("鸡".equals(key)) {
						jiyl = 0;
						++shuyl;
						++niuyl;
						++huyl;
						++tuyl;
						++longyl;
						++sheyl;
						++mayl;
						++yangyl;
						++houyl;
						++gouyl;
						++zhuyl;
					} else if ("狗".equals(key)) {
						gouyl = 0;
						++shuyl;
						++niuyl;
						++huyl;
						++tuyl;
						++longyl;
						++sheyl;
						++mayl;
						++yangyl;
						++houyl;
						++jiyl;
						++zhuyl;
					} else if ("猪".equals(key)) {
						zhuyl = 0;
						++shuyl;
						++niuyl;
						++huyl;
						++tuyl;
						++longyl;
						++sheyl;
						++mayl;
						++yangyl;
						++houyl;
						++jiyl;
						++gouyl;
					}
					// 家野出现次数+1
					if (LiuheUtils.JQList.contains(key)) {
						countmap.put("家禽", 1 + countmap.get("家禽"));
						obj.put("jy", "家禽");
						jiaqinyl = 0;
						++yeshouyl;
					} else if (LiuheUtils.YSList.contains(key)) {
						countmap.put("野兽", 1 + countmap.get("野兽"));
						obj.put("jy", "野兽");
						yeshouyl = 0;
						++jiaqinyl;
					}
					// 遗漏
					obj.put("shuyl", shuyl);
					obj.put("niuyl", niuyl);
					obj.put("huyl", huyl);
					obj.put("tuyl", tuyl);
					obj.put("longyl", longyl);
					obj.put("sheyl", sheyl);
					obj.put("mayl", mayl);
					obj.put("yangyl", yangyl);
					obj.put("houyl", houyl);
					obj.put("jiyl", jiyl);
					obj.put("gouyl", gouyl);
					obj.put("zhuyl", zhuyl);
					obj.put("jiaqinyl", jiaqinyl);
					obj.put("yeshouyl", yeshouyl);
					break;
				}
			}
			rearray.add(obj);
		}
		JSONArray countarray = new JSONArray();
		for (Object count : countmap.values()) {
			countarray.add(count);
		}
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("sxzs", rearray);// 生肖走势
			resobj.put("sxcs", countarray);// 生肖家野出现次数
			// resobj.put("sxcs", new JSONObject(countmap));// 生肖家野出现次数
			redisservice.set(LotteryDict.LHC + "_" + LotteryDict.sxzs, resobj.toJSONString());
		}

	}

	/**
	 * 生成最新100期特码合数+单双走势
	 */
	public void tm_dszs(List<LiuheVo> list) {
		Map<String, Integer> countmap = new LinkedHashMap<String, Integer>();
		countmap.put("单", 0);
		countmap.put("双", 0);
		countmap.put("头单", 0);
		countmap.put("头双", 0);
		countmap.put("合单", 0);
		countmap.put("合双", 0);
		int danyl = 0, shuangyl = 0, toudanyl = 0, toushuangyl = 0, hedanyl = 0, heshuangyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheVo entity : list) {
			JSONObject obj = new JSONObject();
			Integer tm = Integer.valueOf(entity.getNum7());
			// 特码
			obj.put("tm", entity.getNum7());
			String year = DateUtils.format(entity.getTime(), DateUtils.DATE_YEAR);
			// 期数
			obj.put("qs", year + "/" + entity.getPeriods() + "期");
			obj.put("bs", LiuheUtils.getBoSe(entity.getNum7()));
			// 单双
			if (tm % 2 == 1) {
				obj.put("ds", "单");
				countmap.put("单", 1 + countmap.get("单"));
				danyl = 0;
				++shuangyl;
			} else {
				obj.put("ds", "双");
				countmap.put("双", 1 + countmap.get("双"));
				shuangyl = 0;
				++danyl;
			}
			// 特头单双
			if (tm < 10) {// 10的特头是0 标记为双
				obj.put("ttds", "双");
				countmap.put("头双", 1 + countmap.get("头双"));
				toushuangyl = 0;
				++toudanyl;
			} else {
				if (Integer.valueOf(tm.toString().substring(0, 1)) % 2 == 1) {
					obj.put("ttds", "单");
					countmap.put("头单", 1 + countmap.get("头单"));
					toudanyl = 0;
					++toushuangyl;
				} else {
					obj.put("ttds", "双");
					countmap.put("头双", 1 + countmap.get("头双"));
					toushuangyl = 0;
					++toudanyl;
				}
			}
			// 合数单双
			if (tm < 10) {// 10以内直接算
				if (tm % 2 == 1) {
					obj.put("hsds", "单");
					countmap.put("合单", 1 + countmap.get("合单"));
					hedanyl = 0;
					++heshuangyl;
				} else {
					obj.put("hsds", "双");
					countmap.put("合双", 1 + countmap.get("合双"));
					heshuangyl = 0;
					++hedanyl;
				}
			} else {
				// 合数值
				Integer val = Integer.valueOf(tm.toString().substring(0, 1))
						+ Integer.valueOf(tm.toString().substring(1, 2));
				if (val % 2 == 1) {
					obj.put("hsds", "单");
					countmap.put("合单", 1 + countmap.get("合单"));
					hedanyl = 0;
					++heshuangyl;
				} else {
					obj.put("hsds", "双");
					countmap.put("合双", 1 + countmap.get("合双"));
					heshuangyl = 0;
					++hedanyl;
				}
			}
			obj.put("danyl", danyl);
			obj.put("shuangyl", shuangyl);
			obj.put("toudanyl", toudanyl);
			obj.put("toushuangyl", toushuangyl);
			obj.put("hedanyl", hedanyl);
			obj.put("heshuangyl", heshuangyl);
			rearray.add(obj);
		}
		JSONArray countarray = new JSONArray();
		for (Object count : countmap.values()) {
			countarray.add(count);
		}
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("dszs", rearray);// 合数+单双走势
			resobj.put("dscs", countarray);// 合数+单双出现次数
			// resobj.put("dscs", new JSONObject(countmap));// 合数+单双出现次数
			redisservice.set(LotteryDict.LHC + "_" + LotteryDict.dszs, resobj.toJSONString());
		}

	}

	/**
	 * 生成最新100期特码段位走势
	 */
	public void tm_dwzs(List<LiuheVo> list) {
		Map<String, Integer> countmap = new LinkedHashMap<String, Integer>();
		countmap.put("第一段", 0);
		countmap.put("第二段", 0);
		countmap.put("第三段", 0);
		countmap.put("第四段", 0);
		countmap.put("第五段", 0);
		countmap.put("第六段", 0);
		countmap.put("第七段", 0);
		int yiduanyl = 0, erduanyl = 0, sanduanyl = 0, siduanyl = 0, wuduanyl = 0, liuduanyl = 0, qiduanyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheVo entity : list) {
			JSONObject obj = new JSONObject();
			Integer tm = Integer.valueOf(entity.getNum7());
			// 特码
			obj.put("tm", entity.getNum7());
			String year = DateUtils.format(entity.getTime(), DateUtils.DATE_YEAR);
			// 期数
			obj.put("qs", year + "/" + entity.getPeriods() + "期");
			// 波色
			obj.put("bs", LiuheUtils.getBoSe(entity.getNum7()));
			// 段位
			if (LiuheUtils.OneSection.contains(tm)) {
				yiduanyl = 0;
				++erduanyl;
				++sanduanyl;
				++siduanyl;
				++wuduanyl;
				++liuduanyl;
				++qiduanyl;
				countmap.put("第一段", 1 + countmap.get("第一段"));
			} else if (LiuheUtils.TwoSection.contains(tm)) {
				++yiduanyl;
				erduanyl = 0;
				++sanduanyl;
				++siduanyl;
				++wuduanyl;
				++liuduanyl;
				++qiduanyl;
				countmap.put("第二段", 1 + countmap.get("第二段"));
			} else if (LiuheUtils.ThreeSection.contains(tm)) {
				++yiduanyl;
				++erduanyl;
				sanduanyl = 0;
				++siduanyl;
				++wuduanyl;
				++liuduanyl;
				++qiduanyl;
				countmap.put("第三段", 1 + countmap.get("第三段"));
			} else if (LiuheUtils.FourSection.contains(tm)) {
				++yiduanyl;
				++erduanyl;
				++sanduanyl;
				siduanyl = 0;
				++wuduanyl;
				++liuduanyl;
				++qiduanyl;
				countmap.put("第四段", 1 + countmap.get("第四段"));
			} else if (LiuheUtils.FiveSection.contains(tm)) {
				++yiduanyl;
				++erduanyl;
				++sanduanyl;
				++siduanyl;
				wuduanyl = 0;
				++liuduanyl;
				++qiduanyl;
				countmap.put("第五段", 1 + countmap.get("第五段"));
			} else if (LiuheUtils.SixSection.contains(tm)) {
				++yiduanyl;
				++erduanyl;
				++sanduanyl;
				++siduanyl;
				++wuduanyl;
				liuduanyl = 0;
				++qiduanyl;
				countmap.put("第六段", 1 + countmap.get("第六段"));
			} else if (LiuheUtils.SevenSection.contains(tm)) {
				++yiduanyl;
				++erduanyl;
				++sanduanyl;
				++siduanyl;
				++wuduanyl;
				++liuduanyl;
				qiduanyl = 0;
				countmap.put("第七段", 1 + countmap.get("第七段"));
			}
			obj.put("yiduanyl", yiduanyl);
			obj.put("erduanyl", erduanyl);
			obj.put("sanduanyl", sanduanyl);
			obj.put("siduanyl", siduanyl);
			obj.put("wuduanyl", wuduanyl);
			obj.put("liuduanyl", liuduanyl);
			obj.put("qiduanyl", qiduanyl);
			rearray.add(obj);
		}
		JSONArray countarray = new JSONArray();
		for (Object count : countmap.values()) {
			countarray.add(count);
		}
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("dwzs", rearray);// 段位走势
			resobj.put("dwcs", countarray);// 段位出现次数
			// resobj.put("dwcs", new JSONObject(countmap));// 段位出现次数
			redisservice.set(LotteryDict.LHC + "_" + LotteryDict.dwzs, resobj.toJSONString());
		}
	}

	/**
	 * 生成最新100期特码头数走势
	 */
	public void tm_tszs(List<LiuheVo> list) {
		Map<String, Integer> countmap = new LinkedHashMap<String, Integer>();
		countmap.put("0头", 0);
		countmap.put("1头", 0);
		countmap.put("2头", 0);
		countmap.put("3头", 0);
		countmap.put("4头", 0);
		countmap.put("头双(0-2-4)", 0);
		countmap.put("头单(1-3)", 0);
		int lingtouyl = 0, yitouyl = 0, ertouyl = 0, santouyl = 0, sitouyl = 0, toushuangyl = 0, toudanyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheVo entity : list) {
			JSONObject obj = new JSONObject();
			Integer tm = Integer.valueOf(entity.getNum7());
			// 特码
			obj.put("tm", entity.getNum7());
			String year = DateUtils.format(entity.getTime(), DateUtils.DATE_YEAR);
			// 期数
			obj.put("qs", year + "/" + entity.getPeriods() + "期");
			// 波色
			obj.put("bs", LiuheUtils.getBoSe(entity.getNum7()));
			// 特头走势
			if (tm < 10) {
				lingtouyl = 0;
				++yitouyl;
				++ertouyl;
				++santouyl;
				++sitouyl;
				countmap.put("0头", 1 + countmap.get("0头"));
				toushuangyl = 0;
				++toudanyl;
				countmap.put("头双(0-2-4)", 1 + countmap.get("头双(0-2-4)"));
				obj.put("ttds", "双");
			} else {
				if ("1".equals(tm.toString().substring(0, 1))) {
					++lingtouyl;
					yitouyl = 0;
					++ertouyl;
					++santouyl;
					++sitouyl;
					countmap.put("1头", 1 + countmap.get("1头"));
					toudanyl = 0;
					++toushuangyl;
					countmap.put("头单(1-3)", 1 + countmap.get("头单(1-3)"));
					obj.put("ttds", "单");
				} else if ("2".equals(tm.toString().substring(0, 1))) {
					++lingtouyl;
					++yitouyl;
					ertouyl = 0;
					++santouyl;
					++sitouyl;
					countmap.put("2头", 1 + countmap.get("2头"));
					toushuangyl = 0;
					++toudanyl;
					countmap.put("头双(0-2-4)", 1 + countmap.get("头双(0-2-4)"));
					obj.put("ttds", "双");
				} else if ("3".equals(tm.toString().substring(0, 1))) {
					++lingtouyl;
					++yitouyl;
					++ertouyl;
					santouyl = 0;
					++sitouyl;
					countmap.put("3头", 1 + countmap.get("3头"));
					toudanyl = 0;
					++toushuangyl;
					countmap.put("头单(1-3)", 1 + countmap.get("头单(1-3)"));
					obj.put("ttds", "单");
				} else if ("4".equals(tm.toString().substring(0, 1))) {
					++lingtouyl;
					++yitouyl;
					++ertouyl;
					++santouyl;
					sitouyl = 0;
					countmap.put("4头", 1 + countmap.get("4头"));
					toushuangyl = 0;
					++toudanyl;
					countmap.put("头双(0-2-4)", 1 + countmap.get("头双(0-2-4)"));
					obj.put("ttds", "双");
				}
			}
			obj.put("lingtouyl", lingtouyl);
			obj.put("yitouyl", yitouyl);
			obj.put("ertouyl", ertouyl);
			obj.put("santouyl", santouyl);
			obj.put("sitouyl", sitouyl);
			obj.put("toushuangyl", toushuangyl);
			obj.put("toudanyl", toudanyl);
			rearray.add(obj);
		}
		JSONArray countarray = new JSONArray();
		for (Object count : countmap.values()) {
			countarray.add(count);
		}
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("tszs", rearray);// 头数走势
			resobj.put("tscs", countarray);// 头数出现次数
			// resobj.put("tscs", new JSONObject(countmap));// 头数出现次数
			redisservice.set(LotteryDict.LHC + "_" + LotteryDict.tszs, resobj.toJSONString());
		}
	}

	/**
	 * 生成最新100期特码尾数走势
	 */
	public void tm_wszs(List<LiuheVo> list) {
		Map<String, Integer> countmap = new LinkedHashMap<String, Integer>();
		countmap.put("0尾", 0);
		countmap.put("1尾", 0);
		countmap.put("2尾", 0);
		countmap.put("3尾", 0);
		countmap.put("4尾", 0);
		countmap.put("5尾", 0);
		countmap.put("6尾", 0);
		countmap.put("7尾", 0);
		countmap.put("8尾", 0);
		countmap.put("9尾", 0);
		countmap.put("小(0-4)", 0);
		countmap.put("大(5-9)", 0);
		int lingweiyl = 0, yiweiyl = 0, erweiyl = 0, sanweiyl = 0, siweiyl = 0, wuweiyl = 0, liuweiyl = 0, qiweiyl = 0,
				baweiyl = 0, jiuweiyl = 0, weixiaoyl = 0, weidayl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheVo entity : list) {
			JSONObject obj = new JSONObject();
			Integer tm = Integer.valueOf(entity.getNum7());
			// 特码
			obj.put("tm", entity.getNum7());
			String year = DateUtils.format(entity.getTime(), DateUtils.DATE_YEAR);
			// 期数
			obj.put("qs", year + "/" + entity.getPeriods() + "期");
			// 波色
			obj.put("bs", LiuheUtils.getBoSe(entity.getNum7()));
			// 特尾走势
			String val = "";
			if (tm < 10) {
				val = tm.toString();
			} else {
				val = tm.toString().substring(1, 2);
			}
			// 特尾大小
			if (Integer.valueOf(val) > 4) {
				obj.put("twdx", "大");
			} else {
				obj.put("twdx", "小");
			}
			// 特尾号码
			obj.put("tw", val);
			if ("0".equals(val)) {
				lingweiyl = 0;
				++yiweiyl;
				++erweiyl;
				++sanweiyl;
				++siweiyl;
				++wuweiyl;
				++liuweiyl;
				++qiweiyl;
				++baweiyl;
				++jiuweiyl;
				countmap.put("0尾", 1 + countmap.get("0尾"));
				weixiaoyl = 0;
				++weidayl;
				countmap.put("小(0-4)", 1 + countmap.get("小(0-4)"));
			} else if ("1".equals(val)) {
				++lingweiyl;
				yiweiyl = 0;
				++erweiyl;
				++sanweiyl;
				++siweiyl;
				++wuweiyl;
				++liuweiyl;
				++qiweiyl;
				++baweiyl;
				++jiuweiyl;
				countmap.put("1尾", 1 + countmap.get("1尾"));
				weixiaoyl = 0;
				++weidayl;
				countmap.put("小(0-4)", 1 + countmap.get("小(0-4)"));
			} else if ("2".equals(val)) {
				++lingweiyl;
				++yiweiyl;
				erweiyl = 0;
				++sanweiyl;
				++siweiyl;
				++wuweiyl;
				++liuweiyl;
				++qiweiyl;
				++baweiyl;
				++jiuweiyl;
				countmap.put("2尾", 1 + countmap.get("2尾"));
				weixiaoyl = 0;
				++weidayl;
				countmap.put("小(0-4)", 1 + countmap.get("小(0-4)"));
			} else if ("3".equals(val)) {
				++lingweiyl;
				++yiweiyl;
				++erweiyl;
				sanweiyl = 0;
				++siweiyl;
				++wuweiyl;
				++liuweiyl;
				++qiweiyl;
				++baweiyl;
				++jiuweiyl;
				countmap.put("3尾", 1 + countmap.get("3尾"));
				weixiaoyl = 0;
				++weidayl;
				countmap.put("小(0-4)", 1 + countmap.get("小(0-4)"));
			} else if ("4".equals(val)) {
				++lingweiyl;
				++yiweiyl;
				++erweiyl;
				++sanweiyl;
				siweiyl = 0;
				++wuweiyl;
				++liuweiyl;
				++qiweiyl;
				++baweiyl;
				++jiuweiyl;
				countmap.put("4尾", 1 + countmap.get("4尾"));
				weixiaoyl = 0;
				++weidayl;
				countmap.put("小(0-4)", 1 + countmap.get("小(0-4)"));
			} else if ("5".equals(val)) {
				++lingweiyl;
				++yiweiyl;
				++erweiyl;
				++sanweiyl;
				++siweiyl;
				wuweiyl = 0;
				++liuweiyl;
				++qiweiyl;
				++baweiyl;
				++jiuweiyl;
				countmap.put("5尾", 1 + countmap.get("5尾"));
				++weixiaoyl;
				weidayl = 0;
				countmap.put("大(5-9)", 1 + countmap.get("大(5-9)"));
			} else if ("6".equals(val)) {
				++lingweiyl;
				++yiweiyl;
				++erweiyl;
				++sanweiyl;
				++siweiyl;
				++wuweiyl;
				liuweiyl = 0;
				++qiweiyl;
				++baweiyl;
				++jiuweiyl;
				countmap.put("6尾", 1 + countmap.get("6尾"));
				++weixiaoyl;
				weidayl = 0;
				countmap.put("大(5-9)", 1 + countmap.get("大(5-9)"));
			} else if ("7".equals(val)) {
				++lingweiyl;
				++yiweiyl;
				++erweiyl;
				++sanweiyl;
				++siweiyl;
				++wuweiyl;
				++liuweiyl;
				qiweiyl = 0;
				++baweiyl;
				++jiuweiyl;
				countmap.put("7尾", 1 + countmap.get("7尾"));
				++weixiaoyl;
				weidayl = 0;
				countmap.put("大(5-9)", 1 + countmap.get("大(5-9)"));
			} else if ("8".equals(val)) {
				++lingweiyl;
				++yiweiyl;
				++erweiyl;
				++sanweiyl;
				++siweiyl;
				++wuweiyl;
				++liuweiyl;
				++qiweiyl;
				baweiyl = 0;
				++jiuweiyl;
				countmap.put("8尾", 1 + countmap.get("8尾"));
				++weixiaoyl;
				weidayl = 0;
				countmap.put("大(5-9)", 1 + countmap.get("大(5-9)"));
			} else if ("9".equals(val)) {
				++lingweiyl;
				++yiweiyl;
				++erweiyl;
				++sanweiyl;
				++siweiyl;
				++wuweiyl;
				++liuweiyl;
				++qiweiyl;
				++baweiyl;
				jiuweiyl = 0;
				countmap.put("9尾", 1 + countmap.get("9尾"));
				++weixiaoyl;
				weidayl = 0;
				countmap.put("大(5-9)", 1 + countmap.get("大(5-9)"));
			}
			obj.put("lingweiyl", lingweiyl);
			obj.put("yiweiyl", yiweiyl);
			obj.put("erweiyl", erweiyl);
			obj.put("sanweiyl", sanweiyl);
			obj.put("siweiyl", siweiyl);
			obj.put("wuweiyl", wuweiyl);
			obj.put("liuweiyl", liuweiyl);
			obj.put("qiweiyl", qiweiyl);
			obj.put("baweiyl", baweiyl);
			obj.put("jiuweiyl", jiuweiyl);
			obj.put("weixiaoyl", weixiaoyl);
			obj.put("weidayl", weidayl);
			rearray.add(obj);
		}
		JSONArray countarray = new JSONArray();
		for (Object count : countmap.values()) {
			countarray.add(count);
		}
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("wszs", rearray);// 尾数走势
			resobj.put("wscs", countarray);// 尾数出现次数
			redisservice.set(LotteryDict.LHC + "_" + LotteryDict.wszs, resobj.toJSONString());
		}
	}

	/**
	 * 生成最新100期特码五行走势
	 */
	public void tm_wxzs(List<LiuheVo> list) {
		Map<String, Integer> countmap = new LinkedHashMap<String, Integer>();
		countmap.put("金", 0);
		countmap.put("木", 0);
		countmap.put("水", 0);
		countmap.put("火", 0);
		countmap.put("土", 0);
		int jinyl = 0, muyl = 0, shuiyl = 0, huoyl = 0, tuyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheVo entity : list) {
			JSONObject obj = new JSONObject();
			Integer tm = Integer.valueOf(entity.getNum7());
			// 特码
			obj.put("tm", entity.getNum7());
			String year = DateUtils.format(entity.getTime(), DateUtils.DATE_YEAR);
			// 期数
			obj.put("qs", year + "/" + entity.getPeriods() + "期");
			// 波色
			obj.put("bs", LiuheUtils.getBoSe(entity.getNum7()));

			// 五行
			Map<String, List<Integer>> wxmap = LiuheUtils.getWuxing(Integer.valueOf(year));
			for (Map.Entry<String, List<Integer>> entry : wxmap.entrySet()) {
				if (entry.getValue().contains(tm)) {
					String key = entry.getKey();
					obj.put("wx", key);
					// 五行出现次数+1
					countmap.put(key, 1 + countmap.get(key));
					// 遗漏值计算
					if ("金".equals(key)) {
						jinyl = 0;
						++muyl;
						++shuiyl;
						++huoyl;
						++tuyl;
					} else if ("木".equals(key)) {
						++jinyl;
						muyl = 0;
						++shuiyl;
						++huoyl;
						++tuyl;
					} else if ("水".equals(key)) {
						++jinyl;
						++muyl;
						shuiyl = 0;
						++huoyl;
						++tuyl;
					} else if ("火".equals(key)) {
						++jinyl;
						++muyl;
						++shuiyl;
						huoyl = 0;
						++tuyl;
					} else if ("土".equals(key)) {
						++jinyl;
						++muyl;
						++shuiyl;
						++huoyl;
						tuyl = 0;
					}
				}
			}
			obj.put("jinyl", jinyl);
			obj.put("muyl", muyl);
			obj.put("shuiyl", shuiyl);
			obj.put("huoyl", huoyl);
			obj.put("tuyl", tuyl);
			rearray.add(obj);
		}
		JSONArray countarray = new JSONArray();
		for (Object count : countmap.values()) {
			countarray.add(count);
		}
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("wxzs", rearray);// 五行走势
			resobj.put("wxcs", countarray);// 五行出现次数
			redisservice.set(LotteryDict.LHC + "_" + LotteryDict.wxzs, resobj.toJSONString());
		}
	}

	/**
	 * 生成开奖记录
	 * 
	 * @param volist
	 */
	public void genKjjl(List<LiuheVo> volist) {
		if (volist != null && volist.size() > 0) {
			buildKjjl(volist);
		} else {// 如果没有数据，全部生成数据
			String tableName = LotteryConstant.tableNameMap.get(LotteryConstant.LHC);
			// 分页查找
			int count = lotteryMapper.findCount(tableName);
			// 每页条数
			int limit = 100;
			// 每页100条
			int pageNum = count / limit;
			if (pageNum % limit != 0) {
				pageNum += 1;
			}
			for (int i = 1; i <= pageNum; i++) {
				// //分页查找
				int start = (i - 1) * limit;
				List<LiuheVo> list = this.findPage(tableName, start, limit);
				buildKjjl(list);
			}
		}
	}

	/**
	 * 生成开奖记录数据
	 * 
	 * @desc
	 * @author lj
	 * @param list
	 * @author 2018年9月26日
	 */
	private void buildKjjl(List<LiuheVo> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			for (LiuheVo liuhe : list) {
				// 组装开奖列表数据
				List<String> ranklist = new ArrayList<String>();
				ranklist.add(liuhe.getNum1());
				ranklist.add(liuhe.getNum2());
				ranklist.add(liuhe.getNum3());
				ranklist.add(liuhe.getNum4());
				ranklist.add(liuhe.getNum5());
				ranklist.add(liuhe.getNum6());
				ranklist.add(liuhe.getNum7());
				map.put("rank", ranklist);
				// 12生肖
				List<String> sxlist = new ArrayList<String>();
				// 获取五行
				List<String> wxlist = new ArrayList<String>();
				// 家禽野兽
				List<String> jqysList = new ArrayList<String>();
				// 男女生肖
				List<String> nnsxList = new ArrayList<String>();
				// 天地生肖
				List<String> tdsxList = new ArrayList<String>();
				// 四级生肖
				List<String> sjsxList = new ArrayList<String>();
				// 琴棋书画
				List<String> qqshList = new ArrayList<String>();
				// 三色生肖
				List<String> sssxList = new ArrayList<String>();
				// 球的颜色
				List<String> boseList = new ArrayList<String>();

				for (String num : ranklist) {
					sxlist.add(LiuheUtils.getNumSx(Integer.valueOf(num), liuhe.getTime()));
					wxlist.add(LiuheUtils.getNumWx(Integer.valueOf(num), liuhe.getTime()));
					boseList.add(LiuheUtils.getBoSe(num));
				}
				map.put("sxrank", sxlist);
				map.put("wxrank", wxlist);
				map.put("boserank", boseList);

				for (String sx : sxlist) {
					jqysList.add(LiuheUtils.getNumJqys(sx));
					nnsxList.add(LiuheUtils.getNumNnsx(sx));
					tdsxList.add(LiuheUtils.getNumTdsx(sx));
					sjsxList.add(LiuheUtils.getNumSjsx(sx));
					qqshList.add(LiuheUtils.getNumQqsh(sx));
					sssxList.add(LiuheUtils.getNumSssx(sx));
				}
				map.put("jqysrank", jqysList);
				map.put("nnsxrank", nnsxList);
				map.put("tdsxrank", tdsxList);
				map.put("sjsxrank", sjsxList);
				map.put("qqshrank", qqshList);
				map.put("sssxrank", sssxList);

				int tema = Integer.valueOf(liuhe.getNum7());
				// 特码
				map.put("tmds", tema % 2 == 0 ? "双" : "单");// 单双
				map.put("tmdx", tema > 24 ? "大" : "小");

				int sum = Integer.valueOf(liuhe.getNum1()) + Integer.valueOf(liuhe.getNum2())
						+ Integer.valueOf(liuhe.getNum3()) + Integer.valueOf(liuhe.getNum4())
						+ Integer.valueOf(liuhe.getNum5()) + Integer.valueOf(liuhe.getNum6())
						+ Integer.valueOf(liuhe.getNum7());
				map.put("zhsum", sum);
				map.put("zhds", sum % 2 == 0 ? "双" : "单");
				map.put("zhdx", sum > 174 ? "大" : "小");
				map.put("sssxrank", sssxList);
				map.put("starttime", liuhe.getTime().getTime());

				String jsonStr = JSON.toJSONString(map);
				String yearStr = DateUtils.format(liuhe.getTime(), "yyyy");
				redisservice.setHashMap(LotteryDict.LHC + "_" + LotteryDict.kjjl + "_" + yearStr, liuhe.getPeriods(),
						jsonStr);
			}
		}
	}

	/**
	 * 分页查找数据
	 * 
	 * @desc
	 * @author lj
	 * @param tableName
	 * @param start
	 * @param i
	 * @return
	 * @author 2018年9月20日
	 */
	private List<LiuheVo> findPage(String tableName, int start, int limit) {
		List<LiuheVo> list = new ArrayList<LiuheVo>();
		List<Map<String, Object>> listmap = lotteryMapper.findPage(tableName, start, limit);
		if (listmap != null && listmap.size() > 0) {
			for (Map<String, Object> map : listmap) {
				list.add(this.buildLiuheVo(map));
			}
		}
		return list;
	}

	/**
	 * 获取可用年份
	 * 
	 * @desc
	 * @author xg
	 * @author 2018年9月13日
	 */
	public void getYears() {
		List<Integer> list = lotteryProcessMapper.getLhcYearsData();
		// List<Integer> list2 = new ArrayList<Integer>();
		// for (String str : list) {
		// list2.add(Integer.valueOf(str));
		// }
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("years", list);
		String json = JSON.toJSONString(map);
		redisservice.set(LotteryDict.LHC + "_" + LotteryDict.years, json);
	}

	/**
	 * 六合彩视频数据
	 */
	public void lotteryVideo() {
		try {
			// GetMarkSixNumbers接口数据
			String sxArray[] = { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪" };
			String wuxingArray[] = { "金", "木", "水", "火", "土" };
			Date date = new Date();
			Map<String, Object> aMap = new HashMap<String, Object>();
			Map<String, List<Integer>> sxMap = LiuheUtils.getSx(date);
			Map<String, List<Integer>> wxMap = LiuheUtils.getWuxing(date);
			ArrayList<Object> shengxiaoList = new ArrayList<Object>();
			ArrayList<Object> wuxingList = new ArrayList<Object>();
			// 进行排序存储
			for (int i = 0; i < sxArray.length; i++) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("sx", sxArray[i]);
				hashMap.put("nums", StringUtils.join(sxMap.get(sxArray[i]), ","));
				shengxiaoList.add(hashMap);
			}
			for (int i = 0; i < wuxingArray.length; i++) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("wx", wuxingArray[i]);
				hashMap.put("nums", StringUtils.join(wxMap.get(wuxingArray[i]), ","));
				wuxingList.add(hashMap);
			}
			aMap.put("shengxiao", shengxiaoList);
			aMap.put("wuhang", wuxingList);
			Object aJson = JSONObject.toJSON(aMap);
			// marksix 接口数据
			LiuheVo theLastOne = this.getTheLastOne();
			if (theLastOne == null) {
				return;
			}
			Date starttime = theLastOne.getStarttime();
			Map<String, Object> bMap = new LinkedHashMap<String, Object>();
			ArrayList<Object> maList = new ArrayList<Object>();
			maList.add(Integer.parseInt(theLastOne.getNum1()));
			maList.add(LiuheUtils.getNumSx(Integer.valueOf(theLastOne.getNum1()), date));
			maList.add(LiuheUtils.getBoSe(theLastOne.getNum1()));
			maList.add(Integer.parseInt(theLastOne.getNum2()));
			maList.add(LiuheUtils.getNumSx(Integer.valueOf(theLastOne.getNum2()), date));
			maList.add(LiuheUtils.getBoSe(theLastOne.getNum2()));
			maList.add(Integer.parseInt(theLastOne.getNum3()));
			maList.add(LiuheUtils.getNumSx(Integer.valueOf(theLastOne.getNum3()), date));
			maList.add(LiuheUtils.getBoSe(theLastOne.getNum3()));
			maList.add(Integer.parseInt(theLastOne.getNum4()));
			maList.add(LiuheUtils.getNumSx(Integer.valueOf(theLastOne.getNum4()), date));
			maList.add(LiuheUtils.getBoSe(theLastOne.getNum4()));
			maList.add(Integer.parseInt(theLastOne.getNum5()));
			maList.add(LiuheUtils.getNumSx(Integer.valueOf(theLastOne.getNum5()), date));
			maList.add(LiuheUtils.getBoSe(theLastOne.getNum5()));
			maList.add(Integer.parseInt(theLastOne.getNum6()));
			maList.add(LiuheUtils.getNumSx(Integer.valueOf(theLastOne.getNum6()), date));
			maList.add(LiuheUtils.getBoSe(theLastOne.getNum6()));
			maList.add(Integer.parseInt(theLastOne.getNum7()));
			maList.add(LiuheUtils.getNumSx(Integer.valueOf(theLastOne.getNum7()), date));
			maList.add(LiuheUtils.getBoSe(theLastOne.getNum7()));
			bMap.put("id", Integer.parseInt(theLastOne.getPeriods()));
			bMap.put("nextid", Integer.parseInt(theLastOne.getPeriods()));
			bMap.put("year", Integer.parseInt(DateUtils.format(starttime, "yyyy")));
			bMap.put("day", DateUtils.format(starttime, "MM月dd日"));
			bMap.put("time", DateUtils.format(starttime, "HH时mm分"));
			bMap.put("week", DateUtils.getWeekOfDate(starttime));
			bMap.put("type", 5);
			bMap.put("info", "");
			bMap.put("ma", maList);
			Object bJson = JSONObject.toJSON(bMap);

			redisservice.setHashMap(LotteryDict.LHC + "_" + LotteryDict.video, "GetMarkSixNumbers",
					"zodiacData=JsonpReq.completeCall.zodiacData(" + aJson.toString() + ")");
			redisservice.setHashMap(LotteryDict.LHC + "_" + LotteryDict.video, "marksix",
					"JsonpReq.completeCall.markSix(" + bJson.toString() + ")");
		} catch (Exception e) {
			logger.error("六合彩视频数据错误:{}", e);
		}
	}

	/**
	 * 查询最新一条数据
	 * 
	 * @desc
	 * @author lj
	 * @return
	 * @author 2018年9月20日
	 */
	private LiuheVo getTheLastOne() {
		String lottId = LotteryConstant.LHC;
		// 表名
		String tableName = LotteryConstant.tableNameMap.get(lottId);
		String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);

		List<Map<String, Object>> list = lotteryProcessMapper.findLotterysbyLottId(tableName, numStr, 1);
		if (list != null && list.size() > 0) {
			LiuheVo vo = buildLiuheVo(list.get(0));
			return vo;
		}
		return null;
	}

}
