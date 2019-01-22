package com.boying.cpapi.service.pdata;

import io.lottery.common.config.LotteryDict;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.Yilou.X11x5YilouHelp;

/**
 * 
 * @desc 11x5公共service
 * @author abo
 * @date 2018年7月2日 下午7:56:31
 *
 */
@Slf4j
@Service
public class Lottery11x5Service {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;

	/**
	 * @desc 11选5业务
	 * @author ms
	 * @param lottId
	 */
	public void execute(String lottId) {
		// 11x5开奖记录
		this.getKjjl(lottId, null);
		// 11选5 遗漏数据
		this.get11X5Yilou(lottId);
	}

	/**
	 * 
	 * @desc 11x5开奖记录（包含广东11选5，江西11选5）
	 * @author abo
	 * @param date
	 * @date 2018年6月27日 下午3:45:00
	 */
	public void getKjjl(String lottId, String date) {
		/* for (String lottId : LotteryConstant.tableNameMap.keySet()) { */
		// 广东11选5，江西11选5
		if (lottId.equals(LotteryConstant.GD11X5) || lottId.equals(LotteryConstant.JX11X5)) {
			// 表名
			String tablename = LotteryConstant.tableNameMap.get(lottId);
			// 查询球列的字符串
			String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			// 当前彩票的球数
			int num = LotteryConstant.lotteryNumMap.get(lottId);
			// 查询数据库数据
			// List<Map<String,Object>> list =
			// lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 1);

			// 查询数据库数据,添加了日期，如果有日期，则执行那一天的数据生成
			List<Map<String, Object>> list = null;
			if (StringUtils.isBlank(date)) {
				list = lotteryProcessMapper.findLotterysbyLottId(tablename, numStr, 1);
			} else {
				list = lotteryProcessMapper.findLotteryByDate(tablename, numStr, date);
			}

			for (Map<String, Object> m : list) {
				int num1 = Integer.parseInt(m.get("num1").toString());
				int num2 = Integer.parseInt(m.get("num2").toString());
				int num3 = Integer.parseInt(m.get("num3").toString());
				int num4 = Integer.parseInt(m.get("num4").toString());
				int num5 = Integer.parseInt(m.get("num5").toString());
				// 总和
				int sum = num1 + num2 + num3 + num4 + num5;
				// 总和大小
				String zhDx = LotteryConstant.HE;
				if (sum > 30) {
					zhDx = LotteryConstant.DA;
				} else if (sum < 30) {
					zhDx = LotteryConstant.XIAO;
				}
				// 总和单双
				String zhDs = sum % 2 == 0 ? LotteryConstant.SHUANG : LotteryConstant.DAN;
				// 龙虎
				String lh1 = "";
				if (num1 > num5) {
					lh1 = LotteryConstant.LONG;
				} else if (num1 < num5) {
					lh1 = LotteryConstant.HU;
				}
				int[] qs = { num1, num2, num3 };// 前三
				int[] zs = { num2, num3, num4 };// 中三
				int[] hs = { num3, num4, num5 };// 后三
				String qsStr = processIntArray(qs);
				String zsStr = processIntArray(zs);
				String hsStr = processIntArray(hs);

				Map<String, Object> resmap = new HashMap<String, Object>();
				List<Object> numberlist = new ArrayList<Object>();
				for (String position : numStr.split(",")) {
					numberlist.add(m.get(position));
				}
				resmap.put("rank", numberlist);
				resmap.put("lh1", lh1);
				resmap.put("gyhSum", sum);
				resmap.put("zhDx", zhDx);
				resmap.put("zhDs", zhDs);
				resmap.put("qs", qsStr);
				resmap.put("zs", zsStr);
				resmap.put("hs", hsStr);
				resmap.put("periods", m.get("periods"));
				resmap.put("starttime", m.get("starttime"));
				JSONObject resobj = new JSONObject(resmap);
				redisservice.setHashMap(lottId + "_" + LotteryDict.kjjl, resmap.get("periods"), resobj.toJSONString());
			}
			// }
		}
	}

	/**
	 * 
	 * @desc 处理前三、中三、后三类型的方法，数组三个一样的数字是包子，2个一样是对子，每个数字差1是顺子，什么都没有是杂六
	 * @author abo
	 * @date 2018年7月2日 下午6:48:36
	 * @param array
	 * @return
	 */
	private String processIntArray(int[] array) {
		String type = LotteryConstant.ZL;
		Arrays.sort(array);
		// 对子
		if (array[0] == array[1] || array[1] == array[2]) {
			type = LotteryConstant.DZ;
		} else if (array[0] == array[1] && array[0] == array[2]) {
			type = LotteryConstant.BZ;
		} else if (array[2] - 1 == array[1] && array[1] - 1 == array[0]) {
			type = LotteryConstant.SZ;
		} else if (array[2] - 1 == array[1] || array[1] - 1 == array[0]) {
			type = LotteryConstant.BS;
		}
		return type;
	}

	/**
	 * 广东11选5，江西11选5 遗漏数据
	 */
	public void get11X5Yilou(String lottId) {
		/* for (String lottId : LotteryConstant.tableNameMap.keySet()) { */
		// 广东11选5，江西11选5
		if (lottId.equals(LotteryConstant.GD11X5) || lottId.equals(LotteryConstant.JX11X5)) {
			// 今天
			Date td = new Date();
			String currentDate = DateUtil.date2String(td, DateUtil.PATTERN_DATE);
			// 本周第一天(星期一算第一天)
			Date wd = DateUtil.getWeekStartDate();
			// 本月第一天（1号）
			Date md = DateUtil.getFirstDayOfMonth();
			String monthfirstDate = DateUtil.date2String(md, DateUtil.PATTERN_DATE);

			String tablename = LotteryConstant.tableNameMap.get(lottId);
			String numStr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			List<Map<String, Object>> mlist = lotteryProcessMapper.selectLotteryAfterDate(tablename, numStr, monthfirstDate);// 本月数据
			List<Map<String, Object>> wlist = new ArrayList<Map<String, Object>>();// 定义本周数据
			List<Map<String, Object>> clist = new ArrayList<Map<String, Object>>();// 定义当天数据
			for (Map<String, Object> m : mlist) {
				if (((Date) m.get("starttime")).after(wd)) {
					wlist.add(m);
				}
				if (currentDate.equals(m.get("time").toString())) {
					clist.add(m);
				}
			}
			// 11选5所有位置遗漏数据
			JSONObject wzylobj = X11x5YilouHelp.x11x5wzyl(clist, wlist, mlist);
			// 11选5所有号码遗漏数据
			JSONObject hmylobj = X11x5YilouHelp.x11x5hmyl(clist, wlist, mlist);
			// 位置遗漏
			redisservice.set(lottId + "_" + LotteryDict.wzyl, wzylobj.toJSONString());
			// 号码遗漏
			redisservice.set(lottId + "_" + LotteryDict.hmyl, hmylobj.toJSONString());
		}
		// }
	}
}
