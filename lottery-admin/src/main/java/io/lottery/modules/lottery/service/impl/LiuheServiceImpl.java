package io.lottery.modules.lottery.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.lottery.common.config.LotteryDict;
import io.lottery.common.service.RedisService;
import io.lottery.common.utils.DateMatcherAssist;
import io.lottery.common.utils.DateUtils;
import io.lottery.common.utils.HttpUtils;
import io.lottery.common.utils.LiuheUtils;
import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.Query;
import io.lottery.modules.lottery.dao.LiuheDao;
import io.lottery.modules.lottery.entity.LiuheEntity;
import io.lottery.modules.lottery.service.LiuheService;

@Service("liuheService")
public class LiuheServiceImpl extends ServiceImpl<LiuheDao, LiuheEntity> implements LiuheService {

	private static Logger logger = LoggerFactory.getLogger(LiuheServiceImpl.class);

	@Autowired
	private LiuheDao liuheDao;
	@Autowired
	private RedisService redisService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		List<String> list = new ArrayList<String>();
		list.add("time");
		list.add("periods");
		Page<LiuheEntity> page = this.selectPage(new Query<LiuheEntity>(params).getPage(),
				new EntityWrapper<LiuheEntity>().orderDesc(list));

		return new PageUtils(page);
	}

	@Override
	public LiuheEntity getByTimeAndQishu(LiuheEntity liuheEntity) {
		return liuheDao.getByTimeAndQishu(liuheEntity);
	}

	/**
	 * 查询最新100期记录
	 */
	@Override
	public List<LiuheEntity> query100List() {
		List<LiuheEntity> list = this
				.selectList(new EntityWrapper<LiuheEntity>().orderBy("id", false).last("LIMIT 100"));
		return list;
	}

	/**
	 * 生成最新100期号码走势
	 */
	@Override
	public void hmzs(List<LiuheEntity> list) {
		JSONArray rearray = new JSONArray();
		for (LiuheEntity entity : list) {
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
		//Map<String, Object> countmap = new LinkedHashMap<String, Object>();
		JSONArray countarray=new JSONArray();
		for (String num : LiuheUtils.AllNumber) {
			int count = 0;
			for (LiuheEntity entity : list) {
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
			//countmap.put(num, count);
			countarray.add(count);
		}
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("hmzs", rearray);// 号码走势
			resobj.put("hmcs", countarray);// 号码出现次数
			redisService.set(LotteryDict.LHC + "_" + LotteryDict.hmzs, resobj.toJSONString());
		}
	}

	/**
	 * 生成最新100期特码波色走势
	 */
	@Override
	public void tm_bszs(List<LiuheEntity> list) {
		Map<String, Object> countmap = new LinkedHashMap<String, Object>();
		countmap.put("红", 0);
		countmap.put("蓝", 0);
		countmap.put("绿", 0);
		countmap.put("红单", 0);
		countmap.put("蓝单", 0);
		countmap.put("绿单", 0);
		countmap.put("红双", 0);
		countmap.put("蓝双", 0);
		countmap.put("绿双", 0);
		int redyl = 0, blueyl = 0, greenyl = 0, redsigyl = 0, bluesigyl = 0, greensigyl = 0, reddoubyl = 0,bluedoubyl = 0, greendoubyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheEntity entity : list) {
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
				countmap.put("红", 1 + (Integer) countmap.get("红"));
				if (tm % 2 == 1) {
					countmap.put("红单", 1 + (Integer) countmap.get("红单"));
					redsigyl = 0;
					++bluesigyl;
					++greensigyl;
					++reddoubyl;
					++bluedoubyl;
					++greendoubyl;
				} else {
					countmap.put("红双", 1 + (Integer) countmap.get("红双"));
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
				countmap.put("绿", 1 + (Integer) countmap.get("绿"));
				if (tm % 2 == 1) {
					countmap.put("绿单", 1 + (Integer) countmap.get("绿单"));
					++redsigyl;
					++bluesigyl;
					greensigyl = 0;
					++reddoubyl;
					++bluedoubyl;
					++greendoubyl;
				} else {
					countmap.put("绿双", 1 + (Integer) countmap.get("绿双"));
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
				countmap.put("蓝", 1 + (Integer) countmap.get("蓝"));
				if (tm % 2 == 1) {
					countmap.put("蓝单", 1 + (Integer) countmap.get("蓝单"));
					++redsigyl;
					bluesigyl = 0;
					++greensigyl;
					++reddoubyl;
					++bluedoubyl;
					++greendoubyl;
				} else {
					countmap.put("蓝双", 1 + (Integer) countmap.get("蓝双"));
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
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("bszs", rearray);// 波色走势
			resobj.put("bscs", new JSONObject(countmap));// 波色出现次数
			redisService.set(LotteryDict.LHC + "_" + LotteryDict.bszs, resobj.toJSONString());
		}

	}

	/**
	 * 生成最新100期特码生肖+家野走势
	 */
	@Override
	public void tm_sxzs(List<LiuheEntity> list) {
		Map<String, Object> countmap = new LinkedHashMap<String, Object>();
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
		for (LiuheEntity entity : list) {
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
					countmap.put(key, 1 + (Integer) countmap.get(key));
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
						countmap.put("家禽", 1 + (Integer) countmap.get("家禽"));
						obj.put("jy", "家禽");
						jiaqinyl = 0;
						++yeshouyl;
					} else if (LiuheUtils.YSList.contains(key)) {
						countmap.put("野兽", 1 + (Integer) countmap.get("野兽"));
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
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("sxzs", rearray);// 生肖走势
			resobj.put("sxcs", new JSONObject(countmap));// 生肖家野出现次数
			redisService.set(LotteryDict.LHC + "_" + LotteryDict.sxzs, resobj.toJSONString());
		}

	}

	/**
	 * 生成最新100期特码合数+单双走势
	 */
	@Override
	public void tm_dszs(List<LiuheEntity> list) {
		Map<String, Object> countmap = new LinkedHashMap<String, Object>();
		countmap.put("单", 0);
		countmap.put("双", 0);
		countmap.put("头单", 0);
		countmap.put("头双", 0);
		countmap.put("合单", 0);
		countmap.put("合双", 0);
		int danyl = 0, shuangyl = 0, toudanyl = 0, toushuangyl = 0, hedanyl = 0, heshuangyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheEntity entity : list) {
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
				countmap.put("单", 1 + (Integer) countmap.get("单"));
				danyl = 0;
				++shuangyl;
			} else {
				obj.put("ds", "双");
				countmap.put("双", 1 + (Integer) countmap.get("双"));
				shuangyl = 0;
				++danyl;
			}
			// 特头单双
			if (tm < 10) {// 10的特头是0 标记为双
				obj.put("ttds", "双");
				countmap.put("头双", 1 + (Integer) countmap.get("头双"));
				toushuangyl = 0;
				++toudanyl;
			} else {
				if (Integer.valueOf(tm.toString().substring(0, 1)) % 2 == 1) {
					obj.put("ttds", "单");
					countmap.put("头单", 1 + (Integer) countmap.get("头单"));
					toudanyl = 0;
					++toushuangyl;
				} else {
					obj.put("ttds", "双");
					countmap.put("头双", 1 + (Integer) countmap.get("头双"));
					toushuangyl = 0;
					++toudanyl;
				}
			}
			// 合数单双
			if (tm < 10) {// 10以内直接算
				if (tm % 2 == 1) {
					obj.put("hsds", "单");
					countmap.put("合单", 1 + (Integer) countmap.get("合单"));
					hedanyl = 0;
					++heshuangyl;
				} else {
					obj.put("hsds", "双");
					countmap.put("合双", 1 + (Integer) countmap.get("合双"));
					heshuangyl = 0;
					++hedanyl;
				}
			} else {
				// 合数值
				Integer val = Integer.valueOf(tm.toString().substring(0, 1))
						+ Integer.valueOf(tm.toString().substring(1, 2));
				if (val % 2 == 1) {
					obj.put("hsds", "单");
					countmap.put("合单", 1 + (Integer) countmap.get("合单"));
					hedanyl = 0;
					++heshuangyl;
				} else {
					obj.put("hsds", "双");
					countmap.put("合双", 1 + (Integer) countmap.get("合双"));
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
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("dszs", rearray);// 合数+单双走势
			resobj.put("dscs", new JSONObject(countmap));// 合数+单双出现次数
			redisService.set(LotteryDict.LHC + "_" + LotteryDict.dszs, resobj.toJSONString());
		}

	}

	/**
	 * 生成最新100期特码段位走势
	 */
	@Override
	public void tm_dwzs(List<LiuheEntity> list) {
		Map<String, Object> countmap = new LinkedHashMap<String, Object>();
		countmap.put("第一段", 0);
		countmap.put("第二段", 0);
		countmap.put("第三段", 0);
		countmap.put("第四段", 0);
		countmap.put("第五段", 0);
		countmap.put("第六段", 0);
		countmap.put("第七段", 0);
		int yiduanyl = 0, erduanyl = 0, sanduanyl = 0, siduanyl = 0, wuduanyl = 0, liuduanyl = 0, qiduanyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheEntity entity : list) {
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
				countmap.put("第一段", 1 + (Integer) countmap.get("第一段"));
			} else if (LiuheUtils.TwoSection.contains(tm)) {
				++yiduanyl;
				erduanyl = 0;
				++sanduanyl;
				++siduanyl;
				++wuduanyl;
				++liuduanyl;
				++qiduanyl;
				countmap.put("第二段", 1 + (Integer) countmap.get("第二段"));
			} else if (LiuheUtils.ThreeSection.contains(tm)) {
				++yiduanyl;
				++erduanyl;
				sanduanyl = 0;
				++siduanyl;
				++wuduanyl;
				++liuduanyl;
				++qiduanyl;
				countmap.put("第三段", 1 + (Integer) countmap.get("第三段"));
			} else if (LiuheUtils.FourSection.contains(tm)) {
				++yiduanyl;
				++erduanyl;
				++sanduanyl;
				siduanyl = 0;
				++wuduanyl;
				++liuduanyl;
				++qiduanyl;
				countmap.put("第四段", 1 + (Integer) countmap.get("第四段"));
			} else if (LiuheUtils.FiveSection.contains(tm)) {
				++yiduanyl;
				++erduanyl;
				++sanduanyl;
				++siduanyl;
				wuduanyl = 0;
				++liuduanyl;
				++qiduanyl;
				countmap.put("第五段", 1 + (Integer) countmap.get("第五段"));
			} else if (LiuheUtils.SixSection.contains(tm)) {
				++yiduanyl;
				++erduanyl;
				++sanduanyl;
				++siduanyl;
				++wuduanyl;
				liuduanyl = 0;
				++qiduanyl;
				countmap.put("第六段", 1 + (Integer) countmap.get("第六段"));
			} else if (LiuheUtils.SevenSection.contains(tm)) {
				++yiduanyl;
				++erduanyl;
				++sanduanyl;
				++siduanyl;
				++wuduanyl;
				++liuduanyl;
				qiduanyl = 0;
				countmap.put("第七段", 1 + (Integer) countmap.get("第七段"));
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
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("dwzs", rearray);// 段位走势
			resobj.put("dwcs", new JSONObject(countmap));// 段位出现次数
			redisService.set(LotteryDict.LHC + "_" + LotteryDict.dwzs, resobj.toJSONString());
		}
	}

	/**
	 * 生成最新100期特码头数走势
	 */
	@Override
	public void tm_tszs(List<LiuheEntity> list) {
		Map<String, Object> countmap = new LinkedHashMap<String, Object>();
		countmap.put("0头", 0);
		countmap.put("1头", 0);
		countmap.put("2头", 0);
		countmap.put("3头", 0);
		countmap.put("4头", 0);
		countmap.put("头双(0-2-4)", 0);
		countmap.put("头单(1-3)", 0);
		int lingtouyl = 0, yitouyl = 0, ertouyl = 0, santouyl = 0, sitouyl = 0, toushuangyl = 0, toudanyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheEntity entity : list) {
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
				countmap.put("0头", 1 + (Integer) countmap.get("0头"));
				toushuangyl = 0;
				++toudanyl;
				countmap.put("头双(0-2-4)", 1 + (Integer) countmap.get("头双(0-2-4)"));
				obj.put("ttds", "双");
			} else {
				if ("1".equals(tm.toString().substring(0, 1))) {
					++lingtouyl;
					yitouyl = 0;
					++ertouyl;
					++santouyl;
					++sitouyl;
					countmap.put("1头", 1 + (Integer) countmap.get("1头"));
					toudanyl = 0;
					++toushuangyl;
					countmap.put("头单(1-3)", 1 + (Integer) countmap.get("头单(1-3)"));
					obj.put("ttds", "单");
				} else if ("2".equals(tm.toString().substring(0, 1))) {
					++lingtouyl;
					++yitouyl;
					ertouyl = 0;
					++santouyl;
					++sitouyl;
					countmap.put("2头", 1 + (Integer) countmap.get("2头"));
					toushuangyl = 0;
					++toudanyl;
					countmap.put("头双(0-2-4)", 1 + (Integer) countmap.get("头双(0-2-4)"));
					obj.put("ttds", "双");
				} else if ("3".equals(tm.toString().substring(0, 1))) {
					++lingtouyl;
					++yitouyl;
					++ertouyl;
					santouyl = 0;
					++sitouyl;
					countmap.put("3头", 1 + (Integer) countmap.get("3头"));
					toudanyl = 0;
					++toushuangyl;
					countmap.put("头单(1-3)", 1 + (Integer) countmap.get("头单(1-3)"));
					obj.put("ttds", "单");
				} else if ("4".equals(tm.toString().substring(0, 1))) {
					++lingtouyl;
					++yitouyl;
					++ertouyl;
					++santouyl;
					sitouyl = 0;
					countmap.put("4头", 1 + (Integer) countmap.get("4头"));
					toushuangyl = 0;
					++toudanyl;
					countmap.put("头双(0-2-4)", 1 + (Integer) countmap.get("头双(0-2-4)"));
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
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("tszs", rearray);// 头数走势
			resobj.put("tscs", new JSONObject(countmap));// 头数出现次数
			redisService.set(LotteryDict.LHC + "_" + LotteryDict.tszs, resobj.toJSONString());
		}
	}

	/**
	 * 生成最新100期特码尾数走势
	 */
	@Override
	public void tm_wszs(List<LiuheEntity> list) {
		Map<String, Object> countmap = new LinkedHashMap<String, Object>();
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
		for (LiuheEntity entity : list) {
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
				countmap.put("0尾", 1 + (Integer) countmap.get("0尾"));
				weixiaoyl = 0;
				++weidayl;
				countmap.put("小(0-4)", 1 + (Integer) countmap.get("小(0-4)"));
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
				countmap.put("1尾", 1 + (Integer) countmap.get("1尾"));
				weixiaoyl = 0;
				++weidayl;
				countmap.put("小(0-4)", 1 + (Integer) countmap.get("小(0-4)"));
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
				countmap.put("2尾", 1 + (Integer) countmap.get("2尾"));
				weixiaoyl = 0;
				++weidayl;
				countmap.put("小(0-4)", 1 + (Integer) countmap.get("小(0-4)"));
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
				countmap.put("3尾", 1 + (Integer) countmap.get("3尾"));
				weixiaoyl = 0;
				++weidayl;
				countmap.put("小(0-4)", 1 + (Integer) countmap.get("小(0-4)"));
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
				countmap.put("4尾", 1 + (Integer) countmap.get("4尾"));
				weixiaoyl = 0;
				++weidayl;
				countmap.put("小(0-4)", 1 + (Integer) countmap.get("小(0-4)"));
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
				countmap.put("5尾", 1 + (Integer) countmap.get("5尾"));
				++weixiaoyl;
				weidayl = 0;
				countmap.put("大(5-9)", 1 + (Integer) countmap.get("大(5-9)"));
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
				countmap.put("6尾", 1 + (Integer) countmap.get("6尾"));
				++weixiaoyl;
				weidayl = 0;
				countmap.put("大(5-9)", 1 + (Integer) countmap.get("大(5-9)"));
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
				countmap.put("7尾", 1 + (Integer) countmap.get("7尾"));
				++weixiaoyl;
				weidayl = 0;
				countmap.put("大(5-9)", 1 + (Integer) countmap.get("大(5-9)"));
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
				countmap.put("8尾", 1 + (Integer) countmap.get("8尾"));
				++weixiaoyl;
				weidayl = 0;
				countmap.put("大(5-9)", 1 + (Integer) countmap.get("大(5-9)"));
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
				countmap.put("9尾", 1 + (Integer) countmap.get("9尾"));
				++weixiaoyl;
				weidayl = 0;
				countmap.put("大(5-9)", 1 + (Integer) countmap.get("大(5-9)"));
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
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("wszs", rearray);// 尾数走势
			resobj.put("wscs", new JSONObject(countmap));// 尾数出现次数
			redisService.set(LotteryDict.LHC + "_" + LotteryDict.wszs, resobj.toJSONString());
		}
	}

	/**
	 * 生成最新100期特码五行走势
	 */
	@Override
	public void tm_wxzs(List<LiuheEntity> list) {
		Map<String, Object> countmap = new LinkedHashMap<String, Object>();
		countmap.put("金", 0);
		countmap.put("木", 0);
		countmap.put("水", 0);
		countmap.put("火", 0);
		countmap.put("土", 0);
		int jinyl = 0, muyl = 0, shuiyl = 0, huoyl = 0, tuyl = 0;
		JSONArray rearray = new JSONArray();
		for (LiuheEntity entity : list) {
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
					countmap.put(key, 1 + (Integer) countmap.get(key));
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
		JSONObject resobj = new JSONObject();
		if (rearray.size() > 0) {
			Collections.reverse(rearray);
			resobj.put("wxzs", rearray);// 五行走势
			resobj.put("wxcs", new JSONObject(countmap));// 五行出现次数
			redisService.set(LotteryDict.LHC + "_" + LotteryDict.wxzs, resobj.toJSONString());
		}
	}

	/**
	 * 生成开奖记录
	 */
	@Override
	public void genKjjl() {
		// 分页查找
		int count = this.selectCount(null);
		// 每页100条
		int pageNum = count / 100;
		if (pageNum % 100 != 0) {
			pageNum += 1;
		}
		for (int i = 1; i <= pageNum; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("limit", 100 + "");
			params.put("page", i + "");
			Page<LiuheEntity> page = this.selectPage(new Query<LiuheEntity>(params).getPage());
			List<LiuheEntity> list = page.getRecords();
			Map<String, Object> map = new HashMap<String, Object>();
			if (list != null && list.size() > 0) {
				for (LiuheEntity liuhe : list) {
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
					for (String num : ranklist) {
						sxlist.add(LiuheUtils.getNumSx(Integer.valueOf(num), liuhe.getTime()));
					}
					map.put("sxrank", sxlist);

					// 获取五行
					List<String> wxlist = new ArrayList<String>();
					for (String num : ranklist) {
						wxlist.add(LiuheUtils.getNumWx(Integer.valueOf(num), liuhe.getTime()));
					}
					map.put("wxrank", wxlist);

					// 家禽野兽
					List<String> jqysList = new ArrayList<String>();
					for (String sx : sxlist) {
						jqysList.add(LiuheUtils.getNumJqys(sx));
					}
					map.put("jqysrank", jqysList);

					// 男女生肖
					List<String> nnsxList = new ArrayList<String>();
					for (String sx : sxlist) {
						nnsxList.add(LiuheUtils.getNumNnsx(sx));
					}
					map.put("nnsxrank", nnsxList);

					// 天地生肖
					List<String> tdsxList = new ArrayList<String>();
					for (String sx : sxlist) {
						tdsxList.add(LiuheUtils.getNumTdsx(sx));
					}
					map.put("tdsxrank", tdsxList);

					// 四级生肖
					List<String> sjsxList = new ArrayList<String>();
					for (String sx : sxlist) {
						sjsxList.add(LiuheUtils.getNumSjsx(sx));
					}
					map.put("sjsxrank", sjsxList);

					// 琴棋书画
					List<String> qqshList = new ArrayList<String>();
					for (String sx : sxlist) {
						qqshList.add(LiuheUtils.getNumQqsh(sx));
					}
					map.put("qqshrank", qqshList);

					// 三色生肖
					List<String> sssxList = new ArrayList<String>();
					for (String sx : sxlist) {
						sssxList.add(LiuheUtils.getNumSssx(sx));
					}
					map.put("sssxrank", sssxList);

					// 球的颜色
					List<String> boseList = new ArrayList<String>();
					for (String num : ranklist) {
						boseList.add(LiuheUtils.getBoSe(num));
					}
					map.put("boserank", boseList);

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
					redisService.setHashMap(LotteryDict.LHC + "_" + LotteryDict.kjjl + "_" + yearStr,
							liuhe.getPeriods(), jsonStr);
				}
			}
		}
	}

	@Override
	public LiuheEntity getTheLastOne() {
		return liuheDao.getTheLastOne();
	}

	/**
	 * 获取开奖数据
	 */
	@Override
	public void getKjData() {
		// 获取最近一期的开奖数据
		LiuheEntity lastliuhe = this.getTheLastOne();

		Map<String, Object> map = null;
		// 验证开奖数据是否正确

		try {
			map = get1680660KjData();
		} catch (Exception e) {
			map = get6hkbKjData();
		}
		if (map != null) {
			String dqqs = (String) map.get("dqqs");
			String dqkjsj = (String) map.get("dqkjsj");

			// 判断是否为当前期数
			if (lastliuhe != null) {
				// 判断是否更新成了最新的时间
				String currQishu = lastliuhe.getPeriods();
				String currDate = DateUtils.format(lastliuhe.getTime(), DateUtils.DATE_PATTERN);
				if (!(dqqs.equals(currQishu) && dqkjsj.equals(currDate))) {
					// 将数据保存到数据库中
					LiuheEntity liuhe = new LiuheEntity();
					@SuppressWarnings("unchecked")
					List<String> numList = (List<String>) map.get("dqhm");
					liuhe.setNum1(numList.get(0));
					liuhe.setNum2(numList.get(1));
					liuhe.setNum3(numList.get(2));
					liuhe.setNum4(numList.get(3));
					liuhe.setNum5(numList.get(4));
					liuhe.setNum6(numList.get(5));
					liuhe.setNum7(numList.get(6));

					liuhe.setCreatetime(new Date());
					liuhe.setPeriods(dqqs);
					// 下期开奖时间
					Long xqkjsj = Long.valueOf(String.valueOf(map.get("xqkjsj")));
					Date currDate2 = new Date(xqkjsj);
					liuhe.setStarttime(currDate2);
					// 设置开奖时间
					Date kjDate = DateUtils.stringToDate(dqkjsj, DateUtils.DATE_PATTERN);
					liuhe.setTime(kjDate);
					this.insert(liuhe);

					// 更新缓存中的数据
					// 球的颜色
					List<String> boseList = new ArrayList<String>();
					for (String num : numList) {
						boseList.add(LiuheUtils.getBoSe(num));
					}
					map.put("boserank", boseList);

					// 12生肖
					List<String> sxlist = new ArrayList<String>();
					for (String num : numList) {
						sxlist.add(LiuheUtils.getNumSx(Integer.valueOf(num), lastliuhe.getStarttime()));
					}
					map.put("sxrank", sxlist);

					// 获取五行
					List<String> wxlist = new ArrayList<String>();
					for (String num : numList) {
						wxlist.add(LiuheUtils.getNumWx(Integer.valueOf(num), lastliuhe.getStarttime()));
					}
					map.put("wxrank", wxlist);
					// 放到redis中
					redisService.set(LotteryDict.LHC + "_" + LotteryDict.zxkj, JSON.toJSONString(map));
					this.execute();
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
	public static Map<String, Object> get6hkbKjData() {
		String url = "https://www.6hkb.com/?" + System.currentTimeMillis();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String str = HttpUtils.executeGet(url);
			Document doc = Jsoup.parse(str);
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
					numList.add(e.text());
				}
			}
			map.put("dqhm", numList);

		} catch (IOException e) {
			logger.error("请求地址出错:{}", e);
		}
		return map;

	}

	/**
	 * 从https://1680660.com/smallSix/findSmallSixInfo.do获取开奖数据
	 * 
	 * @desc
	 * @author lj
	 * @return
	 * @author 2018年9月19日
	 */
	public static Map<String, Object> get1680660KjData() {
		Map<String, Object> map = new HashMap<String, Object>();
		String url = "https://1680660.com/smallSix/findSmallSixInfo.do?" + System.currentTimeMillis();
		try {
			String str = HttpUtils.executeGet(url);
			JSONObject json = JSON.parseObject(str);
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
			map.put("dqhm", Arrays.asList(hmArr));
			// 一年的总期数
			Integer myqs = data.getInteger("sumTotal");
			map.put("myqs", myqs);
			// 下期开奖时间
			String xqkjsj = data.getString("drawTime");
			Date nextDate = DateUtils.stringToDate(xqkjsj, DateUtils.DATE_TIME_PATTERN);
			map.put("xqkjsj", nextDate.getTime());

		} catch (IOException e) {
			logger.error("请求地址出错:{}", e);
		}

		return map;
	}

	/**
	 * 从http://www.kj5588.com/chajian/bmjg.js?_=获取开奖数据
	 * 
	 * @desc
	 * @author lj
	 * @return
	 * @author 2018年9月19日
	 */
	// public static Map<String, Object> getKj5588Data() {
	// Map<String, Object> map = new HashMap<String, Object>();
	// String url = "http://www.kj5588.com/chajian/bmjg.js?_=" +
	// System.currentTimeMillis();// 加上时间戳
	// try {
	// String str = HttpUtils.executeGet(url);
	// if (StringUtils.isNotBlank(str)) {
	// // 解析查询出来的数据
	// //
	// {"k":"106,11,18,32,23,48,09,26,107,09,20,四,21点30分","t":"1000","联系":"QQ：7136995"}
	// JSONObject obj = JSON.parseObject(str);
	// String v = obj.getString("k");
	// String[] arr = v.split(",");
	//
	// // 判断取回来的是否为数字,非数字，直接返回，非数字还没有开奖成功
	// if (!NumberUtils.isNumeric(arr[1]) || !NumberUtils.isNumeric(arr[2]) ||
	// !NumberUtils.isNumeric(arr[3])
	// || !NumberUtils.isNumeric(arr[4]) || !NumberUtils.isNumeric(arr[5])
	// || !NumberUtils.isNumeric(arr[6]) || !NumberUtils.isNumeric(arr[7])) {
	// return null;
	// }
	// // 当前期数
	// String qishu = arr[0];
	// String nextTerm = arr[8];
	// String riqi = arr[9] + "月" + arr[10] + "日" + arr[12];
	// Calendar cal = Calendar.getInstance();
	// int year = cal.get(Calendar.YEAR);
	// int term = Integer.valueOf(nextTerm);// 如果是下一期，则是下一年的时间
	// if (term == 1) {
	// year += 1;
	// }
	// riqi = year + "年" + riqi;
	// // 下期开奖时间
	// Date date = DateUtils.stringToDate(riqi, "yyyy年MM月dd日HH点mm分");
	// // 当前期数
	// map.put("dqqs", qishu);
	// // 下期期数
	// map.put("xqqs", xqqs);
	//
	// }
	//
	// } catch (IOException e) {
	// logger.error("请求地址出错:{}", e);
	// }
	//
	// return map;
	// }

	public static void main(String[] args) {
		// System.out.println(get1680660KjData());
		// System.out.println(get6hkbKjData());

		Integer a = 1000;
		Integer b = 1000;

		System.out.println(b.equals(a));
		System.out.println(a == b);
	}

	/**
	 * 六合彩视频数据
	 */
	@Override
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
			LiuheEntity theLastOne = liuheDao.getTheLastOne();
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

			redisService.setHashMap(LotteryDict.LHC + "_" + LotteryDict.video, "GetMarkSixNumbers",
					"zodiacData=JsonpReq.completeCall.zodiacData(" + aJson.toString() + ")");
			redisService.setHashMap(LotteryDict.LHC + "_" + LotteryDict.video, "marksix",
					"JsonpReq.completeCall.markSix(" + bJson.toString() + ")");
		} catch (Exception e) {
			logger.error("六合彩视频数据错误:{}", e);
		}
	}

	/**
	 * 生成前端接口需要数据
	 * 
	 * @author ms
	 * @date 2018年9月11日 上午10:52:27
	 */
	public void execute() {
		List<LiuheEntity> list = this.query100List();
		//倒序一次是为了遗漏统计数的排序
		Collections.reverse(list);
		this.hmzs(list);
		this.tm_bszs(list);
		this.tm_sxzs(list);
		this.tm_dszs(list);
		this.tm_dwzs(list);
		this.tm_tszs(list);
		this.tm_wszs(list);
		this.tm_wxzs(list);
		this.genKjjl();
		this.getYears();
		this.lotteryVideo();
	}
	
	/**
	 * 获取可用年份
	 * 
	 * @desc
	 * @author xg
	 * @author 2018年9月13日
	 */
	public void getYears() {
		List<String> list = liuheDao.getYears();
		List<Integer> list2 = new ArrayList<Integer>();
		for (String str : list) {
			list2.add(Integer.valueOf(str));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("years", list2);
		String json = JSON.toJSONString(map);
		redisService.set(LotteryDict.LHC + "_" + LotteryDict.years, json);
	}

}
