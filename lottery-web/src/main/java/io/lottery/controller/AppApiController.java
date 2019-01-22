package io.lottery.controller;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import io.lottery.common.config.LotteryDict;
import io.lottery.common.config.RedisService;
import io.lottery.common.utils.ExceptionUtil;
import io.lottery.common.utils.R;
import io.lottery.service.AppApiChangeDataService;
import io.lottery.service.CmsService;
import io.lottery.service.LiuheService;
import io.lottery.util.DateUtil;

/**
 * 
 * @desc app接口控制类
 * @author abo
 * @date 2018年8月7日 上午11:02:19
 *
 */
@RestController
@RequestMapping("/appapi")
public class AppApiController {
	@Autowired
	private RedisService redisservice;
	@Autowired
	private AppApiChangeDataService appApiChangeDataService;
	@Autowired
	private CmsService cmsService;

	@Autowired
	private LiuheService liuheService;

	private Logger logger = LoggerFactory.getLogger(AppApiController.class);

	@RequestMapping("json")
	public R json(String code) {
		//logger.info("/appapi/json,请求接口入参:code={}", code);
		try {
			if (StringUtils.isNoneBlank(code)) {
				String tempCode = code;
				String code1 = null;
				String type = code.split("_")[1];// 接口类型
				String lottId = code.split("_")[0];
				// 时时彩之前的redis数据是把位置遗漏放在一起没有分开，所以这里获取redis的时候只能获取一个总的。然后通过service进行分割
				if (code.contains("cqssc_wzyl")) {
					code = "cqssc_wzyl";
				} else if (code.contains("xjssc_wzyl")) {
					code = "xjssc_wzyl";
				} else if (code.contains("tjssc_wzyl")) {
					code = "tjssc_wzyl";
				}
				// 如果是综合路珠去获取单双大小
				if (code.contains("zhlzdx")) {
					code = lottId + "_dxlz";
				} else if (code.contains("zhlzds")) {
					code = lottId + "_dslz";
				}
				// 除飞艇和北京赛车，其他都走总和
				if (lottId.contains("bjsc") || lottId.contains("xyft")) {
					code1 = lottId + "_" + "gyhlz";
				} else {
					code1 = lottId + "_" + "zhlz";
				}
				String data = redisservice.get(code);
				String data1 = "";
				if (code1 != null) {
					data1 = redisservice.get(code1);
				}
				JSONArray jsonArray = null;
				if (StringUtils.isNoneBlank(data)) {
					// 处理特殊app端请求
					if (tempCode.contains("zhlzdx") || tempCode.contains("zhlzds")) {
						// 总和路珠比较特殊，基本球会加上冠亚和或者总和
						// 先获取大小，单双基本球
						jsonArray = appApiChangeDataService.changeDslzData(data, code);
						JSONArray temp = null;
						if (lottId.equals("bjsc") || lottId.equals("xyft")) {
							// 再获取冠亚和大小或单双
							temp = appApiChangeDataService.changeGyhLzData(data1);
						} else {
							// 总和大小或单双
							temp = appApiChangeDataService.changeZhlzData(data1, code);
						}
						if (temp != null && temp.size() > 0) {
							if (type.contains("dx")) {
								jsonArray.add(temp.get(0));
							} else {
								jsonArray.add(temp.get(1));
							}
						}
					} else if (code.equals("pcdd_zhlz")) {
						jsonArray = appApiChangeDataService.changePcddZhlzData(data);
					} else if ("zst".equals(type) || "hmzst".equals(type) || "gyhzst".equals(type)) {
						jsonArray = appApiChangeDataService.changeZstData(data, type);
					} else if (tempCode.contains("ssc_wzyl")) {// 时时彩号码位置遗漏
						jsonArray = appApiChangeDataService.changeSscHmylData(data, tempCode);
					} else if ("wzyl".equals(type) || "hmyl".equals(type)) {// 位置遗漏
						jsonArray = appApiChangeDataService.changeWzylHmylData(data, code);
					} else if ("dslz".equals(type) || "dxlz".equals(type) || "hmlz".equals(type)
							|| "hsdslz".equals(type) || "zfblz".equals(type) || "dnxblz".equals(type)) {// 单双、大小路珠、号码路珠、合数、中发白
						jsonArray = appApiChangeDataService.changeDslzData(data, code);
					} else if ("hmqhlz".equals(type)) {// 前后路珠
						jsonArray = appApiChangeDataService.changeQhlzData(data, code);
					} else if ("lhlz".equals(type)) {// 龙虎路珠
						jsonArray = appApiChangeDataService.changeDslzData(data, code);
					} else if ("gyhlz".equals(type)) {// 冠亚和路珠
						jsonArray = appApiChangeDataService.changeGyhLzData(data);
					} else if ("gyhyl".equals(type)) {// 冠亚和遗漏
						jsonArray = appApiChangeDataService.changeGyhylData(data);
					} else if ("zhlz".equals(type)) {// 总和路珠
						jsonArray = appApiChangeDataService.changeZhlzData(data, code);
					} else if ("wsdxlz".equals(type)) {// 总和尾数大小路珠
						jsonArray = appApiChangeDataService.changeWsdxlzData(data, code);
					} else if ("jolz".equals(type) || "sxlz".equals(type)) {// 奇偶路珠、上下
						jsonArray = appApiChangeDataService.changeJolzData(data, type);
					} else if ("bslz".equals(type)) {// 波色路珠
						jsonArray = new JSONArray();
						jsonArray.add(JSONObject.parseObject(data));
					} else {
						return R.okwithdata(JSONObject.parseObject(data, Feature.OrderedField));
					}
					return R.okwithdata(jsonArray);
				} else {
					return R.okwithdata(new JSONObject());
				}
			} else {
				return R.error("接口编码不能为空");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionStackTrace(e));
			return R.error("请求异常");
		}
	}

	/**
	 * @param code
	 * @return
	 */
	@RequestMapping("jsonarray")
	public R jsonarray(String code, String date, String page, String searchkey, String position) {
		//logger.info("/api/jsonarray,请求接口入参:code={},date={},page={},searchkey={},position={}", code, date, page,
		//		searchkey, position);
		try {
			if (StringUtils.isNotBlank(code)) {
				String tempCode = code;
				JSONArray jsonArray = null;
				// 时时彩之前的redis数据是把位置遗漏放在一起没有分开，所以这里获取redis的时候只能获取一个总的。然后通过service进行分割
				if (code.contains("bjsc_sxdwzs")) {
					code = "bjsc_wzzsmq";
				} else if (code.contains("cqssc_sxdwzs")) {
					code = "cqssc_wzzsmq";
				} else if (code.contains("xyft_sxdwzs")) {
					code = "xyft_wzzsmq";
				} else if (code.contains("xjssc_sxdwzs")) {
					code = "xjssc_wzzsmq";
				} else if (code.contains("tjssc_sxdwzs")) {
					code = "tjssc_wzzsmq";
				}

				String[] arr = code.split("_");
				String lottId = arr[0];
				String type = arr[1];
				if (code.contains("hmtjjhlb")) {// 由于redis没有存列表数据，列表数据都是子记录的总和，所以这里拿子记录的集合
					code = lottId + "_hmtjjh";
				} else if (code.contains("lmtjjhlb")) {// 由于redis没有存列表数据，列表数据都是子记录的总和，所以这里拿子记录的集合
					code = lottId + "_lmtjjh";
				}
				// if (code.contains("bjsc_hmtjjhlb")) {//
				// 由于redis没有存列表数据，列表数据都是子记录的总和，所以这里拿子记录的集合
				// code = "bjsc_hmtjjh";
				// } else if (code.contains("xyft_hmtjjhlb")) {//
				// 由于redis没有存列表数据，列表数据都是子记录的总和，所以这里拿子记录的集合
				// code = "xyft_hmtjjh";
				// } else if (code.contains("cqssc_hmtjjhlb")) {//
				// 由于redis没有存列表数据，列表数据都是子记录的总和，所以这里拿子记录的集合
				// code = "cqssc_hmtjjh";
				// } else if (code.contains("gdklsf_hmtjjhlb")) {//
				// 由于redis没有存列表数据，列表数据都是子记录的总和，所以这里拿子记录的集合
				// code = "gdklsf_hmtjjh";
				// } else if (code.contains("bjsc_lmtjjhlb")) {//
				// 由于redis没有存列表数据，列表数据都是子记录的总和，所以这里拿子记录的集合
				// code = "bjsc_lmtjjh";
				// } else if (code.contains("xyft_lmtjjhlb")) {//
				// 由于redis没有存列表数据，列表数据都是子记录的总和，所以这里拿子记录的集合
				// code = "xyft_lmtjjh";
				// } else if (code.contains("cqssc_lmtjjhlb")) {//
				// 由于redis没有存列表数据，列表数据都是子记录的总和，所以这里拿子记录的集合
				// code = "cqssc_lmtjjh";
				// } else if (code.contains("gdklsf_lmtjjhlb")) {//
				// 由于redis没有存列表数据，列表数据都是子记录的总和，所以这里拿子记录的集合
				// code = "gdklsf_lmtjjh";
				// }

				JSONArray array = new JSONArray();
				Map<Object, Object> map = null;
				// 资讯技巧查询，直接查询数据库
				if (type.equals("zxjqcx")) {
					int pageNum = org.springframework.util.StringUtils.isEmpty(page) ? 1 : Integer.parseInt(page);
					jsonArray = cmsService.artListToRedisByLottIdAndSearchKey(lottId, 20, pageNum, searchkey);
					return R.okwithdata(jsonArray);
				}
				// 数据对比直接就处理了
				if (code.contains("sjdb")) {
					String currentDate = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE);
					String yesterday = null;
					if (date == null) {// 数据对比就取今天和昨天的
						yesterday = DateUtil.getDateString(new Date(), null, null, -1, null, null, null,
								DateUtil.PATTERN_DATE);
					} else {
						// 取今天和指定天数的
						yesterday = date;
					}
					Object currentOb = redisservice.getHashMap(code, currentDate);
					Object oldOb = redisservice.getHashMap(code, yesterday);
					if (currentOb == null && oldOb == null) {
						return R.okwithdata(jsonArray);
					} else {
						jsonArray = appApiChangeDataService.changeSjdbData(currentOb, oldOb);
					}
					return R.okwithdata(jsonArray);
				}

				// 根据不同的接口，获取不同的数据条数
				if (code.contains("kjjl")) {
					if (code.contains("lhc_kjjl")) {// 六合彩开奖记录
						map = liuheService.sjkjjl(date);
					} else {
						map = redisservice.getHashMapsByDate(code, date);
					}
				} else if (code.contains("jjsh")) {
					map = redisservice.getHashMapsByLimit(code, 30);
				} else if (code.contains("lmtjjh") || code.contains("hmtjjh")) {
					map = redisservice.getHashMaps(code);// 这个接口只存了当前的，所有这里是获取今日所有
					// String currentDate = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE);
					// map = redisservice.getHashMapsByDate(code, currentDate);
				} else {
					map = redisservice.getHashMapsByLimit(code);// 按照彩种最后20条查询
				}
				// 如果缓存里面资讯技巧列表是空的，那么就去查询数据库并且生成缓存
				if (map.isEmpty() && type.equals("zxjq")) {
					int pageNum = 1;
					// 如果为空就默认第一页
					if (!StringUtils.isEmpty(page)) {
						pageNum = Integer.parseInt(page);
					}
					jsonArray = cmsService.artListToRedisByLottId(lottId, 20, pageNum);
					return R.okwithdata(jsonArray);
				}
				if (!map.isEmpty()) {
					// 狙击杀号
					if (type.equals("jjsh")) {
						jsonArray = appApiChangeDataService.changeJjshData(map);
						return R.okwithdata(jsonArray);
					}
					// 号码计划列表
					if (tempCode.contains("hmtjjhlb")) {
						jsonArray = appApiChangeDataService.changeHmtjjhlbData(map, lottId);
						return R.okwithdata(jsonArray);
					}
					// 号码计划详细
					if (type.equals("hmtjjh")) {
						jsonArray = appApiChangeDataService.changeHmtjjhData(map, searchkey);
						return R.okwithdata(jsonArray);
					}
					// 两面计划列表
					if (tempCode.contains("lmtjjhlb")) {
						jsonArray = appApiChangeDataService.changelmtjjhlbData(map, lottId);
						return R.okwithdata(jsonArray);
					}
					// 两面计划详细
					if (type.equals("lmtjjh")) {
						jsonArray = appApiChangeDataService.changeLmtjjhData(map, searchkey, position);
						return R.okwithdata(jsonArray);
					}
					for (Map.Entry<Object, Object> entry : map.entrySet()) {
						JSONObject obj = new JSONObject();
						String key = entry.getKey() + "";
						obj.put(key, JSON.parseObject(entry.getValue() == null ? "" : entry.getValue() + ""));
						array.add(obj);
					}
					// 处理app端特殊的接口
					if ("kjjl".equals(type)) {
						jsonArray = appApiChangeDataService.changeKjjlData(array, lottId);
					} else if ("lmtzck".equals(type)) {
						jsonArray = appApiChangeDataService.changeLmtzckData(array, lottId);
					} else if ("dsdxls".equals(type)) {
						jsonArray = appApiChangeDataService.changeDsDxLsData(array);
					} else if ("lhls".equals(type)) {// 龙虎历史
						jsonArray = appApiChangeDataService.changeLhlsData(array);
					} else if ("gyhls".equals(type)) {// 冠亚和历史
						jsonArray = appApiChangeDataService.changeGyhlsData(array);
					} else if ("lshmtj".equals(type)) {// 历史号码统计
						jsonArray = appApiChangeDataService.changeLshmtjData(array, code);
					} else if (tempCode.contains("sxdwzs")) {// 上下段位走势
						jsonArray = appApiChangeDataService.changeSxdwzsData(array);
					} else if (tempCode.contains("hmtjjh")) {// 号码推荐计划
						jsonArray = appApiChangeDataService.changeSxdwzsData(array);
					} else {
						return R.okwithdata(array);
					}
					return R.okwithdata(jsonArray);
				} else {
					return R.okwithdata(array);
				}
			} else {
				return R.error("接口编码不能为空");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionStackTrace(e));
			return R.error("请求异常");
		}
	}

	/**
	 * 长龙统计
	 * 
	 * @author ms
	 * @date 2018年8月1日 上午10:13:39
	 */
	@RequestMapping("cltj")
	public R cltj(String lotteryid) {
		Map<Object, Object> resmap = redisservice.getHashMaps(LotteryDict.cltj);
		JSONArray array = new JSONArray();
		if (resmap != null) {
			for (Map.Entry<Object, Object> entry : resmap.entrySet()) {
				JSONObject obj = new JSONObject();
				obj.put(entry.getKey().toString(), JSON.parseArray(entry.getValue().toString()));
				array.add(obj);
			}
			return R.okwithdata(array);
		} else {
			return R.okwithdata(array);
		}
	}

	@RequestMapping("appversion")
	public R appVersion(String machinetype, String version) {
		Object object = redisservice.getHashMap("app_version", machinetype);
		if (object == null) {
			return R.error("machinetype不合法");
		} else {
			JSONObject resobj = JSONObject.parseObject(object.toString());
			if (resobj.getString("version").equals(version)) {
				return R.ok();
			} else {
				return R.okwithdata(resobj);
			}
		}
	}
}
