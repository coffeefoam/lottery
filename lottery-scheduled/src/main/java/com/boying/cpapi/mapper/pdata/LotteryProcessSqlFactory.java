package com.boying.cpapi.mapper.pdata;

import java.util.Map;

/**
 * 
 * @desc 彩票抽取组装factory类
 * @author abo
 * @date 2018年6月23日 下午4:01:35
 *
 */
/**
 * @desc
 * @author xg
 * @author 2018年6月27日
 */
public class LotteryProcessSqlFactory {

	/**
	 * 
	 * @desc 根据内容重新组装sql
	 * @author abo
	 * @date 2018年6月23日 下午4:01:53
	 *
	 */
	public String selectByLottery(Map<String, Object> lotteryMap) {
		String numstr = lotteryMap.get("numstr").toString();// 表名
		String tablename = lotteryMap.get("tablename").toString();// 插入的列名，列名是动态的，每个彩票不一样
		String limit = lotteryMap.get("limit").toString();
		StringBuilder sb = new StringBuilder();
		// 重新组装插入sql
		sb.append("SELECT periods,time,starttime," + numstr + " from " + tablename + " order by starttime desc limit " + limit);
		// System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * @desc 根据内容重新组装sql-查询今天的数据
	 * @author xg
	 * @author 2018年6月27日
	 */
	public String selectLotteryByToday(Map<String, Object> lotteryMap) {
		String numstr = lotteryMap.get("numstr").toString();// 表名
		String tablename = lotteryMap.get("tablename").toString();// 插入的列名，列名是动态的，每个彩票不一样
		String nowTime = lotteryMap.get("nowTime").toString(); //查询日期
		StringBuilder sb = new StringBuilder();
		// 重新组装插入sql
		sb.append("SELECT id,periods,time,starttime," + numstr + ",createtime from " + tablename + " where time = '"+nowTime+"' order by starttime desc ");
		return sb.toString();
	}

	/**
	 * 
	 * @desc 根据传入的时间进行查询
	 * @author abo
	 * @date 2018年6月27日 下午5:01:35
	 * @param lotteryMap
	 * @return
	 */
	public String selectLotteryByDate(Map<String, Object> lotteryMap) {
		String numstr = lotteryMap.get("numstr").toString();// 表名
		String tablename = lotteryMap.get("tablename").toString();// 插入的列名，列名是动态的，每个彩票不一样
		String date = lotteryMap.get("date").toString();// 日期，格式为：yyyy-MM-dd 例子：2018-06-22
		StringBuilder sb = new StringBuilder();
		// 重新组装插入sql
		sb.append("SELECT id,periods,time,starttime," + numstr + ",createtime from " + tablename + " where time = '" + date + "' order by starttime desc ");
		return sb.toString();
	}

	/**
	 * 
	 * @desc 查询大于传入日期的记录
	 * @date
	 * @param lotteryMap
	 * @return
	 */
	public String selectLotteryAfterDate(Map<String, Object> lotteryMap) {
		String numstr = lotteryMap.get("numstr").toString();// 表名
		String tablename = lotteryMap.get("tablename").toString();// 插入的列名，列名是动态的，每个彩票不一样
		String date = lotteryMap.get("date").toString();// 日期，格式为：yyyy-MM-dd 例子：2018-06-22
		StringBuilder sb = new StringBuilder();
		// 重新组装插入sql
		sb.append("SELECT id,periods,time,starttime," + numstr + ",createtime from " + tablename + " where time >= '" + date + "' order by starttime desc ");
		return sb.toString();
	}

	/**
	 * @desc 根据表名查询今天的最大最小期数
	 * @author xg
	 * @author 2018年6月27日
	 */
	public String selectMinAndMaxPeriodsByToday(Map<String, Object> lotteryMap) {
		String tablename = lotteryMap.get("tablename").toString();// 插入的列名，列名是动态的，每个彩票不一样
		String nowTime = lotteryMap.get("nowTime").toString(); //查询日期
		StringBuilder sb = new StringBuilder();
		// 重新组装插入sql
		sb.append("SELECT min(periods) min ,max(periods) max from " + tablename + " where time ='" + nowTime+"'");
		return sb.toString();
	}

	/**
	 * @desc 根据开始期数和条数查询数据
	 * @author xg
	 * @author 2018年6月27日
	 */
	public String selectLotteryByPeriodsAndLimit(Map<String, Object> lotteryMap) {
		String numstr = lotteryMap.get("numstr").toString();// 表名
		String tablename = lotteryMap.get("tablename").toString();// 插入的列名，列名是动态的，每个彩票不一样
		String beginPeriods = lotteryMap.get("beginPeriods").toString();// 插入的列名，列名是动态的，每个彩票不一样
		String limit = lotteryMap.get("limit").toString();// 插入的列名，列名是动态的，每个彩票不一样
		String nowTime = lotteryMap.get("nowTime").toString(); //查询日期
		StringBuilder sb = new StringBuilder();
		// 重新组装插入sql
		sb.append("SELECT id,periods,time,starttime," + numstr + ",createtime from " + tablename + " where periods >= " + beginPeriods
				+ " and time = '"+nowTime+"' ORDER BY periods LIMIT " + limit);
		return sb.toString();
	}

	/**
	 * @desc 根据开始期数和条数查询数据
	 * @author xg
	 * @author 2018年6月27日
	 */
	public String selectLotteryByPeriods(Map<String, Object> lotteryMap) {
		String numstr = lotteryMap.get("numstr").toString();// 表名
		String tablename = lotteryMap.get("tablename").toString();// 插入的列名，列名是动态的，每个彩票不一样
		String beginPeriods = lotteryMap.get("beginPeriods").toString();// 开始期数
		String endPeriods = lotteryMap.get("endPeriods").toString();// 结束期数
		String nowTime = lotteryMap.get("nowTime").toString(); //查询日期
		StringBuilder sb = new StringBuilder();
		// 重新组装插入sql
		sb.append("SELECT id,periods,time,starttime," + numstr + ",createtime from " + tablename + " where periods between " + beginPeriods + " and " + endPeriods
				+ " and time ='"+nowTime+"' ORDER BY periods");
		return sb.toString();
	}

	/**
	 * @desc 查询开奖直播数据
	 * @author xg
	 * @author 2018年6月27日
	 */
	public String selectLotteryLive(Map<String, Object> lotteryMap) {
		String numstr = lotteryMap.get("numstr").toString();// 表名
		String tablename = lotteryMap.get("tablename").toString();// 插入的列名，列名是动态的，每个彩票不一样
		StringBuilder sb = new StringBuilder();
		String nowTime = lotteryMap.get("nowTime").toString(); //查询日期
		// 重新组装插入sql
		sb.append("SELECT periods, time, starttime, " + numstr + ", (SELECT periods from " + tablename + " where time = '"+nowTime+"' ORDER BY"
				+ " starttime LIMIT 1) as firstPeriods FROM " + tablename + " WHERE time = '"+nowTime+"' ORDER BY starttime DESC limit 1");
		return sb.toString();
	}

	/**
	 * 查询赛车走势数据
	 * 
	 * @author anlin
	 * @return
	 */
	public String selectBjTrend(Map<String, Object> lotteryMap) {
		// 获取期数
		String numstr = lotteryMap.get("numstr").toString();
		// 获取表名
		String tablename = lotteryMap.get("tablename").toString();
		String limitNum = lotteryMap.get("limitNum").toString();
		String nowTime = lotteryMap.get("nowTime").toString(); //查询日期
		StringBuilder sb = new StringBuilder();
		if (limitNum.equals("all")) {
			sb.append("SELECT periods,time,starttime ," + numstr + ",createtime from " + tablename + " WHERE time = '"+nowTime+"' ORDER BY periods DESC");
		} else if (limitNum.equals("10dayall")) {
			sb.append("SELECT periods,time,starttime," + numstr + ",createtime from " + tablename + " WHERE time >= date_sub(now(), interval 10 day) ORDER BY periods DESC");
		} else if (limitNum.equals("currenDay")) {
			sb.append("SELECT periods,time,starttime," + numstr + ",createtime from " + tablename + " WHERE time = (select time from " + tablename
					+ " order by periods desc  limit 1) ORDER BY periods DESC");
		} else {
			sb.append("SELECT periods,time,starttime ," + numstr + ",createtime from " + tablename + " ORDER BY periods DESC limit " + limitNum);
		}
		// 重新组装插入sql
		return sb.toString();
	}
}
