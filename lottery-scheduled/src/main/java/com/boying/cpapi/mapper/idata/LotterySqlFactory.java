package com.boying.cpapi.mapper.idata;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * 
 * @desc 彩票抽取组装factory类
 * @author abo
 * @date 2018年6月23日 下午4:01:35
 *
 */
public class LotterySqlFactory {

	/**
	 * 
	 * @desc 根据内容重新组装sql
	 * @author abo
	 * @date 2018年6月23日 下午4:01:53
	 *
	 */
	public String insertLottery(Map<String, Object> lotteryMap) {
		String tableName = lotteryMap.get("tableName").toString();// 表名
		String insertNum = lotteryMap.get("insertNum").toString();// 插入的列名，列名是动态的，每个彩票不一样
		String insertNumValue = lotteryMap.get("insertNumValue").toString();// 插入的列内容，是动态的，每个彩票球数不一样
		String periods = lotteryMap.get("periods").toString();// 期数
		String time = lotteryMap.get("time").toString();// 当前日期
		String starttime = lotteryMap.get("starttime").toString();// 开奖日期+时间
		String createtime = lotteryMap.get("createtime").toString();// 创建时间
		StringBuilder sb = new StringBuilder();
		// 重新组装插入sql
		sb.append(" INSERT IGNORE  INTO " + tableName + " (periods,time,starttime," + insertNum + ",createtime)");
		sb.append(" VALUES('" + periods + "','" + time + "','" + starttime + "'," + insertNumValue + ",'" + createtime
				+ "')");
		return sb.toString();
	}

	/**
	 * 往彩票表里批量插入数据
	 * 
	 * @param map
	 * @return
	 */
	public String batchInsertByLottery(Map map) {
		// System.out.println(map);
		List<Map<String, Object>> listmap = (List<Map<String, Object>>) map.get("list");
		String tableName = map.get("tableName").toString();
		String insertNum = map.get("insertNum").toString();
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT IGNORE  INTO " + tableName + " (periods,time,starttime," + insertNum + ",createtime) VALUES");
		// 拼装格式化模版代码
		String mfStr = "(#'{'list[{0,number,#}].periods},#'{'list[{0,number,#}].time}, #'{'list[{0,number,#}].starttime}, ";
		String[] array = insertNum.split(",");
		for (int i = 0; i < array.length; i++) {
			mfStr += " #'{'list[{0,number,#}].num" + (i + 1) + "},";
		}
		mfStr += " #'{'list[{0,number,#}].createtime})";
		MessageFormat mf = new MessageFormat(mfStr);
		for (int i = 0; i < listmap.size(); i++) {
			sb.append(mf.format(new Object[] { i }));
			if (i < listmap.size() - 1) {
				sb.append(",");
			}
		}
		// System.out.println(sb.toString());
		return sb.toString();
	}
}
