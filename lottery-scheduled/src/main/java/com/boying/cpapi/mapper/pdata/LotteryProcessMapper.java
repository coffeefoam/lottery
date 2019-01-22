package com.boying.cpapi.mapper.pdata;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * 
 * @desc lottery mapper类
 * @author abo
 * @date 2018年6月23日 下午4:04:57
 *
 */
public interface LotteryProcessMapper {

	/**
	 * 
	 * @desc 按照表名的字段进行查询
	 * @author abo
	 * @date 2018年6月27日 下午4:48:54
	 * @param tablename
	 * @param numstr
	 * @param limit
	 * @return
	 */
	@SelectProvider(type = LotteryProcessSqlFactory.class, method = "selectByLottery")
	List<Map<String, Object>> findLotterysbyLottId(@Param("tablename") String tablename, @Param("numstr") String numstr, @Param("limit") int limit);

	/**
	 * @desc 按照表名的字段进行查询-查询今天数据
	 * @author xg
	 * @param tablename
	 * @param numstr
	 * @return
	 * @author 2018年6月30日
	 */
	@SelectProvider(type = LotteryProcessSqlFactory.class, method = "selectLotteryByToday")
	List<Map<String, Object>> findLotteryByToday(@Param("tablename") String tablename, @Param("numstr") String numstr, @Param("nowTime") String nowTime);

	/**
	 * 
	 * @desc 根据传入的时间进行查询
	 * @author abo
	 * @date 2018年6月27日 下午5:05:09
	 * @param tablename
	 * @param numstr
	 * @param date
	 * @return
	 */
	@SelectProvider(type = LotteryProcessSqlFactory.class, method = "selectLotteryByDate")
	List<Map<String, Object>> findLotteryByDate(@Param("tablename") String tablename, @Param("numstr") String numstr, @Param("date") String date);

	/**
	 * @desc 根据表名查询今天的期数
	 * @author xg
	 * @param tablename
	 * @return
	 * @author 2018年6月30日
	 */
	@SelectProvider(type = LotteryProcessSqlFactory.class, method = "selectMinAndMaxPeriodsByToday")
	Map<String, Object> findLotteryMinAndMaxPeriodsByToday(@Param("tablename") String tablename, @Param("nowTime") String nowTime);

	/**
	 * @desc 根据表名查询今天的期数
	 * @author xg
	 * @param tablename
	 * @param numstr
	 * @param beginPeriods
	 * @param limit
	 * @return
	 * @author 2018年6月30日
	 */
	@SelectProvider(type = LotteryProcessSqlFactory.class, method = "selectLotteryByPeriodsAndLimit")
	List<Map<String, Object>> findLotteryByPeriodsAndLimit(@Param("tablename") String tablename, @Param("numstr") String numstr, @Param("beginPeriods") String beginPeriods,
			@Param("limit") String limit, @Param("nowTime") String nowTime);

	/**
	 * 
	 * @desc 查询大于传入日期的记录
	 * @author
	 * @param tablename
	 * @param numstr
	 * @param date
	 * @return
	 */
	@SelectProvider(type = LotteryProcessSqlFactory.class, method = "selectLotteryAfterDate")
	List<Map<String, Object>> selectLotteryAfterDate(@Param("tablename") String tablename, @Param("numstr") String numstr, @Param("date") String date);

	/**
	 * @desc 根据表名查询今天的区间期数
	 * @author xg
	 * @param tablename
	 * @param numstr
	 * @param beginPeriods
	 * @param endPeriods
	 * @return
	 * @author 2018年7月11日
	 */
	@SelectProvider(type = LotteryProcessSqlFactory.class, method = "selectLotteryByPeriods")
	List<Map<String, Object>> findLotteryByPeriods(@Param("tablename") String tablename, @Param("numstr") String numstr, @Param("beginPeriods") String beginPeriods,
			@Param("endPeriods") String endPeriods, @Param("nowTime") String nowTime);

	/**
	 * @desc 查询开奖直播数据
	 * @author xg
	 * @param tablename
	 * @param numstr
	 * @return
	 * @author 2018年7月12日
	 */
	@SelectProvider(type = LotteryProcessSqlFactory.class, method = "selectLotteryLive")
	Map<String, Object> selectLotteryLive(@Param("tablename") String tablename, @Param("numstr") String numstr, @Param("nowTime") String nowTime);

	/**
	 * 查询赛车走势数据
	 * 
	 * @author anlin
	 * @param tablename
	 * @param numstr
	 * @return
	 * @author 2018年7月27日
	 */
	@SelectProvider(type = LotteryProcessSqlFactory.class, method = "selectBjTrend")
	List<Map<String, Object>> selectBjTrend(@Param("tablename") String tablename, @Param("numstr") String numstr, @Param("limitNum") String limitNum,
			@Param("nowTime") String nowTime);

	/**
	 * 
	 * @desc 按照指定条数返回最新的几十条期数
	 * @author abo
	 * @date 2018年8月10日 下午3:20:08
	 * @param tablename
	 * @param limitlistsize
	 * @return
	 */
	@Select(" select periods from ${tablename} order by periods desc limit ${limitlistsize} ")
	List<String> findPeriodsByOrder(@Param("tablename") String tablename, @Param("limitlistsize") int limitlistsize);

	/**
	 * 
	 * @desc
	 * @author abo
	 * @date 2018年8月10日 下午3:38:40
	 * @param tablename
	 * @param limitlistsize
	 * @param date
	 * @return
	 */
	@Select(" select periods from ${tablename} where time = '${date}' order by periods desc ")
	List<String> findPeriodsByDay(@Param("tablename") String tablename, @Param("date") String date);
	
	/**
	 * 根据期数查询开奖时间
	* @author ms
	* @date 2018年9月4日 下午1:50:07
	 */
	@Select("SELECT openingTime FROM lottery_time_config WHERE lotteryId=${lotteryid} and issue =${issue} ORDER BY openingTime asc LIMIT 1")
	String getNextOpeningTime(@Param("lotteryid") int lotteryid,@Param("issue") int issue);

	@Select("SELECT	DATE_FORMAT(l.time, '%Y') nianfen FROM t_bs_liuhe l GROUP BY DATE_FORMAT(l.time, '%Y')")
	List<Integer> getLhcYearsData();
	
}