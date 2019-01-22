package io.lottery.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.lottery.common.config.LotteryDict;
import io.lottery.common.config.RedisService;
import io.lottery.common.utils.DateUtils;

/**
 * @desc 彩种开奖直播接口
 * @author xg
 * @author 2018年7月20日
 */
@RestController
public class VideoController {

	@Autowired
	private RedisService redisservice;
	//新一期标记  如果分布式存到redis里去
	public static Map<String,Object> updateflag =new HashMap<String, Object>();
	
	/**
	 * @desc 北京赛车开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/pk10/ajax")
	public String pk10(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "bjsc");
		if (json == null) {
			return null;
		}
		return setInterval("bjsc", json.toString());
	}

	/**
	 * @desc 重庆时时彩开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/shishicai/ajax")
	public String shishicai(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "cqssc");
		if (json == null) {
			return null;
		}
		return setInterval("cqssc", json.toString());
	}

	/**
	 * @desc 广东快乐十分开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/gdkl10/ajax")
	public String gdkl10(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "gdklsf");
		if (json == null) {
			return null;
		}
		return setInterval("gdklsf", json.toString());
	}

	/**
	 * @desc 幸运飞艇开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/xyft/ajax")
	public String xyft(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "xyft");
		if (json == null) {
			return null;
		}
		return setInterval("xyft", json.toString());
	}

	/**
	 * @desc 幸运农场开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/xync/ajax")
	public String xync(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "xync");
		if (json == null) {
			return null;
		}
		return setInterval("xync", json.toString());
	}

	/**
	 * @desc 圣地彩开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/sdc/ajax")
	public String sdc(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "sdc");
		if (json == null) {
			return null;
		}
		return setInterval("sdc", json.toString());
	}

	/**
	 * @desc 广东11选5开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/gd11x5/ajax")
	public String gd11x5(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "gd11x5");
		if (json == null) {
			return null;
		}
		return setInterval("gd11x5", json.toString());
	}

	/**
	 * @desc 快乐8开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/kl8/ajax")
	public String kl8(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "kl8");
		if (json == null) {
			return null;
		}
		return setInterval("kl8", json.toString());
	}

	/**
	 * @desc pc蛋蛋开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/pcdd/ajax")
	public String pcdd(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "pcdd");
		if (json == null) {
			return null;
		}
		return setInterval("pcdd", json.toString());
	}

	/**
	 * @desc 新疆时时彩开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/xjssc/ajax")
	public String xjssc(String ajaxhandler, String ajaxHandler) {
		if (StringUtils.isNotBlank(ajaxHandler) && StringUtils.isBlank(ajaxhandler)) {
			ajaxhandler = ajaxHandler;
		}
		if (StringUtils.isNotBlank(ajaxhandler)) {
			Object json = null;
			if (ajaxhandler.equals("GetAwardData")) {
				json = redisservice.getHashMap(LotteryDict.video, "xjssc_a");
				json = setInterval("xjssc", json.toString());
				
			} else if (ajaxhandler.equals("GetDrawData")) {
				json = redisservice.getHashMap(LotteryDict.video, "xjssc");
				json = setInterval("xjssc", json.toString());
			}
			if (json == null) {
				return null;
			}
			return json.toString();
		} else {
			return null;
		}
	}

	/**
	 * @desc 天津时时彩开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/tjssc/ajax")
	public String tjssc(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "tjssc");
		if (json == null) {
			return null;
		}
		return setInterval("tjssc", json.toString());
	}


	/**
	 * @desc 广东11选5开奖直播接口
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/jx11x5/ajax")
	public String jx11x5(String ajaxHandler) {
		Object json = redisservice.getHashMap(LotteryDict.video, "jx11x5");
		if (json == null) {
			return null;
		}
		return setInterval("jx11x5", json.toString());
	}

	/**
	 * @desc 江苏快三开奖直播接口A
	 * @author xg
	 * @param ajaxHandler
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/jsk3/ajax")
	public String jsk3A(String ajaxHandler) {
		if ("GetDrawData".equals(ajaxHandler)) {
			Object json = null;
			if (ajaxHandler == null) {
				return null;
			}
			if (ajaxHandler.equals("GetDrawData")) {
				json = redisservice.getHashMap(LotteryDict.video, "jsk3_a");

			}
			if (json == null) {
				return null;
			}
			return setInterval("jsk3", json.toString());
		} else if ("Getjsk3AwardData".equals(ajaxHandler)) {
			Object json = redisservice.getHashMap(LotteryDict.video, "jsk3_b");
			if (json == null) {
				return null;
			}
			return setInterval("jsk3", json.toString());
		} else {
			Object json = null;
			if (ajaxHandler == null) {
				return null;
			}
			if (ajaxHandler.equals("GetJsk3AnalysisData")) {
				json = redisservice.getHashMap(LotteryDict.video, "jsk3_c");
			}
			if (json == null) {
				return null;
			}
			return json.toString();
		}

	}

	@RequestMapping("/static/wap/icpapi/GetMShiPinInfo")
	public String h5Bjsc(String ajaxHandler) {
		//北京赛车
		Object json = redisservice.getHashMap(LotteryDict.video,"bjsc_Mobile");
		String jsonA = setInterval("bjsc", json.toString());
		Map<String, Object> map = (Map) JSONObject.parse(jsonA);
		Map currentMap = (Map) map.get("current");
		Map nextMap = (Map) map.get("next");
		currentMap.put("isEnd", null);
		currentMap.put("nextMinuteInterval", null);
		nextMap.put("awardNumbers", null);
		nextMap.put("pan", null);
		nextMap.put("isEnd", null);
		nextMap.put("nextMinuteInterval", null);
		return JSONObject.toJSONString(map);
	}
	@RequestMapping("static/wap/pk10/ajax")
	public String h5BjscAns(String ajaxHandler) {
		//北京赛车
		Object json = redisservice.getHashMap(LotteryDict.video, "bjsc_MobileA");
		if (json == null) {
			return null;
		}
		return json.toString();	
	}
	
	@RequestMapping("/icpapi/GetMShiPinInfo")
	public String getMShiPinInfo(Integer lotteryid) {
		if(lotteryid==2) {
			//重庆时时彩
			Object json = redisservice.getHashMap(LotteryDict.video,"cqssc_Mobile");
			return setInterval("cqssc", json.toString());
		}else if(lotteryid==5) {
			//幸运农场
			Object json = redisservice.getHashMap(LotteryDict.video,"xync_Mobile");
			return setInterval("xync", json.toString());
		}else if(lotteryid==4) {
			//幸运飞艇
			Object json = redisservice.getHashMap(LotteryDict.video,"xyft_Mobile");
			return setInterval("xyft", json.toString());
		}
		return null;
	}

	/**
	 * @desc 直播时间接口
	 * @author xg
	 * @param
	 * @return
	 * @author 2018年7月20日
	 */
	@RequestMapping("/icpapi/GetLotteryTime")
	public String GetLotteryTime() {
		Object json = redisservice.getHashMap(LotteryDict.video, "GetLotteryTime");
		if (json == null) {
			return null;
		}
		// 设置间格时间(毫秒)

		Map<String, Object> map = (Map) JSONObject.parse(json.toString());
		String lottIdA = null;
		for (String lottId : map.keySet()) {
			if (lottId.equals("pk10")) {
				lottIdA = "bjsc";
			} else if (lottId.equals("shishicai")) {
				lottIdA = "cqssc";
			} else if (lottId.equals("gdkl10")) {
				lottIdA = "gdklsf";
			}else {
				lottIdA=lottId;
			}
			String str=redisservice.get(lottIdA+"_zxkj");
			JSONObject obj=JSONObject.parseObject(str);
			Long xqkjsj=obj.getLong("xqkjsj");
			long jgsj=xqkjsj-System.currentTimeMillis();
			jgsj=Math.abs(uniformTimeGap(lottId,jgsj));
			Map map2 = (Map) map.get(lottId);
			Map map3 = (Map) map2.get("Next");
			map3.put("Interval", jgsj);
			map3.put("AwardInterval", jgsj);
		}
		JSONObject resobj = new JSONObject(map);
		return resobj.toJSONString();
	}

	/**
	 * @desc 设置间格时间
	 * @author xg
	 * @param lottId
	 * @param json
	 * @return
	 * @author 2018年8月15日
	 */
	private String setInterval(String lottId, String json) {
 		long interval = 0L;
		if (lottId.equals("bjsc") || lottId.equals("xyft") || lottId.equals("xyft") || lottId.equals("pcdd") || lottId.equals("kl8")|| lottId.equals("pcdd")) {
			interval = 300000;
		} else if (lottId.equals("gdklsf") || lottId.equals("xync") || lottId.equals("sdc") || lottId.equals("gd11x5") || lottId.equals("jsk3") || lottId.equals("xjssc")
				|| lottId.equals("tjssc") || lottId.equals("jx11x5")) {
			interval = 600000; 
		}else if (lottId.equals("cqssc")) {
			int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if (h >= 10 && h <= 21) {
				interval = 300000;
			}else {
				interval = 600000;
			}
		}
		SimpleDateFormat formatter3 = new SimpleDateFormat("HHmm");
		int t = Integer.parseInt(formatter3.format(new Date()));
		if(lottId.equals("bjsc")||lottId.equals("sdc")||lottId.equals("pcdd")||lottId.equals("kl8")) {
			if(t>=2357||t<907) {
				interval=33000000;
			}
		}else if(lottId.equals("cqssc")){
			if(t>=155&&t<1000) {
				interval=29100000;
			}
		}else if(lottId.equals("xyft")){
			if(t>=404&&t<1309) {
				interval=32700000;
			}
		}else if(lottId.equals("xync")){
			if(t>=203&&t<1003) {
				interval=32400000;
			}
		}else if(lottId.equals("jsk3")){
			if(t>=2210||t<840) {
				interval=37800000;
			}
		}else if(lottId.equals("xjssc")){
			if(t>=200&&t<1010) {
				interval=29400000;
			}
		}else if(lottId.equals("tjssc")||lottId.equals("jx11x5")||lottId.equals("gd11x5")||lottId.equals("gdklsf")){
			if(t>=2300||t<910) {
				interval=36600000;
			}
		}
		Map<String, Object> map = (Map) JSONObject.parse(json.toString());
		String str=redisservice.get(lottId+"_zxkj");
		JSONObject obj=JSONObject.parseObject(str);
		Long xqkjsj=obj.getLong("xqkjsj");
		long jgsj=xqkjsj-System.currentTimeMillis();
		jgsj=uniformTimeGap(lottId,jgsj);
		Map map2;
		if(json.contains("next")) {
			map2 = (Map) map.get("next");
 			map2.put("awardTime", DateUtils.stampToDate(String.valueOf(xqkjsj)));
			//计算间格时间正负
			if(lottId.equals("bjsc")||lottId.equals("xyft")||lottId.equals("xync")||lottId.equals("cqssc")) {
				if (interval - jgsj < 90000) {
					Object flag = updateflag.get(lottId);
					if(flag==null) {
						updateflag.put(lottId ,map2.get("periodNumber"));
						updateflag.put(lottId+"S" ,System.currentTimeMillis()-50000);
						flag=map2.get("periodNumber");
					}
					if(flag.toString().equals(map2.get("periodNumber").toString())) {
						map2.put("awardTimeInterval", -jgsj);
						if(System.currentTimeMillis()-Long.parseLong(updateflag.get(lottId+"S").toString())<90000) {
							map2.put("awardTimeInterval", jgsj);
						}
					}else {
						map2.put("awardTimeInterval", jgsj);
						updateflag.put(lottId ,map2.get("periodNumber"));
						updateflag.put(lottId+"S",System.currentTimeMillis());
					}
				} else {
					map2.put("awardTimeInterval", jgsj);
				}
			}else {
				map2.put("awardTimeInterval", jgsj);
			}
		}else {
			map2 = (Map) map.get("Next");
			//计算间格正负
			if (interval - jgsj < 90000) {
				Object flag = updateflag.get(lottId);
				if(flag==null) {
					updateflag.put(lottId ,map2.get("PrePeriod"));
					updateflag.put(lottId+"S" ,System.currentTimeMillis()-50000);
					flag=map2.get("PrePeriod");
				}
				if(flag.toString().equals(map2.get("PrePeriod").toString())) {
					map2.put("Interval", -jgsj);
					if(System.currentTimeMillis()-Long.parseLong(updateflag.get(lottId+"S").toString())<90000) {
						map2.put("Interval", jgsj);
					}
				}else {
					map2.put("Interval", jgsj);
					updateflag.put(lottId ,map2.get("PrePeriod"));
					updateflag.put(lottId+"S",System.currentTimeMillis());
				}
			} else {
				map2.put("Interval", jgsj);
			}
			map2.put("AwardInterval", jgsj);
			map2.put("DrawTime", DateUtils.stampToDate(String.valueOf(xqkjsj)));
		}
		return JSONObject.toJSONString(map);
	}
	
	/**
	 * @desc 统一时间差距
	 * @author xg
	 * @param lottId
	 * @param json
	 * @return
	 * @author 2018年8月15日
	 */
	private long uniformTimeGap(String lottId, long jgsj) {
		if(lottId.equals("bjsc")||lottId.equals("pk10")||lottId.equals("jx11x5")||lottId.equals("xyft")||lottId.equals("gd11x5")||lottId.equals("jsk3")) {
			jgsj=jgsj-30000;
		}else if(lottId.equals("shishicai")||lottId.equals("cqssc")||lottId.equals("xjssc")||lottId.equals("tjssc")) {
			jgsj=jgsj-25000;
		}else if(lottId.equals("gdklsf")||lottId.equals("gdkl10")) {
			jgsj=jgsj-90000;
		}else if(lottId.equals("xync")) {
			jgsj=jgsj-40000;
		}else if(lottId.equals("kl8")||lottId.equals("pcdd")) {
			jgsj=jgsj-15000;
		}
		return jgsj;
	}
	
	@RequestMapping("/chart/GetMarkSixNumbers")
	public String GetMarkSixNumbers() {
		return redisservice.getHashMap(LotteryDict.LHC+"_"+LotteryDict.video,"GetMarkSixNumbers").toString();
	}
	@RequestMapping("/marksix")
	public String marksix() {
		return redisservice.getHashMap(LotteryDict.LHC+"_"+LotteryDict.video,"marksix").toString();
	}

}
