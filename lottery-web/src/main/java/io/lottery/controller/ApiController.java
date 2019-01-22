package io.lottery.controller;

import java.util.Calendar;
import java.util.HashMap;
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
import io.lottery.service.LiuheService;

/**
 * @author ms
 * @date 2018年7月12日
 */
@RestController
@RequestMapping("/api")
public class ApiController {

	private Logger logger = LoggerFactory.getLogger(ApiController.class);

	@Autowired
	private RedisService redisservice;

	@RequestMapping("json")
	public R json(String code) {
		//logger.info("/api/json,请求接口入参:code={}", code);
		try {
			if (StringUtils.isNoneBlank(code)) {
				String data = redisservice.get(code);
				if (StringUtils.isNoneBlank(data)) {
					JSONObject obj = JSONObject.parseObject(data, Feature.OrderedField);
					// 如果是开奖和走势图的时候，加上type
					if (code.contains("_zst") || code.contains("_zxkj")) {
						obj.put("type", code.split("_")[0]);
					}
					return R.okwithdata(obj);
				} else
					return R.okwithdata(new JSONObject());
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
	public R jsonarray(String code, String date) {
		//logger.info("/api/jsonarray,请求接口入参:code={},date={}", code, date);
		try {
			if (StringUtils.isNotBlank(code)) {
				Map<Object, Object> map = null;
				JSONArray array = new JSONArray();
				if (code.contains("kjjl")) {// 如果是开奖记录的话，走开将记录的查询
					if (StringUtils.isBlank(date)) {
						date = null;
					}
					map = redisservice.getHashMapsByDate(code, date);
				} else {
					map = redisservice.getHashMapsByLimit(code);
				}
				if (map != null) {
					for (Map.Entry<Object, Object> entry : map.entrySet()) {
						JSONObject obj2 = new JSONObject();
						String key = entry.getKey() + "";
						obj2.put(key, JSON.parseObject(entry.getValue() == null ? "" : entry.getValue() + ""));

						array.add(obj2);
					}
				}
				return R.okwithdata(array);
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
		//logger.info("/api/cltj,请求接口入参:lotteryid={}", lotteryid);
		try {
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
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionStackTrace(e));
			return R.error("请求异常");
		}
	}

	@Autowired
	private LiuheService liuheService;

	/**
	 * 开奖记录
	 * 
	 * @desc
	 * @author xg
	 * @param code
	 * @return
	 * @author 2018年9月12日
	 */
	@RequestMapping("/liuhe/kjjl")
	public Map<String, Object> kjjl(String type, String year) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotBlank(year) && year.length() < 4) {
				map.put("message", "年份格式有误");
				map.put("errorCode", 1);
				return map;
			}
			if (StringUtils.isBlank(year)) {
				Calendar cal = Calendar.getInstance();
				year = String.valueOf(cal.get(Calendar.YEAR));
			}
			if (StringUtils.isBlank(type)) {
				type = "1";
			}
			if (Integer.valueOf(type) > 8 || Integer.valueOf(type) < 1) {
				map.put("message", "类型有误");
				map.put("errorCode", 1);
				return map;
			}
			map.put("message", "操作成功");
			map.put("errorCode", 0);

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("businessCode", 0);
			result.put("message", "操作成功");

			Map<String, Object> data = new HashMap<String, Object>();
			result.put("data", data);
			Map<String, Object> title = new HashMap<String, Object>();
			title.put("maxMissingValues", "");
			title.put("appearCount", "");
			data.put("title", title);

			Object bodyList = liuheService.kjjl(type, year);
			data.put("bodyList", bodyList);

			map.put("result", result);

		} catch (Exception e) {
			map.put("message", "请求出错");
			map.put("errorCode", 1);
			logger.error("请求开奖记录出错:{}", e);
		}
		return map;
	}

}
